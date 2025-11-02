# Week 09: Game Loop Pattern and Singleton Pattern

## Overview
Week 09 demonstrates the evolution of a simple game through four progressive branches, teaching two fundamental design patterns: **Game Loop** and **Singleton**.

This is a **progressive learning approach** where each branch builds on the previous one, maintaining solutions while introducing new challenges.

---

## Branch Progression

### 09-00: Without Game Loop (The Problem)
**Purpose**: Demonstrate monolithic anti-pattern

**Problems Shown**:
- ❌ Frame rate coupling: Render delays slow down game logic (80% slower)
- ❌ Untestability: Cannot unit test logic without rendering
- ❌ Poor maintainability: 150+ lines in single main() method
- ❌ Flickering: Full screen clears every frame
- ❌ No scalability: Adding entities causes exponential slowdown

**Key Files**: [docs/09-00-problem.md](09-00-problem.md)

**Intentionally bad code for educational purposes!**

---

### 09-01: With Game Loop (First Solution)
**Purpose**: Solve all 09-00 problems with proper architecture

**Solutions Implemented**:
- ✅ Separated update() and draw() methods
- ✅ Delta time for frame-rate independence
- ✅ Fully testable game logic (100% test coverage!)
- ✅ Clean code structure (Main.java: 3 lines!)
- ✅ Selective rendering (no flickering)
- ✅ 60 FPS stable frame rate

**Key Pattern**: **Game Loop Pattern**
```java
while (running) {
    float delta = calculateDelta();
    update(delta);  // Logic only
    draw();         // Rendering only
    sync();         // Frame rate control
}
```

**Key Files**: [docs/09-01-solution.md](09-01-solution.md)

**Result**: Professional game architecture, 30x faster than 09-00!

---

### 09-02: Without Singleton (New Problems Emerge)
**Purpose**: Show problems when expanding game with features

**New Features Added**:
- Score tracking
- HUD display
- Game time tracking
- Level management

**NEW Problems**:
- ❌ Multiple instances: HUD creates own GameManager (different hashCode!)
- ❌ Object drilling: GameManager passed through 4 constructor levels
- ❌ State inconsistency: HUD shows score = 0, actual score = 30 (BUG!)
- ❌ No compile-time protection: Anyone can create new instances

**Why Problems Appear NOW**:
```
09-01: Simple game (NPC + Coins)
  └─ Each component managed its own state
  └─ No need for shared data
  └─ ✅ Worked perfectly!

09-02: Added features (Score, HUD, Time)
  └─ Multiple components need same data
  └─ Need global state management
  └─ ❌ Current design doesn't scale!
```

**Key Files**: [docs/09-02-problem.md](09-02-problem.md)

**Solutions Carried Forward**: All 09-01 solutions maintained (selective rendering, delta time, etc.)

**Intentionally buggy code for educational purposes!**

---

### 09-03: With Singleton (Final Solution)
**Purpose**: Solve all 09-02 problems using Singleton pattern

**Solutions Implemented**:
- ✅ Single instance guarantee: Private constructor prevents multiple instances
- ✅ No object drilling: Zero constructor parameters!
- ✅ State consistency: HUD shows correct score (30 points, not 0!)
- ✅ Compile-time protection: `new GameManager()` causes compiler error

**Key Pattern**: **Singleton Pattern**
```java
public class GameManager {
    // 1. Static instance variable
    private static GameManager instance = null;

    // 2. Private constructor
    private GameManager() { }

    // 3. Public static accessor
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
}
```

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

// Anywhere: GameManager.getInstance().addScore(10);
```

**Key Files**: [docs/09-03-solution.md](09-03-solution.md)

**Solutions Carried Forward**: All 09-01 AND 09-02 solutions maintained!

---

## Progressive Learning Summary

```
09-00: Monolithic (all problems)
  ❌ No game loop
  ❌ Frame rate coupling
  ❌ Untestable
  ❌ Flickering
  ❌ Unscalable

      ↓ Apply Game Loop Pattern

09-01: Game Loop Pattern (solve 4 problems)
  ✅ Separated update/draw
  ✅ Delta time
  ✅ Testable logic
  ✅ Selective rendering
  ✅ Clean architecture

      ↓ Add new features (Score, HUD)

09-02: Add Features (NEW problem appears!)
  ✅ Keep all 09-01 solutions
  ❌ NEW: Multiple instances
  ❌ NEW: Object drilling
  ❌ NEW: State inconsistency

      ↓ Apply Singleton Pattern

09-03: Singleton Pattern (solve NEW problems)
  ✅ Keep all 09-01 solutions
  ✅ SOLVE: Single instance
  ✅ SOLVE: No object drilling
  ✅ SOLVE: State consistency
```

**Key Insight**: We don't regress - we progress! Each branch maintains previous solutions while solving new challenges.

---

## Running the Demos

### Branch 09-00 (Problem)
```bash
git checkout 09-00-without-game-loop
javac -d bin/09-00-without-game-loop src/Main.java src/entities/*.java src/utils/*.java
cd bin/09-00-without-game-loop
java Main
```

**Observe**: Slow (2 FPS), flickering, all code in main()

### Branch 09-01 (Solution)
```bash
git checkout 09-01-with-game-loop
javac -d bin/09-01-with-game-loop src/Main.java src/GameEngine.java src/GameLogic.java src/entities/*.java src/utils/*.java
cd bin/09-01-with-game-loop
java Main
```

**Observe**: Fast (60 FPS), smooth, clean architecture

### Branch 09-02 (New Problem)
```bash
git checkout 09-02-without-singleton
javac -d bin/09-02-without-singleton src/Main.java src/GameEngine.java src/GameLogic.java src/HUD.java src/entities/*.java src/utils/*.java
cd bin/09-02-without-singleton
java Main
```

**Observe**: Two instances created (different hashCodes), HUD shows 0, actual score increases

### Branch 09-03 (Solution)
```bash
git checkout 09-03-with-singleton
javac -d bin/09-03-with-singleton src/Main.java src/GameEngine.java src/GameLogic.java src/HUD.java src/entities/*.java src/utils/*.java
cd bin/09-03-with-singleton
java Main
```

**Observe**: Single instance (same hashCode everywhere), HUD shows correct score, clean constructors

---

## Comparison Table

| Aspect | 09-00 | 09-01 | 09-02 | 09-03 |
|--------|-------|-------|-------|-------|
| **FPS** | 2 | 60 | 60 | 60 |
| **Flickering** | Yes | No | No | No |
| **Testability** | 0% | 100% | 100% | 100% |
| **Main LOC** | 150+ | 3 | 32 | 3 |
| **Instances** | N/A | N/A | 2+ | 1 |
| **Object Drilling** | N/A | N/A | 4 levels | 0 levels |
| **HUD Score** | N/A | N/A | 0 (wrong) | 30 (correct) |
| **Constructor Params** | N/A | N/A | Many | None |

---

## Key Learning Outcomes

### 1. Game Loop Pattern
**When**: Always! Foundation of all games.

**What**:
- Separate update() and draw()
- Use delta time for frame-rate independence
- Control frame rate with sync()

**Why**:
- Enables testing
- Improves performance
- Professional architecture

**Industry Usage**:
- Unity: `Update()` / `FixedUpdate()`
- Unreal: `Tick(DeltaTime)`
- Godot: `_process(delta)`

### 2. Singleton Pattern
**When**: True global state (configuration, managers, resources)

**What**:
- Private constructor
- Static instance variable
- Public getInstance() method

**Why**:
- Single instance guarantee
- Global access
- No object drilling

**Trade-offs**:
- ✅ Clean code, no parameter passing
- ✅ Compile-time protection
- ⚠️ Global state concerns
- ⚠️ Hidden dependencies

### 3. Progressive Development
**Lesson**: Real software evolves through iterations.

**Pattern**:
1. Start simple (works)
2. Add features (problems emerge)
3. Apply patterns (problems solved)
4. Maintain solutions (no regression)
5. Repeat

This mirrors real-world software development!

---

## Discussion Questions

### Game Loop Pattern
1. Why does separating update/draw solve flickering?
2. How does delta time ensure consistent gameplay speed?
3. Why is testability important for games?
4. What happens if update() takes longer than 16ms?

### Singleton Pattern
5. What prevents creating multiple GameManager instances?
6. Why is getInstance() static?
7. When should you NOT use Singleton?
8. How is Singleton different from static class?

### Design Thinking
9. Could we solve object drilling WITHOUT Singleton? (Yes - Dependency Injection)
10. What are the trade-offs of different approaches?
11. Why didn't 09-00 need Singleton? (Scope was smaller)
12. How do you decide when to apply a pattern?

---

## File Structure

```
rpg/
├── src/
│   ├── Main.java                   # Entry point (varies by branch)
│   ├── GameEngine.java             # Game loop (09-01+)
│   ├── GameLogic.java              # Pure logic (09-01+)
│   ├── HUD.java                    # Heads-up display (09-02+)
│   ├── entities/
│   │   ├── NPC.java                # Non-player character
│   │   ├── Coin.java               # Collectible
│   │   └── GameManager.java       # Global state (09-02+)
│   └── utils/
│       └── GridRenderer.java       # Rendering utilities
├── test/
│   └── GameLogicTest.java          # Unit tests (09-01+)
├── docs/
│   ├── WEEK-09-OVERVIEW.md         # This file
│   ├── 09-00-problem.md            # Monolithic problems
│   ├── 09-01-solution.md           # Game loop solution
│   ├── 09-02-problem.md            # Multiple instance problems
│   └── 09-03-solution.md           # Singleton solution
└── bin/
    ├── 09-00-without-game-loop/
    ├── 09-01-with-game-loop/
    ├── 09-02-without-singleton/
    └── 09-03-with-singleton/
```

---

## Teaching Strategy

### Week Structure

**Day 1**: Branch 09-00
- Run demo (observe slowness)
- Identify problems in code
- Discuss why monolithic is bad

**Day 2**: Branch 09-01
- Run demo (observe improvement)
- Explain game loop pattern
- Show delta time concept
- Run tests (demonstrate testability)

**Day 3**: Branch 09-02
- Introduce new features (score, HUD)
- Run demo (observe bug: HUD shows 0)
- Debug: Find two instances!
- Discuss object drilling
- Explain why problem emerged NOW

**Day 4**: Branch 09-03
- Introduce Singleton pattern
- Show private constructor
- Run demo (observe fix: HUD correct!)
- Compare all four branches
- Discuss when to use/not use Singleton

**Day 5**: Discussion & Practice
- Design patterns principles
- Trade-offs discussion
- Alternative solutions
- Practice implementing patterns

---

## Alternative Solutions Discussion

### For Object Drilling

**1. Singleton** (Used in 09-03)
```java
GameManager.getInstance().addScore(10);
```
- ✅ Clean constructors
- ⚠️ Global state

**2. Dependency Injection**
```java
public GameLogic(GameManager manager) {
    this.manager = manager;
}
```
- ✅ Explicit dependencies
- ⚠️ Object drilling returns

**3. Service Locator**
```java
ServiceLocator.get(GameManager.class).addScore(10);
```
- ✅ Flexible
- ⚠️ Less type-safe

**Trade-offs**: Discuss with students!

---

## Summary

**Week 09 teaches**:
1. ✅ Game Loop Pattern (fundamental)
2. ✅ Singleton Pattern (global state)
3. ✅ Progressive development (real-world approach)
4. ✅ Pattern trade-offs (no silver bullets)
5. ✅ Testing importance (TDD principles)

**Key Takeaways**:
- Patterns solve problems (don't use without reason)
- Solutions evolve as requirements grow
- Maintain previous wins while solving new problems
- Always consider trade-offs
- Clean architecture enables growth

---

**Remember**: Perfect is the enemy of good. Start simple, improve iteratively, apply patterns when needed!
