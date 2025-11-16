# Week 12-02: Strategy Pattern (SOLUTION)

**Branch**: `12-02-strategy-pattern`

## Overview

This branch demonstrates the **SOLUTION** using the Strategy Pattern to replace the hardcoded difficulty logic from Week 12-01. This is the "right way" to implement a flexible, extensible difficulty system that follows SOLID principles.

## Evolution from Week 12-01

### Before (12-01): Hardcoded Anti-Pattern
```java
// WorldController.java - ANTI-PATTERN
private String difficulty;  // "EASY", "NORMAL", or "HARD"

public WorldController(Entity entity, String difficulty) {
    this.difficulty = difficulty;

    // Switch-case #1: Spawn interval
    switch (difficulty) {
        case "EASY":
            this.spawnInterval = 1.0f;
            break;
        case "NORMAL":
            this.spawnInterval = 0.5f;
            break;
        case "HARD":
            this.spawnInterval = 0.3f;
            break;
    }
}

private void spawnInitialObstacles() {
    // Switch-case #2: Initial enemy counts
    int spikeCount, goblinCount, wolfCount;
    switch (difficulty) {
        case "EASY":
            spikeCount = 3; goblinCount = 3; wolfCount = 0;
            break;
        case "NORMAL":
            spikeCount = 3; goblinCount = 3; wolfCount = 2;
            break;
        case "HARD":
            spikeCount = 4; goblinCount = 4; wolfCount = 4;
            break;
    }
}

private void spawnRandomObstacle() {
    // Switch-case #3: Enemy type distribution
    int enemyType;
    switch (difficulty) {
        case "EASY":
            enemyType = random.nextInt(2);  // Spikes and Goblins only
            break;
        case "NORMAL":
            enemyType = random.nextInt(3);  // All types equally
            break;
        case "HARD":
            // Wolf emphasis logic...
            break;
    }
}
```

**Problems**:
- 3+ switch-case statements scattered across WorldController
- String-based comparisons (error-prone!)
- Violates Open/Closed Principle
- Hard to add new difficulties

### After (12-02): Strategy Pattern Solution
```java
// WorldController.java - SOLUTION
import difficulty.DifficultyStrategy;

private final DifficultyStrategy strategy;  // Interface, not string!

public WorldController(Entity entity, DifficultyStrategy strategy) {
    this.strategy = strategy;
    // No switch statements needed!
}

private void spawnInitialObstacles() {
    // Ask strategy for counts - polymorphism at work!
    int spikeCount = strategy.getInitialSpikeCount();
    int goblinCount = strategy.getInitialGoblinCount();
    int wolfCount = strategy.getInitialWolfCount();
    // Spawn using these counts...
}

public void update(float delta) {
    // Ask strategy if continuous spawning is enabled
    if (strategy.hasContinuousSpawning()) {
        spawnTimer += delta;
        if (spawnTimer >= strategy.getSpawnInterval()) {
            spawnRandomObstacle();
            spawnTimer = 0;
        }
    }
}

private void spawnRandomObstacle() {
    int randomValue = random.nextInt(10);
    // Ask strategy which enemy type to spawn
    int enemyType = strategy.getEnemyTypeToSpawn(randomValue);
    ObstaclePool pool = pools.get(enemyType);
    // Spawn from pool...
}
```

**Benefits**:
- Zero switch-case statements!
- Type-safe (no strings!)
- Follows Open/Closed Principle
- Easy to add new difficulties

## New Architecture

### 1. DifficultyStrategy Interface
**File**: `src/difficulty/DifficultyStrategy.java`

```java
package difficulty;

public interface DifficultyStrategy {
    float getSpawnInterval();           // How fast enemies spawn
    int getInitialSpikeCount();        // Initial Spikes
    int getInitialGoblinCount();       // Initial Goblins
    int getInitialWolfCount();         // Initial Wolves
    boolean hasContinuousSpawning();   // Auto-spawn enabled?
    int getEnemyTypeToSpawn(int random); // Which enemy type? (0=Spike, 1=Goblin, 2=Wolf)
    String getName();                  // Difficulty name for display
}
```

This interface defines the contract that all difficulty strategies must implement.

### 2. Concrete Strategy Classes

#### EasyDifficulty
**File**: `src/difficulty/EasyDifficulty.java`

```java
public class EasyDifficulty implements DifficultyStrategy {
    @Override
    public int getInitialSpikeCount() { return 3; }

    @Override
    public int getInitialGoblinCount() { return 3; }

    @Override
    public int getInitialWolfCount() { return 0; }  // NO wolves!

    @Override
    public boolean hasContinuousSpawning() { return false; }

    @Override
    public int getEnemyTypeToSpawn(int random) {
        return random % 2;  // 0=Spike, 1=Goblin (no wolves!)
    }

    @Override
    public String getName() { return "EASY"; }
}
```

**Characteristics**:
- Only Spikes and Goblins (no Wolves)
- 6 initial enemies total
- No continuous auto-spawning

#### NormalDifficulty
**File**: `src/difficulty/NormalDifficulty.java`

```java
public class NormalDifficulty implements DifficultyStrategy {
    @Override
    public int getInitialSpikeCount() { return 3; }

    @Override
    public int getInitialGoblinCount() { return 3; }

    @Override
    public int getInitialWolfCount() { return 2; }

    @Override
    public boolean hasContinuousSpawning() { return false; }

    @Override
    public int getEnemyTypeToSpawn(int random) {
        return random % 3;  // 0=Spike, 1=Goblin, 2=Wolf (equal distribution)
    }

    @Override
    public String getName() { return "NORMAL"; }
}
```

**Characteristics**:
- All enemy types (Spikes, Goblins, Wolves)
- 8 initial enemies total
- No continuous auto-spawning
- Equal enemy distribution

#### HardDifficulty
**File**: `src/difficulty/HardDifficulty.java`

```java
public class HardDifficulty implements DifficultyStrategy {
    @Override
    public int getInitialSpikeCount() { return 4; }

    @Override
    public int getInitialGoblinCount() { return 4; }

    @Override
    public int getInitialWolfCount() { return 4; }

    @Override
    public boolean hasContinuousSpawning() { return true; }

    @Override
    public float getSpawnInterval() { return 0.3f; }

    @Override
    public int getEnemyTypeToSpawn(int random) {
        // Wolf emphasis: 30% Spike, 30% Goblin, 40% Wolf
        if (random < 3) return 0;      // 0-2 = Spike
        else if (random < 6) return 1; // 3-5 = Goblin
        else return 2;                 // 6-9 = Wolf
    }

    @Override
    public String getName() { return "HARD"; }
}
```

**Characteristics**:
- All enemy types with Wolf emphasis
- 12 initial enemies total
- Continuous auto-spawning every 0.3 seconds
- 40% wolves, 30% spikes, 30% goblins

## Modified Files

### 1. WorldController.java
**Before (12-01)**:
```java
public WorldController(Entity entity, String difficulty) {
    this.difficulty = difficulty;
    // Switch-case for spawn interval...
}
```

**After (12-02)**:
```java
import difficulty.DifficultyStrategy;

public WorldController(Entity entity, DifficultyStrategy strategy) {
    this.strategy = strategy;
    // No switch-case needed!
}
```

**Key Changes**:
- Constructor accepts `DifficultyStrategy` instead of `String`
- All switch-case statements removed (3 total eliminated)
- Uses polymorphism: `strategy.getInitialSpikeCount()`, etc.

### 2. GameLogic.java
**Before (12-01)**:
```java
public GameLogic(String difficulty) {
    this.difficulty = difficulty;
    this.worldController = new WorldController(player, difficulty);
}
```

**After (12-02)**:
```java
import difficulty.DifficultyStrategy;

public GameLogic(DifficultyStrategy strategy) {
    this.strategy = strategy;
    this.worldController = new WorldController(player, strategy);
}
```

### 3. GameEngine.java
**Before (12-01)**:
```java
public GameEngine(String difficulty) {
    this.difficulty = difficulty;
    this.logic = new GameLogic(difficulty);
}
```

**After (12-02)**:
```java
import difficulty.DifficultyStrategy;

public GameEngine(DifficultyStrategy strategy) {
    this.strategy = strategy;
    this.logic = new GameLogic(strategy);
}

public void start() {
    System.out.println("Difficulty: " + strategy.getName());
}
```

### 4. MainMenu.java
**Before (12-01)**:
```java
public String show() {
    String choice = scanner.nextLine().trim();

    switch (choice) {
        case "1":
            return "EASY";
        case "2":
            return "NORMAL";
        case "3":
            return "HARD";
    }
}
```

**After (12-02)**:
```java
import difficulty.*;

public DifficultyStrategy show() {
    String choice = scanner.nextLine().trim();

    DifficultyStrategy strategy;
    switch (choice) {
        case "1":
            strategy = new EasyDifficulty();
            break;
        case "2":
            strategy = new NormalDifficulty();
            break;
        case "3":
            strategy = new HardDifficulty();
            break;
        default:
            strategy = new NormalDifficulty();
            break;
    }
    return strategy;
}
```

### 5. Main.java
**Before (12-01)**:
```java
public static void main(String[] args) {
    MainMenu menu = new MainMenu();
    String difficulty = menu.show();
    GameEngine engine = new GameEngine(difficulty);
    engine.start();
}
```

**After (12-02)**:
```java
import difficulty.DifficultyStrategy;

public static void main(String[] args) {
    MainMenu menu = new MainMenu();
    DifficultyStrategy strategy = menu.show();
    GameEngine engine = new GameEngine(strategy);
    engine.start();
}
```

## Benefits of Strategy Pattern

### 1. Open/Closed Principle (OCP)
**Open for extension, closed for modification**

Want to add "EXTREME" difficulty? Just create one new file:

```java
// src/difficulty/ExtremeDifficulty.java
package difficulty;

public class ExtremeDifficulty implements DifficultyStrategy {
    @Override
    public int getInitialSpikeCount() { return 10; }

    @Override
    public int getInitialGoblinCount() { return 10; }

    @Override
    public int getInitialWolfCount() { return 10; }

    @Override
    public boolean hasContinuousSpawning() { return true; }

    @Override
    public float getSpawnInterval() { return 0.1f; }  // Very fast!

    @Override
    public int getEnemyTypeToSpawn(int random) {
        return 2;  // All wolves!
    }

    @Override
    public String getName() { return "EXTREME"; }
}
```

Then update only the menu:
```java
// MainMenu.java - only one file to modify!
case "4":
    strategy = new ExtremeDifficulty();
    break;
```

**No changes needed in**:
- WorldController.java
- GameLogic.java
- GameEngine.java

### 2. Single Responsibility Principle (SRP)
Each class has one clear responsibility:
- `EasyDifficulty`: Knows only about EASY behavior
- `NormalDifficulty`: Knows only about NORMAL behavior
- `HardDifficulty`: Knows only about HARD behavior
- `WorldController`: Manages obstacles, delegates difficulty logic to strategy

### 3. Dependency Inversion Principle (DIP)
High-level modules depend on abstractions, not concrete classes:
- `WorldController` depends on `DifficultyStrategy` interface
- Not tied to `EasyDifficulty`, `NormalDifficulty`, or `HardDifficulty`
- Can work with any strategy that implements the interface

### 4. Compile-Time Safety
**Before (12-01)**: String comparisons
```java
if (difficulty.equals("EASY")) {  // Typo: "EAZY" - runtime bug!
    // ...
}
```

**After (12-02)**: Type-safe
```java
int count = strategy.getInitialSpikeCount();  // Compile error if typo!
```

### 5. Easy to Test
You can inject test strategies:
```java
// Test with custom strategy
class TestStrategy implements DifficultyStrategy {
    @Override
    public int getInitialSpikeCount() { return 1; }
    // ... custom test values
}

WorldController controller = new WorldController(player, new TestStrategy());
```

No need to modify production code for testing!

## Design Patterns Kept

✅ **Strategy Pattern** (Week 12-02): Difficulty behaviors
✅ **Observer Pattern** (Week 11-04): Event systems
✅ **Command Pattern** (Week 11-02): Input handling
✅ **Object Pool Pattern** (Week 10-04): Obstacle reuse
✅ **Factory Pattern** (Week 10-02): Obstacle creation
✅ **Singleton Pattern** (Week 09-03): GameManager, EventBus

## Comparison: Anti-Pattern vs Solution

| Aspect | Week 12-01 (Anti-Pattern) | Week 12-02 (Solution) |
|--------|---------------------------|----------------------|
| **Difficulty Type** | String ("EASY", "NORMAL", "HARD") | DifficultyStrategy object |
| **Switch Statements** | 3+ scattered across code | 0 (eliminated) |
| **Add New Difficulty** | Modify 3+ files, update 3+ switch-cases | Create 1 new file, update menu only |
| **Compile Safety** | Runtime errors from typos | Compile-time type checking |
| **Testability** | Hard to test, need to modify code | Easy to inject mock strategies |
| **SOLID Principles** | Violates OCP, SRP | Follows OCP, SRP, DIP |
| **Maintainability** | Hard to maintain, logic scattered | Easy to maintain, logic encapsulated |

## Key Teaching Points

### 1. Encapsulation
Each difficulty strategy encapsulates its own behavior. You don't need to know how EASY works to understand HARD.

### 2. Polymorphism
All strategies implement the same interface, so `WorldController` can treat them uniformly:
```java
// Works with any strategy!
int count = strategy.getInitialSpikeCount();
```

### 3. Flexibility
Want to change difficulty behavior? Just modify one strategy class. Want to add a new difficulty? Just add a new strategy class.

### 4. No Code Duplication
Before: Same switch-case logic repeated 3+ times
After: Each strategy defines its behavior once

### 5. Easy to Extend
Adding features is trivial:
```java
// Want different spawn rates per difficulty?
public interface DifficultyStrategy {
    float getPlayerSpeed();      // New method!
    int getPlayerStartingHP();   // New method!
    // Existing methods...
}
```

## Real-World Applications

The Strategy Pattern is used in:
1. **Game Difficulty Systems**: Exactly what we're doing here
2. **Sorting Algorithms**: Different sorting strategies (QuickSort, MergeSort, etc.)
3. **Payment Processing**: Different payment methods (CreditCard, PayPal, Bitcoin)
4. **Compression Algorithms**: Different compression strategies (ZIP, GZIP, BZIP2)
5. **Route Planning**: Different routing algorithms (shortest, fastest, scenic)

## Compilation

```bash
# Compile difficulty package first
javac -d bin/12-02-strategy-pattern src/difficulty/*.java

# Compile everything else
javac -d bin/12-02-strategy-pattern -cp bin/12-02-strategy-pattern \
  src/*.java src/entities/*.java src/obstacles/*.java src/factories/*.java \
  src/pools/*.java src/world/*.java src/events/*.java src/systems/*.java \
  src/utils/*.java src/input/*.java src/ui/*.java src/commands/*.java
```

## Running the Game

```bash
java -cp bin/12-02-strategy-pattern Main
```

Select difficulty at menu, then play!

## Testing Different Difficulties

### Test EASY Difficulty
Expected behavior:
- Only 3 Spikes (^) and 3 Goblins (G) spawn initially
- NO Wolves (W) should appear
- No continuous auto-spawning

### Test NORMAL Difficulty
Expected behavior:
- 3 Spikes, 3 Goblins, and 2 Wolves spawn initially
- All enemy types present
- No continuous auto-spawning

### Test HARD Difficulty
Expected behavior:
- 4 Spikes, 4 Goblins, and 4 Wolves spawn initially
- Continuous auto-spawning every 0.3 seconds
- More wolves spawn than other types (40% wolves)

## Next Steps

**Week 12-03**: Boss Battle with State Pattern
- Use State Pattern for boss AI
- Different boss states: Idle, Aggressive, Defensive, Enraged
- Demonstrate state transitions
- Show how State Pattern differs from Strategy Pattern
