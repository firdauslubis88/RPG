package entities;

/**
 * Collectible coin that falls from top to bottom.
 * Uses float for precise delta-time based movement.
 */
public class Coin {
    private float x;
    private float y;
    private float fallSpeed;  // Pixels per second

    /**
     * Creates a coin at the specified position with fall speed.
     * @param x Initial X coordinate
     * @param y Initial Y coordinate
     * @param fallSpeed Fall speed in pixels per second
     */
    public Coin(float x, float y, float fallSpeed) {
        this.x = x;
        this.y = y;
        this.fallSpeed = fallSpeed;
    }

    // Getters and setters
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getFallSpeed() {
        return fallSpeed;
    }

    public void setFallSpeed(float fallSpeed) {
        this.fallSpeed = fallSpeed;
    }
}
