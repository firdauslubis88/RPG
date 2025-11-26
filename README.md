# Dungeon Escape - Java RPG Game for Design Pattern Learning

## Overview

**Dungeon Escape** adalah game RPG berbasis console yang dikembangkan sebagai materi pembelajaran **Object-Oriented Programming** dan **Design Patterns** di Java. Project ini menggunakan pendekatan **Problem-Solution** dimana setiap minggu memperkenalkan masalah (anti-pattern) dan kemudian solusinya (design pattern).

```
  ╔════════════════════════════════════════╗
  ║         DUNGEON ESCAPE                 ║
  ║   A Design Pattern Learning Game       ║
  ╚════════════════════════════════════════╝
```

## Quick Start

```bash
# Option 1: Use batch files (Windows)
build.bat           # Compile
test-gameplay.bat   # Run game

# Option 2: Manual compile
javac -d bin -sourcepath src src/Main.java
cd bin && java Main
```

## Gameplay

1. **Select Difficulty** (1-3): EASY, NORMAL, HARD
2. **Select Level** (1-4): Dungeon, Forest, Castle, Boss Arena
3. **Controls**: WASD + Enter to move, Q to quit
4. **Goal**: Reach the exit (`D`) to fight the boss!

### Game Flow (Game State Pattern)
```
MENU → PLAYING → BATTLE → VICTORY/DEFEAT
```

## Features

- **Game State Pattern** for complete game flow management
- 4 unique levels with different maps, enemies, and music
- Turn-based boss battle system with State Pattern AI
- Achievement system with Observer Pattern
- Multiple difficulty modes with Strategy Pattern
- Object pooling for performance optimization

---

## Design Patterns Covered

### Week 9: Foundation Patterns
| Branch | Pattern | Description |
|--------|---------|-------------|
| `09-00` | - | Monolithic code (PROBLEM) |
| `09-01` | Game Loop | Fixed timestep, 60 FPS |
| `09-02` | - | Multiple instances issue |
| `09-03` | Singleton | GameManager single instance |

### Week 10: Creational Patterns
| Branch | Pattern | Description |
|--------|---------|-------------|
| `10-01` | - | Hardcoded spawning (PROBLEM) |
| `10-02` | Factory | ObstacleFactory for enemy creation |
| `10-03` | - | GC performance issues |
| `10-04` | Object Pool | Reuse obstacle instances |

### Week 11: Behavioral Patterns I
| Branch | Pattern | Description |
|--------|---------|-------------|
| `11-01` | - | Hardcoded input (PROBLEM) |
| `11-02` | Command | Configurable key bindings |
| `11-03` | - | Tight coupling issue |
| `11-04` | Observer | EventBus for decoupled systems |

### Week 12: Behavioral Patterns II
| Branch | Pattern | Description |
|--------|---------|-------------|
| `12-01` | - | Hardcoded difficulty (PROBLEM) |
| `12-02` | Strategy | DifficultyStrategy for game modes |
| `12-03` | - | Complex boss AI conditionals |
| `12-04` | State | Boss behavior state machine |
| `12-05` | Game State | Game flow state machine (Menu→Playing→Battle→End) |

### Week 13: Structural Patterns
| Branch | Pattern | Description |
|--------|---------|-------------|
| `13-01` | - | Code duplication in level loading |
| `13-02` | Template Method | LevelLoader algorithm skeleton |
| `13-03` | - | Tightly coupled battle system |
| `13-04` | Facade | BattleFacade for subsystems |

---

## Project Structure

```
rpg/
├── src/
│   ├── Main.java                 # Entry point (uses GameStateContext)
│   │
│   ├── engine/                   # Core game engine
│   │   ├── GameEngine.java       # Game loop (60 FPS)
│   │   ├── GameLogic.java        # Game rules & collision
│   │   ├── HUD.java              # UI display (Observer)
│   │   ├── WorldController.java  # Obstacle management
│   │   └── PerformanceMonitor.java # GC & frame monitoring
│   │
│   ├── gamestate/                # Game State Pattern
│   │   ├── GameState.java        # State interface
│   │   ├── GameStateContext.java # State machine context
│   │   ├── MenuState.java        # Menu selection state
│   │   ├── PlayingState.java     # Dungeon exploration state
│   │   ├── BattleState.java      # Boss battle state
│   │   ├── VictoryState.java     # Win state
│   │   └── DefeatState.java      # Lose state
│   │
│   ├── battle/                   # Battle system
│   │   ├── BattleFacade.java     # Facade Pattern
│   │   ├── BattleSystem.java     # Turn-based combat
│   │   ├── BattleContext.java    # State Pattern context
│   │   └── *State.java           # State Pattern (boss AI)
│   │
│   ├── commands/                 # Command Pattern
│   │   ├── Command.java          # Command interface
│   │   └── Move*Command.java     # Movement commands
│   │
│   ├── difficulty/               # Strategy Pattern
│   │   ├── DifficultyStrategy.java
│   │   └── *Difficulty.java      # EASY, NORMAL, HARD
│   │
│   ├── entities/                 # Game entities
│   │   ├── Entity.java           # Base entity interface
│   │   ├── Player.java           # Player character
│   │   ├── Coin.java             # Collectibles
│   │   ├── DungeonExit.java      # Level exit point
│   │   └── GameManager.java      # Singleton Pattern
│   │
│   ├── events/                   # Observer Pattern
│   │   ├── EventBus.java         # Event dispatcher (Singleton)
│   │   ├── GameEvent.java        # Event interface
│   │   ├── GameEventListener.java # Observer interface
│   │   └── *Event.java           # Concrete events
│   │
│   ├── factories/                # Factory Pattern
│   │   └── ObstacleFactory.java  # Creates Spike/Goblin/Wolf
│   │
│   ├── input/                    # Input handling
│   │   └── InputHandler.java     # Command Pattern invoker
│   │
│   ├── level/                    # Template Method Pattern
│   │   ├── LevelLoader.java      # Abstract template class
│   │   ├── DungeonLevelLoader.java
│   │   ├── ForestLevelLoader.java
│   │   ├── CastleLevelLoader.java
│   │   └── BossArenaLoader.java
│   │
│   ├── obstacles/                # Enemy types
│   │   ├── Obstacle.java         # Interface
│   │   ├── Spike.java            # Static trap
│   │   ├── Goblin.java           # Patrol enemy
│   │   └── Wolf.java             # Chase enemy
│   │
│   ├── pools/                    # Object Pool Pattern
│   │   └── ObstaclePool.java     # Reuses obstacle instances
│   │
│   ├── systems/                  # Game systems
│   │   ├── SoundSystem.java      # Audio (Observer + Music)
│   │   └── AchievementSystem.java # Achievements (Observer)
│   │
│   ├── ui/                       # User interface
│   │   └── MainMenu.java         # Difficulty & level selection
│   │
│   ├── utils/                    # Utilities
│   │   └── GridRenderer.java     # Console rendering with ANSI
│   │
│   └── world/                    # World/Map system
│       ├── GameMap.java          # Abstract map class
│       ├── DungeonMap.java       # Static map manager
│       ├── DungeonMapLayout.java # Dark dungeon layout
│       ├── ForestMapLayout.java  # Forest layout
│       ├── CastleMapLayout.java  # Castle layout
│       └── BossArenaLayout.java  # Arena layout
│
├── bin/                          # Compiled classes
├── build.bat                     # Compile script
├── test-gameplay.bat             # Run game script
├── assets/
│   └── music/                    # Background music (WAV files)
└── docs/                         # Week-by-week documentation
    ├── week-*-overview.md        # Weekly overviews
    ├── *-problem.md              # Problem documentation
    └── *-solution.md             # Solution documentation
```

---

## Level Details

| Level | Wall | Floor | Enemies | Music |
|-------|------|-------|---------|-------|
| Dark Dungeon | `#` | `.` | `g` goblin, `w` wolf, `^` spike | dark_dungeon_ambient.wav |
| Enchanted Forest | `T` | `,` | `S` spirit, `W` wolf, `*` thorn | enchanted_forest_theme.wav |
| Haunted Castle | `\|` | `:` | `K` knight, `G` ghost, `X` trap | haunted_castle_orchestra.wav |
| Boss Arena | `=` | ` ` | Direct boss battle | epic_boss_battle.wav |

---

## Design Pattern Summary

```
Week 9:  Game Loop ────► Singleton
Week 10: Factory ──────► Object Pool
Week 11: Command ──────► Observer
Week 12: Strategy ─────► State ────► Game State
Week 13: Template Method ► Facade
```

**Total: 11 Design Patterns**

### Pattern Relationships

```
                    ┌─────────────────┐
                    │   GameEngine    │
                    │   (Game Loop)   │
                    └────────┬────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│  InputHandler │   │  GameLogic    │   │  LevelLoader  │
│  (Command)    │   │               │   │  (Template)   │
└───────────────┘   └───────┬───────┘   └───────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│  EventBus     │   │ BattleFacade  │   │ WorldController│
│  (Observer)   │   │  (Facade)     │   │               │
└───────────────┘   └───────┬───────┘   └───────┬───────┘
                            │                   │
                            ▼                   ▼
                    ┌───────────────┐   ┌───────────────┐
                    │ BattleSystem  │   │ ObstaclePool  │
                    │  (State)      │   │ (Object Pool) │
                    └───────────────┘   └───────────────┘
```

---

## For Educators

### Teaching Approach
Each week follows the **Problem → Solution** methodology:
1. Show the problematic code (anti-pattern branch)
2. Identify issues through class discussion
3. Introduce the design pattern concept
4. Implement the solution together
5. Compare before/after metrics

### Documentation Resources
- `docs/` - Week-by-week documentation with problem/solution pairs

### Assessment
See individual week documentation for rubrics and exercises.

---

## For Students

### Learning Path
1. Start with Week 9 (foundation patterns)
2. Progress through each week sequentially
3. Study the **PROBLEM** branch first (odd-numbered: 01, 03)
4. Then study the **SOLUTION** branch (even-numbered: 02, 04)
5. Compare the changes and understand the trade-offs

### Practice Exercises
- Modify enemy behaviors using different patterns
- Add new power-ups with Observer events
- Create new levels using Template Method
- Implement a save/load system with Singleton

---

## Building Javadocs

```bash
# Generate Javadocs for all packages
javadoc -d docs/javadoc -sourcepath src \
  -subpackages battle:commands:difficulty:entities:events:factories:input:level:obstacles:pools:systems:ui:utils:world \
  -windowtitle "Dungeon Escape API" \
  -doctitle "Dungeon Escape - Design Pattern Learning Game" \
  src/*.java
```

Or on Windows:
```cmd
javadoc -d docs\javadoc -sourcepath src -subpackages battle;commands;difficulty;entities;events;factories;input;level;obstacles;pools;systems;ui;utils;world -windowtitle "Dungeon Escape API" src\*.java
```

---

## Requirements

- **Java**: JDK 8 or higher
- **Platform**: Windows (uses ANSI escape codes for console)
- **Terminal**: Windows Terminal recommended for best display
- **Audio**: WAV files in `assets/music/` for background music (optional)

---

## References

### Books
- "Design Patterns: Elements of Reusable OO Software" (Gang of Four)
- "Head First Design Patterns" (O'Reilly)
- "Game Programming Patterns" by Robert Nystrom (FREE: gameprogrammingpatterns.com)
- "Effective Java" by Joshua Bloch

### Websites
- Refactoring Guru: https://refactoring.guru/design-patterns
- Game Programming Patterns: https://gameprogrammingpatterns.com/

---

## Version History

| Week | Main Feature | Design Patterns |
|------|--------------|-----------------|
| 9 | Game Loop & Global State | Game Loop, Singleton |
| 10 | Enemy Spawning System | Factory, Object Pool |
| 11 | Input & Event System | Command, Observer |
| 12 | Difficulty & Boss AI | Strategy, State, Game State |
| 13 | Level Loading & Battle | Template Method, Facade |

---

## License

Educational use only. Created for OOP/Design Pattern courses.

---

**Version**: v0.1
**Status**: ✅ Complete (Week 13)
**Last Updated**: November 2025
**Patterns Implemented**: 11 Design Patterns across 5 weeks
