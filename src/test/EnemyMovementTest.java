package test;

import world.DungeonMap;
import entities.NPC;
import entities.GameManager;
import obstacles.Goblin;
import obstacles.Wolf;
import utils.GridRenderer;

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

        // Track previous positions for selective rendering
        int prevNPCX = npc.getX();
        int prevNPCY = npc.getY();
        int prevGoblinX = goblin.getX();
        int prevGoblinY = goblin.getY();
        int prevWolfX = wolf.getX();
        int prevWolfY = wolf.getY();
        boolean goblinCleared = false;
        boolean wolfCleared = false;
        boolean firstFrame = true;

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

            // Render using selective rendering (like GameEngine)
            if (firstFrame) {
                // First frame: Draw everything
                GridRenderer.clearScreen();
                char[][] grid = DungeonMap.getMapCopy();

                // Draw entities
                grid[npc.getY()][npc.getX()] = 'N';
                if (goblin.isActive()) grid[goblin.getY()][goblin.getX()] = 'G';
                if (wolf.isActive()) grid[wolf.getY()][wolf.getX()] = 'W';

                GridRenderer.drawGrid(grid);

                // Print initial stats to the right of grid
                printStats(manager, goblin, wolf, npc, frames);

                firstFrame = false;
            } else {
                // Selective rendering - only update changed cells
                int currentNPCX = npc.getX();
                int currentNPCY = npc.getY();

                // Update NPC position
                if (currentNPCX != prevNPCX || currentNPCY != prevNPCY) {
                    char oldTile = DungeonMap.getTile(prevNPCX, prevNPCY);
                    GridRenderer.drawCell(oldTile, prevNPCX, prevNPCY);
                    GridRenderer.drawCell('N', currentNPCX, currentNPCY);
                    prevNPCX = currentNPCX;
                    prevNPCY = currentNPCY;
                }

                // Update Goblin position
                int currentGoblinX = goblin.getX();
                int currentGoblinY = goblin.getY();
                if (goblin.isActive()) {
                    if (currentGoblinX != prevGoblinX || currentGoblinY != prevGoblinY) {
                        char oldTile = DungeonMap.getTile(prevGoblinX, prevGoblinY);
                        GridRenderer.drawCell(oldTile, prevGoblinX, prevGoblinY);
                        GridRenderer.drawCell('G', currentGoblinX, currentGoblinY);
                        prevGoblinX = currentGoblinX;
                        prevGoblinY = currentGoblinY;
                    }
                } else if (!goblinCleared) {
                    // Clear goblin once when it becomes inactive
                    char oldTile = DungeonMap.getTile(prevGoblinX, prevGoblinY);
                    GridRenderer.drawCell(oldTile, prevGoblinX, prevGoblinY);
                    goblinCleared = true;
                }

                // Update Wolf position
                int currentWolfX = wolf.getX();
                int currentWolfY = wolf.getY();
                if (wolf.isActive()) {
                    if (currentWolfX != prevWolfX || currentWolfY != prevWolfY) {
                        char oldTile = DungeonMap.getTile(prevWolfX, prevWolfY);
                        GridRenderer.drawCell(oldTile, prevWolfX, prevWolfY);
                        GridRenderer.drawCell('W', currentWolfX, currentWolfY);
                        prevWolfX = currentWolfX;
                        prevWolfY = currentWolfY;
                    }
                } else if (!wolfCleared) {
                    // Clear wolf once when it becomes inactive
                    char oldTile = DungeonMap.getTile(prevWolfX, prevWolfY);
                    GridRenderer.drawCell(oldTile, prevWolfX, prevWolfY);
                    wolfCleared = true;
                }

                // Update stats every 15 frames
                if (frames % 15 == 0) {
                    printStats(manager, goblin, wolf, npc, frames);
                }
            }

            // Check collisions
            checkCollision(npc, goblin, wolf, manager);

            frames++;
            Thread.sleep(16);  // Simulate 60 FPS
        }

        // Move cursor far below the grid and stats before printing final results
        System.out.print("\033[35;1H");
        System.out.flush();

        System.out.println("\n\n========================================");
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

        System.out.println(); // Extra newline for spacing before command prompt
    }

    private static void printStats(GameManager manager, Goblin goblin, Wolf wolf, NPC npc, int frames) {
        float seconds = frames / 60.0f;

        // Calculate distance from Wolf to NPC
        float dx = npc.getX() - wolf.getX();
        float dy = npc.getY() - wolf.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Position stats to the right of the map (below HUD)
        int startCol = 28;  // Same column as HUD
        int startRow = 11;  // Below HUD (HUD ends at row 9)

        // Draw stats box
        System.out.print(String.format("\033[%d;%dH", startRow, startCol));
        System.out.print("╔════════════════════════════╗");

        System.out.print(String.format("\033[%d;%dH", startRow + 1, startCol));
        System.out.print(String.format("║ Time: %-20.1fs║", seconds));

        System.out.print(String.format("\033[%d;%dH", startRow + 2, startCol));
        System.out.print("╠════════════════════════════╣");

        System.out.print(String.format("\033[%d;%dH", startRow + 3, startCol));
        System.out.print(String.format("║ NPC:    (%2d,%2d)          ║", npc.getX(), npc.getY()));

        System.out.print(String.format("\033[%d;%dH", startRow + 4, startCol));
        System.out.print(String.format("║ Goblin: (%2d,%2d) %-6s  ║",
            goblin.getX(), goblin.getY(), goblin.isActive() ? "ACTIVE" : "DEAD"));

        System.out.print(String.format("\033[%d;%dH", startRow + 5, startCol));
        System.out.print(String.format("║ Wolf:   (%2d,%2d) %-6s  ║",
            wolf.getX(), wolf.getY(), wolf.isActive() ? "ACTIVE" : "DEAD"));

        System.out.print(String.format("\033[%d;%dH", startRow + 6, startCol));
        System.out.print(String.format("║ Wolf dist: %-15.1f║", distance));

        System.out.print(String.format("\033[%d;%dH", startRow + 7, startCol));
        System.out.print("╚════════════════════════════╝");

        System.out.flush();
    }

    private static void checkCollision(NPC npc, Goblin goblin, Wolf wolf, GameManager manager) {
        int npcX = npc.getX();
        int npcY = npc.getY();

        // Check Goblin collision
        if (npcX == goblin.getX() && npcY == goblin.getY() && goblin.isActive()) {
            manager.takeDamage(goblin.getDamage());
            goblin.setActive(false);
            // Print notification to the right of map (below stats box)
            System.out.print("\033[20;28H");
            System.out.print("╔════════════════════════════╗");
            System.out.print("\033[21;28H");
            System.out.print("║  >>> GOBLIN HIT!           ║");
            System.out.print("\033[22;28H");
            System.out.print(String.format("║      -%d HP                 ║", goblin.getDamage()));
            System.out.print("\033[23;28H");
            System.out.print("╚════════════════════════════╝");
            System.out.flush();
        }

        // Check Wolf collision
        if (npcX == wolf.getX() && npcY == wolf.getY() && wolf.isActive()) {
            manager.takeDamage(wolf.getDamage());
            wolf.setActive(false);
            // Print notification to the right of map (below stats box)
            System.out.print("\033[20;28H");
            System.out.print("╔════════════════════════════╗");
            System.out.print("\033[21;28H");
            System.out.print("║  >>> WOLF HIT!             ║");
            System.out.print("\033[22;28H");
            System.out.print(String.format("║      -%d HP                 ║", wolf.getDamage()));
            System.out.print("\033[23;28H");
            System.out.print("╚════════════════════════════╝");
            System.out.flush();
        }
    }
}
