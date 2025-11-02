# Week 12: Complexity & State Management

## 🎯 Week Goals

### Primary Learning Objectives
1. **Understand Strategy Pattern**
   - Algorithm encapsulation
   - Runtime behavior swapping
   - Family of algorithms
   - Difficulty scaling systems

2. **Understand State Pattern**
   - State-driven behavior
   - State machine implementation
   - Eliminating conditional complexity
   - Clean state transitions

### Secondary Learning Objectives
- Dynamic difficulty adjustment
- Game state machines (FSM)
- Turn-based battle systems
- Boss encounter mechanics
- Save/load game state (JSON persistence)

---

## 🎮 Game State at End of Week 12

### 🏆 COMPLETE GAME!

### What Works
✅ All Week 9-11 features (loop, singleton, spawning, pooling, input, events)
✅ **NEW**: Dynamic difficulty scaling (spawn rate adapts to score)
✅ **NEW**: Multiple difficulty strategies (Easy, Medium, Hard)
✅ **NEW**: Full game state machine:
   - Main Menu State
   - Playing State  
   - Boss Battle State (turn-based combat!)
   - Victory/Game Over States
✅ **NEW**: Boss encounter at dungeon exit
✅ **NEW**: Turn-based battle system (separate from exploration)
✅ **NEW**: Save/load game (JSON persistence)
✅ **NEW**: Complete game loop (menu → play → battle → result)

### Nothing Missing!
🎉 **This is the complete, playable game!**

---

## 📚 Patterns Covered

### 1. Strategy Pattern

**Category**: Behavioral Pattern
**GoF Classification**: Object Behavioral

#### The Problem
```java
// ❌ Hard-coded difficulty algorithms
public class PlayingState {
    private Difficulty level = EASY;
    
    public void spawnObstacles(float delta) {
        switch(level) {
            case EASY:
                // Spawn 1 obstacle every 5 seconds
                if (timer > 5.0f) {
                    spawn(randomObstacle());
                    timer = 0;
                }
                break;
            case MEDIUM:
                // Spawn 2 obstacles every 3 seconds
                // ... 30 lines of code
                break;
            case HARD:
                // Spawn 3 obstacles every 2 seconds
                // ... 40 lines of code
                break;
            case NIGHTMARE:
                // ... 50 more lines
                break;
        }
    }
}
```

**Pain Points**:
- PlayingState = god class (all difficulty logic inside)
- Bloated method (150+ lines for all cases)
- Add difficulty = modify PlayingState
- Cannot test individual difficulty logic
- Violates SRP (PlayingState does too much)

#### The Solution
```java
// ✅ Strategy pattern
public interface DifficultyStrategy {
    void spawnObstacles(float delta, WorldController world);
}

public class EasyDifficulty implements DifficultyStrategy {
    public void spawnObstacles(float delta, WorldController world) {
        // Only Easy logic here (20 lines)
    }
}

// PlayingState becomes clean
public class PlayingState {
    private DifficultyStrategy strategy;
    
    public void update(float delta) {
        strategy.spawnObstacles(delta, worldController);
        // PlayingState doesn't know HOW spawning works!
    }
    
    public void setDifficulty(DifficultyStrategy newStrategy) {
        this.strategy = newStrategy;
        // Change difficulty at runtime!
    }
}
```

#### Why This Matters
- **Encapsulation**: Each algorithm isolated in its own class
- **Testability**: Test each difficulty independently
- **Extensibility**: Add Nightmare difficulty = new class only
- **Maintainability**: PlayingState stays clean (30 lines vs 150+)
- **Runtime flexibility**: Change strategy on the fly

#### Real-World Examples
- Payment methods (CreditCard, PayPal, Bitcoin)
- Compression algorithms (ZIP, RAR, 7Z)
- Routing algorithms (shortest, fastest, scenic)
- AI behaviors (aggressive, defensive, balanced)

---

### 2. State Pattern

**Category**: Behavioral Pattern
**GoF Classification**: Object Behavioral

#### The Problem
```java
// ❌ God switch for game states
public class GameManager {
    private enum GameState { MENU, PLAYING, BATTLE, GAME_OVER }
    private GameState currentState = MENU;
    
    public void update(float delta) {
        switch(currentState) {
            case MENU:
                // 50 lines: button handling, menu navigation
                if (startButton.clicked()) {
                    currentState = PLAYING;
                    // Transition logic scattered
                }
                break;
            case PLAYING:
                // 80 lines: gameplay, collision, spawning
                if (player.reachedExit()) {
                    currentState = BATTLE;
                    // More transition logic
                }
                break;
            case BATTLE:
                // 100 lines: turn-based combat
                if (boss.isDead()) {
                    currentState = GAME_OVER;
                }
                break;
            case GAME_OVER:
                // 40 lines: show results, restart
                break;
        }
    }
    
    public void draw() {
        // Another 150+ line switch!
    }
    
    public void handleInput() {
        // Another 100+ line switch!
    }
}
```

**Pain Points**:
- GameManager = super god class (300+ lines)
- Three giant switches (update, draw, input)
- Add state = modify all three switches
- Transition logic scattered throughout
- Hard to understand control flow
- Hard to test individual states

#### The Solution
```java
// ✅ State pattern
public interface GameState {
    void update(float delta);
    void draw();
    void handleInput();
    void onEnter();  // Called when entering state
    void onExit();   // Called when leaving state
}

public class PlayingState implements GameState {
    public void update(float delta) {
        // Only Playing logic (30 lines)
        if (player.reachedExit()) {
            stateManager.setState(new BattleState());
        }
    }
    // Clean, focused class
}

// GameManager becomes minimal
public class GameManager {
    private GameState currentState;
    
    public void update(float delta) {
        currentState.update(delta);  // That's it!
    }
    
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.onExit();
        }
        currentState = newState;
        currentState.onEnter();
    }
}
```

#### Why This Matters
- **Organization**: Each state in its own file
- **SRP**: Each state class has one responsibility
- **Encapsulation**: State transitions managed by states
- **Testability**: Test each state independently
- **Extensibility**: Add PausedState = new class only
- **Clarity**: Control flow obvious (state diagram)

#### Real-World Examples
- TCP connection states (Closed, Listen, Established, etc)
- Document workflow (Draft, Review, Published)
- Elevator states (Idle, Moving, DoorsOpen)
- Game AI FSM (Patrol, Chase, Attack, Flee)

---

## 🌳 Branch Roadmap

### Branch 12-01: hardcoded-difficulty
**Type**: Problem demonstration
**Purpose**: Show bloated class with switch-case

**What You'll See**:
```java
// 150+ line method
public void spawnObstacles(float delta) {
    switch(currentDifficulty) {
        case EASY:    // 30 lines
        case MEDIUM:  // 40 lines
        case HARD:    // 50 lines
        case NIGHTMARE: // 30 lines
    }
}
```

**Pain Points**:
- PlayingState.java = 250+ lines
- Add difficulty = modify this bloated file
- Difficult to test individual difficulties
- Hard to read and maintain

**Demonstration**:
- Task: "Add Insane difficulty"
- Observe: Must add another 40-line case in switch
- Risk: Accidentally break existing difficulties

---

### Branch 12-02: with-strategy
**Type**: Solution
**Purpose**: Show Strategy pattern for difficulty

**What You'll See**:
```java
// Strategy interface
public interface DifficultyStrategy {
    void spawnObstacles(float delta, WorldController world);
}

// Clean strategies
public class EasyDifficulty implements DifficultyStrategy {
    // Only Easy logic: 25 lines
}

public class HardDifficulty implements DifficultyStrategy {
    // Only Hard logic: 35 lines
}

// PlayingState becomes clean
public class PlayingState {
    private DifficultyStrategy strategy;
    
    public void update(float delta) {
        strategy.spawnObstacles(delta, world);
        // PlayingState: 40 lines total!
    }
}
```

**Benefits**:
- Each difficulty = separate, testable class
- PlayingState stays clean (40 lines vs 250+)
- Add difficulty = new class only
- Can change difficulty at runtime

---

### Branch 12-03: god-switch-states
**Type**: Problem demonstration
**Purpose**: Show FSM with giant switches

**What You'll See**:
```java
public class GameManager {
    public void update(float delta) {
        switch(state) {
            case MENU:       // 50 lines
            case PLAYING:    // 80 lines
            case BATTLE:     // 100 lines
            case GAME_OVER:  // 40 lines
        }
    }
    
    public void draw() {
        switch(state) {
            // Another 150 lines!
        }
    }
    
    public void handleInput() {
        switch(state) {
            // Another 100 lines!
        }
    }
}
```

**Pain Points**:
- GameManager = 500+ lines
- Three giant switch statements
- Logic for Menu scattered across three methods
- Add PausedState? Modify 3 switches
- Transition logic unclear

---

### Branch 12-04: with-state
**Type**: Solution
**Purpose**: Show State pattern for FSM

**What You'll See**:
```java
// State interface
public interface GameState {
    void update(float delta);
    void draw();
    void handleInput();
}

// Each state is focused
public class MainMenuState implements GameState {
    // Only menu logic: 50 lines
}

public class PlayingState implements GameState {
    // Only gameplay logic: 80 lines
}

public class BattleState implements GameState {
    // Only battle logic: 120 lines
    // Turn-based combat separate from exploration!
}

// GameManager becomes minimal
public class GameManager {
    private GameState currentState;
    
    public void update(float delta) {
        currentState.update(delta);  // Clean!
    }
}
```

**Benefits**:
- Each state = separate, focused file
- GameManager minimal (50 lines)
- Add state = new class only
- Clear state transitions
- Easy to test each state

---

### Branch 12-analysis
**Type**: Comparison & final integration

**Contents**:
1. **Strategy Pattern Analysis**
   - LOC comparison (switch vs strategies)
   - Testability improvements
   - Extensibility demonstration
   - Performance considerations

2. **State Pattern Analysis**
   - Complexity reduction metrics
   - Code organization benefits
   - State diagram visualization
   - Transition clarity

3. **Full Game Integration**
   - How all 8 patterns work together
   - Data flow through system
   - Performance metrics (final)
   - Complete architecture diagram

4. **Pattern Synergies**
   - Singleton → manages everything
   - Factory+Pool → spawning
   - Command+Observer → interactivity
   - Strategy+State → behavior/state
   - How they complement each other

5. **Lessons Learned**
   - When to use which pattern
   - Trade-offs summary
   - Anti-patterns to avoid
   - Next steps for learning

---

## 📊 Success Metrics

### For Students
By end of Week 12, students should:
- [ ] Explain WHY Strategy pattern needed
- [ ] Implement pluggable algorithms
- [ ] Explain WHY State pattern needed
- [ ] Implement FSM with State pattern
- [ ] Understand all 8 patterns covered
- [ ] Explain how patterns work together
- [ ] Discuss trade-offs of each pattern
- [ ] Know when to use (and not use) each pattern

### For Code
- [ ] Difficulty scales dynamically
- [ ] Complete game loop (menu → play → battle → result)
- [ ] Boss battle works (turn-based)
- [ ] Save/load works (JSON)
- [ ] All 8 patterns implemented correctly
- [ ] No god classes (all < 200 lines)
- [ ] All tests passing
- [ ] Playable from start to finish!

---

## 🧪 Demonstration Scenarios

### Demo 1: Difficulty Switch Bloat
**Setup**: Branch 12-01
**Action**: Show 150+ line spawnObstacles method
**Task**: "Add Insane difficulty"
**Observe**: Must add 40 more lines to switch
**Learning**: "Switch grows unbounded!"

### Demo 2: Strategy Elegance
**Setup**: Branch 12-02
**Action**: Same task
**Observe**: Create InsaneDifficulty.java (30 lines), done!
**Learning**: "Extension without modification!"

### Demo 3: State Switch Hell
**Setup**: Branch 12-03
**Action**: Show three 100+ line switch methods
**Task**: "Add PausedState"
**Observe**: Must modify update(), draw(), handleInput()
**Learning**: "Three places to modify for one feature!"

### Demo 4: State Pattern Clean
**Setup**: Branch 12-04
**Action**: Same task
**Observe**: Create PausedState.java, register it, done!
**Learning**: "State = one file, one responsibility!"

### Demo 5: Complete Game
**Setup**: Branch 12-04
**Action**: Play from menu to boss battle to victory
**Observe**: All 8 patterns working in harmony
**Learning**: "Patterns make complexity manageable!"

---

## 🎓 Teaching Notes

### Key Concepts to Emphasize

#### 1. Strategy vs State
Students often confuse these:

**Strategy**:
- Focus: HOW (algorithm selection)
- Context: Usually keeps same strategy
- Example: Difficulty stays same during gameplay

**State**:
- Focus: WHAT (object's condition)
- Context: Frequently changes state
- Example: Menu → Playing → Battle (constant transitions)

#### 2. State Machine Complexity
```
Without State Pattern:
    Complexity = States × Methods
    4 states × 3 methods = 12 switch cases

With State Pattern:
    Complexity = States + Methods  
    4 state classes + 1 manager = 5 classes
    
Even better: Each state class is focused!
```

#### 3. Boss Battle Design
Turn-based combat vs real-time exploration:
- PlayingState: Real-time, WASD movement
- BattleState: Turn-based, menu selection
- Completely different mechanics!
- State pattern makes this clean

#### 4. JSON Persistence
Simple manual JSON for educational purposes:
```java
// Save
String json = "{ \"score\": " + score + ", \"health\": " + health + " }";
Files.writeString(path, json);

// Load
String json = Files.readString(path);
// Manual parsing (no libraries!)
```

---

### Common Student Questions

**Q**: "Strategy pattern = if-else statement dengan extra steps?"
**A**: For 2 strategies, maybe. For 5+ strategies, it prevents code bloat. Plus: OCP, testability, runtime swapping.

**Q**: "State pattern = Strategy pattern?"
**A**: Structure similar, intent different:
- Strategy = swap algorithm
- State = object changes behavior based on internal state

**Q**: "Bagaimana choose between Strategy and State?"
**A**: Ask: "Is this about ALGORITHM or CONDITION?"
- Algorithm → Strategy
- Condition → State

**Q**: "Boss battle = separate game?"
**A**: Exactly! It's a mini turn-based game inside our real-time game. State pattern makes this natural.

**Q**: "8 patterns = over-engineering untuk game kecil?"
**A**: Untuk production game kecil: maybe. Untuk learning: NO! Ini investasi untuk understand patterns deeply.

---

## 📦 Deliverables

### Student Deliverables
- [ ] Complete playable game
- [ ] All 8 patterns implemented
- [ ] Unit tests for all patterns
- [ ] Save/load functionality
- [ ] Final reflection document:
  - What I learned
  - Which patterns most useful
  - When to use / not use
  - Trade-offs experienced

### Teaching Material Deliverables
- [ ] All 5 branches implemented
- [ ] SCENARIO/GUIDELINE/PROMPT for each
- [ ] Complete game demo video
- [ ] Architecture diagram (all patterns)
- [ ] Final analysis document
- [ ] Pattern decision flowchart
- [ ] Common mistakes FAQ

---

## ⏱️ Time Allocation

### Lecture (2 hours)
- 20 min: Strategy pattern explanation
- 10 min: Demo problem (12-01)
- 10 min: Demo solution (12-02)
- 20 min: State pattern explanation
- 15 min: Demo FSM problem (12-03)
- 15 min: Demo State solution (12-04)
- 20 min: Integration & synergies
- 10 min: Lessons learned & next steps

### Lab (4 hours) - Extended for final project
- 2 hours: Implement Strategy pattern
- 2 hours: Implement State pattern + FSM
- Boss battle integration
- Final testing

### Homework (8 hours) - Final project
- Complete boss battle implementation
- Implement save/load
- Polish game (balance, UI)
- Write final reflection
- Prepare presentation

---

## 🔗 Connections to All Weeks

### Week 9 Foundation
- Singleton GameManager coordinates everything
- Game loop runs all states

### Week 10 World Building
- Factory creates obstacles
- Pool manages obstacle reuse
- Used by difficulty Strategies

### Week 11 Interactivity
- Commands handle input in all States
- Observers react to state transitions

### Week 12 Integration
- Strategy selects spawning algorithm
- State manages game flow
- All patterns work together!

---

## 🎮 Boss Battle Mechanics

### Boss Encounter
- Located at dungeon exit (D)
- Player reaches D → transition to BattleState
- Turn-based combat (separate from real-time gameplay)

### Battle System
```
Player Turn:
  - Attack (deal damage)
  - Defend (reduce next damage)
  - Use Item (heal, buff)
  
Boss Turn:
  - Attack (deal damage)
  - Special Move (high damage, cooldown)
  
Win: Boss HP = 0 → VictoryState
Lose: Player HP = 0 → GameOverState
```

### Why Turn-Based?
- Demonstrates State pattern (different behavior)
- Easier to implement (no real-time collision)
- Classic RPG feel (contrast to exploration)
- Educational: two different game modes

---

## 📚 Additional Resources

### Recommended Reading
- "Design Patterns" GoF - Strategy & State chapters
- "Game Programming Patterns" - State & Bytecode chapters
- "Refactoring" by Martin Fowler - Replace Conditional with Polymorphism
- FSM tutorials (game AI)

### Real Implementations
- Unity Animator (FSM with State pattern)
- Unreal Behavior Trees (Strategy-like)
- AI FSMs in game engines
- Spring State Machine

---

## 🏆 Final Project Assessment

### Grading Criteria (Example)
- **Functionality (40%)**
  - Game playable start to finish
  - All features working
  - No critical bugs

- **Pattern Implementation (40%)**
  - All 8 patterns correctly used
  - Trade-offs understood
  - Clean code (no god classes)

- **Understanding (20%)**
  - Can explain WHY patterns used
  - Can discuss alternatives
  - Final reflection thoughtful

---

## ✅ Week 12 Checklist

Course completion checklist:
- [ ] All 8 patterns implemented
- [ ] Complete game playable
- [ ] Boss battle works
- [ ] Save/load works
- [ ] All tests passing
- [ ] No god classes (< 200 lines)
- [ ] Students can explain all patterns
- [ ] Students understand synergies
- [ ] Final reflection complete
- [ ] Ready for portfolio! 🎉

---

## 🎓 Course Conclusion

### What Students Learned
1. **Technical Skills**
   - 8 design patterns
   - Game development basics
   - Performance optimization
   - Event-driven architecture

2. **Soft Skills**
   - Problem decomposition
   - Trade-off analysis
   - Code organization
   - Testing discipline

3. **Mindset**
   - Patterns as tools (not rules)
   - When to use / not use
   - Balance (simplicity vs flexibility)
   - Continuous learning

### Next Steps
- Build own game with patterns
- Study more patterns (MVC, MVP, MVVM)
- Learn game engine (Unity, Godot)
- Contribute to open source
- Portfolio project

---

**Congratulations! 🎊 Complete game + 8 patterns mastered!** 🚀
