# Week 11-04: Observer Pattern Solution

**Branch:** `11-04-observer-pattern`

**Teaching Focus:** Demonstrating the Observer Pattern to decouple event-driven systems

---

## ✅ SOLUTION: Observer Pattern with EventBus

This branch demonstrates the **Observer Pattern** which solves the tight coupling problems from branch 11-03.

### What is the Observer Pattern?

The Observer Pattern defines a one-to-many dependency between objects where when one object changes state, all its dependents are notified automatically.

**Key Components:**
1. **Subject (Publisher)** - The object being observed (e.g., Player)
2. **Observer (Listener)** - Objects that want to be notified of changes (e.g., SoundSystem)
3. **EventBus** - Central coordinator that manages the pub-sub relationship

### Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                       EventBus                          │
│              (Singleton - Central Hub)                  │
│                                                         │
│  - subscribe(listener)                                  │
│  - publish(event)                                       │
│  - Maintains list of all listeners                      │
└─────────────────────────────────────────────────────────┘
                    ▲                    │
                    │                    │
                    │ subscribe          │ publish
                    │                    │
                    │                    ▼
┌───────────────────┴──────┐    ┌──────────────────────┐
│     Publishers           │    │     Listeners        │
│                          │    │                      │
│  Player                  │    │  SoundSystem         │
│  AchievementSystem       │    │  AchievementSystem   │
│  GameEngine              │    │  HUD                 │
│                          │    │                      │
│  publish(event)          │    │  onEvent(event)      │
└──────────────────────────┘    └──────────────────────┘
```

### Event System Components

#### 1. GameEvent (Base Class)

```java
package events;

public abstract class GameEvent {
    private final String eventType;
    private final long timestamp;

    public GameEvent(String eventType) {
        this.eventType = eventType;
        this.timestamp = System.currentTimeMillis();
    }

    public String getEventType() { return eventType; }
    public long getTimestamp() { return timestamp; }
}
```

**Purpose:** Base class for all events carrying common metadata (type, timestamp).

#### 2. GameEventListener (Interface)

```java
package events;

public interface GameEventListener {
    void onEvent(GameEvent event);
}
```

**Purpose:** All observers must implement this interface to receive events.

#### 3. EventBus (Singleton)

```java
package events;

public class EventBus {
    private static EventBus instance;
    private List<GameEventListener> listeners;

    private EventBus() {
        this.listeners = new ArrayList<>();
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public void subscribe(GameEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void publish(GameEvent event) {
        for (GameEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
```

**Purpose:** Central hub that coordinates all event publishing and subscription.

#### 4. Event Classes

**DamageTakenEvent:**
```java
public class DamageTakenEvent extends GameEvent {
    private final int damage;
    private final int remainingHealth;

    public DamageTakenEvent(int damage, int remainingHealth) {
        super("DamageTaken");
        this.damage = damage;
        this.remainingHealth = remainingHealth;
    }
    // Getters...
}
```

**CoinCollectedEvent:**
```java
public class CoinCollectedEvent extends GameEvent {
    private final int value;
    private final int totalScore;

    public CoinCollectedEvent(int value, int totalScore) {
        super("CoinCollected");
        this.value = value;
        this.totalScore = totalScore;
    }
    // Getters...
}
```

**AchievementUnlockedEvent:**
```java
public class AchievementUnlockedEvent extends GameEvent {
    private final String achievementName;

    public AchievementUnlockedEvent(String achievementName) {
        super("AchievementUnlocked");
        this.achievementName = achievementName;
    }
    // Getters...
}
```

**GameTimeEvent:**
```java
public class GameTimeEvent extends GameEvent {
    private final float elapsedTime;

    public GameTimeEvent(float elapsedTime) {
        super("GameTime");
        this.elapsedTime = elapsedTime;
    }
    // Getters...
}
```

### Refactored Components

#### Player (Publisher)

**Before (11-03):**
```java
public class Player implements Entity {
    private SoundSystem soundSystem;  // ❌ Tight coupling!
    private AchievementSystem achievementSystem;  // ❌ Tight coupling!

    // ❌ Constructor explosion
    public Player(int x, int y, SoundSystem soundSystem, AchievementSystem achievementSystem) {
        // ...
    }

    public void takeDamage(int amount) {
        health -= amount;
        soundSystem.playHurtSound();  // ❌ Direct call
        achievementSystem.onDamageTaken(amount);  // ❌ Direct call
    }
}
```

**After (11-04):**
```java
public class Player implements Entity {
    // ✅ No dependencies!

    // ✅ Simple constructor!
    public Player(int x, int y) {
        // ...
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;

        // ✅ Just publish event - observers react automatically
        EventBus.getInstance().publish(new DamageTakenEvent(amount, health));
    }

    public void collectCoin(int value) {
        score += value;
        // ✅ Just publish event - observers react automatically
        EventBus.getInstance().publish(new CoinCollectedEvent(value, score));
    }
}
```

#### SoundSystem (Observer)

**Before (11-03):**
```java
public class SoundSystem {
    // Methods called directly by other classes
    public void playHurtSound() { /* ... */ }
    public void playCoinSound() { /* ... */ }
    public void playAchievementSound() { /* ... */ }
}
```

**After (11-04):**
```java
public class SoundSystem implements GameEventListener {
    @Override
    public void onEvent(GameEvent event) {
        // ✅ React to events automatically
        if (event instanceof DamageTakenEvent) {
            playHurtSound();
        } else if (event instanceof CoinCollectedEvent) {
            playCoinSound();
        } else if (event instanceof AchievementUnlockedEvent) {
            playAchievementSound();
        }
    }

    private void playHurtSound() {
        System.out.print("\007");
        System.out.flush();
    }
    // ... other private methods
}
```

#### AchievementSystem (Observer and Publisher)

**Before (11-03):**
```java
public class AchievementSystem {
    private SoundSystem soundSystem;  // ❌ Dependency!

    // ❌ Needs SoundSystem parameter
    public AchievementSystem(SoundSystem soundSystem) {
        this.soundSystem = soundSystem;
    }

    private void unlockAchievement(String achievement) {
        unlockedAchievements.add(achievement);
        soundSystem.playAchievementSound();  // ❌ Direct call
    }
}
```

**After (11-04):**
```java
public class AchievementSystem implements GameEventListener {
    // ✅ No dependencies!

    public AchievementSystem() {
        this.unlockedAchievements = new ArrayList<>();
    }

    @Override
    public void onEvent(GameEvent event) {
        // ✅ Listen to events
        if (event instanceof DamageTakenEvent) {
            onDamageTaken((DamageTakenEvent) event);
        } else if (event instanceof CoinCollectedEvent) {
            onCoinCollected();
        } else if (event instanceof GameTimeEvent) {
            checkSurvivor(((GameTimeEvent) event).getElapsedTime());
        }
    }

    private void unlockAchievement(String achievement) {
        unlockedAchievements.add(achievement);
        // ✅ Publish event instead of calling systems
        EventBus.getInstance().publish(new AchievementUnlockedEvent(achievement));
    }
}
```

#### HUD (Observer)

**Before (11-03):**
```java
public class HUD {
    private AchievementSystem achievementSystem;  // ❌ Dependency!

    // ❌ Needs AchievementSystem parameter
    public HUD(AchievementSystem achievementSystem) {
        this.achievementSystem = achievementSystem;
    }

    public void draw() {
        // ❌ Query system directly
        List<String> achievements = achievementSystem.getUnlockedAchievements();
    }
}
```

**After (11-04):**
```java
public class HUD implements GameEventListener {
    private List<String> achievements;  // ✅ Stores locally

    // ✅ Simple constructor - no dependencies!
    public HUD() {
        this.achievements = new ArrayList<>();
    }

    @Override
    public void onEvent(GameEvent event) {
        // ✅ Listen to achievement events
        if (event instanceof AchievementUnlockedEvent) {
            AchievementUnlockedEvent achievementEvent = (AchievementUnlockedEvent) event;
            achievements.add(achievementEvent.getAchievementName());
        }
    }

    public void draw() {
        // ✅ Use locally stored achievements
        // Display achievements...
    }
}
```

#### GameLogic (Initialization)

**Before (11-03):**
```java
public GameLogic() {
    // ❌ Complex dependency chain
    this.soundSystem = new SoundSystem();
    this.achievementSystem = new AchievementSystem(soundSystem);  // Needs SoundSystem!
    this.hud = new HUD(achievementSystem);  // Needs AchievementSystem!
    this.player = new Player(10, 10, soundSystem, achievementSystem);  // Needs both!
}
```

**After (11-04):**
```java
public GameLogic() {
    this.random = new Random();

    // ✅ Create systems independently - no dependencies!
    this.soundSystem = new SoundSystem();
    this.achievementSystem = new AchievementSystem();
    this.hud = new HUD();

    // ✅ Register all observers with EventBus
    EventBus eventBus = EventBus.getInstance();
    eventBus.subscribe(soundSystem);
    eventBus.subscribe(achievementSystem);
    eventBus.subscribe(hud);

    // ✅ Simple Player constructor!
    this.player = new Player(10, 10);
}
```

### Benefits of Observer Pattern

#### 1. Loose Coupling

**Before:**
```
Player ──depends on──> SoundSystem
Player ──depends on──> AchievementSystem
AchievementSystem ──depends on──> SoundSystem
HUD ──depends on──> AchievementSystem
```

**After:**
```
Player ──publishes──> EventBus <──subscribes── SoundSystem
                                <──subscribes── AchievementSystem
                                <──subscribes── HUD
```

Systems don't know about each other!

#### 2. Easy to Extend

**Want to add ParticleSystem?**

Before (11-03):
```java
// Must modify:
// 1. Player constructor - add ParticleSystem parameter
// 2. Player.takeDamage() - add particle.playEffect()
// 3. Player.collectCoin() - add particle.playEffect()
// 4. GameLogic constructor - create and pass ParticleSystem
```

After (11-04):
```java
// Just create and register!
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
EventBus.getInstance().subscribe(particles);  // Done!
```

#### 3. Easy to Test

**Before (11-03):**
```java
// To test Player, must mock:
Player player = new Player(10, 10, mockSound, mockAchievement);
// Complex setup!
```

**After (11-04):**
```java
// To test Player, just verify events:
Player player = new Player(10, 10);  // Simple!
player.takeDamage(10);
// Verify DamageTakenEvent was published
```

#### 4. Single Responsibility Principle

**Player's responsibilities:**
- ✅ Manage player state (position, health, score)
- ✅ Publish events when state changes
- ❌ ~~Notify sound system~~ (now handled by EventBus)
- ❌ ~~Notify achievement system~~ (now handled by EventBus)
- ❌ ~~Know about all game systems~~ (now handled by EventBus)

#### 5. Open/Closed Principle

**The system is:**
- **Open for extension** - Add new observers without modifying existing code
- **Closed for modification** - Player doesn't need to change when adding new systems

### Comparison Table

| Aspect | Branch 11-03 (Tight Coupling) | Branch 11-04 (Observer Pattern) |
|--------|-------------------------------|----------------------------------|
| **Player Constructor** | `Player(x, y, soundSystem, achievementSystem)` | `Player(x, y)` |
| **Dependencies** | Player knows about all systems | Player knows only EventBus |
| **Adding New System** | Must modify Player code | Just register with EventBus |
| **System Communication** | Direct method calls | Event publishing |
| **Initialization Order** | Must create in dependency order | Create in any order |
| **Testing Complexity** | Must mock all systems | Just verify events |
| **Coupling Level** | Tight (direct dependencies) | Loose (via events) |
| **Extensibility** | Hard (modify existing code) | Easy (add observers) |

### Event Flow Example

**User presses key to move into obstacle:**

1. **Command Pattern** handles input:
   ```java
   MoveCommand.execute() → player.moveDown()
   ```

2. **Collision detection:**
   ```java
   gameLogic.checkCollisions() → player.takeDamage(10)
   ```

3. **Player publishes event:**
   ```java
   player.takeDamage(10) → EventBus.publish(new DamageTakenEvent(10, 90))
   ```

4. **EventBus notifies all listeners:**
   ```java
   EventBus.publish() → soundSystem.onEvent(event)
                     → achievementSystem.onEvent(event)
                     → hud.onEvent(event)
   ```

5. **Each listener reacts:**
   ```java
   soundSystem → playHurtSound() → Console beep!
   achievementSystem → check "First Blood" → unlockAchievement()
   achievementSystem → publish(AchievementUnlockedEvent)
   ```

6. **Achievement event flows back:**
   ```java
   EventBus.publish(AchievementUnlockedEvent) → soundSystem.onEvent()
                                              → hud.onEvent()
   ```

7. **Final reactions:**
   ```java
   soundSystem → playAchievementSound() → Triple beep!
   hud → Store achievement → Display in next draw()
   ```

**Complete decoupling! Player doesn't know anything about sound or achievements.**

### Running the Demo

```bash
cd bin/11-04-observer-pattern
java Main
```

**Try it:**
- Move player into obstacles to take damage
- Hear sound effect (console beep)
- See "First Blood" achievement unlock with triple beep
- Notice achievement displayed in HUD
- Collect 5 coins to unlock "Coin Collector"
- Survive 30 seconds to unlock "Survivor"

**All features work identically to 11-03, but with clean architecture!**

### Design Patterns Used

This branch now demonstrates **five design patterns working together:**

1. **Singleton Pattern** - EventBus, GameManager (from Week 9)
2. **Factory Pattern** - NPC creation (from Week 9)
3. **Object Pool Pattern** - NPC recycling (from Week 10)
4. **Command Pattern** - Flexible input handling (from Week 11-02)
5. **Observer Pattern** - Event-driven systems (from Week 11-04)

### Teaching Points

**This demonstrates why the Observer Pattern is essential:**

1. **Decoupling** ✅
   - Publishers don't know about subscribers
   - Systems don't know about each other
   - Communication via events only

2. **Extensibility** ✅
   - Add new observers without modifying publishers
   - Add new event types without modifying existing listeners
   - Follows Open/Closed Principle

3. **Maintainability** ✅
   - Simple constructors with no dependencies
   - Easy to understand event flow
   - Easy to test each component independently

4. **Scalability** ✅
   - Add unlimited observers without code changes
   - Multiple observers can react to same event
   - One observer can listen to multiple event types

---

**Evolution Path:**
- Branch 11-01: Hardcoded input (anti-pattern)
- Branch 11-02: Command Pattern (solution)
- Branch 11-03: Tight coupling (anti-pattern)
- Branch 11-04: Observer Pattern (solution) ← **YOU ARE HERE**

**Next Steps:** Students can explore:
- Adding new event types (PlayerMovedEvent, NPCDefeatedEvent)
- Adding new observers (ParticleSystem, Logger, Analytics)
- Event filtering and priorities
- Asynchronous event handling
