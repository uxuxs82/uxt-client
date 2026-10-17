<div align="center">

![UXT Banner](assets/banner.png)

[![Minecraft](https://img.shields.io/badge/Minecraft-1.16.5-brightgreen.svg)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Fabric-0.14.0-blue.svg)](https://fabricmc.net/)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/github/license/uxuxs82/uxt-client)](LICENSE)
[![Build](https://img.shields.io/github/actions/workflow/status/uxuxs82/uxt-client/build.yml?branch=main)](https://github.com/uxuxs82/uxt-client/actions)

**31 powerful modules** • **Clean GUI** • **Fully configurable**

[Features](#features) • [Installation](#installation) • [Controls](#controls) • [Building](#building-from-source)

</div>

---

## 📸 Preview

<div align="center">

![UXT GUI](assets/gui_preview.png)

*Multi-column GUI with draggable interface and configurable settings*

</div>

## ✨ Features

### ⚔️ Combat
- **KillAura** - Automatically attacks nearby entities with configurable range
- **Aimbot** - Locks onto targets with FOV and range settings
- **Velocity** - Reduces knockback taken from attacks
- **AntiKnockback** - Prevents knockback with adjustable strength

### 🏃 Movement
- **Speed** - Increases movement speed with configurable multiplier
- **Sprint** - Automatically sprints when moving forward
- **AutoSprint** - Always keeps sprint enabled
- **NoFall** - Prevents fall damage
- **NoSlow** - Removes slowdown when using items
- **Step** - Allows stepping up higher blocks
- **Jesus** - Walk on water surfaces
- **Blink** - Freezes server position while moving
- **Freecam** - Fly freely through blocks

### 👤 Player
- **AutoEat** - Automatically eats when hungry
- **AutoArmor** - Equips best armor automatically
- **AntiFire** - Extinguishes fire instantly
- **AntiAFK** - Prevents AFK kicks with random movements

### 🌍 World
- **Scaffold** - Automatically places blocks under you
- **Tunnel** - Mines a tunnel in the direction you're facing
- **FastPlace** - Removes block placement delay
- **AutoFish** - Automatically fishes for you

### 👁️ Render
- **Xray** - See ores and valuable blocks through walls
- **Fullbright** - Maximum brightness in all areas
- **ESP** - See entities through walls
- **ChestESP** - Highlights chests in render distance
- **ItemESP** - Highlights dropped items with range setting
- **MobESP** - Highlights hostile mobs with configurable range

### ⚙️ Misc
- **Timer** - Speed up or slow down game time

## 🎮 Controls

| Action | Key |
|--------|-----|
| Open/Close GUI | `X` or `Z` |
| Toggle Module | `Single Click` |
| Open Settings | `Double Click` |
| View Info | `INFO Button` |

> Works with both RU and EN keyboard layouts

## 📦 Installation

### Requirements
- Minecraft 1.16.5 Java Edition
- [Fabric Loader](https://fabricmc.net/use/installer/)
- [Fabric API](https://modrinth.com/mod/fabric-api)

### Steps

1. Install **Fabric Loader** for Minecraft 1.16.5
2. Download the latest `uxt-1.0.0.jar` from [Releases](https://github.com/uxuxs82/uxt-client/releases)
3. Place the JAR file in your `.minecraft/mods` folder
4. Make sure **Fabric API** is also in the mods folder
5. Launch Minecraft with Fabric profile

## ⚙️ Module Settings

Access settings with **double-click** on any module:

| Module | Settings | Range |
|--------|----------|-------|
| KillAura | Range | 3.0 - 6.0 |
| Aimbot | Range, FOV | 20-100, 30-180 |
| Speed | Multiplier | 1.0 - 5.0 |
| Step | Height | 0.5 - 2.0 |
| AutoEat | Hunger | 5-19 |
| Timer | Speed | 0.5 - 5.0 |
| ItemESP | Range | 16-64 |
| MobESP | Range | 32-128 |

## 🔨 Building from Source

```bash
git clone https://github.com/uxuxs82/uxt-client.git
cd uxt-client
./gradlew build
```

The compiled JAR will be in `build/libs/uxt-1.0.0.jar`

## 🤝 Compatibility

| Component | Version |
|-----------|---------|
| Minecraft | 1.16.5 Java Edition |
| Mod Loader | Fabric |
| Java | 17+ |
| Platform | Windows, Linux, macOS, Android |

> **Note**: Client-side only. Some modules may be detectable by anti-cheat plugins.

## ⚠️ Disclaimer

This client is for **educational purposes only**. Use responsibly and only on servers where cheats are allowed. I am not responsible for any bans or consequences from using this mod.

## 👨‍💻 Author

**uxuxx**

Built with Fabric and love for the Minecraft community.

## 📄 License

See [LICENSE](LICENSE) file for details.

---

<div align="center">

**[⬆ Back to Top](#)**

Made with 💚 by [uxuxx](https://github.com/uxuxs82)

</div>
