# Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN)

**Branch**: `13-03-tightly-coupled`

**Compilation**: `javac -d bin/13-03-tightly-coupled src/battle/subsystems/*.java src/SubsystemDemo.java`

**Run**: `java -cp bin/13-03-tightly-coupled SubsystemDemo`

---

## Overview

Branch ini mendemonstrasikan **masalah** yang terjadi ketika client code berinteraksi langsung dengan banyak subsystem kompleks. Client harus mengetahui detail internal setiap subsystem, menyebabkan **tight coupling** dan **complex client code**.

---

## Masalah: Client Tightly Coupled ke Subsystems

### Client Harus Tahu Semua Detail

```java
public class BattleWithoutFacade {
    public void startBattle() {
        // Client harus tahu cara initialize BattleAnimationSystem
        BattleAnimationSystem animation = new BattleAnimationSystem();
        animation.init();
        animation.loadBattleSprites();

        // Client harus tahu cara initialize BattleSoundSystem
        BattleSoundSystem sound = new BattleSoundSystem();
        sound.init();
        sound.loadBattleSounds();
        sound.playBattleMusic("boss_battle.mp3");

        // Client harus tahu cara initialize BattleUISystem
        BattleUISystem ui = new BattleUISystem();
        ui.init();
        ui.createBattleUI();
        ui.showBattleScreen();

        // ═══════════════════════════════════════════════════
        // Untuk SETIAP aksi, client harus koordinasi SEMUA subsystems!
        // ═══════════════════════════════════════════════════

        // Player Attack - 5+ method calls!
        animation.playAttackAnimation("Player");
        sound.playAttackSound();
        animation.playDamageAnimation("Boss", 45);
        ui.showDamageNumber("Boss", 45);
        ui.updateHealthBars(100, 455);

        // Cleanup (harus tahu urutan yang benar!)
        sound.stopMusic();
        ui.cleanup();
        sound.cleanup();
        animation.cleanup();
    }
}
```

---

## Masalah yang Teridentifikasi

### 1. Client Complexity
```
Client harus tahu:
├── BattleAnimationSystem
│   ├── init() - first!
│   ├── loadBattleSprites() - after init!
│   ├── playAttackAnimation()
│   ├── playDefendAnimation()
│   ├── playMagicAnimation()
│   └── playDamageAnimation()
├── BattleSoundSystem
│   ├── init() - first!
│   ├── loadBattleSounds() - after init!
│   ├── playBattleMusic()
│   ├── playAttackSound()
│   ├── playMagicSound()
│   └── playBossRoar()
└── BattleUISystem
    ├── init() - first!
    ├── createBattleUI() - after init!
    ├── showBattleScreen() - after createBattleUI!
    ├── updateHealthBars()
    ├── showActionMenu()
    └── showDamageNumber()

Total: 3 subsystems dengan 15+ methods yang harus dipahami client!
```

### 2. Tight Coupling
```java
// Client langsung bergantung pada SEMUA subsystem classes
import battle.subsystems.BattleAnimationSystem;
import battle.subsystems.BattleSoundSystem;
import battle.subsystems.BattleUISystem;

// Perubahan di SATU subsystem = perubahan di client!
```

### 3. Coordination Burden
```java
// Untuk SETIAP battle action, client harus:
void playerAttack() {
    animation.playAttackAnimation("Player");  // 1. Animation
    sound.playAttackSound();                  // 2. Sound
    animation.playDamageAnimation("Boss", 45);// 3. More animation
    ui.showDamageNumber("Boss", 45);          // 4. UI update
    ui.updateHealthBars(100, 455);            // 5. More UI
    // 5+ method calls untuk SATU aksi!
}
```

### 4. Initialization Order Bugs
```java
// Urutan initialization penting! Mudah salah:
BattleUISystem badUI = new BattleUISystem();
badUI.showBattleScreen();  // ✗ ERROR: UI not created!
// Client lupa call init() dan createBattleUI() dulu!

// Correct order:
ui.init();
ui.createBattleUI();
ui.showBattleScreen();  // ✓ Works
```

### 5. Code Duplication
```java
// Setiap tempat yang butuh battle harus copy-paste coordination!
class MainGame {
    void startBossBattle() {
        // Copy-paste all initialization...
        // Copy-paste coordination code...
    }
}

class ArenaMode {
    void startArenaBattle() {
        // Copy-paste all initialization...
        // Copy-paste coordination code...
    }
}
```

---

## File Structure

```
src/
├── SubsystemDemo.java              # Demo client (complex!)
└── battle/
    └── subsystems/
        ├── BattleAnimationSystem.java  # Sprites, animations
        ├── BattleSoundSystem.java      # Audio, music, SFX
        └── BattleUISystem.java         # Health bars, menus
```

---

## Implementasi

### BattleAnimationSystem.java
```java
package battle.subsystems;

public class BattleAnimationSystem {
    private boolean initialized = false;
    private boolean spritesLoaded = false;

    // Step 1: MUST be called first!
    public void init() {
        System.out.println("[AnimationSystem] Initializing...");
        initialized = true;
    }

    // Step 2: REQUIRES init() first!
    public void loadBattleSprites() {
        if (!initialized) {
            System.out.println("[AnimationSystem] ✗ ERROR: Not initialized!");
            return;
        }
        System.out.println("[AnimationSystem] Loading battle sprites...");
        spritesLoaded = true;
    }

    // REQUIRES loadBattleSprites() first!
    public void playAttackAnimation(String attacker) {
        if (!spritesLoaded) {
            System.out.println("[AnimationSystem] ✗ ERROR: Sprites not loaded!");
            return;
        }
        System.out.println("[AnimationSystem] ⚔ " + attacker + " attack animation");
    }

    // ... more methods

    public void cleanup() {
        System.out.println("[AnimationSystem] Cleaning up...");
        spritesLoaded = false;
        initialized = false;
    }
}
```

### BattleSoundSystem.java
```java
package battle.subsystems;

public class BattleSoundSystem {
    private boolean initialized = false;
    private boolean soundsLoaded = false;

    // Step 1: MUST be called first!
    public void init() { ... }

    // Step 2: REQUIRES init() first!
    public void loadBattleSounds() { ... }

    // REQUIRES loadBattleSounds() first!
    public void playBattleMusic(String musicFile) { ... }
    public void playAttackSound() { ... }
    public void playMagicSound() { ... }

    public void cleanup() { ... }
}
```

### BattleUISystem.java
```java
package battle.subsystems;

public class BattleUISystem {
    private boolean initialized = false;
    private boolean uiCreated = false;
    private boolean screenVisible = false;

    // Step 1: MUST be called first!
    public void init() { ... }

    // Step 2: REQUIRES init() first!
    public void createBattleUI() { ... }

    // Step 3: REQUIRES createBattleUI() first!
    public void showBattleScreen() { ... }

    // REQUIRES showBattleScreen() first!
    public void updateHealthBars(int playerHp, int bossHp) { ... }
    public void showActionMenu() { ... }

    public void cleanup() { ... }
}
```

### SubsystemDemo.java
```java
import battle.subsystems.BattleAnimationSystem;
import battle.subsystems.BattleSoundSystem;
import battle.subsystems.BattleUISystem;

/**
 * ANTI-PATTERN: Client tightly coupled to all subsystems
 */
public class SubsystemDemo {
    public static void main(String[] args) {
        // Client creates ALL subsystems
        BattleAnimationSystem animation = new BattleAnimationSystem();
        BattleSoundSystem sound = new BattleSoundSystem();
        BattleUISystem ui = new BattleUISystem();

        // Client must know initialization ORDER
        animation.init();
        animation.loadBattleSprites();
        sound.init();
        sound.loadBattleSounds();
        sound.playBattleMusic("boss_battle.mp3");
        ui.init();
        ui.createBattleUI();
        ui.showBattleScreen();

        // For EACH action, client coordinates ALL subsystems
        // Player Attack - 5 method calls!
        animation.playAttackAnimation("Player");
        sound.playAttackSound();
        animation.playDamageAnimation("Boss", 45);
        ui.showDamageNumber("Boss", 45);
        ui.updateHealthBars(100, 455);

        // Cleanup
        sound.stopMusic();
        ui.cleanup();
        sound.cleanup();
        animation.cleanup();
    }
}
```

---

## Output Demo

```
╔══════════════════════════════════════════════════════╗
║   WEEK 13-03: TIGHTLY COUPLED SUBSYSTEMS             ║
║   (ANTI-PATTERN DEMONSTRATION)                       ║
╚══════════════════════════════════════════════════════╝

This demo shows PROBLEMS with tightly coupled subsystems:
  1. Client knows ALL subsystem internals
  2. Client manages initialization ORDER
  3. Client coordinates actions across subsystems
  4. Client handles cleanup order
  5. Subsystem changes affect ALL clients

════════════════════════════════════════════════════════════
 CLIENT CREATES ALL SUBSYSTEMS DIRECTLY
════════════════════════════════════════════════════════════

[AnimationSystem] Initializing...
[AnimationSystem] Loading battle sprites...
[SoundSystem] Initializing...
[SoundSystem] Loading battle sounds...
[SoundSystem] ♪ Now playing: boss_battle.mp3
[UISystem] Initializing...
[UISystem] Creating battle UI...
[UISystem] Showing battle screen...

════════════════════════════════════════════════════════════
 DEMO: What happens if order is WRONG?
════════════════════════════════════════════════════════════

[UISystem] ✗ ERROR: UI not created!
  → Client forgot to call createBattleUI() first!

════════════════════════════════════════════════════════════
 PROBLEM: Client coordinates battle actions manually
════════════════════════════════════════════════════════════

┌─── Player chooses: ATTACK ────────────────────────┐

[AnimationSystem] ⚔ Player attack animation
[SoundSystem] 🔊 *slash*
[AnimationSystem] 💥 Boss takes 45 damage!
[UISystem] Boss: -45 HP
[UISystem] Health - Player: 100 HP | Boss: 455 HP

// That was 5 method calls just for ONE attack!
```

---

## Solusi

Masalah ini diselesaikan dengan **Facade Pattern** di branch `13-04-facade-pattern`:
- Satu interface sederhana (`BattleFacade`)
- Client hanya tahu `startBattle()`, `playerAttack()`, `endBattle()`
- Detail subsystem tersembunyi di balik Facade
- Perubahan subsystem tidak mempengaruhi client

Lihat: [Week 13-04: Facade Pattern](./week-13-04-facade-pattern.md)
