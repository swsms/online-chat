package org.artb.onlinechat.serverside

import org.slf4j.LoggerFactory

object Server {
    private val logger = LoggerFactory.getLogger(javaClass)

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("Server started.")
        logger.info("Server stopped.")
    }
}