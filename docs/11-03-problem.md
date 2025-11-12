# Week 11-03: Tight Coupling for Event Systems (ANTI-PATTERN)

**Branch:** `11-03-tight-coupling-events`

**Teaching Focus:** Demonstrating tight coupling problems when adding event-driven systems

---

## ❌ PROBLEM: Tight Coupling Between Systems

This branch demonstrates the **tight coupling anti-pattern** that occurs when adding event-driven systems (sound, achievements) without proper design patterns.

### New Features Added

1. **SoundSystem** - Plays console bell sounds for game events
2. **AchievementSystem** - Tracks player achievements
3. **HUD Enhancement** - Displays unlocked achievements

### The Tight Coupling Problem

#### Problem 1: Object Drilling

**GameLogic must create systems in dependency order:**
```java
// ❌ Complex dependency chain
this.soundSystem = new SoundSystem();
this.achievementSystem = new AchievementSystem(soundSystem);  // Needs SoundSystem!
this.hud = new HUD(achievementSystem);  // Needs AchievementSystem!
this.player = new Player(10, 10, soundSystem, achievementSystem);  // Needs both!
```

**Dependency Graph:**
```
GameLogic
    ↓
SoundSystem
    ↓
AchievementSystem (needs SoundSystem)
    ↓
HUD (needs AchievementSystem)
    ↓
Player (needs SoundSystem + AchievementSystem)
```

#### Problem 2: Constructor Explosion

**Player constructor becomes complex:**
```java
// ❌ Before: Simple
public Player(int x, int y)

// ❌ Now: Complex with dependencies
public Player(int x, int y, SoundSystem soundSystem, AchievementSystem achievementSystem)
```

**AchievementSystem also has dependencies:**
```java
// ❌ Needs SoundSystem to play achievement sound
public AchievementSystem(SoundSystem soundSystem)
```

**HUD also has dependencies:**
```java
// ❌ Needs AchievementSystem to display achievements
public HUD(AchievementSystem achievementSystem)
```

#### Problem 3: Manual Event Notification

**Player must manually notify all systems:**
```java
public void takeDamage(int amount) {
    health -= amount;

    // ❌ Player must know about all systems!
    soundSystem.playHurtSound();
    achievementSystem.onDamageTaken(amount);

    // Want to add ParticleSystem? Must modify this method!
}

public void collectCoin(int value) {
    score += value;

    // ❌ Player must know about all systems!
    soundSystem.playCoinSound();
    achievementSystem.onCoinCollected();
}
```

#### Problem 4: Violates Single Responsibility Principle

**Player does too much:**
- ✅ Manages player state (position, health, score)
- ❌ Notifies sound system
- ❌ Notifies achievement system
- ❌ Knows about all game systems

### Implementation Details

#### SoundSystem (`systems/SoundSystem.java`)
```java
public class SoundSystem {
    public void playHurtSound() {
        System.out.print("\007");  // Console bell
        System.out.flush();
    }

    public void playCoinSound() {
        System.out.print("\007\007");  // Double beep
        System.out.flush();
    }

    public void playAchievementSound() {
        System.out.print("\007\007\007");  // Triple beep
        System.out.flush();
    }
}
```

#### AchievementSystem (`systems/AchievementSystem.java`)
```java
public class AchievementSystem {
    private SoundSystem soundSystem;  // ❌ Tight coupling!
    private List<String> unlockedAchievements;

    // ❌ Constructor needs SoundSystem
    public AchievementSystem(SoundSystem soundSystem) {
        this.soundSystem = soundSystem;
        this.unlockedAchievements = new ArrayList<>();
    }

    public void onDamageTaken(int damage) {
        if (!firstBloodUnlocked) {
            firstBloodUnlocked = true;
            unlockAchievement("First Blood - Took damage for the first time!");
        }
    }

    private void unlockAchievement(String achievement) {
        unlockedAchievements.add(achievement);
        soundSystem.playAchievementSound();  // ❌ Direct call to SoundSystem!
        System.out.println("*** ACHIEVEMENT UNLOCKED: " + achievement + " ***");
    }
}
```

#### HUD (`HUD.java`)
```java
public class HUD {
    private AchievementSystem achievementSystem;  // ❌ Tight coupling!

    // ❌ Constructor needs AchievementSystem
    public HUD(AchievementSystem achievementSystem) {
        this.achievementSystem = achievementSystem;
    }

    public void draw() {
        // Draw main HUD...

        // ❌ Must query AchievementSystem directly
        List<String> achievements = achievementSystem.getUnlockedAchievements();
        // Draw achievements...
    }
}
```

### Problems Demonstrated

1. **Hard to Extend**
   ```java
   // Want to add ParticleSystem?
   // Must modify:
   // - Player constructor (add parameter)
   // - Player.takeDamage() (add particle call)
   // - Player.collectCoin() (add particle call)
   // - GameLogic constructor (create and pass system)
   ```

2. **Hard to Test**
   ```java
   // To test Player, must mock:
   // - SoundSystem
   // - AchievementSystem
   // Complex setup required!
   ```

3. **Hard to Maintain**
   ```java
   // Changing one system affects multiple classes
   // Complex initialization order dependencies
   // Risk of circular dependencies
   ```

4. **Violates Open/Closed Principle**
   ```java
   // Cannot add new systems without modifying existing code
   // Player class must be modified for every new system
   ```

### Running the Demo

```bash
cd bin/11-03-tight-coupling-events
java Main
```

**Try it:**
- Move player into obstacles to take damage
- Hear sound effect (console beep)
- See "First Blood" achievement unlock
- Notice achievement displayed in HUD
- Collect 5 coins to unlock "Coin Collector"
- Survive 30 seconds to unlock "Survivor"

**All features work, but the code is tightly coupled!**

### What Went Wrong?

1. **Systems know about each other**
   - AchievementSystem knows about SoundSystem
   - HUD knows about AchievementSystem
   - Player knows about all systems

2. **Complex initialization order**
   - Must create SoundSystem first
   - Then AchievementSystem (needs SoundSystem)
   - Then HUD (needs AchievementSystem)
   - Then Player (needs both)

3. **No separation of concerns**
   - Player is responsible for notifying all systems
   - Systems are tightly coupled to each other

### Teaching Points

**This demonstrates why we need the Observer Pattern:**

1. **Decoupling** - Systems shouldn't know about each other
2. **Event-driven** - Publish events, let observers react
3. **Extensibility** - Add new observers without modifying publishers
4. **Single Responsibility** - Each class has one job

---

**Next:** Branch 11-04 demonstrates the Observer Pattern solution
