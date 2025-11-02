package entities;


/**
 * Non-Player Character that auto-moves from left to right.
 * Uses float for precise delta-time based movement.
 *
 * ❌ PROBLEM: Now requires GameManager parameter (object drilling).
 */
public class NPC {
    private float x;
    private float y;
    private float velocity;  // Pixels per second
    private final GameManager manager;  // ❌ Dependency

    /**
     * ❌ PROBLEM: Constructor requires GameManager parameter!
     *
     * Before: Simple constructor
     * Now: Must pass manager from 3 levels up!
     *
     * @param manager The GameManager instance (passed down from Main)
     */
    public NPC(GameManager manager) {
        this.manager = manager;
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
