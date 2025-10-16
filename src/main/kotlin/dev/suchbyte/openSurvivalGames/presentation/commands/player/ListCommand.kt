package dev.suchbyte.openSurvivalGames.presentation.commands.player

import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ListCommand(
    private val gameManager: GameManager
) : BaseCommand("list") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        execute(player)
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        execute(sender)
    }

    private fun execute(sender: CommandSender) {
        when (gameManager.currentGameState) {
            GameState.Lobby -> {
                val waitingPlayers = Bukkit.getOnlinePlayers()
                sender.sendMessageWithPrefix(
                    "§6Watiting players (${waitingPlayers.size}): ${waitingPlayers.joinToString { it.displayName }}",
                    Prefix.SurvivalGames
                )
            }

            else -> {
                val alivePlayers = gameManager.alivePlayers
                val spectators = gameManager.spectatingPlayers
                sender.sendMessageWithPrefix(
                    "§6Alive players (${alivePlayers.size}): ${alivePlayers.joinToString { it.displayName }}",
                    Prefix.SurvivalGames
                )
                sender.sendMessageWithPrefix(
                    "§eSpectating players (${spectators.size}): ${spectators.joinToString { it.displayName }}",
                    Prefix.SurvivalGames
                )
            }
        }
    }
}