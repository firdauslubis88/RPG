package obstacles;

import entities.NPC;

/**
 * Wolf - Chase obstacle that follows the NPC
 *
 * Week 10 Branch 10-01: Hard-coded spawning demo
 *
 * Behavior: Moves towards NPC if within detection range
 * Damage: 25 HP
 * Symbol: 'W' (Wolf)
 * Movement: Chase pattern (moves towards target)
 */
public class Wolf implements Obstacle {
    private float x;
    private float y;
    private final float speed = 2.5f;  // Grid units per second
    private final float detectionRange = 5.0f;  // Grid units
    private final int damage = 25;
    private boolean active = true;
    private NPC target;  // Reference to NPC to chase

    public Wolf(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * Set the target NPC to chase
     * @param target NPC to follow
     */
    public void setTarget(NPC target) {
        this.target = target;
    }

    @Override
    public void update(float delta) {
        if (target == null) {
            return;  // No target, no movement
        }

        // Calculate distance to target
        float dx = target.getX() - x;
        float dy = target.getY() - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Only chase if within detection range
        if (distance < detectionRange && distance > 0.5f) {
            // Normalize direction and move
            float dirX = dx / distance;
            float dirY = dy / distance;

            float newX = x + dirX * speed * delta;
            float newY = y + dirY * speed * delta;

            // Clamp to walkable area (25x25 map)
            newX = Math.max(1, Math.min(23, newX));
            newY = Math.max(1, Math.min(23, newY));

            x = newX;
            y = newY;
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
        return 'W';
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
