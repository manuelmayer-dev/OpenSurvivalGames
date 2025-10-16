package dev.suchbyte.openSurvivalGames.core.cosmetics.arrowTrails

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Effect
import org.bukkit.plugin.Plugin

class VillagerThundercloudArrowTrail(
    plugin: Plugin
): ArrowTrail(plugin) {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGVillagerThundercloudArrowTrail
    override val effect: Effect
        get() = Effect.VILLAGER_THUNDERCLOUD
}