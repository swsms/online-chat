package org.artb.onlinechat.clientside

import org.slf4j.LoggerFactory
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.Socket
import java.util.*
import java.io.IOException
import java.time.LocalDateTime


class ChatClient(val serverIp: String, val serverPort: Int) {
    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(ChatClient::class.java)
    }

    @Volatile
    private var running = false

    fun start() {
        val scanner = Scanner(System.`in`)

        println("Please, enter your name:")
        val name = scanner.nextLine()

        Socket(InetAddress.getByName(serverIp), serverPort).use {
            logger.info("Established connection to $serverIp:$serverPort")
            running = true

            val output = DataOutputStream(it.getOutputStream())
            val input = DataInputStream(it.getInputStream())

            val receivedMessagesListener = Thread {
                try {
                    while (running) {
                        val msg = input.readUTF()
                        val now = LocalDateTime.now().withNano(0)
                        println("[${now.toString().replace('T', ' ')}] $msg")
                    }
                } catch (e: IOException) {
                    logger.error("Cannot interact with the server", e)
                }
            }

            receivedMessagesListener.start()

            while (running) {
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