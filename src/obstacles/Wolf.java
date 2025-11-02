package obstacles;

import entities.NPC;
import world.DungeonMap;

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

            // Round to grid coordinates
            int targetX = Math.round(newX);
            int targetY = Math.round(newY);

            // Only move if target position is walkable (not wall)
            if (DungeonMap.isWalkable(targetX, targetY)) {
                x = newX;
                y = newY;
            }
            // If blocked by wall, try moving in only X or Y direction
            else {
                // Try moving only horizontally
                int tryX = Math.round(x + dirX * speed * delta);
                int tryY = Math.round(y);
                if (DungeonMap.isWalkable(tryX, tryY)) {
                    x = x + dirX * speed * delta;
                }
                // Try moving only vertically
                else {
                    tryX = Math.round(x);
                    tryY = Math.round(y + dirY * speed * delta);
                    if (DungeonMap.isWalkable(tryX, tryY)) {
                        y = y + dirY * speed * delta;
                    }
                }
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
        return 'W';
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * Reset wolf state for reuse in object pool
     * Week 10 Branch 10-04: Object Pool pattern support
     */
    @Override
    public void reset(int newX, int newY) {
        this.x = (float) newX;
        this.y = (float) newY;
        this.active = true;
        this.target = null;  // Reset target (will be set by WorldController)
    }
}
