package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import dev.suchbyte.openSurvivalGames.core.game.states.WarmupGameStateHandler
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SkipWarmupCommand(
    private val warmupGameStateHandler: WarmupGameStateHandler
): BaseCommand("skipwarmup", "opensurvivalgames.commands.skipwarmup") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        warmupGameStateHandler.skipWarmup()
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        warmupGameStateHandler.skipWarmup()
    }
}