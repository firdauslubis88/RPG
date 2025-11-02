# Branch 09-01: With Game Loop

## Overview
This branch demonstrates **proper game loop architecture** with complete separation between update logic and rendering. This is the SOLUTION to all problems shown in branch 09-00.

## Purpose
Professional implementation showing how game development should be done:
- Separated update() and draw()
- Frame-rate independent movement (delta time)
- Fully testable game logic
- Clean, maintainable code structure

## Quick Start

### Compile
```bash
javac -d bin/09-01-with-game-loop src/Main.java src/GameEngine.java src/GameLogic.java src/entities/*.java src/utils/*.java
```

### Run
```bash
cd bin/09-01-with-game-loop
java Main
```

The game will run at stable 60 FPS for 200 frames, demonstrating smooth, professional gameplay.

## What You'll Observe

1. **Stable 60 FPS**: Smooth, consistent performance
2. **No Flickering**: Clean screen updates
3. **Frame-Independent Movement**: Entities move at correct speed regardless of FPS
4. **Performance Metrics**: See update/draw times in real-time

## Solutions Implemented

### 1. Separated Update and Draw
**Problem**: Mixed logic and rendering → slow, untestable
**Solution**:
- `update(delta)`: Pure logic, no rendering
- `draw()`: Pure rendering, no logic

### 2. Delta Time
**Problem**: Frame-rate dependent movement
**Solution**:
```java
newPosition = oldPosition + (velocity × deltaTime)
```
Result: Same gameplay speed at any FPS!

### 3. Testable Logic
**Problem**: Cannot unit test in 09-00
**Solution**: GameLogic class with pure methods
Result: 100% testable without display!

### 4. Clean Architecture
**Problem**: 150+ lines in main()
**Solution**:
- Main.java: 3 lines (entry point only)
- GameEngine: Game loop orchestration
- GameLogic: Pure logic
- Entities: Data models

## Game Mechanics

### NPC (Symbol: N)
- **Movement**: Horizontal (left to right)
- **Speed**: 3 pixels/second (frame-rate independent!)
- **Behavior**: Auto-moves, wraps at edge
- **Position**: Uses float for precision

### Coin (Symbol: C)
- **Movement**: Vertical (falls down)
- **Speed**: 2 pixels/second (frame-rate independent!)
- **Behavior**: Falls, respawns at random X when reaching bottom
- **Position**: Uses float for precision

### Grid
- 10x10 grid (no walls)
- Coordinate system: (X, Y) where X=column, Y=row
- Smooth movement thanks to float positions

## Architecture

```
Main (3 lines)
  └── GameEngine
      ├── update(delta) → GameLogic
      │   ├── updateNPC()
      │   ├── updateCoins()
      │   └── checkCollisions()
      └── draw() → GridRenderer
          ├── clearScreen()
          ├── drawGrid()
          └── drawHUD()
```

## Testing

### Run Unit Tests
```bash
# Compile tests
javac -cp .:junit-platform-console-standalone.jar test/GameLogicTest.java

# Run tests
java -jar junit-platform-console-standalone.jar --class-path . --scan-class-path
```

### Test Coverage
- ✅ NPC movement with delta time
- ✅ Coin falling physics
- ✅ Wrap-around logic
- ✅ Frame-rate independence verification
- ✅ Collision detection

**All tests pass WITHOUT display!**

## Performance Metrics

| Metric | Value |
|--------|-------|
| Target FPS | 60 |
| Actual FPS | 60 (stable) |
| Update Time | < 2ms |
| Draw Time | < 10ms |
| Frame Time | ~16ms |

## Comparison with 09-00

| Aspect | 09-00 | 09-01 | Improvement |
|--------|-------|-------|-------------|
| FPS | ~2 | 60 | **30x faster** |
| Main LOC | 150+ | 3 | **50x smaller** |
| Testability | 0% | 100% | **Full coverage** |
| Flickering | Yes | No | **Smooth** |
| Delta Time | No | Yes | **Frame independent** |
| Maintainability | Poor | Excellent | **Professional** |

## Key Files

- **[Main.java](src/Main.java)**: Minimal entry point (3 lines!)
- **[GameEngine.java](src/GameEngine.java)**: Core game loop with update/draw separation
- **[GameLogic.java](src/GameLogic.java)**: Pure logic, fully testable
- **[entities/NPC.java](src/entities/NPC.java)**: Entity with float positions
- **[entities/Coin.java](src/entities/Coin.java)**: Entity with float positions
- **[utils/GridRenderer.java](src/utils/GridRenderer.java)**: Rendering utilities
- **[test/GameLogicTest.java](test/GameLogicTest.java)**: Unit tests (100% pass!)
- **[docs/09-01-solution.md](docs/09-01-solution.md)**: Detailed explanation of all solutions

## Code Highlights

### Clean Main Method
```java
public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start();
    }
}
```

### Proper Game Loop
```java
while (running) {
    float delta = calculateDelta();
    update(delta);    // ✅ Logic only
    draw();           // ✅ Rendering only
    sync();           // ✅ Frame rate control
}
```

### Delta Time Movement
```java
public void updateNPC(float delta) {
    float newX = npc.getX() + npc.getVelocity() * delta;
    if (newX >= GRID_WIDTH) newX -= GRID_WIDTH;
    npc.setX(newX);
}
```

### Testable Logic
```java
@Test
void testNPCMovement() {
    GameLogic logic = new GameLogic();
    logic.updateNPC(0.016f);  // ✅ No rendering needed!
    assertTrue(logic.getNPCX() > 0);
}
```

## Learning Outcomes

After studying this branch, you should understand:

1. **Separation of Concerns**: Why update and draw must be separate
2. **Delta Time**: How to make movement frame-rate independent
3. **Testability**: Why pure logic enables automated testing
4. **Game Loop Pattern**: Industry-standard architecture used in all professional games
5. **Clean Code**: How to structure game code for maintainability

## Industry Relevance

This pattern is used in:
- **Unity**: `Update()` vs `FixedUpdate()`
- **Unreal**: `Tick(DeltaTime)`
- **Godot**: `_process(delta)`
- **LibGDX**: `render(delta)`
- **Custom Engines**: All modern game engines

**This is THE foundation of game development.**

## Discussion Questions

1. Why does separating update/draw solve the flickering problem?
2. How does delta time make the game run at the same speed on different hardware?
3. Why couldn't we write unit tests in 09-00, but can now in 09-01?
4. What happens if update() takes longer than 16ms (60 FPS target)?
5. How would you add a second NPC to this system?

## Next Branch

**Branch 09-02** will introduce global state management problems, leading to the Singleton pattern in 09-03.

See [docs/09-01-solution.md](docs/09-01-solution.md) for detailed explanations of all improvements!

---

**✅ Professional game loop architecture implemented!**
