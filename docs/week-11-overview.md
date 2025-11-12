# Week 11 Overview: Command Pattern and Observer Pattern

**Focus:** Behavioral Design Patterns for Flexible Input and Event-Driven Systems

---

## Week 11 Learning Objectives

By the end of Week 11, students will understand:

1. **Command Pattern** - Encapsulating requests as objects for flexible input handling
2. **Observer Pattern** - Decoupling event publishers from listeners
3. **Anti-Pattern Recognition** - Identifying tight coupling and hardcoded dependencies
4. **Event-Driven Architecture** - Building scalable systems with publish-subscribe pattern
5. **Design Principles** - Applying Single Responsibility, Open/Closed, and Dependency Inversion

---

## Branch Progression

Week 11 follows an **anti-pattern → solution** teaching approach:

```
11-01-hardcoded-input (ANTI-PATTERN)
    ↓ demonstrates problems
    ↓ motivates need for flexibility
11-02-with-command (SOLUTION)
    ↓ implements Command Pattern
    ↓ enables flexible key bindings
    ↓
11-03-tight-coupling-events (ANTI-PATTERN)
    ↓ demonstrates tight coupling
    ↓ motivates need for decoupling
11-04-observer-pattern (SOLUTION)
    ↓ implements Observer Pattern
    ↓ enables event-driven architecture
```

---

## Branch Summaries

### Branch 11-01: Hardcoded Input (ANTI-PATTERN)

**File:** [docs/11-01-problem.md](11-01-problem.md)

**Problem Demonstrated:**
- Hardcoded WASD key bindings in giant if-else chain
- Tight coupling to Player class
- Impossible to remap keys without modifying code
- Cannot implement undo, macros, or key rebinding

**New Features:**
- Player-controlled movement with WASD + Enter
- Collision detection (Player vs Coins/Obstacles, Obstacle vs Obstacle)
- Double buffering rendering (reduced flickering)
- HUD integrated with buffering
- Hidden cursor during gameplay

**Key Code Smell:**
```java
// ❌ ANTI-PATTERN: Hardcoded keys in InputHandler
if (input.equals("w")) {
    player.moveUp();
} else if (input.equals("s")) {
    player.moveDown();
} // ... more hardcoded bindings
```

**Teaching Point:** Shows WHY we need Command Pattern

---

### Branch 11-02: Command Pattern (SOLUTION)

**File:** [docs/11-02-solution.md](11-02-solution.md)

**Solution Implemented:**
- Command Pattern with Command interface
- Concrete commands (MoveUpCommand, MoveDownCommand, etc.)
- InputHandler maps keys to Command objects
- Configuration file for key bindings (config/keybindings.txt)
- Undo functionality (press 'u' to undo last move)
- Macro recording support (press 'm' to start/stop, 'p' to play)

**Benefits:**
```java
// ✅ SOLUTION: Flexible key bindings
inputHandler.bindKey("w", new MoveUpCommand(player));
inputHandler.bindKey("↑", new MoveUpCommand(player));  // Alternative binding!

// ✅ Easy to add new commands
inputHandler.bindKey("t", new TeleportCommand(player));  // No modification needed!

// ✅ Undo support
Command lastCommand = inputHandler.getLastCommand();
lastCommand.undo();  // Restore previous state
```

**Design Principles Applied:**
- **Single Responsibility** - Each command has one job
- **Open/Closed** - Open for extension, closed for modification
- **Dependency Inversion** - Depend on Command interface, not concrete commands

---

### Branch 11-03: Tight Coupling Events (ANTI-PATTERN)

**File:** [docs/11-03-problem.md](11-03-problem.md)

**Problem Demonstrated:**
- Tight coupling between systems (Player, SoundSystem, AchievementSystem, HUD)
- Object drilling (passing dependencies through constructors)
- Constructor explosion (too many parameters)
- Manual event notification (Player must call all systems)

**New Features:**
- **SoundSystem** - Plays console bell sounds (\007) for game events
- **AchievementSystem** - Tracks achievements (First Blood, Coin Collector, Survivor)
- **HUD Enhancement** - Displays unlocked achievements

**Key Code Smells:**

**1. Object Drilling:**
```java
// ❌ ANTI-PATTERN: Complex dependency chain
this.soundSystem = new SoundSystem();
this.achievementSystem = new AchievementSystem(soundSystem);  // Needs SoundSystem!
this.hud = new HUD(achievementSystem);  // Needs AchievementSystem!
this.player = new Player(10, 10, soundSystem, achievementSystem);  // Needs both!
```

**2. Constructor Explosion:**
```java
// ❌ ANTI-PATTERN: Player constructor with dependencies
public Player(int x, int y, SoundSystem soundSystem, AchievementSystem achievementSystem)
```

**3. Manual Event Notification:**
```java
// ❌ ANTI-PATTERN: Player must notify all systems
public void takeDamage(int amount) {
    health -= amount;
    soundSystem.playHurtSound();  // Manual call
    achievementSystem.onDamageTaken(amount);  // Manual call
    // Want ParticleSystem? Must modify this method!
}
```

**Teaching Point:** Shows WHY we need Observer Pattern

---

### Branch 11-04: Observer Pattern (SOLUTION)

**File:** [docs/11-04-solution.md](11-04-solution.md)

**Solution Implemented:**
- **EventBus** - Singleton central event dispatcher
- **GameEvent** - Base class for all events
- **GameEventListener** - Interface for all observers
- **Event Classes** - DamageTakenEvent, CoinCollectedEvent, AchievementUnlockedEvent, GameTimeEvent
- **Refactored Systems** - All systems implement GameEventListener

**Benefits:**

**1. Loose Coupling:**
```java
// ✅ SOLUTION: Simple constructor - no dependencies!
public Player(int x, int y) {
    // ...
}

// ✅ Just publish event - observers react automatically
public void takeDamage(int amount) {
    health -= amount;
    EventBus.getInstance().publish(new DamageTakenEvent(amount, health));
}
```

**2. Easy to Extend:**
```java
// ✅ Want ParticleSystem? Just register it!
public class ParticleSystem implements GameEventListener {
    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof DamageTakenEvent) {
            playHitEffect();
        }
    }
}

// In GameLogic:
ParticleSystem particles = new ParticleSystem();
EventBus.getInstance().subscribe(particles);  // Done! No other changes needed!
```

**3. Easy to Test:**
```java
// ✅ Test Player without mocking systems
Player player = new Player(10, 10);  // Simple!
player.takeDamage(10);
// Verify DamageTakenEvent was published
```

**Design Principles Applied:**
- **Single Responsibility** - Player only manages state, EventBus handles notification
- **Open/Closed** - Add observers without modifying publishers
- **Dependency Inversion** - Depend on GameEventListener interface

---

## Comparison Tables

### Input Handling Evolution (11-01 → 11-02)

| Aspect | 11-01 (Hardcoded) | 11-02 (Command Pattern) |
|--------|-------------------|-------------------------|
| **Key Bindings** | Hardcoded in if-else | Configurable file |
| **Adding Keys** | Modify InputHandler | Add to config file |
| **Alternative Keys** | Not possible | Easy (multiple keys → same command) |
| **Undo Support** | Not possible | Built-in |
| **Macro Recording** | Not possible | Built-in |
| **Extensibility** | Hard (modify code) | Easy (implement Command) |
| **Testing** | Hard (coupled to Player) | Easy (test commands independently) |

### Event System Evolution (11-03 → 11-04)

| Aspect | 11-03 (Tight Coupling) | 11-04 (Observer Pattern) |
|--------|------------------------|--------------------------|
| **Player Constructor** | `Player(x, y, sound, achievement)` | `Player(x, y)` |
| **Dependencies** | Player knows about all systems | Player knows only EventBus |
| **Adding New System** | Must modify Player code | Just register with EventBus |
| **System Communication** | Direct method calls | Event publishing |
| **Initialization Order** | Must create in dependency order | Create in any order |
| **Testing Complexity** | Must mock all systems | Just verify events |
| **Coupling Level** | Tight (direct dependencies) | Loose (via events) |
| **Extensibility** | Hard (modify existing code) | Easy (add observers) |

---

## Design Patterns Demonstrated

By the end of Week 11, the project demonstrates **five design patterns:**

### 1. Singleton Pattern (Week 9)
- **GameManager** - Global game state
- **EventBus** (Week 11) - Central event dispatcher

### 2. Factory Pattern (Week 9)
- **NPCFactory** - Creates different NPC types

### 3. Object Pool Pattern (Week 10)
- **NPCPool** - Reuses NPC instances to eliminate GC pressure

### 4. Command Pattern (Week 11)
- **Command Interface** - Encapsulates actions as objects
- **Concrete Commands** - MoveUpCommand, MoveDownCommand, etc.
- **InputHandler** - Maps keys to commands

### 5. Observer Pattern (Week 11)
- **GameEvent** - Base event class
- **GameEventListener** - Observer interface
- **EventBus** - Subject that notifies observers
- **Systems** - SoundSystem, AchievementSystem, HUD as observers

---

## Key Concepts

### Command Pattern Key Concepts

1. **Encapsulation** - Actions are objects
2. **Decoupling** - Invoker doesn't know about receiver
3. **Undo/Redo** - Store command history
4. **Macro Recording** - Sequence of commands
5. **Configuration** - Runtime key binding changes

### Observer Pattern Key Concepts

1. **Publish-Subscribe** - Publishers don't know subscribers
2. **Event-Driven** - React to state changes automatically
3. **One-to-Many** - One event, many listeners
4. **Loose Coupling** - Systems independent of each other
5. **Extensibility** - Add observers without modifying code

---

## Code Evolution Highlights

### Player Class Evolution

**Week 9-10:**
```java
public class Player implements Entity {
    public Player(int x, int y) {
        // Simple constructor
    }
}
```

**Week 11-03 (Regression for teaching):**
```java
public class Player implements Entity {
    private SoundSystem soundSystem;  // ❌ Tight coupling!
    private AchievementSystem achievementSystem;  // ❌ Tight coupling!

    public Player(int x, int y, SoundSystem soundSystem, AchievementSystem achievementSystem) {
        // ❌ Constructor explosion
    }

    public void takeDamage(int amount) {
        soundSystem.playHurtSound();  // ❌ Manual notification
        achievementSystem.onDamageTaken(amount);  // ❌ Manual notification
    }
}
```

**Week 11-04 (Final clean design):**
```java
public class Player implements Entity {
    public Player(int x, int y) {
        // ✅ Back to simple constructor!
    }

    public void takeDamage(int amount) {
        health -= amount;
        // ✅ Just publish event - clean!
        EventBus.getInstance().publish(new DamageTakenEvent(amount, health));
    }
}
```

### InputHandler Evolution

**Week 11-01:**
```java
public class InputHandler {
    public void handleInput(String input) {
        // ❌ Giant if-else chain
        if (input.equals("w")) player.moveUp();
        else if (input.equals("s")) player.moveDown();
        // ... hardcoded bindings
    }
}
```

**Week 11-02:**
```java
public class InputHandler {
    private Map<String, Command> keyBindings;
    private Stack<Command> commandHistory;

    public void bindKey(String key, Command command) {
        // ✅ Flexible key binding
        keyBindings.put(key, command);
    }

    public void handleInput(String input) {
        Command command = keyBindings.get(input);
        if (command != null) {
            command.execute();
            commandHistory.push(command);  // ✅ Undo support
        }
    }
}
```

---

## Running the Branches

### Branch 11-01 (Hardcoded Input)
```bash
cd bin/11-01-hardcoded-input
java Main
```
- WASD + Enter to move (Windows console limitation)
- Player collects coins, avoids wolves
- Notice: Cannot remap keys, no undo

### Branch 11-02 (Command Pattern)
```bash
cd bin/11-02-with-command
java Main
```
- WASD + Enter to move
- **U** to undo last move
- **M** to start/stop macro recording
- **P** to play recorded macro
- Edit `config/keybindings.txt` to customize keys

### Branch 11-03 (Tight Coupling)
```bash
cd bin/11-03-tight-coupling-events
java Main
```
- All features from 11-02 (Command Pattern kept)
- Sound effects (console beep) when damaged/collecting coins
- Achievement unlocks (First Blood, Coin Collector, Survivor)
- HUD displays achievements
- Notice: Code is tightly coupled but features work

### Branch 11-04 (Observer Pattern)
```bash
cd bin/11-04-observer-pattern
java Main
```
- All features from 11-03
- Identical functionality but clean architecture
- Systems completely decoupled
- Easy to extend with new observers

---

## Teaching Approach

Week 11 uses the **anti-pattern → solution** pedagogical approach:

### Step 1: Show the Problem (Anti-Pattern)
Students first see the anti-pattern with working features. This demonstrates:
- **Why** the pattern is needed (problems are concrete and visible)
- **What** happens without proper design (maintainability issues)
- **When** to apply the pattern (recognition of code smells)

### Step 2: Show the Solution (Design Pattern)
Students then see the pattern implementation. This demonstrates:
- **How** to implement the pattern
- **Benefits** of the pattern (comparison with anti-pattern)
- **Principles** behind the pattern

### Step 3: Compare and Contrast
Students can run both branches and see:
- **Same functionality** - Features work identically
- **Different architecture** - Clean vs messy code
- **Different maintainability** - Easy vs hard to extend

---

## Key Takeaways

### Command Pattern Takeaways

1. **Flexibility** - Runtime configuration of key bindings
2. **Decoupling** - InputHandler doesn't know about Player
3. **Undo/Redo** - Command history enables undo
4. **Extensibility** - Add commands without modifying InputHandler
5. **Testability** - Test commands independently

### Observer Pattern Takeaways

1. **Decoupling** - Publishers don't know subscribers
2. **Scalability** - Add unlimited observers
3. **Maintainability** - Simple constructors, clear responsibilities
4. **Extensibility** - Add systems without modifying publishers
5. **Testability** - Test components independently

---

## Discussion Questions

### Command Pattern Questions

1. What are the advantages of using Command Pattern over hardcoded input?
2. How does Command Pattern enable undo functionality?
3. Why is it easier to add new commands in 11-02 than new keys in 11-01?
4. How could you implement redo functionality?
5. What other use cases can benefit from Command Pattern? (e.g., transaction systems, job queues)

### Observer Pattern Questions

1. What are the advantages of using Observer Pattern over tight coupling?
2. How does EventBus decouple publishers from subscribers?
3. Why is it easier to add new systems in 11-04 than in 11-03?
4. What are the trade-offs of event-driven architecture? (e.g., harder to trace flow)
5. What other use cases can benefit from Observer Pattern? (e.g., GUI events, notifications)

---

## Extensions and Exercises

### Command Pattern Extensions

1. **Redo Functionality** - Implement redo stack alongside undo
2. **Command Logging** - Log all commands to file for replay
3. **Composite Commands** - Macro commands that execute multiple commands
4. **Command Validation** - Check if command can execute before executing
5. **Command Serialization** - Save/load command history

### Observer Pattern Extensions

1. **Event Filtering** - Listeners subscribe to specific event types only
2. **Event Priority** - Listeners react in priority order
3. **Asynchronous Events** - Events processed in separate thread
4. **Event History** - Store event log for debugging
5. **Conditional Events** - Events only published if condition met

---

## Summary

Week 11 demonstrates two essential **behavioral design patterns** that work together to create a flexible, extensible, and maintainable game architecture:

- **Command Pattern** enables flexible input handling with undo support
- **Observer Pattern** enables event-driven systems with loose coupling

Combined with previous patterns (Singleton, Factory, Object Pool), the project now showcases **five design patterns** working harmoniously to solve real-world game development challenges.

**The teaching approach of showing anti-patterns first makes the value of design patterns concrete and memorable for students.**
