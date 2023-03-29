package me.veress.dlnamini
package multicast

import config.DLNAMiniConfig

import java.net.{DatagramPacket, DatagramSocket, InetAddress, InetSocketAddress, MulticastSocket, NetworkInterface, SocketAddress}
import scala.jdk.CollectionConverters.EnumerationHasAsScala

object MulticastSender {
  def sendMessages(ip: String, port: Int, messages: Seq[String]): Unit = {
    val datagramSocket = new DatagramSocket
    val targetSocket = new InetSocketAddress(InetAddress.getByName(ip), port)
    for (message <- messages) {
      /*println(
        s"""
          |--------------------- SEND MESSAGE ----------------------
          |FROM:
          |${datagramSocket.getLocalAddress.getHostAddress}:${datagramSocket.getPort}
          |TO:
          |${targetSocket.getHostName}:${targetSocket.getPort}
          |MESSAGE:
          |$message
          |=========================================================
          |""".stripMargin
      )*/
      datagramSocket.send(new DatagramPacket(message.getBytes, message.length, targetSocket))
    }
    datagramSocket.close()
  }
}
