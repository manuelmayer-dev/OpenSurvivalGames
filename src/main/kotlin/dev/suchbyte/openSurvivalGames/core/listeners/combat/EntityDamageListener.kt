package dev.suchbyte.openSurvivalGames.core.listeners.combat

import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class EntityDamageListener(
    private val gameManager: BaseGameManager,
    private val statsManager: StatsManager
) : Listener {
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if ((gameManager.currentGameState != GameState.InGame && gameManager.currentGameState != GameState.DeathMatch)
            || gameManager.spectatingPlayers.contains(event.entity)) {
            event.isCancelled = true
            return
        }

        if (event.entity is Player) {
            statsManager.addDamageTaken(event.entity as Player, event.damage)
        }
    }
}