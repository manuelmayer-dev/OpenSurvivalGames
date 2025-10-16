package dev.suchbyte.openSurvivalGames.common.utils

import org.bukkit.Bukkit


class Title {
    companion object {
        fun sendToOnlinePlayers(title: String, subtitle: String) {
            Bukkit.getServer().onlinePlayers.forEach { it.sendTitle(title, subtitle) }
        }
    }
}