import obstacles.Obstacle;
import factories.SpikeFactory;
import factories.GoblinFactory;
import factories.WolfFactory;
import pools.ObstaclePool;
import entities.NPC;
import world.DungeonMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
 * WorldController - Manages obstacle spawning using OBJECT POOL PATTERN
 *
 * Week 10 Branch 10-04: OBJECT POOL PATTERN SOLUTION
 *
 * ✅ SOLUTION: Reuse obstacles instead of creating/destroying!
 * ✅ SOLUTION: Pre-allocated pools eliminate GC pressure
 * ✅ SOLUTION: Stable 60 FPS performance
 *
 * Teaching Points:
 * - Object pooling eliminates allocation/deallocation overhead
 * - Trade memory (keep objects) for performance (no GC)
 * - Essential pattern for real-time applications
 */
public class WorldController {
    private final List<Obstacle> activeObstacles;
    private final List<ObstaclePool> pools;
    private final Random random;
    private final NPC npc;

    // Same spawn rate, but NOW with pooling!
    private float spawnTimer = 0;
    private static final float SPAWN_INTERVAL = 0.05f;  // 20 obstacles/second
    private static final int OFF_SCREEN_Y = 25;

    public WorldController(NPC npc) {
        this.activeObstacles = new ArrayList<>();
        this.random = new Random();
        this.npc = npc;

        // ✅ SOLUTION: Create pools instead of factories!
        // Pre-allocate 10 of each type, max 50 per pool
        this.pools = Arrays.asList(
            new ObstaclePool(new SpikeFactory(), 10, 50),
            new ObstaclePool(new GoblinFactory(), 10, 50),
            new ObstaclePool(new WolfFactory(), 10, 50)
        );

        // Spawn initial obstacles using pools
        spawnInitialObstacles();
    }

    /**
     * Spawn initial set of obstacles from POOL
     *
     * ✅ SOLUTION: Borrows from pool instead of creating new objects
     */
    private void spawnInitialObstacles() {
        ObstaclePool spikePool = pools.get(0);
        ObstaclePool goblinPool = pools.get(1);
        ObstaclePool wolfPool = pools.get(2);

        // Spawn 4 Spikes at strategic positions (25x25 map)
        Obstacle spike1 = spikePool.acquire(6, 6);
        Obstacle spike2 = spikePool.acquire(12, 8);
        Obstacle spike3 = spikePool.acquire(18, 12);
        Obstacle spike4 = spikePool.acquire(8, 19);
        if (spike1 != null) activeObstacles.add(spike1);
        if (spike2 != null) activeObstacles.add(spike2);
        if (spike3 != null) activeObstacles.add(spike3);
        if (spike4 != null) activeObstacles.add(spike4);

        // Spawn 4 Goblins that patrol corridors
        Obstacle goblin1 = goblinPool.acquire(8, 4);
        Obstacle goblin2 = goblinPool.acquire(15, 10);
        Obstacle goblin3 = goblinPool.acquire(10, 17);
        Obstacle goblin4 = goblinPool.acquire(20, 20);
        if (goblin1 != null) activeObstacles.add(goblin1);
        if (goblin2 != null) activeObstacles.add(goblin2);
        if (goblin3 != null) activeObstacles.add(goblin3);
        if (goblin4 != null) activeObstacles.add(goblin4);

        // Spawn 3 Wolves that chase
        Obstacle wolf1 = wolfPool.acquire(7, 12);
        Obstacle wolf2 = wolfPool.acquire(17, 7);
        Obstacle wolf3 = wolfPool.acquire(12, 18);
        if (wolf1 != null) activeObstacles.add(wolf1);
        if (wolf2 != null) activeObstacles.add(wolf2);
        if (wolf3 != null) activeObstacles.add(wolf3);
    }

    /**
     * Update all obstacles
     *
     * ✅ SOLUTION: Same spawn rate, but using pool!
     * ✅ SOLUTION: No new objects created
     * ✅ SOLUTION: No GC pressure!
     */
    public void update(float delta) {
        // ✅ Spawn obstacles at same rate (20/second) but from POOL
        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnRandomObstacle();  // Now uses pool.acquire()!
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

        // ✅ SOLUTION: Return to pool instead of destroying
        List<Obstacle> toRemove = new ArrayList<>();
        for (Obstacle obs : activeObstacles) {
            if (!obs.isActive() || obs.getY() > OFF_SCREEN_Y) {
                toRemove.add(obs);
            }
        }

        for (Obstacle obs : toRemove) {
            activeObstacles.remove(obs);
            returnToPool(obs);  // ✅ Reuse instead of destroy!
        }
    }

    /**
     * Spawn random obstacle from POOL
     *
     * ✅ SOLUTION: Borrows from pool (reuse!) instead of new object
     */
    private void spawnRandomObstacle() {
        ObstaclePool pool = pools.get(random.nextInt(pools.size()));

        // Try to find safe spawn position (max 10 attempts)
        int x = -1, y = -1;
        int attempts = 0;
        while (attempts < 10) {
            int tryX = 1 + random.nextInt(23);  // 1-23 (avoid borders)
            int tryY = 1 + random.nextInt(23);  // 1-23 (avoid borders)

            if (isSafePosition(tryX, tryY)) {
                x = tryX;
                y = tryY;
                break;
            }
            attempts++;
        }

        // Only spawn if we found a safe position
        if (x != -1 && y != -1) {
            // ✅ Borrow from pool (no new keyword!)
            Obstacle obstacle = pool.acquire(x, y);
            if (obstacle != null) {
                activeObstacles.add(obstacle);
            }
        }
    }

    /**
     * Return obstacle to its pool
     * ✅ SOLUTION: Object stays in memory for reuse!
     */
    private void returnToPool(Obstacle obstacle) {
        // Find correct pool and return obstacle
        for (ObstaclePool pool : pools) {
            if (pool.ownsObstacle(obstacle)) {
                pool.release(obstacle);
                return;
            }
        }
    }

    /**
     * Check if position is safe for spawning
     */
    private boolean isSafePosition(int x, int y) {
        // Check 1: Must be walkable floor
        if (!DungeonMap.isWalkable(x, y)) return false;

        // Check 2: Must not be too close to NPC (minimum 3 tiles)
        int distance = Math.abs(x - npc.getX()) + Math.abs(y - npc.getY());
        if (distance < 3) return false;

        // Check 3: Must not overlap with existing obstacles
        for (Obstacle obs : activeObstacles) {
            if (obs.getX() == x && obs.getY() == y) return false;
        }

        return true;
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

    /**
     * Print pool statistics (for debugging)
     */
    public void printPoolStats() {
        System.out.println("\n=== POOL STATISTICS ===");
        pools.get(0).printStats("Spike");
        pools.get(1).printStats("Goblin");
        pools.get(2).printStats("Wolf");
        System.out.println("======================\n");
    }
}
