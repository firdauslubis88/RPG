# Kickstart Prompt for Claude Code

Copy-paste ini untuk memulai branch `09-02-without-singleton`:

---

```
Saya ingin membuat demo untuk mengajarkan MENGAPA Singleton pattern diperlukan dengan menunjukkan masalah multiple instances dan object drilling. Code ini SENGAJA memiliki bugs untuk tujuan pembelajaran.

## Context
Branch ketiga project "Dungeon Escape". Mahasiswa sudah punya proper game loop (09-01), sekarang kita tunjukkan masalah global state management tanpa Singleton.

## Game Specs (Same as Before)
- Terminal-based dengan game loop
- NPC auto-move, coins falling
- **NEW**: GameManager untuk global state (score, time, level)

## Architecture (INTENTIONALLY BUGGY!)
- GameManager: PUBLIC constructor (âŒ anyone can create!)
- All classes: Receive manager in constructor (object drilling)
- HUD class: âŒ CREATES OWN INSTANCE (bug demonstration!)
- Result: State inconsistency, score mismatch

## The Bug We're Demonstrating
```java
// GameLogic updates score in instance A
manager.addScore(10);  // Instance A score = 10

// HUD reads score from instance B
int score = manager.getScore();  // Instance B score = 0!
// HUD shows wrong score!
```

## File Structure
```
src/
├── Main.java                  # Creates manager, passes to GameEngine
├── GameEngine.java            # Receives manager, passes to logic+HUD
├── GameLogic.java             # Receives manager, updates score
├── GameManager.java           # âŒ PUBLIC constructor!
├── HUD.java                   # âŒ Creates OWN instance!
├── entities/
│   ├── NPC.java              # Receives manager in constructor
│   └── Coin.java             # Receives manager in constructor
└── utils/
    └── GridRenderer.java
```

## GameManager.java (THE PROBLEM!)
```java
public class GameManager {
    private int score;
    private float gameTime;
    private int level;
    
    // âŒ PROBLEM: Public constructor allows multiple instances!
    public GameManager() {
        this.score = 0;
        this.gameTime = 0;
        this.level = 1;
    }
    
    public void addScore(int points) { score += points; }
    public int getScore() { return score; }
    public void updateTime(float delta) { gameTime += delta; }
    public float getGameTime() { return gameTime; }
    // ... more getters/setters
}
```

## Object Drilling Pattern
```java
// Main.java
GameManager manager = new GameManager();
GameEngine engine = new GameEngine(manager);  // âŒ Pass

// GameEngine.java
public GameEngine(GameManager manager) {      // âŒ Receive
    this.logic = new GameLogic(manager);      // âŒ Pass
    this.hud = new HUD(manager);              // âŒ Pass
}

// GameLogic.java
public GameLogic(GameManager manager) {       // âŒ Receive
    this.npc = new NPC(manager);              // âŒ Pass
    this.coins.add(new Coin(manager));        // âŒ Pass
}

// NPC.java
public NPC(GameManager manager) {             // âŒ Receive
    this.manager = manager;
}

// Total: 12+ constructor parameters!
```

## HUD.java (THE BUG!)
```java
public class HUD {
    // âŒ PROBLEM: Creates NEW instance instead of using passed one!
    private final GameManager manager = new GameManager();
    
    // Receives manager but ignores it (simulates dev mistake)
    public HUD(GameManager passedManager) {
        // Intentionally ignore parameter!
    }
    
    public void draw() {
        // âŒ Reads from WRONG instance!
        System.out.println("Score: " + manager.getScore());  // Always 0!
    }
}
```

## GameLogic.java
```java
public class GameLogic {
    private final GameManager manager;
    private NPC npc;
    private List<Coin> coins;
    
    public GameLogic(GameManager manager) {
        this.manager = manager;
        this.npc = new NPC(manager);
        this.coins = List.of(new Coin(manager), new Coin(manager));
    }
    
    private void checkCollisions() {
        for (Coin coin : coins) {
            if (isColliding(npc, coin)) {
                manager.addScore(10);
                // âœ… Debug log
                System.out.println("[GameLogic] Score: " + manager.getScore());
                coin.respawn();
            }
        }
    }
}
```

## Expected Output (Bug Demo)
```
[GameLogic] Score: 10
[GameLogic] Score: 20
[GameLogic] Score: 30

========== HUD ==========
Score: 0          âŒ BUG! Different instance!
Time: 5s
Level: 1
========================
```

## Testing (Overhead Demo)
```java
@Test
void testScoreUpdate() {
    // âŒ PROBLEM: Much setup!
    GameManager manager = new GameManager();
    GameLogic logic = new GameLogic(manager);
    NPC npc = new NPC(manager);
    Coin coin = new Coin(manager);
    
    // Only 2 lines of actual test!
    logic.checkCollisions();
    assertEquals(10, manager.getScore());
}

@Test
void testMultipleInstancesBug() {
    GameManager m1 = new GameManager();
    GameManager m2 = new GameManager();
    
    m1.addScore(50);
    
    assertEquals(50, m1.getScore());  // âœ… OK
    assertEquals(0, m2.getScore());   // âŒ Different state!
}
```

## Anti-Patterns Required
1. âŒ GameManager public constructor
2. âŒ HUD creates own instance (bug!)
3. âŒ Object drilling (pass manager everywhere)
4. âŒ 10+ constructor parameters total
5. âŒ State inconsistency bug

## Comments to Add
`// âŒ PROBLEM: Public constructor allows multiple instances`
`// âŒ PROBLEM: HUD creates own instance instead of using passed one`
`// âŒ PROBLEM: Object drilling - manager passed through 3 levels`
`// âŒ PROBLEM: Test setup overhead (4 lines setup, 2 lines test)`

## Demonstration Features
1. Score mismatch: Logic says 50, HUD shows 0
2. Constructor count: 12+ manager parameters
3. Test overhead: Setup > test logic

## Success Criteria
- âœ… HUD shows wrong score (bug working!)
- âœ… Debug logs show score in GameLogic
- âœ… Multiple instances possible
- âœ… Object drilling evident (many constructors)
- âœ… Test demonstrates bug

Include PROBLEM.md documenting all issues!

Implementasikan dengan fokus pada demonstrating the problem clearly!
```
