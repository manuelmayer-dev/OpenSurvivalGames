package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class PigDeathBattleCry  : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGPigDeathBattleCry

    override val sound: Sound
        get() = Sound.PIG_DEATH
}