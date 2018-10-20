package org.artb.onlinechat.serverside

import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.ServerSocket
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class ChatServer(val ip: String, val port: Int) {
    companion object {
        private val logger = LoggerFactory.getLogger(ChatServer::class.java)
    }

    private val handlers = ConcurrentHashMap<UUID, ClientHandler>()
    private val lock = ReentrantReadWriteLock()

    @Volatile
    private var running = false

    fun start() {
        ServerSocket(port, 50, InetAddress.getByName(ip)).use {
            running = true
            logger.info("The chat server has been successfully started on $ip:$port")
            while (running) {
                logger.info("Waiting for a new client")
                registerNewClient(it.accept())
            }
        }
    }

    fun stop() {
        running = false
        handlers.forEach { clientId, clientHandler ->
            try {
                clientHandler.disconnect()
            } catch (e: Exception) {
                logger.error("Cannot disconnect $clientId from server", e)
            }
        }
    }

    fun registerNewClient(client: Socket) {
        val clientId = UUID.randomUUID()
        logger.info("New client $clientId is coming")
        lock.write {
            try {
                val handler = ClientHandler(clientId, this, client)
                handler.start()
                handlers[clientId] = handler
            } catch (e: IOException) {
                logger.error("Cannot register a new client with id $clientId", e)
            }
        }
    }

    fun unregisterClient(clientId: UUID) {
        lock.write {
            val unreg = handlers.remove(clientId)
            logger.info("Unregistered client $unreg")
        }
    }

    fun broadcastMessage(msg: String) {
        lock.read {
            logger.info("$handlers")
            handlers.forEach { clientId, clientHandler ->
                try {
                    clientHandler.sendMsg(msg)
                } catch (e: Exception) {
                    logger.error("Cannot send msg to $clientId", e)
                }
            }
        }
    }
}