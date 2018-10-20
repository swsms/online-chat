package org.artb.onlinechat.serverside.command

import org.artb.onlinechat.serverside.ClientHandler

class UnknownCommand : Command {

    override fun execute(handler: ClientHandler) {
        handler.sendMsg("You entered an unknown command")
    }
}