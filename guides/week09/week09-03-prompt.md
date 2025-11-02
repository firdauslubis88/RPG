# Kickstart Prompt for Claude Code

Copy-paste ini untuk memulai branch `09-03-with-singleton`:

---

```
Saya ingin refactor branch 09-02 untuk menerapkan Singleton pattern dan menyelesaikan semua masalah multiple instances dan object drilling. Ini adalah SOLUSI dari problem di 09-02.

## Context
Branch keempat project "Dungeon Escape". Mahasiswa baru saja melihat bugs di 09-02, sekarang kita tunjukkan solusi profesional menggunakan Singleton pattern.

## Game Specs (Same as Before)
- Terminal-based dengan proper game loop
- NPC auto-move, coins falling
- GameManager untuk global state

## Key Fixes from 09-02
1. âœ… GameManager: Private constructor + getInstance()
2. âœ… Zero constructor parameters (no more drilling)
3. âœ… Single instance guarantee (no more bugs)
4. âœ… Simplified testing (reset() method)
5. âœ… State consistency everywhere

## File Structure
```
src/
├── Main.java                  # âœ… No manager parameter
├── GameEngine.java            # âœ… No manager parameter
├── GameLogic.java             # âœ… No manager parameter
├── GameManager.java           # âœ… SINGLETON!
├── HUD.java                   # âœ… Uses getInstance()
├── entities/
│   ├── NPC.java              # âœ… No manager parameter
│   └── Coin.java             # âœ… No manager parameter
└── utils/
    └── GridRenderer.java

test/
├── GameManagerTest.java      # Test singleton behavior
└── GameLogicTest.java        # Simplified tests
```

## GameManager.java (SINGLETON IMPLEMENTATION!)
```java
public class GameManager {
    // âœ… 1. Private static instance
    private static GameManager instance;
    
    // Game state
    private int score;
    private float gameTime;
    private int level;
    private boolean gameOver;
    
    // âœ… 2. Private constructor (prevents new)
    private GameManager() {
        this.score = 0;
        this.gameTime = 0;
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
    public void addScore(int points) { score += points; }
    public int getScore() { return score; }
    public void updateTime(float delta) { gameTime += delta; }
    public float getGameTime() { return gameTime; }
    
    // âœ… For testing
    public void reset() {
        score = 0;
        gameTime = 0;
        level = 1;
        gameOver = false;
    }
}
```

## The Three Components
```
1. Private static instance field
2. Private constructor
3. Public static getInstance() method

All three REQUIRED for proper Singleton!
```

## Main.java (Simplified!)
```java
public class Main {
    public static void main(String[] args) {
        // âœ… No manager creation!
        // âœ… No manager passing!
        GameEngine engine = new GameEngine();
        engine.start();
    }
}
```

## GameEngine.java (No Parameters!)
```java
public class GameEngine {
    private GameLogic logic;
    private HUD hud;
    
    // âœ… No constructor parameters!
    public GameEngine() {
        this.logic = new GameLogic();
        this.hud = new HUD();
    }
    
    public void start() {
        while (running) {
            // âœ… Direct access
            GameManager.getInstance().updateTime(delta);
            
            update(delta);
            draw();
        }
    }
}
```

## GameLogic.java (No Parameters!)
```java
public class GameLogic {
    private NPC npc;
    private List<Coin> coins;
    
    // âœ… No constructor parameters!
    public GameLogic() {
        this.npc = new NPC();
        this.coins = List.of(new Coin(), new Coin());
    }
    
    private void checkCollisions() {
        for (Coin coin : coins) {
            if (isColliding(npc, coin)) {
                // âœ… Direct access to singleton
                GameManager.getInstance().addScore(10);
                coin.respawn();
            }
        }
    }
}
```

## HUD.java (Bug Fixed!)
```java
public class HUD {
    // âœ… No instance field!
    // âœ… No constructor parameter!
    
    public HUD() { }
    
    public void draw() {
        // âœ… Access singleton directly
        int score = GameManager.getInstance().getScore();
        System.out.println("Score: " + score);  // âœ… Correct!
    }
}
```

## Entity Classes (Clean Constructors!)
```java
public class NPC {
    public NPC() { }  // âœ… No parameters!
    
    public void update(float delta) {
        // Can access manager when needed
        GameManager.getInstance().addScore(5);
    }
}

public class Coin {
    public Coin() { }  // âœ… No parameters!
}
```

## Testing (Simplified!)
```java
@Test
void testOnlyOneInstance() {
    GameManager m1 = GameManager.getInstance();
    GameManager m2 = GameManager.getInstance();
    
    // âœ… Same instance!
    assertSame(m1, m2);
}

@Test
void testStateShared() {
    GameManager m1 = GameManager.getInstance();
    m1.addScore(50);
    
    GameManager m2 = GameManager.getInstance();
    assertEquals(50, m2.getScore());  // âœ… Same state!
}

@Test
void testCoinCollection() {
    // âœ… Simple setup!
    GameManager.getInstance().reset();
    GameLogic logic = new GameLogic();
    
    // Test logic...
}
```

## Expected Output (Bug Fixed!)
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

## Comparison with 09-02
```
09-02 (Bad):
- Constructor parameters: 12+
- GameManager instances: N (uncontrolled)
- HUD shows: 0 (bug!)
- Test setup: 5+ lines

09-03 (Good):
- Constructor parameters: 0
- GameManager instances: 1 (guaranteed)
- HUD shows: Correct score
- Test setup: 1-2 lines
```

## Bonus: Thread-Safe Variant (Optional)
```java
// Double-Checked Locking
public static GameManager getInstance() {
    if (instance == null) {
        synchronized (GameManager.class) {
            if (instance == null) {
                instance = new GameManager();
            }
        }
    }
    return instance;
}
```

## Comments to Add
`// âœ… SOLUTION: Private constructor prevents multiple instances`
`// âœ… SOLUTION: getInstance() provides global access point`
`// âœ… SOLUTION: No constructor parameters needed`
`// âœ… SOLUTION: State consistent everywhere`

## Success Criteria
1. âœ… Private constructor (compile-time prevention)
2. âœ… Static getInstance() method
3. âœ… Zero constructor parameters
4. âœ… HUD shows correct score
5. âœ… Tests verify single instance
6. âœ… All tests passing

Include SOLUTION.md explaining benefits and trade-offs!

Implementasikan dengan fokus pada clean singleton implementation!
```
