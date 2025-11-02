# PROJECT BRIEF: Dungeon Escape - Text-Based RPG for OOP Teaching

## 🎯 Project Overview

### Purpose
Educational project untuk mengajarkan Design Patterns melalui progressive game development. Mahasiswa akan membangun text-based RPG game sambil menerapkan patterns di setiap tahap.

### Target Audience
- **Level**: Mahasiswa semester 3, Teknik Informatika
- **Prerequisites**: Java basics, 4 pilar OOP, SOLID principles, Iterator pattern
- **Context**: Sudah belajar backend (Spring Boot) di semester sebelumnya
- **Duration**: 4 minggu (Pekan 9-12)

---

## 🎮 Game Concept: "Dungeon Escape"

### Genre
Text-based Roguelike / Endless Runner hybrid

### Core Premise
Player terjebak di dungeon yang terus bergerak. Harus bertahan hidup, mengumpulkan coins, menghindari obstacles, dan mencapai dungeon exit untuk melawan boss dalam turn-based battle.

### Visual Style
Terminal-based ASCII art dengan grid representation:
```
╔════════════════════════════════════════╗
║  DUNGEON ESCAPE - Level 3             ║
╠════════════════════════════════════════╣
║                                        ║
║  ████  ░░░░░░░░  ████  ░░░░  ████    ║
║  ████  ░░P░░░░░  ░░░░  ░G░░  ████    ║
║  ████  ░░░░░S░░  ░░░░  ░░░░  ████    ║
║  ░░░░  ░░░░░░░░  ████  ░░W░  ░D░░    ║
║  ░░C░  ████████  ████  ░░░░  ░░░░    ║
║                                        ║
╠════════════════════════════════════════╣
║  HP: ██████████ 100/100               ║
║  Score: 2450  |  Coins: 12  |  Lvl: 3 ║
╚════════════════════════════════════════╝

Legend: P=Player  S=Spike  G=Goblin  W=Wolf
        C=Coin    D=Dungeon Exit  █=Wall
```

---

## 📚 Learning Progression (Week by Week)

### **WEEK 9: Foundation & Singleton**
**Game State**: No player control yet, NPC auto-moves

**What Works**:
- Game loop (update-render separation)
- NPC auto-movement (like Flappy Bird without input)
- Coin spawning and falling (gravity)
- Collision detection (NPC vs Coins)
- Terminal rendering

**Design Patterns**:
1. **Game Loop Paradigm** (before patterns)
   - Problem: Monolithic loop (update-render mixed)
   - Solution: Separated update() and draw()
   
2. **Singleton Pattern**
   - Problem: Multiple GameManager instances
   - Solution: Single global state manager

**Learning Objectives**:
- Understand paradigm shift (Request-Response → Continuous Loop)
- Understand global state management
- Understand why separation of concerns matters

---

### **WEEK 10: World Building & Object Management**
**Game State**: Still NPC auto-move, focus on spawning system

**What Works**:
- Dynamic obstacle spawning (Spike, Goblin, Wolf)
- Variety of enemy behaviors:
  - Spike: Static, damage on touch
  - Goblin: Patrol pattern (left-right)
  - Wolf: Chase NPC if in range
- Performance optimization for many entities

**Design Patterns**:
1. **Factory Method Pattern**
   - Problem: Hard-coded obstacle creation (switch-case)
   - Solution: Delegated creation via factories
   
2. **Object Pool Pattern**
   - Problem: GC lag from frequent new/delete
   - Solution: Reuse objects instead of creating new

**Learning Objectives**:
- Understand flexible object creation
- Understand performance implications
- Understand memory management in games

---

### **WEEK 11: Interactivity & Events**
**Game State**: ⭐ PLAYER CONTROL INTRODUCED! ⭐

**What Works**:
- WASD keyboard input (real-time movement)
- Player-controlled character (not auto anymore!)
- Coin collection (player walks over coin)
- Damage system (player hit by enemy)
- Event propagation (damage → HUD update, sound play, etc)

**Design Patterns**:
1. **Command Pattern**
   - Problem: Hard-coded input handling (if-else hell)
   - Solution: Commands as objects, remappable keys
   
2. **Observer Pattern**
   - Problem: Player class tightly coupled to all systems
   - Solution: Event system, publish-subscribe

**Learning Objectives**:
- Understand decoupling input from action
- Understand event-driven architecture
- Understand one-to-many communication

---

### **WEEK 12: Complexity & State Management**
**Game State**: Full game with difficulty scaling and boss battle

**What Works**:
- Difficulty system (spawn rate increases with score)
- Game state machine:
  - MainMenu → Playing → BossBattle → GameOver/Victory
- Boss encounter at dungeon exit
- Turn-based battle system (separate from exploration)
- Save/load game state (JSON persistence)

**Design Patterns**:
1. **Strategy Pattern**
   - Problem: Hard-coded difficulty algorithms (switch-case)
   - Solution: Pluggable difficulty strategies
   
2. **State Pattern**
   - Problem: God switch for game states (menu/playing/battle)
   - Solution: State objects with transitions

**Learning Objectives**:
- Understand algorithm encapsulation
- Understand state machine design
- Understand when to use which behavioral pattern

---

## 🛠️ Technical Specifications

### Tech Stack
- **Language**: Java (JDK 11+)
- **Build**: No build tool (plain javac, for simplicity)
- **Dependencies**: Standard library ONLY
  - No LibGDX, no external game engines
  - No GUI frameworks (terminal only)
  - No JSON libraries (use simple file I/O or built-in parsing)
- **Display**: Terminal/Console (System.out, ANSI codes)
- **Input**: System.in with Scanner or Console
- **Persistence**: Plain text files (JSON format, manual parsing)

### Performance Targets
- **Frame Rate**: 60 FPS (16.67ms per frame)
- **Input Latency**: < 50ms
- **Memory**: Stable (no memory leaks)
- **GC Pauses**: < 10ms after optimization

### Code Quality Standards
- **Max Method Length**: 30 lines (except where demonstrating anti-pattern)
- **Max Class Size**: 200 lines
- **Test Coverage**: 70%+ for logic classes
- **Documentation**: Javadoc for public APIs

---

## 🌳 Git Branching Strategy

### Structure
```
main (skeleton)
│
├── week-09-foundation/
│   ├── 09-00-without-game-loop       ❌ Demo: monolithic
│   ├── 09-01-with-game-loop          ✅ Solution: separated
│   ├── 09-02-without-singleton       ❌ Demo: multiple instances
│   ├── 09-03-with-singleton          ✅ Solution: single instance
│   └── 09-analysis/                  📊 Comparison & metrics
│
├── week-10-world-building/
│   ├── 10-01-hardcoded-spawning      ❌ Demo: switch-case
│   ├── 10-02-with-factory            ✅ Solution: factories
│   ├── 10-03-gc-performance-issue    ❌ Demo: GC lag
│   ├── 10-04-with-pool               ✅ Solution: pooling
│   └── 10-analysis/
│
├── week-11-interactivity/
│   ├── 11-01-hardcoded-input         ❌ Demo: if-else hell
│   ├── 11-02-with-command            ✅ Solution: commands
│   ├── 11-03-tight-coupling-events   ❌ Demo: coupled systems
│   ├── 11-04-with-observer           ✅ Solution: events
│   └── 11-analysis/
│
├── week-12-complexity/
│   ├── 12-01-hardcoded-difficulty    ❌ Demo: switch difficulty
│   ├── 12-02-with-strategy           ✅ Solution: strategies
│   ├── 12-03-god-switch-states       ❌ Demo: FSM switch
│   ├── 12-04-with-state              ✅ Solution: state objects
│   └── 12-analysis/
│
└── release/
    └── final-game                     🎮 Complete integrated game
```

### Branch Naming Convention
- `XX-YY-description` where:
  - XX = week number (09, 10, 11, 12)
  - YY = sequence (00, 01, 02, ...)
  - description = kebab-case short name

### Branch Types
1. **Problem Branches** (`-without-`, `hardcoded-`, etc)
   - Intentionally bad design
   - Demonstrate pain points
   - Include bugs and anti-patterns
   
2. **Solution Branches** (`-with-`, etc)
   - Clean implementation
   - Apply design pattern
   - Fix problems from previous branch
   
3. **Analysis Branches** (`-analysis`)
   - Comparison tables
   - Metrics (LOC, complexity, performance)
   - When-to-use guidelines
   - Migration guides

---

## 📦 File Structure (Final State)

```
dungeon-escape/
├── src/
│   ├── Main.java
│   ├── GameEngine.java
│   ├── entities/
│   │   ├── Player.java
│   │   ├── NPC.java
│   │   ├── Coin.java
│   │   └── obstacles/
│   │       ├── Obstacle.java (interface)
│   │       ├── Spike.java
│   │       ├── Goblin.java
│   │       ├── Wolf.java
│   │       └── Boss.java
│   ├── managers/
│   │   ├── GameManager.java (Singleton)
│   │   ├── InputManager.java
│   │   └── StateManager.java
│   ├── states/
│   │   ├── GameState.java (interface)
│   │   ├── MainMenuState.java
│   │   ├── PlayingState.java
│   │   ├── BattleState.java
│   │   └── GameOverState.java
│   ├── commands/
│   │   ├── Command.java (interface)
│   │   ├── MoveCommand.java
│   │   ├── AttackCommand.java
│   │   └── UseItemCommand.java
│   ├── factories/
│   │   ├── ObstacleFactory.java (abstract)
│   │   ├── SpikeFactory.java
│   │   ├── GoblinFactory.java
│   │   └── WolfFactory.java
│   ├── pool/
│   │   ├── ObjectPool.java
│   │   └── Poolable.java (interface)
│   ├── strategies/
│   │   ├── DifficultyStrategy.java (interface)
│   │   ├── EasyDifficulty.java
│   │   ├── MediumDifficulty.java
│   │   └── HardDifficulty.java
│   ├── observers/
│   │   ├── GameObserver.java (interface)
│   │   ├── HUDObserver.java
│   │   ├── SoundObserver.java
│   │   └── AchievementObserver.java
│   ├── utils/
│   │   ├── GridRenderer.java
│   │   ├── CollisionDetector.java
│   │   └── JsonParser.java
│   └── logic/
│       ├── GameLogic.java
│       ├── BattleLogic.java
│       └── PhysicsEngine.java
│
├── test/
│   ├── entities/
│   ├── logic/
│   └── patterns/
│
├── resources/
│   └── save.json
│
└── docs/
    ├── DESIGN.md
    ├── PATTERNS.md
    └── API.md
```

---

## 🎯 Key Pedagogical Principles

### 1. Problem-First Approach
Setiap pattern HARUS didahului dengan demonstration masalah nyata.
- ❌ Show bad design first
- 📊 Measure pain (metrics, bugs)
- ✅ Then show pattern as solution

### 2. Real-World Justification
Setiap demonstration harus explain KENAPA ini penting di dunia nyata:
- Industry practices
- Production scenarios
- Performance implications
- Team collaboration issues

### 3. Progressive Complexity
Game features unlock sesuai patterns:
- Week 9: NPC only (foundation)
- Week 10: Still NPC (spawning focus)
- Week 11: Player control introduced!
- Week 12: Full game features

### 4. Hands-On Learning
Mahasiswa harus:
- Run problematic code
- Observe bugs
- Measure performance
- Implement solutions
- Compare before/after

### 5. Code Quality Focus
Meskipun game sederhana, code harus:
- Well-tested
- Well-documented
- Maintainable
- Professional standard

---

## 📊 Success Metrics

### For Students
- Can explain WHY pattern is needed (not just HOW)
- Can identify when to use which pattern
- Can articulate trade-offs
- Can implement pattern from scratch

### For Code
- All tests passing
- No memory leaks
- Stable 60 FPS
- No GC pauses > 10ms

### For Teaching
- Clear problem demonstration
- Measurable improvements
- Real-world relevance
- Student engagement

---

## 🚀 Implementation Philosophy

### For Problem Branches
- **Make it obviously bad**: Don't be subtle
- **Include bugs**: Intentional for demonstration
- **Add comments**: `// ❌ PROBLEM: ...`
- **Make it painful**: Students should feel the pain
- **Keep it simple**: One problem at a time

### For Solution Branches
- **Clean implementation**: Professional quality
- **Well-tested**: 70%+ coverage
- **Well-documented**: Clear javadocs
- **Incremental refactor**: Clear migration path
- **Trade-offs noted**: Nothing is perfect

### For Analysis Branches
- **Data-driven**: Metrics, not opinions
- **Comparative**: Clear before/after
- **Actionable**: When-to-use guidelines
- **Teaching-focused**: FAQ, common mistakes

---

## ⚠️ Common Pitfalls to Avoid

### 1. Over-Engineering
- ❌ Don't add features not needed for pattern demo
- ❌ Don't use external libraries
- ✅ Keep it simple and focused

### 2. Under-Engineering
- ❌ Don't make code too trivial
- ❌ Don't skip error handling
- ✅ Make it realistic but focused

### 3. Losing Focus
- ❌ Don't let game complexity overshadow patterns
- ❌ Don't add features for "coolness"
- ✅ Every feature must serve pedagogical purpose

### 4. Skipping Justification
- ❌ Don't just say "this is better"
- ❌ Don't skip real-world context
- ✅ Always explain WHY with concrete examples

---

## 📝 Documentation Requirements

### Each Branch Must Have
1. **SCENARIO.md**
   - Learning objective
   - Context and background
   - Problems demonstrated
   - Real-world justification
   - Teaching notes

2. **GUIDELINE.md**
   - File structure
   - Implementation requirements
   - Key classes and methods
   - Testing requirements
   - Demonstration scripts

3. **PROMPT.md**
   - Copy-paste ready for Claude Code
   - Complete context
   - Clear requirements
   - Expected outputs

### Each Week Must Have
1. **OVERVIEW.md**
   - Week goals
   - Pattern summary
   - Branch roadmap
   - Prerequisites

2. **ANALYSIS.md** (in analysis branch)
   - Comparison tables
   - Performance metrics
   - When-to-use guide
   - Migration guide
   - Common mistakes

---

## 🎓 Final Notes

This is an ambitious project that balances:
- **Game development** (fun, engaging)
- **Software engineering** (patterns, principles)
- **Education** (progressive, justified)

The goal is NOT to build the best game, but to build the best LEARNING EXPERIENCE through game development.

Every decision should ask: "Does this help students understand patterns better?"

Good luck! 🚀
