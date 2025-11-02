# Week 11: Interactivity & Event Systems

## 🎯 Week Goals

### Primary Learning Objectives
1. **Understand Command Pattern**
   - Encapsulation of actions as objects
   - Decoupling invoker from receiver
   - Undo/redo capability foundation
   - Remappable controls

2. **Understand Observer Pattern**
   - Event-driven architecture
   - One-to-many communication
   - Loose coupling between components
   - Publish-subscribe model

### Secondary Learning Objectives
- Real-time input handling
- Event propagation systems
- UI updates via events
- Audio system integration
- Achievement system basics

---

## 🎮 Game State at End of Week 11

### 🌟 MAJOR MILESTONE: Player Control Introduced!

### What Works
✅ All Week 9-10 features (loop, singleton, spawning, pooling)
✅ **NEW**: ⭐ WASD keyboard input (real-time)
✅ **NEW**: ⭐ Player-controlled character
✅ **NEW**: Player collision with obstacles (takes damage)
✅ **NEW**: Player collision with coins (collects, score++)
✅ **NEW**: Event system (damage → HUD update, sound, etc)
✅ **NEW**: Remappable keys
✅ **NEW**: Multiple UI elements reacting to same event

### What Still Doesn't Work
❌ No difficulty scaling (all spawns same rate)
❌ No state machine (no menu, no game over screen)
❌ No boss battle
❌ No save/load

**Why**: Difficulty and states require Strategy and State patterns (Week 12).

---

## 📚 Patterns Covered

### 1. Command Pattern

**Category**: Behavioral Pattern
**GoF Classification**: Object Behavioral

#### The Problem
```java
// ❌ Hard-coded input handling
public void handleInput() {
    if (Gdx.input.isKeyPressed(Keys.W)) {
        player.jump();  // Tightly coupled!
    }
    if (Gdx.input.isKeyPressed(Keys.SPACE)) {
        player.attack();  // Cannot remap!
    }
}
```

**Pain Points**:
- InputHandler knows all about Player methods
- Cannot remap keys (W hardcoded to jump)
- Cannot implement undo/redo
- Cannot record/replay actions
- Tight coupling (if Player.jump signature changes, must update InputHandler)

#### The Solution
```java
// ✅ Commands as objects
public interface Command {
    void execute();
}

public class JumpCommand implements Command {
    private Player player;
    public void execute() { player.jump(); }
}

// Input mapping
Map<Integer, Command> keyMappings = new HashMap<>();
keyMappings.put(Keys.W, new JumpCommand(player));
// Remapping = just change the map!
```

#### Why This Matters
- **Flexibility**: Change key bindings at runtime
- **Undo/Redo**: Store command history, call undo()
- **Macros**: Combine multiple commands
- **Replay**: Record and playback input
- **Testing**: Mock commands easily
- **AI**: AI can issue same commands as player

#### Real-World Examples
- Photoshop undo system
- IDE refactoring commands
- Game replay systems (StarCraft, fighting games)
- UI button actions

---

### 2. Observer Pattern

**Category**: Behavioral Pattern
**GoF Classification**: Object Behavioral

#### The Problem
```java
// ❌ Tight coupling to all systems
public class Player {
    private HealthBar healthBar;
    private SoundSystem soundSystem;
    private AchievementSystem achievements;
    
    public void takeDamage(int amount) {
        this.health -= amount;
        healthBar.update(health);      // Player knows UI!
        soundSystem.playHurt();        // Player knows audio!
        achievements.checkDamaged();   // Player knows achievements!
        // Want to add particles? MODIFY PLAYER!
    }
}
```

**Pain Points**:
- Player depends on HealthBar, SoundSystem, Achievements, etc
- Adding new system (e.g., ParticleSystem) = modify Player
- Cannot test Player without all systems
- Violates Single Responsibility (Player shouldn't manage UI)
- Object drilling (pass all systems to Player constructor)

#### The Solution
```java
// ✅ Event-driven architecture
public class Player extends Subject {
    public void takeDamage(int amount) {
        this.health -= amount;
        notifyObservers(new DamageEvent(amount, this));
        // That's it! Player doesn't know who's listening
    }
}

// Systems register themselves
player.addObserver(new HealthBarObserver());
player.addObserver(new SoundObserver());
player.addObserver(new AchievementObserver());
// Add ParticleObserver? NO MODIFICATION TO PLAYER!
```

#### Why This Matters
- **Loose coupling**: Player doesn't know observers
- **Extensibility**: Add observers without modifying Subject
- **Open/Closed**: Subject closed for modification, open for extension
- **Testing**: Test Player without any observers
- **Modularity**: Each system is independent

#### Real-World Examples
- Java Swing event listeners
- Android event bus (EventBus, Otto)
- JavaScript DOM events
- MVC pattern (Model notifies View)
- Newsletter subscriptions

---

## 🌳 Branch Roadmap

### Branch 11-01: hardcoded-input
**Type**: Problem demonstration
**Purpose**: Show tight coupling in input handling

**What You'll See**:
```java
// InputHandler knows EVERYTHING
public void handleInput() {
    if (key == W) player.jump();
    if (key == A) player.moveLeft();
    if (key == S) player.crouch();
    if (key == D) player.moveRight();
    if (key == SPACE) player.attack();
    if (key == E) player.useItem();
    if (key == M) uiManager.toggleMenu();
    // 50+ lines of if-else!
}
```

**Pain Points**:
- InputHandler = god class (knows all actions)
- Remap keys? Modify InputHandler
- Add action? Modify InputHandler
- Test action? Need InputHandler mock

**Demonstration**:
- Task: "Make controls remappable in settings"
- Realize: Need to modify InputHandler for every possible mapping
- Complexity: Exponential with number of actions

---

### Branch 11-02: with-command
**Type**: Solution
**Purpose**: Show Command pattern implementation

**What You'll See**:
```java
// Clean mapping
Map<Key, Command> bindings = new HashMap<>();
bindings.put(Keys.W, new JumpCommand(player));
bindings.put(Keys.SPACE, new AttackCommand(player));

// Remapping = trivial
public void remapKey(Key oldKey, Key newKey) {
    Command cmd = bindings.remove(oldKey);
    bindings.put(newKey, cmd);
}

// Execution = simple
public void handleInput() {
    for (var entry : bindings.entrySet()) {
        if (isPressed(entry.getKey())) {
            entry.getValue().execute();
        }
    }
}
```

**Benefits**:
- InputHandler independent of Player
- Remapping = configuration change
- Add action = new Command class only
- Undo/redo = store command history

**Bonus Features** (if time):
- Command history for undo
- Macro commands (combo moves)
- Command queue (input buffering)

---

### Branch 11-03: tight-coupling-events
**Type**: Problem demonstration
**Purpose**: Show one-to-many communication problem

**What You'll See**:
```java
public class Player {
    // Player must know ALL systems!
    private HealthBarUI healthBar;
    private SoundSystem sound;
    private AchievementSystem achievements;
    private ParticleSystem particles;
    private CameraController camera;
    
    public Player(HealthBar h, SoundSystem s, 
                  Achievements a, Particles p, Camera c) {
        // Constructor nightmare!
    }
    
    public void takeDamage(int dmg) {
        health -= dmg;
        healthBar.update(health);
        sound.playHurt();
        achievements.onDamaged();
        particles.spawnBlood();
        camera.shake();
        // Want to add QuestSystem? MODIFY THIS!
    }
}
```

**Pain Points**:
- Player depends on 5+ systems
- Constructor has 5+ parameters (object drilling)
- Add system? Modify Player
- Test Player? Need 5+ mocks
- Violates SRP (Player manages everything)

---

### Branch 11-04: with-observer
**Type**: Solution
**Purpose**: Show Observer pattern implementation

**What You'll See**:
```java
// Player is Subject
public class Player implements Subject {
    private List<Observer> observers = new ArrayList<>();
    
    public void takeDamage(int dmg) {
        health -= dmg;
        notifyObservers(PlayerEvent.DAMAGE_TAKEN);
        // That's ALL Player needs to do!
    }
}

// Systems are Observers
public class HealthBarObserver implements Observer {
    public void onNotify(PlayerEvent event) {
        if (event == DAMAGE_TAKEN) {
            updateDisplay(player.getHealth());
        }
    }
}

// Registration (done externally)
player.addObserver(new HealthBarObserver());
player.addObserver(new SoundObserver());
// Add more? No problem, Player unchanged!
```

**Benefits**:
- Player independent of all systems
- Add system = register observer (no Player modification)
- Test Player = no observers needed
- SRP satisfied (Player manages self only)

**Event Types**:
- DAMAGE_TAKEN
- COIN_COLLECTED
- ENEMY_KILLED
- LEVEL_UP
- ITEM_USED

---

### Branch 11-analysis
**Type**: Comparison & metrics

**Contents**:
1. **Command Pattern Analysis**
   - Flexibility comparison
   - LOC comparison
   - Extensibility metrics
   - Use cases (undo, macro, replay)

2. **Observer Pattern Analysis**
   - Coupling metrics
   - Constructor complexity
   - Testability improvements
   - Performance considerations (notification overhead)

3. **Synergy Analysis**
   - Commands can notify observers
   - Input → Command → Action → Event → Observers
   - Complete data flow

4. **Trade-offs Discussion**
   - Command: More classes (explosion)
   - Observer: Memory leaks (forgot unregister)
   - Observer: Cascade notifications (hard to debug)

---

## 📊 Success Metrics

### For Students
By end of Week 11, students should:
- [ ] Explain WHY Command pattern needed
- [ ] Implement remappable controls
- [ ] Understand undo/redo foundation
- [ ] Explain WHY Observer pattern needed
- [ ] Implement event system
- [ ] Discuss memory leak risks (observer)
- [ ] Understand cascade notification issues

### For Code
- [ ] Player responds to WASD input
- [ ] Keys are remappable at runtime
- [ ] Multiple systems respond to one event
- [ ] No tight coupling (Player independent of UI/Audio)
- [ ] No memory leaks (observers properly unregistered)
- [ ] All tests passing

---

## 🧪 Demonstration Scenarios

### Demo 1: Remapping Impossible
**Setup**: Branch 11-01
**Task**: "Let user remap jump from W to Space"
**Observe**: Must modify InputHandler if-else chain
**Learning**: "Hardcoded = inflexible!"

### Demo 2: Remapping Trivial
**Setup**: Branch 11-02
**Task**: Same remapping task
**Observe**: One line: `bindings.put(SPACE, jumpCmd)`
**Learning**: "Command = flexibility!"

### Demo 3: Object Drilling Hell
**Setup**: Branch 11-03
**Action**: Add new ParticleSystem observer
**Observe**: Must modify Player constructor, pass from Main, etc
**Learning**: "Tight coupling = maintenance nightmare!"

### Demo 4: Event System
**Setup**: Branch 11-04
**Action**: Add ParticleSystem
**Observe**: Just register: `player.addObserver(particles)`
**Learning**: "Observer = extensibility!"

### Demo 5: Memory Leak
**Setup**: Branch 11-04
**Action**: Create temporary observer, forget to unregister
**Observe**: Memory grows over time
**Learning**: "Observer = discipline required!"

---

## 🎓 Teaching Notes

### Key Concepts to Emphasize

#### 1. Command as First-Class Object
In Command pattern, actions become objects:
- Can be stored (command history)
- Can be queued (input buffer)
- Can be serialized (save replay)
- Can be parameterized (macro)

#### 2. Observer != Polling
```java
// ❌ Polling (inefficient)
while (true) {
    if (player.healthChanged()) {
        ui.update();
    }
}

// ✅ Observer (efficient)
player.addObserver(ui);
// UI notified only when needed
```

#### 3. Event Propagation
```
Player takes damage
  ↓
Player notifies all observers
  ↓
HealthBar receives notification → updates display
SoundSystem receives notification → plays sound
Achievements receives notification → checks progress
  ↓
Each observer can trigger more events (cascade)
```

#### 4. Memory Management with Observer
**Critical**: Observer can cause memory leaks!
```java
// Register
player.addObserver(enemy);

// Enemy dies
enemies.remove(enemy);

// ❌ LEAK! Player still holds reference
// ✅ FIX: player.removeObserver(enemy);
```

---

### Common Student Questions

**Q**: "Command pattern = Strategy pattern?"
**A**: Similar structure, different intent:
- Command = encapsulate REQUEST (do something)
- Strategy = encapsulate ALGORITHM (how to do)

**Q**: "Observer = Listener in Android/Java?"
**A**: Yes! Java Event Listeners are Observer pattern. Same concept, different name.

**Q**: "Bagaimana mencegah cascade notification infinite loop?"
**A**: 
1. Don't notify in observer's onNotify
2. Use event queue (buffer, process later)
3. Track notification depth (stop at level 3)

**Q**: "Observer performance overhead?"
**A**: Minimal for game (< 1ms for 100 observers). Only problem: cascade can be slow.

**Q**: "Command pattern untuk AI?"
**A**: Yes! AI can generate commands same as player. Commands = language for actions.

---

## 📦 Deliverables

### Student Deliverables
- [ ] Remappable control system
- [ ] Event-driven architecture
- [ ] Multiple systems reacting to events
- [ ] No tight coupling between systems
- [ ] Unit tests for commands and observers
- [ ] Reflection on trade-offs

### Teaching Material Deliverables
- [ ] All 5 branches implemented
- [ ] SCENARIO/GUIDELINE/PROMPT for each
- [ ] Input remapping demo
- [ ] Event propagation visualization
- [ ] Memory leak demonstration
- [ ] Analysis with metrics

---

## ⏱️ Time Allocation

### Lecture (2 hours)
- 20 min: Command pattern explanation
- 10 min: Demo problem (11-01)
- 10 min: Demo solution (11-02)
- 20 min: Observer pattern explanation
- 15 min: Demo coupling problem (11-03)
- 15 min: Demo event solution (11-04)
- 10 min: Memory leak warning
- 20 min: Q&A and trade-offs

### Lab (3 hours)
- 1.5 hour: Implement Command pattern
- 1.5 hour: Implement Observer pattern
- Testing and experimentation

### Homework (6 hours)
- Implement undo/redo using Command
- Add 3 new observers to event system
- Write analysis on cascade notifications
- Memory leak testing and prevention

---

## 🔗 Connections to Other Weeks

### Builds on Week 9-10
- Uses GameManager (Singleton) for event coordination
- Commands operate on Player/entities
- Observers react to gameplay events

### Prepares for Week 12
- Commands will be issued by AI (Strategy)
- Events will trigger state transitions (State)
- Foundation for complex gameplay

---

## 📚 Additional Resources

### Recommended Reading
- "Design Patterns" GoF - Command & Observer chapters
- "Head First Design Patterns" - Same chapters
- "Game Programming Patterns" - Command & Observer
- Android developer guide - Event handling

### Real Implementations
- Java Swing ActionListener (Command)
- JavaScript event system (Observer)
- Unity Event System (Observer)
- RxJava (Reactive Extensions = advanced Observer)

---

## ✅ Week 11 Checklist

Before moving to Week 12:
- [ ] Player responds to keyboard input
- [ ] Controls are remappable
- [ ] Event system works (1 event → many reactions)
- [ ] No tight coupling Player ↔ Systems
- [ ] No memory leaks (tested with profiler)
- [ ] Students understand WHY (not just HOW)
- [ ] Students aware of memory leak risk
- [ ] All tests passing
- [ ] Documentation complete

---

**Major Milestone**: Week 11 = game becomes truly INTERACTIVE! 🎮⌨️
