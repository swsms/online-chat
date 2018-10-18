package org.artb.onlinechat.clientside

import org.artb.onlinechat.common.Settings
import org.slf4j.LoggerFactory

object ClientRunner {

    @JvmStatic
    private val logger = LoggerFactory.getLogger(javaClass)

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val server = ChatClient()
            server.start()
        } catch (e: Exception) {
            logger.error("Cannot start a server on port ${Settings.SERVER_PORT}", e)
        }
    }
}