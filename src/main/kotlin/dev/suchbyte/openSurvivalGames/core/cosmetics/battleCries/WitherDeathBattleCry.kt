package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class WitherDeathBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGWitherDeathBattleCry

    override val sound: Sound
        get() = Sound.WITHER_DEATH
}