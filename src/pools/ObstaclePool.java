package pools;

import obstacles.Obstacle;
import factories.ObstacleFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ObstaclePool - Reuses obstacles instead of creating new ones
 *
 * Week 10 Branch 10-04: OBJECT POOL PATTERN SOLUTION
 *
 * ✅ SOLUTION: Pre-allocate obstacles and reuse them!
 * ✅ SOLUTION: No new objects = No garbage = No GC pauses!
 * ✅ SOLUTION: Stable 60 FPS performance
 *
 * Teaching Points:
 * - Object pooling eliminates GC pressure
 * - Trade memory (keep objects) for performance (no GC)
 * - Essential for real-time applications (games, servers)
 */
public class ObstaclePool {
    private final List<Obstacle> availableObstacles;
    private final List<Obstacle> allObstacles;  // Track all pool objects
    private final ObstacleFactory factory;
    private final int maxPoolSize;

    // Statistics
    private int acquireCount = 0;
    private int releaseCount = 0;
    private int createCount = 0;

    /**
     * Create obstacle pool with pre-allocation
     *
     * @param factory Factory for creating this type of obstacle
     * @param initialSize How many to pre-allocate (one-time cost)
     * @param maxSize Maximum pool size (safety limit)
     */
    public ObstaclePool(ObstacleFactory factory, int initialSize, int maxSize) {
        this.factory = factory;
        this.maxPoolSize = maxSize;
        this.availableObstacles = new ArrayList<>();
        this.allObstacles = new ArrayList<>();

        // ✅ Pre-allocate obstacles at startup (one-time GC cost)
        for (int i = 0; i < initialSize; i++) {
            Obstacle obstacle = factory.createObstacle(0, 0);
            obstacle.setActive(false);  // Start as inactive
            availableObstacles.add(obstacle);
            allObstacles.add(obstacle);
            createCount++;
        }
    }

    /**
     * Borrow an obstacle from the pool
     * ✅ REUSES existing object instead of creating new one!
     *
     * @param x Spawn X position
     * @param y Spawn Y position
     * @return Reused obstacle, or null if pool exhausted
     */
    public Obstacle acquire(int x, int y) {
        acquireCount++;
        Obstacle obstacle;

        if (!availableObstacles.isEmpty()) {
            // ✅ Reuse from pool (no allocation!)
            obstacle = availableObstacles.remove(availableObstacles.size() - 1);
        } else if (allObstacles.size() < maxPoolSize) {
            // Pool empty but can grow - create new one
            obstacle = factory.createObstacle(0, 0);
            allObstacles.add(obstacle);
            createCount++;
        } else {
            // Pool at max capacity
            return null;
        }

        // Reset obstacle state for reuse
        obstacle.reset(x, y);
        obstacle.setActive(true);
        return obstacle;
    }

    /**
     * Return obstacle to pool for reuse
     * ✅ Object stays in memory instead of being garbage collected!
     *
     * @param obstacle The obstacle to return
     */
    public void release(Obstacle obstacle) {
        if (obstacle == null) return;

        releaseCount++;
        obstacle.setActive(false);

        // Only add if it's from this pool and not already available
        if (allObstacles.contains(obstacle) && !availableObstacles.contains(obstacle)) {
            availableObstacles.add(obstacle);
        }
    }

    /**
     * Check if obstacle belongs to this pool
     */
    public boolean ownsObstacle(Obstacle obstacle) {
        return allObstacles.contains(obstacle);
    }

    /**
     * Get count of available obstacles (ready to borrow)
     */
    public int getAvailableCount() {
        return availableObstacles.size();
    }

    /**
     * Get total pool size (all objects, borrowed + available)
     */
    public int getTotalSize() {
        return allObstacles.size();
    }

    /**
     * Get count of currently borrowed obstacles
     */
    public int getInUseCount() {
        return allObstacles.size() - availableObstacles.size();
    }

    /**
     * Print pool statistics (for debugging)
     */
    public void printStats(String poolName) {
        System.out.println(String.format("%s Pool Stats:", poolName));
        System.out.println(String.format("  Total Created: %d", createCount));
        System.out.println(String.format("  Acquire Calls: %d", acquireCount));
        System.out.println(String.format("  Release Calls: %d", releaseCount));
        System.out.println(String.format("  Total Size: %d", getTotalSize()));
        System.out.println(String.format("  Available: %d", getAvailableCount()));
        System.out.println(String.format("  In Use: %d", getInUseCount()));
    }
}
