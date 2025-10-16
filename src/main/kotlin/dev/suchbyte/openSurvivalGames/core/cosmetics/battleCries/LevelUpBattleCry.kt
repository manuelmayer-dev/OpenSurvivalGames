package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class LevelUpBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGLevelUpBattleCry

    override val sound: Sound
        get() = Sound.LEVEL_UP
}