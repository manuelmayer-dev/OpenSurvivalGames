package dev.suchbyte.openSurvivalGames.common.utils

import dev.suchbyte.openSurvivalGames.common.extensions.DateExtensions.Companion.toSecondsPolyfill
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

class DateUtils {
    companion object {
        fun formatSecondsToMinutesAndSeconds(seconds: Long): String {
            val duration = Duration.ofSeconds(seconds)
            val minutes = duration.toMinutes()
            val secondsPart = duration.seconds % 60

            return String.format("%02d:%02d", minutes, secondsPart)
        }

        fun formatSeconds(seconds: Long, alwaysShowSeconds: Boolean = false): String {
            val duration = Duration.ofSeconds(seconds)
            return formatDuration(duration, alwaysShowSeconds)
        }

        fun formatTimeSpan(datetime: LocalDateTime): String {
            val now = LocalDateTime.now(ZoneOffset.UTC)
            val duration = Duration.between(now, datetime).abs()
            return formatDuration(duration)
        }

        fun formatMinutesToHours(minutesDuration: Int): String {
            val duration = Duration.ofMinutes(minutesDuration.toLong())
            val hours = duration.toHours()
            val minutes = duration.minusHours(hours).toMinutes()

            val parts = mutableListOf<String>()
            if (hours > 0) parts.add("$hours ${if (hours == 1L) "hour" else "hours"}")
            if (minutes > 0) parts.add("$minutes ${if (minutes == 1L) "minute" else "minutes"}")

            return parts.joinToString(", ")
        }

        private fun formatDuration(duration: Duration, alwaysShowSeconds: Boolean = false) : String {
            val days = duration.toDays()
            val hours = duration.minusDays(days).toHours()
            val minutes = duration.minusDays(days).minusHours(hours).toMinutes()
            val seconds = duration.minusDays(days).minusHours(hours).minusMinutes(minutes).toSecondsPolyfill()

            val parts = mutableListOf<String>()

            if (days > 0) parts.add("$days ${if (days == 1L) "day" else "days"}")
            if (hours > 0) parts.add("$hours ${if (hours == 1L) "hour" else "hours"}")
            if (minutes > 0) parts.add("$minutes ${if (minutes == 1L) "minute" else "minutes"}")
            if (minutes < 1 || alwaysShowSeconds) parts.add("$seconds ${if (seconds == 1L) "second" else "seconds"}")

            return parts.joinToString(", ")
        }
    }
}