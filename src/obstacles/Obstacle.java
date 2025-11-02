package obstacles;

/**
 * Obstacle interface - base contract for all game obstacles
 *
 * Obstacles are entities that can harm or block the NPC.
 * Different obstacle types have different behaviors.
 */
public interface Obstacle {
    /**
     * Update obstacle state based on delta time
     * @param delta Time elapsed since last frame (in seconds)
     */
    void update(float delta);

    /**
     * Get X coordinate (column) on grid
     * @return X position (0-9)
     */
    int getX();

    /**
     * Get Y coordinate (row) on grid
     * @return Y position (0-9)
     */
    int getY();

    /**
     * Get damage inflicted on contact
     * @return Damage amount
     */
    int getDamage();

    /**
     * Check if obstacle is still active
     * @return true if active, false if should be removed
     */
    boolean isActive();

    /**
     * Get display character for rendering
     * @return Character to display on grid
     */
    char getSymbol();
}
