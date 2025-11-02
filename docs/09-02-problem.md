# Branch 09-02: Without Singleton (Problem Demonstration)

## Purpose
This branch intentionally demonstrates **problems from NOT using Singleton pattern** for global state management. This is the PROBLEM that Singleton pattern solves.

**⚠️ This code is intentionally buggy for educational purposes!**

---

## What This Branch Demonstrates

### ❌ Problem 1: Multiple Instances Bug

**The Issue**:
```java
// Main creates instance A
GameManager managerA = new GameManager();  // hashCode: 498931366

// HUD creates instance B (BUG!)
GameManager managerB = new GameManager();  // hashCode: 349885916

// GameLogic updates score in A
managerA.addScore(10);  // Score = 10 in instance A

// HUD displays score from B
managerB.getScore();    // Score = 0 in instance B! ❌
```

**Observed Behavior**:
```
[DEBUG] GameManager instance created: 498931366  ← Main's instance
[DEBUG] GameManager instance created: 349885916  ← HUD's instance (BUG!)

[GameManager:498931366] Score updated: 10
[GameManager:498931366] Score updated: 20
[GameManager:498931366] Score updated: 30

HUD DISPLAY:
  Score: 0 points  ← ❌ WRONG! Reading from different instance!
  Manager hashCode: 349885916

[GameEngine] Actual score: 30  ← ✅ Correct (from GameLogic's instance)
```

**Root Cause**:
- `GameManager` constructor is PUBLIC
- Anyone can create new instances
- HUD creates its own instance instead of using the shared one
- Result: State inconsistency

---

### ❌ Problem 2: Object Drilling

**Constructor Parameter Explosion**:
```
Main.java:
  GameManager manager = new GameManager();  // Create
  GameEngine engine = new GameEngine(manager);  // Pass (level 1)

GameEngine.java:
  public GameEngine(GameManager manager) {
    this.logic = new GameLogic(manager);  // Pass (level 2)
    this.hud = new HUD(manager);          // Pass (level 2)
  }

GameLogic.java:
  public GameLogic(GameManager manager) {
    this.npc = new NPC(manager);     // Pass (level 3)
    this.coins.add(new Coin(manager));  // Pass (level 3)
    this.coins.add(new Coin(manager));  // Pass (level 3)
  }

NPC.java, Coin.java:
  public NPC(GameManager manager) { ... }  // Receive (level 4)
  public Coin(GameManager manager) { ... }  // Receive (level 4)
```

**Parameter Count**:
- Main → GameEngine: 1 manager
- GameEngine → GameLogic: 1 manager
- GameEngine → HUD: 1 manager
- GameLogic → NPC: 1 manager
- GameLogic → Coin (x2): 2 managers
- **Total: 6 manager references passed through constructors!**

**Problems**:
- Every class needs manager parameter
- Deep parameter drilling (4 levels!)
- Refactoring nightmare (change affects 6+ files)
- Team collaboration conflicts

---

### ❌ Problem 3: No Compile-Time Protection

**Nothing Prevents This Mistake**:
```java
// Developer A (correct)
GameManager manager = new GameManager();
GameEngine engine = new GameEngine(manager);

// Developer B (mistake - compiles fine!)
public class HUD {
    private GameManager manager = new GameManager();  // ❌ NEW instance!
}
```

**Compiler says**: ✅ OK
**Runtime behavior**: ❌ BUG (wrong data)

---

## File Changes from 09-01

### Files Created
- **[src/entities/GameManager.java](../src/entities/GameManager.java)**
  - Global state manager with PUBLIC constructor
  - Tracks score, game time, level
  - **❌ BUG**: Anyone can create instances!

- **[src/HUD.java](../src/HUD.java)**
  - Displays game stats
  - **❌ BUG**: Creates own GameManager instance!

### Files Modified
- **[src/Main.java](../src/Main.java)**
  - Creates GameManager
  - Passes to GameEngine
  - **❌ PROBLEM**: Object drilling starts here

- **[src/GameEngine.java](../src/GameEngine.java)**
  - Receives GameManager parameter
  - Passes to GameLogic and HUD
  - **❌ PROBLEM**: Parameter passthrough

- **[src/GameLogic.java](../src/GameLogic.java)**
  - Receives GameManager parameter
  - Passes to NPC and Coins
  - Uses manager for score updates
  - **❌ PROBLEM**: Deep parameter drilling

- **[src/entities/NPC.java](../src/entities/NPC.java)**
  - **❌ CHANGE**: Now requires GameManager in constructor
  - Before: `new NPC(x, y, velocity)`
  - After: `new NPC(manager)`

- **[src/entities/Coin.java](../src/entities/Coin.java)**
  - **❌ CHANGE**: Now requires GameManager in constructor
  - Before: `new Coin(x, y, fallSpeed)`
  - After: `new Coin(manager)`

---

## Running This Branch

### Compile
```bash
javac -d bin/09-02-without-singleton src/Main.java src/GameEngine.java src/GameLogic.java src/HUD.java src/entities/*.java src/utils/*.java
```

### Run
```bash
cd bin/09-02-without-singleton
java Main
```

### Observe Problems

**Watch for**:
1. ✅ Two `GameManager instance created` messages (different hashCodes)
2. ✅ HUD shows "Score: 0" while actual score increases
3. ✅ Debug messages show different instance hashCodes
4. ✅ "BUG: Reading from wrong instance!" message

**Expected Output**:
```
[DEBUG] GameManager instance created: 498931366
[Main] Created GameManager instance: 498931366
[GameLogic] Using manager instance: 498931366
[DEBUG] GameManager instance created: 349885916  ← Second instance!
[HUD] ❌ BUG: Using own instance, not received instance!

... after collisions ...

[GameManager:498931366] Score updated: 30

HUD DISPLAY:
  Score: 0 points  ← ❌ BUG!
  Manager hashCode: 349885916

[GameEngine] Actual score: 30  ← ✅ Correct
```

---

## Problems Summary

| Problem | Impact | Fix (in 09-03) |
|---------|--------|----------------|
| Multiple instances | State inconsistency | Private constructor |
| Object drilling | Parameter pollution | getInstance() method |
| No protection | Developer mistakes | Singleton enforcement |
| Hard to test | Setup overhead | Single instance control |

---

## Key Learning Points

1. **Global State Needs Protection**
   - Games need shared state (score, settings, etc.)
   - Without protection → multiple instances
   - Multiple instances → chaos

2. **Object Drilling is Real Pain**
   - Parameter passing through 4 levels
   - 6+ constructor modifications for changes
   - Team collaboration nightmare

3. **Compiler Can't Help**
   - Creating `new GameManager()` is valid Java
   - No compile error for logic bug
   - Pattern needed to prevent mistakes

4. **This Happens in Production**
   - New developer creates new instance
   - Copy-paste error
   - Refactoring mistake
   - Hard to debug (timing dependent)

---

## Discussion Questions

1. How many GameManager instances exist in this program?
2. Why does HUD show score = 0?
3. What happens if we add a third class that also creates its own GameManager?
4. How would you debug this if you didn't have the debug print statements?
5. What prevents developers from accidentally creating multiple instances?

---

## Next Step

See **Branch 09-03** for the solution using Singleton pattern!

**Spoiler**: Private constructor + getInstance() = Single instance guaranteed!

---

**Remember**: This branch is intentionally buggy. The bug is real, the solution is coming!
