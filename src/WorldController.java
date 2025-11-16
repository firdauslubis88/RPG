import obstacles.Obstacle;
import factories.SpikeFactory;
import factories.GoblinFactory;
import factories.WolfFactory;
import pools.ObstaclePool;
import entities.Entity;
import world.DungeonMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
 * WorldController - Manages obstacle spawning with HARDCODED DIFFICULTY (ANTI-PATTERN)
 *
 * Week 12-01: HARDCODED DIFFICULTY (ANTI-PATTERN)
 *
 * ✅ KEPT: Object Pool Pattern (from 10-04)
 * ❌ ANTI-PATTERN: Hardcoded switch-case for difficulty!
 *
 * Problems Demonstrated:
 * - Spawn logic tightly coupled to difficulty string
 * - Switch-case in multiple methods
 * - Hard to add new difficulties
 * - Violates Open/Closed Principle
 */
public class WorldController {
    private final List<Obstacle> activeObstacles;
    private final List<ObstaclePool> pools;
    private final Random random;
    private final Entity entity;  // Week 11: Can be Player or NPC

    // Week 12-01: ❌ ANTI-PATTERN - Hardcoded difficulty string!
    private final String difficulty;

    // Week 11: Reduced spawn rate for better gameplay balance
    private float spawnTimer = 0;
    private static final int OFF_SCREEN_Y = 25;

    // Week 12-01: ❌ ANTI-PATTERN - Different intervals per difficulty!
    private float spawnInterval;  // Set based on difficulty

    public WorldController(Entity entity, String difficulty) {
        this.activeObstacles = new ArrayList<>();
        this.random = new Random();
        this.entity = entity;
        this.difficulty = difficulty;

        // Week 12-01: ❌ ANTI-PATTERN - Hardcoded switch for spawn interval!
        switch (difficulty) {
            case "EASY":
                this.spawnInterval = 1.0f;  // 1 obstacle/second
                break;
            case "NORMAL":
                this.spawnInterval = 0.5f;  // 2 obstacles/second
                break;
            case "HARD":
                this.spawnInterval = 0.3f;  // 3.3 obstacles/second
                break;
            default:
                this.spawnInterval = 0.5f;  // Default to NORMAL
                break;
        }

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
     * Spawn initial set of obstacles from POOL based on DIFFICULTY
     *
     * Week 12-01: ❌ ANTI-PATTERN - Hardcoded switch for initial enemies!
     * ✅ KEPT: Object Pool Pattern (from 10-04)
     */
    private void spawnInitialObstacles() {
        ObstaclePool spikePool = pools.get(0);
        ObstaclePool goblinPool = pools.get(1);
        ObstaclePool wolfPool = pools.get(2);

        // Week 12-01: ❌ ANTI-PATTERN - Hardcoded switch for initial spawns!
        switch (difficulty) {
            case "EASY":
                // EASY: Only Spikes and Goblins
                // Spawn 3 Spikes
                addIfNotNull(activeObstacles, spikePool.acquire(6, 6));
                addIfNotNull(activeObstacles, spikePool.acquire(12, 8));
                addIfNotNull(activeObstacles, spikePool.acquire(18, 12));

                // Spawn 3 Goblins
                addIfNotNull(activeObstacles, goblinPool.acquire(8, 4));
                addIfNotNull(activeObstacles, goblinPool.acquire(15, 10));
                addIfNotNull(activeObstacles, goblinPool.acquire(10, 17));
                // No Wolves in EASY!
                break;

            case "NORMAL":
                // NORMAL: Balanced mix
                // Spawn 3 Spikes
                addIfNotNull(activeObstacles, spikePool.acquire(6, 6));
                addIfNotNull(activeObstacles, spikePool.acquire(12, 8));
                addIfNotNull(activeObstacles, spikePool.acquire(18, 12));

                // Spawn 3 Goblins
                addIfNotNull(activeObstacles, goblinPool.acquire(8, 4));
                addIfNotNull(activeObstacles, goblinPool.acquire(15, 10));
                addIfNotNull(activeObstacles, goblinPool.acquire(10, 17));

                // Spawn 2 Wolves
                addIfNotNull(activeObstacles, wolfPool.acquire(7, 12));
                addIfNotNull(activeObstacles, wolfPool.acquire(17, 7));
                break;

            case "HARD":
                // HARD: More enemies, more wolves
                // Spawn 4 Spikes
                addIfNotNull(activeObstacles, spikePool.acquire(6, 6));
                addIfNotNull(activeObstacles, spikePool.acquire(12, 8));
                addIfNotNull(activeObstacles, spikePool.acquire(18, 12));
                addIfNotNull(activeObstacles, spikePool.acquire(8, 19));

                // Spawn 4 Goblins
                addIfNotNull(activeObstacles, goblinPool.acquire(8, 4));
                addIfNotNull(activeObstacles, goblinPool.acquire(15, 10));
                addIfNotNull(activeObstacles, goblinPool.acquire(10, 17));
                addIfNotNull(activeObstacles, goblinPool.acquire(20, 20));

                // Spawn 4 Wolves (more aggressive!)
                addIfNotNull(activeObstacles, wolfPool.acquire(7, 12));
                addIfNotNull(activeObstacles, wolfPool.acquire(17, 7));
                addIfNotNull(activeObstacles, wolfPool.acquire(12, 18));
                addIfNotNull(activeObstacles, wolfPool.acquire(4, 14));
                break;

            default:
                // Default to NORMAL
                addIfNotNull(activeObstacles, spikePool.acquire(6, 6));
                addIfNotNull(activeObstacles, spikePool.acquire(12, 8));
                addIfNotNull(activeObstacles, goblinPool.acquire(8, 4));
                addIfNotNull(activeObstacles, goblinPool.acquire(15, 10));
                addIfNotNull(activeObstacles, wolfPool.acquire(7, 12));
                break;
        }
    }

    /**
     * Helper method to add obstacle if not null
     */
    private void addIfNotNull(List<Obstacle> list, Obstacle obstacle) {
        if (obstacle != null) {
            list.add(obstacle);
        }
    }

    /**
     * Update all obstacles
     *
     * Week 12-01: ❌ ANTI-PATTERN - Only HARD difficulty has continuous spawning
     * ✅ KEPT: Object Pool Pattern (from 10-04)
     */
    public void update(float delta) {
        // Week 12-01: ❌ ANTI-PATTERN - Only spawn continuously on HARD difficulty!
        if (difficulty.equals("HARD")) {
            spawnTimer += delta;
            if (spawnTimer >= spawnInterval) {
                spawnRandomObstacle();  // Now uses pool.acquire()!
                spawnTimer = 0;
            }
        }
        // EASY and NORMAL: No continuous spawning, only initial obstacles

        // Store old positions before update
        List<int[]> oldPositions = new ArrayList<>();
        for (Obstacle obstacle : activeObstacles) {
            oldPositions.add(new int[]{obstacle.getX(), obstacle.getY()});
        }

        // Update visible obstacles
        for (int i = 0; i < activeObstacles.size(); i++) {
            Obstacle obstacle = activeObstacles.get(i);
            int[] oldPos = oldPositions.get(i);

            obstacle.update(delta);

            // Week 11: Wolf targets player/entity
            if (obstacle instanceof obstacles.Wolf) {
                ((obstacles.Wolf) obstacle).setTarget(entity);
            }

            // Week 11: Check collision with other obstacles after movement
            int newX = obstacle.getX();
            int newY = obstacle.getY();

            // If moved, check for collision with other obstacles
            if (newX != oldPos[0] || newY != oldPos[1]) {
                for (int j = 0; j < activeObstacles.size(); j++) {
                    if (i != j) {  // Don't check self
                        Obstacle other = activeObstacles.get(j);
                        if (other.getX() == newX && other.getY() == newY) {
                            // Collision detected! Revert to old position
                            obstacle.setPosition(oldPos[0], oldPos[1]);
                            break;
                        }
                    }
                }
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
     * Spawn random obstacle from POOL with DIFFICULTY-BASED TYPE SELECTION
     *
     * Week 12-01: ❌ ANTI-PATTERN - Hardcoded switch for enemy types!
     * ✅ KEPT: Object Pool Pattern (from 10-04)
     */
    private void spawnRandomObstacle() {
        ObstaclePool pool;

        // Week 12-01: ❌ ANTI-PATTERN - Hardcoded switch based on difficulty!
        switch (difficulty) {
            case "EASY":
                // EASY: Only Spikes and Goblins (no Wolves!)
                pool = pools.get(random.nextInt(2));  // 0 or 1 (Spike or Goblin)
                break;
            case "NORMAL":
                // NORMAL: All enemy types (Spikes, Goblins, Wolves)
                pool = pools.get(random.nextInt(3));  // 0, 1, or 2
                break;
            case "HARD":
                // HARD: All enemy types with emphasis on Goblins and Wolves
                int type = random.nextInt(10);
                if (type < 3) {
                    pool = pools.get(0);  // 30% Spike
                } else if (type < 6) {
                    pool = pools.get(1);  // 30% Goblin
                } else {
                    pool = pools.get(2);  // 40% Wolf (more aggressive!)
                }
                break;
            default:
                pool = pools.get(random.nextInt(3));  // Default to NORMAL
                break;
        }

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

        // Week 12-01: Check 2: Must not be dungeon exit position (23, 23)
        if (x == 23 && y == 23) return false;

        // Check 3: Must not be too close to entity/player (minimum 3 tiles)
        int distance = Math.abs(x - entity.getX()) + Math.abs(y - entity.getY());
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
