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
    private int hp;  // Week 10: HP for obstacle damage
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
        this.hp = 100;  // Week 10: Start with 100 HP
        this.gameOver = false;
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
     * Week 10: HP management for obstacle damage
     */
    public int getHp() {
        return this.hp;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }

        if (this.hp <= 0) {
            this.gameOver = true;
        }
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
        this.hp = 100;
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
