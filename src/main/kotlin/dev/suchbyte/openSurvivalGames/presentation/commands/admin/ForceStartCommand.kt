package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import dev.suchbyte.openSurvivalGames.core.game.states.LobbyGameStateHandler
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ForceStartCommand(
    private val lobbyGameStateHandler: LobbyGameStateHandler
) : BaseCommand("forcestart", "opensurvivalgames.commands.forcestart") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        lobbyGameStateHandler.forceStart()
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        lobbyGameStateHandler.forceStart()
    }
}