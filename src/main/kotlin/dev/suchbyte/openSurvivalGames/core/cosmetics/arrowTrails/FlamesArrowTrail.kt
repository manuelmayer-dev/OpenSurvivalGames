package dev.suchbyte.openSurvivalGames.core.cosmetics.arrowTrails

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Effect
import org.bukkit.plugin.Plugin

class FlamesArrowTrail(
    plugin: Plugin
) : ArrowTrail(plugin) {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGFlamesArrowTrail
    override val effect: Effect
        get() = Effect.FLAME
}