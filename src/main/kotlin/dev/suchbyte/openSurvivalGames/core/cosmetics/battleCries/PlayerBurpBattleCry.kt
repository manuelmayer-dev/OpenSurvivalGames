package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class PlayerBurpBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGPlayerBurpBattleCry

    override val sound: Sound
        get() = Sound.BURP
}