package org.artb.onlinechat.serverside

import org.artb.onlinechat.common.ServerSettings
import org.slf4j.LoggerFactory

object ServerRunner {

    @JvmStatic
    private val logger = LoggerFactory.getLogger(javaClass)

    @JvmStatic
    fun main(cliArgs: Array<String>) {
        val args = ServerSettings.parseArguments(cliArgs)
        try {
            val server = ChatServer(args.address, args.port)
            server.start()
        } catch (e: Exception) {
            logger.error("Cannot start a server on ${args.address}:${args.port}", e)
        }
    }
}