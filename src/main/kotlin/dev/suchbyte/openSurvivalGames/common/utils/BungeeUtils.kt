package dev.suchbyte.openSurvivalGames.common.utils

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class BungeeUtils {
    companion object {
        fun sendPlayerToServer(plugin: Plugin, player: Player, serverName: String) {
            writeAndSend(plugin, "BungeeCord", player, "Connect", serverName)
        }

        private fun writeAndSend(plugin: Plugin, channel: String, player: Player, vararg messages: String) {
            val b = ByteArrayOutputStream()
            val out = DataOutputStream(b)
            try {
                messages.forEach { out.writeUTF(it) }
            }
            catch (error: IOException) {
                error.printStackTrace()
            }
            player.sendPluginMessage(plugin, channel, b.toByteArray())
        }
    }
}