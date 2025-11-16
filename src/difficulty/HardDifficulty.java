package difficulty;

/**
 * Week 12-02: Hard Difficulty Strategy (STRATEGY PATTERN SOLUTION)
 *
 * ✅ SOLUTION: Encapsulate HARD difficulty behavior in separate class
 *
 * Hard Difficulty:
 * - All enemy types with Wolf emphasis (40% wolves)
 * - 4 Spikes, 4 Goblins, 4 Wolves initially
 * - Continuous auto-spawning every 0.3 seconds
 * - Most challenging difficulty
 */
public class HardDifficulty implements DifficultyStrategy {

    @Override
    public float getSpawnInterval() {
        return 0.3f;  // Fast spawning for HARD difficulty
    }

    @Override
    public int getInitialSpikeCount() {
        return 4;
    }

    @Override
    public int getInitialGoblinCount() {
        return 4;
    }

    @Override
    public int getInitialWolfCount() {
        return 4;
    }

    @Override
    public boolean hasContinuousSpawning() {
        return true;  // HARD: Continuous spawning enabled!
    }

    @Override
    public int getEnemyTypeToSpawn(int random) {
        // Wolf emphasis: 30% Spike, 30% Goblin, 40% Wolf
        if (random < 3) {
            return 0;  // Spike (0-2 = 30%)
        } else if (random < 6) {
            return 1;  // Goblin (3-5 = 30%)
        } else {
            return 2;  // Wolf (6-9 = 40%)
        }
    }

    @Override
    public String getName() {
        return "HARD";
    }
}
