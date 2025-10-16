package dev.suchbyte.openSurvivalGames.common.extensions

import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import org.bukkit.command.CommandSender

class CommandSenderExtensions {
    companion object {
        fun CommandSender.sendNoPermissionsMessage(prefix: Prefix = Prefix.SurvivalGames) {
            this.sendMessageWithPrefix("§cYou have no permission to perform this action.", prefix)
        }

        fun CommandSender.sendInternalErrorMessage(prefix: Prefix = Prefix.SurvivalGames) {
            this.sendMessageWithPrefix("§cSomething went wrong during this action.", prefix)
        }

        fun CommandSender.sendMessageWithPrefix(message: String, prefix: Prefix = Prefix.SurvivalGames) {
            this.sendMessage("${prefix.getFormatted()} $message")
        }
    }
}