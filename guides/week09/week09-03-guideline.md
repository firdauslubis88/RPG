# Branch 09-03: Implementation Guidelines for Claude Code

## 📁 File Structure
```
src/
├── Main.java                      # No manager parameter needed!
├── GameEngine.java                # No manager parameter!
├── GameLogic.java                 # No manager parameter!
├── GameManager.java               # âœ… SINGLETON implementation
├── HUD.java                       # Uses getInstance()
├── entities/
│   ├── NPC.java                  # No manager parameter!
│   └── Coin.java                 # No manager parameter!
└── utils/
    └── GridRenderer.java

test/
├── GameManagerTest.java          # Test singleton behavior
└── GameLogicTest.java            # Simplified tests
```

---

## 🎯 Implementation Requirements

### 1. GameManager.java (SINGLETON IMPLEMENTATION!)

**Purpose**: Global game state with guaranteed single instance.

```java
public class GameManager {
    // âœ… 1. Private static instance
    private static GameManager instance;
    
    // Game state fields
    private int score;
    private float gameTime;
    private int level;
    private boolean gameOver;
    
    // âœ… 2. Private constructor (prevents new)
    private GameManager() {
        this.score = 0;
        this.gameTime = 0.0f;
        this.level = 1;
        this.gameOver = false;
    }
    
    // âœ… 3. Public static getInstance()
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    
    // Business methods
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
    
    // âœ… Utility for testing
    public void reset() {
        this.score = 0;
        this.gameTime = 0.0f;
        this.level = 1;
        this.gameOver = false;
    }
}
```

**Critical Points**:
- âœ… Private constructor prevents `new GameManager()`
- âœ… Static instance field holds the single instance
- âœ… getInstance() creates instance lazily (first call only)
- âœ… reset() for test isolation (optional but recommended)

---

### 2. Main.java (Simplified!)

```java
public class Main {
    public static void main(String[] args) {
        // âœ… No manager creation or passing!
        GameEngine engine = new GameEngine();
        engine.start();
    }
}
```

**Comparison with 09-02**:
```java
// âŒ Before (09-02): Object drilling starts here
GameManager manager = new GameManager();
GameEngine engine = new GameEngine(manager);

// âœ… After (09-03): Clean entry point
GameEngine engine = new GameEngine();
```

---

### 3. GameEngine.java (No Manager Parameter!)

```java
public class GameEngine {
    private final GameLogic logic;
    private final HUD hud;
    private boolean running;
    
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;
    
    // âœ… No constructor parameters!
    public GameEngine() {
        this.logic = new GameLogic();
        this.hud = new HUD();
    }
    
    public void start() {
        running = true;
        long lastTime = System.nanoTime();
        
        while (running) {
            long currentTime = System.nanoTime();
            float delta = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;
            
            // âœ… Direct access to singleton
            GameManager.getInstance().updateTime(delta);
            
            update(delta);
            draw();
            
            sync(currentTime);
        }
    }
    
    private void update(float delta) {
        logic.update(delta);
    }
    
    private void draw() {
        GridRenderer.clearScreen();
        logic.draw();
        hud.draw();
    }
    
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
}
```

---

### 4. GameLogic.java (No Manager Parameter!)

```java
public class GameLogic {
    private NPC npc;
    private List<Coin> coins;
    
    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    
    // âœ… No constructor parameters!
    public GameLogic() {
        this.npc = new NPC();
        this.coins = new ArrayList<>();
        this.coins.add(new Coin());
        this.coins.add(new Coin());
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
                // âœ… Direct access to singleton
                GameManager.getInstance().addScore(10);
                
                // Debug log
                System.out.println("[Logic] Score: " + 
                    GameManager.getInstance().getScore());
                
                coin.respawn();
            }
        }
    }
    
    private boolean isColliding(NPC npc, Coin coin) {
        return npc.getX() == coin.getX() && npc.getY() == coin.getY();
    }
    
    public void draw() {
        GridRenderer.drawEntity('N', npc.getX(), npc.getY());
        for (Coin coin : coins) {
            GridRenderer.drawEntity('C', coin.getX(), coin.getY());
        }
    }
}
```

---

### 5. HUD.java (Fixed!)

```java
public class HUD {
    // âœ… No instance field, no constructor parameter!
    
    public HUD() {
        // Clean constructor
    }
    
    public void draw() {
        // âœ… Access singleton directly
        int score = GameManager.getInstance().getScore();
        float time = GameManager.getInstance().getGameTime();
        int level = GameManager.getInstance().getLevel();
        
        System.out.println("\n========== HUD ==========");
        System.out.println("Score: " + score);  // âœ… Correct value!
        System.out.println("Time: " + (int)time + "s");
        System.out.println("Level: " + level);
        System.out.println("========================\n");
    }
}
```

**Comparison with 09-02**:
```java
// âŒ Before (09-02): Bug - created own instance
private GameManager manager = new GameManager();
public HUD(GameManager passedManager) { /* ignored */ }

// âœ… After (09-03): Uses singleton
public HUD() { }
int score = GameManager.getInstance().getScore();
```

---

### 6. NPC.java (No Manager Parameter!)

```java
public class NPC {
    private float x, y;
    private float velocity;
    
    // âœ… Clean constructor!
    public NPC() {
        this.x = 0;
        this.y = 5;
        this.velocity = 2.0f;
    }
    
    public void update(float delta) {
        x += velocity * delta;
        if (x >= 10) x = 0;  // Wrap
    }
    
    // Can access manager if needed
    public void doSomething() {
        // âœ… Direct access when needed
        GameManager.getInstance().addScore(5);
    }
    
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
}
```

---

### 7. Coin.java (No Manager Parameter!)

```java
public class Coin {
    private float x, y;
    private float fallSpeed;
    
    // âœ… Clean constructor!
    public Coin() {
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
    
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
}
```

---

## 🧪 Testing Requirements

### GameManagerTest.java (Singleton Behavior Tests)

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    
    @BeforeEach
    void setUp() {
        // âœ… Reset singleton state before each test
        GameManager.getInstance().reset();
    }
    
    @Test
    void testOnlyOneInstanceExists() {
        GameManager instance1 = GameManager.getInstance();
        GameManager instance2 = GameManager.getInstance();
        
        // âœ… Should be same object reference
        assertSame(instance1, instance2);
    }
    
    @Test
    void testStateSharedAcrossReferences() {
        GameManager instance1 = GameManager.getInstance();
        GameManager instance2 = GameManager.getInstance();
        
        // Modify via instance1
        instance1.addScore(50);
        
        // Read via instance2
        assertEquals(50, instance2.getScore());
        
        // âœ… Changes visible everywhere!
    }
    
    @Test
    void testScoreManagement() {
        GameManager manager = GameManager.getInstance();
        
        assertEquals(0, manager.getScore());
        
        manager.addScore(10);
        assertEquals(10, manager.getScore());
        
        manager.addScore(20);
        assertEquals(30, manager.getScore());
    }
    
    @Test
    void testTimeUpdate() {
        GameManager manager = GameManager.getInstance();
        
        assertEquals(0.0f, manager.getGameTime(), 0.001f);
        
        manager.updateTime(0.016f);  // 1 frame
        assertTrue(manager.getGameTime() > 0);
    }
    
    @Test
    void testReset() {
        GameManager manager = GameManager.getInstance();
        
        // Set some state
        manager.addScore(100);
        manager.updateTime(10.0f);
        manager.setLevel(5);
        
        // Reset
        manager.reset();
        
        // Verify reset
        assertEquals(0, manager.getScore());
        assertEquals(0.0f, manager.getGameTime(), 0.001f);
        assertEquals(1, manager.getLevel());
        assertFalse(manager.isGameOver());
    }
}
```

---

### GameLogicTest.java (Simplified!)

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {
    
    @BeforeEach
    void setUp() {
        // âœ… Simple reset, no mocking needed!
        GameManager.getInstance().reset();
    }
    
    @Test
    void testCoinCollectionIncreasesScore() {
        // âœ… No constructor parameters!
        GameLogic logic = new GameLogic();
        
        int initialScore = GameManager.getInstance().getScore();
        
        // Simulate collision (implementation specific)
        // ... collision logic ...
        
        // Verify score increased
        assertTrue(GameManager.getInstance().getScore() >= initialScore);
    }
    
    @Test
    void testMultipleCollisions() {
        GameLogic logic = new GameLogic();
        
        // Simulate 3 collisions
        for (int i = 0; i < 3; i++) {
            GameManager.getInstance().addScore(10);
        }
        
        assertEquals(30, GameManager.getInstance().getScore());
    }
}
```

**Comparison with 09-02**:
```java
// âŒ Before (09-02): Much setup
GameManager manager = new GameManager();
GameLogic logic = new GameLogic(manager);
NPC npc = new NPC(manager);
Coin coin = new Coin(manager);
// 4 lines setup

// âœ… After (09-03): Minimal setup
GameManager.getInstance().reset();
GameLogic logic = new GameLogic();
// 2 lines setup
```

---

## 🎬 Demonstration Scripts

### Demo 1: State Consistency

**Run the game and collect 3 coins.**

**Expected Output**:
```
[Logic] Score: 10
[Logic] Score: 20
[Logic] Score: 30

========== HUD ==========
Score: 30         âœ… Correct! Same instance!
Time: 5s
Level: 1
========================
```

**Discussion**:
- "Logic and HUD show same score!"
- "Because they access same singleton instance."
- "Problem from 09-02 is fixed!"

---

### Demo 2: Constructor Parameters Eliminated

**Count constructor calls:**

```
Main.java:
new GameEngine()                    âœ… 0 parameters

GameEngine.java:
public GameEngine()                 âœ… 0 parameters
new GameLogic()                     âœ… 0 parameters
new HUD()                           âœ… 0 parameters

GameLogic.java:
public GameLogic()                  âœ… 0 parameters
new NPC()                           âœ… 0 parameters
new Coin()                          âœ… 0 parameters

Total parameters: 0 (vs 12+ in 09-02!)
```

**Discussion**:
- "No more object drilling!"
- "Clean constructors everywhere."
- "Easy to refactor - just call getInstance()."

---

### Demo 3: Singleton Test

**Run GameManagerTest:**

```bash
./gradlew test --tests GameManagerTest
```

**Expected Output**:
```
GameManagerTest > testOnlyOneInstanceExists PASSED
GameManagerTest > testStateSharedAcrossReferences PASSED
GameManagerTest > testScoreManagement PASSED
GameManagerTest > testReset PASSED

âœ… All singleton behavior verified!
```

---

## ⚠️ Critical Implementation Notes

### DO (For Proper Singleton)
1. âœ… Make constructor PRIVATE
2. âœ… Create static instance field
3. âœ… Provide static getInstance() method
4. âœ… Add reset() for testing
5. âœ… Remove ALL constructor parameters

### DON'T
1. ❌ Make constructor public (defeats purpose!)
2. ❌ Create static class (not same as Singleton)
3. ❌ Forget lazy initialization check
4. ❌ Pass manager as parameter (use getInstance!)
5. ❌ Create multiple instances in tests

---

## 🔧 Bonus: Thread-Safe Singleton (Optional)

### Simple Thread Safety (Double-Checked Locking)
```java
public class GameManager {
    private static volatile GameManager instance;
    
    private GameManager() { }
    
    public static GameManager getInstance() {
        if (instance == null) {  // First check (no lock)
            synchronized (GameManager.class) {  // Lock
                if (instance == null) {  // Second check
                    instance = new GameManager();
                }
            }
        }
        return instance;
    }
}
```

### Better: Initialization-on-demand Holder
```java
public class GameManager {
    private GameManager() { }
    
    private static class Holder {
        private static final GameManager INSTANCE = new GameManager();
    }
    
    public static GameManager getInstance() {
        return Holder.INSTANCE;
    }
}
```

**Discussion**: For single-threaded games, simple lazy init is fine. For multiplayer or multi-threaded games, use thread-safe variant.

---

## 📊 Comparison Summary

Create SOLUTION.md with:

```markdown
# Solutions in Branch 09-03

## 1. State Consistency Restored
- Single instance guarantee
- All components read same state
- âœ… HUD shows correct score

## 2. Object Drilling Eliminated
- Constructor parameters: 0 (vs 12+)
- Files to modify: 0 (vs 6+)
- âœ… Clean code structure

## 3. Testing Simplified
- Setup overhead: 1-2 lines (vs 5+ lines)
- No mocking needed
- âœ… Focus on behavior

## 4. Benefits
âœ… Consistent state
âœ… Easy access
âœ… Memory efficient (1 instance)
âœ… Thread-safe (optional DCL)

## 5. Trade-offs Acknowledged
âš ï¸ Global state (testing consideration)
âš ï¸ Hidden dependency (not in signature)
âš ï¸ Harder to mock (but reset() helps)

Professional games accept these trade-offs for the benefits!
```

---

## âœ… Success Checklist

Before submitting branch:
- [ ] GameManager constructor is PRIVATE
- [ ] Static getInstance() method exists
- [ ] Only one instance possible (tested)
- [ ] All constructors have 0 manager parameters
- [ ] HUD shows correct score (bug fixed!)
- [ ] State consistent across all components
- [ ] Tests use reset() for isolation
- [ ] Singleton behavior tests passing
- [ ] Comments explain pattern
- [ ] SOLUTION.md documents benefits

---

**Note for Claude Code**: This is the PROPER implementation of Singleton. Make it clean, professional, and demonstrate all benefits clearly! 🎯
