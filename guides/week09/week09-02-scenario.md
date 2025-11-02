# Branch: 09-02-without-singleton

## 🎯 Learning Objective
Memahami MASALAH dari tidak menggunakan Singleton pattern: **multiple instances** dan **state inconsistency**.

---

## 📖 Skenario: Game Tanpa Global State Management

### Context
Game sudah punya proper game loop (dari 09-01), tapi sekarang ada masalah baru:
- **GameManager**: Menyimpan global state (score, game time, level)
- **Problem**: Bisa dibuat multiple instances
- **Result**: State tidak konsisten antar components

### Architecture Problem
```java
// ❌ Multiple instances possible
GameManager manager1 = new GameManager();
GameManager manager2 = new GameManager();

// NPC updates score in manager1
manager1.addScore(10);

// HUD reads score from manager2
int displayScore = manager2.getScore();  // Still 0!
```

---

## 🔴 Problems yang Akan Muncul

### Problem 1: Multiple Instances Bug
**Issue**: Setiap class bisa create GameManager instance sendiri.

**Real-World Scenario**:
```java
// GameLogic creates instance A
GameManager managerA = new GameManager();
managerA.addScore(10);  // Score = 10 in instance A

// HUD creates instance B
GameManager managerB = new GameManager();
int score = managerB.getScore();  // Score = 0 in instance B!
```

**Consequences**:
- Player collects 5 coins → score shows 0
- Game time = 120 seconds → HUD shows 0
- Level 3 → HUD shows Level 1
- Extremely confusing bugs!

**Real-World Justification**:
Production scenarios ini terjadi di:
- **Microservices**: Multiple instances tanpa shared state
- **Desktop apps**: Multiple windows, inconsistent data
- **Mobile apps**: Activity recreation, lost state
- **Games**: Different managers in different scenes

### Problem 2: Object Drilling Hell
**Issue**: Harus pass GameManager ke SEMUA classes.

**Code Smell**:
```java
// Constructor pollution
GameEngine(GameManager manager) { ... }
GameLogic(GameManager manager) { ... }
NPC(GameManager manager) { ... }
Coin(GameManager manager) { ... }
HUD(GameManager manager) { ... }
Enemy(GameManager manager) { ... }

// Deep drilling
Main → GameEngine → GameLogic → NPC → manager
5 levels deep just to access score!
```

**Real-World Pain**:
- Refactoring nightmare
- Adding parameter to 20 classes
- Merge conflicts in teams
- Testing requires many mocks

**Real-World Justification**:
- **React**: Props drilling (solved by Context API)
- **Spring**: Constructor injection everywhere (solved by DI)
- **Angular**: Service injection chains
- **Games**: Entity systems needing global state

### Problem 3: Testing Complexity
**Issue**: Setiap test harus create dan mock GameManager.

**Test Pollution**:
```java
@Test
void testNPCMovement() {
    // ❌ Setup overhead
    GameManager mockManager = mock(GameManager.class);
    GameLogic logic = new GameLogic(mockManager);
    NPC npc = new NPC(mockManager);
    
    // Test actual logic...
}

// Repeat for EVERY test!
```

**Real-World Impact**:
- Test setup > test logic (bad ratio)
- Slow test execution
- Brittle tests (break on refactor)
- Developers skip writing tests

---

## 🧪 Demonstration Points

### Demo 1: Score Inconsistency Bug
**Purpose**: Show multiple instances cause state mismatch.

**Why This Matters in Real World**:
Production bugs yang susah di-debug:
- "User says they have 100 points, but leaderboard shows 0"
- "Game thinks player is dead, but UI shows full health"
- "Purchase completed, but inventory empty"
- Hard to reproduce (timing dependent)

**Implementation**:
```java
// GameLogic.java
public class GameLogic {
    private GameManager manager = new GameManager();  // Instance A
    
    public void collectCoin() {
        manager.addScore(10);
        System.out.println("Logic score: " + manager.getScore());  // 10
    }
}

// HUD.java
public class HUD {
    private GameManager manager = new GameManager();  // Instance B
    
    public void display() {
        System.out.println("HUD score: " + manager.getScore());  // 0!
    }
}
```

**Run & Observe**:
```
Player collects 5 coins...
Logic score: 10
Logic score: 20
Logic score: 30

HUD displays:
Score: 0  ← BUG!
```

**Expected Learning**:
"We need ONE source of truth for game state!"

---

### Demo 2: Constructor Explosion
**Purpose**: Show object drilling complexity.

**Why This Matters in Real World**:
Code maintenance disasters:
- Add new feature → modify 15 constructors
- Refactor → merge conflicts everywhere
- Onboarding new dev → confusing parameter chains
- "Where is this value coming from?" → trace through 5 files

**Implementation**:
```java
// Every class needs manager parameter
public class Main {
    public static void main(String[] args) {
        GameManager manager = new GameManager();
        GameEngine engine = new GameEngine(manager);
        engine.start();
    }
}

public class GameEngine {
    private GameLogic logic;
    
    public GameEngine(GameManager manager) {
        this.logic = new GameLogic(manager);
    }
}

public class GameLogic {
    private NPC npc;
    private List<Coin> coins;
    
    public GameLogic(GameManager manager) {
        this.npc = new NPC(manager);
        this.coins = List.of(new Coin(manager), new Coin(manager));
    }
}

// And so on...
```

**Count Parameters**:
- Main: 1 manager
- GameEngine: 1 manager
- GameLogic: 1 manager
- NPC: 1 manager
- Coin (x3): 3 managers
- Total: 7 manager references!

**Expected Learning**:
"This doesn't scale. Need global access pattern."

---

### Demo 3: Testing Overhead
**Purpose**: Show test complexity without singleton.

**Implementation**:
```java
@Test
void testCoinCollection() {
    // ❌ SO MUCH SETUP!
    GameManager manager = new GameManager();
    GameLogic logic = new GameLogic(manager);
    NPC npc = new NPC(manager);
    Coin coin = new Coin(manager);
    
    // Position entities
    npc.setPosition(5, 5);
    coin.setPosition(5, 5);
    
    // Actual test (only 2 lines!)
    logic.checkCollisions();
    assertEquals(10, manager.getScore());
}

// Every test repeats this setup!
```

**Comparison**:
- Setup: 6 lines
- Test logic: 2 lines
- Ratio: 3:1 (bad!)

**Expected Learning**:
"Tests should focus on behavior, not setup."

---

## 📊 Metrics to Track

| Aspect | Without Singleton |
|--------|-------------------|
| Constructor parameters | 5-10 per class |
| GameManager instances | N (uncontrolled) |
| State consistency | ❌ Multiple states |
| Test setup LOC | 5-10 lines per test |
| Refactoring risk | ❌ High (many files) |
| Debugging difficulty | ❌ Hard (which instance?) |

---

## 🎓 Teaching Notes

### Key Points to Emphasize

#### 1. Single Source of Truth
In distributed systems:
- **Database**: Primary key = single source
- **Blockchain**: Consensus = single truth
- **Game**: GameManager = single state

Without this → chaos!

#### 2. The Object Drilling Problem
This is a known anti-pattern:
- **React**: Props drilling
- **Spring**: Constructor injection chains
- **Games**: Manager passing

Solutions vary:
- React: Context API
- Spring: Dependency Injection
- Games: Singleton pattern

#### 3. When Multiple Instances ARE Good
Singleton is NOT always the answer:
- **Player**: Each player = own instance
- **Enemy**: Many enemies = many instances
- **Bullet**: Pool of instances

Use Singleton only for **truly global** state.

---

### Common Student Questions

**Q**: "Kenapa tidak pakai static class?"
**A**: Static class = namespace. Singleton = instance dengan lifecycle control. Singleton bisa lazy init, implement interface, dll.

**Q**: "Bagaimana dengan dependency injection (Spring)?"
**A**: DI container ensures singleton behavior. Concept sama, implementation beda. Game tidak pakai Spring (too heavy).

**Q**: "Apakah global state selalu buruk?"
**A**: Dalam OOP purist view, ya. Tapi di game development, pragmatism > purity. Game perlu performance, singleton helps.

**Q**: "Bagaimana testing jika pakai singleton?"
**A**: Trade-off. Singleton = harder to mock. Solution: reset methods, test utilities. Atau use Multiton for tests.

---

## 🔄 How This Happens in Production

### Scenario 1: New Developer Mistake
```java
// Dev A creates first use
GameManager manager = new GameManager();

// Dev B doesn't know, creates another
GameManager manager = new GameManager();

// No compiler error! Bug lurks...
```

### Scenario 2: Refactoring Gone Wrong
```java
// Before: Singleton
GameManager.getInstance().addScore(10);

// Refactor: Remove singleton (mistake)
GameManager manager = new GameManager();
manager.addScore(10);

// Forgot to update other files...
// Now multiple instances exist!
```

### Scenario 3: Copy-Paste Error
```java
// Copy from example without understanding
public class HUD {
    // ❌ Oops, created new instance
    private GameManager manager = new GameManager();
}
```

**Prevention**: Make constructor private!

---

## ✅ Success Criteria (For Problem Demo)

Students should observe:
- [ ] Score inconsistency bug (HUD shows 0)
- [ ] Many constructor parameters
- [ ] Test setup overhead
- [ ] Difficulty tracing state

Code should intentionally have:
- [ ] Multiple GameManager instances
- [ ] Public constructor (no prevention)
- [ ] Object drilling (5+ levels deep)
- [ ] Inconsistent state bugs
- [ ] Comments marking problems

---

**Next**: Branch 09-03 will solve ALL these problems with Singleton pattern! 🚀

**Key Message**: "Global state is necessary in games. Without Singleton, we get chaos. But Singleton comes with trade-offs (testing, hidden dependencies). Choose wisely!"
