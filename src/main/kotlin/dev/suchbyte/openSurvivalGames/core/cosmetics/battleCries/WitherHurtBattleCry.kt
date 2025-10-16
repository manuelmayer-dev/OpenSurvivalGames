package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class WitherHurtBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGWitherHurtBattleCry

    override val sound: Sound
        get() = Sound.WITHER_HURT
}