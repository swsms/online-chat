package org.artb.onlinechat.clientside

import org.artb.onlinechat.common.ServerSettings
import org.slf4j.LoggerFactory

object ClientRunner {

    @JvmStatic
    private val logger = LoggerFactory.getLogger(javaClass)

    @JvmStatic
    fun main(cliArgs: Array<String>) {
        val args = ServerSettings.parseArguments(cliArgs)
        try {
            val client = ChatClient(args.address, args.port)
            client.start()
        } catch (e: Exception) {
            logger.error("Cannot start a server on ${args.address}:${args.port}", e)
        }
    }
}