import obstacles.Obstacle;
import factories.ObstacleFactory;
import factories.SpikeFactory;
import factories.GoblinFactory;
import factories.WolfFactory;
import entities.NPC;
import world.DungeonMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
 * WorldController - Manages obstacle spawning and lifecycle
 *
 * Week 10 Branch 10-03: GARBAGE COLLECTION PERFORMANCE PROBLEM
 *
 * ❌ PROBLEM: High-frequency object creation/destruction causing GC lag!
 * ❌ PROBLEM: Spawning 20 obstacles/second = 1200/minute
 * ❌ PROBLEM: Frequent destruction when off-screen
 * ❌ PROBLEM: GC pauses 150-200ms causing visible frame drops
 *
 * This branch demonstrates what happens when we don't reuse objects.
 * Next branch will fix this with Object Pool pattern.
 *
 * Teaching Points:
 * - GC cost of frequent allocation/deallocation
 * - Stop-the-world pauses impact game performance
 * - Why object pooling matters in real-time applications
 */
public class WorldController {
    private final List<Obstacle> activeObstacles;
    private final List<ObstacleFactory> factories;
    private final Random random;
    private final NPC npc;

    // ❌ PROBLEM: High spawn rate causing GC pressure!
    private float spawnTimer = 0;
    private static final float SPAWN_INTERVAL = 0.05f;  // 20 obstacles/second!
    private static final int OFF_SCREEN_Y = 25;  // Boundary for destruction

    public WorldController(NPC npc) {
        this.activeObstacles = new ArrayList<>();
        this.random = new Random();
        this.npc = npc;

        // ✅ SOLUTION: Register factories (loose coupling!)
        // WorldController doesn't know Spike, Goblin, Wolf classes!
        this.factories = Arrays.asList(
            new SpikeFactory(),
            new GoblinFactory(),
            new WolfFactory()
        );

        // Week 10: Spawn initial obstacles using FACTORY PATTERN
        spawnInitialObstacles();
    }

    /**
     * Spawn initial set of obstacles using FACTORY PATTERN
     *
     * ✅ SOLUTION: Uses factories instead of hard-coded creation
     * ✅ SOLUTION: WorldController doesn't know concrete types
     * ✅ SOLUTION: Can extend without modification
     */
    private void spawnInitialObstacles() {
        // ✅ Use factories for creation - no hard-coded types!

        // Spawn 4 Spikes at strategic positions (25x25 map)
        SpikeFactory spikeFactory = new SpikeFactory();
        activeObstacles.add(spikeFactory.createObstacle(6, 6));
        activeObstacles.add(spikeFactory.createObstacle(12, 8));
        activeObstacles.add(spikeFactory.createObstacle(18, 12));
        activeObstacles.add(spikeFactory.createObstacle(8, 19));

        // Spawn 4 Goblins that patrol corridors
        GoblinFactory goblinFactory = new GoblinFactory();
        activeObstacles.add(goblinFactory.createObstacle(8, 4));
        activeObstacles.add(goblinFactory.createObstacle(15, 10));
        activeObstacles.add(goblinFactory.createObstacle(10, 17));
        activeObstacles.add(goblinFactory.createObstacle(20, 20));

        // Spawn 3 Wolves that chase
        WolfFactory wolfFactory = new WolfFactory();
        activeObstacles.add(wolfFactory.createObstacle(7, 12));
        activeObstacles.add(wolfFactory.createObstacle(17, 7));
        activeObstacles.add(wolfFactory.createObstacle(12, 18));

        // ✅ Want to add Boss? Just create BossFactory and use it here!
        // ✅ No modification to WorldController logic needed!
    }

    /**
     * Update all obstacles
     *
     * ❌ PROBLEM: High-frequency spawning AND destruction creating GC pressure!
     * ❌ PROBLEM: Creates 20 new objects per second
     * ❌ PROBLEM: Short-lived objects quickly become garbage
     * ❌ PROBLEM: GC cannot keep up - causes 150-200ms pauses!
     */
    public void update(float delta) {
        // ❌ PROBLEM: Spawn obstacles at high rate (20/second) for GC pressure
        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnRandomObstacle();  // Spawns at safe positions
            spawnTimer = 0;
        }

        // Update visible obstacles
        for (Obstacle obstacle : activeObstacles) {
            obstacle.update(delta);

            // ⚠️ TEMPORARY: Still using instanceof for Wolf targeting
            if (obstacle instanceof obstacles.Wolf) {
                ((obstacles.Wolf) obstacle).setTarget(npc);
            }
        }

        // ❌ PROBLEM: Remove off-screen obstacles (creates garbage!)
        activeObstacles.removeIf(obs -> !obs.isActive() || obs.getY() > OFF_SCREEN_Y);
    }

    /**
     * Spawn random obstacle using factory pattern at SAFE position
     *
     * ✅ SOLUTION: Spawns at safe positions (not near NPC, walls, or other obstacles)
     * ❌ PROBLEM: Called 20 times per second!
     * ❌ PROBLEM: Each call creates NEW object (no reuse)
     * ❌ PROBLEM: After 1 minute: 1200 objects created + destroyed
     */
    private void spawnRandomObstacle() {
        // Pick random factory
        ObstacleFactory factory = factories.get(random.nextInt(factories.size()));

        // Try to find safe spawn position (max 10 attempts)
        int x = -1, y = -1;
        int attempts = 0;
        while (attempts < 10) {
            // Random position (1-23, avoiding borders at 0 and 24)
            int tryX = 1 + random.nextInt(23);  // 1-23
            int tryY = 1 + random.nextInt(23);  // 1-23

            // ✅ Check if position is SAFE
            if (isSafePosition(tryX, tryY)) {
                x = tryX;
                y = tryY;
                break;
            }
            attempts++;
        }

        // Only spawn if we found a safe position
        if (x != -1 && y != -1) {
            // ❌ Create new object (no pooling!)
            Obstacle newObstacle = factory.createObstacle(x, y);
            activeObstacles.add(newObstacle);
        }
    }

    /**
     * Check if position is safe for spawning
     * - Must be walkable floor (not wall)
     * - Must not be too close to NPC (minimum 3 tiles distance)
     * - Must not overlap with existing obstacles
     */
    private boolean isSafePosition(int x, int y) {
        // Check 1: Must be walkable floor
        if (!DungeonMap.isWalkable(x, y)) {
            return false;
        }

        // Check 2: Must not be too close to NPC (minimum 3 tiles Manhattan distance)
        int npcX = npc.getX();
        int npcY = npc.getY();
        int distance = Math.abs(x - npcX) + Math.abs(y - npcY);
        if (distance < 3) {
            return false;  // Too close to NPC!
        }

        // Check 3: Must not overlap with existing obstacles
        for (Obstacle obs : activeObstacles) {
            if (obs.getX() == x && obs.getY() == y) {
                return false;  // Position occupied!
            }
        }

        return true;  // Safe to spawn here!
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
