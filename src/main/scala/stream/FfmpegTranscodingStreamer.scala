package me.veress.dlnamini
package stream

import org.apache.commons.exec.{ CommandLine, PumpStreamHandler, DefaultExecutor, ExecuteResultHandler, ExecuteException }
import java.io.{OutputStream, PipedInputStream, DataInputStream, PipedOutputStream}
import config.DLNAMiniConfig

import java.io.OutputStream

class FfmpegTranscodingStreamer {
  private val BUFSIZE: Int = 67108864 //Magic number, ugh
  private val FFMPEG_COMMAND: String = "ffmpeg"
  private val FFMPEG_OPTS: Array[String] = DLNAMiniConfig.ffmpegOpts.split(" ")

  def transcodeAndStream(outputStream: OutputStream) = {
    // see http://stackoverflow.com/questions/956323/capturing-large-amounts-of-output-from-apache-commons-exec
    val commandLine = new CommandLine(FFMPEG_COMMAND)
    val options = Array("-i", DLNAMiniConfig.videoInfo.filePath) ++ FFMPEG_OPTS
    options.foreach(s => commandLine.addArgument(s))

    // pipes for the executed process
    val stdout = new PipedOutputStream()
    val streamHandler = new PumpStreamHandler(stdout, System.err)

    val executor = new DefaultExecutor()
    try {
      val pipedInput = new DataInputStream(new PipedInputStream(stdout))
      executor.setStreamHandler(streamHandler)

      // start process asynchronously
      executor.execute(commandLine, new ExecuteResultHandler() {
        def onProcessComplete(c: Int) = {}
        def onProcessFailed(e: ExecuteException) = {}
      })

      val buffer = new Array[Byte](BUFSIZE)
      var len: Int = 0

      // stream data and flush it immediately
      while ( {
        len = pipedInput.read(buffer, 0, BUFSIZE); len != -1
      }) {
        outputStream.write(buffer, 0, len)
        outputStream.flush()
      }
      outputStream.close()
    } catch {
      case e => {
        executor.getWatchdog.destroyProcess()
      }
    }
  }
}
