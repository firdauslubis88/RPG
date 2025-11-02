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
    private final int x;
    private final int y;
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

    public void setActive(boolean active) {
        this.active = active;
    }
}
