package entities;

/**
 * ✅ SOLUTION: Global game state WITH singleton protection!
 *
 * This class demonstrates the Singleton pattern for global state management:
 * - PRIVATE constructor → only this class can create instances
 * - Single instance → consistent state everywhere
 * - Global access → no object drilling needed
 * - Easy testing → reset() controls the single instance
 *
 * This solves all problems from 09-02!
 */
public class GameManager {
    // ✅ SOLUTION: Single static instance (lazy initialization)
    private static GameManager instance = null;

    private int score;
    private float gameTime;
    private int level;
    private boolean gameOver;

    /**
     * ✅ SOLUTION: PRIVATE constructor prevents external instantiation!
     *
     * This means:
     *   GameManager m = new GameManager();  // ❌ Compiler error!
     *
     * Only getInstance() can create the instance.
     */
    private GameManager() {
        this.score = 0;
        this.gameTime = 0.0f;
        this.level = 1;
        this.gameOver = false;

        // Debug: Show when the SINGLE instance is created
        System.out.println("[DEBUG] GameManager singleton instance created: " + this.hashCode());
    }

    /**
     * ✅ SOLUTION: Global access point to the single instance.
     *
     * Lazy initialization: Instance created on first call.
     * Thread-safe: Not needed for single-threaded game (can add if needed).
     *
     * @return The single GameManager instance
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Adds points to the score.
     * Now guaranteed to update THE score (only one exists!).
     */
    public void addScore(int points) {
        this.score += points;
        System.out.println("[GameManager:" + this.hashCode() + "] Score updated: " + this.score);
    }

    public int getScore() {
        return this.score;
    }

    /**
     * Updates game time (called every frame).
     */
    public void updateTime(float delta) {
        this.gameTime += delta;
    }

    public float getGameTime() {
        return this.gameTime;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * ✅ SOLUTION: Reset for testing (resets THE instance).
     *
     * For testing, we can reset the single instance to clean state.
     * Optionally, add resetInstance() to fully recreate if needed.
     */
    public void reset() {
        this.score = 0;
        this.gameTime = 0.0f;
        this.level = 1;
        this.gameOver = false;
    }

    /**
     * ✅ BONUS: For testing - fully reset singleton instance.
     *
     * Use this in test teardown to ensure clean state between tests.
     */
    public static void resetInstance() {
        if (instance != null) {
            instance.reset();
        }
    }
}
