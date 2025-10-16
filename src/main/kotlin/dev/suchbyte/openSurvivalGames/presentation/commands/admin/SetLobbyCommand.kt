package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import dev.suchbyte.openSurvivalGames.core.config.ConfigLoader
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.common.extensions.ConfigLocationExtensions.Companion.toConfigLocation
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class SetLobbyCommand(
    private val plugin: Plugin,
    private val config: PluginConfig
): BaseCommand("setlobby", "opensurvivalgames.commands.setup") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        config.lobbyLocation = player.location.toConfigLocation()
        ConfigLoader.saveConfig(plugin.dataFolder, config)
        player.sendMessageWithPrefix("§aUpdated lobby location")
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        sendOnlyPlayerMessage(sender)
    }
}