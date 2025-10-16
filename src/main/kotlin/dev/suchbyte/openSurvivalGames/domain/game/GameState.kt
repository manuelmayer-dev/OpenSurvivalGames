package dev.suchbyte.openSurvivalGames.domain.game

enum class GameState(val value: Int) {
    Lobby(10),
    Warmup(20),
    InGame(30),
    DeathMatch(40),
    Ending(50)
}
