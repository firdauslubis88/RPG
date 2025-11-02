package entities;

/**
 * Week 10: Player character that can move in dungeon.
 * Movement respects wall boundaries defined in DungeonMap.
 */
public class NPC {
    private int x;
    private int y;

    /**
     * Create NPC at starting position (1, 1) - walkable floor
     */
    public NPC() {
        this.x = 1;
        this.y = 1;
    }

    /**
     * Try to move to new position. Only moves if target is walkable.
     * Returns true if move was successful.
     */
    public boolean tryMove(int newX, int newY) {
        // Check if target position is walkable (not wall)
        if (isValidPosition(newX, newY)) {
            this.x = newX;
            this.y = newY;
            return true;
        }
        return false;
    }

    /**
     * Check if position is valid (in bounds and walkable)
     */
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        if (isValidPosition(x, this.y)) {
            this.x = x;
        }
    }

    public void setY(int y) {
        if (isValidPosition(this.x, y)) {
            this.y = y;
        }
    }
}
