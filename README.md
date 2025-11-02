# Branch 09-00: Without Game Loop

## Overview
This branch demonstrates **intentionally bad design** - a monolithic game implementation where update logic and rendering are completely mixed together. This is an anti-pattern used for educational purposes to show WHY proper game loop architecture is needed.

## Purpose
Educational demonstration of problems that occur when:
- Update and render logic are mixed
- No separation of concerns
- Everything in one giant main() method
- Frame rate coupling affects gameplay

## Quick Start

### Compile
```bash
cd src
javac Main.java entities/*.java utils/*.java
```

### Run
```bash
java Main
```

The game will run for 50 frames and then stop, showing you how slow it becomes due to coupling.

## What You'll Observe

1. **Slow Performance**: Expected ~10 FPS, actually ~2 FPS (80% slower!)
2. **Coupled Timing**: Rendering delays directly slow down game logic
3. **Messy Code**: 150+ lines in main() with no structure
4. **Untestable**: Cannot write unit tests (see [MainTest.java](test/MainTest.java))

## Problems Demonstrated

### 1. Frame Rate Coupling
- Render speed affects game logic speed
- Each `Thread.sleep()` delays the entire game
- Real-world equivalent: GPU bottleneck slows physics

### 2. Untestability
- Cannot unit test collision detection
- Cannot test scoring logic separately
- Cannot run in CI/CD pipeline
- See [MainTest.java](test/MainTest.java) for examples

### 3. Poor Maintainability
- All code in one 150+ line method
- Logic mixed with rendering
- Hard to debug and extend

### 4. No Scalability
- 1 entity: ~2 FPS
- 10 entities: Would be ~0.2 FPS
- 100 entities: Game freezes

## File Structure

```
src/
├── Main.java                 # ❌ Monolithic main() with everything
├── entities/
│   ├── NPC.java             # Auto-moving character
│   └── Coin.java            # Falling collectible
└── utils/
    └── GridRenderer.java    # Terminal rendering utilities

test/
└── MainTest.java            # Demonstrates untestability

PROBLEM.md                   # Detailed problem analysis
README.md                    # This file
```

## Key Anti-Patterns (Intentional!)

1. ❌ **No Separation**: Update and draw mixed
2. ❌ **No Delta Time**: Fixed Thread.sleep()
3. ❌ **Everything in main()**: No helper methods
4. ❌ **No Encapsulation**: All local variables
5. ❌ **Side Effects**: System.out mixed with logic

## Code Highlights

### The Problematic Main Loop
```java
while (true) {
    npc.moveRight();                      // Update
    GridRenderer.drawEntity(...);         // Render immediately!
    Thread.sleep(100);                    // Delay affects BOTH!

    coin.fall();                          // Update
    GridRenderer.drawEntity(...);         // Render immediately!
    Thread.sleep(100);                    // More delays!

    // Collision + scoring + rendering all mixed
    if (collision) {
        score += 10;
        System.out.println(...);
        Thread.sleep(100);
    }
}
```

### Why This Is Bad
- **Expected**: 10 updates/second
- **Actual**: 2 updates/second
- **Loss**: 80% performance

## Learning Objectives

After experiencing this code, you should understand:

1. **Why separation matters**: Logic and rendering must be independent
2. **Frame rate coupling**: Render speed shouldn't affect game speed
3. **Testability**: Separation enables automated testing
4. **Real-world relevance**: All professional games separate update/render

## Comparison: Industry Standards

| This Code | Professional Games |
|-----------|-------------------|
| Mixed update/render | Separated update() and draw() |
| Fixed delays | Delta time calculation |
| 2 FPS unstable | 60 FPS stable |
| 0% testable | 70%+ test coverage |
| 150+ line main() | Clean architecture |

## Next Steps

See **branch 09-01-with-game-loop** for the solution:
- ✅ Separated update() and draw()
- ✅ Proper delta time handling
- ✅ Testable game logic
- ✅ Clean architecture
- ✅ Stable 60 FPS

## Discussion Questions

1. What happens if we add 10 more coins?
2. How would you test collision detection with this architecture?
3. Why does rendering delay affect gameplay speed?
4. Can you identify all the SOLID principles being violated?

## References

- [Game Programming Patterns - Game Loop](http://gameprogrammingpatterns.com/game-loop.html)
- [Fix Your Timestep](https://gafferongames.com/post/fix_your_timestep/)

---

**⚠️ WARNING**: This code is intentionally bad for educational purposes. **DO NOT use this pattern in production!**
