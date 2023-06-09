package me.veress.dlnamini
package multicast

import config.DLNAMiniConfig

import java.net.DatagramPacket
import java.util.concurrent.{ScheduledExecutorService, ScheduledFuture, TimeUnit}
import scala.annotation.unused

class UpnpDiscoveryManager {
  private val SENDING_INTERVAL = 10
  private val M_SEARCH = "M-SEARCH"
  private val CONTENT_DIRECTORY = "urn:schemas-upnp-org:service:ContentDirectory:1"
  private val ROOT_DEVICE = "upnp:rootdevice"
  private val MEDIA_SERVER = "urn:schemas-upnp-org:device:MediaServer:"

  def announce(executor: ScheduledExecutorService): ScheduledFuture[_] = {
    val messages = List(
      MessageFactory.rootDeviceMsg(),
      MessageFactory.usnMsg(),
      MessageFactory.mediaServerMsg(),
      MessageFactory.contentDirectoryMsg(),
      MessageFactory.connectionManagerMsg()
    )

    val sendingTask: Runnable = () => {
      MulticastSender.sendMessages(DLNAMiniConfig.dlnaIp, DLNAMiniConfig.dlnaPort, messages)
    }
    executor.scheduleAtFixedRate(sendingTask, 0, SENDING_INTERVAL, TimeUnit.SECONDS)
  }

  def byebye(): Unit = {
    val messages = List(
      MessageFactory.byeRootDevice(),
      MessageFactory.byeMediaServer(),
      MessageFactory.byeContentDirectory(),
      MessageFactory.byeConnectionManager()
    )

    println("Sending byebye messages")

    MulticastSender.sendMessages(DLNAMiniConfig.dlnaIp, DLNAMiniConfig.dlnaPort, messages)
  }

  def startListening(): Unit = {
    MulticastListener.listen(discoveryMessageReceived)
  }

  @unused
  def stopListening(): Unit = {
    MulticastListener.stopExecutor()
  }

  private def discoveryMessageReceived(packet: DatagramPacket): Unit = {
    val message: String = new String(packet.getData)
    val remoteAddress = extractAddress(packet)
    val remotePort = extractPort(packet)

    /*println(
      s"""
         |----------------------RANDOM PACKET RECIEVED-----------------------
         |FROM: $remoteAddress:$remotePort
         |MESSAGE: ${new String(packet.getData, packet.getOffset, packet.getLength)}
         |============================================================
         |""".stripMargin
    )*/

    if (message.startsWith(M_SEARCH)) {

      /*println(
        s"""
           |----------------------M-SEARCH PACKET RECIEVED-----------------------
           |FROM: $remoteAddress:$remotePort
           |MESSAGE: ${new String(packet.getData, packet.getOffset, packet.getLength)}
           |============================================================
           |""".stripMargin
      )*/

      if (message.contains(CONTENT_DIRECTORY)) {
        MulticastSender.sendMessages(remoteAddress, remotePort, List(MessageFactory.contentDirectoryMsg()))
      } else if (message.contains(ROOT_DEVICE)) {
        MulticastSender.sendMessages(remoteAddress, remotePort, List(MessageFactory.rootDeviceMsg()))
      } else if (message.contains(MEDIA_SERVER)) {
        MulticastSender.sendMessages(remoteAddress, remotePort, List(MessageFactory.mediaServerMsg()))
      } else if (message.contains("ssdp:discover")) {
        MulticastSender.sendMessages(remoteAddress, remotePort, List(MessageFactory.rootDeviceMsg()))
      }
    }
  }

  private def extractAddress(packet: DatagramPacket): String = packet.getAddress.getHostAddress
  private def extractPort(packet: DatagramPacket): Int = packet.getPort
}
