package entities;

/**
 * Collectible coin that falls from top to bottom.
 * Respawns at random X position when collected or falls off screen.
 */
public class Coin {
    private int x;
    private int y;
    private int fallSpeed;
    private boolean collected;

    /**
     * Creates a coin at the specified position.
     * @param x Initial X coordinate
     * @param y Initial Y coordinate
     */
    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
        this.fallSpeed = 1; // Fixed speed: 1 pixel per frame
        this.collected = false;
    }

    /**
     * Makes the coin fall downward by its fall speed.
     */
    public void fall() {
        y += fallSpeed;
    }

    /**
     * Respawns the coin at a new X position at the top of the screen.
     * @param newX The new X coordinate
     */
    public void respawn(int newX) {
        this.x = newX;
        this.y = 0;
        this.collected = false;
    }

    /**
     * Checks if coin has fallen off the bottom of the screen.
     * @param gridHeight The height of the game grid
     * @return true if coin is below the grid
     */
    public boolean isOffScreen(int gridHeight) {
        return y >= gridHeight;
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFallSpeed() {
        return fallSpeed;
    }

    public void setFallSpeed(int fallSpeed) {
        this.fallSpeed = fallSpeed;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
