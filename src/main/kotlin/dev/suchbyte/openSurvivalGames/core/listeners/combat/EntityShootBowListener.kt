package dev.suchbyte.openSurvivalGames.core.listeners.combat

import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

class EntityShootBowListener(
    private val gameManager: BaseGameManager,
    private val statsManager: StatsManager
) : Listener {
    @EventHandler
    fun onEntitySpawn(event: EntityShootBowEvent) {
        if (event.entity !is Player || !gameManager.isInGame()) {
            return
        }

        statsManager.arrowShot(event.entity as Player)
    }
}