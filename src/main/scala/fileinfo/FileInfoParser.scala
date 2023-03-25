package me.veress.dlnamini
package fileinfo

import java.nio.file.attribute.BasicFileAttributes
import scala.util.matching.Regex
import sys.process.{Process, ProcessLogger}
import java.nio.file.{Files, Paths, attribute}
import java.text.SimpleDateFormat
import java.util.TimeZone

object FileInfoParser {
  private val VIDEO_INFO_PATTERN: Regex = """Duration: ([\d:.]+),.*bitrate: (\d+).*Stream.*Video.*, (\d+)x(\d+) \[""".r.unanchored

  def parseVideoInfo(filePath: String): Option[VideoInfo] = {
    val ffprobeOutput = getFfprobeInfo(filePath)
    ffprobeOutput match {
      case VIDEO_INFO_PATTERN(duration, bitrate, width, height) =>
        Some(
          VideoInfo(
            filePath,
            getFileName(filePath),
            getDurationMs(duration),
            bitrate.toLong,
            width.toInt,
            height.toInt,
            getCreationTime(filePath)
          )
        )
      case _ => None
    }
  }

  private def getFfprobeInfo(filePath: String): String = {
    val processOutput = getProcessOutput(s"ffprobe -hide_banner \"$filePath\"")
    processOutput._2.toString
  }

  private def getFileName(filePath: String): String = {
    Paths.get(filePath).getFileName.toString
  }

  private def getCreationTime(filePath: String): String = {
    Files.readAttributes(Paths.get(filePath), classOf[BasicFileAttributes]).creationTime.toString
  }

  private def getDurationMs(durationString: String): Long = {
    val durationFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    durationFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    durationFormat.parse("1970-01-01 " + durationString).getTime
  }

  private def getProcessOutput(command: String) = {
    val out = new StringBuilder
    val err = new StringBuilder
    val status = Process(command) ! ProcessLogger(out append _, err append _)
    (out, err, status)
  }
}
