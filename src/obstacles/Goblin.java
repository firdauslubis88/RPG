package obstacles;

/**
 * Goblin - Patrol obstacle that moves left-right
 *
 * Week 10 Branch 10-01: Hard-coded spawning demo
 *
 * Behavior: Moves horizontally, respects walls
 * Damage: 15 HP
 * Symbol: 'G' (Goblin)
 * Movement: Patrol pattern (horizontal only)
 */
public class Goblin implements Obstacle {
    private float x;
    private float y;
    private final float velocity = 2.0f;  // Grid units per second
    private int direction = 1;  // 1 = right, -1 = left
    private final int damage = 15;
    private boolean active = true;
    private float moveTimer = 0;
    private final float moveInterval = 0.5f;  // Move every 0.5 seconds

    public Goblin(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    @Override
    public void update(float delta) {
        moveTimer += delta;

        if (moveTimer >= moveInterval) {
            moveTimer = 0;

            // Try to move in current direction
            int newX = Math.round(x) + direction;
            int currentY = Math.round(y);

            // Check if new position is valid (30x30 map)
            if (newX > 0 && newX < 29) {
                x = newX;
            } else {
                // Hit boundary, reverse direction
                direction *= -1;
            }
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
