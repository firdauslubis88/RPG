# Week 09 Problem Statement: Game Loop Pattern

## Overview
This week introduces the **Game Loop Pattern** - the fundamental architecture pattern used in all game development. Students will learn why separating game logic from rendering is critical for performance, testability, and maintainability.

---

## The Core Problem

When building interactive real-time applications (games, simulations, animations), we face several interconnected challenges:

### 1. Frame Rate Coupling
**Problem**: Game logic speed should NOT depend on rendering speed.

**Real-world impact**:
- Slow GPU → gameplay slows down
- Different hardware → different gameplay experience
- Cannot skip rendering without breaking game logic

**Example**:
```java
// ❌ BAD: Logic tied to rendering delay
movePlayer();
Thread.sleep(100);  // Rendering delay affects gameplay!
drawPlayer();
```

### 2. Untestability
**Problem**: Cannot test game logic without rendering to screen.

**Real-world impact**:
- No automated testing possible
- Cannot run tests in CI/CD pipelines
- Manual testing only = slow, error-prone
- Cannot test on headless servers

**Example**:
```java
// ❌ BAD: Cannot test collision detection without display
void main() {
    while (true) {
        // All logic mixed with System.out.println
        // How to unit test this?
    }
}
```

### 3. Poor Maintainability
**Problem**: When logic and rendering are mixed, code becomes impossible to maintain.

**Real-world impact**:
- Bug in physics? Must search through rendering code
- Want to optimize graphics? Risk breaking game logic
- Team cannot work in parallel (logic + graphics developers)
- Code reviews become nightmares

**Example**:
```java
// ❌ BAD: 150+ lines in main() with logic and rendering mixed
void main() {
    while (true) {
        // Move NPC
        // Draw NPC
        // Check collision
        // Draw effect
        // Update score
        // Draw score
        // ... everything mixed together
    }
}
```

### 4. Visual Flickering
**Problem**: Clearing screen, then running logic, then drawing creates visible gaps.

**Real-world impact**:
- Poor user experience
- Looks unprofessional
- Difficult to see what's happening
- Eye strain for players

**Example**:
```java
// ❌ BAD: Long gap between clear and draw
clearScreen();
npc.move();          // Logic takes time
Thread.sleep(100);   // More delay
coin.fall();         // More logic
Thread.sleep(100);   // More delay
drawEverything();    // Finally draw = FLICKER!
```

---

## Why This Matters

### Industry Context
Every professional game engine uses the Game Loop pattern:
- **Unity**: `Update()` vs `FixedUpdate()` vs `LateUpdate()`
- **Unreal Engine**: `Tick(DeltaTime)`
- **Godot**: `_process(delta)` vs `_physics_process(delta)`
- **LibGDX**: `render(delta)`
- **Custom Engines**: All use separation of update/draw

**This is not optional knowledge - this is THE foundation of game development.**

### Real-World Scenarios

**Mobile Games**:
- Battery saver mode → reduce draw FPS to 30
- Keep logic at 60 FPS for responsive controls
- Impossible without separation!

**VR Games**:
- Must maintain 90 FPS to prevent motion sickness
- GPU lag should not slow down physics
- Input lag = player discomfort

**Multiplayer Games**:
- Server runs game logic (no rendering)
- Clients render server state
- Same code, different contexts
- Only possible with separated architecture

**Competitive Games (eSports)**:
- Frame drops should not affect gameplay
- Input response more important than graphics
- Frame skipping maintains responsiveness

---

## Learning Objectives

By the end of Week 09, students should understand:

1. **Separation of Concerns**: Why update() and draw() must be separate
2. **Delta Time**: How to make movement frame-rate independent
3. **Testability**: Why pure logic enables automated testing
4. **Game Loop Pattern**: Industry-standard architecture
5. **Performance**: How separation enables optimization
6. **Real-world Application**: Where this pattern is used

---

## Week 09 Branches

### Branch 09-00: The Problem (Monolithic Architecture)
**Purpose**: Demonstrate what happens when you DON'T use game loop pattern.

**Intentional Problems**:
- All code in main() (150+ lines)
- Logic mixed with rendering
- Frame rate coupling (2 FPS instead of 10 FPS)
- Untestable (all tests fail)
- Severe flickering
- Poor maintainability

**Learning Point**: "This is what NOT to do"

See: [docs/09-00-problem.md](09-00-problem.md)

### Branch 09-01: The Solution (Game Loop Pattern)
**Purpose**: Demonstrate proper game loop architecture.

**Solutions Implemented**:
- Separated update() and draw()
- Delta time for frame-rate independence
- Clean architecture (3-line main!)
- 100% testable logic
- Smooth rendering (60 FPS)
- Excellent maintainability

**Learning Point**: "This is professional game development"

See: [docs/09-01-solution.md](09-01-solution.md)

### Branch 09-02: New Problem (Global State)
**Purpose**: Introduce global state management issues (leads to Singleton).

**Coming next...**

### Branch 09-03: Singleton Pattern
**Purpose**: Solve global state problems with Singleton pattern.

**Coming next...**

---

## Success Criteria

Students should be able to:

✅ Explain why separating update and draw is critical
✅ Implement delta time based movement
✅ Write unit tests for game logic without display
✅ Identify frame rate coupling in code
✅ Refactor monolithic game loop into clean architecture
✅ Apply this pattern to their own projects

---

## Discussion Questions

1. Why does render delay affect gameplay in 09-00 but not in 09-01?
2. How does delta time ensure consistent movement across different hardware?
3. Why is testing impossible in 09-00 but trivial in 09-01?
4. What would happen if update() takes longer than 16ms (60 FPS target)?
5. How would you implement slow-motion or fast-forward with this architecture?
6. In a multiplayer game, which part runs on server vs client?

---

## Resources

- [Game Programming Patterns - Game Loop](http://gameprogrammingpatterns.com/game-loop.html)
- [Fix Your Timestep](https://gafferongames.com/post/fix_your_timestep/)
- [Unity Execution Order](https://docs.unity3d.com/Manual/ExecutionOrder.html)
- [LibGDX Game Loop](https://libgdx.com/wiki/app/the-life-cycle)

---

**This week builds the foundation for all future patterns. Master this, and the rest becomes easier!**
