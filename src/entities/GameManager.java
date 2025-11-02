package entities;

/**
 * ❌ PROBLEM: Global game state WITHOUT singleton protection!
 *
 * This class demonstrates what happens when you need global state
 * but don't use Singleton pattern:
 * - PUBLIC constructor → anyone can create instances
 * - Multiple instances → state inconsistency
 * - Object drilling → parameter pollution
 * - Testing overhead → complex setup
 *
 * This is INTENTIONALLY BAD code for educational purposes!
 */
public class GameManager {
    private int score;
    private float gameTime;
    private int level;
    private boolean gameOver;

    /**
     * ❌ PROBLEM: PUBLIC constructor allows multiple instances!
     *
     * Anyone can do:
     *   GameManager m1 = new GameManager();
     *   GameManager m2 = new GameManager();
     *
     * Result: Two separate states! Chaos!
     */
    public GameManager() {
        this.score = 0;
        this.gameTime = 0.0f;
        this.level = 1;
        this.gameOver = false;

        // Debug: Show when instance is created
        System.out.println("[DEBUG] GameManager instance created: " + this.hashCode());
    }

    /**
     * Adds points to the score.
     * But which instance's score? That's the problem!
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
     * Reset for testing (but which instance gets reset?).
     */
    public void reset() {
        this.score = 0;
        this.gameTime = 0.0f;
        this.level = 1;
        this.gameOver = false;
    }
}
