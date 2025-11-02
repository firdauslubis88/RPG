import entities.Coin;
import utils.GridRenderer;

/**
 * ✅ SOLUTION: Proper game loop with separated update and draw.
 *
 * This is the core game loop following industry-standard architecture:
 * - update(): Pure logic, no rendering
 * - draw(): Pure rendering, no logic
 * - Delta time: Frame-rate independent movement
 * - Frame rate control: Stable 60 FPS target
 *
 * This pattern is used in Unity, Unreal, LibGDX, and all professional game engines.
 */
public class GameEngine {
    private final GameLogic gameLogic;
    private boolean running;

    // Frame rate control
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;  // Nanoseconds per frame

    // Performance tracking
    private long updateTime = 0;
    private long drawTime = 0;
    private int currentFPS = 0;

    /**
     * Creates game engine with game logic.
     */
    public GameEngine() {
        this.gameLogic = new GameLogic();
        this.running = false;
    }

    /**
     * ✅ SOLUTION: Main game loop with proper separation.
     *
     * This is THE pattern for game development:
     * 1. Calculate delta time
     * 2. Update game logic (no rendering)
     * 3. Draw everything (no logic)
     * 4. Control frame rate
     */
    public void start() {
        running = true;
        long lastTime = System.nanoTime();
        long lastFPSTime = System.nanoTime();
        int frames = 0;

        System.out.println("=================================");
        System.out.println("  DUNGEON ESCAPE - WITH GAME LOOP");
        System.out.println("=================================");
        System.out.println("✅ Proper separation of concerns");
        System.out.println("✅ Frame-rate independent movement");
        System.out.println("✅ Testable game logic");
        System.out.println("=================================\n");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        while (running) {
            long cycleStart = System.nanoTime();

            // ✅ SOLUTION: Calculate delta time
            long currentTime = System.nanoTime();
            float delta = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            // ✅ SOLUTION: Update phase (pure logic)
            long updateStart = System.nanoTime();
            update(delta);
            updateTime = (System.nanoTime() - updateStart) / 1_000_000;  // Convert to milliseconds

            // ✅ SOLUTION: Draw phase (pure rendering)
            long drawStart = System.nanoTime();
            draw();
            drawTime = (System.nanoTime() - drawStart) / 1_000_000;  // Convert to milliseconds

            // FPS calculation
            frames++;
            if (currentTime - lastFPSTime >= 1_000_000_000) {
                currentFPS = frames;
                frames = 0;
                lastFPSTime = currentTime;
            }

            // ✅ SOLUTION: Frame rate control
            sync(cycleStart);

            // Stop after demo frames
            if (gameLogic.getFrameCount() >= 200) {
                running = false;
            }
        }

        System.out.println("\n=================================");
        System.out.println("Demo ended after 200 frames");
        System.out.println("=================================");
        System.out.println("\n✅ IMPROVEMENTS ACHIEVED:");
        System.out.println("1. Smooth 60 FPS (not 2 FPS!)");
        System.out.println("2. Update and draw separated");
        System.out.println("3. Frame-rate independent movement");
        System.out.println("4. Clean, maintainable code");
        System.out.println("5. Fully testable logic");
        System.out.println("\nCompare with 09-00 to see the difference!");
    }

    /**
     * ✅ SOLUTION: Pure logic update - NO rendering!
     *
     * @param delta Time since last frame in seconds
     */
    private void update(float delta) {
        gameLogic.updateNPC(delta);
        gameLogic.updateCoins(delta);
        gameLogic.checkCollisions();
        gameLogic.incrementFrame();

        // ✅ NO System.out.println here!
        // ✅ NO rendering code here!
        // ✅ PURE LOGIC ONLY!
    }

    /**
     * ✅ SOLUTION: Pure rendering - NO logic!
     */
    private void draw() {
        // Clear screen once at the start
        GridRenderer.clearScreen();

        // Create and populate grid
        char[][] grid = GridRenderer.createEmptyGrid();
        GridRenderer.drawEntity(grid, 'N', gameLogic.getNPCX(), gameLogic.getNPCY());

        for (Coin coin : gameLogic.getCoins()) {
            GridRenderer.drawEntity(grid, 'C', (int)coin.getX(), (int)coin.getY());
        }

        // Draw grid
        GridRenderer.drawGrid(grid);

        // Draw HUD
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       GAME STATE                       ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("  Score: %d points%n", gameLogic.getScore());
        System.out.printf("  Frame: %d%n", gameLogic.getFrameCount());
        System.out.printf("  FPS: %d | Update: %dms | Draw: %dms%n", currentFPS, updateTime, drawTime);
        System.out.println("╚════════════════════════════════════════╝");

        // ✅ NO game logic here!
        // ✅ NO collision detection here!
        // ✅ PURE RENDERING ONLY!
    }

    /**
     * ✅ SOLUTION: Frame rate control for consistent timing.
     *
     * @param cycleStart The start time of this frame cycle
     */
    private void sync(long cycleStart) {
        long elapsedTime = System.nanoTime() - cycleStart;
        long waitTime = OPTIMAL_TIME - elapsedTime;

        if (waitTime > 0) {
            try {
                Thread.sleep(waitTime / 1_000_000, (int)(waitTime % 1_000_000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        // If we're running slow, don't sleep (frame drop)
    }
}
