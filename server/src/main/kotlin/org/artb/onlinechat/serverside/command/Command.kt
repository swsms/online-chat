package org.artb.onlinechat.serverside.command

import org.artb.onlinechat.serverside.ClientHandler

interface Command {
    companion object {
        const val COMMAND_CHARACTER = '/'

        fun getByNameOrUnknown(command: String) = when(command) {
            "${COMMAND_CHARACTER}users" -> ListUsersCommand()
            else -> UnknownCommand()
        }

        fun isCommand(mayBeCommand: String) = mayBeCommand.trim().startsWith(Command.COMMAND_CHARACTER)
    }

    fun execute(handler: ClientHandler)
}