# Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN)

**Branch**: `13-03-tightly-coupled`

**Compilation**: `javac -d bin/13-03-tightly-coupled src/*.java src/**/*.java`

**Run**: `java -cp bin/13-03-tightly-coupled SubsystemDemo`

---

## Overview

Branch ini mendemonstrasikan **masalah** yang terjadi ketika client code berinteraksi langsung dengan banyak subsystem kompleks. Client harus mengetahui detail internal setiap subsystem, menyebabkan **tight coupling** dan **complex client code**.

---

## Masalah: Client Tightly Coupled ke Subsystems

### Client Harus Tahu Semua Detail

```java
public class GameWithoutFacade {
    public void startGame() {
        // ═══════════════════════════════════════════════
        // Client harus tahu cara initialize AudioSystem
        // ═══════════════════════════════════════════════
        AudioCodec codec = new AudioCodec();
        codec.setSampleRate(44100);
        codec.setBitDepth(16);
        codec.initialize();

        AudioBuffer buffer = new AudioBuffer(1024);
        buffer.setCodec(codec);
        buffer.allocate();

        AudioMixer mixer = new AudioMixer();
        mixer.setBuffer(buffer);
        mixer.setMasterVolume(0.8f);

        AudioPlayer player = new AudioPlayer();
        player.setMixer(mixer);
        player.loadFile("music.mp3");

        // ═══════════════════════════════════════════════
        // Client harus tahu cara initialize PhysicsEngine
        // ═══════════════════════════════════════════════
        PhysicsWorld world = new PhysicsWorld();
        world.setGravity(new Vector2(0, -9.8f));
        world.setBounds(0, 0, 800, 600);

        CollisionDetector detector = new CollisionDetector();
        detector.setWorld(world);
        detector.setCollisionLayers(3);

        RigidBodyManager bodyManager = new RigidBodyManager();
        bodyManager.setWorld(world);
        bodyManager.setDetector(detector);

        // ═══════════════════════════════════════════════
        // Client harus tahu cara initialize VideoSystem
        // ═══════════════════════════════════════════════
        Window window = new Window(800, 600, "Game");
        window.create();

        GLContext context = new GLContext();
        context.setWindow(window);
        context.initialize();

        ShaderManager shaders = new ShaderManager();
        shaders.setContext(context);
        shaders.loadShader("vertex.glsl");
        shaders.loadShader("fragment.glsl");
        shaders.compile();

        FrameBuffer frameBuffer = new FrameBuffer();
        frameBuffer.setContext(context);
        frameBuffer.setSize(800, 600);

        Renderer renderer = new Renderer();
        renderer.setContext(context);
        renderer.setShaderManager(shaders);
        renderer.setFrameBuffer(frameBuffer);

        // ═══════════════════════════════════════════════
        // Client harus koordinasi semua subsystems
        // ═══════════════════════════════════════════════
        player.play();
        world.start();
        renderer.begin();

        // Game loop
        while (running) {
            world.step(deltaTime);
            detector.checkCollisions();
            bodyManager.updateBodies();

            renderer.beginFrame();
            renderer.render();
            renderer.endFrame();

            mixer.update();
        }

        // Cleanup (harus tahu urutan yang benar!)
        renderer.cleanup();
        shaders.cleanup();
        context.cleanup();
        window.destroy();
        bodyManager.cleanup();
        detector.cleanup();
        world.cleanup();
        player.stop();
        mixer.cleanup();
        buffer.deallocate();
        codec.cleanup();
    }
}
```

---

## Masalah yang Teridentifikasi

### 1. Client Complexity
```
Client harus tahu:
├── AudioSystem
│   ├── AudioCodec (sample rate, bit depth)
│   ├── AudioBuffer (size, allocation)
│   ├── AudioMixer (volume, channels)
│   └── AudioPlayer (file loading)
├── PhysicsEngine
│   ├── PhysicsWorld (gravity, bounds)
│   ├── CollisionDetector (layers)
│   └── RigidBodyManager (bodies)
└── VideoSystem
    ├── Window (size, title)
    ├── GLContext (OpenGL setup)
    ├── ShaderManager (GLSL files)
    ├── FrameBuffer (render target)
    └── Renderer (draw calls)

Total: 12+ classes yang harus dipahami client!
```

### 2. Tight Coupling
```java
// Client langsung bergantung pada SEMUA subsystem classes
import audio.AudioCodec;
import audio.AudioBuffer;
import audio.AudioMixer;
import audio.AudioPlayer;
import physics.PhysicsWorld;
import physics.CollisionDetector;
import physics.RigidBodyManager;
import video.Window;
import video.GLContext;
import video.ShaderManager;
import video.FrameBuffer;
import video.Renderer;

// Perubahan di SATU subsystem = perubahan di client!
```

### 3. Code Duplication
```java
// Setiap tempat yang butuh game engine harus copy-paste initialization!
class MainMenu {
    void startGame() {
        // Copy-paste 50+ lines of initialization...
    }
}

class LoadGame {
    void continueGame() {
        // Copy-paste 50+ lines of initialization...
    }
}

class MultiplayerLobby {
    void joinGame() {
        // Copy-paste 50+ lines of initialization...
    }
}
```

### 4. Difficult Maintenance
```java
// Jika AudioCodec API berubah:
// - Client di MainMenu harus diubah
// - Client di LoadGame harus diubah
// - Client di MultiplayerLobby harus diubah
// - ... dan semua tempat lain!
```

### 5. Initialization Order Bugs
```java
// Urutan initialization penting! Mudah salah:
renderer.begin();      // ✗ Error! Context belum ready
context.initialize();  // Seharusnya ini dulu

// Urutan cleanup juga penting!
context.cleanup();    // ✗ Error! Renderer masih pakai context
renderer.cleanup();   // Seharusnya ini dulu
```

---

## File Structure

```
src/
├── SubsystemDemo.java          # Demo client (complex!)
├── audio/
│   ├── AudioCodec.java
│   ├── AudioBuffer.java
│   ├── AudioMixer.java
│   └── AudioPlayer.java
├── physics/
│   ├── PhysicsWorld.java
│   ├── CollisionDetector.java
│   └── RigidBodyManager.java
└── video/
    ├── Window.java
    ├── GLContext.java
    ├── ShaderManager.java
    ├── FrameBuffer.java
    └── Renderer.java
```

---

## Implementasi (Simplified Demo)

### AudioSystem.java
```java
package engine.audio;

/**
 * Complex audio subsystem (simplified for demo)
 * In real implementation: codec, buffer, mixer, player
 */
public class AudioSystem {
    private boolean codecInitialized = false;
    private boolean bufferAllocated = false;
    private boolean mixerReady = false;

    // Client must know to call these in order!
    public void initCodec(int sampleRate, int bitDepth) {
        System.out.println("[AudioCodec] Initializing: " + sampleRate + "Hz, " + bitDepth + "bit");
        codecInitialized = true;
    }

    public void allocateBuffer(int size) {
        if (!codecInitialized) {
            System.out.println("[AudioBuffer] ERROR: Codec not initialized!");
            return;
        }
        System.out.println("[AudioBuffer] Allocating " + size + " bytes");
        bufferAllocated = true;
    }

    public void setupMixer(float volume) {
        if (!bufferAllocated) {
            System.out.println("[AudioMixer] ERROR: Buffer not allocated!");
            return;
        }
        System.out.println("[AudioMixer] Setting volume: " + (volume * 100) + "%");
        mixerReady = true;
    }

    public void playMusic(String file) {
        if (!mixerReady) {
            System.out.println("[AudioPlayer] ERROR: Mixer not ready!");
            return;
        }
        System.out.println("[AudioPlayer] ♪ Playing: " + file);
    }

    public void stopMusic() {
        System.out.println("[AudioPlayer] Music stopped");
    }

    public void cleanup() {
        System.out.println("[AudioSystem] Cleaning up audio resources...");
        mixerReady = false;
        bufferAllocated = false;
        codecInitialized = false;
    }
}
```

### PhysicsEngine.java
```java
package engine.physics;

/**
 * Complex physics subsystem (simplified for demo)
 * In real implementation: world, bodies, collision detection
 */
public class PhysicsEngine {
    private boolean worldCreated = false;
    private boolean detectorReady = false;

    public void createWorld(float gravityX, float gravityY) {
        System.out.println("[PhysicsWorld] Creating world with gravity: (" + gravityX + ", " + gravityY + ")");
        worldCreated = true;
    }

    public void setBounds(int x, int y, int width, int height) {
        if (!worldCreated) {
            System.out.println("[PhysicsWorld] ERROR: World not created!");
            return;
        }
        System.out.println("[PhysicsWorld] Bounds: " + width + "x" + height);
    }

    public void initCollisionDetector(int layers) {
        if (!worldCreated) {
            System.out.println("[CollisionDetector] ERROR: World not created!");
            return;
        }
        System.out.println("[CollisionDetector] Initialized with " + layers + " layers");
        detectorReady = true;
    }

    public void step(float deltaTime) {
        if (!worldCreated) return;
        System.out.println("[PhysicsEngine] Step: " + deltaTime + "s");
    }

    public void checkCollisions() {
        if (!detectorReady) return;
        System.out.println("[CollisionDetector] Checking collisions...");
    }

    public void cleanup() {
        System.out.println("[PhysicsEngine] Destroying physics world...");
        detectorReady = false;
        worldCreated = false;
    }
}
```

### VideoSystem.java
```java
package engine.video;

/**
 * Complex video subsystem (simplified for demo)
 * In real implementation: window, context, shaders, framebuffer, renderer
 */
public class VideoSystem {
    private boolean windowCreated = false;
    private boolean contextReady = false;
    private boolean shadersLoaded = false;

    public void createWindow(int width, int height, String title) {
        System.out.println("[Window] Creating: " + title + " (" + width + "x" + height + ")");
        windowCreated = true;
    }

    public void initContext() {
        if (!windowCreated) {
            System.out.println("[GLContext] ERROR: Window not created!");
            return;
        }
        System.out.println("[GLContext] Initializing OpenGL context...");
        contextReady = true;
    }

    public void loadShaders(String vertex, String fragment) {
        if (!contextReady) {
            System.out.println("[ShaderManager] ERROR: Context not ready!");
            return;
        }
        System.out.println("[ShaderManager] Loading: " + vertex + ", " + fragment);
        shadersLoaded = true;
    }

    public void createFrameBuffer(int width, int height) {
        if (!contextReady) {
            System.out.println("[FrameBuffer] ERROR: Context not ready!");
            return;
        }
        System.out.println("[FrameBuffer] Created: " + width + "x" + height);
    }

    public void beginFrame() {
        if (!shadersLoaded) return;
        System.out.println("[Renderer] Begin frame...");
    }

    public void render() {
        System.out.println("[Renderer] Rendering scene...");
    }

    public void endFrame() {
        System.out.println("[Renderer] End frame, swap buffers");
    }

    public void cleanup() {
        System.out.println("[VideoSystem] Destroying window and releasing GPU resources...");
        shadersLoaded = false;
        contextReady = false;
        windowCreated = false;
    }
}
```

### SubsystemDemo.java
```java
import engine.audio.AudioSystem;
import engine.physics.PhysicsEngine;
import engine.video.VideoSystem;

/**
 * ANTI-PATTERN: Client tightly coupled to all subsystems
 *
 * Problems demonstrated:
 * 1. Client must know internal details of each subsystem
 * 2. Client must manage initialization order
 * 3. Client must coordinate all subsystems
 * 4. Changes in any subsystem affect client
 */
public class SubsystemDemo {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  WEEK 13-03: TIGHTLY COUPLED SUBSYSTEMS    ║");
        System.out.println("║  (ANTI-PATTERN DEMONSTRATION)              ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.println("\nPROBLEM: Client must manage all subsystem details!\n");

        // ═══════════════════════════════════════════════════════════
        // Client creates and manages ALL subsystem objects directly
        // ═══════════════════════════════════════════════════════════
        AudioSystem audio = new AudioSystem();
        PhysicsEngine physics = new PhysicsEngine();
        VideoSystem video = new VideoSystem();

        System.out.println("═".repeat(50));
        System.out.println("INITIALIZATION (Client must know correct order!)");
        System.out.println("═".repeat(50) + "\n");

        // Video initialization (client must know order!)
        System.out.println("--- Video Subsystem ---");
        video.createWindow(800, 600, "My Game");
        video.initContext();
        video.loadShaders("vertex.glsl", "fragment.glsl");
        video.createFrameBuffer(800, 600);

        // Audio initialization (client must know order!)
        System.out.println("\n--- Audio Subsystem ---");
        audio.initCodec(44100, 16);
        audio.allocateBuffer(1024);
        audio.setupMixer(0.8f);
        audio.playMusic("background.mp3");

        // Physics initialization (client must know order!)
        System.out.println("\n--- Physics Subsystem ---");
        physics.createWorld(0, -9.8f);
        physics.setBounds(0, 0, 800, 600);
        physics.initCollisionDetector(3);

        // ═══════════════════════════════════════════════════════════
        // Game loop - client coordinates everything manually
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(50));
        System.out.println("GAME LOOP (Client coordinates all subsystems)");
        System.out.println("═".repeat(50) + "\n");

        for (int frame = 1; frame <= 2; frame++) {
            System.out.println("--- Frame " + frame + " ---");
            physics.step(0.016f);
            physics.checkCollisions();
            video.beginFrame();
            video.render();
            video.endFrame();
            System.out.println();
        }

        // ═══════════════════════════════════════════════════════════
        // Cleanup - client must know correct order (reverse!)
        // ═══════════════════════════════════════════════════════════
        System.out.println("═".repeat(50));
        System.out.println("CLEANUP (Client must know reverse order!)");
        System.out.println("═".repeat(50) + "\n");

        physics.cleanup();
        audio.stopMusic();
        audio.cleanup();
        video.cleanup();

        // Summary
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  PROBLEMS IDENTIFIED                       ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║  ✗ Client knows ALL subsystem internals    ║");
        System.out.println("║  ✗ Client manages initialization order     ║");
        System.out.println("║  ✗ Client coordinates frame updates        ║");
        System.out.println("║  ✗ Client handles cleanup sequence         ║");
        System.out.println("║  ✗ Any subsystem change affects client     ║");
        System.out.println("║  ✗ Code duplicated if multiple clients     ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║  SOLUTION: Facade Pattern                  ║");
        System.out.println("║  See branch: 13-04-facade-pattern          ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }
}
```

---

## Output Demo

```
╔════════════════════════════════════════════╗
║  WEEK 13-03: TIGHTLY COUPLED SUBSYSTEMS    ║
║  (ANTI-PATTERN DEMONSTRATION)              ║
╚════════════════════════════════════════════╝

PROBLEM: Client must manage all subsystem details!

══════════════════════════════════════════════════
INITIALIZATION (Client must know correct order!)
══════════════════════════════════════════════════

--- Video Subsystem ---
[Window] Creating: My Game (800x600)
[GLContext] Initializing OpenGL context...
[ShaderManager] Loading: vertex.glsl, fragment.glsl
[FrameBuffer] Created: 800x600

--- Audio Subsystem ---
[AudioCodec] Initializing: 44100Hz, 16bit
[AudioBuffer] Allocating 1024 bytes
[AudioMixer] Setting volume: 80.0%
[AudioPlayer] ♪ Playing: background.mp3
...
```

---

## Solusi

Masalah ini diselesaikan dengan **Facade Pattern** di branch `13-04-facade-pattern`:
- Satu interface sederhana (`GameEngineFacade`)
- Client hanya tahu `startGame()`, `updateFrame()`, `shutdown()`
- Detail subsystem tersembunyi di balik Facade
- Perubahan subsystem tidak mempengaruhi client

Lihat: [Week 13-04: Facade Pattern](./week-13-04-facade-pattern.md)
