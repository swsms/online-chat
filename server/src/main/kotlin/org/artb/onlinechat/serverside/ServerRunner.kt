package org.artb.onlinechat.serverside

import org.artb.onlinechat.common.Settings
import org.slf4j.LoggerFactory

object ServerRunner {

    @JvmStatic
    private val logger = LoggerFactory.getLogger(javaClass)

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val server = Server(Settings.SERVER_PORT)
            server.start()
        } catch (e: Exception) {
            logger.error("Cannot start a server on port ${Settings.SERVER_PORT}", e)
        }
    }
}