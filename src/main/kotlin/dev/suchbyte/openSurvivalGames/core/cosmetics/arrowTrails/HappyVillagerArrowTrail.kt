package dev.suchbyte.openSurvivalGames.core.cosmetics.arrowTrails

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Effect
import org.bukkit.plugin.Plugin

class HappyVillagerArrowTrail(
    plugin: Plugin
): ArrowTrail(plugin) {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGHappyVillagerArrowTrail
    override val effect: Effect
        get() = Effect.HAPPY_VILLAGER
}