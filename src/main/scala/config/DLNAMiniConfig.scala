package me.veress.dlnamini
package config

import com.typesafe.config.{Config, ConfigFactory}

object DLNAMiniConfig {
  private val config: Config = ConfigFactory.load("application.conf")

  def getString(configName: String): String = {
    config.getString(configName)
  }
}
