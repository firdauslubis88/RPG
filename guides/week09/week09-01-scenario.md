# Branch: 09-01-with-game-loop

## 🎯 Learning Objective
Memahami SOLUSI dari problem monolithic loop dengan menerapkan proper game loop architecture: **separated update() and draw()**.

---

## 📖 Skenario: Game Dengan Separation of Concerns

### Context
Setelah melihat masalah di branch 09-00, kita refactor code dengan memisahkan:
- **update()**: Pure logic, no rendering
- **draw()**: Pure rendering, no logic
- **Delta time**: Frame rate independent movement

### Architecture Solution
```
main() {
    GameEngine engine = new GameEngine();
    engine.start();
}

GameEngine {
    void start() {
        while(running) {
            delta = calculateDeltaTime();
            update(delta);      // âœ… Logic only
            draw();             // âœ… Rendering only
            waitForNextFrame();
        }
    }
}
```

---

## âœ… Solutions to Previous Problems

### Solution 1: Frame Rate Independence
**Fix**: Logic uses delta time, rendering can skip frames.

**Implementation**:
```java
// Movement speed independent of FPS
float newX = oldX + (velocity * deltaTime);
```

**Real-World Benefits**:
- 60 FPS or 30 FPS → same gameplay speed
- GPU lag → visual lag only, not gameplay lag
- Different hardware → same experience

**Industry Standard**:
- Unity: `Update()` (frame-dependent) vs `FixedUpdate()` (time-based)
- Unreal: `Tick(DeltaTime)`
- LibGDX: `render(delta)`

### Solution 2: Testable Logic
**Fix**: update() is pure function, no side effects.

**Implementation**:
```java
@Test
void testNPCMovement() {
    NPC npc = new NPC(0, 5);
    GameLogic.updateNPC(npc, 0.016f);  // Simulate 1 frame
    assertEquals(1, npc.getX());       // âœ… No rendering needed!
}
```

**Real-World Benefits**:
- CI/CD pipeline compatible (no display)
- Fast test execution (< 1 second)
- Deterministic behavior (no flakiness)
- Refactoring confidence

### Solution 3: Maintainable Code
**Fix**: Clear structure with separated concerns.

**Implementation**:
```
GameEngine
├── update()        # Business logic (50 lines)
│   ├── updateNPC()
│   ├── updateCoins()
│   └── checkCollisions()
└── draw()          # Rendering (30 lines)
    ├── drawEntities()
    └── drawHUD()
```

**Real-World Benefits**:
- Bug in rendering? Only check draw()
- Bug in physics? Only check update()
- Team: 1 person logic, 1 person graphics
- Code review: Easier to spot issues

---

## 🎬 Demonstration Points

### Demo 1: Render Delay Doesn't Affect Logic
**Purpose**: Prove logic and render are decoupled.

**Why This Matters in Real World**:
Production scenarios where rendering slows:
- **GPU bottleneck**: Complex particle effects, shadows
- **Network sync**: Multiplayer waiting for server state
- **Display issues**: VSync, screen tearing prevention
- **Background tasks**: OS suddenly takes GPU resources

**Implementation**:
1. Add `Thread.sleep(50)` in draw() only
2. Run game
3. **Observe**: 
   - Visual = choppy (30 FPS)
   - Gameplay = smooth (NPC moves consistent speed)
4. Log frame time: update() still 16ms, draw() now 66ms

**Expected Learning**:
"Logic doesn't wait for rendering! Game still playable even if display lags."

### Demo 2: Unit Test Collision Detection
**Purpose**: Prove logic is testable.

**Why This Matters in Real World**:
Modern game development requires:
- **Automated testing**: Regression suite for every commit
- **CI/CD integration**: GitHub Actions, Jenkins
- **Parallel execution**: 100 tests in 10 seconds
- **No flakiness**: Deterministic, reliable results

**Implementation**:
```java
@Test
void testCoinCollection() {
    NPC npc = new NPC(5, 5);
    Coin coin = new Coin(5, 5);
    
    boolean collision = GameLogic.checkCollision(npc, coin);
    
    assertTrue(collision);  // âœ… No rendering, pure logic test!
}
```

**Expected Learning**:
"We can test game logic like any other code!"

### Demo 3: Frame Skipping
**Purpose**: Show advanced technique (optional).

**Implementation**:
1. Target 60 FPS
2. If update() takes 20ms → skip draw() this frame
3. Logic continues at 60 FPS, visual drops to 50 FPS
4. Game still responsive!

**Real-World Example**:
- Mobile games on battery save mode
- VR games maintaining high physics rate
- Competitive games prioritizing input response

---

## 📊 Metrics Comparison

| Aspect | 09-00 (Bad) | 09-01 (Good) |
|--------|-------------|--------------|
| Lines in main() | 150+ | 20 |
| Update-Render separation | ❌ Mixed | ✅ Separated |
| Delta time | ❌ No | ✅ Yes |
| Testability | ❌ Impossible | ✅ Full coverage |
| Frame rate independence | ❌ No | ✅ Yes |
| Maintainability | ❌ Spaghetti | ✅ Clean |
| CI/CD compatible | ❌ No | ✅ Yes |
| Render delay impact | ❌ Breaks game | ✅ Visual only |

---

## 🎓 Teaching Notes

### Key Concepts to Emphasize

#### 1. Delta Time Formula
```
distance = velocity × time
newPosition = oldPosition + (velocity × deltaTime)
```

**Example**:
- 60 FPS: delta ≈ 0.016s → move 10 × 0.016 = 0.16 pixels
- 30 FPS: delta ≈ 0.033s → move 10 × 0.033 = 0.33 pixels
- Same distance per second! (10 pixels/second)

#### 2. Separation of Concerns
Not just for web apps (MVC, layered architecture), also for games!

**Web Backend**:
- Controller (input)
- Service (logic)
- View (output)

**Game**:
- InputHandler (input)
- update() (logic)
- draw() (output)

#### 3. Professional Game Loop Pattern
All major engines use variants of this:
```
Unity: Update() + FixedUpdate()
Unreal: Tick(DeltaTime)
Godot: _process(delta) + _physics_process(delta)
LibGDX: render(delta)
```

This is THE foundation of game development.

---

### Common Student Questions

**Q**: "Kenapa tidak langsung render setelah update entity?"
**A**: Karena ada banyak entities. Update semua dulu, baru render semua sekaligus. Lebih efisien.

**Q**: "Bagaimana jika update() lebih lama dari 16ms (60 FPS)?"
**A**: Frame drop. Ada teknik advanced: fixed timestep, frame skipping. Untuk kuliah ini, kita keep simple.

**Q**: "Delta time bisa lebih dari 1 detik?"
**A**: Ya, kalau game freeze. Harus cap delta (max 0.1s) untuk prevent "spiral of death".

**Q**: "Apakah bisa update() dipanggil 2x sebelum draw()?"
**A**: Ya! Namanya fixed timestep. Physics engines sering pakai. Advanced topic.

---

## 🔄 Migration Guide (from 09-00)

### Step 1: Extract Logic
```java
// Before: logic in main
npc.x += 1;
coin.y += 1;

// After: logic in method
void updateNPC(NPC npc, float delta) {
    npc.x += npc.speed * delta;
}
```

### Step 2: Extract Rendering
```java
// Before: render in main
GridRenderer.drawEntity('N', npc.x, npc.y);

// After: render in method
void drawEntities() {
    GridRenderer.clear();
    GridRenderer.drawEntity('N', npc.x, npc.y);
    GridRenderer.drawEntity('C', coin.x, coin.y);
}
```

### Step 3: Calculate Delta Time
```java
long lastTime = System.nanoTime();

while (running) {
    long currentTime = System.nanoTime();
    float delta = (currentTime - lastTime) / 1_000_000_000.0f;
    lastTime = currentTime;
    
    update(delta);
    draw();
}
```

### Step 4: Frame Rate Control
```java
final int TARGET_FPS = 60;
final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

while (running) {
    long startTime = System.nanoTime();
    
    // ... update and draw ...
    
    long elapsedTime = System.nanoTime() - startTime;
    long waitTime = OPTIMAL_TIME - elapsedTime;
    
    if (waitTime > 0) {
        Thread.sleep(waitTime / 1_000_000);
    }
}
```

---

## ✅ Success Criteria

Students should be able to:
- [ ] Explain WHY separation is needed (not just HOW)
- [ ] Implement separated update() and draw()
- [ ] Calculate and use delta time correctly
- [ ] Write unit tests for game logic
- [ ] Explain frame rate independence

Code should:
- [ ] Run at stable 60 FPS
- [ ] update() has ZERO rendering code
- [ ] draw() has ZERO logic code
- [ ] All unit tests passing
- [ ] Render delay doesn't affect gameplay

---

**Next**: Branch 09-02 will introduce global state management problem (multiple GameManager instances) → leading to Singleton pattern in 09-03! 🚀
