package org.artb.onlinechat.clientside

import org.slf4j.LoggerFactory

object Client {
    private val logger = LoggerFactory.getLogger(javaClass)

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("Client started.")
        logger.info("Client stopped.")
    }
}