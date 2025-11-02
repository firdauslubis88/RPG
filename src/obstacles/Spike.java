package obstacles;

/**
 * Spike - Static obstacle that doesn't move
 *
 * Week 10 Branch 10-01: Hard-coded spawning demo
 *
 * Behavior: Stays in one position
 * Damage: 20 HP
 * Symbol: '^' (looks like spike pointing up)
 */
public class Spike implements Obstacle {
    private int x;  // Changed from final for factory pattern support
    private int y;  // Changed from final for factory pattern support
    private final int damage = 20;
    private boolean active = true;

    public Spike(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(float delta) {
        // Static obstacle - no movement logic
        // Could add animation frames here in future
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public char getSymbol() {
        return '^';
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Reset spike state for reuse in object pool
     * Week 10 Branch 10-04: Object Pool pattern support
     */
    @Override
    public void reset(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.active = true;
        // No additional state to reset for Spike
    }
}
