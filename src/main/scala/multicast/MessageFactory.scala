package me.veress.dlnamini
package multicast

import config.DLNAMiniConfig

object MessageFactory {
  private val LF = "\r\n"

  private val HEADER = s"NOTIFY & HTTP/1.1$LF"
  private val CACHE_CONTROL = s"CACHE-CONTROL: max-age=1800$LF"
  private val DESCRIPTION_RESOURCE = "/dlna/device/description"
  private val ALIVE = s"NTS: ssdp:alive$LF"
  private val SERVER = s"SERVER: DLNA, UPnP/1.0, DLNA1.0$LF$LF"
  private val BYE_BYE = s"NTS: ssdp:byebye$LF"

  def rootDeviceMsg(): String =
    HEADER +
      createHost() +
      "NT: upnp:rootdevice" + LF +
      ALIVE +
      createLocation() +
      "USN: uuid:" + DLNAMiniConfig.getUuid + "::upnp:rootdevice" + LF +
      CACHE_CONTROL + SERVER

  def usnMsg(): String =
    HEADER +
      createHost() +
      "NT: uuid:" + DLNAMiniConfig.getUuid+ LF +
      ALIVE +
      createLocation() +
      createUsn("") +
      CACHE_CONTROL + SERVER

  def mediaServerMsg(): String =
    HEADER +
      createHost() +
      "NT: urn:schemas-upnp-org:device:MediaServer:1" + LF +
      ALIVE +
      createLocation() +
      createUsn("::urn:schemas-upnp-org:device:MediaServer:1") +
      CACHE_CONTROL + SERVER

  def contentDirectoryMsg(): String =
    HEADER +
      createHost() +
      "NT: urn:schemas-upnp-org:service:ContentDirectory:1" + LF +
      ALIVE +
      createLocation() +
      createUsn("::urn:schemas-upnp-org:service:ContentDirectory:1") +
      CACHE_CONTROL + SERVER

  def connectionManagerMsg(): String =
    HEADER +
      createHost() +
      "NT: urn:schemas-upnp-org:service:ConnectionManager:1" + LF +
      ALIVE +
      createLocation() +
      createUsn("::urn:schemas-upnp-org:service:ConnectionManager:1") +
      CACHE_CONTROL + SERVER

  def byeRootDevice(): String =
    HEADER +
      createHost() +
      "NT: upnp:rootdevice" + LF +
      BYE_BYE +
      createUsn("::upnp:rootdevice") + LF

  def byeMediaServer(): String =
    HEADER +
      createHost() +
      "NT: urn:schemas-upnp-org:device:MediaServer:1" + LF +
      BYE_BYE +
      createUsn("::urn:schemas-upnp-org:device:MediaServer:1") + LF

  def byeContentDirectory(): String =
    HEADER +
      createHost() +
      "NT: urn:schemas-upnp-org:service:ContentDirectory:1" + LF +
      BYE_BYE +
      createUsn("::urn:schemas-upnp-org:service:ContentDirectory:1") + LF

  def byeConnectionManager(): String =
    HEADER +
      createHost() +
      "NT: urn:schemas-upnp-org:service:ConnectionManager:1" + LF +
      BYE_BYE +
      createUsn("::urn:schemas-upnp-org:service:ConnectionManager:1") + LF

  private def createHost() = s"HOST: ${DLNAMiniConfig.dlnaIp}:${DLNAMiniConfig.dlnaPort}$LF"
  private def createLocation() = s"LOCATION: http://${DLNAMiniConfig.httpIp}:${DLNAMiniConfig.httpPort}$DESCRIPTION_RESOURCE$LF"
  private def createUsn(urn: String) = s"USN: uuid:${DLNAMiniConfig.getUuid}$urn$LF"
}
