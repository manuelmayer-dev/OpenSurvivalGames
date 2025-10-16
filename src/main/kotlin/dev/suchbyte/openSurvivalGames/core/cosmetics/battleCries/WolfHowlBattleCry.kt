package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class WolfHowlBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGWolfHowlBattleCry

    override val sound: Sound
        get() = Sound.WOLF_HOWL
}