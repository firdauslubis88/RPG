package difficulty;

/**
 * Week 12-02: DifficultyStrategy Interface (STRATEGY PATTERN SOLUTION)
 *
 * ✅ SOLUTION: Define strategy interface for difficulty behaviors
 *
 * Benefits of Strategy Pattern:
 * 1. Open/Closed Principle: Add new difficulties without modifying existing code
 * 2. Single Responsibility: Each difficulty encapsulates its own logic
 * 3. Compile-time Safety: No string comparisons!
 * 4. Easy to Test: Each strategy can be tested independently
 * 5. Flexible: Can swap strategies at runtime (if needed)
 *
 * Contrast with Week 12-01 Anti-Pattern:
 * ❌ Before: switch(difficulty) scattered across multiple files
 * ✅ Now: Each difficulty is a separate class implementing this interface
 */
public interface DifficultyStrategy {

    /**
     * Get the spawn interval for this difficulty
     * @return Time in seconds between spawns
     */
    float getSpawnInterval();

    /**
     * Get the number of Spikes to spawn initially
     */
    int getInitialSpikeCount();

    /**
     * Get the number of Goblins to spawn initially
     */
    int getInitialGoblinCount();

    /**
     * Get the number of Wolves to spawn initially
     */
    int getInitialWolfCount();

    /**
     * Should this difficulty have continuous spawning during gameplay?
     * @return true if enemies should auto-spawn, false otherwise
     */
    boolean hasContinuousSpawning();

    /**
     * Get the enemy type to spawn (0=Spike, 1=Goblin, 2=Wolf)
     * @param random Random number (0-9) for probability distribution
     * @return Enemy type index: 0 (Spike), 1 (Goblin), 2 (Wolf), or -1 (no wolves)
     */
    int getEnemyTypeToSpawn(int random);

    /**
     * Get the display name of this difficulty
     */
    String getName();
}
