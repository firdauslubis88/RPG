import obstacles.Obstacle;
import factories.SpikeFactory;
import factories.GoblinFactory;
import factories.WolfFactory;
import pools.ObstaclePool;
import entities.Entity;
import world.DungeonMap;
import difficulty.DifficultyStrategy;  // Week 12-02: Strategy Pattern!

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
 * WorldController - Manages obstacle spawning with STRATEGY PATTERN (SOLUTION)
 *
 * Week 12-02: STRATEGY PATTERN (SOLUTION)
 *
 * ✅ KEPT: Object Pool Pattern (from 10-04)
 * ✅ SOLUTION: Strategy Pattern for flexible difficulty!
 *
 * Benefits:
 * - Open/Closed Principle: Add new difficulties WITHOUT modifying this class
 * - Single Responsibility: Difficulty logic in strategy classes
 * - Easy to Test: Can inject different strategies
 * - Compile-time Safety: No string comparisons!
 *
 * Evolution from Week 12-01:
 * ❌ Before: switch (difficulty) { case "EASY": ... }
 * ✅ Now: strategy.getInitialSpikeCount()
 */
public class WorldController {
    private final List<Obstacle> activeObstacles;
    private final List<ObstaclePool> pools;
    private final Random random;
    private final Entity entity;  // Week 11: Can be Player or NPC

    // Week 12-02: ✅ STRATEGY PATTERN - Depend on interface, not concrete class!
    private final DifficultyStrategy strategy;

    // Week 11: Reduced spawn rate for better gameplay balance
    private float spawnTimer = 0;
    private static final int OFF_SCREEN_Y = 25;

    /**
     * Week 12-02: Constructor with Strategy Pattern
     *
     * ✅ SOLUTION: Accept DifficultyStrategy interface instead of string!
     *
     * @param entity The player or NPC entity
     * @param strategy The difficulty strategy to use
     */
    public WorldController(Entity entity, DifficultyStrategy strategy) {
        this.activeObstacles = new ArrayList<>();
        this.random = new Random();
        this.entity = entity;
        this.strategy = strategy;  // ✅ Store strategy (polymorphism!)

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
     * Spawn initial set of obstacles from POOL based on STRATEGY
     *
     * Week 12-02: ✅ STRATEGY PATTERN - No more switch-case!
     * ✅ KEPT: Object Pool Pattern (from 10-04)
     */
    private void spawnInitialObstacles() {
        ObstaclePool spikePool = pools.get(0);
        ObstaclePool goblinPool = pools.get(1);
        ObstaclePool wolfPool = pools.get(2);

        // Week 12-02: ✅ STRATEGY PATTERN - Ask strategy how many of each!
        // No more switch-case! Strategy encapsulates difficulty logic.

        // Spawn Spikes
        int spikeCount = strategy.getInitialSpikeCount();
        int[][] spikePositions = {{6, 6}, {12, 8}, {18, 12}, {8, 19}};
        for (int i = 0; i < Math.min(spikeCount, spikePositions.length); i++) {
            addIfNotNull(activeObstacles, spikePool.acquire(spikePositions[i][0], spikePositions[i][1]));
        }

        // Spawn Goblins
        int goblinCount = strategy.getInitialGoblinCount();
        int[][] goblinPositions = {{8, 4}, {15, 10}, {10, 17}, {20, 20}};
        for (int i = 0; i < Math.min(goblinCount, goblinPositions.length); i++) {
            addIfNotNull(activeObstacles, goblinPool.acquire(goblinPositions[i][0], goblinPositions[i][1]));
        }

        // Spawn Wolves
        int wolfCount = strategy.getInitialWolfCount();
        int[][] wolfPositions = {{7, 12}, {17, 7}, {12, 18}, {4, 14}};
        for (int i = 0; i < Math.min(wolfCount, wolfPositions.length); i++) {
            addIfNotNull(activeObstacles, wolfPool.acquire(wolfPositions[i][0], wolfPositions[i][1]));
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
     * Week 12-02: ✅ STRATEGY PATTERN - Ask strategy if continuous spawning!
     * ✅ KEPT: Object Pool Pattern (from 10-04)
     */
    public void update(float delta) {
        // Week 12-02: ✅ STRATEGY PATTERN - No more hardcoded "HARD" check!
        if (strategy.hasContinuousSpawning()) {
            spawnTimer += delta;
            if (spawnTimer >= strategy.getSpawnInterval()) {
                spawnRandomObstacle();  // Now uses pool.acquire()!
                spawnTimer = 0;
            }
        }

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
     * Spawn random obstacle from POOL with STRATEGY-BASED TYPE SELECTION
     *
     * Week 12-02: ✅ STRATEGY PATTERN - No more switch-case!
     * ✅ KEPT: Object Pool Pattern (from 10-04)
     */
    private void spawnRandomObstacle() {
        // Week 12-02: ✅ STRATEGY PATTERN - Ask strategy which enemy type!
        int randomValue = random.nextInt(10);
        int enemyType = strategy.getEnemyTypeToSpawn(randomValue);

        ObstaclePool pool = pools.get(enemyType);

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
