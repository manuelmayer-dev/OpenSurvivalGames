package dev.suchbyte.openSurvivalGames.domain.stats

import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import org.bukkit.command.CommandSender
import java.time.LocalDateTime

data class PlayerStats(
    var kills: Int,
    var deaths: Int,
    var wins: Int,
    var points: Int,
    var gamesPlayed: Int,
    var chestsOpened: Int,
    var deathMatches: Int,
    var arrowsShot: Int,
    var arrowsHit: Int,
    var rodThrownOut: Int,
    var rodHit: Int,
    var damageMade: Double,
    var damageTaken: Double,
    var lastWon: LocalDateTime?,
    var lastPlayed: LocalDateTime?
) {
    fun sendToPlayer(sender: CommandSender,) {
        sender.sendMessageWithPrefix("§7----------------------------", Prefix.SurvivalGames)
        sender.sendMessageWithPrefix("§6Kills: §e${kills}", Prefix.SurvivalGames)
        sender.sendMessageWithPrefix("§6Deaths: §e${deaths}", Prefix.SurvivalGames)
        sender.sendMessageWithPrefix("§6Points: §e${points}", Prefix.SurvivalGames)
        sender.sendMessageWithPrefix("§6Victories: §e${wins}", Prefix.SurvivalGames)
        sender.sendMessageWithPrefix("§6Crates: §e${chestsOpened}", Prefix.SurvivalGames)
        sender.sendMessageWithPrefix("§6DeathMatches: §e${deathMatches}", Prefix.SurvivalGames)
        sender.sendMessageWithPrefix("§6Games Played: §e${gamesPlayed}", Prefix.SurvivalGames)
        sender.sendMessageWithPrefix("§7----------------------------", Prefix.SurvivalGames)
    }
}