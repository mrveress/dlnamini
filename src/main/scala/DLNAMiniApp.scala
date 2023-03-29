package me.veress.dlnamini

import config.DLNAMiniConfig
import fileinfo.*
import http.HttpServer
import multicast.UpnpDiscoveryManager

import java.nio.file.{Files, Paths}
import java.util.concurrent.Executors
import scala.io.StdIn.readLine

//Common vars
var discovery: UpnpDiscoveryManager = null

//Main method
@main
def main(): Unit = {
  //println(config.DLNAMiniConfig.getString("app.dlna.port"))
  var filePath: String = inputPath()
  while (!Files.isRegularFile(Paths.get(filePath))) {
    println("Wrong file, please, try again.")
    filePath = inputPath()
  }
  println(s"File accepted: $filePath")
  FileInfoParser.parseVideoInfo(filePath) match {
    case Some(videoInfo) =>
      println(videoInfo)
      DLNAMiniConfig.videoInfo = videoInfo

      HttpServer.run()

      discovery = new UpnpDiscoveryManager

      discovery.announce(Executors.newScheduledThreadPool(1))
      discovery.startListening()

      shutdownHook()
    case _ => println("Incorrect video")
  }
}

def inputPath(): String = {
  print("Please, input full path to the movie: ")
  readLine()
}

def shutdownHook(): Unit = {
  Runtime.getRuntime.addShutdownHook(new Thread {
    override def run(): Unit = {
      try {
        discovery.byebye()
        HttpServer.stop()
        Thread.sleep(500)
      } catch
        case _: Exception =>
    }
  })
}
