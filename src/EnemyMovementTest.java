import world.DungeonMap;

import entities.NPC;
import entities.GameManager;
import obstacles.Goblin;
import obstacles.Wolf;
import utils.GridRenderer;

/**
 * Visual test to demonstrate enemy movement and wall collision
 * - Goblin: Patrol left-right, reverses at walls
 * - Wolf: Chase NPC, navigates around walls
 */
public class EnemyMovementTest {
    public static void main(String[] args) throws InterruptedException {
        GameManager.resetInstance();
        GameManager manager = GameManager.getInstance();

        System.out.println("========================================");
        System.out.println("   ENEMY MOVEMENT & WALL COLLISION TEST");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Legend:");
        System.out.println("  N = NPC (you)");
        System.out.println("  G = Goblin (patrols left-right)");
        System.out.println("  W = Wolf (chases NPC)");
        System.out.println("  # = Wall (impassable)");
        System.out.println("  . = Floor (walkable)");
        System.out.println();
        System.out.println("This test shows:");
        System.out.println("  1. Goblin patrolling and reversing at walls");
        System.out.println("  2. Wolf chasing NPC and respecting walls");
        System.out.println();
        Thread.sleep(3000);

        // Create NPC at position (10, 10)
        NPC npc = new NPC();
        npc.tryMove(10, 10);

        // Create Goblin at (7, 10) - will patrol horizontally
        // Walls: check DungeonMap to ensure path is clear
        Goblin goblin = new Goblin(7, 10);

        // Create Wolf at (15, 10) - will chase NPC
        Wolf wolf = new Wolf(15, 10);
        wolf.setTarget(npc);

        // Simulate game loop
        float delta = 0.016f;  // 60 FPS
        int frames = 0;
        int maxFrames = 300;  // 5 seconds at 60 FPS

        System.out.println("Starting simulation (5 seconds)...\n");
        Thread.sleep(1000);

        while (frames < maxFrames) {
            // Update enemies
            goblin.update(delta);
            wolf.update(delta);

            // Render every 30 frames (0.5 seconds)
            if (frames % 30 == 0) {
                GridRenderer.clearScreen();
                drawMap(npc, goblin, wolf);
                printStats(manager, goblin, wolf, npc, frames);

                // Check collisions
                checkCollision(npc, goblin, wolf, manager);
            }

            frames++;
            Thread.sleep(16);  // Simulate 60 FPS
        }

        System.out.println("\n========================================");
        System.out.println("   SIMULATION COMPLETE!");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Observations:");
        System.out.println("  - Goblin patrols left-right");
        System.out.println("  - Goblin reverses direction at walls");
        System.out.println("  - Wolf moves towards NPC");
        System.out.println("  - Wolf respects walls (doesn't pass through)");
        System.out.println();
        System.out.println("Final State:");
        System.out.println("  Score: " + manager.getScore());
        System.out.println("  HP: " + manager.getHp() + " / 100");
    }

    private static void drawMap(NPC npc, Goblin goblin, Wolf wolf) {
        // Draw a map section (20x15 grid) centered around position (10, 10)
        System.out.println("┌────────────────────────────────────────┐");
        for (int y = 3; y < 18; y++) {
            System.out.print("│");
            for (int x = 3; x < 23; x++) {
                char c = '.';

                // Check map tile
                char mapTile = DungeonMap.getTile(x, y);
                if (mapTile == '#') {
                    c = '#';
                }

                // Check if NPC is at this position
                if (npc.getX() == x && npc.getY() == y) {
                    c = 'N';
                }
                // Check if goblin is at this position
                else if (goblin.getX() == x && goblin.getY() == y && goblin.isActive()) {
                    c = 'G';
                }
                // Check if wolf is at this position
                else if (wolf.getX() == x && wolf.getY() == y && wolf.isActive()) {
                    c = 'W';
                }

                System.out.print(c + " ");
            }
            System.out.println("│");
        }
        System.out.println("└────────────────────────────────────────┘");
    }

    private static void printStats(GameManager manager, Goblin goblin, Wolf wolf, NPC npc, int frames) {
        float seconds = frames / 60.0f;
        System.out.println();
        System.out.println("═══════════════════════════════════════");
        System.out.println("  Time: " + String.format("%.1f", seconds) + "s");
        System.out.println("═══════════════════════════════════════");
        System.out.println("  NPC:    (" + npc.getX() + ", " + npc.getY() + ")");
        System.out.println("  Goblin: (" + goblin.getX() + ", " + goblin.getY() + ") " + (goblin.isActive() ? "ACTIVE" : "DEFEATED"));
        System.out.println("  Wolf:   (" + wolf.getX() + ", " + wolf.getY() + ") " + (wolf.isActive() ? "ACTIVE" : "DEFEATED"));
        System.out.println("───────────────────────────────────────");
        System.out.println("  Score: " + manager.getScore());
        System.out.println("  HP: " + manager.getHp() + " / 100");
        System.out.println("═══════════════════════════════════════");
    }

    private static void checkCollision(NPC npc, Goblin goblin, Wolf wolf, GameManager manager) {
        int npcX = npc.getX();
        int npcY = npc.getY();

        // Check Goblin collision
        if (npcX == goblin.getX() && npcY == goblin.getY() && goblin.isActive()) {
            manager.takeDamage(goblin.getDamage());
            goblin.setActive(false);
            System.out.println("\n>>> GOBLIN HIT! -" + goblin.getDamage() + " HP");
        }

        // Check Wolf collision
        if (npcX == wolf.getX() && npcY == wolf.getY() && wolf.isActive()) {
            manager.takeDamage(wolf.getDamage());
            wolf.setActive(false);
            System.out.println("\n>>> WOLF HIT! -" + wolf.getDamage() + " HP");
        }
    }
}
