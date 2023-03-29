package me.veress.dlnamini
package http

import com.sun.net.httpserver.{HttpExchange, HttpHandler}
import config.DLNAMiniConfig
import stream.FfmpegTranscodingStreamer

import me.veress.dlnamini.http.response.{ConnectionManager, ConnectionManagerPost, ContentDirectory, ContentDirectoryBrowse, ContentDirectoryRoot, ContentDirectoryCapabilities, Description}

import java.io.InputStream
import scala.collection.mutable.ListBuffer
import xml.{Elem, XML}

object HttpServerHandler extends HttpHandler {

  private def responseDeviceDescription(exchange: HttpExchange) = {
    val headers = exchange.getResponseHeaders
    headers.set("Content-Type","text/xml; charset=\"utf-8\"")
    headers.set("Cache-Control",  "no-cache")
    headers.set("Expires", "0")
    headers.set("Accept-Ranges", "bytes")
    headers.set("Connection", "keep-alive")

    val responseBody = exchange.getResponseBody
    val response = buildResponse(Description.xml())
    exchange.sendResponseHeaders(200, response.getBytes.length)
    responseBody.write(response.getBytes)
    responseBody.close()
  }

  private def responseContentDirectory(exchange: HttpExchange) = {
    val headers = exchange.getResponseHeaders
    headers.set("Content-Type", "text/xml; charset=\"utf-8\"")
    headers.set("Cache-Control", "no-cache")
    headers.set("Expires", "0")
    headers.set("Accept-Ranges", "bytes")
    headers.set("Connection", "keep-alive")

    val responseBody = exchange.getResponseBody
    val body = buildResponse(ContentDirectory.xml)
    exchange.sendResponseHeaders(200, body.getBytes.length)
    responseBody.write(body.getBytes)
    responseBody.close()
  }

  private def responseConnectionManager(exchange: HttpExchange) = {
    val headers = exchange.getResponseHeaders
    headers.set("Content-Type", "text/xml; charset=\"utf-8\"")
    headers.set("Cache-Control", "no-cache")
    headers.set("Expires", "0")
    headers.set("Accept-Ranges", "bytes")
    headers.set("Connection", "keep-alive")

    val responseBody = exchange.getResponseBody
    val body = buildResponse(ConnectionManager.xml)
    exchange.sendResponseHeaders(200, body.getBytes.length)
    responseBody.write(body.getBytes)
    responseBody.close()
  }

  private def responseStreamInfo(exchange: HttpExchange): Unit = {
    val headers = exchange.getResponseHeaders
    headers.set("Content-Type", "video/mpeg")
    headers.set("Cache-Control",  "no-cache")
    headers.set("contentFeatures.dlna.org", "DLNA.ORG_PN=MPEG_TS_SD_EU_ISO;DLNA.ORG_OP=10;DLNA.ORG_CI=1;DLNA.ORG_FLAGS=01500000000000000000000000000000")
    headers.set("transferMode.dlna.org", "Streaming")
    exchange.sendResponseHeaders(200, -1)
  }

  private def responseIcon(exchange: HttpExchange, imageUrl: String): Unit = {
    val headers = exchange.getResponseHeaders
    headers.set("Content-Type", "image/png")
    headers.set("Accept-Ranges", "bytes")

    val responseBody = exchange.getResponseBody

    val data: Array[Byte] = inputStreamToByteArray(getClass.getResourceAsStream(imageUrl))
    exchange.sendResponseHeaders(200, data.length)
    responseBody.write(data)
    responseBody.close()
  }

  private def responsePlayStream(exchange: HttpExchange) = {
    val headers = exchange.getResponseHeaders
    headers.set("TransferMode.DLNA.ORG", "Streaming")
    headers.set("Content-Type", "video/mpeg")
    headers.set("ContentFeatures.DLNA.ORG", "DLNA.ORG_PN=MPEG_TS_SD_EU_ISO;DLNA.ORG_OP=10;DLNA.ORG_CI=1;DLNA.ORG_FLAGS=01500000000000000000000000000000")
    headers.set("Cache-Control",  "no-cache")
    exchange.sendResponseHeaders(200, 0)

    val streamer = new FfmpegTranscodingStreamer
    streamer.transcodeAndStream(exchange.getResponseBody)
  }

  private def responsePostContentDirectory(exchange: HttpExchange) = {
    val headers = exchange.getResponseHeaders
    val responseBody = exchange.getResponseBody
    var responseBodyData: String = ""
    headers.set("Content-Type", "text/xml; charset=\"utf-8\"")

    // see http://libdlna.sourcearchive.com/documentation/0.2.3/dlna_8h-source.html
    val requestXml = XML.load(exchange.getRequestBody)
    // do we have a browse request?
    if ((requestXml \\ "BrowseFlag").nonEmpty) {
      // is this a browse directory request?
      if ("0/0".equalsIgnoreCase((requestXml \\ "ObjectID").text)) {
        responseBodyData = buildResponse(ContentDirectoryBrowse.xml())
        // otherwise it is treated as a browse root directory request
      } else {
        responseBodyData = buildResponse(ContentDirectoryRoot.xml())
      }
    } else {
      responseBodyData = buildResponse(ContentDirectoryCapabilities.xml)
    }

    exchange.sendResponseHeaders(200, responseBodyData.getBytes.length)
    responseBody.write(responseBodyData.getBytes)
    responseBody.close()
  }

  private def responsePostConnectionManager(exchange: HttpExchange) = {
    val headers = exchange.getResponseHeaders
    headers.set("Content-Type", "text/xml; charset=\"utf-8\"")

    val responseBody = exchange.getResponseBody
    val body = buildResponse(ConnectionManagerPost.xml)
    exchange.sendResponseHeaders(200, body.getBytes.length)
    responseBody.write(body.getBytes)
    responseBody.close()
  }

  override def handle(exchange: HttpExchange): Unit = {
    println(
      s"""
         |--------------------------HTTP REQUEST---------------------------
         |METHOD: ${exchange.getRequestMethod}
         |URI: ${exchange.getRequestURI.toString}
         |""".stripMargin)

    exchange.getRequestMethod match {
      case "GET" => exchange.getRequestURI.toString match {
        case "/dlna/device/description" => responseDeviceDescription(exchange)
        case "/dlna/device/contentdirectory" => responseContentDirectory(exchange)
        case "/dlna/device/connectionmanager" => responseConnectionManager(exchange)
        case "/dlna/stream/1" => responsePlayStream(exchange)
        case "/dlna/images/icon_server.png" => responseIcon(exchange, DLNAMiniConfig.imageServer)
        case "/dlna/folder/image.png" => responseIcon(exchange, DLNAMiniConfig.imageFolder)
        case "/thumbnails/stream/1/image.png" => responseIcon(exchange, DLNAMiniConfig.imageVideo)
        case _ => responseIcon(exchange, DLNAMiniConfig.imageServer)
      }
      case "POST" => exchange.getRequestURI.toString match {
        case "/dlna/control/contentdirectory" => responsePostContentDirectory(exchange)
        case "/dlna/control/connectionmanager" => responsePostConnectionManager(exchange)
      }
      case "HEAD" => responseStreamInfo(exchange)
    }
  }

  private def inputStreamToByteArray(is: InputStream): Array[Byte] = {
    val buffer = ListBuffer[Byte]()
    var byte = is.read()
    while (byte != -1) {
      buffer.append(byte.byteValue)
      byte = is.read()
    }
    buffer.toArray
  }

  private def buildResponse(elem: Elem): String =
    s"""<?xml version="1.0" encoding="utf-8"?>
       |${elem.toString}"""
}
