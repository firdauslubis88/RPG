# Week 12-01: Hardcoded Difficulty (ANTI-PATTERN)

**Branch**: `12-01-hardcoded-difficulty`

## Overview

This branch demonstrates the **ANTI-PATTERN** of hardcoding difficulty logic using switch-case statements. While functional, this approach violates the Open/Closed Principle and creates tight coupling between difficulty levels and game logic.

## What's New

### 1. Main Menu System
- **File**: `src/ui/MainMenu.java`
- Text-based menu for difficulty selection before game starts
- Three difficulty levels: EASY, NORMAL, HARD
- Difficulty is locked once selected (cannot change during gameplay)

### 2. Dungeon Exit Entity
- **File**: `src/entities/DungeonExit.java`
- Static entity at position (23, 23)
- Represented by 'D' symbol
- Placeholder for future boss battle feature (Week 12-03)
- Exit position changed from wall '#' to floor '.' for open door effect

### 3. Hardcoded Difficulty Logic (ANTI-PATTERN)
- **Files**: `src/WorldController.java`, `src/GameLogic.java`, `src/GameEngine.java`
- String-based difficulty: "EASY", "NORMAL", "HARD"
- Switch-case statements in multiple locations
- Different spawn rates and enemy types per difficulty

## Difficulty Effects

### EASY
- **Enemy Types**: Only Spikes and Goblins (NO Wolves)
- **Initial Enemies**: 3 Spikes, 3 Goblins (6 total)
- **Spawn Rate**: NO continuous spawning (only initial placement)
- **Best For**: Learning the game mechanics

### NORMAL
- **Enemy Types**: Spikes, Goblins, and Wolves
- **Initial Enemies**: 3 Spikes, 3 Goblins, 2 Wolves (8 total)
- **Spawn Rate**: NO continuous spawning (only initial placement)
- **Best For**: Standard gameplay experience

### HARD
- **Enemy Types**: All types with Wolf emphasis (40% wolves)
- **Initial Enemies**: 4 Spikes, 4 Goblins, 4 Wolves (12 total)
- **Spawn Rate**: Continuous auto-spawning every 0.3s
- **Enemy Distribution**: 30% Spike, 30% Goblin, 40% Wolf
- **Best For**: Challenge seekers

## Problems Demonstrated (ANTI-PATTERN)

### 1. Hardcoded Switch-Case Logic
**Location**: `WorldController.java` - THREE different switch statements!

```java
// Constructor - Spawn interval
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
```

**Problem**: Must modify code in 3 places to add new difficulty!

### 2. String-Based Comparison
```java
if (difficulty.equals("HARD")) {
    // Only HARD has continuous spawning
}
```

**Problems**:
- No compile-time safety
- Typos cause runtime errors
- Case-sensitive comparisons

### 3. Violates Open/Closed Principle
- **Open for extension**: Should be able to add new difficulties WITHOUT modifying existing code
- **Closed for modification**: Should NOT need to change `WorldController` to add "EXTREME" difficulty

**Current Reality**: Must update 3+ switch statements to add new difficulty!

### 4. Tight Coupling
- `WorldController` tightly coupled to difficulty string
- Spawn logic scattered across multiple methods
- Difficult to test different configurations
- Hard to reuse difficulty logic elsewhere

### 5. Code Duplication
Similar switch-case logic repeated in:
1. Constructor (spawn interval)
2. `spawnInitialObstacles()` (initial enemy counts)
3. `spawnRandomObstacle()` (enemy type distribution)

## Modified Files

### New Files
- `src/ui/MainMenu.java` - Main menu for difficulty selection
- `src/entities/DungeonExit.java` - Exit point entity

### Modified Files
- `src/Main.java` - Show menu before starting game
- `src/GameEngine.java` - Accept difficulty parameter, display in welcome message
- `src/GameLogic.java` - Accept difficulty, create DungeonExit, pass to WorldController
- `src/WorldController.java` - Hardcoded difficulty logic with switch-case
- `src/world/DungeonMap.java` - Changed (23, 23) from wall to floor for open exit
- `src/pools/ObstaclePool.java` - Enhanced statistics labels for clarity

## Design Patterns Kept

✅ **Observer Pattern** (from 11-04): Event systems
✅ **Command Pattern** (from 11-02): Input handling
✅ **Object Pool Pattern** (from 10-04): Obstacle reuse
✅ **Factory Pattern** (from 10-02): Obstacle creation
✅ **Singleton Pattern** (from 09-03): GameManager, EventBus

## Key Teaching Points

### Why This Is An Anti-Pattern
1. **Not Extensible**: Adding "EXTREME" difficulty requires changing multiple files
2. **Error-Prone**: String comparisons can have typos
3. **Hard to Test**: Can't easily test different difficulty configurations
4. **Violates SOLID**: Violates Open/Closed Principle
5. **Maintenance Nightmare**: Difficulty logic scattered across codebase

### What Students Should Notice
- How many places need to change to add new difficulty
- How switch-case statements duplicate logic
- How string-based logic is fragile
- How this approach doesn't scale

### Foreshadowing Week 12-02
The next branch (12-02) will refactor this using the **Strategy Pattern**:
- Each difficulty is a separate strategy class
- Easy to add new difficulties (just add new class)
- Compile-time safety (no strings!)
- Each strategy encapsulates its own logic
- WorldController depends on interface, not concrete difficulties

## Pool Statistics Enhancement

### Problem
Original statistics were misleading:
- Showed "Total Created: 10" for Wolf pool even on EASY difficulty
- Users thought Wolves were spawned when they weren't

### Root Cause
Object pools **pre-allocate** all enemy types (10 Spike, 10 Goblin, 10 Wolf) at initialization, regardless of difficulty. This is correct pool behavior to avoid garbage collection during gameplay.

### Solution
Enhanced `ObstaclePool.printStats()` with clearer labels:
- **Pre-allocated**: Objects created at pool initialization (always 10 of each type)
- **Actually Spawned**: How many times `acquire()` was called (real game usage)
- **Returned to Pool**: How many were returned via `release()`
- **Still In Use**: Currently active obstacles

### Example Output
```
=== POOL STATISTICS ===
Spike Pool Stats:
  Pre-allocated: 10 (initial pool size)
  Actually Spawned: 15 (acquire calls)
  Returned to Pool: 12 (release calls)
  Total Pool Size: 10
  Available: 7
  Still In Use: 3

Wolf Pool Stats:
  Pre-allocated: 10 (initial pool size)
  Actually Spawned: 0 (acquire calls)  // ✅ Clear: No wolves in EASY!
  Returned to Pool: 0 (release calls)
  Total Pool Size: 10
  Available: 10
  Still In Use: 0
======================
```

## Compilation

```bash
# Compile to separate bin folder
javac -d bin/12-01-hardcoded-difficulty -cp bin/12-01-hardcoded-difficulty \
  src/*.java src/entities/*.java src/obstacles/*.java src/factories/*.java \
  src/pools/*.java src/world/*.java src/events/*.java src/systems/*.java \
  src/utils/*.java src/input/*.java src/ui/*.java src/commands/*.java

# Run the game
java -cp bin/12-01-hardcoded-difficulty Main
```

## Next Steps

**Week 12-02**: Strategy Pattern (SOLUTION)
- Refactor hardcoded difficulty into separate strategy classes
- Demonstrate proper use of Strategy Pattern
- Show how it solves all the problems from 12-01
