# Week 13-04: Facade Pattern (SOLUTION)

**Branch**: `13-04-facade-pattern`

**Compilation**: `javac -d bin/13-04-facade-pattern src/battle/subsystems/*.java src/battle/BattleFacade.java src/SubsystemDemo.java`

**Run**: `java -cp bin/13-04-facade-pattern SubsystemDemo`

---

## Overview

Facade Pattern menyediakan **interface sederhana** (unified interface) untuk sekumpulan interface dalam subsystem yang kompleks. Facade mendefinisikan interface higher-level yang membuat subsystem lebih **mudah digunakan**.

Branch ini menyelesaikan masalah **tight coupling** yang ditunjukkan di branch `13-03-tightly-coupled`.

---

## Masalah Sebelumnya (Week 13-03)

Lihat: [Week 13-03: Tightly Coupled Subsystems](./week-13-03-tightly-coupled.md)

### Client Terikat ke Banyak Subsystem

```java
// Client code tanpa Facade - sangat kompleks!
BattleAnimationSystem animation = new BattleAnimationSystem();
BattleSoundSystem sound = new BattleSoundSystem();
BattleUISystem ui = new BattleUISystem();

// 7+ initialization calls in specific order!
animation.init();
animation.loadBattleSprites();
sound.init();
sound.loadBattleSounds();
sound.playBattleMusic("boss_battle.mp3");
ui.init();
ui.createBattleUI();
ui.showBattleScreen();

// 5+ calls for EACH battle action!
animation.playAttackAnimation("Player");
sound.playAttackSound();
animation.playDamageAnimation("Boss", 45);
ui.showDamageNumber("Boss", 45);
ui.updateHealthBars(100, 455);
```

**Masalah**:
- Client harus tahu detail setiap subsystem
- Client harus tahu urutan initialization
- Client harus koordinasi setiap aksi
- Perubahan subsystem mempengaruhi client

---

## Solusi: Facade Pattern

### Struktur

```
┌─────────────────┐
│     Client      │
│ (SubsystemDemo) │
└────────┬────────┘
         │
         │  Simple interface
         ▼
┌─────────────────────────────────────┐
│           BattleFacade              │
│ ───────────────────────────────────│
│ + startBattle()                     │
│ + playerAttack()                    │
│ + playerDefend()                    │
│ + playerMagic()                     │
│ + bossTurn()                        │
│ + endBattle()                       │
└─────────────────┬───────────────────┘
                  │
                  │  Delegates to subsystems
         ┌────────┼────────┐
         │        │        │
         ▼        ▼        ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│  Animation   │ │    Sound     │ │      UI      │
│   System     │ │   System     │ │   System     │
│ ──────────── │ │ ──────────── │ │ ──────────── │
│ - sprites    │ │ - sounds     │ │ - healthBars │
│ + init()     │ │ + init()     │ │ + init()     │
│ + playAttack │ │ + playAttack │ │ + showDamage │
│ + cleanup()  │ │ + cleanup()  │ │ + cleanup()  │
└──────────────┘ └──────────────┘ └──────────────┘
```

---

## Implementasi

### 1. BattleFacade.java

```java
package battle;

import battle.subsystems.BattleAnimationSystem;
import battle.subsystems.BattleSoundSystem;
import battle.subsystems.BattleUISystem;

/**
 * FACADE PATTERN
 *
 * Provides a simple, unified interface to the complex
 * animation, sound, and UI subsystems.
 *
 * Benefits:
 * - Client doesn't need to know subsystem details
 * - Easy to use: startBattle(), playerAttack(), endBattle()
 * - Changes to subsystems don't affect client code
 */
public class BattleFacade {
    // Complex subsystems (hidden from client)
    private BattleAnimationSystem animation;
    private BattleSoundSystem sound;
    private BattleUISystem ui;

    // Battle state
    private int playerHp = 100;
    private int bossHp = 500;

    public BattleFacade() {
        this.animation = new BattleAnimationSystem();
        this.sound = new BattleSoundSystem();
        this.ui = new BattleUISystem();
    }

    /**
     * Start battle - ONE call initializes EVERYTHING!
     * Facade handles complex initialization order internally.
     */
    public void startBattle() {
        // Facade handles initialization ORDER internally
        animation.init();
        animation.loadBattleSprites();
        sound.init();
        sound.loadBattleSounds();
        sound.playBattleMusic("boss_battle.mp3");
        ui.init();
        ui.createBattleUI();
        ui.showBattleScreen();
        ui.updateHealthBars(playerHp, bossHp);
    }

    /**
     * Player attack - ONE call coordinates ALL subsystems!
     */
    public void playerAttack() {
        animation.playAttackAnimation("Player");
        sound.playAttackSound();
        int damage = 45;
        bossHp -= damage;
        animation.playDamageAnimation("Boss", damage);
        ui.showDamageNumber("Boss", damage);
        ui.updateHealthBars(playerHp, bossHp);
    }

    /**
     * Player defend - ONE call!
     */
    public void playerDefend() {
        animation.playDefendAnimation("Player");
        sound.playDefendSound();
    }

    /**
     * Player magic - ONE call!
     */
    public void playerMagic() {
        animation.playMagicAnimation("Player");
        sound.playMagicSound();
        int damage = 80;
        bossHp -= damage;
        animation.playDamageAnimation("Boss", damage);
        ui.showDamageNumber("Boss", damage);
        ui.updateHealthBars(playerHp, bossHp);
    }

    /**
     * Boss turn - ONE call!
     */
    public void bossTurn() {
        sound.playBossRoar();
        animation.playAttackAnimation("Boss");
        sound.playAttackSound();
        int damage = 30;
        playerHp -= damage;
        animation.playDamageAnimation("Player", damage);
        ui.showDamageNumber("Player", damage);
        ui.updateHealthBars(playerHp, bossHp);
    }

    /**
     * End battle - ONE call cleans up EVERYTHING!
     * Facade handles cleanup order internally.
     */
    public void endBattle() {
        sound.stopMusic();
        ui.cleanup();
        sound.cleanup();
        animation.cleanup();
    }
}
```

### 2. Client Code (SubsystemDemo.java)

```java
import battle.BattleFacade;

/**
 * Client code using Facade Pattern
 *
 * Notice how simple this is compared to managing
 * all subsystems directly!
 */
public class SubsystemDemo {
    public static void main(String[] args) {
        // Create facade - hides all complexity
        BattleFacade battle = new BattleFacade();

        // Start battle - ONE simple call
        battle.startBattle();

        // Battle actions - simple calls
        battle.showActions();
        battle.playerAttack();
        battle.bossTurn();
        battle.playerMagic();
        battle.playerDefend();

        // End battle - ONE simple call
        battle.endBattle();
    }
}
```

---

## Output

```
╔══════════════════════════════════════════════════════╗
║   WEEK 13-04: FACADE PATTERN                         ║
║   (SOLUTION DEMONSTRATION)                           ║
╚══════════════════════════════════════════════════════╝

Facade Pattern SOLVES all previous problems:
  ✓ Client only knows ONE class (BattleFacade)
  ✓ Initialization order handled INTERNALLY
  ✓ Subsystem coordination handled INTERNALLY
  ✓ Cleanup handled INTERNALLY
  ✓ Subsystem changes DON'T affect client!

════════════════════════════════════════════════════════════
 CLIENT ONLY CREATES ONE OBJECT - THE FACADE!
════════════════════════════════════════════════════════════

// Simple client code:
BattleFacade battle = new BattleFacade();

════════════════════════════════════════════════════════════
 SIMPLE: One call to start battle!
════════════════════════════════════════════════════════════

// Just ONE method call:
battle.startBattle();

[BattleFacade] ═══ Starting Battle ═══

[AnimationSystem] Initializing...
[AnimationSystem] Loading battle sprites...
[SoundSystem] Initializing...
[SoundSystem] Loading battle sounds...
[SoundSystem] ♪ Now playing: boss_battle.mp3
[UISystem] Initializing...
[UISystem] Creating battle UI...
[UISystem] Showing battle screen...

[BattleFacade] ═══ Battle Started! ═══

════════════════════════════════════════════════════════════
 SIMPLE: Easy battle actions!
════════════════════════════════════════════════════════════

[BattleFacade] → Player Attack

[AnimationSystem] ⚔ Player attack animation
[SoundSystem] 🔊 *slash*
[AnimationSystem] 💥 Boss takes 45 damage!
[UISystem] Boss: -45 HP
[UISystem] Health - Player: 100 HP | Boss: 455 HP

════════════════════════════════════════════════════════════
 SIMPLE: One call to end battle!
════════════════════════════════════════════════════════════

[BattleFacade] ═══ Ending Battle ═══

[SoundSystem] Stopping: boss_battle.mp3
[UISystem] Cleaning up...
[SoundSystem] Cleaning up...
[AnimationSystem] Cleaning up...

[BattleFacade] ═══ Battle Ended ═══
```

---

## Perbandingan: Tanpa vs Dengan Facade

### Tanpa Facade (13-03)
```java
// Client creates 3 subsystems
BattleAnimationSystem animation = new BattleAnimationSystem();
BattleSoundSystem sound = new BattleSoundSystem();
BattleUISystem ui = new BattleUISystem();

// 7+ initialization calls in specific order!
animation.init();
animation.loadBattleSprites();
sound.init();
sound.loadBattleSounds();
sound.playBattleMusic("boss_battle.mp3");
ui.init();
ui.createBattleUI();
ui.showBattleScreen();

// 5+ calls per battle action!
animation.playAttackAnimation("Player");
sound.playAttackSound();
animation.playDamageAnimation("Boss", 45);
ui.showDamageNumber("Boss", 45);
ui.updateHealthBars(100, 455);

// 4 cleanup calls
sound.stopMusic();
ui.cleanup();
sound.cleanup();
animation.cleanup();
```

### Dengan Facade (13-04)
```java
// Client creates 1 facade
BattleFacade battle = new BattleFacade();

// 1 call to start
battle.startBattle();

// 1 call per action
battle.playerAttack();

// 1 call to end
battle.endBattle();
```

### Comparison Summary

| Aspect | Without Facade | With Facade |
|--------|----------------|-------------|
| Objects to create | 3 subsystems | 1 facade |
| Initialization calls | 7+ | 1 |
| Calls per action | 5+ | 1 |
| Cleanup calls | 4 | 1 |
| Client knowledge | ALL internals | Simple interface |

---

## Principle of Least Knowledge (Law of Demeter)

Facade Pattern menerapkan **Principle of Least Knowledge**:
> "Talk only to your immediate friends"

```
Tanpa Facade:
Client → AnimationSystem → sprites → renderer
Client → SoundSystem → codec → mixer → player
Client → UISystem → components → layout

Dengan Facade:
Client → Facade → (handles everything internally)
```

**Guideline**:
Hanya panggil method pada:
1. Object itu sendiri
2. Object yang dipass sebagai parameter
3. Object yang dibuat/diinstansiasi langsung
4. Component objects yang dimiliki

**Jangan**: `object.getX().getY().doSomething()`

---

## Keuntungan Facade Pattern

1. **Simplicity**: Interface sederhana untuk sistem kompleks
2. **Decoupling**: Client tidak terikat ke subsystem
3. **Easier Maintenance**: Perubahan subsystem tidak mempengaruhi client
4. **Layered Architecture**: Facade sebagai entry point ke layer
5. **Single Point of Entry**: Satu tempat untuk mengakses banyak fitur

---

## Kapan Menggunakan?

✅ **Gunakan Facade** ketika:
- Ada subsystem kompleks dengan banyak class
- Ingin menyediakan interface sederhana untuk library/framework
- Ingin decoupling antara client dan subsystem
- Membangun layered architecture

❌ **Jangan gunakan** ketika:
- Sistem sudah cukup sederhana
- Client memang perlu akses detail ke subsystem
- Hanya ada satu atau dua class dalam "subsystem"

---

## Facade vs Other Patterns

| Pattern | Intent | Relationship |
|---------|--------|--------------|
| **Facade** | Simplify interface | One-to-many (1 facade → many subsystems) |
| **Adapter** | Convert interface | One-to-one (adapt one interface) |
| **Mediator** | Coordinate objects | Many-to-many (objects interact via mediator) |
| **Proxy** | Control access | One-to-one (proxy → real object) |

---

## File Structure

```
src/
├── SubsystemDemo.java           # Client using facade
└── battle/
    ├── BattleFacade.java        # FACADE
    └── subsystems/
        ├── BattleAnimationSystem.java  # Subsystem
        ├── BattleSoundSystem.java      # Subsystem
        └── BattleUISystem.java         # Subsystem
```

---

## Testing

```bash
# Compile
javac -d bin/13-04-facade-pattern src/battle/subsystems/*.java src/battle/BattleFacade.java src/SubsystemDemo.java

# Run demo
java -cp bin/13-04-facade-pattern SubsystemDemo
```

---

## References

- **GoF**: Facade Pattern
- **Head First Design Patterns**: Chapter 7
- **Principle of Least Knowledge** (Law of Demeter)
