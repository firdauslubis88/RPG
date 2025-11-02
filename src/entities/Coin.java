package entities;

/**
 * Collectible coin that falls from top to bottom.
 * Uses float for precise delta-time based movement.
 *
 * ❌ PROBLEM: Now requires GameManager parameter (object drilling).
 */
public class Coin {
    private float x;
    private float y;
    private float fallSpeed;  // Pixels per second
    private final GameManager manager;  // ❌ Dependency

    /**
     * ❌ PROBLEM: Constructor requires GameManager parameter!
     *
     * Before: Simple constructor
     * Now: Must pass manager from 3 levels up!
     *
     * @param manager The GameManager instance (passed down from Main)
     */
    public Coin(GameManager manager) {
        this.manager = manager;
        this.x = (float)(Math.random() * 10);
        this.y = 0;
        this.fallSpeed = 3.0f;  // Pixels per second
    }

    /**
     * Respawns coin at top with random X position.
     */
    public void respawn() {
        this.y = 0;
        this.x = (float)(Math.random() * 10);
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
