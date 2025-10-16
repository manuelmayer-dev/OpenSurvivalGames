package dev.suchbyte.openSurvivalGames.core.cosmetics.arrowTrails

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Effect
import org.bukkit.entity.Arrow
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

abstract class ArrowTrail(
    private val plugin: Plugin
) {
    abstract val cosmetic: Cosmetics
    abstract val effect: Effect

    open fun launch(arrow: Arrow) {
        object : BukkitRunnable() {
            override fun run() {
                if (!arrow.isValid || arrow.isOnGround) {
                    cancel()
                    return
                }
                val effectsCount = Random.nextInt(1, 4)

                repeat(effectsCount) {
                    val offsetX = (Random.nextDouble(-0.3, 0.3))
                    val offsetY = (Random.nextDouble(-0.3, 0.3))
                    val offsetZ = (Random.nextDouble(-0.3, 0.3))
                    val effectLocation = arrow.location.clone().add(offsetX, offsetY, offsetZ)

                    arrow.world.playEffect(effectLocation, effect, getData())
                }
            }
        }.runTaskTimer(plugin, 0, 2)
    }

    open fun getData(): Int {
        return 0
    }
}