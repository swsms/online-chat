package org.artb.onlinechat.serverside.command

import org.artb.onlinechat.serverside.ClientHandler

class ListUsersCommand: Command {

    override fun execute(handler: ClientHandler) {
        val users = listOf("Homer J. Simpson", "John Smith", "Alice Bright") // TODO load the list from server
        handler.sendMsg(users.toString())
    }
}
