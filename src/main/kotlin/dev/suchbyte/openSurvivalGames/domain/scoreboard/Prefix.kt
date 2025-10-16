package dev.suchbyte.openSurvivalGames.domain.scoreboard

enum class Prefix(val value: String) {
    SurvivalGames("§3SurvivalGames");

    fun getFormatted(): String {
        return "§8▎ ${this.value} §8▏§r"
    }
}