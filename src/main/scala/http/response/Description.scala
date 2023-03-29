package me.veress.dlnamini
package http.response

import config.DLNAMiniConfig

object Description {
  def xml() =
    <root xmlns:dlna="urn:schemas-dlna-org:device-1-0" xmlns="urn:schemas-upnp-org:device-1-0">
      <specVersion>
        <major>1</major>
        <minor>0</minor>
      </specVersion>
      <URLBase>http://{DLNAMiniConfig.httpIp}:{DLNAMiniConfig.httpPort}/</URLBase>
      <device>
        <dlna:X_DLNADOC xmlns:dlna="urn:schemas-dlna-org:device-1-0">DMS-1.50</dlna:X_DLNADOC>
        <dlna:X_DLNADOC xmlns:dlna="urn:schemas-dlna-org:device-1-0">M-DMS-1.50</dlna:X_DLNADOC>
        <deviceType>urn:schemas-upnp-org:device:MediaServer:1</deviceType>
        <friendlyName>{DLNAMiniConfig.serverName}</friendlyName>
        <manufacturer>{DLNAMiniConfig.serverName}</manufacturer>
        <manufacturerURL>https://github.com/mrveress/dlnamini</manufacturerURL>
        <modelDescription>DLNA Server</modelDescription>
        <modelName>dlnamini</modelName>
        <modelNumber>01</modelNumber>
        <modelURL>https://github.com/mrveress/dlnamini</modelURL>
        <serialNumber/>
        <UPC/>
        <UDN>uuid:{DLNAMiniConfig.getUuid}</UDN>
        <iconList>
          <icon>
            <mimetype>image/png</mimetype>
            <width>512</width>
            <height>512</height>
            <depth>24</depth>
            <url>/dlna/images/icon_server.png</url>
          </icon>
        </iconList>
        <presentationURL>http://{DLNAMiniConfig.httpIp}:{DLNAMiniConfig.httpPort}/index.html</presentationURL>
        <serviceList>
          <service>
            <serviceType>urn:schemas-upnp-org:service:ContentDirectory:1</serviceType>
            <serviceId>urn:upnp-org:serviceId:ContentDirectory</serviceId>
            <SCPDURL>/dlna/device/contentdirectory</SCPDURL>
            <controlURL>/dlna/control/contentdirectory</controlURL>
            <eventSubURL>/dlna/event/contentdirectory</eventSubURL>
          </service>
          <service>
            <serviceType>urn:schemas-upnp-org:service:ConnectionManager:1</serviceType>
            <serviceId>urn:upnp-org:serviceId:ConnectionManager</serviceId>
            <SCPDURL>/dlna/device/connectionmanager</SCPDURL>
            <controlURL>/dlna/control/connectionmanager</controlURL>
            <eventSubURL>/dlna/event/connectionmanager</eventSubURL>
          </service>
        </serviceList>
      </device>
    </root>
}
