package dev.suchbyte.openSurvivalGames.core.listeners.world

import dev.suchbyte.openSurvivalGames.common.Constants
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class BlockPlaceListener(
    private val gameManager: BaseGameManager
) : Listener {
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) {
            return
        }

        if ((!gameManager.isInGame() && gameManager.currentGameState != GameState.Ending)
            || !gameManager.alivePlayers.contains(event.player)
            || !Constants.blockPlaceWhitelist.contains(event.block.type)
        ) {
            event.isCancelled = true
            return
        }

        when (event.block.type) {
            Material.TNT -> {
                if (event.isCancelled) {
                    return
                }
                event.block.type = Material.AIR
                event.block.world.playSound(event.block.location, Sound.CREEPER_HISS, 1f, 1f)

                val tnt = event.block.world.spawn(event.block.location.add(0.5, 0.5, 0.5), TNTPrimed::class.java)
                tnt.fuseTicks = 40
                tnt.customName = event.player.uniqueId.toString()
            }

            else -> {}
        }
    }
}