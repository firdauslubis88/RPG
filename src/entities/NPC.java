package entities;

/**
 * Non-Player Character that auto-moves from left to right.
 * No player control in this version - purely autonomous movement.
 */
public class NPC {
    private int x;
    private int y;
    private int speed;

    /**
     * Creates an NPC at the specified position.
     * @param x Initial X coordinate
     * @param y Initial Y coordinate
     */
    public NPC(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 1; // Fixed speed: 1 pixel per frame
    }

    /**
     * Moves the NPC to the right by its speed value.
     */
    public void moveRight() {
        x += speed;
    }

    /**
     * Wraps the NPC position when it reaches the edge.
     * @param gridWidth The width of the game grid
     */
    public void wrapAtEdge(int gridWidth) {
        if (x >= gridWidth) {
            x = 0;
        }
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
