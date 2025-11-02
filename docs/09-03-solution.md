# Branch 09-03: With Singleton Pattern (Solution)

## Purpose
This branch demonstrates the **Singleton pattern solution** to the problems from 09-02: multiple instances, object drilling, and state inconsistency.

**Context**: Branch 09-02 showed problems from expanding the game without proper global state management. This branch fixes those problems.

**✅ This code shows the SOLUTION!**

---

## What Is the Singleton Pattern?

### Definition
**Singleton** is a creational design pattern that ensures:
1. **Single instance**: Only ONE instance of a class exists
2. **Global access**: That instance is accessible from anywhere
3. **Lazy initialization**: Instance created only when first needed

### The Three Components

```java
public class GameManager {
    // 1. Static instance variable (holds THE instance)
    private static GameManager instance = null;

    // 2. Private constructor (prevents external instantiation)
    private GameManager() {
        // Initialize...
    }

    // 3. Public static accessor (provides global access)
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
}
```

### Why It Works

**Problem**: Anyone can create instances
```java
GameManager m1 = new GameManager();  // ✅ Compiles
GameManager m2 = new GameManager();  // ✅ Compiles
// Result: Two instances! ❌
```

**Solution**: Private constructor prevents instantiation
```java
GameManager m1 = new GameManager();  // ❌ Compiler error!
GameManager m2 = GameManager.getInstance();  // ✅ Gets THE instance
GameManager m3 = GameManager.getInstance();  // ✅ Gets SAME instance
// m2 == m3  →  true
```

---

## Problems Solved

### ✅ Problem 1: Multiple Instances → SOLVED

**Before (09-02)**:
```java
[DEBUG] GameManager instance created: 498931366  ← Instance A
[DEBUG] GameManager instance created: 349885916  ← Instance B (BUG!)
```

**After (09-03)**:
```java
[DEBUG] GameManager singleton instance created: 146589023  ← Only ONE!
[HUD] Singleton instance: 146589023  ← Same instance
[GameLogic] Singleton instance: 146589023  ← Same instance
```

**How**: Private constructor prevents `new GameManager()` calls.

---

### ✅ Problem 2: Object Drilling → ELIMINATED

**Before (09-02)**: Parameter passing through 4 levels
```java
Main.java:
  GameManager manager = new GameManager();
  GameEngine engine = new GameEngine(manager);  // Pass level 1

GameEngine.java:
  public GameEngine(GameManager manager) {
    this.logic = new GameLogic(manager);  // Pass level 2
    this.hud = new HUD(manager);          // Pass level 2
  }

GameLogic.java:
  public GameLogic(GameManager manager) {
    this.npc = new NPC(manager);  // Pass level 3
    this.coin = new Coin(manager);  // Pass level 3
  }
```

**After (09-03)**: Clean constructors!
```java
Main.java:
  GameEngine engine = new GameEngine();  // No parameters!

GameEngine.java:
  public GameEngine() {
    this.logic = new GameLogic();  // No parameters!
    this.hud = new HUD();          // No parameters!
  }

GameLogic.java:
  public GameLogic() {
    this.npc = new NPC();  // No parameters!
    this.coin = new Coin();  // No parameters!
  }

// When needed, access directly:
GameManager.getInstance().addScore(10);
```

**How**: `getInstance()` provides global access without passing parameters.

---

### ✅ Problem 3: State Inconsistency → FIXED

**Before (09-02)**: HUD showed wrong score
```java
[GameManager:498931366] Score updated: 30  ← GameLogic's instance
HUD DISPLAY: Score: 0 points  ← HUD's instance (WRONG!)
```

**After (09-03)**: HUD shows correct score
```java
[GameManager:146589023] Score updated: 30  ← THE instance
HUD DISPLAY: Score: 30 points  ← THE instance (CORRECT!)
```

**How**: Only one instance exists, so everyone reads the same data.

---

## File Changes from 09-02

### Modified Files

#### [src/entities/GameManager.java](../src/entities/GameManager.java)
**Changes**:
- ✅ Private constructor (was public)
- ✅ Static `instance` variable
- ✅ `getInstance()` method
- ✅ Added `resetInstance()` for testing

**Key Code**:
```java
public class GameManager {
    private static GameManager instance = null;

    private GameManager() {  // ✅ PRIVATE!
        // ...
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
}
```

#### [src/Main.java](../src/Main.java)
**Changes**:
- ❌ Removed: `GameManager manager = new GameManager();`
- ❌ Removed: `new GameEngine(manager);`
- ✅ Added: `new GameEngine();` (no parameters!)

**Before (09-02)**:
```java
public static void main(String[] args) {
    GameManager manager = new GameManager();  // ❌ Create
    GameEngine engine = new GameEngine(manager);  // ❌ Pass
    engine.start();
}
```

**After (09-03)**:
```java
public static void main(String[] args) {
    GameEngine engine = new GameEngine();  // ✅ Clean!
    engine.start();
}
```

#### [src/GameEngine.java](../src/GameEngine.java)
**Changes**:
- ❌ Removed: `private final GameManager manager;` field
- ❌ Removed: `GameManager manager` parameter from constructor
- ✅ Changed: `manager.updateTime(delta)` → `GameManager.getInstance().updateTime(delta)`
- ✅ Maintained: Selective rendering from 09-01

**Before (09-02)**:
```java
public class GameEngine {
    private final GameManager manager;  // ❌ Field

    public GameEngine(GameManager manager) {  // ❌ Parameter
        this.manager = manager;
        this.logic = new GameLogic(manager);  // ❌ Pass
        this.hud = new HUD(manager);          // ❌ Pass
    }
}
```

**After (09-03)**:
```java
public class GameEngine {
    // ✅ No manager field!

    public GameEngine() {  // ✅ No parameter!
        this.logic = new GameLogic();  // ✅ Clean!
        this.hud = new HUD();          // ✅ Clean!
    }

    public void start() {
        // ✅ Access directly when needed
        GameManager.getInstance().updateTime(delta);
    }
}
```

#### [src/GameLogic.java](../src/GameLogic.java)
**Changes**:
- ❌ Removed: `private final GameManager manager;` field
- ❌ Removed: `GameManager manager` parameter from constructor
- ✅ Changed: `manager.addScore(10)` → `GameManager.getInstance().addScore(10)`

**Before (09-02)**:
```java
public class GameLogic {
    private final GameManager manager;

    public GameLogic(GameManager manager) {
        this.manager = manager;
        this.npc = new NPC(manager);  // ❌ Pass
        this.coin = new Coin(manager);  // ❌ Pass
    }

    public void checkCollisions() {
        manager.addScore(10);  // ❌ Field access
    }
}
```

**After (09-03)**:
```java
public class GameLogic {
    // ✅ No manager field!

    public GameLogic() {
        this.npc = new NPC();  // ✅ Clean!
        this.coin = new Coin();  // ✅ Clean!
    }

    public void checkCollisions() {
        GameManager.getInstance().addScore(10);  // ✅ Direct access
    }
}
```

#### [src/HUD.java](../src/HUD.java)
**Changes**:
- ❌ Removed: `private final GameManager manager = new GameManager();` (THE BUG!)
- ❌ Removed: `GameManager passedManager` parameter
- ✅ Changed: `manager.getScore()` → `GameManager.getInstance().getScore()`
- ✅ Fixed: Now reads from THE instance, not a wrong instance

**Before (09-02)** - INTENTIONAL BUG:
```java
public class HUD {
    private final GameManager manager = new GameManager();  // ❌ BUG!

    public HUD(GameManager passedManager) {
        // ❌ Ignores parameter!
    }

    public void draw() {
        int score = manager.getScore();  // ❌ Wrong instance!
    }
}
```

**After (09-03)** - FIXED:
```java
public class HUD {
    // ✅ No field!

    public HUD() {
        // ✅ No parameter!
    }

    public void draw() {
        int score = GameManager.getInstance().getScore();  // ✅ THE instance!
    }
}
```

#### [src/entities/NPC.java](../src/entities/NPC.java) and [src/entities/Coin.java](../src/entities/Coin.java)
**Changes**:
- ❌ Removed: `private final GameManager manager;` field
- ❌ Removed: `GameManager manager` parameter from constructor
- ✅ Result: Clean entity classes with no dependencies

**Before (09-02)**:
```java
public class NPC {
    private final GameManager manager;

    public NPC(GameManager manager) {
        this.manager = manager;
        // ...
    }
}
```

**After (09-03)**:
```java
public class NPC {
    // ✅ No manager field!

    public NPC() {
        // ✅ No parameter!
        // ...
    }
}
```

---

## Running This Branch

### Compile
```bash
javac -d bin/09-03-with-singleton src/Main.java src/GameEngine.java src/GameLogic.java src/HUD.java src/entities/*.java src/utils/*.java
```

### Run
```bash
cd bin/09-03-with-singleton
java Main
```

### Observe Solutions

**Watch for**:
1. ✅ Only ONE `GameManager singleton instance created` message
2. ✅ All components reference the SAME hashCode
3. ✅ HUD shows correct score (matches actual score)
4. ✅ No parameter passing in constructors
5. ✅ "✅ FIXED: Reading from THE instance!" message

**Expected Output**:
```
[GameLogic] Using Singleton - no parameters needed!
[HUD] Using Singleton - no parameters needed!
[DEBUG] GameManager singleton instance created: 146589023
[HUD] Singleton instance: 146589023
[GameEngine] Using Singleton - no object drilling!

=================================
  DUNGEON ESCAPE - WITH SINGLETON
=================================
✅ Single GameManager instance!
✅ No object drilling!
✅ Score consistency guaranteed!
=================================

... after collisions ...

[GameManager:146589023] Score updated: 30

║ HUD DISPLAY ║
║ Score: 30 points  ← ✅ CORRECT!
║ Singleton hashCode: 146589023

[GameEngine] Score from Singleton: 30  ← ✅ SAME!
```

---

## Solutions Carried Forward

This branch maintains ALL solutions from previous branches:

### From 09-01 (Game Loop Pattern)
- ✅ Separated update() and draw() methods
- ✅ Delta time for frame-rate independence
- ✅ Selective rendering (no flickering!)
- ✅ Testable game logic
- ✅ 60 FPS target with frame rate control

### From 09-03 (Singleton Pattern)
- ✅ Single instance guarantee
- ✅ No object drilling
- ✅ State consistency
- ✅ Clean constructors

**Key Insight**: We don't throw away previous solutions. Each branch builds on successes!

---

## Singleton Pattern Details

### Lazy Initialization

**What**: Instance created only when first accessed

**Code**:
```java
public static GameManager getInstance() {
    if (instance == null) {  // ✅ Check if exists
        instance = new GameManager();  // ✅ Create only if needed
    }
    return instance;
}
```

**Benefit**: No wasted memory if never used

### Eager Initialization (Alternative)

**Alternative approach**:
```java
private static GameManager instance = new GameManager();  // ✅ Create immediately

public static GameManager getInstance() {
    return instance;  // ✅ Always exists
}
```

**Trade-off**: Memory used immediately, but simpler code

### Thread Safety (Not Needed Here)

**Question**: What if two threads call `getInstance()` simultaneously?

**For single-threaded games**: Not a problem!

**For multi-threaded apps**: Use synchronized or eager initialization
```java
public static synchronized GameManager getInstance() {
    if (instance == null) {
        instance = new GameManager();
    }
    return instance;
}
```

---

## Benefits vs. Trade-offs

### ✅ Benefits

1. **Single Instance Guarantee**
   - Compiler enforced (private constructor)
   - Can't accidentally create second instance
   - State consistency guaranteed

2. **No Object Drilling**
   - Clean constructors (no parameters)
   - Easy refactoring (no parameter chains)
   - Less coupling between classes

3. **Global Access**
   - Access from anywhere: `GameManager.getInstance()`
   - No need to pass references
   - Convenient for truly global state

4. **Testability**
   - Can reset state with `resetInstance()`
   - Single point of control for testing
   - Predictable behavior

### ⚠️ Trade-offs

1. **Global State**
   - Can be misused (everything becomes global)
   - Hidden dependencies (not visible in constructor)
   - Harder to track who modifies state

2. **Testing Challenges**
   - Must reset state between tests
   - Can cause test interference
   - Mocking can be difficult

3. **Tight Coupling**
   - Classes directly depend on `GameManager`
   - Harder to swap implementations
   - Less flexible than dependency injection

4. **Concurrency Issues**
   - Not thread-safe by default
   - Needs synchronization for multi-threading
   - Can become bottleneck

---

## When to Use Singleton

### ✅ Good Use Cases

1. **Configuration/Settings**
   ```java
   GameSettings.getInstance().getSoundVolume()
   ```

2. **Resource Managers**
   ```java
   TextureManager.getInstance().loadTexture("player.png")
   ```

3. **Logging/Telemetry**
   ```java
   Logger.getInstance().log("Player died")
   ```

4. **Game State** (as demonstrated)
   ```java
   GameManager.getInstance().getScore()
   ```

### ❌ Bad Use Cases (Consider Alternatives)

1. **Entities/Objects** (use regular classes)
   ```java
   // ❌ DON'T: Player.getInstance()
   // ✅ DO: new Player(x, y)
   ```

2. **Utilities** (use static methods)
   ```java
   // ❌ DON'T: MathUtils.getInstance().sqrt(4)
   // ✅ DO: Math.sqrt(4)
   ```

3. **Services** (use dependency injection)
   ```java
   // ❌ DON'T: DatabaseService.getInstance()
   // ✅ DO: Pass DatabaseService in constructor
   ```

---

## Comparison: 09-02 vs 09-03

| Aspect | 09-02 (Without Singleton) | 09-03 (With Singleton) |
|--------|---------------------------|------------------------|
| **Instances** | Multiple (2+) | Single (1) |
| **Constructor params** | GameManager everywhere | None! |
| **Parameter passing** | 4 levels deep | Zero levels |
| **State consistency** | ❌ Broken (HUD shows 0) | ✅ Fixed (HUD shows actual) |
| **Compile protection** | ❌ None | ✅ Private constructor |
| **Code cleanliness** | ❌ Parameter pollution | ✅ Clean constructors |
| **Refactoring effort** | ❌ High (6+ files) | ✅ Low (isolated change) |

---

## Progressive Learning Summary

```
09-00: Monolithic (all problems)
  ❌ No game loop
  ❌ Frame rate coupling
  ❌ Untestable
  ❌ Flickering

09-01: Game Loop Pattern (solve 4 problems)
  ✅ Separated update/draw
  ✅ Delta time
  ✅ Testable logic
  ✅ Selective rendering

09-02: Add Features (NEW problem appears)
  ✅ Keep all 09-01 solutions
  ❌ NEW: Multiple instances
  ❌ NEW: Object drilling
  ❌ NEW: State inconsistency

09-03: Singleton Pattern (solve NEW problems)
  ✅ Keep all 09-01 solutions
  ✅ SOLVE: Single instance
  ✅ SOLVE: No object drilling
  ✅ SOLVE: State consistency
```

**Key Insight**: Each branch solves problems without creating NEW ones!

---

## Discussion Questions

1. How does private constructor prevent multiple instances?
2. Why is `getInstance()` static?
3. What happens if we make constructor public again?
4. Is Singleton the ONLY solution to object drilling?
5. When would Singleton be a BAD choice?
6. How would you test code that uses Singleton?
7. **Compare**: Singleton vs. Dependency Injection - when to use each?
8. **Think**: Can you have TWO Singletons? What about variations?
9. **Design**: How would you modify Singleton to allow exactly 3 instances?

---

## Alternative Solutions

### 1. Dependency Injection
**Instead of**:
```java
GameManager.getInstance().addScore(10);
```

**Alternative**:
```java
public class GameLogic {
    private GameManager manager;

    public GameLogic(GameManager manager) {
        this.manager = manager;  // ✅ Injected dependency
    }
}
```

**Trade-off**: More flexible, but object drilling returns!

### 2. Service Locator
```java
ServiceLocator.get(GameManager.class).addScore(10);
```

**Trade-off**: More flexible, but less type-safe

### 3. Static Class
```java
public class GameManager {
    private static int score;

    public static void addScore(int points) {
        score += points;
    }
}
```

**Trade-off**: Simpler, but can't reset for testing

---

## Summary

**Singleton pattern solves**:
1. ✅ Multiple instance problem → Private constructor
2. ✅ Object drilling problem → Global access via getInstance()
3. ✅ State inconsistency → Single source of truth

**Key components**:
1. Private constructor
2. Static instance variable
3. Public static getInstance() method

**Real-world usage**:
- Game state management
- Configuration/settings
- Resource managers
- Logging systems

**Remember**: Singleton is powerful but should be used judiciously. Overuse leads to tight coupling and testing difficulties!

---

## Next Steps

This completes the Week 09 progression:
- ✅ 09-00: Problem (monolithic)
- ✅ 09-01: Solution (game loop)
- ✅ 09-02: New problem (multiple instances)
- ✅ 09-03: Solution (singleton)

**Key Takeaway**: Software development is iterative. Problems emerge as systems grow. Patterns help solve problems without breaking existing solutions!
