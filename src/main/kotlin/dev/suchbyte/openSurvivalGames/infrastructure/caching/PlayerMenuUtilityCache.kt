package dev.suchbyte.openSurvivalGames.infrastructure.caching

import dev.suchbyte.openSurvivalGames.presentation.gui.base.PlayerMenuUtility
import org.bukkit.entity.Player
import java.util.concurrent.locks.ReentrantLock

class PlayerMenuUtilityCache {
    companion object {
        private val playerMenusLock = ReentrantLock()
        private val playerMenus = mutableMapOf<Player, PlayerMenuUtility>()

        fun getPlayerMenuUtility(player: Player): PlayerMenuUtility {
            playerMenusLock.lock()
            if (playerMenus.containsKey(player)) {
                return playerMenus[player]!!
            }

            val playerMenu = PlayerMenuUtility(player)
            playerMenus[player] = playerMenu
            playerMenusLock.unlock()
            return playerMenu
        }

        fun removePlayerMenu(player: Player) {
            playerMenusLock.lock()
            if (playerMenus.containsKey(player)) {
                playerMenus.remove(player)
            }
            playerMenusLock.unlock()
        }
    }
}
