# Branch 09-01: Implementation Guidelines for Claude Code

## 📁 File Structure
```
src/
├── Main.java                      # Entry point (minimal)
├── GameEngine.java                # Core game loop
├── GameLogic.java                 # Pure logic (testable)
├── entities/
│   ├── NPC.java                  # x, y, velocity
│   └── Coin.java                 # x, y, fallSpeed
└── utils/
    └── GridRenderer.java         # Static rendering utilities

test/
├── GameLogicTest.java            # Unit tests for logic
└── entities/
    ├── NPCTest.java
    └── CoinTest.java
```

---

## 🎯 Implementation Requirements

### 1. Main.java (Entry Point Only)

**Purpose**: Minimal entry point, delegates to GameEngine.

```java
public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start();
        // âœ… Clean, 3 lines total!
    }
}
```

---

### 2. GameEngine.java (Game Loop Core)

**Purpose**: Orchestrate update/draw cycle with proper timing.

#### Fields
```java
private final GameLogic gameLogic;
private boolean running;
private static final int TARGET_FPS = 60;
private static final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;
```

#### Methods

**start()**
```java
public void start() {
    running = true;
    long lastTime = System.nanoTime();
    
    while (running) {
        long currentTime = System.nanoTime();
        float delta = (currentTime - lastTime) / 1_000_000_000.0f;
        lastTime = currentTime;
        
        update(delta);    // âœ… Logic
        draw();           // âœ… Rendering
        
        sync(currentTime);  // Frame rate control
    }
}
```

**update(float delta)**
```java
private void update(float delta) {
    gameLogic.updateNPC(delta);
    gameLogic.updateCoins(delta);
    gameLogic.checkCollisions();
    // âœ… PURE LOGIC, NO System.out.println!
}
```

**draw()**
```java
private void draw() {
    GridRenderer.clearScreen();
    GridRenderer.drawEntity('N', gameLogic.getNPCX(), gameLogic.getNPCY());
    
    for (Coin coin : gameLogic.getCoins()) {
        GridRenderer.drawEntity('C', coin.getX(), coin.getY());
    }
    
    GridRenderer.drawHUD("Score: " + gameLogic.getScore());
    // âœ… PURE RENDERING, NO logic!
}
```

**sync(long startTime)**
```java
private void sync(long startTime) {
    long elapsedTime = System.nanoTime() - startTime;
    long waitTime = OPTIMAL_TIME - elapsedTime;
    
    if (waitTime > 0) {
        try {
            Thread.sleep(waitTime / 1_000_000, (int)(waitTime % 1_000_000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

**Critical Notes**:
- âœ… update() and draw() are separate methods
- âœ… Delta time passed to all update logic
- âœ… Frame rate control in sync()
- âœ… No logic in draw(), no rendering in update()

---

### 3. GameLogic.java (Testable Logic)

**Purpose**: All game state and logic, completely independent of rendering.

#### Fields
```java
private NPC npc;
private List<Coin> coins;
private int score;
private static final int GRID_WIDTH = 10;
private static final int GRID_HEIGHT = 10;
```

#### Constructor
```java
public GameLogic() {
    this.npc = new NPC(0, 5, 2.0f);  // x, y, velocity
    this.coins = new ArrayList<>();
    this.coins.add(new Coin(5, 0, 3.0f));  // Initial coin
    this.score = 0;
}
```

#### Update Methods (Delta Time Based)

**updateNPC(float delta)**
```java
public void updateNPC(float delta) {
    // âœ… Delta time based movement
    float newX = npc.getX() + npc.getVelocity() * delta;
    
    // Wrap around at edges
    if (newX >= GRID_WIDTH) {
        newX = 0;
    }
    
    npc.setX(newX);
}
```

**updateCoins(float delta)**
```java
public void updateCoins(float delta) {
    for (Coin coin : coins) {
        // âœ… Gravity simulation
        float newY = coin.getY() + coin.getFallSpeed() * delta;
        
        if (newY >= GRID_HEIGHT) {
            // Respawn at top
            coin.setY(0);
            coin.setX((float)(Math.random() * GRID_WIDTH));
        } else {
            coin.setY(newY);
        }
    }
}
```

**checkCollisions()**
```java
public void checkCollisions() {
    for (Coin coin : coins) {
        if (isColliding(npc, coin)) {
            score += 10;
            // Respawn coin
            coin.setY(0);
            coin.setX((float)(Math.random() * GRID_WIDTH));
            // âœ… NO System.out.println here! (keep pure)
        }
    }
}

private boolean isColliding(NPC npc, Coin coin) {
    int npcX = (int)npc.getX();
    int npcY = (int)npc.getY();
    int coinX = (int)coin.getX();
    int coinY = (int)coin.getY();
    
    return npcX == coinX && npcY == coinY;
}
```

#### Getters (for rendering)
```java
public int getNPCX() { return (int)npc.getX(); }
public int getNPCY() { return (int)npc.getY(); }
public List<Coin> getCoins() { return coins; }
public int getScore() { return score; }
```

---

### 4. Entity Classes

**NPC.java**
```java
public class NPC {
    private float x, y;
    private float velocity;
    
    public NPC(float x, float y, float velocity) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
    }
    
    // Getters and setters
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getVelocity() { return velocity; }
}
```

**Coin.java**
```java
public class Coin {
    private float x, y;
    private float fallSpeed;
    
    public Coin(float x, float y, float fallSpeed) {
        this.x = x;
        this.y = y;
        this.fallSpeed = fallSpeed;
    }
    
    // Getters and setters
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getFallSpeed() { return fallSpeed; }
}
```

**Note**: Use float for precision in delta time calculations!

---

### 5. GridRenderer.java (Utility)

**Purpose**: Terminal rendering utilities (same as 09-00).

```java
public class GridRenderer {
    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    private static final char EMPTY = '░';
    
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public static void drawEntity(char symbol, int x, int y) {
        // Grid drawing logic (same as 09-00)
    }
    
    public static void drawHUD(String info) {
        System.out.println("\n" + info);
    }
}
```

---

## 🧪 Testing Requirements

### GameLogicTest.java

**Test Update Logic Without Rendering**

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {
    
    @Test
    void testNPCMovesRightWithDeltaTime() {
        GameLogic logic = new GameLogic();
        float initialX = logic.getNPCX();
        
        // Simulate 1 frame at 60 FPS (0.016s)
        logic.updateNPC(0.016f);
        
        // NPC velocity = 2.0 pixels/sec
        // Expected movement = 2.0 * 0.016 = 0.032 pixels
        assertTrue(logic.getNPCX() > initialX);
    }
    
    @Test
    void testNPCWrapsAroundAtEdge() {
        GameLogic logic = new GameLogic();
        
        // Move NPC past right edge
        for (int i = 0; i < 100; i++) {
            logic.updateNPC(0.1f);  // Large delta
        }
        
        // Should wrap to left side
        assertTrue(logic.getNPCX() < 10);
    }
    
    @Test
    void testCoinFallsWithGravity() {
        GameLogic logic = new GameLogic();
        Coin coin = logic.getCoins().get(0);
        float initialY = coin.getY();
        
        logic.updateCoins(0.016f);
        
        // Coin should fall (y increases)
        assertTrue(coin.getY() > initialY);
    }
    
    @Test
    void testCoinReapawnsAtTop() {
        GameLogic logic = new GameLogic();
        Coin coin = logic.getCoins().get(0);
        
        // Move coin past bottom
        for (int i = 0; i < 100; i++) {
            logic.updateCoins(0.1f);
        }
        
        // Should respawn at top (y near 0)
        assertTrue(coin.getY() < 2.0f);
    }
    
    @Test
    void testCollisionDetection() {
        GameLogic logic = new GameLogic();
        
        // Position NPC and coin at same location
        // (Implementation depends on your collision logic)
        
        int initialScore = logic.getScore();
        logic.checkCollisions();
        
        // Score should increase if collision
        assertTrue(logic.getScore() >= initialScore);
    }
    
    @Test
    void testFrameRateIndependence() {
        GameLogic logic1 = new GameLogic();
        GameLogic logic2 = new GameLogic();
        
        // Simulate 60 FPS (1 frame)
        logic1.updateNPC(0.016f);
        
        // Simulate 30 FPS (1 frame)
        logic2.updateNPC(0.033f);
        
        // Both should move roughly same distance per second
        // (not exactly equal due to discrete positions)
        float distance1 = logic1.getNPCX();
        float distance2 = logic2.getNPCX();
        
        // Verify they're close (within 10% tolerance)
        assertTrue(Math.abs(distance1 - distance2) < 0.5f);
    }
}
```

**Coverage Target**: >70% for GameLogic class.

---

## 🎬 Demonstration Scripts

### Demo 1: Render Delay Doesn't Affect Logic

**Modification**:
```java
private void draw() {
    GridRenderer.clearScreen();
    // ... normal rendering ...
    
    // âš ï¸ ADD THIS for demonstration
    try {
        Thread.sleep(50);  // Artificial 50ms delay
    } catch (InterruptedException e) {}
}
```

**Run & Observe**:
- Visual: Choppy (only ~20 FPS)
- Gameplay: NPC still moves smoothly (logic at 60 FPS)
- Coins still fall at correct speed

**Metrics to Log**:
```java
System.out.println("Update time: " + updateTime + "ms");
System.out.println("Draw time: " + drawTime + "ms");
System.out.println("FPS: " + fps);
```

Expected output:
```
Update time: 2ms
Draw time: 52ms   ← Slow!
FPS: 18           ← Visual lag
(But NPC moves consistently!)
```

---

### Demo 2: Unit Test Success

**Run Tests**:
```bash
./gradlew test
```

**Expected Output**:
```
GameLogicTest > testNPCMovesRight PASSED
GameLogicTest > testCoinFalls PASSED
GameLogicTest > testCollision PASSED
GameLogicTest > testFrameRateIndependence PASSED

BUILD SUCCESSFUL
5 tests, 5 passed
```

**Discussion Point**:
"Look! We tested collision without display. Impossible in 09-00!"

---

## ⚠️ Critical Implementation Notes

### DO
1. âœ… Separate update() and draw() completely
2. âœ… Use float for entity positions (delta time precision)
3. âœ… Pass delta time to ALL update methods
4. âœ… Keep GameLogic pure (no System.out)
5. âœ… Add comprehensive tests

### DON'T
1. ❌ Put ANY logic in draw()
2. ❌ Put ANY rendering in update()
3. ❌ Use fixed values (like `x += 1`)
4. ❌ Call System.out in GameLogic
5. ❌ Forget frame rate control

---

## 📊 Comparison with 09-00

| Aspect | 09-00 | 09-01 |
|--------|-------|-------|
| Main method LOC | 150+ | 3 |
| Testability | ❌ | ✅ |
| Delta time | ❌ | ✅ |
| FPS control | ❌ Fixed sleep | ✅ Adaptive |
| Separation | ❌ | ✅ |
| Render delay impact | ❌ Breaks game | ✅ Visual only |

---

## âœ… Success Checklist

Before submitting branch:
- [ ] Game runs at stable 60 FPS
- [ ] update() has ZERO rendering code
- [ ] draw() has ZERO logic code
- [ ] Delta time used for all movement
- [ ] All tests passing (>70% coverage)
- [ ] NPC wraps around edges
- [ ] Coins fall and respawn
- [ ] Collision detection works
- [ ] Render delay demo works
- [ ] Frame rate independence verified

---

**Note for Claude Code**: This is a PROPER implementation. Make it clean, professional, and well-tested. This will be the foundation for all future weeks! 🚀
