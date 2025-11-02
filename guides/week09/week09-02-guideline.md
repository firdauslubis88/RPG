# Branch 09-02: Implementation Guidelines for Claude Code

## 📁 File Structure
```
src/
├── Main.java                      # Creates GameManager instance
├── GameEngine.java                # Receives manager in constructor
├── GameLogic.java                 # Receives manager in constructor
├── GameManager.java               # ❌ PUBLIC constructor!
├── HUD.java                       # ❌ Creates OWN manager instance!
├── entities/
│   ├── NPC.java                  # Receives manager in constructor
│   └── Coin.java                 # Receives manager in constructor
└── utils/
    └── GridRenderer.java

test/
└── GameLogicTest.java            # ❌ Much setup overhead
```

---

## 🎯 Implementation Requirements

### 1. GameManager.java (INTENTIONALLY BAD!)

**Purpose**: Global game state WITHOUT singleton protection.

#### Fields
```java
private int score;
private float gameTime;
private int level;
private boolean gameOver;
```

#### Constructor (âŒ PUBLIC!)
```java
// âŒ PROBLEM: Anyone can create new instance!
public GameManager() {
    this.score = 0;
    this.gameTime = 0.0f;
    this.level = 1;
    this.gameOver = false;
}
```

#### Methods
```java
public void addScore(int points) {
    this.score += points;
}

public int getScore() {
    return this.score;
}

public void updateTime(float delta) {
    this.gameTime += delta;
}

public float getGameTime() {
    return this.gameTime;
}

public int getLevel() {
    return this.level;
}

public void setLevel(int level) {
    this.level = level;
}

public boolean isGameOver() {
    return this.gameOver;
}

public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
}
```

**Critical Anti-Pattern**:
```java
// âŒ PROBLEM: Public constructor allows this!
GameManager m1 = new GameManager();
GameManager m2 = new GameManager();
// Two instances with different state!
```

---

### 2. Main.java (Object Drilling Start)

```java
public class Main {
    public static void main(String[] args) {
        // Creates THE instance (but others can create more!)
        GameManager manager = new GameManager();
        
        // Must pass to GameEngine
        GameEngine engine = new GameEngine(manager);
        engine.start();
        
        // âŒ PROBLEM: Must pass manager everywhere!
    }
}
```

---

### 3. GameEngine.java (Receives + Passes Manager)

```java
public class GameEngine {
    private final GameManager manager;  // âŒ Dependency
    private final GameLogic logic;
    private final HUD hud;
    private boolean running;
    
    // âŒ PROBLEM: Constructor parameter explosion
    public GameEngine(GameManager manager) {
        this.manager = manager;
        this.logic = new GameLogic(manager);  // Pass down
        this.hud = new HUD(manager);          // Pass down
    }
    
    public void start() {
        running = true;
        long lastTime = System.nanoTime();
        
        while (running) {
            float delta = calculateDelta(lastTime);
            lastTime = System.nanoTime();
            
            manager.updateTime(delta);  // Update global time
            
            update(delta);
            draw();
            
            sync();
        }
    }
    
    private void update(float delta) {
        logic.update(delta);
    }
    
    private void draw() {
        GridRenderer.clearScreen();
        logic.draw();
        hud.draw();  // HUD will show WRONG score!
    }
    
    // ... sync(), calculateDelta() methods
}
```

---

### 4. GameLogic.java (Receives + Uses Manager)

```java
public class GameLogic {
    private final GameManager manager;  // âŒ Dependency
    private NPC npc;
    private List<Coin> coins;
    
    // âŒ PROBLEM: Must receive manager
    public GameLogic(GameManager manager) {
        this.manager = manager;
        this.npc = new NPC(manager);  // Pass down further
        this.coins = new ArrayList<>();
        this.coins.add(new Coin(manager));
        this.coins.add(new Coin(manager));
    }
    
    public void update(float delta) {
        npc.update(delta);
        
        for (Coin coin : coins) {
            coin.update(delta);
        }
        
        checkCollisions();
    }
    
    private void checkCollisions() {
        for (Coin coin : coins) {
            if (isColliding(npc, coin)) {
                // âœ… Uses manager from constructor
                manager.addScore(10);
                
                // Debug log
                System.out.println("[GameLogic] Score updated: " + manager.getScore());
                
                coin.respawn();
            }
        }
    }
    
    public void draw() {
        GridRenderer.drawEntity('N', npc.getX(), npc.getY());
        for (Coin coin : coins) {
            GridRenderer.drawEntity('C', coin.getX(), coin.getY());
        }
    }
    
    // ... helper methods
}
```

---

### 5. HUD.java (âŒ CREATES OWN INSTANCE!)

**Purpose**: Display score and stats. INTENTIONALLY creates its own GameManager!

```java
public class HUD {
    // âŒ PROBLEM: Creates NEW instance instead of using passed one!
    private final GameManager manager = new GameManager();
    
    // Constructor receives manager but IGNORES IT!
    public HUD(GameManager passedManager) {
        // âŒ Intentionally ignore parameter for demonstration!
        // This simulates developer mistake
    }
    
    public void draw() {
        // Reads from WRONG instance!
        int score = manager.getScore();
        float time = manager.getGameTime();
        int level = manager.getLevel();
        
        System.out.println("\n========== HUD ==========");
        System.out.println("Score: " + score);  // âŒ Always 0!
        System.out.println("Time: " + (int)time + "s");
        System.out.println("Level: " + level);
        System.out.println("========================\n");
    }
}
```

**Critical Bug**:
- GameLogic uses manager instance A (passed from Main)
- HUD creates manager instance B (new)
- Score updated in A, displayed from B
- Result: HUD always shows 0!

---

### 6. NPC.java (Receives Manager)

```java
public class NPC {
    private float x, y;
    private float velocity;
    private final GameManager manager;  // âŒ Dependency
    
    // âŒ PROBLEM: Constructor parameter
    public NPC(GameManager manager) {
        this.manager = manager;
        this.x = 0;
        this.y = 5;
        this.velocity = 2.0f;
    }
    
    public void update(float delta) {
        x += velocity * delta;
        if (x >= 10) x = 0;  // Wrap
    }
    
    // Getters
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
}
```

---

### 7. Coin.java (Receives Manager)

```java
public class Coin {
    private float x, y;
    private float fallSpeed;
    private final GameManager manager;  // âŒ Dependency
    
    // âŒ PROBLEM: Constructor parameter
    public Coin(GameManager manager) {
        this.manager = manager;
        this.x = (float)(Math.random() * 10);
        this.y = 0;
        this.fallSpeed = 3.0f;
    }
    
    public void update(float delta) {
        y += fallSpeed * delta;
        if (y >= 10) {
            respawn();
        }
    }
    
    public void respawn() {
        y = 0;
        x = (float)(Math.random() * 10);
    }
    
    // Getters
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
}
```

---

## 🧪 Testing Requirements

### GameLogicTest.java (Overhead Demonstration)

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {
    
    @Test
    void testCoinCollectionIncreasesScore() {
        // âŒ PROBLEM: Much setup!
        GameManager manager = new GameManager();
        GameLogic logic = new GameLogic(manager);
        
        // Get initial score
        int initialScore = manager.getScore();
        
        // Force collision (implementation specific)
        // ... collision logic ...
        
        // Verify score increased
        assertTrue(manager.getScore() > initialScore);
    }
    
    @Test
    void testMultipleManagersShowInconsistency() {
        // âŒ Demonstrate the bug!
        GameManager manager1 = new GameManager();
        GameManager manager2 = new GameManager();
        
        // Update score in manager1
        manager1.addScore(10);
        manager1.addScore(10);
        manager1.addScore(10);
        
        // Check scores
        assertEquals(30, manager1.getScore());  // âœ… Correct
        assertEquals(0, manager2.getScore());   // âŒ Bug! Different state
        
        // This is the PROBLEM we're demonstrating!
    }
    
    @Test
    void testHUDShowsWrongScore() {
        // Simulate the bug
        GameManager realManager = new GameManager();
        realManager.addScore(50);
        
        // HUD creates its own instance
        HUD hud = new HUD(realManager);
        
        // HUD's internal manager has score = 0
        // Manual verification needed (HUD.draw() will show 0)
    }
}
```

---

## 🎬 Demonstration Script

### Demo 1: Score Inconsistency Bug

**Run the game and collect 5 coins.**

**Expected Output**:
```
[GameLogic] Score updated: 10
[GameLogic] Score updated: 20
[GameLogic] Score updated: 30
[GameLogic] Score updated: 40
[GameLogic] Score updated: 50

========== HUD ==========
Score: 0          âŒ BUG!
Time: 5s
Level: 1
========================
```

**Discussion**:
- "GameLogic says score = 50"
- "HUD shows score = 0"
- "Why? Different instances!"
- "This is why we need Singleton."

---

### Demo 2: Count Constructor Parameters

**Count how many times GameManager is passed:**

```
Main.java:
- GameManager manager = new GameManager();        // 1
- new GameEngine(manager)                         // 1

GameEngine.java:
- public GameEngine(GameManager manager)          // 1
- new GameLogic(manager)                          // 1
- new HUD(manager)                                // 1

GameLogic.java:
- public GameLogic(GameManager manager)           // 1
- new NPC(manager)                                // 1
- new Coin(manager) × 2                           // 2

NPC.java:
- public NPC(GameManager manager)                 // 1

Coin.java:
- public Coin(GameManager manager) × 2            // 2

TOTAL: 12 manager references!
```

**Discussion**:
- "Imagine 50 classes..."
- "Refactoring nightmare!"
- "Need global access pattern."

---

### Demo 3: Test Setup Overhead

**Compare test code:**

```java
// Setup: 4 lines
GameManager manager = new GameManager();
GameLogic logic = new GameLogic(manager);
NPC npc = new NPC(manager);
Coin coin = new Coin(manager);

// Actual test: 2 lines
logic.checkCollisions();
assertEquals(10, manager.getScore());
```

**Ratio**: 4:2 = 2:1 (setup : test)

**Discussion**:
- "Most of test is setup, not testing!"
- "Every test repeats this."
- "With Singleton, setup = 1 line."

---

## ⚠️ Critical Implementation Notes

### DO (For Problem Demonstration)
1. âœ… Keep GameManager constructor PUBLIC
2. âœ… Make HUD create its OWN instance (bug!)
3. âœ… Pass manager to ALL classes
4. âœ… Add debug logs showing score mismatch
5. âœ… Add comments marking problems

### DON'T
1. ❌ Fix the bug (that's next branch!)
2. ❌ Make constructor private
3. ❌ Use static getInstance()
4. ❌ Make it clean (it should be messy!)

---

## 📊 Metrics to Document

Create a PROBLEM.md file with:

```markdown
# Problems in Branch 09-02

## 1. Multiple Instances Bug
- GameLogic instance: A
- HUD instance: B
- Score in A: 50
- Score in B: 0
- Bug: HUD shows wrong score

## 2. Object Drilling
- Main → GameEngine: 1 level
- GameEngine → GameLogic: 2 levels
- GameLogic → NPC/Coin: 3 levels
- Total parameter passes: 12

## 3. Constructor Explosion
Classes needing manager parameter: 6
Files modified if manager changes: 6+

## 4. Test Overhead
Average setup: 4 lines
Average test: 2 lines
Ratio: 2:1 (bad!)

## Next: 09-03-with-singleton
Singleton pattern will solve ALL these issues!
```

---

## âœ… Success Checklist

Before submitting branch:
- [ ] GameManager constructor is PUBLIC
- [ ] HUD creates its own instance (bug present!)
- [ ] All classes receive manager in constructor
- [ ] Score inconsistency bug reproducible
- [ ] Debug logs show score mismatch
- [ ] Count constructor parameters: 10+
- [ ] Test demonstrates multiple instances
- [ ] Comments mark all problems
- [ ] PROBLEM.md documents issues

---

**Note for Claude Code**: This is INTENTIONALLY buggy code. Make the problems OBVIOUS so students feel the pain! 🎯
