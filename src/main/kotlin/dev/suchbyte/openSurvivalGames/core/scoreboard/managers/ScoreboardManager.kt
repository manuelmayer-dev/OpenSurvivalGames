package dev.suchbyte.openSurvivalGames.core.scoreboard.managers

import dev.suchbyte.openSurvivalGames.core.scoreboard.builders.ScoreboardBuilder
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.domain.scoreboard.ScoreboardType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class ScoreboardManager(
    private val config: PluginConfig
) : Listener {
    private val scoreboards = mutableMapOf<Player, ScoreboardBuilder>()
    private val selectedInGameScoreboard = mutableMapOf<UUID, ScoreboardType>()

    fun registerScoreboard(player: Player, scoreboard: ScoreboardBuilder) {
        scoreboards[player] = scoreboard
    }

    fun unregisterScoreboard(player: Player) {
        if (scoreboards.containsKey(player)) {
            scoreboards.remove(player)
        }
    }

    fun getAllScoreboards(): List<ScoreboardBuilder> {
        return scoreboards.values.toList()
    }

    fun setInGameScoreboard(player: Player, type: ScoreboardType, completion: (() -> Unit)) {
        selectedInGameScoreboard[player.uniqueId] = type
        updateSelectedScoreboardType(player, completion)
    }

    fun getSelectedInGameScoreboard(player: Player): ScoreboardType {
        return selectedInGameScoreboard[player.uniqueId] ?: ScoreboardType.Modern
    }

    fun removeScoreboard(player: Player) {
        // TODO: Remove Scoreboard
        /*val scoreboard = swordGameCoreApi.getScoreboard(player)
        scoreboard.getObjective("deleted")?.unregister()
        val objective = scoreboard.registerNewObjective("deleted", "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR*/
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        selectedInGameScoreboard.remove(event.player.uniqueId)
        scoreboards.remove(event.player)
    }

    private fun loadSelectedScoreboardType(player: Player) {
        // TODO: Load Selected Scoreboard Type
    }

    private fun updateSelectedScoreboardType(player: Player, completion: (() -> Unit)? = null) {
        val selectedType = selectedInGameScoreboard.getOrElse(player.uniqueId) { ScoreboardType.Modern }
        // TODO: Update scoreboard type
        completion?.invoke()
    }
}