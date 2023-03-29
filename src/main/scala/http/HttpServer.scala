package me.veress.dlnamini
package http

import com.sun.net.httpserver
import config.DLNAMiniConfig

import java.net.{InetAddress, InetSocketAddress}
import java.util.concurrent.Executors

object HttpServer {
  private var server: httpserver.HttpServer = _

  def run(): Unit = {
    server = httpserver.HttpServer.create(
      new InetSocketAddress(
        InetAddress.getByName(DLNAMiniConfig.httpIp),
        DLNAMiniConfig.httpPort
      ),
      0
    )
    server.createContext("/", me.veress.dlnamini.http.HttpServerHandler)
    server.setExecutor(Executors.newCachedThreadPool())
    server.start()

    println(s"HttpServer started on address ${DLNAMiniConfig.httpIp}:${DLNAMiniConfig.httpPort}")
  }

  def stop(): Unit = {
    if (server != null) {
      println("HttpServer was stopped")
      server.stop(0)
    }
  }
}
