package me.veress.dlnamini
package multicast

import config.DLNAMiniConfig

import java.net.{DatagramPacket, InetAddress, InetSocketAddress, MulticastSocket, NetworkInterface, SocketAddress}
import scala.jdk.CollectionConverters.EnumerationHasAsScala

object MulticastSender {
  private def openSocket(port: Int): MulticastSocket = {
    val socket = SocketFactory.openSocket(DLNAMiniConfig.dlnaIp, port)
    socket.setTimeToLive(DLNAMiniConfig.dlnaSenderTimeToLive)
    socket.setSoTimeout(DLNAMiniConfig.dlnaSenderSocketTimeout)
    socket.setReuseAddress(true)
    socket
  }

  def sendMessages(ip: String, port: Int, messages: Seq[String]): Unit = {
    val senderSocket = openSocket(port)
    val targetSocket = new InetSocketAddress(InetAddress.getByName(ip), port)
    for (message <- messages) {
      println(
        s"""
          |--------------------- SEND MESSAGE ----------------------
          |FROM:
          |${senderSocket.getNetworkInterface.getName}
          |${senderSocket.getLocalAddress}
          |${senderSocket.getPort}
          |TO:
          |${targetSocket.getHostString}
          |${targetSocket.getPort}
          |MESSAGE:
          |$message
          |=========================================================
          |""".stripMargin
      )
      senderSocket.send(new DatagramPacket(message.getBytes, message.length, targetSocket))
    }
    senderSocket.close()
  }
}
