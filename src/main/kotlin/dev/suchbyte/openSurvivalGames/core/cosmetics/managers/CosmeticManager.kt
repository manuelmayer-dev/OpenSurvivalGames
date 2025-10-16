package dev.suchbyte.openSurvivalGames.core.cosmetics.managers

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class CosmeticManager(
    private val config: PluginConfig
) : Listener {
    private val enabledCosmetics = mutableMapOf<UUID, MutableList<Int>>()

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        enabledCosmetics.remove(event.player.uniqueId)
    }

    fun hasEnabled(player: Player, cosmeticsUid: Int): Boolean {
        return enabledCosmetics[player.uniqueId]?.contains(cosmeticsUid) ?: false
    }

    fun hasEnabled(player: Player, cosmetics: Cosmetics): Boolean {
        return enabledCosmetics[player.uniqueId]?.contains(cosmetics.uid) ?: false
    }

    fun disableCosmetics(player: Player, cosmetics: List<Cosmetics>, completion: () -> Unit) {
        enabledCosmetics.getOrPut(player.uniqueId) { mutableListOf() }.removeAll(cosmetics.map { x -> x.uid })
        updateEnabledCosmetics(player, completion)
    }

    fun enableCosmetic(player: Player, cosmetic: Cosmetics) {
        enabledCosmetics.getOrPut(player.uniqueId) { mutableListOf() }.add(cosmetic.uid)
        updateEnabledCosmetics(player)
    }

    fun toggleCosmetic(player: Player, cosmetic: Cosmetics, completion: (Boolean) -> Unit) {
        if (!hasEnabled(player, cosmetic)) {
            enableCosmetic(player, cosmetic)
            completion(true)
        } else {
            enabledCosmetics.getOrPut(player.uniqueId) { mutableListOf() }.remove(cosmetic.uid)
            completion(false)
        }

        updateEnabledCosmetics(player)
    }

    private fun loadEnabledCosmetics(player: Player) {
        // TODO: Load enabled cosmetics
    }

    private fun updateEnabledCosmetics(player: Player, completion: (() -> Unit)? = null) {
        val enabledCosmetics = enabledCosmetics.getOrElse(player.uniqueId) { mutableListOf() }
        // TODO: Update enabled cosmetics
        completion?.invoke()
    }
}