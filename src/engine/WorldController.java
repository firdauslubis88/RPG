package engine;

import obstacles.Obstacle;
import factories.SpikeFactory;
import factories.GoblinFactory;
import factories.WolfFactory;
import pools.ObstaclePool;
import entities.Entity;
import world.DungeonMap;
import difficulty.DifficultyStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
 * WorldController - Manages obstacle spawning with Strategy Pattern
 *
 * Design Patterns Used:
 * - Object Pool Pattern for obstacle reuse
 * - Strategy Pattern for difficulty-based spawning
 */
public class WorldController {
    private final List<Obstacle> activeObstacles;
    private final List<ObstaclePool> pools;
    private final Random random;
    private final Entity entity;
    private final DifficultyStrategy strategy;

    private float spawnTimer = 0;
    private static final int OFF_SCREEN_Y = 25;

    public WorldController(Entity entity, DifficultyStrategy strategy) {
        this.activeObstacles = new ArrayList<>();
        this.random = new Random();
        this.entity = entity;
        this.strategy = strategy;

        this.pools = Arrays.asList(
            new ObstaclePool(new SpikeFactory(), 10, 50),
            new ObstaclePool(new GoblinFactory(), 10, 50),
            new ObstaclePool(new WolfFactory(), 10, 50)
        );

        spawnInitialObstacles();
    }

    private void spawnInitialObstacles() {
        ObstaclePool spikePool = pools.get(0);
        ObstaclePool goblinPool = pools.get(1);
        ObstaclePool wolfPool = pools.get(2);

        int spikeCount = strategy.getInitialSpikeCount();
        int[][] spikePositions = {{6, 6}, {12, 8}, {18, 12}, {8, 19}};
        for (int i = 0; i < Math.min(spikeCount, spikePositions.length); i++) {
            addIfNotNull(activeObstacles, spikePool.acquire(spikePositions[i][0], spikePositions[i][1]));
        }

        int goblinCount = strategy.getInitialGoblinCount();
        int[][] goblinPositions = {{8, 4}, {15, 10}, {10, 17}, {20, 20}};
        for (int i = 0; i < Math.min(goblinCount, goblinPositions.length); i++) {
            addIfNotNull(activeObstacles, goblinPool.acquire(goblinPositions[i][0], goblinPositions[i][1]));
        }

        int wolfCount = strategy.getInitialWolfCount();
        int[][] wolfPositions = {{7, 12}, {17, 7}, {12, 18}, {4, 14}};
        for (int i = 0; i < Math.min(wolfCount, wolfPositions.length); i++) {
            addIfNotNull(activeObstacles, wolfPool.acquire(wolfPositions[i][0], wolfPositions[i][1]));
        }
    }

    private void addIfNotNull(List<Obstacle> list, Obstacle obstacle) {
        if (obstacle != null) {
            list.add(obstacle);
        }
    }

    public void update(float delta) {
        if (strategy.hasContinuousSpawning()) {
            spawnTimer += delta;
            if (spawnTimer >= strategy.getSpawnInterval()) {
                spawnRandomObstacle();
                spawnTimer = 0;
            }
        }

        List<int[]> oldPositions = new ArrayList<>();
        for (Obstacle obstacle : activeObstacles) {
            oldPositions.add(new int[]{obstacle.getX(), obstacle.getY()});
        }

        for (int i = 0; i < activeObstacles.size(); i++) {
            Obstacle obstacle = activeObstacles.get(i);
            int[] oldPos = oldPositions.get(i);

            obstacle.update(delta);

            if (obstacle instanceof obstacles.Wolf) {
                ((obstacles.Wolf) obstacle).setTarget(entity);
            }

            int newX = obstacle.getX();
            int newY = obstacle.getY();

            if (newX != oldPos[0] || newY != oldPos[1]) {
                for (int j = 0; j < activeObstacles.size(); j++) {
                    if (i != j) {
                        Obstacle other = activeObstacles.get(j);
                        if (other.getX() == newX && other.getY() == newY) {
                            obstacle.setPosition(oldPos[0], oldPos[1]);
                            break;
                        }
                    }
                }
            }
        }

        List<Obstacle> toRemove = new ArrayList<>();
        for (Obstacle obs : activeObstacles) {
            if (!obs.isActive() || obs.getY() > OFF_SCREEN_Y) {
                toRemove.add(obs);
            }
        }

        for (Obstacle obs : toRemove) {
            activeObstacles.remove(obs);
            returnToPool(obs);
        }
    }

    private void spawnRandomObstacle() {
        int randomValue = random.nextInt(10);
        int enemyType = strategy.getEnemyTypeToSpawn(randomValue);

        ObstaclePool pool = pools.get(enemyType);

        int x = -1, y = -1;
        int attempts = 0;
        while (attempts < 10) {
            int tryX = 1 + random.nextInt(23);
            int tryY = 1 + random.nextInt(23);

            if (isSafePosition(tryX, tryY)) {
                x = tryX;
                y = tryY;
                break;
            }
            attempts++;
        }

        if (x != -1 && y != -1) {
            Obstacle obstacle = pool.acquire(x, y);
            if (obstacle != null) {
                activeObstacles.add(obstacle);
            }
        }
    }

    private void returnToPool(Obstacle obstacle) {
        for (ObstaclePool pool : pools) {
            if (pool.ownsObstacle(obstacle)) {
                pool.release(obstacle);
                return;
            }
        }
    }

    private boolean isSafePosition(int x, int y) {
        if (!DungeonMap.isWalkable(x, y)) return false;
        if (x == 23 && y == 23) return false;

        int distance = Math.abs(x - entity.getX()) + Math.abs(y - entity.getY());
        if (distance < 3) return false;

        for (Obstacle obs : activeObstacles) {
            if (obs.getX() == x && obs.getY() == y) return false;
        }

        return true;
    }

    public List<Obstacle> getActiveObstacles() { return activeObstacles; }
    public int getObstacleCount() { return activeObstacles.size(); }

    public void printPoolStats() {
        System.out.println("\n=== POOL STATISTICS ===");
        pools.get(0).printStats("Spike");
        pools.get(1).printStats("Goblin");
        pools.get(2).printStats("Wolf");
        System.out.println("======================\n");
    }
}
