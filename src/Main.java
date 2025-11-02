import entities.NPC;
import entities.Coin;
import utils.GridRenderer;
import java.util.Random;

/**
 * ❌ INTENTIONALLY BAD DESIGN - FOR EDUCATIONAL DEMONSTRATION ONLY
 *
 * This is a monolithic game loop with NO separation between update and render.
 * All game logic, rendering, and state management is mixed in one giant main() method.
 *
 * PROBLEMS DEMONSTRATED:
 * 1. Frame rate coupling - render speed affects game logic speed
 * 2. Untestable - cannot unit test logic without rendering
 * 3. Unmaintainable - all code in one 150+ line method
 * 4. No separation of concerns - update/draw/collision all mixed
 *
 * This is the "before" version showing WHY we need proper game loop architecture.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // ❌ PROBLEM: All state as local variables in main()
        NPC npc = new NPC(0, 5);
        Random random = new Random();
        Coin coin = new Coin(random.nextInt(GridRenderer.getGridWidth()), 0);
        int score = 0;
        int frameCount = 0;

        System.out.println("=================================");
        System.out.println("  DUNGEON ESCAPE - NO GAME LOOP");
        System.out.println("=================================");
        System.out.println("This version has NO separation!");
        System.out.println("Watch how slow it becomes...");
        System.out.println("=================================\n");
        Thread.sleep(2000);

        // ❌ PROBLEM: Infinite loop with everything inside
        while (true) {
            frameCount++;

            // ❌ PROBLEM: Clear screen mixed with game logic
            GridRenderer.clearScreen();

            // ❌ PROBLEM: Update NPC position
            npc.moveRight();

            // ❌ PROBLEM: Artificial rendering delay simulating real-world GPU bottleneck
            // In production: complex 3D rendering, particle effects, shadows, etc.
            Thread.sleep(100);

            // ❌ PROBLEM: Wrap NPC at edge (more logic mixed in)
            npc.wrapAtEdge(GridRenderer.getGridWidth());

            // ❌ PROBLEM: Update coin position
            coin.fall();

            // ❌ PROBLEM: Another rendering delay (still mixed with update logic!)
            Thread.sleep(100);

            // ❌ PROBLEM: Check if coin fell off screen
            if (coin.isOffScreen(GridRenderer.getGridHeight())) {
                coin.respawn(random.nextInt(GridRenderer.getGridWidth()));
            }

            // ❌ PROBLEM: Collision detection mixed with rendering
            if (npc.getX() == coin.getX() && npc.getY() == coin.getY()) {
                // ❌ PROBLEM: Score update mixed in
                score += 10;

                // ❌ PROBLEM: Game logic (respawn) mixed in
                coin.respawn(random.nextInt(GridRenderer.getGridWidth()));

                // ❌ PROBLEM: Yet another rendering delay
                Thread.sleep(100);
            }

            // ❌ PROBLEM: Create and draw entire grid (rendering mixed with logic)
            char[][] grid = GridRenderer.createEmptyGrid();
            GridRenderer.drawEntity(grid, 'N', npc.getX(), npc.getY());
            GridRenderer.drawEntity(grid, 'C', coin.getX(), coin.getY());
            GridRenderer.drawGrid(grid);

            // ❌ PROBLEM: UI rendering mixed in
            System.out.println("\n╔════════════════════════════════╗");
            System.out.println("║       GAME STATE               ║");
            System.out.println("╠════════════════════════════════╣");
            System.out.println("  Score: " + score + " points");
            System.out.println("  Frame: " + frameCount);
            System.out.println("  NPC: (" + npc.getX() + ", " + npc.getY() + ")  Coin: (" + coin.getX() + ", " + coin.getY() + ")");
            System.out.println("╚════════════════════════════════╝");

            // ❌ PROBLEM: More rendering delay
            Thread.sleep(100);

            // ❌ PROBLEM: Fixed sleep, no delta time
            // If rendering is slow, game logic becomes slow too!
            Thread.sleep(100);

            // ❌ PROBLEM: Total sleep per frame = 500ms!
            // Expected game speed: 10 updates/sec (with 100ms sleep)
            // Actual game speed: ~2 updates/sec (80% slower!)

            // Stop after some frames for demo purposes
            if (frameCount >= 50) {
                System.out.println("\n=================================");
                System.out.println("Demo ended after 50 frames");
                System.out.println("Notice how SLOW everything was?");
                System.out.println("=================================");
                System.out.println("\nPROBLEMS OBSERVED:");
                System.out.println("1. Expected ~10 fps, got ~2 fps (80% slower!)");
                System.out.println("2. Every Thread.sleep() affects GAME LOGIC speed");
                System.out.println("3. Cannot test collision without rendering");
                System.out.println("4. All code in one giant method (150+ lines)");
                System.out.println("5. Impossible to maintain or extend");
                System.out.println("\nNext: See 09-01-with-game-loop for solution!");
                break;
            }
        }

        // ❌ PROBLEM: No clean shutdown, no resource management
        // ❌ PROBLEM: No way to pause/resume
        // ❌ PROBLEM: No way to change game speed without changing render speed
        // ❌ PROBLEM: Adding more entities = exponential slowdown
    }

    // ❌ PROBLEM: No helper methods, everything in main()
    // ❌ PROBLEM: No classes for game state management
    // ❌ PROBLEM: No abstraction, no encapsulation
    // ❌ PROBLEM: Violates Single Responsibility Principle
    // ❌ PROBLEM: Violates Open/Closed Principle
    // ❌ PROBLEM: Impossible to unit test
}
