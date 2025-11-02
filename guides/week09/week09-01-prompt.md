# Kickstart Prompt for Claude Code

Copy-paste ini untuk memulai branch `09-01-with-game-loop`:

---

```
Saya ingin refactor branch 09-00 untuk menerapkan proper game loop architecture dengan separation of concerns. Ini adalah SOLUSI dari problem yang ditunjukkan di 09-00.

## Context
Branch kedua project "Dungeon Escape" untuk kuliah OOP. Mahasiswa baru saja melihat masalah di 09-00, sekarang kita tunjukkan solusi profesional.

## Game Specs (Same as 09-00)
- Terminal-based, auto-scrolling
- NPC: Bergerak otomatis kiri-kanan
- Coin: Spawn random, jatuh ke bawah
- Mechanics: NPC nabrak coin → score++
- Display: Grid 10x10

## Architecture (PROPER DESIGN!)
- Main.java: Entry point only (3 lines)
- GameEngine.java: Game loop orchestration
- GameLogic.java: Pure logic, no rendering
- Entities: Data models only
- GridRenderer: Utility class

## Key Improvements from 09-00
1. ✅ update() dan draw() terpisah
2. ✅ Delta time untuk frame rate independence
3. ✅ Testable logic (no rendering in tests)
4. ✅ Clean structure (max 30 lines per method)
5. ✅ Frame rate control (60 FPS target)

## File Structure
```
src/
├── Main.java                  # 3 lines: new GameEngine().start()
├── GameEngine.java            # Game loop core (start, update, draw, sync)
├── GameLogic.java             # Pure logic (updateNPC, updateCoins, checkCollisions)
├── entities/
│   ├── NPC.java              # float x,y,velocity
│   └── Coin.java             # float x,y,fallSpeed
└── utils/
    └── GridRenderer.java     # Same as 09-00

test/
└── GameLogicTest.java        # Unit tests WITHOUT rendering
```

## GameEngine.java Structure
```java
public class GameEngine {
    private GameLogic gameLogic;
    private boolean running;
    
    public void start() {
        running = true;
        long lastTime = System.nanoTime();
        
        while (running) {
            // ✅ Calculate delta time
            float delta = calculateDelta(lastTime);
            lastTime = System.nanoTime();
            
            // ✅ Separated concerns
            update(delta);    // PURE LOGIC
            draw();           // PURE RENDERING
            
            // ✅ Frame rate control
            sync(startTime);
        }
    }
    
    private void update(float delta) {
        gameLogic.updateNPC(delta);
        gameLogic.updateCoins(delta);
        gameLogic.checkCollisions();
        // ✅ NO System.out here!
    }
    
    private void draw() {
        GridRenderer.clearScreen();
        GridRenderer.drawEntity('N', gameLogic.getNPCX(), ...);
        GridRenderer.drawEntity('C', ...);
        GridRenderer.drawHUD("Score: " + gameLogic.getScore());
        // ✅ NO logic here!
    }
}
```

## GameLogic.java (Testable)
```java
public class GameLogic {
    private NPC npc;
    private List<Coin> coins;
    private int score;
    
    // ✅ Delta time based movement
    public void updateNPC(float delta) {
        float newX = npc.getX() + npc.getVelocity() * delta;
        if (newX >= GRID_WIDTH) newX = 0;  // Wrap
        npc.setX(newX);
    }
    
    public void updateCoins(float delta) {
        for (Coin coin : coins) {
            float newY = coin.getY() + coin.getFallSpeed() * delta;
            if (newY >= GRID_HEIGHT) {
                coin.setY(0);  // Respawn
                coin.setX(random());
            } else {
                coin.setY(newY);
            }
        }
    }
    
    public void checkCollisions() {
        // Check NPC vs Coins
        // Update score if collision
        // ✅ NO System.out here!
    }
    
    // Getters for rendering
    public int getNPCX() { return (int)npc.getX(); }
    // ... more getters
}
```

## Entity Classes
**NPC.java**: float x, y, velocity | getters/setters
**Coin.java**: float x, y, fallSpeed | getters/setters

**IMPORTANT**: Use FLOAT for precision (delta time calculations)!

## Testing Requirements
```java
@Test
void testNPCMovement() {
    GameLogic logic = new GameLogic();
    logic.updateNPC(0.016f);  // 1 frame at 60 FPS
    // ✅ No rendering needed!
    assertTrue(logic.getNPCX() > 0);
}

@Test
void testFrameRateIndependence() {
    // 60 FPS: 0.016s delta
    // 30 FPS: 0.033s delta
    // Both should move same distance/second
}

@Test
void testCollision() {
    // Test collision without any display
}
```

## Demonstration Features
1. **Render Delay Test**: Add Thread.sleep(50) in draw() → logic still smooth
2. **Unit Tests**: Run tests without display
3. **Frame Rate Log**: Show update=2ms, draw=1ms, FPS=60

## Comments to Add
`// ✅ SOLUTION: Update and draw separated`
`// ✅ SOLUTION: Delta time for frame independence`
`// ✅ SOLUTION: Testable logic`

## Output
```
░░░░░░░░░░
░░░░C░░░░░
░N░░░░░░░░

Score: 10
FPS: 60 | Update: 2ms | Draw: 1ms
```

## Success Criteria
1. ✅ update() has ZERO System.out
2. ✅ draw() has ZERO logic
3. ✅ All tests passing (no display needed)
4. ✅ Stable 60 FPS
5. ✅ Render delay doesn't affect gameplay

Include SOLUTION.md explaining benefits!

Implementasikan dengan fokus pada clean architecture dan testability!
```
