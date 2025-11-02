package entities;

/**
 * Non-Player Character that auto-moves from left to right.
 * Uses float for precise delta-time based movement.
 */
public class NPC {
    private float x;
    private float y;
    private float velocity;  // Pixels per second

    /**
     * Creates an NPC at the specified position with velocity.
     * @param x Initial X coordinate
     * @param y Initial Y coordinate
     * @param velocity Movement speed in pixels per second
     */
    public NPC(float x, float y, float velocity) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
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

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}
