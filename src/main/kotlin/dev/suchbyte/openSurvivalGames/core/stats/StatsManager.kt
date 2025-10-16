package dev.suchbyte.openSurvivalGames.core.stats

import dev.suchbyte.openSurvivalGames.domain.stats.PlayerStats
import dev.suchbyte.openSurvivalGames.domain.stats.RoundStats
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.removeMinecraftColorCodes
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.math.roundToInt

class StatsManager(
    private val plugin: OpenSurvivalGamesPlugin,
    private val config: PluginConfig
) {
    private val cachedSeasonStats = mutableMapOf<UUID, PlayerStats>()
    private val cachedPlayerStats = mutableMapOf<UUID, PlayerStats>()
    private val fakePlayerStats = mutableMapOf<String, PlayerStats>()
    private val playerStatsCommitted = mutableListOf<UUID>()
    private val cachedRoundStats = mutableMapOf<UUID, RoundStats>()
    private val roundStatsCommitted = mutableListOf<UUID>()
    private val scrambledStats = mutableMapOf<UUID, PlayerStats>()

    fun resetGlobalStats(player: Player) {
        cachedPlayerStats[player.uniqueId] = getInitialStats()
        putPlayerStats(player)
        player.sendMessageWithPrefix("§6Your §2global stats §6have been successfully reset.", Prefix.SurvivalGames)
    }

    fun toggleScramble(player: Player) {
        if (scrambledStats.containsKey(player.uniqueId)) {
            scrambledStats.remove(player.uniqueId)
            player.sendMessageWithPrefix("§6Your stats are no longer scrambled.", Prefix.SurvivalGames)
            return
        }

        scrambledStats[player.uniqueId] = getScrambledStats()
        player.sendMessageWithPrefix("§6Your stats are now scrambled.", Prefix.SurvivalGames)
    }

    fun setKiller(player: Player, killer: Player) {
        getRoundStats(player).killerName = killer.customName.removeMinecraftColorCodes()
        getRoundStats(player).killerHealth = killer.health
    }

    fun getRoundStatsOrNull(player: Player): RoundStats? {
        return cachedRoundStats[player.uniqueId]
    }

    fun getRoundStats(player: Player): RoundStats {
        return cachedRoundStats.getOrDefault(player.uniqueId, getInitialRoundStats())
    }

    fun getStatsWithScrambled(player: Player): PlayerStats {
        if (scrambledStats.containsKey(player.uniqueId)) {
            return scrambledStats[player.uniqueId]!!
        }

        return getStats(player)
    }

    fun getSeasonStatsWithScrambled(player: Player): PlayerStats {
        if (scrambledStats.containsKey(player.uniqueId)) {
            return scrambledStats[player.uniqueId]!!
        }

        return getSeasonStats(player)
    }

    fun getSeasonStats(player: Player): PlayerStats {
        return cachedSeasonStats.getOrDefault(player.uniqueId, getInitialStats())
    }

    fun getStats(player: Player): PlayerStats {
        return cachedPlayerStats.getOrDefault(player.uniqueId, getInitialStats())
    }

    fun commitAll() {
        Bukkit.getOnlinePlayers().forEach { player ->
            commit(player)
        }
    }

    fun removeCached(player: Player) {
        if (cachedPlayerStats.containsKey(player.uniqueId)) {
            cachedPlayerStats.remove(player.uniqueId)
        }

        if (cachedRoundStats.containsKey(player.uniqueId)) {
            cachedRoundStats.remove(player.uniqueId)
        }

        if (cachedSeasonStats.containsKey(player.uniqueId)) {
            cachedSeasonStats.remove(player.uniqueId)
        }

        if (scrambledStats.containsKey(player.uniqueId)) {
            scrambledStats.remove(player.uniqueId)
        }
    }

    fun commit(player: Player) {
        commitPlayerStats(player)
    }

    fun updateLastPlayed(player: Player) {
        getOrPutPlayerStats(player).lastPlayed = LocalDateTime.now(ZoneOffset.UTC)
        getOrPutSeasonStats(player).lastPlayed = LocalDateTime.now(ZoneOffset.UTC)
    }

    fun addDeathMatch(player: Player) {
        getOrPutPlayerStats(player).deathMatches += 1
        getOrPutSeasonStats(player).deathMatches += 1
        getOrPutRoundStats(player).wasInDeathMatch = true
    }

    fun addWins(player: Player) {
        getOrPutPlayerStats(player).wins += 1
        getOrPutSeasonStats(player).wins += 1
        getOrPutPlayerStats(player).lastWon = LocalDateTime.now(ZoneOffset.UTC)
        getOrPutSeasonStats(player).lastWon = LocalDateTime.now(ZoneOffset.UTC)
        getOrPutRoundStats(player).hasWon = true
    }

    fun addDamageMade(player: Player, damage: Double) {
        getOrPutPlayerStats(player).damageMade += damage
        getOrPutSeasonStats(player).damageMade += damage
        getOrPutRoundStats(player).damageMade += damage
    }

    fun addDamageTaken(player: Player, damage: Double) {
        getOrPutPlayerStats(player).damageTaken += damage
        getOrPutSeasonStats(player).damageTaken += damage
        getOrPutRoundStats(player).damageTaken += damage
    }

    fun addChestOpened(player: Player) {
        getOrPutPlayerStats(player).chestsOpened += 1
        getOrPutSeasonStats(player).chestsOpened += 1
        getOrPutRoundStats(player).chestsOpened += 1
    }

    fun addKill(player: Player) {
        getOrPutPlayerStats(player).kills += 1
        getOrPutSeasonStats(player).kills += 1
        getOrPutRoundStats(player).kills += 1
    }

    fun addSeasonPoints(player: Player, points: Int) {
        getOrPutSeasonStats(player).points += points
    }

    fun addPoints(player: Player, points: Int) {
        getOrPutPlayerStats(player).points += points
        getOrPutRoundStats(player).gainedPoints += points
    }

    fun addDeath(player: Player) {
        getOrPutPlayerStats(player).deaths += 1
        getOrPutSeasonStats(player).deaths += 1
        getOrPutRoundStats(player).hasDied = true
    }

    fun removeSeasonPoints(player: Player, points: Int) {
        getOrPutSeasonStats(player).points -= points
    }

    fun removePoints(player: Player, points: Int) {
        getOrPutPlayerStats(player).points -= points
        getOrPutRoundStats(player).lostPoints += points
    }

    fun addGamesPlayed(player: Player) {
        getOrPutPlayerStats(player).gamesPlayed += 1
        getOrPutSeasonStats(player).gamesPlayed += 1
    }

    fun arrowHit(player: Player) {
        getOrPutPlayerStats(player).arrowsHit += 1
        getOrPutSeasonStats(player).arrowsHit += 1
        getOrPutRoundStats(player).arrowsHit += 1
    }

    fun arrowShot(player: Player) {
        getOrPutPlayerStats(player).arrowsShot += 1
        getOrPutSeasonStats(player).arrowsShot += 1
        getOrPutRoundStats(player).arrowsShot += 1
    }

    fun rodHit(player: Player) {
        getOrPutPlayerStats(player).rodHit += 1
        getOrPutSeasonStats(player).rodHit += 1
        getOrPutRoundStats(player).rodHit += 1
    }

    fun rodThrownOut(player: Player) {
        getOrPutPlayerStats(player).rodThrownOut += 1
        getOrPutSeasonStats(player).rodThrownOut += 1
        getOrPutRoundStats(player).rodThrownOut += 1
    }

    fun isPlayerScrambled(player: Player): Boolean {
        return scrambledStats.containsKey(player.uniqueId)
    }

    fun requestStats(
        playerName: String,
        completion: (PlayerStats) -> Unit
    ) {
        // TODO: Request Stats
        completion(getOrPutPlayerStats(Bukkit.getPlayer(playerName) ?: return))
    }

    private fun commitPlayerStats(player: Player) {
        playerStatsCommitted.add(player.uniqueId)
        putPlayerStats(player)
    }

    private fun putPlayerStats(player: Player) {
        // TODO: Save Stats
    }

    private fun loadPlayerStats(id: Long, completion: (PlayerStats) -> Unit) {
        // TODO: Load Stats
    }

    private fun getOrPutRoundStats(player: Player): RoundStats {
        return cachedRoundStats.getOrPut(player.uniqueId) {
            getInitialRoundStats()
        }
    }

    private fun getOrPutPlayerStats(player: Player): PlayerStats {
        return cachedPlayerStats.getOrPut(player.uniqueId) {
            getInitialStats()
        }
    }

    private fun getOrPutSeasonStats(player: Player): PlayerStats {
        return cachedSeasonStats.getOrPut(player.uniqueId) {
            getInitialStats()
        }
    }

    private fun getInitialRoundStats(): RoundStats {
        return RoundStats(
            0,
            hasDied = false,
            hasWon = false,
            wasInDeathMatch = false,
            gainedPoints = 0,
            lostPoints = 0,
            chestsOpened = 0,
            votedMap = null,
            arrowsShot = 0,
            arrowsHit = 0,
            rodThrownOut = 0,
            rodHit = 0,
            damageMade = 0.0,
            damageTaken = 0.0
        )
    }

    private fun getInitialStats(): PlayerStats {
        return PlayerStats(
            kills = 0,
            deaths = 0,
            wins = 0,
            points = 100,
            gamesPlayed = 0,
            chestsOpened = 0,
            deathMatches = 0,
            arrowsShot = 0,
            arrowsHit = 0,
            rodThrownOut = 0,
            rodHit = 0,
            damageMade = 0.0,
            damageTaken = 0.0,
            lastWon = null,
            lastPlayed = null
        )
    }

    private fun getScrambledStats(): PlayerStats {
        val random = Random() // Create a single instance of Random
        val kills = random.nextInt(251) + 50
        val gamesPlayed = random.nextInt(kills / 3 - kills / 6) + (kills / 6)
        val wins = random.nextInt(gamesPlayed / 3 - gamesPlayed / 10) + (gamesPlayed / 10)
        val points = random.nextInt(kills * 10 - kills * 5) + (kills * 5)
        val deathMatches = (random.nextFloat() * (wins * 3.0f - wins * 1.3f) + wins * 1.3f).roundToInt()
        val crates = random.nextInt(gamesPlayed * 50 - gamesPlayed * 20) + (gamesPlayed * 20)
        return PlayerStats(
            kills = kills,
            deaths = gamesPlayed - wins,
            wins = wins,
            points = points,
            gamesPlayed = gamesPlayed,
            chestsOpened = crates,
            deathMatches = deathMatches,
            arrowsShot = 0,
            arrowsHit = 0,
            rodThrownOut = 0,
            rodHit = 0,
            damageMade = 0.0,
            damageTaken = 0.0,
            lastWon = null,
            lastPlayed = null
        )
    }
}