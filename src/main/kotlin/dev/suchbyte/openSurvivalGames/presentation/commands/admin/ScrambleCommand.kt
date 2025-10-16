package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ScrambleCommand(
    private val statsManager: StatsManager
) : BaseCommand("scramble", "swordgamemc.stats.scramble") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        statsManager.toggleScramble(player)
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        sendOnlyPlayerMessage(sender)
    }
}