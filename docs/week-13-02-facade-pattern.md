# Week 13-02: Facade Pattern

**Branch**: `13-02-facade-pattern`

**Compilation**: `javac -d bin/13-02-facade-pattern src/*.java src/**/*.java`

**Run**: `java -cp bin/13-02-facade-pattern Main`

---

## Overview

Facade Pattern menyediakan **interface sederhana** (unified interface) untuk sekumpulan interface dalam subsystem yang kompleks. Facade mendefinisikan interface higher-level yang membuat subsystem lebih **mudah digunakan**.

---

## Motivasi

### Masalah: Subsystem yang Kompleks

Bayangkan game engine dengan banyak subsystem:

```java
// Client code tanpa Facade - sangat kompleks!
public class GameWithoutFacade {
    public void startGame() {
        // Initialize audio system
        AudioCodec codec = new AudioCodec();
        codec.initialize();
        AudioBuffer buffer = new AudioBuffer();
        buffer.setCodec(codec);
        AudioPlayer player = new AudioPlayer();
        player.setBuffer(buffer);
        player.loadFile("music.mp3");

        // Initialize physics
        PhysicsWorld world = new PhysicsWorld();
        world.setGravity(9.8f);
        CollisionDetector detector = new CollisionDetector();
        detector.setWorld(world);
        RigidBodyManager bodyManager = new RigidBodyManager();
        bodyManager.setDetector(detector);

        // Initialize video
        VideoBuffer videoBuffer = new VideoBuffer();
        Renderer renderer = new Renderer();
        renderer.setBuffer(videoBuffer);
        ShaderManager shaderManager = new ShaderManager();
        shaderManager.loadShaders();
        renderer.setShaderManager(shaderManager);

        // Start everything
        player.play();
        world.start();
        renderer.begin();

        // Game loop
        while (running) {
            world.step(deltaTime);
            detector.checkCollisions();
            renderer.render();
        }
    }
}
```

**Masalah**:
- Client harus tahu detail setiap subsystem
- Banyak dependencies antar class
- Sulit untuk diubah jika subsystem berubah
- Code duplication di setiap tempat yang menggunakan subsystems

---

## Solusi: Facade Pattern

### Struktur

```
┌─────────────────┐
│     Client      │
│   (GameMain)    │
└────────┬────────┘
         │
         │  Simple interface
         ▼
┌─────────────────────────────────────┐
│         GameEngineFacade            │
│ ───────────────────────────────────│
│ + startGame()                       │
│ + updateFrame(deltaTime)            │
│ + shutdown()                        │
│ + playSound(name)                   │
│ + pauseGame()                       │
└─────────────────┬───────────────────┘
                  │
                  │  Delegates to subsystems
         ┌────────┼────────┐
         │        │        │
         ▼        ▼        ▼
┌─────────────┐ ┌──────────────┐ ┌─────────────┐
│ AudioSystem │ │PhysicsEngine │ │ VideoSystem │
│ ─────────── │ │ ──────────── │ │ ─────────── │
│ - codec     │ │ - world      │ │ - buffer    │
│ - buffer    │ │ - detector   │ │ - renderer  │
│ - player    │ │ - bodies     │ │ - shaders   │
│ + init()    │ │ + init()     │ │ + init()    │
│ + play()    │ │ + step()     │ │ + render()  │
│ + stop()    │ │ + cleanup()  │ │ + cleanup() │
└─────────────┘ └──────────────┘ └─────────────┘
```

---

## Implementasi

### 1. Subsystems (Sistem yang Kompleks)

**`src/engine/audio/AudioSystem.java`**:
```java
package engine.audio;

/**
 * Complex audio subsystem
 * In real game: would have codec, buffer, mixer, etc.
 */
public class AudioSystem {
    private boolean initialized = false;
    private String currentMusic = null;

    public void init() {
        System.out.println("[AudioSystem] Initializing audio drivers...");
        System.out.println("[AudioSystem] Loading audio codecs...");
        System.out.println("[AudioSystem] Setting up audio buffer...");
        initialized = true;
        System.out.println("[AudioSystem] Ready!");
    }

    public void playMusic(String musicFile) {
        if (!initialized) {
            throw new IllegalStateException("AudioSystem not initialized!");
        }
        currentMusic = musicFile;
        System.out.println("[AudioSystem] ♪ Playing: " + musicFile);
    }

    public void playSound(String soundFile) {
        if (!initialized) {
            throw new IllegalStateException("AudioSystem not initialized!");
        }
        System.out.println("[AudioSystem] Playing SFX: " + soundFile);
    }

    public void stopMusic() {
        if (currentMusic != null) {
            System.out.println("[AudioSystem] Stopping: " + currentMusic);
            currentMusic = null;
        }
    }

    public void setVolume(float volume) {
        System.out.println("[AudioSystem] Volume set to: " + (volume * 100) + "%");
    }

    public void cleanup() {
        stopMusic();
        System.out.println("[AudioSystem] Releasing audio resources...");
        initialized = false;
    }
}
```

**`src/engine/physics/PhysicsEngine.java`**:
```java
package engine.physics;

/**
 * Complex physics subsystem
 * In real game: would have world, bodies, collision detection, etc.
 */
public class PhysicsEngine {
    private boolean initialized = false;
    private float gravity = 9.8f;

    public void init() {
        System.out.println("[PhysicsEngine] Creating physics world...");
        System.out.println("[PhysicsEngine] Setting gravity: " + gravity);
        System.out.println("[PhysicsEngine] Initializing collision detection...");
        initialized = true;
        System.out.println("[PhysicsEngine] Ready!");
    }

    public void setGravity(float g) {
        this.gravity = g;
        System.out.println("[PhysicsEngine] Gravity updated: " + g);
    }

    public void step(float deltaTime) {
        if (!initialized) {
            throw new IllegalStateException("PhysicsEngine not initialized!");
        }
        // Simulate physics step
        System.out.println("[PhysicsEngine] Physics step: " + deltaTime + "s");
    }

    public void checkCollisions() {
        System.out.println("[PhysicsEngine] Checking collisions...");
    }

    public void cleanup() {
        System.out.println("[PhysicsEngine] Destroying physics world...");
        initialized = false;
    }
}
```

**`src/engine/video/VideoSystem.java`**:
```java
package engine.video;

/**
 * Complex video/rendering subsystem
 * In real game: would have renderer, shaders, framebuffers, etc.
 */
public class VideoSystem {
    private boolean initialized = false;
    private int width = 800;
    private int height = 600;

    public void init() {
        System.out.println("[VideoSystem] Creating window: " + width + "x" + height);
        System.out.println("[VideoSystem] Initializing OpenGL context...");
        System.out.println("[VideoSystem] Loading shaders...");
        System.out.println("[VideoSystem] Setting up framebuffers...");
        initialized = true;
        System.out.println("[VideoSystem] Ready!");
    }

    public void setResolution(int w, int h) {
        this.width = w;
        this.height = h;
        System.out.println("[VideoSystem] Resolution: " + w + "x" + h);
    }

    public void beginFrame() {
        if (!initialized) {
            throw new IllegalStateException("VideoSystem not initialized!");
        }
        System.out.println("[VideoSystem] Begin frame...");
    }

    public void render() {
        System.out.println("[VideoSystem] Rendering scene...");
    }

    public void endFrame() {
        System.out.println("[VideoSystem] End frame, swap buffers");
    }

    public void cleanup() {
        System.out.println("[VideoSystem] Destroying window...");
        System.out.println("[VideoSystem] Releasing GPU resources...");
        initialized = false;
    }
}
```

### 2. Facade Class

**`src/engine/GameEngineFacade.java`**:
```java
package engine;

import engine.audio.AudioSystem;
import engine.physics.PhysicsEngine;
import engine.video.VideoSystem;

/**
 * FACADE PATTERN
 *
 * Provides a simple, unified interface to the complex
 * audio, physics, and video subsystems.
 *
 * Benefits:
 * - Client doesn't need to know about subsystem details
 * - Easy to use: just call startGame(), updateFrame(), shutdown()
 * - Changes to subsystems don't affect client code
 */
public class GameEngineFacade {
    // Complex subsystems
    private AudioSystem audio;
    private PhysicsEngine physics;
    private VideoSystem video;

    private boolean running = false;

    public GameEngineFacade() {
        // Create subsystems
        this.audio = new AudioSystem();
        this.physics = new PhysicsEngine();
        this.video = new VideoSystem();
    }

    /**
     * Simple method to start the game
     * Hides all the complex initialization
     */
    public void startGame() {
        System.out.println("\n========== STARTING GAME ENGINE ==========\n");

        // Initialize all subsystems in correct order
        video.init();
        audio.init();
        physics.init();

        // Start background music
        audio.playMusic("main_theme.mp3");

        running = true;

        System.out.println("\n========== GAME ENGINE READY ==========\n");
    }

    /**
     * Simple method to update one frame
     * Hides physics, rendering, and audio coordination
     */
    public void updateFrame(float deltaTime) {
        if (!running) return;

        // Update in correct order
        physics.step(deltaTime);
        physics.checkCollisions();

        video.beginFrame();
        video.render();
        video.endFrame();
    }

    /**
     * Simple method to play a sound effect
     */
    public void playSound(String soundName) {
        audio.playSound(soundName);
    }

    /**
     * Simple method to change background music
     */
    public void changeMusic(String musicFile) {
        audio.stopMusic();
        audio.playMusic(musicFile);
    }

    /**
     * Simple method to pause/resume
     */
    public void pauseGame() {
        running = false;
        audio.stopMusic();
        System.out.println("\n========== GAME PAUSED ==========\n");
    }

    public void resumeGame() {
        running = true;
        audio.playMusic("main_theme.mp3");
        System.out.println("\n========== GAME RESUMED ==========\n");
    }

    /**
     * Simple method to shut down everything
     * Hides complex cleanup sequence
     */
    public void shutdown() {
        System.out.println("\n========== SHUTTING DOWN ==========\n");

        running = false;

        // Cleanup in reverse order
        physics.cleanup();
        audio.cleanup();
        video.cleanup();

        System.out.println("\n========== SHUTDOWN COMPLETE ==========\n");
    }

    public boolean isRunning() {
        return running;
    }
}
```

### 3. Client Code

**`src/Main.java`**:
```java
import engine.GameEngineFacade;

/**
 * Client code using Facade Pattern
 *
 * Notice how simple this is compared to managing
 * all subsystems directly!
 */
public class Main {
    public static void main(String[] args) {
        // Create facade - hides all complexity
        GameEngineFacade engine = new GameEngineFacade();

        // Start game - one simple call
        engine.startGame();

        // Game loop - simple update call
        System.out.println("\n--- Running 3 frame updates ---\n");
        for (int i = 0; i < 3; i++) {
            System.out.println("Frame " + (i + 1) + ":");
            engine.updateFrame(0.016f);  // ~60 FPS
            System.out.println();
        }

        // Play a sound effect - simple call
        engine.playSound("sword_hit.wav");

        // Pause and resume
        engine.pauseGame();
        engine.resumeGame();

        // Shutdown - one simple call
        engine.shutdown();
    }
}
```

---

## Output

```
========== STARTING GAME ENGINE ==========

[VideoSystem] Creating window: 800x600
[VideoSystem] Initializing OpenGL context...
[VideoSystem] Loading shaders...
[VideoSystem] Setting up framebuffers...
[VideoSystem] Ready!
[AudioSystem] Initializing audio drivers...
[AudioSystem] Loading audio codecs...
[AudioSystem] Setting up audio buffer...
[AudioSystem] Ready!
[PhysicsEngine] Creating physics world...
[PhysicsEngine] Setting gravity: 9.8
[PhysicsEngine] Initializing collision detection...
[PhysicsEngine] Ready!
[AudioSystem] ♪ Playing: main_theme.mp3

========== GAME ENGINE READY ==========

--- Running 3 frame updates ---

Frame 1:
[PhysicsEngine] Physics step: 0.016s
[PhysicsEngine] Checking collisions...
[VideoSystem] Begin frame...
[VideoSystem] Rendering scene...
[VideoSystem] End frame, swap buffers

Frame 2:
[PhysicsEngine] Physics step: 0.016s
[PhysicsEngine] Checking collisions...
[VideoSystem] Begin frame...
[VideoSystem] Rendering scene...
[VideoSystem] End frame, swap buffers

Frame 3:
[PhysicsEngine] Physics step: 0.016s
[PhysicsEngine] Checking collisions...
[VideoSystem] Begin frame...
[VideoSystem] Rendering scene...
[VideoSystem] End frame, swap buffers

[AudioSystem] Playing SFX: sword_hit.wav

========== GAME PAUSED ==========

[AudioSystem] Stopping: main_theme.mp3

========== GAME RESUMED ==========

[AudioSystem] ♪ Playing: main_theme.mp3

========== SHUTTING DOWN ==========

[PhysicsEngine] Destroying physics world...
[AudioSystem] Releasing audio resources...
[VideoSystem] Destroying window...
[VideoSystem] Releasing GPU resources...

========== SHUTDOWN COMPLETE ==========
```

---

## Perbandingan: Tanpa vs Dengan Facade

### Tanpa Facade
```java
// Client harus tahu semua detail
AudioSystem audio = new AudioSystem();
audio.init();
PhysicsEngine physics = new PhysicsEngine();
physics.init();
VideoSystem video = new VideoSystem();
video.init();
audio.playMusic("theme.mp3");

// Update loop - client harus koordinasi semua
while (running) {
    physics.step(dt);
    physics.checkCollisions();
    video.beginFrame();
    video.render();
    video.endFrame();
}

// Cleanup - client harus tahu urutan yang benar
physics.cleanup();
audio.cleanup();
video.cleanup();
```

### Dengan Facade
```java
// Client hanya tahu Facade
GameEngineFacade engine = new GameEngineFacade();
engine.startGame();

// Update loop - simple
while (engine.isRunning()) {
    engine.updateFrame(dt);
}

// Cleanup - simple
engine.shutdown();
```

---

## Principle of Least Knowledge (Law of Demeter)

Facade Pattern menerapkan **Principle of Least Knowledge**:
> "Talk only to your immediate friends"

```
Tanpa Facade:
Client → AudioCodec → AudioBuffer → AudioPlayer → File

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

## Aplikasi dalam RPG Game

### Contoh: Battle System Facade

```java
public class BattleFacade {
    private BattleSystem battle;
    private AudioManager audio;
    private UIManager ui;
    private AnimationManager animations;

    // Simple interface for starting battle
    public void startBattle(Player player, Boss boss) {
        ui.showBattleScreen();
        audio.playBattleMusic();
        animations.playIntroAnimation();
        battle.initialize(player, boss);
    }

    // Simple interface for player action
    public void playerAttack() {
        animations.playAttackAnimation();
        audio.playSound("sword_swing");
        battle.processPlayerAttack();
        ui.updateHealthBars();
    }

    // Simple interface for ending battle
    public void endBattle(boolean victory) {
        if (victory) {
            audio.playVictoryFanfare();
            ui.showVictoryScreen();
        } else {
            audio.playDefeatMusic();
            ui.showDefeatScreen();
        }
        animations.playOutroAnimation();
    }
}
```

### Contoh: Save/Load Facade

```java
public class SaveGameFacade {
    private FileManager files;
    private Serializer serializer;
    private Compressor compressor;
    private Encryptor encryptor;

    // Simple save
    public void saveGame(GameState state, String slot) {
        byte[] data = serializer.serialize(state);
        byte[] compressed = compressor.compress(data);
        byte[] encrypted = encryptor.encrypt(compressed);
        files.write("save_" + slot + ".dat", encrypted);
    }

    // Simple load
    public GameState loadGame(String slot) {
        byte[] encrypted = files.read("save_" + slot + ".dat");
        byte[] compressed = encryptor.decrypt(encrypted);
        byte[] data = compressor.decompress(compressed);
        return serializer.deserialize(data);
    }
}
```

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
├── Main.java                    # Client using facade
└── engine/
    ├── GameEngineFacade.java    # FACADE
    ├── audio/
    │   └── AudioSystem.java     # Subsystem
    ├── physics/
    │   └── PhysicsEngine.java   # Subsystem
    └── video/
        └── VideoSystem.java     # Subsystem
```

---

## Testing

```bash
# Compile
javac -d bin/13-02-facade-pattern src/*.java src/**/*.java

# Run demo
java -cp bin/13-02-facade-pattern Main
```

---

## References

- **GoF**: Facade Pattern
- **Head First Design Patterns**: Chapter 7
- **Principle of Least Knowledge** (Law of Demeter)
