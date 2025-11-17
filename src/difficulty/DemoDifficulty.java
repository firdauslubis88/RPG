package difficulty;

/**
 * Week 12-02: Demo Difficulty Strategy (for testing/demo purposes)
 *
 * ✅ SOLUTION: Implements DifficultyStrategy interface
 *
 * Demo Settings (Very Easy):
 * - Spawn Interval: 5.0 seconds (very slow)
 * - Initial Obstacles: 1 spike only
 * - Enemy Types: Only spikes (no goblins/wolves)
 * - Boss: Only uses DEFEND (guaranteed to lose)
 *
 * Perfect for:
 * - Testing game flow without dying
 * - Demonstrating battle mechanics
 * - Quick playthroughs
 */
public class DemoDifficulty implements DifficultyStrategy {
    @Override
    public float getSpawnInterval() {
        return 5.0f; // Very slow spawning (5 seconds)
    }

    @Override
    public int getInitialSpikeCount() {
        return 1; // Just 1 spike
    }

    @Override
    public int getInitialGoblinCount() {
        return 0; // No goblins
    }

    @Override
    public int getInitialWolfCount() {
        return 0; // No wolves
    }

    @Override
    public boolean hasContinuousSpawning() {
        return true; // Keep spawning (but very slow)
    }

    @Override
    public int getEnemyTypeToSpawn(int random) {
        // Always spawn Spike (enemy type 0)
        return 0;
    }

    @Override
    public String getName() {
        return "DEMO";
    }
}
