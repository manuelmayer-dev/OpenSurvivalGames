package dev.suchbyte.openSurvivalGames.core.cosmetics.managers

import dev.suchbyte.openSurvivalGames.core.cosmetics.arrowTrails.*
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*

class ArrowTrailManager(
    plugin: Plugin,
    private val cosmeticManager: CosmeticManager
) : Listener {
    val arrowTrails = listOf(
        ColoredDustArrowTrail(plugin),
        FlamesArrowTrail(plugin),
        HappyVillagerArrowTrail(plugin),
        HeartsArrowTrail(plugin),
        LavaDripArrowTrail(plugin),
        LavaPopArrowTrail(plugin),
        NotesArrowTrail(plugin),
        VillagerThundercloudArrowTrail(plugin)
    )

    private val cachedArrowTrail = mutableMapOf<UUID, ArrowTrail>()

    fun cachePlayer(player: Player) {
        val arrowTrail = arrowTrails.find { x -> cosmeticManager.hasEnabled(player, x.cosmetic.uid) } ?: return
        cachedArrowTrail[player.uniqueId] = arrowTrail
    }

    private fun launch(player: Player, arrow: Arrow) {
        cachedArrowTrail[player.uniqueId]?.launch(arrow)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        cachedArrowTrail.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        val player = event.entity.shooter as? Player ?: return
        val arrow = event.entity as? Arrow ?: return
        launch(player, arrow)
    }
}