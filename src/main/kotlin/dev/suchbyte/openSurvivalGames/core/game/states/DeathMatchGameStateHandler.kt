package dev.suchbyte.openSurvivalGames.core.game.states

import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.common.extensions.ConfigLocationExtensions.Companion.toBukkitLocation
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayersWithPrefix
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.core.maps.managers.DeathMatchMapManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import dev.suchbyte.openSurvivalGames.common.utils.DateUtils
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.scheduler.BukkitTask

class DeathMatchGameStateHandler(
    private val plugin: OpenSurvivalGamesPlugin,
    private val gameManager: GameManager,
    private val statsManager: StatsManager,
    private val deathMatchMapManager: DeathMatchMapManager
) : GameStateHandler(plugin, GameState.DeathMatch) {
    private var deathMatchTimerTask: BukkitTask? = null
    private var deathMatchCountdown = 60 * 5 + 1

    override fun onEnable() {
        super.onEnable()
        teleportAlivePlayers()
        teleportSpectators()
        startDeathMatchTimer()
    }

    override fun onDisable() {
        super.onDisable()
        deathMatchTimerTask?.cancel()
    }

    fun getTimeLeft(): Int {
        return deathMatchCountdown
    }

    private fun teleportAlivePlayers() {
        val spawns = deathMatchMapManager.mapConfig?.spawnLocations?.map { it.toBukkitLocation() } ?: return
        var index = 0
        gameManager.alivePlayers.forEach { player ->
            statsManager.addDeathMatch(player)
            player.teleport(spawns[index++])
        }
    }

    private fun teleportSpectators() {
        val spectatorSpawn = deathMatchMapManager.mapConfig?.spawnLocations?.last()?.toBukkitLocation() ?: return
        gameManager.spectatingPlayers.forEach { player ->
            player.allowFlight = true
            player.isFlying = true
            player.teleport(spectatorSpawn)
        }
    }

    private fun startDeathMatchTimer() {
        if (deathMatchTimerTask != null) {
            return
        }

        deathMatchTimerTask = plugin.server.scheduler.runTaskTimer(plugin, {
            deathMatchCountdown -= 1

            if (deathMatchCountdown > 60) {
                if (deathMatchCountdown % 60 * 5 == 0) {
                    announceGameEnd()
                }
            }

            if (deathMatchCountdown in 11..60) {
                if (deathMatchCountdown % 10 == 0) {
                    announceGameEnd()
                }
            }

            if (deathMatchCountdown <= 10) {
                announceGameEnd()
            }

            if (deathMatchCountdown <= 3) {
                Bukkit.getOnlinePlayers().forEach { player ->
                    player.playSound(player.eyeLocation, Sound.SUCCESSFUL_HIT, 1f, 1f)
                }
            }

            if (deathMatchCountdown < 1) {
                deathMatchTimerTask?.cancel()
                deathMatchTimerTask = null
                onDisable()
            }
        }, 20, 20)
    }

    private fun announceGameEnd() {
        "§b${DateUtils.formatSeconds(deathMatchCountdown.toLong())} §6until the game ends!"
            .sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
    }
}