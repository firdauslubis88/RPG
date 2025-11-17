# Week 12-04: State Pattern for Boss AI (SOLUTION)

**Branch**: `12-04-state-pattern`

**Compilation**: `javac -d bin/12-04-state-pattern src/*.java src/**/*.java`

**Run**: `java -cp bin/12-04-state-pattern Main`

---

## Overview

Week 12-04 demonstrates the **State Pattern** to solve the hardcoded boss AI anti-pattern from Week 12-03. This branch refactors the giant if/else chains and scattered state logic into clean, encapsulated state classes.

The State Pattern allows boss behavior to change dynamically based on HP percentage, with each state encapsulating both AI decision logic and counter relationship rules in a single class.

---

## What Changed from Week 12-03

### Problems in Week 12-03 (Anti-Pattern)

```java
// ❌ Hardcoded HP thresholds scattered everywhere
private String getBossState() {
    float hpPercent = (float) bossHp / bossMaxHp;
    if (hpPercent > 0.75f) return "NORMAL";
    else if (hpPercent > 0.50f) return "ANGRY";
    else if (hpPercent > 0.25f) return "DEFENSIVE";
    else return "ENRAGED";
}

// ❌ Giant if/else chain for AI decision
private String bossTurn() {
    float hpPercent = (float) bossHp / bossMaxHp;
    if (hpPercent > 0.75f) {
        // NORMAL AI logic
    } else if (hpPercent > 0.50f) {
        // ANGRY AI logic
    } else if (hpPercent > 0.25f) {
        // DEFENSIVE AI logic
    } else {
        // ENRAGED AI logic
    }
}

// ❌ Giant if/else dispatching to state-specific methods
private void resolveActions(String playerAction, String bossAction) {
    float hpPercent = (float) bossHp / bossMaxHp;
    if (hpPercent > 0.75f) resolveNormalState(playerAction, bossAction);
    else if (hpPercent > 0.50f) resolveAngryState(playerAction, bossAction);
    else if (hpPercent > 0.25f) resolveDefensiveState(playerAction, bossAction);
    else resolveEnragedState(playerAction, bossAction);
}

// ❌ Separate method for each state (duplicated structure)
private void resolveNormalState(...) { /* 50+ lines */ }
private void resolveAngryState(...) { /* 50+ lines */ }
private void resolveDefensiveState(...) { /* 50+ lines */ }
private void resolveEnragedState(...) { /* 50+ lines */ }
```

**Issues**:
- Hardcoded thresholds (0.75, 0.50, 0.25) repeated 3+ times
- Cannot add new states without modifying existing code
- State behavior scattered across multiple methods
- No compile-time safety

### Solution in Week 12-04 (State Pattern)

```java
// ✅ Clean state interface
public interface BossState {
    String chooseAction(Random random);
    void resolveActions(String playerAction, String bossAction, BattleContext context);
    BossState checkTransition(float hpPercent);
    String getStateName();
    String getRulesDescription();
}

// ✅ Each state is a separate class (example: NormalState)
public class NormalState implements BossState {
    @Override
    public String chooseAction(Random random) {
        // NORMAL AI logic encapsulated here
        int choice = random.nextInt(4);
        switch (choice) {
            case 0: return BattleContext.ATTACK;
            case 1: return BattleContext.DEFEND;
            case 2: return BattleContext.MAGIC;
            case 3: return BattleContext.COUNTER;
        }
    }

    @Override
    public void resolveActions(String playerAction, String bossAction, BattleContext context) {
        // NORMAL counter rules encapsulated here
        if (playerAction.equals(BattleContext.ATTACK) && bossAction.equals(BattleContext.MAGIC)) {
            context.dealDamageToBoss(25);
        }
        // ... all NORMAL state rules in one place
    }

    @Override
    public BossState checkTransition(float hpPercent) {
        // Transition logic encapsulated in state
        if (hpPercent <= 0.75f) {
            return AngryState.getInstance();
        }
        return null;
    }
}

// ✅ BattleSystem is now clean and simple
private BossState currentState = NormalState.getInstance();

// AI decision: just delegate to state!
String bossAction = currentState.chooseAction(random);

// Action resolution: just delegate to state!
currentState.resolveActions(playerAction, bossAction, context);

// State transition: just ask state!
BossState newState = currentState.checkTransition(hpPercent);
if (newState != null) {
    currentState = newState;
}
```

**Benefits**:
- Each state encapsulates ALL its behavior in one class
- Adding new states: create new class, no modification to existing code
- Thresholds defined once in each state's `checkTransition()`
- Easy to test individual states
- Follows Open/Closed Principle

---

## State Pattern Architecture

### Class Diagram

```
┌─────────────────┐
│  BattleSystem   │
│─────────────────│
│ - currentState  │───────┐
│ - bossHp        │       │
│ - player        │       │
└─────────────────┘       │
                          │
                          │ uses
                          ▼
                  ┌───────────────┐
                  │  <<interface>>│
                  │   BossState   │
                  │───────────────│
                  │ + chooseAction()         │
                  │ + resolveActions()       │
                  │ + checkTransition()      │
                  │ + getStateName()         │
                  │ + getRulesDescription()  │
                  └───────────────┘
                          △
                          │ implements
         ┌────────────────┼────────────────┬────────────────┐
         │                │                │                │
┌────────┴────────┐  ┌───┴──────────┐  ┌──┴────────────┐  ┌┴──────────────┐
│  NormalState    │  │  AngryState  │  │DefensiveState │  │ EnragedState  │
│─────────────────│  │──────────────│  │───────────────│  │───────────────│
│ HP > 75%        │  │ HP > 50%     │  │ HP > 25%      │  │ HP ≤ 25%      │
│ Balanced AI     │  │ Aggressive   │  │ Defensive     │  │ Berserk       │
└─────────────────┘  └──────────────┘  └───────────────┘  └───────────────┘
```

### Components

#### 1. **BossState Interface** (`battle/BossState.java`)

Defines the contract that all boss states must implement:

```java
public interface BossState {
    // AI: Choose action based on state's strategy
    String chooseAction(Random random);

    // Rules: Resolve actions with state-specific counter relationships
    void resolveActions(String playerAction, String bossAction, BattleContext context);

    // Transition: Determine if state should change
    BossState checkTransition(float hpPercent);

    // Display: State name for UI
    String getStateName();

    // Display: Rules description for UI
    String getRulesDescription();
}
```

#### 2. **BattleContext** (`battle/BattleContext.java`)

Provides context for states to apply damage and query battle info:

```java
public class BattleContext {
    public static final String ATTACK = "ATTACK";
    public static final String DEFEND = "DEFEND";
    public static final String MAGIC = "MAGIC";
    public static final String COUNTER = "COUNTER";

    public void dealDamageToPlayer(int damage);
    public void dealDamageToBoss(int damage);
    public int getBossHp();
    public int getBossMaxHp();
    public float getBossHpPercent();
    public int getPlayerHp();
}
```

#### 3. **Concrete States** (`battle/*State.java`)

Four state classes, each encapsulating unique behavior:

##### NormalState (HP > 75%)
- **AI**: Random 25% each action (balanced)
- **Rules**: ATTACK>MAGIC(25), MAGIC>DEFEND(30), DEFEND>ATTACK(15), COUNTER>ALL(40)
- **Transition**: To AngryState at 75% HP

##### AngryState (75% ≥ HP > 50%)
- **AI**: 50% ATTACK, 30% COUNTER, 20% MAGIC (aggressive)
- **Rules**: ATTACK>DEFEND(35), MAGIC>COUNTER(35), DEFEND>MAGIC(20), COUNTER>ATTACK(50)
- **Transition**: To DefensiveState at 50% HP

##### DefensiveState (50% ≥ HP > 25%)
- **AI**: 60% DEFEND, 30% COUNTER, 10% MAGIC (defensive)
- **Rules**: MAGIC>DEFEND(40), DEFEND>ATTACK(25), COUNTER>MAGIC(45), ATTACK>COUNTER(30)
- **Transition**: To EnragedState at 25% HP

##### EnragedState (HP ≤ 25%)
- **AI**: 70% ATTACK, 20% MAGIC, 10% COUNTER (berserk)
- **Rules**: DEFEND>ATTACK(35), ATTACK>MAGIC(45), MAGIC>COUNTER(40), COUNTER>DEFEND(60)
- **Transition**: None (final state)

#### 4. **BattleSystem** (`battle/BattleSystem.java`)

Now clean and simple, delegates to states:

```java
public class BattleSystem {
    private BossState currentState;  // Current boss state

    // Initialize with NORMAL state
    this.currentState = NormalState.getInstance();

    // Game loop delegates to current state:
    String bossAction = currentState.chooseAction(random);
    currentState.resolveActions(playerAction, bossAction, context);
    BossState newState = currentState.checkTransition(hpPercent);
    if (newState != null) currentState = newState;
}
```

---

## Design Pattern Details

### State Pattern Elements

1. **Context**: `BattleSystem` maintains reference to current state
2. **State Interface**: `BossState` defines common operations
3. **Concrete States**: `NormalState`, `AngryState`, `DefensiveState`, `EnragedState`
4. **State Transition**: Each state determines when to transition

### Singleton Pattern for States

States use Singleton pattern (optional optimization):

```java
public class NormalState implements BossState {
    private static NormalState instance = null;

    public static NormalState getInstance() {
        if (instance == null) {
            instance = new NormalState();
        }
        return instance;
    }

    private NormalState() {} // Private constructor
}
```

**Why Singleton for states?**
- States are stateless (no instance variables except for thresholds)
- No need to create multiple instances
- Memory efficient
- Easy to compare states: `currentState == NormalState.getInstance()`

---

## Comparison: Anti-Pattern vs State Pattern

| Aspect | Week 12-03 (Anti-Pattern) | Week 12-04 (State Pattern) |
|--------|---------------------------|---------------------------|
| **State Determination** | `getBossState()` with if/else | Polymorphic state objects |
| **AI Decision** | `bossTurn()` with giant if/else | `currentState.chooseAction()` |
| **Counter Rules** | `resolveXxxState()` methods (4x50 lines) | `state.resolveActions()` |
| **Transition Logic** | Recalculated every turn | `state.checkTransition()` |
| **Adding New State** | Modify 3+ existing methods | Create 1 new state class |
| **Code in BattleSystem** | ~550 lines | ~260 lines (52% reduction!) |
| **Testability** | Hard (tightly coupled) | Easy (isolated state classes) |
| **Maintainability** | Low (scattered logic) | High (encapsulated behavior) |
| **OCP Compliance** | ❌ Violates | ✅ Follows |

### Lines of Code Reduction

**Week 12-03 (Anti-Pattern)**:
- `BattleSystem.java`: ~550 lines
  - `getBossState()`: 15 lines
  - `bossTurn()`: 35 lines
  - `resolveActions()`: 15 lines
  - `resolveNormalState()`: 50 lines
  - `resolveAngryState()`: 60 lines
  - `resolveDefensiveState()`: 60 lines
  - `resolveEnragedState()`: 60 lines

**Week 12-04 (State Pattern)**:
- `BattleSystem.java`: ~260 lines (no state-specific code!)
- `BossState.java`: 60 lines (interface)
- `BattleContext.java`: 75 lines (helper)
- `NormalState.java`: 100 lines (all NORMAL logic)
- `AngryState.java`: 120 lines (all ANGRY logic)
- `DefensiveState.java`: 120 lines (all DEFENSIVE logic)
- `EnragedState.java`: 120 lines (all ENRAGED logic)

**Total**: ~855 lines vs ~550 lines (+55% code)

**But**:
- Much better organized (each state is self-contained)
- Easy to add new states (no modification to existing code)
- Easy to test (each state can be unit tested independently)
- Following SOLID principles (OCP, SRP)

---

## New Files Created

### State Pattern Implementation

1. **`src/battle/BossState.java`**: State interface defining boss behavior contract
2. **`src/battle/BattleContext.java`**: Context object for damage application and state queries
3. **`src/battle/NormalState.java`**: NORMAL state (HP > 75%) with balanced AI
4. **`src/battle/AngryState.java`**: ANGRY state (HP > 50%) with aggressive AI
5. **`src/battle/DefensiveState.java`**: DEFENSIVE state (HP > 25%) with defensive AI
6. **`src/battle/EnragedState.java`**: ENRAGED state (HP ≤ 25%) with berserk AI

### Modified Files

1. **`src/battle/BattleSystem.java`**: Refactored to use State Pattern
   - Removed: `getBossState()`, `bossTurn()`, `resolveActions()`, `resolveXxxState()` methods
   - Added: `currentState` field and state delegation
   - Reduced from ~550 lines to ~260 lines

2. **`src/GameLogic.java`**: Updated header comments to reflect State Pattern

---

## How to Add a New Boss State

Thanks to the State Pattern, adding a new state is easy!

### Example: Adding "BERSERK" State (HP ≤ 10%)

1. **Create new state class** (`battle/BerserkState.java`):

```java
public class BerserkState implements BossState {
    private static BerserkState instance = null;

    public static BerserkState getInstance() {
        if (instance == null) instance = new BerserkState();
        return instance;
    }

    @Override
    public String chooseAction(Random random) {
        // BERSERK AI: 90% ATTACK, 10% COUNTER
        return random.nextInt(100) < 90 ? BattleContext.ATTACK : BattleContext.COUNTER;
    }

    @Override
    public void resolveActions(String playerAction, String bossAction, BattleContext context) {
        // BERSERK rules: Only DEFEND can save you!
        if (playerAction.equals(BattleContext.DEFEND) && bossAction.equals(BattleContext.ATTACK)) {
            context.dealDamageToBoss(50);
        } else {
            context.dealDamageToPlayer(80); // Massive damage!
        }
    }

    @Override
    public BossState checkTransition(float hpPercent) {
        return null; // Final state
    }

    @Override
    public String getStateName() {
        return "🔥 BERSERK - One-shot attacks!";
    }

    @Override
    public String getRulesDescription() {
        return "DEFEND OR DIE! Only DEFEND can counter ATTACK!";
    }
}
```

2. **Update EnragedState transition**:

```java
// In EnragedState.java
@Override
public BossState checkTransition(float hpPercent) {
    if (hpPercent <= 0.10f) {
        return BerserkState.getInstance();
    }
    return null;
}
```

**That's it!** No need to modify `BattleSystem` or any other existing code.

Compare to Week 12-03 where you'd need to:
1. Add new if/else branch in `getBossState()` ❌
2. Add new if/else branch in `bossTurn()` ❌
3. Add new if/else branch in `resolveActions()` ❌
4. Create new `resolveBerserkState()` method ❌

---

## Design Patterns Used

### ✅ Patterns in This Branch

1. **State Pattern** (Week 12-04 - NEW!): Boss AI behavior
   - Context: `BattleSystem`
   - State Interface: `BossState`
   - Concrete States: `NormalState`, `AngryState`, `DefensiveState`, `EnragedState`
   - Benefits: Encapsulated behavior, easy to extend, testable

2. **Singleton Pattern** (Weeks 09-03, 12-04): Global instances
   - `GameManager`: Global game state
   - `EventBus`: Global event system
   - State classes: Stateless singletons for memory efficiency

3. **Strategy Pattern** (Week 12-02): Difficulty system
   - `DifficultyStrategy` interface with `DemoDifficulty`, `EasyDifficulty`, etc.

4. **Observer Pattern** (Week 11-04): Event system
   - `EventBus` publishes events to registered listeners

5. **Command Pattern** (Week 11-02): Input handling
   - `Command` interface with move commands

6. **Object Pool Pattern** (Week 10-04): Obstacle reuse
   - `ObstaclePool` for memory efficiency

7. **Factory Pattern** (Week 10-02): Obstacle creation
   - `ObstacleFactory` implementations

---

## State Transition Flow

```
Start Battle
     │
     ▼
┌────────────┐
│  NORMAL    │  HP > 75%
│  😐        │  Balanced AI
│  Random    │  ATTACK>MAGIC | MAGIC>DEFEND | DEFEND>ATTACK
└────┬───────┘
     │ HP ≤ 75%
     ▼
┌────────────┐
│  ANGRY     │  HP > 50%
│  😠        │  Aggressive AI (50% ATTACK, 30% COUNTER)
│  Offensive │  ATTACK>DEFEND | MAGIC>COUNTER | COUNTER>ATTACK
└────┬───────┘
     │ HP ≤ 50%
     ▼
┌────────────┐
│ DEFENSIVE  │  HP > 25%
│  🛡️        │  Defensive AI (60% DEFEND, 30% COUNTER)
│  Shields   │  MAGIC>DEFEND | DEFEND>ATTACK | COUNTER>MAGIC
└────┬───────┘
     │ HP ≤ 25%
     ▼
┌────────────┐
│  ENRAGED   │  HP ≤ 25%
│  😡        │  Berserk AI (70% ATTACK, 20% MAGIC)
│  Chaos     │  DEFEND>ATTACK | ATTACK>MAGIC | MAGIC>COUNTER
└────┬───────┘
     │
     ▼
   Death
```

---

## Code Quality Metrics

### Cyclomatic Complexity

**Week 12-03 (Anti-Pattern)**:
- `bossTurn()`: Complexity = 5 (4 branches)
- `resolveActions()`: Complexity = 5 (4 branches)
- `resolveNormalState()`: Complexity = 8 (7 branches)
- Total: 18+ (high complexity, hard to test)

**Week 12-04 (State Pattern)**:
- `BattleSystem.startBattle()`: Complexity = 3 (minimal branching)
- Each state's `chooseAction()`: Complexity = 4 (simple switch)
- Each state's `resolveActions()`: Complexity = 8 (isolated)
- Total: Distributed across isolated classes (easy to test)

### Cohesion and Coupling

**Week 12-03 (Anti-Pattern)**:
- Low cohesion: State logic scattered across multiple methods
- High coupling: BattleSystem tightly coupled to all state behaviors

**Week 12-04 (State Pattern)**:
- High cohesion: Each state class contains all related behavior
- Low coupling: BattleSystem only depends on `BossState` interface

---

## Testing Strategy

### Unit Testing States (Now Easy!)

```java
public class NormalStateTest {
    @Test
    public void testAIRandomness() {
        NormalState state = NormalState.getInstance();
        Random random = new Random(42); // Seeded for reproducibility

        // Test AI decision
        String action = state.chooseAction(random);
        assertTrue(action.equals("ATTACK") || action.equals("DEFEND") ||
                   action.equals("MAGIC") || action.equals("COUNTER"));
    }

    @Test
    public void testCounterRules() {
        NormalState state = NormalState.getInstance();
        MockBattleContext context = new MockBattleContext();

        // Test ATTACK > MAGIC
        state.resolveActions("ATTACK", "MAGIC", context);
        assertEquals(25, context.getBossDamageTaken());
    }

    @Test
    public void testTransitionToAngry() {
        NormalState state = NormalState.getInstance();

        // Should transition at 75% or below
        BossState newState = state.checkTransition(0.75f);
        assertNotNull(newState);
        assertTrue(newState instanceof AngryState);

        // Should NOT transition above 75%
        newState = state.checkTransition(0.76f);
        assertNull(newState);
    }
}
```

Compare to Week 12-03 where testing individual states required:
- Mocking entire BattleSystem
- Setting up boss HP to specific percentages
- Testing giant methods with multiple responsibilities

---

## SOLID Principles Compliance

### Single Responsibility Principle (SRP)
✅ **Before (12-03)**: BattleSystem handled AI, rules, transitions, UI
✅ **Now (12-04)**:
- BattleSystem: Battle flow orchestration
- BossState: State-specific behavior
- BattleContext: Damage application and queries

### Open/Closed Principle (OCP)
✅ **Before (12-03)**: Must modify existing code to add states ❌
✅ **Now (12-04)**: Add new states by creating new classes, no modification ✅

### Liskov Substitution Principle (LSP)
✅ All state classes can be used interchangeably through `BossState` interface

### Interface Segregation Principle (ISP)
✅ `BossState` interface is focused and cohesive

### Dependency Inversion Principle (DIP)
✅ BattleSystem depends on `BossState` abstraction, not concrete states

---

## Performance Considerations

### State Instance Management

States use Singleton pattern:
- **Memory**: Only 1 instance per state class (4 total)
- **Performance**: No allocation overhead during battle
- **Thread Safety**: Single-threaded game, no synchronization needed

### Transition Checking

States check their own transitions:
- **Efficiency**: Only check when HP changes (after damage)
- **Clarity**: Transition logic co-located with state behavior

---

## Next Steps

**Week 12-05: Game State Pattern**

Implement overall game flow states:
- Menu State: Main menu and difficulty selection
- Playing State: Dungeon exploration
- Battle State: Boss battle
- Victory State: Win screen
- Defeat State: Game over screen

---

## Teaching Points

### What This Branch Demonstrates

1. **State Pattern**: Clean way to manage behavior that changes based on state
2. **Encapsulation**: Each state contains ALL its logic in one place
3. **Open/Closed Principle**: Easy to add new states without modifying existing code
4. **Polymorphism**: `currentState.chooseAction()` calls different code based on runtime type
5. **Testability**: Each state can be unit tested independently

### Why State Pattern is Better

- **Maintainability**: State logic is centralized, not scattered
- **Extensibility**: Adding states is trivial (just create new class)
- **Readability**: Each state is self-documenting
- **Testability**: States can be tested in isolation
- **Flexibility**: States can be swapped at runtime

### When to Use State Pattern

✅ **Use State Pattern when**:
- Object behavior changes based on internal state
- State-specific code is duplicated across multiple methods
- Many conditional statements depend on object state

❌ **Don't use State Pattern when**:
- Only 2-3 simple states
- State transitions are trivial
- Overhead of multiple classes not justified

---

## Related Branches

- **Previous**: `12-03-hardcoded-boss` - Hardcoded boss AI (anti-pattern)
- **Next**: `12-05-game-state-pattern` - Overall game flow states
- **Related**: `12-02-strategy-pattern` - Strategy Pattern for difficulty

---

## References

- **Design Pattern**: State Pattern (GoF)
- **Related Patterns**: Strategy Pattern, Command Pattern
- **Principles**: Open/Closed Principle, Single Responsibility Principle
- **Book**: "Design Patterns: Elements of Reusable Object-Oriented Software" by Gang of Four

---

## Compilation Output

Compiled to: `bin/12-04-state-pattern/`

New battle package structure:
```
battle/
├── BossState.class            (interface)
├── BattleContext.class        (helper)
├── BattleSystem.class         (context)
├── NormalState.class          (concrete state)
├── AngryState.class           (concrete state)
├── DefensiveState.class       (concrete state)
└── EnragedState.class         (concrete state)
```
