package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CancelCommand(
    private val delegate: (CommandSender) -> Unit,
) : BaseCommand("cancel", "opensurvivalgames.commands.cancel") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        cancel(player)
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        cancel(sender)
    }

    private fun cancel(sender: CommandSender) {
        delegate(sender)
    }
}