package me.veress.dlnamini
package multicast

import java.net.{InetAddress, InetSocketAddress, MulticastSocket, NetworkInterface}

object SocketFactory {
  def openSocket(ip: String, port: Int): MulticastSocket = {
    val multicastAddress = InetAddress.getByName(ip)
    val group = new InetSocketAddress(multicastAddress, port)
    val netIf = NetworkInterface.getByInetAddress(multicastAddress)
    val socket = new MulticastSocket(port)
    socket.joinGroup(group, netIf)
    socket
  }
}
