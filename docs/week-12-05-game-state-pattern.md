# Week 12-05: Game State Pattern

**Branch**: `12-05-game-state-pattern`

**Compilation**: `javac -d bin/12-05-game-state-pattern src/*.java src/**/*.java`

**Run**: `java -cp bin/12-05-game-state-pattern MainWithGameState`

---

## Overview

Week 12-05 demonstrates the **Game State Pattern** to manage overall game flow and transitions between different phases: Menu → Playing → Battle → Victory/Defeat.

This pattern applies the State Pattern (from Week 12-04) at a higher level to manage the entire game lifecycle, not just boss behavior.

---

## Game State Flow

```
┌──────────────┐
│  MenuState   │  Player selects difficulty
└──────┬───────┘
       │
       ▼
┌─────────────────────┐
│ SimplePlayingState  │  Dungeon exploration
└──────┬──────────────┘  (Simplified for demonstration)
       │
       ▼
┌──────────────┐
│ BattleState  │  Boss battle
└──────┬───────┘
       │
       ├─────────┐
       ▼         ▼
┌─────────┐  ┌──────────┐
│ Victory │  │  Defeat  │
│  State  │  │  State   │
└────┬────┘  └─────┬────┘
     │             │
     └──────┬──────┘
            │
            ▼
      (Play Again?)
            │
            ▼
     ┌──────────────┐
     │  MenuState   │  Loop back
     └──────────────┘
```

---

## New Files Created

### 1. Game State Interface

**`src/gamestate/GameState.java`**: Interface defining state contract
```java
public interface GameState {
    void enter();                            // Called when entering state
    GameState update(float deltaTime);       // Update and return next state
    void render();                           // Render state
    void exit();                             // Called when leaving state
    String getStateName();                   // For debugging
}
```

### 2. Game State Context

**`src/gamestate/GameStateContext.java`**: Manages current state and transitions
```java
public class GameStateContext {
    private GameState currentState;

    public void setState(GameState newState) {
        if (currentState != null) currentState.exit();
        currentState = newState;
        if (currentState != null) currentState.enter();
    }

    public void update(float deltaTime) {
        GameState nextState = currentState.update(deltaTime);
        if (nextState != null && nextState != currentState) {
            setState(nextState);
        }
    }
}
```

### 3. Concrete State Classes

**`src/gamestate/MenuState.java`**: Main menu with difficulty selection
- Shows MainMenu UI
- Transitions to SimplePlayingState

**`src/gamestate/SimplePlayingState.java`**: Simplified playing state (demonstration)
- In full implementation, would integrate GameEngine
- For now, demonstrates concept by immediately transitioning

**`src/gamestate/BattleState.java`**: Boss battle
- Runs BattleSystem
- Transitions to VictoryState or DefeatState

**`src/gamestate/VictoryState.java`**: Win screen
- Shows victory message
- Asks to play again

**`src/gamestate/DefeatState.java`**: Game over screen
- Shows defeat message
- Asks to try again

### 4. New Main Class

**`src/MainWithGameState.java`**: Entry point using Game State Pattern
```java
public class MainWithGameState {
    public static void main(String[] args) {
        GameStateContext context = new GameStateContext();
        context.setState(new MenuState());

        while (context.isRunning() && context.getCurrentState() != null) {
            context.update(0.016f);
            context.render();
        }
    }
}
```

---

## Benefits of Game State Pattern

### 1. Clean Separation of Concerns
Each game phase has its own class with dedicated logic:
- MenuState handles menus
- PlayingState handles exploration
- BattleState handles combat
- VictoryState/DefeatState handle end screens

### 2. Explicit State Transitions
Transitions are clear and manageable:
```java
// In MenuState
return new SimplePlayingState(selectedStrategy);

// In BattleState
if (playerWon) return new VictoryState(strategy);
else return new DefeatState(strategy);
```

### 3. Easy to Extend
Adding new states is trivial:
- Pause state: Just create `PauseState.java`
- Inventory state: Just create `InventoryState.java`
- Shop state: Just create `ShopState.java`

### 4. State-Specific Input Handling
Each state can have its own input logic:
- Menu: Arrow keys + Enter
- Playing: WASD movement
- Battle: Numbered choices
- Victory/Defeat: Y/N prompt

---

## Design Pattern Comparison

### Boss AI State Pattern (Week 12-04)
- **Scope**: Boss behavior during battle
- **States**: NormalState, AngryState, DefensiveState, EnragedState
- **Transitions**: Based on HP percentage
- **Purpose**: Manage boss behavior changes

### Game State Pattern (Week 12-05)
- **Scope**: Overall game flow
- **States**: MenuState, PlayingState, BattleState, VictoryState, DefeatState
- **Transitions**: Based on player actions and game events
- **Purpose**: Manage game phase transitions

**Both use the same State Pattern, just at different levels!**

---

## Implementation Notes

### Simplified Playing State

This branch uses `SimplePlayingState` as a demonstration because fully integrating GameEngine would require significant refactoring:

**Current GameEngine**: Blocking game loop
```java
public void start() {
    while (running) {
        // Game loop blocks here
    }
}
```

**State Pattern needs**: Frame-based updates
```java
public GameState update(float deltaTime) {
    // Update one frame
    // Return next state if needed
}
```

**Future Work**: Refactor GameEngine to support frame-based updates for full integration.

---

## Pattern Evolution Across Weeks

| Week | Pattern | Scope |
|------|---------|-------|
| 09-03 | Singleton | GameManager, EventBus |
| 10-02 | Factory | Obstacle creation |
| 10-04 | Object Pool | Obstacle reuse |
| 11-02 | Command | Input handling |
| 11-04 | Observer | Event system |
| 12-02 | Strategy | Difficulty selection |
| **12-04** | **State** | **Boss AI behavior** |
| **12-05** | **State** | **Game flow management** |

---

## Code Structure

```
src/
├── MainWithGameState.java         # ✨ NEW: State Pattern entry point
├── Main.java                       # Original entry point (still works)
│
└── gamestate/                      # ✨ NEW: Game state package
    ├── GameState.java              # State interface
    ├── GameStateContext.java       # State context manager
    ├── MenuState.java              # Menu state
    ├── SimplePlayingState.java     # Playing state (simplified)
    ├── BattleState.java            # Battle state
    ├── VictoryState.java           # Victory state
    └── DefeatState.java            # Defeat state
```

---

## Testing the Game State Pattern

Run the new main class:
```bash
java -cp bin/12-05-game-state-pattern MainWithGameState
```

You'll see state transitions logged:
```
[MenuState] Entering main menu...
[MenuState] Exiting main menu...
[SimplePlayingState] Would start dungeon exploration here...
[SimplePlayingState] Exiting dungeon exploration...
[BattleState] Entering boss battle...
[BattleState] Exiting boss battle...
[VictoryState] Player won!
```

---

## Next Steps

### Week 13: Template Method and Facade Patterns

Now that we have comprehensive state management at both micro (boss AI) and macro (game flow) levels, Week 13 will add:

1. **Template Method Pattern**: Define algorithm skeleton for enemies/screens
2. **Facade Pattern**: Simplify complex subsystem interactions

---

## Design Patterns Summary (Week 12)

Week 12 demonstrated **State Pattern** at two levels:

| Branch | Pattern Application | Problem Solved |
|--------|-------------------|----------------|
| 12-03 | ANTI-PATTERN | Hardcoded boss AI with if/else chains |
| 12-04 | State Pattern (Boss AI) | Clean, extensible boss behaviors |
| 12-05 | State Pattern (Game Flow) | Clean, extensible game phase management |

**Key Insight**: State Pattern is versatile - use it whenever behavior changes based on state, whether that's AI behavior, game phases, UI screens, or any state-dependent logic.

---

## Related Branches

- **Previous**: `12-04-state-pattern` - State Pattern for boss AI
- **Next**: Week 13 branches - Template Method and Facade patterns
- **Original**: `main` - Baseline without game state pattern

---

## References

- **Design Pattern**: State Pattern (GoF)
- **Related**: Strategy Pattern (similar structure, different intent)
- **Principles**: Open/Closed Principle, Single Responsibility Principle
