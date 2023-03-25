package me.veress.dlnamini

import fileinfo._

import scala.io.StdIn.readLine
import java.nio.file.{Files, Paths}

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
    case Some(videoInfo) => println(videoInfo)
    case _ => println("Incorrect video")
  }
}

def inputPath(): String = {
  print("Please, input full path to the movie: ")
  readLine()
}
