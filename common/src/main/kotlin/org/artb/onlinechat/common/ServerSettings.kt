package org.artb.onlinechat.common

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter

class ServerSettings {

    @Parameter(names = ["-ip"])
    var address: String = "127.0.0.1"

    @Parameter(names = ["-port"])
    var port: Int = 9182

    override fun toString(): String {
        return "ServerSettings(address=$address, port=$port)"
    }

    companion object {
        @JvmStatic
        fun parseArguments(cliArgs: Array<String>): ServerSettings {
            val args = ServerSettings()
            JCommander.newBuilder()
                    .addObject(args)
                    .build()
                    .parse(*cliArgs)
            return args
        }
    }
}