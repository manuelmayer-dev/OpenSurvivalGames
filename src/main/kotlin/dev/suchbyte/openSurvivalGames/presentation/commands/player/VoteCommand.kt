package dev.suchbyte.openSurvivalGames.presentation.commands.player

import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapVoting
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VoteCommand(
    private val mapVoting: MapVoting,
    private val gameManager: GameManager
) : BaseCommand("vote") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        if (gameManager.currentGameState != GameState.Lobby && !gameManager.alivePlayers.contains(player)) {
            player.sendMessageWithPrefix("§cYou cannot vote now!")
            return
        }

        val mapIndex = args.getOrNull(0)?.toIntOrNull()

        if (mapIndex == null) {
            mapVoting.sendVotableMaps(player)
            return
        }

        mapVoting.voteMap(player, mapIndex)
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        sendOnlyPlayerMessage(sender)
    }
}