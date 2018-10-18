package org.artb.onlinechat.serverside

import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.Socket
import java.net.ServerSocket
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class Server(val port: Int) {
    companion object {
        private val logger = LoggerFactory.getLogger(Server::class.java)
    }

    private val handlers = ConcurrentHashMap<UUID, ClientHandler>()
    private val lock = ReentrantReadWriteLock()

    fun start() {
        ServerSocket(port).use {
            logger.info("The chat server has been successfully started on port $port")
            while (true) {
                logger.info("Waiting for a new client")
                regNewClient(it.accept())
            }
        }
    }

    fun regNewClient(client: Socket) {
        val clientId = UUID.randomUUID()
        logger.info("New client $clientId is coming")
        lock.write {
            try {
                val handler = ClientHandler(UUID.randomUUID(), this, client)
                handler.start()
                handlers[clientId] = handler
            } catch (e: IOException) {
                logger.error("Cannot register a new client with id $clientId", e)
            }
        }
    }

    fun broadcastMessage(msg: String) {
        lock.read {
            handlers.forEach { clientId, clientHandler ->
                try {
                    clientHandler.sendMsg(msg)
                } catch (e: IOException) {
                    logger.error("Cannot send msg to $clientId", e)
                }
            }
        }
    }
}