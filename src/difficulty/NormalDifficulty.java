package difficulty;

/**
 * Week 12-02: Normal Difficulty Strategy (STRATEGY PATTERN SOLUTION)
 *
 * ✅ SOLUTION: Encapsulate NORMAL difficulty behavior in separate class
 *
 * Normal Difficulty:
 * - All enemy types (Spikes, Goblins, Wolves)
 * - 3 Spikes, 3 Goblins, 2 Wolves initially
 * - NO continuous spawning (only initial enemies)
 * - Standard balanced gameplay
 */
public class NormalDifficulty implements DifficultyStrategy {

    @Override
    public float getSpawnInterval() {
        return 0.5f;  // Not used since no continuous spawning
    }

    @Override
    public int getInitialSpikeCount() {
        return 3;
    }

    @Override
    public int getInitialGoblinCount() {
        return 3;
    }

    @Override
    public int getInitialWolfCount() {
        return 2;
    }

    @Override
    public boolean hasContinuousSpawning() {
        return false;  // NORMAL: No continuous spawning
    }

    @Override
    public int getEnemyTypeToSpawn(int random) {
        // All enemy types with equal distribution
        return random % 3;  // 0 = Spike, 1 = Goblin, 2 = Wolf
    }

    @Override
    public String getName() {
        return "NORMAL";
    }
}
