import engine.audio.AudioSystem;
import engine.physics.PhysicsEngine;
import engine.video.VideoSystem;

/**
 * Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN) - Demo
 *
 * This demo shows PROBLEMS with tightly coupled subsystems:
 * 1. Client must know ALL subsystem internals
 * 2. Client must manage initialization ORDER
 * 3. Client must coordinate frame updates
 * 4. Client must handle cleanup in correct order
 * 5. Any subsystem change affects ALL clients
 * 6. Code is duplicated across multiple clients
 *
 * SOLUTION: Facade Pattern (see branch 13-04-facade-pattern)
 */
public class SubsystemDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║                                                      ║");
        System.out.println("║   WEEK 13-03: TIGHTLY COUPLED SUBSYSTEMS             ║");
        System.out.println("║   (ANTI-PATTERN DEMONSTRATION)                       ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        System.out.println("\nThis demo shows PROBLEMS with tightly coupled subsystems:\n");
        System.out.println("  1. Client knows ALL subsystem internals");
        System.out.println("  2. Client manages initialization ORDER");
        System.out.println("  3. Client coordinates frame updates");
        System.out.println("  4. Client handles cleanup order");
        System.out.println("  5. Subsystem changes affect ALL clients");

        // ════════════════════════════════════════════════════════════
        // Client creates ALL subsystem objects directly
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" CLIENT CREATES ALL SUBSYSTEMS DIRECTLY");
        System.out.println("═".repeat(60));

        System.out.println("\n// Client code must create each subsystem:");
        System.out.println("AudioSystem audio = new AudioSystem();");
        System.out.println("PhysicsEngine physics = new PhysicsEngine();");
        System.out.println("VideoSystem video = new VideoSystem();\n");

        AudioSystem audio = new AudioSystem();
        PhysicsEngine physics = new PhysicsEngine();
        VideoSystem video = new VideoSystem();

        // ════════════════════════════════════════════════════════════
        // PROBLEM: Client must know correct initialization ORDER
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" PROBLEM: Client must know CORRECT initialization order!");
        System.out.println("═".repeat(60));

        System.out.println("\n┌─────────────────────────────────────────────────┐");
        System.out.println("│ Video Subsystem (4 steps in specific order!)    │");
        System.out.println("└─────────────────────────────────────────────────┘\n");

        video.createWindow(800, 600, "My RPG Game");
        video.initContext();
        video.loadShaders("vertex.glsl", "fragment.glsl");
        video.createFrameBuffer(800, 600);

        System.out.println("\n┌─────────────────────────────────────────────────┐");
        System.out.println("│ Audio Subsystem (3 steps in specific order!)    │");
        System.out.println("└─────────────────────────────────────────────────┘\n");

        audio.initCodec(44100, 16);
        audio.allocateBuffer(4096);
        audio.setupMixer(0.8f);
        audio.playMusic("adventure_theme.mp3");

        System.out.println("\n┌─────────────────────────────────────────────────┐");
        System.out.println("│ Physics Subsystem (3 steps in specific order!)  │");
        System.out.println("└─────────────────────────────────────────────────┘\n");

        physics.createWorld(0, -9.8f);
        physics.setBounds(0, 0, 800, 600);
        physics.initCollisionDetector(3);

        // ════════════════════════════════════════════════════════════
        // Show what happens with WRONG order
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" DEMO: What happens if order is WRONG?");
        System.out.println("═".repeat(60));

        System.out.println("\n// Creating a new video system with WRONG order:\n");

        VideoSystem badVideo = new VideoSystem();
        System.out.println("// Oops! Trying to load shaders before creating window...\n");
        badVideo.loadShaders("vertex.glsl", "fragment.glsl");  // ERROR!
        System.out.println("\n// Client made a mistake - hard to debug!\n");

        // ════════════════════════════════════════════════════════════
        // PROBLEM: Client must coordinate game loop manually
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" PROBLEM: Client coordinates game loop manually");
        System.out.println("═".repeat(60));

        System.out.println("\n// Every frame, client must call ALL systems:\n");

        for (int frame = 1; frame <= 2; frame++) {
            System.out.println("┌─── Frame " + frame + " ───────────────────────────────────────┐");

            // Client manually coordinates all subsystems
            physics.step(0.016f);
            physics.checkCollisions();
            video.beginFrame();
            video.render();
            video.endFrame();

            System.out.println("└────────────────────────────────────────────────────┘\n");
        }

        // ════════════════════════════════════════════════════════════
        // PROBLEM: Client must handle cleanup in correct order
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" PROBLEM: Client must cleanup in CORRECT order!");
        System.out.println("═".repeat(60));

        System.out.println("\n// Cleanup must be in REVERSE order of initialization:\n");

        physics.cleanup();
        audio.stopMusic();
        audio.cleanup();
        video.cleanup();

        // ════════════════════════════════════════════════════════════
        // Summary
        // ════════════════════════════════════════════════════════════
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                  PROBLEMS IDENTIFIED                 ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Client Complexity                                 ║");
        System.out.println("║    Must know internals of 3+ subsystems!             ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Initialization Order                              ║");
        System.out.println("║    10+ method calls in specific sequence!            ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Coordination Burden                               ║");
        System.out.println("║    Client coordinates all systems every frame!       ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Cleanup Order                                     ║");
        System.out.println("║    Must cleanup in reverse order or crash!           ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Tight Coupling                                    ║");
        System.out.println("║    Any subsystem change breaks ALL clients!          ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Code Duplication                                  ║");
        System.out.println("║    Same init code in MainMenu, LoadGame, etc.!       ║");
        System.out.println("║                                                      ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  SOLUTION: Facade Pattern                            ║");
        System.out.println("║  See branch: 13-04-facade-pattern                    ║");
        System.out.println("║                                                      ║");
        System.out.println("║  With Facade:                                        ║");
        System.out.println("║    GameEngineFacade engine = new GameEngineFacade(); ║");
        System.out.println("║    engine.startGame();  // ONE call!                 ║");
        System.out.println("║    engine.updateFrame(deltaTime);                    ║");
        System.out.println("║    engine.shutdown();   // ONE call!                 ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
}
