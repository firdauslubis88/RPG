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

    public WorldController(NPC npc) {
        this.activeObstacles = new ArrayList<>();
        this.random = new Random();
        this.npc = npc;

        // Week 10: Spawn initial obstacles using HARD-CODED creation
        spawnInitialObstacles();
    }

    /**
     * Spawn initial set of obstacles using HARD-CODED creation
     *
     * ❌ PROBLEM: Hard-coded instantiation!
     * ❌ PROBLEM: WorldController knows ALL concrete types
     * ❌ PROBLEM: Cannot extend without modification
     */
    private void spawnInitialObstacles() {
        // ❌ HARD-CODED CREATION - Must know all concrete types!
        // Spawn 4 Spikes at strategic positions (25x25 map)
        activeObstacles.add(new Spike(6, 6));
        activeObstacles.add(new Spike(12, 8));
        activeObstacles.add(new Spike(18, 12));
        activeObstacles.add(new Spike(8, 19));

        // Spawn 4 Goblins that patrol corridors
        activeObstacles.add(new Goblin(8, 4));
        activeObstacles.add(new Goblin(15, 10));
        activeObstacles.add(new Goblin(10, 17));
        activeObstacles.add(new Goblin(20, 20));

        // Spawn 3 Wolves that chase
        activeObstacles.add(new Wolf(7, 12));
        activeObstacles.add(new Wolf(17, 7));
        activeObstacles.add(new Wolf(12, 18));

        // ❌ Want to add Boss? Must add new lines here!
        // ❌ This violates Open/Closed Principle!
    }

    /**
     * Update all obstacles (no periodic spawning)
     */
    public void update(float delta) {
        // Update all active obstacles
        for (Obstacle obstacle : activeObstacles) {
            obstacle.update(delta);

            // ❌ ANOTHER PROBLEM: instanceof checks (type checking)
            // Set target for Wolf obstacles
            if (obstacle instanceof Wolf) {
                ((Wolf) obstacle).setTarget(npc);
            }
        }

        // Remove only inactive obstacles
        activeObstacles.removeIf(obs -> !obs.isActive());
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
