package dev.suchbyte.openSurvivalGames.core.listeners.combat

import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.entity.Arrow
import org.bukkit.entity.FishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EntityDamageByEntityListener(
    private val gameManager: BaseGameManager,
    private val statsManager: StatsManager
) : Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (!gameManager.isInGame()
            || gameManager.spectatingPlayers.contains(event.entity)
            || gameManager.spectatingPlayers.contains(event.damager)) {
            event.isCancelled = true
            return
        }

        var damager: Player? = null

        if (event.damager is Arrow) {
            val arrow = event.damager as Arrow
            if (arrow.shooter is Player) {
                damager = arrow.shooter as Player
                statsManager.arrowHit(damager)
            }
        } else if (event.damager is FishHook) {
            val hook = event.damager as FishHook
            if (hook.shooter is Player) {
                damager = hook.shooter as Player
                statsManager.rodHit(damager)
            }
        } else if (event.damager is Player) {
            damager = event.damager as Player
        }

        if (damager is Player) {
            statsManager.addDamageMade(damager, event.damage)
        }

        if (event.entity is Player) {
            gameManager.playerDamaged(event.entity as Player, damager)
        }
    }
}