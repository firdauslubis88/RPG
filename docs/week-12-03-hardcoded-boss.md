# Week 12-03: Hardcoded Boss AI (ANTI-PATTERN)

**Branch**: `12-03-hardcoded-boss`

**Compilation**: `javac -d bin/12-03-hardcoded-boss src/*.java src/**/*.java`

**Run**: `java -cp bin/12-03-hardcoded-boss Main`

---

## Overview

Week 12-03 demonstrates the **ANTI-PATTERN** of hardcoded boss AI with giant if/else chains and scattered state logic. This branch adds a turn-based boss battle system when the player reaches the dungeon exit, but implements it using hardcoded HP thresholds and duplicated logic.

This anti-pattern will be refactored in Week 12-04 using the **State Pattern**.

---

## What's New in Week 12-03

### 1. Turn-Based Boss Battle System

When the player reaches the dungeon exit at position (23, 23), the game transitions to a turn-based boss battle:

- **Strategic Rock-Paper-Scissors Combat**: Player must predict boss actions and choose countering moves
- **4 Player Actions**: ATTACK, DEFEND, MAGIC, COUNTER (+ RUN to flee)
- **Dynamic Counter Relationships**: Rules change based on boss state (HP percentage)
- **Boss AI with 4 States**: NORMAL, ANGRY, DEFENSIVE, ENRAGED

### 2. DEMO Difficulty Mode

Added a special DEMO difficulty for testing and demonstration:

- Very slow enemy spawning (5 seconds interval)
- Boss only uses DEFEND action (guaranteed player victory)
- Perfect for testing game flow without dying

### 3. New Files

**New Battle System**:
- `src/battle/BattleSystem.java` - Turn-based battle with hardcoded boss AI (ANTI-PATTERN)

**New Difficulty**:
- `src/difficulty/DemoDifficulty.java` - DEMO difficulty strategy

**Modified Files**:
- `src/GameLogic.java` - Trigger boss battle at dungeon exit, pass DEMO mode flag
- `src/ui/MainMenu.java` - Add DEMO difficulty option (choice 0)

---

## Boss Battle Mechanics

### Turn Structure

Each turn follows this flow:

1. **Display State**: Show boss state, player HP, boss HP
2. **Player Choice**: Choose from ATTACK, DEFEND, MAGIC, COUNTER, RUN
3. **Boss AI Decision**: Boss chooses action based on current state
4. **Simultaneous Resolution**: Actions resolve using state-specific rules
5. **HP Update**: Apply damage to player and/or boss
6. **State Check**: Boss may transition to new state based on HP

### Boss States and AI (ANTI-PATTERN!)

The boss has 4 states determined by hardcoded HP thresholds:

#### 😐 NORMAL State (HP > 75%)
**AI Behavior**:
```java
// Random 25% each action
int choice = random.nextInt(4);
switch (choice) {
    case 0: return ATTACK;
    case 1: return DEFEND;
    case 2: return MAGIC;
    case 3: return COUNTER;
}
```

**Counter Relationship Rules**:
- ATTACK > MAGIC (25 damage)
- MAGIC > DEFEND (30 damage)
- DEFEND > ATTACK (15 damage)
- COUNTER > ALL (40 damage, risky!)
- Same action = CLASH (both take 10 damage)

#### 😠 ANGRY State (75% ≥ HP > 50%)
**AI Behavior**:
```java
// 50% ATTACK, 30% COUNTER, 20% MAGIC
int roll = random.nextInt(100);
if (roll < 50) return ATTACK;
else if (roll < 80) return COUNTER;
else return MAGIC;
```

**Counter Relationship Rules**:
- ATTACK > DEFEND (35 damage - buffed!)
- MAGIC > COUNTER (35 damage)
- DEFEND > MAGIC (20 damage)
- COUNTER > ATTACK (50 damage - dangerous!)
- Same action = CLASH (both take 15 damage)

#### 🛡️ DEFENSIVE State (50% ≥ HP > 25%)
**AI Behavior**:
```java
// 60% DEFEND, 30% COUNTER, 10% MAGIC
int roll = random.nextInt(100);
if (roll < 60) return DEFEND;
else if (roll < 90) return COUNTER;
else return MAGIC;
```

**Counter Relationship Rules**:
- MAGIC > DEFEND (40 damage - best choice!)
- DEFEND > ATTACK (25 damage)
- COUNTER > MAGIC (45 damage)
- ATTACK > COUNTER (30 damage)
- Same action = CLASH (both take 12 damage)

#### 😡 ENRAGED State (HP ≤ 25%)
**AI Behavior**:
```java
// 70% ATTACK, 20% MAGIC, 10% COUNTER
int roll = random.nextInt(100);
if (roll < 70) return ATTACK;
else if (roll < 90) return MAGIC;
else return COUNTER;
```

**Counter Relationship Rules**:
- DEFEND > ATTACK (35 damage - defend is key!)
- ATTACK > MAGIC (45 damage)
- MAGIC > COUNTER (40 damage)
- COUNTER > DEFEND (60 damage - risky!)
- Same action = CLASH (both take 20 damage)

---

## ANTI-PATTERN Demonstration

### Problem 1: Hardcoded HP Thresholds

```java
// ❌ ANTI-PATTERN: Magic numbers scattered everywhere!
private String getBossState() {
    float hpPercent = (float) bossHp / bossMaxHp;

    if (hpPercent > 0.75f) {
        return "😐 NORMAL - Balanced combat";
    } else if (hpPercent > 0.50f) {
        return "😠 ANGRY - Aggressive attacks!";
    } else if (hpPercent > 0.25f) {
        return "🛡️ DEFENSIVE - Shields up!";
    } else {
        return "😡 ENRAGED - Chaos unleashed!";
    }
}
```

**Issues**:
- Hardcoded thresholds (0.75, 0.50, 0.25) repeated across multiple methods
- No compile-time safety
- Difficult to add new states or change thresholds

### Problem 2: Giant If/Else Chains for AI

```java
// ❌ ANTI-PATTERN: Giant method with duplicated if/else structure!
private String bossTurn() {
    if (isDemoMode) {
        return DEFEND;
    }

    float hpPercent = (float) bossHp / bossMaxHp;

    if (hpPercent > 0.75f) {
        // NORMAL state AI logic
        int choice = random.nextInt(4);
        switch (choice) { ... }
    } else if (hpPercent > 0.50f) {
        // ANGRY state AI logic
        int roll = random.nextInt(100);
        if (roll < 50) return ATTACK;
        ...
    } else if (hpPercent > 0.25f) {
        // DEFENSIVE state AI logic
        ...
    } else {
        // ENRAGED state AI logic
        ...
    }
}
```

**Issues**:
- Giant method handling all states
- Duplicated threshold checks
- Cannot easily test individual state behaviors

### Problem 3: Scattered State Logic

```java
// ❌ ANTI-PATTERN: Separate resolve method for each state!
private void resolveActions(String playerAction, String bossAction) {
    float hpPercent = (float) bossHp / bossMaxHp;

    // YET ANOTHER set of hardcoded thresholds!
    if (hpPercent > 0.75f) {
        resolveNormalState(playerAction, bossAction);
    } else if (hpPercent > 0.50f) {
        resolveAngryState(playerAction, bossAction);
    } else if (hpPercent > 0.25f) {
        resolveDefensiveState(playerAction, bossAction);
    } else {
        resolveEnragedState(playerAction, bossAction);
    }
}

// ❌ ANTI-PATTERN: Giant methods for each state's rules!
private void resolveNormalState(String playerAction, String bossAction) {
    System.out.println("📘 NORMAL STATE RULES:");
    System.out.println("   ATTACK > MAGIC | MAGIC > DEFEND | DEFEND > ATTACK");
    // ... 50+ lines of hardcoded if/else for counter relationships
}

private void resolveAngryState(String playerAction, String bossAction) {
    // ... Another 50+ lines of duplicated structure
}

private void resolveDefensiveState(String playerAction, String bossAction) {
    // ... Another 50+ lines of duplicated structure
}

private void resolveEnragedState(String playerAction, String bossAction) {
    // ... Another 50+ lines of duplicated structure
}
```

**Issues**:
- Code duplication across 4 similar methods
- Each state's logic is scattered (AI in `bossTurn()`, rules in `resolveXxxState()`)
- Difficult to maintain consistency

### Problem 4: Violates Open/Closed Principle

To add a new boss state (e.g., "BERSERK" at 10% HP):

1. Add new threshold to `getBossState()` (modify existing method)
2. Add new if/else branch to `bossTurn()` (modify existing method)
3. Add new if/else branch to `resolveActions()` (modify existing method)
4. Create new `resolveBerserkState()` method (add new method)

**Must modify existing code in 3 places** - violates Open/Closed Principle!

---

## Design Patterns Used

### ✅ Patterns Kept from Previous Weeks

1. **Observer Pattern** (Week 11-04): Event system for game events
2. **Command Pattern** (Week 11-02): Input handling with key bindings
3. **Strategy Pattern** (Week 12-02): Difficulty system (now includes DEMO)
4. **Object Pool Pattern** (Week 10-04): Obstacle reuse
5. **Factory Pattern** (Week 10-02): Obstacle creation
6. **Singleton Pattern** (Week 09-03): GameManager and EventBus

### ❌ Anti-Pattern Demonstrated

**Hardcoded State Logic**: Boss AI uses giant if/else chains with hardcoded HP thresholds instead of proper State Pattern.

---

## Game Flow

### Victory Condition
Player reaches dungeon exit (23, 23) and defeats the boss in turn-based battle.

### Defeat Conditions
1. Player HP reaches 0 during exploration (hit by obstacles)
2. Player HP reaches 0 during boss battle

### Escape Option
Player can RUN from boss battle, but will be pushed back from the exit.

---

## How to Play

### Controls (Exploration Phase)
- `W` - Move Up
- `S` - Move Down
- `A` - Move Left
- `D` - Move Right
- `Q` - Quit Game

### Battle Commands
1. **ATTACK**: Standard damage, beats MAGIC in most states
2. **DEFEND**: Block attacks, beats ATTACK in most states
3. **MAGIC**: Piercing damage, beats DEFEND in most states
4. **COUNTER**: Risky high damage, can beat ALL or backfire
5. **RUN**: Flee from battle (pushes player back from exit)

### Strategy Tips
- **Watch the boss state**: Rules change with each state transition
- **NORMAL state**: COUNTER is risky but powerful
- **ANGRY state**: Boss favors ATTACK, use DEFEND
- **DEFENSIVE state**: Boss favors DEFEND, use MAGIC
- **ENRAGED state**: Boss favors ATTACK, use DEFEND heavily
- **DEMO mode**: Boss only defends, experiment freely!

---

## DEMO Mode Testing

The DEMO difficulty is perfect for testing:

```bash
# Compile
javac -d bin/12-03-hardcoded-boss src/*.java src/**/*.java

# Run and select "0. DEMO"
java -cp bin/12-03-hardcoded-boss Main
```

DEMO mode features:
- Very slow enemy spawning (easy to navigate)
- Boss only uses DEFEND (guaranteed victory)
- Test battle system without risk of losing

---

## Teaching Points

### What This Branch Demonstrates

1. **Working but Inflexible Code**: The boss AI works, but is hard to maintain
2. **Code Duplication**: Similar logic repeated across multiple methods
3. **Violation of OCP**: Must modify existing code to add new states
4. **Magic Numbers**: Hardcoded thresholds (0.75, 0.50, 0.25) everywhere
5. **Scattered Concerns**: State logic split between AI decision and rule resolution

### Why This is an Anti-Pattern

- **Hard to extend**: Adding "BERSERK" state requires modifying 3 methods
- **Hard to test**: Cannot easily unit test individual states
- **Hard to maintain**: Threshold changes must be replicated in multiple places
- **Brittle**: Easy to introduce bugs when modifying one state affects others

### What We'll Fix in Week 12-04

The State Pattern will solve these problems by:

1. **Encapsulating state behavior**: Each state is a separate class
2. **Eliminating if/else chains**: Polymorphism replaces conditionals
3. **Following OCP**: New states added without modifying existing code
4. **Centralizing state logic**: AI and rules together in state class

---

## Code Structure

```
src/
├── Main.java                          # Entry point with MainMenu
├── GameEngine.java                    # Game loop
├── GameLogic.java                     # ✨ NEW: Trigger boss battle at exit
├── HUD.java                           # Display game stats
├── PerformanceMonitor.java            # Track performance
├── WorldController.java               # Spawn and update obstacles
│
├── battle/
│   └── BattleSystem.java              # ✨ NEW: Turn-based boss battle (ANTI-PATTERN!)
│
├── commands/
│   ├── Command.java                   # Command interface
│   ├── MoveUpCommand.java             # Move up command
│   ├── MoveDownCommand.java           # Move down command
│   ├── MoveLeftCommand.java           # Move left command
│   ├── MoveRightCommand.java          # Move right command
│   └── QuitCommand.java               # Quit command
│
├── difficulty/
│   ├── DifficultyStrategy.java        # Strategy interface
│   ├── DemoDifficulty.java            # ✨ NEW: DEMO difficulty (boss only defends)
│   ├── EasyDifficulty.java            # Easy difficulty
│   ├── NormalDifficulty.java          # Normal difficulty
│   └── HardDifficulty.java            # Hard difficulty
│
├── entities/
│   ├── Entity.java                    # Entity interface
│   ├── Player.java                    # Player entity
│   ├── Coin.java                      # Coin entity
│   ├── DungeonExit.java               # Exit entity (triggers boss)
│   ├── GameManager.java               # Singleton game state
│   └── NPC.java                       # (unused in this branch)
│
├── events/
│   ├── GameEvent.java                 # Event base class
│   ├── GameEventListener.java         # Observer interface
│   ├── EventBus.java                  # Event bus singleton
│   ├── DamageTakenEvent.java          # Damage event
│   ├── CoinCollectedEvent.java        # Coin event
│   ├── AchievementUnlockedEvent.java  # Achievement event
│   └── GameTimeEvent.java             # Time event
│
├── factories/
│   ├── ObstacleFactory.java           # Factory interface
│   ├── SpikeFactory.java              # Spike factory
│   ├── GoblinFactory.java             # Goblin factory
│   └── WolfFactory.java               # Wolf factory
│
├── input/
│   └── InputHandler.java              # Input handler with Command Pattern
│
├── obstacles/
│   ├── Obstacle.java                  # Obstacle base class
│   ├── Spike.java                     # Spike obstacle
│   ├── Goblin.java                    # Goblin obstacle
│   └── Wolf.java                      # Wolf obstacle
│
├── pools/
│   └── ObstaclePool.java              # Object pool for obstacles
│
├── systems/
│   ├── SoundSystem.java               # Sound system (observer)
│   └── AchievementSystem.java         # Achievement system (observer)
│
├── ui/
│   └── MainMenu.java                  # ✨ MODIFIED: Add DEMO option
│
├── utils/
│   └── GridRenderer.java              # Render game grid
│
└── world/
    └── DungeonMap.java                # Dungeon map data
```

---

## Comparison: 12-03 (ANTI-PATTERN) vs 12-04 (State Pattern)

| Aspect | Week 12-03 (This Branch) | Week 12-04 (Next Week) |
|--------|--------------------------|------------------------|
| **State Determination** | Giant if/else with hardcoded HP % | Polymorphic state objects |
| **Boss AI Logic** | Scattered across `bossTurn()` method | Encapsulated in `BossState.chooseAction()` |
| **Counter Rules** | 4 separate `resolveXxxState()` methods | Each state has `resolveAction()` method |
| **Adding New State** | Modify 3+ existing methods | Create 1 new state class |
| **Testability** | Hard to test individual states | Easy to unit test each state |
| **Maintainability** | Threshold changes affect multiple places | Change only in state class |
| **OCP Compliance** | ❌ Violates (must modify existing code) | ✅ Follows (extend with new classes) |

---

## Next Steps

**Week 12-04: State Pattern (SOLUTION)**

We'll refactor the boss AI to use the State Pattern:

1. Create `BossState` interface with `chooseAction()` and `resolveAction()` methods
2. Create concrete state classes: `NormalState`, `AngryState`, `DefensiveState`, `EnragedState`
3. Add state transition logic in `BattleSystem`
4. Each state encapsulates both AI behavior AND counter relationship rules
5. Eliminate all hardcoded if/else chains

Benefits:
- Each state is independently testable
- Adding new states requires no modification to existing code
- State logic (AI + rules) is centralized in one class
- No more magic number thresholds scattered everywhere

---

## Compilation Output

Compiled to: `bin/12-03-hardcoded-boss/`

This keeps compiled artifacts separate per branch for easy comparison and testing.

---

## Related Branches

- **Previous**: `12-02-strategy-pattern` - Strategy Pattern for difficulty
- **Next**: `12-04-state-pattern` - Refactor boss AI with State Pattern
- **Related**: `11-04-observer-pattern` - Observer Pattern for events

---

## References

- **Anti-Pattern**: Hardcoded state logic with giant if/else chains
- **Solution Pattern**: State Pattern (Week 12-04)
- **Game Design**: Turn-based combat with rock-paper-scissors mechanics
- **State Machines**: Boss behavior changes based on HP percentage
