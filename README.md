# OpenSurvivalGames (formerly SGHub plugin)

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.8.8+-green.svg)](https://www.spigotmc.org/)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-AGPLv3-blue.svg)](LICENSE)

**OpenSurvivalGames** is a feature-rich Survival Games (Hunger Games) plugin for Minecraft servers, inspired by the legendary **HiveSG** minigame. Battle against other players, collect loot from chests, and be the last one standing!

> **ℹ️ About this Project**  
> This is the **official Survival Games plugin from the SGHub server**, rewritten to work as a standalone plugin. The original plugin had extensive dependencies on SGHub's internal systems and infrastructure. This version has been refactored to remove those dependencies, making it usable on any Spigot server. Please note that **not all features from the original SGHub version are available** in this standalone release due to the removal of server-specific integrations.

## 🏗️ Under Construction
This project is currently under construction. Some features may not work as expected or may be missing.

#### Missing features:
- Stats
- Automatic map reset (current workaround is to place the maps in a different folder and restore them via script at the server start)
- Cosmetics are not fully implemented yet
- Built JAR is not available yet, you need to build it yourself

## 🎮 Features

- **Classic Survival Games Gameplay** - Inspired by HiveSG
- **Multiple Game Phases** - Lobby → Warmup → In-Game → Deathmatch → End
- **Map Voting System** - Players vote for their favorite maps
- **Deathmatch System** - When time runs out or few players remain, all survivors are teleported to a smaller arena
- **Customizable Scoreboards** - Multiple scoreboard styles including Modern, Hive 2013, and Hive 2014
- **Cosmetics System**:
  - **Arrow Trails** - Visual effects for arrows (Flames, Hearts, Notes, Lava, and more)
  - **Battle Cries** - Sound effects when eliminating opponents
- **Spectator Mode** - Dead players can spectate the match
- **Chest Tier System** - Two-tier loot distribution for balanced gameplay
- **Configurable Maps** - Easy map setup with spawn points and configuration
- **Performance Optimized** - Built with Kotlin for modern, efficient code

## 📋 Requirements

- **Minecraft Server**: Spigot/Paper 1.8.8 or higher
- **Java**: Java 21 or higher
- **Server Type**: Spigot, Paper, or any Spigot-based fork

## 🚀 Installation

1. Download the latest release from the [Releases](https://github.com/manuelmayer-dev/OpenSurvivalGames/releases) page
2. Place `OpenSurvivalGames-X.X.X.jar` in your server's `plugins/` folder
3. Start or restart your server
4. Configure the plugin in `plugins/OpenSurvivalGames/config.yml`
5. Set up your maps and lobby (see [Setup Guide](#-setup-guide))

## ⚙️ Setup Guide

### Initial Setup

1. **Set Lobby Spawn**
   ```
   /setlobby
   ```
   Stand at your desired lobby location and execute this command.

2. **Create a Map**
   ```
   /createmap <map-name>
   ```
   This imports the world, creates the map configuration, and teleports you to the map.

3. **Set Map Spawns**
   ```
   /setmapspawn
   ```
   Position yourself at a spawn point and execute the command to add the current location as a spawn point.

4. **Configure Tier 2 Chests** (Optional)
   ```
   /tier2tool
   ```
   Right-click chests to mark them as Tier 2 (better loot).

5. **Restart**
   Restart your server to finish the setup.

## 📝 Commands

### Player Commands

| Command | Aliases | Description |
|---------|---------|-------------|
| `/stats` | `/records`, `/globalstats` | Display your statistics |
| `/list` | - | Show online players |
| `/scramble` | - | Hide your stats from other players |
| `/vote` | `/v` | Vote for a map during lobby phase |

### Admin Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/cancel` | `opensurvivalgames.commands.cancel` | Cancel the lobby or deathmatch countdown |
| `/setlobby` | `opensurvivalgames.commands.setup` | Set the lobby spawn location |
| `/createmap <name>` | `opensurvivalgames.commands.setup` | Import a map and create its configuration |
| `/skipwarmup` | `opensurvivalgames.commands.skipwarmup` | Shorten the warmup countdown |
| `/setmapspawn` | `opensurvivalgames.commands.setup` | Set a spawn point for the current map |
| `/setmapspawnitem` | `opensurvivalgames.commands.setup` | Get the convenient spawn-setting tool |
| `/tier2tool` | `opensurvivalgames.commands.setup` | Get the tool to mark Tier 2 chests |
| `/start` | `opensurvivalgames.commands.start` | Shorten the lobby countdown |
| `/forcestart` | `opensurvivalgames.commands.forcestart` | Bypass minimum players and start immediately |
| `/forcemap <map>` | `opensurvivalgames.commands.forcemap` | Directly select a map from the pool |

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `opensurvivalgames.commands.cancel` | Cancel countdowns | OP |
| `opensurvivalgames.commands.setup` | Setup maps and lobby | OP |
| `opensurvivalgames.commands.skipwarmup` | Skip warmup phase | OP |
| `opensurvivalgames.commands.start` | Start countdown early | OP |
| `opensurvivalgames.commands.forcestart` | Force start bypassing requirements | OP |
| `opensurvivalgames.commands.forcemap` | Force select a specific map | OP |

## 🗺️ Map Configuration

Maps are configured in `<map-name>/config.json`:

## 🏗️ Project Structure

The plugin follows a clean domain-driven architecture:

```
src/main/kotlin/dev/suchbyte/survivalGamesPlugin/
├── core/                   # Plugin core & configuration
├── domain/                 # Data models & enums
├── application/            # Business logic & game management
│   ├── game/              # Game state handlers
│   ├── maps/              # Map management
│   ├── cosmetics/         # Cosmetics system
│   ├── stats/             # Statistics tracking
│   └── scoreboard/        # Scoreboard implementations
├── presentation/           # User interface
│   ├── commands/          # Command handlers
│   └── gui/               # Inventory menus
├── listeners/              # Event handlers
└── infrastructure/         # Technical utilities
```

## 🛠️ Building from Source

### Prerequisites
- Java 21 JDK
- Maven 3.6+

### Build Steps

```bash
# Clone the repository
git clone https://github.com/manuelmayer-dev/OpenSurvivalGames.git
cd OpenSurvivalGames

# Build with Maven
mvn clean package

# The compiled JAR will be in target/OpenSurvivalGames-1.0-SNAPSHOT.jar
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📜 License

This project is licensed under the **GNU Affero General Public License v3.0 (AGPLv3)** - see the [LICENSE](LICENSE) file for details.

### What does this mean?

- ✅ **You can** use, modify, and distribute this software
- ✅ **You can** use it for commercial purposes
- ⚠️ **You must** disclose the source code of any modifications
- ⚠️ **You must** license derivative works under AGPLv3
- ⚠️ **Network use** counts as distribution (you must share your modifications even if only running a server)

For the complete license terms, see [LICENSE](LICENSE) or visit [gnu.org/licenses/agpl-3.0](https://www.gnu.org/licenses/agpl-3.0.html).

## 🙏 Acknowledgments

- **SGHub Server** - Original home of this plugin
- Inspired by **The Hive's Survival Games** (HiveSG)
- Built with [Spigot API](https://www.spigotmc.org/)
- Developed with [Kotlin](https://kotlinlang.org/)

## ℹ️ Standalone vs. SGHub Version

This standalone version has been refactored from the original SGHub server plugin to work independently. The following features are **not available** in the standalone version due to dependencies on SGHub's internal infrastructure:

- **SGHub-specific integrations:**
  - Cross-server statistics synchronization
  - Network-wide leaderboards
  - BungeeCord lobby integration
  - Custom permission system integration
  - Internal API endpoints
  - Server-specific cosmetics unlocking system
  - Network economy integration

All core gameplay features remain fully functional in this standalone release.

---

**Made with ❤️ by Manuel Mayer**
