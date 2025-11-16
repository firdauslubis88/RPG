package difficulty;

/**
 * Week 12-02: Easy Difficulty Strategy (STRATEGY PATTERN SOLUTION)
 *
 * ✅ SOLUTION: Encapsulate EASY difficulty behavior in separate class
 *
 * Easy Difficulty:
 * - Only Spikes and Goblins (NO Wolves!)
 * - 3 Spikes, 3 Goblins initially
 * - NO continuous spawning (only initial enemies)
 * - Best for learning the game
 */
public class EasyDifficulty implements DifficultyStrategy {

    @Override
    public float getSpawnInterval() {
        return 1.0f;  // Not used since no continuous spawning
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
        return 0;  // NO wolves in EASY!
    }

    @Override
    public boolean hasContinuousSpawning() {
        return false;  // EASY: No continuous spawning
    }

    @Override
    public int getEnemyTypeToSpawn(int random) {
        // Only Spike or Goblin (no wolves!)
        return random % 2;  // 0 = Spike, 1 = Goblin
    }

    @Override
    public String getName() {
        return "EASY";
    }
}
