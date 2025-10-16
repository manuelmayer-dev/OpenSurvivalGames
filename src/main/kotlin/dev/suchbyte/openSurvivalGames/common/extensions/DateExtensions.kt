package dev.suchbyte.openSurvivalGames.common.extensions

import java.time.Duration

class DateExtensions {
    companion object {
        fun Duration.toSecondsPolyfill(): Long {
            return this.toMillis() / 1000
        }
    }
}