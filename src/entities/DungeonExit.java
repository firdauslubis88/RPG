package entities;

/**
 * DungeonExit - Exit point to trigger boss battle
 *
 * Week 12-01: Added for future boss battle feature
 * Serves as trigger point for state transition to BossBattle
 */
public class DungeonExit implements Entity {
    private int x;
    private int y;
    private final char symbol = 'D';

    public DungeonExit(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public char getSymbol() { return symbol; }

    public void update(float delta) {
        // Dungeon exit is static
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
