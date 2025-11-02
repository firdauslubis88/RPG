package entities;


/**
 * Non-Player Character that auto-moves from left to right.
 * Uses float for precise delta-time based movement.
 *
 * ✅ SOLUTION: No longer requires GameManager parameter!
 */
public class NPC {
    private float x;
    private float y;
    private float velocity;  // Pixels per second

    /**
     * ✅ SOLUTION: Constructor no longer needs GameManager parameter!
     *
     * Before (09-02): NPC(manager) for object drilling
     * Now (09-03): Clean constructor, no parameters needed
     */
    public NPC() {
        this.x = 0;
        this.y = 5;
        this.velocity = 2.0f;  // Pixels per second
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
