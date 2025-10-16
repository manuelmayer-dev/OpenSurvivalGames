package dev.suchbyte.openSurvivalGames.presentation.commands.player

import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StatsCommand(
    private val statsManager: StatsManager
) : BaseCommand("stats") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        val playerName = args.getOrNull(0) ?: player.name
        showStats(player, playerName)
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        val playerName = args.getOrNull(0)
        if (playerName == null) {
            sender.sendMessageWithPrefix("§cUsage: /$commandName <PlayerName>", Prefix.SurvivalGames)
            return
        }

        showStats(sender, playerName)
    }

    private fun showStats(sender: CommandSender, playerName: String) {
        sender.sendMessageWithPrefix("§6Loading stats for player $playerName...", Prefix.SurvivalGames)

    }
}