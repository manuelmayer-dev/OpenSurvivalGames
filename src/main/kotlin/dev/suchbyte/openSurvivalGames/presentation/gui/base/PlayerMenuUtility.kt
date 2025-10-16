package dev.suchbyte.openSurvivalGames.presentation.gui.base

import org.bukkit.entity.Player

class PlayerMenuUtility(
    private val owner: Player
) {
    fun getOwner(): Player {
        return owner
    }
}