package obstacles;

/**
 * Goblin - Patrol obstacle that moves left-right
 *
 * Week 10 Branch 10-01: Hard-coded spawning demo
 *
 * Behavior: Moves horizontally, reverses at grid edges
 * Damage: 15 HP
 * Symbol: 'G' (Goblin)
 * Movement: Patrol pattern (horizontal only)
 */
public class Goblin implements Obstacle {
    private float x;
    private float y;
    private final float velocity = 3.0f;  // Grid units per second
    private int direction = 1;  // 1 = right, -1 = left
    private final int damage = 15;
    private boolean active = true;

    public Goblin(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    @Override
    public void update(float delta) {
        // Move horizontally
        x += velocity * direction * delta;

        // Reverse direction at grid boundaries
        if (x >= 9.0f) {
            x = 9.0f;
            direction = -1;  // Go left
        } else if (x <= 0.0f) {
            x = 0.0f;
            direction = 1;   // Go right
        }
    }

    @Override
    public int getX() {
        return Math.round(x);
    }

    @Override
    public int getY() {
        return Math.round(y);
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
        return 'G';
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
