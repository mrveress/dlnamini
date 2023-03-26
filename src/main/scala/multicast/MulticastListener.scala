package me.veress.dlnamini
package multicast

import config.*

import java.net.{DatagramPacket, InetAddress, InetSocketAddress, MulticastSocket, NetworkInterface, SocketTimeoutException}
import java.util.concurrent.{ExecutorService, Executors}

object MulticastListener {
  private var executor: Option[ExecutorService] = createExecutor()

  private def openSocket(): MulticastSocket = {
    val socket = SocketFactory.openSocket(DLNAMiniConfig.dlnaIp, DLNAMiniConfig.dlnaPort)
    socket.setTimeToLive(DLNAMiniConfig.dlnaListenerTimeToLive)
    socket.setReuseAddress(true)
    socket
  }

  def listen(callback: DatagramPacket => Unit): Unit = {
    val socket = openSocket()

    val buffer = Array.ofDim[Byte](DLNAMiniConfig.dlnaListenerBufferSize)
    val packet = new DatagramPacket(buffer, buffer.length)

    executor match {
      case Some(executor: ExecutorService) =>
        executor.execute(
          () => {
            while (!executor.isShutdown) {
              try {
                socket.receive(packet)
                println(
                  s"""
                     |----------------------PACKET RECIEVED-----------------------
                     |${new String(packet.getData, packet.getOffset, packet.getLength)}
                     |============================================================
                     |""".stripMargin
                )
                callback(packet)
              } catch {
                case e: SocketTimeoutException => ()
              }
            }
            socket.close()
          }
        )
      case _ => ()
    }
  }

  private def createExecutor(): Option[ExecutorService] = {
    executor match {
      case Some(_: ExecutorService) => stopExecutor()
      case _ => ()
    }
    executor = Some(Executors.newCachedThreadPool)
    executor
  }

  def stopExecutor(): Unit = {
    executor match {
      case Some(executor: ExecutorService) => executor.shutdown()
      case _ => ()
    }
    executor = None
  }
}
