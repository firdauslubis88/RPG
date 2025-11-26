package obstacles;

import entities.Entity;
import world.DungeonMap;

/**
 * Wolf - Chase obstacle that follows the player
 *
 * Week 13: Uses DungeonMap.getWolfChar() for level-specific appearance
 *
 * Behavior: Moves towards target if within detection range
 * Damage: 25 HP
 * Symbol: Dynamic based on level (e.g., 'w' dungeon, 'W' forest, 'G' castle ghost)
 * Movement: Chase pattern (moves towards target)
 */
public class Wolf implements Obstacle {
    private float x;
    private float y;
    private final float speed = 1.0f;  // Week 11: Reduced from 2.5 (too fast)
    private final float detectionRange = 5.0f;  // Grid units
    private final int damage = 25;
    private boolean active = true;
    private Entity target;  // Week 11: Reference to Entity (Player/NPC) to chase

    public Wolf(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * Week 11: Set the target entity to chase
     * @param target Entity (Player/NPC) to follow
     */
    public void setTarget(Entity target) {
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
        // Week 13: Dynamic symbol based on active map
        return DungeonMap.getWolfChar();
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
