package org.artb.onlinechat.serverside.command

import org.artb.onlinechat.serverside.ClientHandler

class ListUsersCommand: Command {

    override fun execute(handler: ClientHandler) {
        val users = listOf("Homer J. Simpson", "John Smith", "Alice Bright")
        handler.sendMsg(users.toString())
    }
}