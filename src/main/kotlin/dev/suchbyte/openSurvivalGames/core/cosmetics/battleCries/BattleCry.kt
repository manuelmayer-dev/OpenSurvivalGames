package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound
import org.bukkit.entity.Player

abstract class BattleCry {
    abstract val cosmetic: Cosmetics
    abstract val sound: Sound

    fun play(players: List<Player>) {
        players.forEach { it.playSound(it.eyeLocation, sound, 1f, 1f) }
    }
}