package me.veress.dlnamini
package http.response

import config.DLNAMiniConfig

object ContentDirectoryRoot {
  def xml() = 
    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
      <s:Body>
        <u:BrowseResponse xmlns:u="urn:schemas-upnp-org:service:ContentDirectory:1">
          <Result>
            &lt;DIDL-Lite xmlns="urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:upnp="urn:schemas-upnp-org:metadata-1-0/upnp/"&gt;
            &lt;container id="0/0" childCount="1" parentID="0" restricted="true"&gt;
            &lt;dc:title&gt;Content&lt;/dc:title&gt;
            &lt;res protocolInfo="http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_TN"&gt;http://{DLNAMiniConfig.httpIp}:{DLNAMiniConfig.httpPort}/dlna/folder/image.png&lt;/res&gt;
            &lt;dc:date&gt;{DLNAMiniConfig.videoInfo.createdDate}&lt;/dc:date&gt;&lt;upnp:class&gt;object.container.storageFolder&lt;/upnp:class&gt;&lt;/container&gt;
            &lt;/DIDL-Lite&gt;
          </Result>
          <NumberReturned>1</NumberReturned>
          <TotalMatches>1</TotalMatches>
          <UpdateID>1</UpdateID>
        </u:BrowseResponse>
      </s:Body>
    </s:Envelope>
}
