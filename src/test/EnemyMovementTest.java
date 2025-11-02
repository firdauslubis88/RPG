package test;

import world.DungeonMap;
import entities.NPC;
import entities.GameManager;
import obstacles.Goblin;
import obstacles.Wolf;

/**
 * Visual test to demonstrate enemy movement and wall collision
 * - Goblin: Patrol left-right, reverses at walls
 * - Wolf: Chase NPC, tries to reach NPC position, navigates around walls
 * - NPC: Moves randomly to trigger Wolf chase behavior
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
        System.out.println("  3. NPC moving randomly (Wolf will chase)");
        System.out.println();
        Thread.sleep(3000);

        // Create NPC at position (10, 10)
        NPC npc = new NPC();
        npc.tryMove(10, 10);

        // Create Goblin at (7, 10) - will patrol horizontally
        Goblin goblin = new Goblin(7, 10);

        // Create Wolf at (14, 10) - will chase NPC (4 tiles away, within detection range)
        Wolf wolf = new Wolf(14, 10);
        wolf.setTarget(npc);

        // Simulate game loop
        float delta = 0.016f;  // 60 FPS
        int frames = 0;
        int maxFrames = 600;  // 10 seconds at 60 FPS

        // NPC movement timer
        float npcMoveTimer = 0;
        float npcMoveInterval = 0.5f;  // NPC moves every 0.5 seconds
        java.util.Random random = new java.util.Random();

        System.out.println("Starting simulation (10 seconds)...\n");
        Thread.sleep(1000);

        // Clear screen once at start
        clearScreen();

        while (frames < maxFrames) {
            // Update NPC (random movement)
            npcMoveTimer += delta;
            if (npcMoveTimer >= npcMoveInterval) {
                npcMoveTimer = 0;

                // Try random movement
                int direction = random.nextInt(4);  // 0=up, 1=right, 2=down, 3=left
                int newX = npc.getX();
                int newY = npc.getY();

                switch(direction) {
                    case 0: newY--; break;  // Up
                    case 1: newX++; break;  // Right
                    case 2: newY++; break;  // Down
                    case 3: newX--; break;  // Left
                }

                // Only move if walkable
                if (DungeonMap.isWalkable(newX, newY)) {
                    npc.tryMove(newX, newY);
                }
            }

            // Update enemies
            goblin.update(delta);
            wolf.update(delta);

            // Render every 60 frames (1 second) to reduce flickering
            if (frames % 60 == 0) {
                // Move cursor to top-left without clearing entire screen
                moveCursorHome();
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
        System.out.println("  - Wolf chases NPC (tries to reach NPC)");
        System.out.println("  - Wolf respects walls (doesn't pass through)");
        System.out.println("  - NPC moves randomly, triggering Wolf chase");
        System.out.println();
        System.out.println("Final State:");
        System.out.println("  Score: " + manager.getScore());
        System.out.println("  HP: " + manager.getHp() + " / 100");

        if (manager.getHp() < 100) {
            System.out.println("\n  ✓ Collision detected! Enemies dealt damage.");
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void moveCursorHome() {
        System.out.print("\033[H");
        System.out.flush();
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

        // Calculate distance from Wolf to NPC
        float dx = npc.getX() - wolf.getX();
        float dy = npc.getY() - wolf.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        System.out.println("  Wolf distance to NPC: " + String.format("%.1f", distance) + " tiles");

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
