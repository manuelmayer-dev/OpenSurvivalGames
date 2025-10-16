package dev.suchbyte.openSurvivalGames

import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.ArrowTrailManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.BattleCryManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.CosmeticManager
import dev.suchbyte.openSurvivalGames.presentation.commands.player.ListCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.ScrambleCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.player.StatsCommand
import dev.suchbyte.openSurvivalGames.core.config.ConfigLoader
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.core.game.states.LobbyGameStateHandler
import dev.suchbyte.openSurvivalGames.core.game.states.WarmupGameStateHandler
import dev.suchbyte.openSurvivalGames.core.game.states.InGameGameStateHandler
import dev.suchbyte.openSurvivalGames.core.game.states.DeathMatchGameStateHandler
import dev.suchbyte.openSurvivalGames.core.game.states.GameStateHandler
import dev.suchbyte.openSurvivalGames.core.maps.managers.DeathMatchMapManager
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapManager
import dev.suchbyte.openSurvivalGames.core.maps.modules.FyreGames2Module
import dev.suchbyte.openSurvivalGames.core.scoreboard.managers.ScoreboardManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import dev.suchbyte.openSurvivalGames.core.listeners.world.BlockBreakListener
import dev.suchbyte.openSurvivalGames.core.listeners.world.BlockFadeListener
import dev.suchbyte.openSurvivalGames.core.listeners.world.BlockPlaceListener
import dev.suchbyte.openSurvivalGames.core.listeners.chat.ChatListener
import dev.suchbyte.openSurvivalGames.core.listeners.world.ChunkLoadListener
import dev.suchbyte.openSurvivalGames.core.listeners.world.EntityChangeBlockListener
import dev.suchbyte.openSurvivalGames.core.listeners.combat.EntityDamageByEntityListener
import dev.suchbyte.openSurvivalGames.core.listeners.combat.EntityDamageListener
import dev.suchbyte.openSurvivalGames.core.listeners.combat.EntityShootBowListener
import dev.suchbyte.openSurvivalGames.core.listeners.entity.EntityTargetLivingEntityListener
import dev.suchbyte.openSurvivalGames.core.listeners.chat.InteractionListener
import dev.suchbyte.openSurvivalGames.core.listeners.entity.CreatureSpawnListener
import dev.suchbyte.openSurvivalGames.core.listeners.inventory.InventoryClickListener
import dev.suchbyte.openSurvivalGames.core.listeners.inventory.InventoryCloseListener
import dev.suchbyte.openSurvivalGames.core.listeners.inventory.InventoryOpenListener
import dev.suchbyte.openSurvivalGames.core.listeners.inventory.ItemDropListener
import dev.suchbyte.openSurvivalGames.core.listeners.inventory.ItemPickupListener
import dev.suchbyte.openSurvivalGames.core.listeners.player.PlayerDeathListener
import dev.suchbyte.openSurvivalGames.core.listeners.player.PlayerFishListener
import dev.suchbyte.openSurvivalGames.core.listeners.player.PlayerJoinListener
import dev.suchbyte.openSurvivalGames.core.listeners.player.PlayerPreLoginListener
import dev.suchbyte.openSurvivalGames.core.listeners.player.PlayerQuitListener
import dev.suchbyte.openSurvivalGames.core.listeners.inventory.PrepareItemCraftListener
import dev.suchbyte.openSurvivalGames.core.listeners.player.RespawnListener
import dev.suchbyte.openSurvivalGames.core.listeners.server.ServerListPingListener
import dev.suchbyte.openSurvivalGames.core.listeners.entity.EntityExplodeListener
import dev.suchbyte.openSurvivalGames.core.listeners.entity.FoodLevelChangeListener
import dev.suchbyte.openSurvivalGames.core.listeners.world.BlockSpreadListener
import dev.suchbyte.openSurvivalGames.core.listeners.world.ChunkUnloadListener
import dev.suchbyte.openSurvivalGames.core.listeners.world.WeatherChangeListener
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.SetLobbyCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.TierTwoItemCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.CreateMapCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.SetMapSpawnCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.SetMapSpawnItemCommand
import dev.suchbyte.openSurvivalGames.presentation.gui.listeners.MenuListener
import dev.suchbyte.openSurvivalGames.infrastructure.tasks.DeathCrateParticlesRunnable
import dev.suchbyte.openSurvivalGames.infrastructure.tasks.HideSpectatorsRunnable
import dev.suchbyte.openSurvivalGames.infrastructure.tasks.ScoreboardUpdateRunnable
import org.bukkit.plugin.java.JavaPlugin

class OpenSurvivalGamesPlugin : JavaPlugin() {
    private val mapModules = mapOf(
        "65a3f746-0732-44d3-a5a0-bac268d58d2e" to FyreGames2Module::class.java
    )

    private val config = ConfigLoader.loadConfig(this.dataFolder, PluginConfig())
    val gameStates = mutableListOf<GameStateHandler>()

    private lateinit var cosmeticManager: CosmeticManager
    private lateinit var battleCryManager: BattleCryManager
    private lateinit var arrowTrailManager: ArrowTrailManager
    private lateinit var scoreboardManager: ScoreboardManager
    private lateinit var mapManager: MapManager
    private lateinit var gameManager: GameManager
    private lateinit var statsManager: StatsManager
    private lateinit var deathMatchMapManager: DeathMatchMapManager

    override fun onEnable() {
        super.onEnable()

        cosmeticManager = CosmeticManager(config)
        battleCryManager = BattleCryManager(cosmeticManager)
        arrowTrailManager = ArrowTrailManager(this, cosmeticManager)
        scoreboardManager = ScoreboardManager(config)
        statsManager = StatsManager(this, config)
        deathMatchMapManager = DeathMatchMapManager(config)
        mapManager = MapManager()
        gameManager =
            GameManager(
                this,
                config,
                statsManager,
                mapManager,
                mapModules,
                cosmeticManager,
                battleCryManager
            )

        registerGameStates()
        registerEventHandlers()
        registerCommands()

        gameManager.start()
        deathMatchMapManager.loadAllMaps()

        HideSpectatorsRunnable(gameManager).runTaskTimer(this, 20, 20)
        DeathCrateParticlesRunnable(gameManager).runTaskTimer(this, 20, 20)
        ScoreboardUpdateRunnable(scoreboardManager).runTaskTimer(this, 10, 10)
    }

    private fun registerGameStates() {
        gameStates.add(LobbyGameStateHandler(this, config, mapManager, scoreboardManager, statsManager, gameManager))
        gameStates.add(
            WarmupGameStateHandler(
                this,
                config,
                mapManager,
                scoreboardManager,
                statsManager,
                battleCryManager,
                arrowTrailManager
            )
        )
        gameStates.add(
            InGameGameStateHandler(
                this,
                config,
                mapManager,
                gameManager,
                statsManager,
                scoreboardManager,
                deathMatchMapManager,
                cosmeticManager
            )
        )
        gameStates.add(
            DeathMatchGameStateHandler(
                this,
                gameManager,
                statsManager,
                deathMatchMapManager
            )
        )
    }

    private fun registerCommands() {
        SetLobbyCommand(this, config).register(this)
        CreateMapCommand(this, config).register(this)
        SetMapSpawnCommand(config).register(this)
        TierTwoItemCommand().register(this)
        SetMapSpawnItemCommand().register(this)
        ScrambleCommand(statsManager).register(this)
        ListCommand(gameManager).register(this)
        StatsCommand(statsManager).register(this)
    }

    private fun registerEventHandlers() {
        server.pluginManager.registerEvents(FoodLevelChangeListener(gameManager), this)
        server.pluginManager.registerEvents(EntityExplodeListener(gameManager), this)
        server.pluginManager.registerEvents(PlayerJoinListener(this, gameManager), this)
        server.pluginManager.registerEvents(PlayerQuitListener(statsManager, gameManager, scoreboardManager), this)
        server.pluginManager.registerEvents(PlayerPreLoginListener(gameManager), this)
        server.pluginManager.registerEvents(PlayerDeathListener(gameManager), this)
        server.pluginManager.registerEvents(RespawnListener(config, gameManager), this)
        server.pluginManager.registerEvents(PlayerFishListener(gameManager, statsManager), this)
        server.pluginManager.registerEvents(EntityDamageListener(gameManager, statsManager), this)
        server.pluginManager.registerEvents(EntityDamageByEntityListener(gameManager, statsManager), this)
        server.pluginManager.registerEvents(EntityShootBowListener(gameManager, statsManager), this)
        server.pluginManager.registerEvents(BlockBreakListener(gameManager), this)
        server.pluginManager.registerEvents(BlockPlaceListener(gameManager), this)
        server.pluginManager.registerEvents(BlockFadeListener(), this)
        server.pluginManager.registerEvents(ChunkLoadListener(), this)
        server.pluginManager.registerEvents(EntityChangeBlockListener(), this)
        server.pluginManager.registerEvents(EntityTargetLivingEntityListener(gameManager), this)
        server.pluginManager.registerEvents(InventoryClickListener(), this)
        server.pluginManager.registerEvents(InventoryOpenListener(), this)
        server.pluginManager.registerEvents(InventoryCloseListener(), this)
        server.pluginManager.registerEvents(ItemDropListener(gameManager), this)
        server.pluginManager.registerEvents(ItemPickupListener(gameManager), this)
        server.pluginManager.registerEvents(PrepareItemCraftListener(), this)
        server.pluginManager.registerEvents(ChatListener(gameManager, statsManager), this)
        server.pluginManager.registerEvents(
            InteractionListener(
                this,
                config,
                gameManager,
                statsManager,
                cosmeticManager,
                battleCryManager,
                arrowTrailManager,
                scoreboardManager
            ), this
        )
        server.pluginManager.registerEvents(ServerListPingListener(mapManager), this)
        server.pluginManager.registerEvents(BlockSpreadListener(), this)
        server.pluginManager.registerEvents(ChunkUnloadListener(), this)
        server.pluginManager.registerEvents(CreatureSpawnListener(), this)
        server.pluginManager.registerEvents(EntityChangeBlockListener(), this)
        server.pluginManager.registerEvents(InventoryClickListener(), this)
        server.pluginManager.registerEvents(InventoryOpenListener(), this)
        server.pluginManager.registerEvents(InventoryCloseListener(), this)
        server.pluginManager.registerEvents(PrepareItemCraftListener(), this)
        server.pluginManager.registerEvents(WeatherChangeListener(), this)
        server.pluginManager.registerEvents(ChunkLoadListener(), this)
        server.pluginManager.registerEvents(BlockFadeListener(), this)
        server.pluginManager.registerEvents(MenuListener(), this)
        server.pluginManager.registerEvents(cosmeticManager, this)
        server.pluginManager.registerEvents(battleCryManager, this)
        server.pluginManager.registerEvents(arrowTrailManager, this)
    }
}
