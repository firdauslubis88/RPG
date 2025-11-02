# Week 9: Foundation & Singleton Pattern

## 🎯 Week Goals

### Primary Learning Objectives
1. **Understand Game Loop Paradigm**
   - Shift from Request-Response to Continuous Loop
   - Separation of update (logic) and draw (rendering)
   - Delta time and frame rate independence

2. **Understand Singleton Pattern**
   - Global state management
   - Single instance guarantee
   - Trade-offs (global state vs testability)

### Secondary Learning Objectives
- Terminal-based rendering fundamentals
- Collision detection basics
- Basic entity management
- Test-driven development for games

---

## 🎮 Game State at End of Week 9

### What Works
✅ Game loop running at 60 FPS
✅ NPC auto-moving left to right
✅ Coins spawning and falling (gravity)
✅ Collision detection (NPC vs Coin)
✅ Score tracking
✅ Terminal rendering (grid display)

### What Doesn't Work Yet
❌ No player control (NPC is automated)
❌ No obstacles (only coins)
❌ No difficulty scaling
❌ No save/load
❌ No boss battle

**Why**: Player control comes in Week 11 (Command pattern). This week focuses on foundation.

---

## 📚 Patterns Covered

### 1. Game Loop Paradigm (Pre-Pattern)

**Not a GoF pattern, but essential game architecture.**

#### The Problem
Web backend model (Request → Process → Response) doesn't work for games. Games need continuous, real-time updates.

#### The Solution
```
while (gameRunning) {
    delta = calculateDeltaTime()
    update(delta)    // Pure logic
    draw()           // Pure rendering
    waitForNextFrame()
}
```

#### Why This Matters
- **Frame rate independence**: Logic runs consistently across different hardware
- **Testability**: Can unit test update() without rendering
- **Maintainability**: Clear separation of concerns

---

### 2. Singleton Pattern

**Category**: Creational Pattern

#### The Problem
Game needs global state (score, game time, player stats) accessible from everywhere. Passing GameManager reference to every class = "object drilling" hell.

#### The Solution
```java
public class GameManager {
    private static GameManager instance;
    private GameManager() {}  // Private constructor
    
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
}
```

#### Why This Matters
- **Single source of truth**: Only one game state
- **Global access**: No parameter passing everywhere
- **Resource efficiency**: Heavy objects created once

#### Trade-offs
⚠️ **Cons**:
- Global state (harder to test in isolation)
- Hidden dependencies (not explicit in signatures)
- Thread safety concerns (needs DCL for multi-threaded)

✅ **Pros**:
- Eliminates object drilling
- Ensures consistency
- Simple access pattern

---

## 🌳 Branch Roadmap

### Branch 09-00: without-game-loop
**Type**: Problem demonstration
**Purpose**: Show why monolithic loop is bad

**What You'll See**:
- Update and render mixed together
- Frame rate coupled to rendering speed
- Untestable code
- Code spaghetti

**Pain Points**:
- Add rendering delay → game logic slows down
- Cannot unit test collision detection
- 150+ lines in one method

**Real-World Parallel**:
This is how beginner game programmers start. Professional engines (Unity, Unreal) all separate update/render.

---

### Branch 09-01: with-game-loop
**Type**: Solution
**Purpose**: Show proper game loop architecture

**What You'll See**:
- Separated update() and draw() methods
- Delta time-based movement
- Testable logic
- Clean structure

**Benefits**:
- Render delay doesn't affect logic
- Can skip frames (choppy visual, smooth gameplay)
- Unit tests work without display
- Maintainable code

**Real-World Standard**:
Every professional game uses this pattern. This is THE foundation of game development.

---

### Branch 09-02: without-singleton
**Type**: Problem demonstration
**Purpose**: Show object drilling problem

**What You'll See**:
- Multiple GameManager instances possible
- State inconsistency bugs
- Constructor parameters everywhere
- Testing nightmare (many mocks needed)

**Pain Points**:
- NPC updates score in instance A
- HUD reads score from instance B
- Score = 0 despite collecting coins!

**Real-World Parallel**:
This happens in microservices with multiple instances. In games, we need ONE source of truth for game state.

---

### Branch 09-03: with-singleton
**Type**: Solution
**Purpose**: Show Singleton pattern implementation

**What You'll See**:
- Private constructor (prevents `new`)
- Static getInstance() method
- Single instance guarantee
- No more parameter passing

**Benefits**:
- Consistent state everywhere
- Clean class constructors
- Easy access pattern

**Trade-offs Discussed**:
- Global state (testing considerations)
- Hidden dependencies
- Thread safety (bonus: DCL implementation)

---

### Branch 09-analysis
**Type**: Comparison & metrics
**Purpose**: Data-driven analysis

**What You'll See**:
- Comparison tables (before vs after)
- Code metrics (LOC, complexity, test coverage)
- Performance metrics (if applicable)
- When-to-use guidelines
- Migration guide (how to refactor)
- Common mistakes & FAQs

---

## 📊 Success Metrics

### For Students
By end of Week 9, students should be able to:
- [ ] Explain WHY game loop is needed (not just how)
- [ ] Implement separated update/render
- [ ] Calculate delta time correctly
- [ ] Explain Singleton pattern use cases
- [ ] Implement Singleton correctly
- [ ] Discuss Singleton trade-offs

### For Code
- [ ] Game runs at stable 60 FPS
- [ ] All unit tests passing
- [ ] Update() has no rendering code
- [ ] Draw() has no logic code
- [ ] GameManager truly singleton (tested)

---

## 🧪 Demonstration Scenarios

### Demo 1: Frame Rate Coupling
**Setup**: Branch 09-00
**Action**: Add `Thread.sleep(100)` in rendering
**Observe**: Game logic slows to 50% speed
**Learning**: "Logic shouldn't wait for rendering!"

### Demo 2: Testing Impossibility
**Setup**: Branch 09-00
**Action**: Try to write unit test for collision
**Observe**: Cannot separate logic from display
**Learning**: "Need separation for testability!"

### Demo 3: Multiple Instances Bug
**Setup**: Branch 09-02
**Action**: Create two GameManager instances
**Observe**: State inconsistency (score = 0 in HUD)
**Learning**: "Need single source of truth!"

### Demo 4: Singleton Solution
**Setup**: Branch 09-03
**Action**: Try to create second instance
**Observe**: Compiler error (private constructor)
**Learning**: "Pattern enforces single instance!"

---

## 🎓 Teaching Notes

### Key Concepts to Emphasize

#### 1. Paradigm Shift
This is bigger than just a pattern. Students are shifting from:
- **Passive** (waiting for HTTP requests) 
- to **Active** (continuous game loop)

Analogy: Restaurant waiter vs assembly line worker.

#### 2. Delta Time Formula
```
distance = velocity × time
newPos = oldPos + (velocity × deltaTime)
```

Why important:
- 60 FPS: delta ≈ 0.016s
- 30 FPS: delta ≈ 0.033s
- Same velocity × different delta = same distance!

#### 3. Singleton Not Always Answer
Important to discuss when NOT to use:
- Business logic classes (use DI instead)
- Objects that need multiple contexts
- Objects with complex lifecycle

---

### Common Student Questions

**Q**: "Kenapa tidak pakai Spring @Autowired untuk singleton?"
**A**: Game tidak pakai Spring (too heavy). Tapi konsep sama: DI container juga ensure single instance. Singleton = manual DI.

**Q**: "Bagaimana dengan multiplayer game? Multiple game states?"
**A**: Setiap player punya GameState sendiri. Singleton bisa dimodifikasi jadi "Multiton" (map of instances). Advanced topic.

**Q**: "Fixed timestep vs variable timestep?"
**A**: Variable (delta time) = most common. Fixed = untuk physics determinism (fighting games, networking). Topic untuk advanced course.

**Q**: "Thread safety Singleton?"
**A**: Simple: Double-Checked Locking (DCL). Better: Initialization-on-demand holder idiom. Show bonus branch if time allows.

---

## 📦 Deliverables

### Student Deliverables
- [ ] Working game with separated update/render
- [ ] Singleton GameManager implementation
- [ ] Unit tests for GameLogic
- [ ] Documentation explaining patterns

### Teaching Material Deliverables
- [ ] All 5 branches implemented and tested
- [ ] SCENARIO.md for each branch
- [ ] GUIDELINE.md for each branch
- [ ] PROMPT.md for each branch
- [ ] Analysis document with metrics
- [ ] Common mistakes FAQ

---

## ⏱️ Time Allocation

### Lecture Time (2 hours)
- 30 min: Game loop paradigm explanation
- 15 min: Demo problematic code (09-00)
- 15 min: Demo solution (09-01)
- 30 min: Singleton pattern explanation
- 15 min: Demo problematic code (09-02)
- 15 min: Demo solution (09-03)

### Lab Time (3 hours)
- 1 hour: Students implement game loop
- 1 hour: Students implement Singleton
- 1 hour: Testing and experimentation

### Homework (5 hours)
- Refactor existing project to use patterns
- Write reflection on trade-offs
- Prepare questions for next week

---

## 🔗 Connections to Other Weeks

### Prerequisites from Previous Weeks
- Java basics ✅
- OOP fundamentals ✅
- SOLID principles ✅
- Iterator pattern ✅

### Preparation for Next Weeks
- **Week 10**: GameManager will spawn obstacles (Factory)
- **Week 11**: GameManager will handle events (Observer)
- **Week 12**: GameManager will manage states (State pattern)

---

## 📚 Additional Resources

### Recommended Reading
- "Game Programming Patterns" by Robert Nystrom (Game Loop chapter)
- "Effective Java" by Joshua Bloch (Singleton chapter)
- Unity documentation on Update() and FixedUpdate()

### Code Examples from Industry
- LibGDX ApplicationListener (game loop)
- Unity MonoBehaviour (update/render separation)
- Minecraft World singleton

---

## ✅ Week 9 Checklist

Before moving to Week 10, ensure:
- [ ] Game loop running smoothly at 60 FPS
- [ ] Update/render completely separated
- [ ] GameManager implemented as Singleton
- [ ] All tests passing
- [ ] Students understand WHY (not just HOW)
- [ ] Students can explain trade-offs
- [ ] All documentation complete
- [ ] All demonstration scenarios tested

---

**Ready to start?** Begin with Branch 09-00 → Read SCENARIO.md first! 🚀
