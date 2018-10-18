package org.artb.onlinechat.serverside

import org.slf4j.LoggerFactory
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.UUID
import java.net.Socket
import java.io.IOException

class ClientHandler(val clientId: UUID, val server: Server, val clientSocket: Socket) : Thread() {
    companion object {
        private val logger = LoggerFactory.getLogger(ClientHandler::class.java)
    }

    private val input = DataInputStream(clientSocket.getInputStream())
    private val output = DataOutputStream(clientSocket.getOutputStream())

    override fun run() {
        try {
            server.broadcastMessage("Client $clientId is ready to chatting")

            while (true) {
                if (clientSocket.isClosed) {
                    break
                }

                val msg = input.readUTF()
                if (msg.trim { it <= ' ' }.equals("/exit", ignoreCase = true)) {
                    disconnect()
                    break
                }

                val msgWithClient = "$clientId: $msg"
                logger.info(msgWithClient)
                server.broadcastMessage(msgWithClient)
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

    private fun disconnect() {
        try {
            clientSocket.close()
            input.close()
            output.close()
        } catch (e: IOException) {
            logger.error("Cannot disconnect $clientId", e)
        }
        server.broadcastMessage(String.format("$clientId disconnected from the server\n"))
    }
}