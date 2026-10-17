<div align="center">

![UXT Banner](assets/banner.png)

[![Minecraft](https://img.shields.io/badge/Minecraft-1.16.5-brightgreen.svg)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Fabric-0.14.0-blue.svg)](https://fabricmc.net/)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/github/license/uxuxs82/uxt-client)](LICENSE)
[![Build](https://img.shields.io/github/actions/workflow/status/uxuxs82/uxt-client/build.yml?branch=main)](https://github.com/uxuxs82/uxt-client/actions)

</div>

## Preview

![UXT GUI](assets/gui_preview.png)

## Combat

- **KillAura** - Attack nearby entities
- **Aimbot** - Lock onto targets
- **Velocity** - Reduce knockback
- **AntiKnockback** - Prevent knockback

## Movement

- **Speed** - Move faster
- **Sprint** - Auto sprint forward
- **AutoSprint** - Always sprint
- **NoFall** - No fall damage
- **NoSlow** - No item slowdown
- **Step** - Step up blocks
- **Jesus** - Walk on water
- **Blink** - Freeze server position
- **Freecam** - Fly through blocks

## Player

- **AutoEat** - Eat automatically
- **AutoArmor** - Equip armor
- **AntiFire** - Extinguish fire
- **AntiAFK** - Prevent kicks

## World

- **Scaffold** - Place blocks under you
- **Tunnel** - Mine tunnels
- **FastPlace** - Fast block placement
- **AutoFish** - Auto fishing

## Render

- **Xray** - See ores through walls
- **Fullbright** - Max brightness
- **ESP** - See entities
- **ChestESP** - Highlight chests
- **ItemESP** - Highlight items
- **MobESP** - Highlight mobs

## Misc

- **Timer** - Change game speed
- **HUD** - Show enabled modules, coordinates and FPS

## Controls

- `X` or `Z` - Open GUI
- Single click - Toggle module
- Double click - Open settings
- INFO button - View info

## Config

Module states and settings are saved automatically to `config/uxt-client.cfg` and restored on the next launch.

## Installation

1. Install Fabric Loader for Minecraft 1.16.5
2. Download latest JAR from releases
3. Put in `.minecraft/mods` folder
4. Install Fabric API
5. Launch game

## Module Settings

- KillAura: Range 3.0-6.0
- Aimbot: Range 20-100, FOV 30-180
- Speed: 1.0-5.0
- Step: Height 0.5-2.0
- AutoEat: Hunger 5-19
- Timer: 0.5-5.0
- ItemESP: Range 16-64
- MobESP: Range 32-128

## Build

```bash
git clone https://github.com/uxuxs82/uxt-client.git
cd uxt-client
./gradlew build
```

## Compatibility

- Minecraft 1.16.5
- Fabric Loader
- Java 17+

## Author

uxuxx

## License

See LICENSE file.
