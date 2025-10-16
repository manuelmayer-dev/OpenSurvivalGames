package dev.suchbyte.openSurvivalGames.common.extensions

import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import org.bukkit.Bukkit

class StringExtensions {
    companion object {
        fun String.sendToOnlinePlayersWithPrefix(prefix: Prefix = Prefix.SurvivalGames) {
            Bukkit.getServer().onlinePlayers.forEach { player ->
                player.sendMessageWithPrefix(this, prefix)
            }
            Bukkit.getLogger().info(this.removeMinecraftColorCodes())
        }

        fun String.sendToOnlinePlayers() {
            Bukkit.getServer().onlinePlayers.forEach { player ->
                player.sendMessage(this)
            }
            Bukkit.getLogger().info(this.removeMinecraftColorCodes())
        }

        fun String.removeMinecraftColorCodes(): String {
            // Regex to match Minecraft color codes (§ followed by any valid color/formatting character)
            val regex = "§[0-9a-fk-orA-FK-OR]".toRegex()
            return this.replace(regex, "")
        }
    }
}
