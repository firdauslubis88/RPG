package engine;

import engine.audio.AudioSystem;
import engine.physics.PhysicsEngine;
import engine.video.VideoSystem;

/**
 * Week 13-04: Facade Pattern (SOLUTION)
 *
 * GameEngineFacade provides a SIMPLE, UNIFIED interface to all
 * the complex game engine subsystems (Audio, Physics, Video).
 *
 * Benefits:
 * - Client only needs to know ONE class
 * - Initialization order handled internally
 * - Frame coordination handled internally
 * - Cleanup handled internally
 * - Subsystem changes don't affect client
 *
 * Principle of Least Knowledge: "Talk only to your immediate friends"
 * - Client talks to Facade
 * - Facade talks to subsystems
 * - Client doesn't know about subsystems!
 */
public class GameEngineFacade {

    // Complex subsystems hidden behind the facade
    private AudioSystem audio;
    private PhysicsEngine physics;
    private VideoSystem video;

    private boolean running = false;
    private String currentMusic = "adventure_theme.mp3";

    /**
     * Create the facade (subsystems created but not initialized)
     */
    public GameEngineFacade() {
        this.audio = new AudioSystem();
        this.physics = new PhysicsEngine();
        this.video = new VideoSystem();
    }

    /**
     * SIMPLE interface: Start the game engine
     *
     * Hides ALL the complex initialization:
     * - Video: window, context, shaders, framebuffer
     * - Audio: codec, buffer, mixer
     * - Physics: world, bounds, collision detector
     *
     * Client just calls: engine.startGame()
     */
    public void startGame() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║   GameEngineFacade: Starting Engine      ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        // Initialize video (correct order handled internally!)
        System.out.println("┌─── Initializing Video ───────────────────┐");
        video.createWindow(800, 600, "RPG Adventure");
        video.initContext();
        video.loadShaders("default_vertex.glsl", "default_fragment.glsl");
        video.createFrameBuffer(800, 600);
        System.out.println("└──────────────────────────────────────────┘\n");

        // Initialize audio (correct order handled internally!)
        System.out.println("┌─── Initializing Audio ───────────────────┐");
        audio.initCodec(44100, 16);
        audio.allocateBuffer(4096);
        audio.setupMixer(0.8f);
        System.out.println("└──────────────────────────────────────────┘\n");

        // Initialize physics (correct order handled internally!)
        System.out.println("┌─── Initializing Physics ─────────────────┐");
        physics.createWorld(0, -9.8f);
        physics.setBounds(0, 0, 800, 600);
        physics.initCollisionDetector(3);
        System.out.println("└──────────────────────────────────────────┘\n");

        // Start background music
        audio.playMusic(currentMusic);

        running = true;

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   ✓ Game Engine Ready!                   ║");
        System.out.println("╚══════════════════════════════════════════╝\n");
    }

    /**
     * SIMPLE interface: Update one frame
     *
     * Hides ALL the complex coordination:
     * - Physics step
     * - Collision detection
     * - Begin frame
     * - Render
     * - End frame
     *
     * Client just calls: engine.updateFrame(deltaTime)
     */
    public void updateFrame(float deltaTime) {
        if (!running) return;

        // Physics update
        physics.step(deltaTime);
        physics.checkCollisions();

        // Render
        video.beginFrame();
        video.render();
        video.endFrame();
    }

    /**
     * SIMPLE interface: Play a sound effect
     */
    public void playSound(String soundFile) {
        audio.playSound(soundFile);
    }

    /**
     * SIMPLE interface: Change background music
     */
    public void changeMusic(String musicFile) {
        audio.stopMusic();
        currentMusic = musicFile;
        audio.playMusic(musicFile);
    }

    /**
     * SIMPLE interface: Pause the game
     */
    public void pauseGame() {
        running = false;
        audio.stopMusic();
        System.out.println("\n[GameEngineFacade] Game paused\n");
    }

    /**
     * SIMPLE interface: Resume the game
     */
    public void resumeGame() {
        running = true;
        audio.playMusic(currentMusic);
        System.out.println("\n[GameEngineFacade] Game resumed\n");
    }

    /**
     * SIMPLE interface: Shutdown everything
     *
     * Hides ALL the complex cleanup:
     * - Stop music
     * - Cleanup physics
     * - Cleanup audio
     * - Cleanup video
     * - All in correct REVERSE order!
     *
     * Client just calls: engine.shutdown()
     */
    public void shutdown() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║   GameEngineFacade: Shutting Down        ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        running = false;

        // Cleanup in reverse order (handled internally!)
        System.out.println("┌─── Cleaning Up Physics ──────────────────┐");
        physics.cleanup();
        System.out.println("└──────────────────────────────────────────┘\n");

        System.out.println("┌─── Cleaning Up Audio ────────────────────┐");
        audio.stopMusic();
        audio.cleanup();
        System.out.println("└──────────────────────────────────────────┘\n");

        System.out.println("┌─── Cleaning Up Video ────────────────────┐");
        video.cleanup();
        System.out.println("└──────────────────────────────────────────┘\n");

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   ✓ Game Engine Shutdown Complete        ║");
        System.out.println("╚══════════════════════════════════════════╝\n");
    }

    /**
     * Check if game is running
     */
    public boolean isRunning() {
        return running;
    }
}
