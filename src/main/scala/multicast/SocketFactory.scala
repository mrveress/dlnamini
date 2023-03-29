package me.veress.dlnamini
package multicast

import java.net.{InetAddress, InetSocketAddress, MulticastSocket, NetworkInterface}
import scala.util.Random

object SocketFactory {
  def openSocket(ip: String, port: Int): MulticastSocket = {
    val multicastAddress = InetAddress.getByName(ip)
    val netIf = NetworkInterface.getByInetAddress(multicastAddress)

    var socket: MulticastSocket = null
    var tryPort = port

    while (socket == null) {
      try {
        socket = new MulticastSocket(tryPort)
        println(s"Socket $ip:$tryPort for sending UDP message opened successfully")
      } catch {
        case _: Throwable => {
          println(s"Failed to open socket $ip:$tryPort for sending UDP message, reroll")
          tryPort = Random.nextInt(65535)
        }
      }
    }

    socket.joinGroup(new InetSocketAddress(multicastAddress, tryPort), netIf)
    socket
  }
}
