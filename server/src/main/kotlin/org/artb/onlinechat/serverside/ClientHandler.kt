package org.artb.onlinechat.serverside

import org.slf4j.LoggerFactory
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.UUID
import java.net.Socket
import java.io.IOException

class ClientHandler(val clientId: UUID, val server: ChatServer, val clientSocket: Socket) : Thread() {
    companion object {
        private val logger = LoggerFactory.getLogger(ClientHandler::class.java)
    }

    private val input = DataInputStream(clientSocket.getInputStream())
    private val output = DataOutputStream(clientSocket.getOutputStream())

    @Volatile
    private var running = false

    override fun run() {
        try {
            server.broadcastMessage("Client $clientId is ready to chatting", this)
            running = true
            while (running) {
                if (clientSocket.isClosed) {
                    break
                }

                val msg = input.readUTF()
                if (msg.trim { it <= ' ' }.equals("/exit", ignoreCase = true)) {
                    disconnect()
                    break
                }

                logger.info(msg)
                server.broadcastMessage(msg, this)
            }
        } catch (e: IOException) {
            logger.error("Cannot interact with $clientId", e)
            disconnect()
        }

    }

    @Throws(IOException::class)
    fun sendMsg(msg: String) {
        output.writeUTF(msg)
        output.flush()
    }

    @Throws(IOException::class)
    fun disconnect() {
        running = false
        clientSocket.close()
        input.close()
        output.close()
    }
}