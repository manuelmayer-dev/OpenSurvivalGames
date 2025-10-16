package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class ExplosionBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGExplosionBattleCry

    override val sound: Sound
        get() = Sound.EXPLODE
}