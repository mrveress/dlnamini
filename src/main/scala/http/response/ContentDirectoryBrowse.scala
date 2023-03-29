package me.veress.dlnamini
package http.response

import config.DLNAMiniConfig

object ContentDirectoryBrowse {
  def xml() =
    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
      <s:Body>
        <u:BrowseResponse xmlns:u="urn:schemas-upnp-org:service:ContentDirectory:1">
          <Result>&lt;DIDL-Lite xmlns="urn:schemas-upnp-org:DLNAMiniConfig.videoInfo-1-0/DIDL-Lite/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:upnp="urn:schemas-upnp-org:DLNAMiniConfig.videoInfo-1-0/upnp/"&gt;
            &lt;item id="0/0/0" parentID="0/0" restricted="true"&gt;
            &lt;dc:title&gt;{DLNAMiniConfig.videoInfo.videoTitle}&lt;/dc:title&gt;
            &lt;res bitrate="{DLNAMiniConfig.videoInfo.bitrate}" duration="{DLNAMiniConfig.videoInfo.duration}" protocolInfo="http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_TS_SD_EU_ISO;DLNA.ORG_OP=10;DLNA.ORG_CI=1;DLNA.ORG_FLAGS=01500000000000000000000000000000" resolution="{DLNAMiniConfig.videoInfo.width}x{DLNAMiniConfig.videoInfo.height}"&gt;http://{DLNAMiniConfig.httpIp}:{DLNAMiniConfig.httpPort}/dlna/stream/1&lt;/res&gt;
            &lt;res bitrate="{DLNAMiniConfig.videoInfo.bitrate}" duration="{DLNAMiniConfig.videoInfo.duration}" protocolInfo="http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_TS_SD_NA_ISO;DLNA.ORG_OP=10;DLNA.ORG_CI=1;DLNA.ORG_FLAGS=01500000000000000000000000000000" resolution="{DLNAMiniConfig.videoInfo.width}x{DLNAMiniConfig.videoInfo.height}"&gt;http://{DLNAMiniConfig.httpIp}:{DLNAMiniConfig.httpPort}/dlna/stream/1&lt;/res&gt;
            &lt;res bitrate="{DLNAMiniConfig.videoInfo.bitrate}" duration="{DLNAMiniConfig.videoInfo.duration}" protocolInfo="http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_TS_SD_KO_ISO;DLNA.ORG_OP=10;DLNA.ORG_CI=1;DLNA.ORG_FLAGS=01500000000000000000000000000000" resolution="{DLNAMiniConfig.videoInfo.width}x{DLNAMiniConfig.videoInfo.height}"&gt;http://{DLNAMiniConfig.httpIp}:{DLNAMiniConfig.httpPort}/dlna/stream/1&lt;/res&gt;
            &lt;upnp:albumArtURI xmlns:dlna="urn:schemas-dlna-org:DLNAMiniConfig.videoInfo-1-0/" dlna:profileID="JPEG_TN"&gt;http://{DLNAMiniConfig.httpIp}:{DLNAMiniConfig.httpPort}/thumbnails/stream/1/image.png&lt;/upnp:albumArtURI&gt;
            &lt;res protocolInfo="http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_TN"&gt;http://{DLNAMiniConfig.httpIp}:{DLNAMiniConfig.httpPort}/thumbnails/stream/1/image.png&lt;/res&gt;
            &lt;dc:date&gt;{DLNAMiniConfig.videoInfo.createdDate}&lt;/dc:date&gt;
            &lt;upnp:class&gt;object.item.videoItem&lt;/upnp:class&gt;
            &lt;/item&gt;&lt;/DIDL-Lite&gt;
          </Result>
          <NumberReturned>1</NumberReturned>
          <TotalMatches>1</TotalMatches>
          <UpdateID>1</UpdateID>
        </u:BrowseResponse>
      </s:Body>
    </s:Envelope>
}
