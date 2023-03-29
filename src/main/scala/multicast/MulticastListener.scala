package me.veress.dlnamini
package multicast

import config.*

import java.net.{DatagramPacket, InetAddress, InetSocketAddress, MulticastSocket, NetworkInterface, SocketTimeoutException}
import java.util.concurrent.{ExecutorService, Executors}
import scala.util.Random

object MulticastListener {
  private var executor: Option[ExecutorService] = createExecutor()

  def listen(callback: DatagramPacket => Unit): Unit = {
    val buffer = Array.ofDim[Byte](DLNAMiniConfig.dlnaListenerBufferSize)
    val packet = new DatagramPacket(buffer, buffer.length)

    val socket: MulticastSocket = new MulticastSocket(DLNAMiniConfig.dlnaPort)
    //socket.joinGroup(InetAddress.getByName(DLNAMiniConfig.dlnaIp))
    val mcastaddr = new InetSocketAddress(InetAddress.getByName(DLNAMiniConfig.dlnaIp), DLNAMiniConfig.dlnaPort)
    socket.joinGroup(mcastaddr, NetworkInterface.getByInetAddress(InetAddress.getByName(DLNAMiniConfig.dlnaIp)))
    socket.setTimeToLive(DLNAMiniConfig.dlnaListenerTimeToLive)
    socket.setReuseAddress(true)

    executor match {
      case Some(executor: ExecutorService) =>
        executor.execute(
          () => {
            while (!executor.isShutdown) {
              try {
                socket.receive(packet)
                callback(packet)
              } catch {
                case _: SocketTimeoutException => ()
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
