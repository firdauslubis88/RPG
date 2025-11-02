# Week 09-03: Singleton Pattern Solution

## Overview
This branch demonstrates the **Singleton pattern solution** to problems from branch 09-02: multiple instances, object drilling, and state inconsistency.

## Quick Links
- **Full Documentation**: [docs/09-03-solution.md](docs/09-03-solution.md)
- **Problem Branch**: [09-02-without-singleton](../09-02-without-singleton/)
- **Previous Solution**: [09-01-with-game-loop](../09-01-with-game-loop/)

## What's Different from 09-02?

### ✅ Problems Solved

1. **Multiple Instances** → Single instance guaranteed
   - Private constructor prevents `new GameManager()`
   - Only `getInstance()` can access the instance
   - Compiler enforced!

2. **Object Drilling** → Clean constructors
   - No more parameter passing through 4 levels
   - Constructors have zero parameters
   - Clean, simple code

3. **State Inconsistency** → Fixed HUD bug
   - HUD now shows correct score
   - Everyone reads from THE instance
   - No more confusion

### Code Changes Summary

**Before (09-02)**:
```java
// Main.java
GameManager manager = new GameManager();
GameEngine engine = new GameEngine(manager);

// GameEngine.java
public GameEngine(GameManager manager) {
    this.logic = new GameLogic(manager);
    this.hud = new HUD(manager);
}

// HUD.java
private final GameManager manager = new GameManager();  // BUG!
```

**After (09-03)**:
```java
// Main.java
GameEngine engine = new GameEngine();  // Clean!

// GameEngine.java
public GameEngine() {
    this.logic = new GameLogic();  // Clean!
    this.hud = new HUD();          // Clean!
}

// HUD.java
public void draw() {
    int score = GameManager.getInstance().getScore();  // Fixed!
}
```

## Singleton Pattern Structure

```java
public class GameManager {
    // 1. Static instance variable
    private static GameManager instance = null;

    // 2. Private constructor
    private GameManager() {
        // Initialize...
    }

    // 3. Public static accessor
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
}
```

## Running the Demo

### Compile
```bash
javac -d bin/09-03-with-singleton src/Main.java src/GameEngine.java src/GameLogic.java src/HUD.java src/entities/*.java src/utils/*.java
```

### Run
```bash
cd bin/09-03-with-singleton
java Main
```

### Expected Behavior

**Startup Messages**:
```
[DEBUG] GameManager singleton instance created: 146589023  ← Only ONE!
[GameLogic] Using Singleton - no parameters needed!
[HUD] Singleton instance: 146589023  ← Same hashCode
[GameEngine] Using Singleton - no object drilling!

=================================
  DUNGEON ESCAPE - WITH SINGLETON
=================================
✅ Single GameManager instance!
✅ No object drilling!
✅ Score consistency guaranteed!
=================================
```

**During Gameplay**:
```
[GameManager:146589023] Score updated: 10
[GameManager:146589023] Score updated: 20
[GameManager:146589023] Score updated: 30

║ HUD DISPLAY ║
║ Score: 30 points  ← ✅ CORRECT! (was 0 in 09-02)
║ Singleton hashCode: 146589023  ← Same instance!

[GameEngine] Score from Singleton: 30  ← ✅ Matches!
```

## Solutions Carried Forward

### From 09-01 (Game Loop)
- ✅ Separated update() and draw()
- ✅ Delta time for smooth movement
- ✅ Selective rendering (no flickering!)
- ✅ Testable game logic
- ✅ 60 FPS frame rate control

### New in 09-03 (Singleton)
- ✅ Single instance guarantee
- ✅ Global access without parameters
- ✅ State consistency
- ✅ Clean constructors

## Files Modified

| File | Change | Purpose |
|------|--------|---------|
| `entities/GameManager.java` | Private constructor + getInstance() | Singleton implementation |
| `Main.java` | Removed manager creation/passing | Clean entry point |
| `GameEngine.java` | Removed manager parameter | Clean constructor |
| `GameLogic.java` | Use getInstance() | Direct access |
| `HUD.java` | Use getInstance() | Fixed bug! |
| `entities/NPC.java` | Removed manager parameter | Clean entity |
| `entities/Coin.java` | Removed manager parameter | Clean entity |

## Key Learning Points

1. **Singleton = Private constructor + getInstance()**
   - Prevents external instantiation
   - Provides global access
   - Lazy initialization

2. **When to Use Singleton**
   - ✅ Game state management
   - ✅ Configuration/settings
   - ✅ Resource managers
   - ❌ Regular game entities
   - ❌ Utility functions

3. **Benefits**
   - Single instance guarantee
   - No object drilling
   - Easy global access
   - Testable with reset()

4. **Trade-offs**
   - Global state concerns
   - Hidden dependencies
   - Threading considerations
   - Can be overused

## Comparison: 09-02 vs 09-03

| Aspect | 09-02 | 09-03 |
|--------|-------|-------|
| Instances created | 2+ | 1 |
| Constructor parameters | Many | None |
| HUD shows score | 0 (wrong) | 30 (correct) |
| Object drilling levels | 4 | 0 |
| Compile-time protection | None | Private constructor |
| Refactoring difficulty | High | Low |

## Progressive Learning Path

```
09-00: Monolithic
  └─ Problems: Everything mixed together

09-01: Game Loop Pattern
  └─ Solutions: Separation, delta time, selective rendering

09-02: Add Features (NEW problems appear)
  └─ Problems: Multiple instances, object drilling

09-03: Singleton Pattern (current)
  └─ Solutions: Single instance, no drilling, clean code
```

**Key Insight**: Each branch maintains previous solutions while solving new problems!

## Next Steps

- Read [docs/09-03-solution.md](docs/09-03-solution.md) for comprehensive details
- Compare with 09-02 to see the differences
- Experiment: Try breaking Singleton (make constructor public) to see compiler errors
- Discuss: When is Singleton NOT the right choice?

## Discussion Questions

1. What happens if you try `new GameManager()` now?
2. Why is `getInstance()` static?
3. How does this solve the HUD bug from 09-02?
4. When should you NOT use Singleton?
5. How would you test code that uses Singleton?

---

**Branch**: 09-03-with-singleton
**Pattern**: Singleton
**Status**: ✅ Complete
**Builds on**: 09-01, 09-02
**Demonstrates**: Global state management done right
