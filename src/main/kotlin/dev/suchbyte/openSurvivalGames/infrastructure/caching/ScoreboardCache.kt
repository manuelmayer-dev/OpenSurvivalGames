package dev.suchbyte.openSurvivalGames.infrastructure.caching

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class ScoreboardCache {
    companion object {
        private val scoreboards: ConcurrentHashMap<UUID, Scoreboard> = ConcurrentHashMap()

        fun getScoreboard(player: Player): Scoreboard {
            val scoreboard = scoreboards.getOrPut(player.uniqueId) {
                Bukkit.getScoreboardManager().newScoreboard
            }

            player.scoreboard = scoreboard
            return scoreboard
        }

        fun removeScoreboard(player: Player) {
            if (scoreboards.containsKey(player.uniqueId)) {
                scoreboards.remove(player.uniqueId)
            }
        }
    }
}