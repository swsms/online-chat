package org.artb.onlinechat.clientside

import org.artb.onlinechat.common.Settings
import org.slf4j.LoggerFactory
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.Socket
import java.util.*
import java.io.IOException
import java.time.LocalDateTime


class ChatClient {
    companion object {
        private val logger = LoggerFactory.getLogger(ChatClient::class.java)
    }

    fun start() {
        val scanner = Scanner(System.`in`)

        Socket(InetAddress.getByName("127.0.0.1"), Settings.SERVER_PORT).use {
            logger.info("Established connection to ${it.remoteSocketAddress}")

            val output = DataOutputStream(it.getOutputStream())
            val input = DataInputStream(it.getInputStream())

            println("Please, enter your name:")
            val name = scanner.nextLine()

            val receivedMessagesListener = Thread {
                try {
                    while (true) {
                        val msg = input.readUTF()
                        val now = LocalDateTime.now().withNano(0)
                        println("[${now.toString().replace('T', ' ')}] $msg")
                    }
                } catch (e: IOException) {
                    logger.error("Cannot interact with the server", e)
                }
            }

            receivedMessagesListener.start()

            while (true) {
                val msgToSend = scanner.nextLine()
                output.writeUTF("$name: $msgToSend")

                if (msgToSend.trim().equals("/exit", ignoreCase = true)) {
                    break
                }
            }

            it.close()
            input.close()
            output.close()
        }
    }
}