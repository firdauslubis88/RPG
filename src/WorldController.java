import obstacles.Obstacle;
import obstacles.Spike;
import obstacles.Goblin;
import obstacles.Wolf;
import entities.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * WorldController - Manages obstacle spawning and lifecycle
 *
 * Week 10 Branch 10-01: HARD-CODED SPAWNING (ANTI-PATTERN DEMO)
 *
 * ❌ PROBLEM: This class uses HARD-CODED object creation!
 * ❌ PROBLEM: WorldController knows ALL concrete obstacle types
 * ❌ PROBLEM: Violates Open/Closed Principle
 * ❌ PROBLEM: Adding new obstacle type requires MODIFYING this file
 *
 * This is INTENTIONALLY bad design to demonstrate the problem
 * that Factory Method pattern solves in the next branch (10-02).
 *
 * Teaching Points:
 * - Switch-case hell
 * - Tight coupling to concrete classes
 * - Merge conflict hotspot
 * - Cannot extend without modification
 */
public class WorldController {
    private final List<Obstacle> activeObstacles;
    private final Random random;
    private final NPC npc;
    private float spawnTimer;
    private final float spawnInterval = 2.0f;  // Spawn every 2 seconds

    public WorldController(NPC npc) {
        this.activeObstacles = new ArrayList<>();
        this.random = new Random();
        this.npc = npc;
        this.spawnTimer = 0.0f;
    }

    /**
     * Update all obstacles and handle spawning
     */
    public void update(float delta) {
        // Update spawn timer
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnRandomObstacle();
            spawnTimer = 0.0f;
        }

        // Update all active obstacles
        for (Obstacle obstacle : activeObstacles) {
            obstacle.update(delta);

            // ❌ ANOTHER PROBLEM: instanceof checks (type checking)
            // Set target for Wolf obstacles
            if (obstacle instanceof Wolf) {
                ((Wolf) obstacle).setTarget(npc);
            }
        }

        // Remove inactive obstacles or those that went offscreen
        activeObstacles.removeIf(obs -> !obs.isActive() || obs.getY() > 9);
    }

    /**
     * Spawn a random obstacle using HARD-CODED creation
     *
     * ❌ PROBLEM: Switch-case hell!
     * ❌ PROBLEM: Cannot extend without modification
     * ❌ PROBLEM: Merge conflict hotspot in team development
     * ❌ PROBLEM: WorldController tightly coupled to ALL obstacle types
     */
    private void spawnRandomObstacle() {
        int type = random.nextInt(3);  // 0, 1, or 2

        // Spawn in walkable area only (not on walls)
        int x = random.nextInt(7) + 1;  // Random column (1-7, avoid walls at 0 and 9)
        int y = random.nextInt(7) + 1;  // Random row (1-7, avoid walls at 0 and 9)

        Obstacle obstacle = null;

        // ❌ HARD-CODED CREATION PROBLEM!
        // Every new obstacle type requires modifying this switch-case
        // What if we want to add Boss, Missile, Trap? Must modify here!
        // Two developers adding obstacles = MERGE CONFLICT!
        switch(type) {
            case 0:
                obstacle = new Spike(x, y);
                break;
            case 1:
                obstacle = new Goblin(x, y);
                break;
            case 2:
                obstacle = new Wolf(x, y);
                break;
            // ❌ Want to add Boss? Must add case 3 here!
            // ❌ Want to add Missile? Must add case 4 here!
            // ❌ This violates the Open/Closed Principle!
        }

        if (obstacle != null) {
            activeObstacles.add(obstacle);
        }
    }

    /**
     * Get all active obstacles for rendering
     */
    public List<Obstacle> getActiveObstacles() {
        return activeObstacles;
    }

    /**
     * Get count of active obstacles (for debugging)
     */
    public int getObstacleCount() {
        return activeObstacles.size();
    }
}
