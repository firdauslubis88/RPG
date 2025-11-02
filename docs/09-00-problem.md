# Branch 09-00: Without Game Loop (Problem Demonstration)

## Purpose
This branch intentionally demonstrates **bad architecture** - a monolithic game loop where logic and rendering are completely mixed. This is the PROBLEM that the Game Loop pattern solves.

**⚠️ This code is intentionally bad for educational purposes!**

---

## What This Branch Demonstrates

### ❌ Problem 1: Monolithic Main Method

**Code**: 150+ lines all in `main()`

```java
public static void main(String[] args) {
    // Initialize
    NPC npc = new NPC(5, 5, 1);
    Coin coin = new Coin(2, 0, 1);
    int score = 0;
    int frameCount = 0;

    while (true) {
        frameCount++;

        // Clear screen
        GridRenderer.clearScreen();
        System.out.println("=".repeat(40));

        // Move NPC
        npc.moveRight();
        Thread.sleep(100);  // ❌ Rendering delay!
        npc.wrapAtEdge(10);

        // Move coin
        coin.fall();
        Thread.sleep(100);  // ❌ More delay!

        // Check collision
        if (Math.abs(npc.getX() - coin.getX()) < 1 &&
            Math.abs(npc.getY() - coin.getY()) < 1) {
            score += 10;
            coin.respawn();
        }

        // Render everything
        char[][] grid = createGrid();
        grid[npc.getY()][npc.getX()] = 'N';
        grid[coin.getY()][coin.getX()] = 'C';
        drawGrid(grid);

        // Draw HUD
        System.out.println("Score: " + score);
        System.out.println("Frame: " + frameCount);

        Thread.sleep(100);  // ❌ Target 10 FPS
    }
}
```

**Problems**:
- Everything in one method - impossible to maintain
- Cannot extract logic for testing
- Cannot reuse any part of this code
- Changes to rendering require touching logic
- Team cannot work in parallel

---

### ❌ Problem 2: Frame Rate Coupling

**Expected**: 10 FPS (100ms per frame)
**Actual**: ~2 FPS (500ms per frame)

**Why?**
```java
GridRenderer.clearScreen();   // ~0ms
npc.moveRight();              // ~0ms
Thread.sleep(100);            // ❌ 100ms DELAY
npc.wrapAtEdge(10);           // ~0ms
coin.fall();                  // ~0ms
Thread.sleep(100);            // ❌ 100ms DELAY
// ... collision detection
// ... rendering
Thread.sleep(100);            // ❌ 100ms DELAY
```

**Total**: 300ms+ delays = 80% slower than expected!

**Real-world Impact**:
- Slow GPU → gameplay slows down
- Different machines → different gameplay speed
- Cannot maintain consistent experience

---

### ❌ Problem 3: Untestable Code

**Test Attempt**:
```java
@Test
void testCollisionDetection() {
    // ❌ How to test?
    // All logic is inside main()
    // Cannot call it from test
    // Cannot mock System.out
    // Cannot run without display

    fail("Impossible to test! Logic is inside main()...");
}
```

**Result**: ALL tests fail!

**Why Testing Fails**:
1. Logic is not extracted to methods
2. System.out.println mixed with logic
3. Thread.sleep blocks test execution
4. No way to verify state without rendering
5. Cannot run in CI/CD pipelines

---

### ❌ Problem 4: Severe Flickering

**Visible Behavior**:
- Screen clears
- Long pause (logic + delays)
- Content appears
- Immediately clears again
- Repeat = FLICKER!

**Timeline of One Frame**:
```
Time 0ms:    clearScreen()           ← Screen blank
Time 0ms:    print separator         ← Separator visible
Time 0ms:    npc.moveRight()
Time 100ms:  sleep(100)              ← Still blank!
Time 100ms:  coin.fall()
Time 200ms:  sleep(100)              ← Still blank!
Time 200ms:  collision check
Time 200ms:  drawGrid()              ← Finally content appears!
Time 200ms:  drawHUD()
Time 300ms:  sleep(100)
Time 300ms:  LOOP → clearScreen()    ← Blank again!
```

**Gap between clear and draw**: 200ms+
**Result**: Severe visible flickering

---

### ❌ Problem 5: Poor Entity Design

**Code**:
```java
public class NPC {
    private int x;     // ❌ int, not float
    private int y;
    private int speed; // ❌ pixels per frame, not per second

    public void moveRight() {
        x += speed;  // ❌ No delta time!
    }
}
```

**Problems**:
- Integer positions → jerky movement
- Speed in pixels/frame → frame rate dependent
- No velocity concept → physics impossible
- Methods modify state directly → hard to test

---

## Comparison with Professional Code

| Aspect | 09-00 (This Branch) | Professional Games |
|--------|---------------------|-------------------|
| **Main Method** | 150+ lines | 3 lines |
| **Separation** | ❌ Everything mixed | ✅ Update vs Draw |
| **Frame Rate** | 2 FPS (80% slower!) | Stable 60+ FPS |
| **Movement** | Frame-dependent | Delta time based |
| **Testing** | 0% testable | 100% testable |
| **Flickering** | Severe | None |
| **Maintainability** | Impossible | Easy |
| **Team Work** | Blocked | Parallel |

---

## Why We Built This

**Educational Value**:
1. **Experience the pain** - Students must SEE why this is bad
2. **Contrast with solution** - Makes 09-01 improvements obvious
3. **Real anti-patterns** - These mistakes happen in real projects
4. **Motivation** - Clear reason to learn proper patterns

**Learning Approach**:
- Problem FIRST, solution SECOND
- Feel the frustration before the relief
- Understand WHY before HOW
- Appreciate good design by experiencing bad design

---

## Running This Branch

### Compile
```bash
javac -d bin/09-00-without-game-loop src/Main.java src/entities/*.java src/utils/*.java
```

### Run
```bash
cd bin/09-00-without-game-loop
java Main
```

### Observe Problems
Watch for:
- ✅ Severe flickering (screen clearing visible)
- ✅ Slow frame rate (~2 FPS, not 10 FPS)
- ✅ Separator lines appearing between frames
- ✅ Jerky movement (integer positions)

### Try to Test
```bash
javac -cp .:junit-platform-console-standalone.jar test/MainTest.java
java -jar junit-platform-console-standalone.jar --class-path . --scan-class-path
```

**Result**: All tests fail! ❌

---

## Key Files

- **[src/Main.java](../src/Main.java)**: Monolithic 150+ line main method
- **[src/entities/NPC.java](../src/entities/NPC.java)**: Entity with int positions and frame-dependent movement
- **[src/entities/Coin.java](../src/entities/Coin.java)**: Entity with int positions
- **[src/utils/GridRenderer.java](../src/utils/GridRenderer.java)**: Basic rendering utilities
- **[test/MainTest.java](../test/MainTest.java)**: All tests fail (demonstrates untestability)

---

## Discussion Questions

1. **Frame Rate**: Why is actual FPS 2 instead of 10?
2. **Flickering**: What causes the visible screen flickering?
3. **Testing**: Why can't we write unit tests for this code?
4. **Maintenance**: How would you add a second NPC? What would break?
5. **Team Work**: Could a graphics programmer work while a gameplay programmer also works?
6. **Refactoring**: Where would you start to fix this code?

---

## Expected Student Reactions

**"This code is terrible!"** ← Exactly! Now you understand WHY we need patterns.

**"Why did you write it this way?"** ← To show you what NOT to do, through experience.

**"How do we fix it?"** ← That's what branch 09-01 demonstrates!

**"I've written code like this..."** ← Great! Now you know how to improve it.

---

## Next Step

See [docs/09-01-solution.md](09-01-solution.md) for how professional games solve ALL of these problems with the Game Loop pattern.

**Spoiler**: Main.java becomes 3 lines. Tests pass. 60 FPS. No flicker. Professional architecture.

---

**Remember**: This branch is intentionally bad. The problems are real. The solution is coming in 09-01.
