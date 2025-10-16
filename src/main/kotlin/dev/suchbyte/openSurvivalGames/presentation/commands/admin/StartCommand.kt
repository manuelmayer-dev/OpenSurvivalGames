package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StartCommand(
    private val delegate: (CommandSender) -> Unit,
) : BaseCommand("start", "opensurvivalgames.commands.start") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        delegate(player)
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        delegate(sender)
    }
}