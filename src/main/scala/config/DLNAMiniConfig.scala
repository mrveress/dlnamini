package me.veress.dlnamini
package config

import fileinfo.VideoInfo

import com.typesafe.config.{Config, ConfigFactory}

import java.net.{InetAddress, NetworkInterface}
import java.util.UUID.{nameUUIDFromBytes, randomUUID}

object DLNAMiniConfig {
  private val config: Config = ConfigFactory.load("application.conf")

  private def getString(configName: String): String = {
    config.getString(configName)
  }

  private def getInt(configName: String): Int = {
    config.getInt(configName)
  }

  def getUuid: String = {
    val netIf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost)
    var result: String = randomUUID().toString
    if (netIf != null) {
      val mac = netIf.getHardwareAddress
      if (mac != null) {
        result = nameUUIDFromBytes(mac).toString
      }
    }
    result
  }

  val serverName: String = getString("app.serverName")

  val ffmpegOpts: String = getString("app.ffmpegOpts")

  val httpIp: String = getString("app.http.ip")
  val httpPort: Int = getInt("app.http.port")

  val dlnaIp: String = getString("app.dlna.ip")
  val dlnaPort: Int = getInt("app.dlna.port")
  //val dlnaSenderTimeToLive: Int = getInt("app.dlna.senderTimeToLive")
  //val dlnaSenderSocketTimeout: Int = getInt("app.dlna.senderSocketTimeout")
  val dlnaListenerTimeToLive: Int = getInt("app.dlna.listenerTimeToLive")
  val dlnaListenerBufferSize: Int = getInt("app.dlna.listenerBufferSize")

  val imageServer: String = getString("app.image.server")
  val imageFolder: String = getString("app.image.folder")
  val imageVideo: String = getString("app.image.video")

  var videoInfo : VideoInfo = _
}
