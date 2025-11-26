package world;

/**
 * Week 13: Abstract GameMap for different level layouts
 *
 * Option B+ (Hybrid Approach):
 * - Abstract class provides polymorphism for map layouts
 * - DungeonMap remains as static manager to minimize refactoring
 * - Each concrete map has different layout, characters, music
 *
 * This demonstrates polymorphism without changing all 20+ files
 * that currently use DungeonMap's static methods.
 */
public abstract class GameMap {
    protected static final int WIDTH = 25;
    protected static final int HEIGHT = 25;

    /**
     * Get the map layout (25x25 grid)
     * @return 2D char array representing the map
     */
    public abstract char[][] getLayout();

    /**
     * Get the wall character for this map
     * @return Character used for walls
     */
    public abstract char getWallChar();

    /**
     * Get the floor character for this map
     * @return Character used for floors
     */
    public abstract char getFloorChar();

    /**
     * Get the music file/description for this level
     * @return Music description or filename
     */
    public abstract String getMusic();

    /**
     * Get the level name
     * @return Name of the level
     */
    public abstract String getLevelName();

    /**
     * Get enemy character for this level's goblins
     * @return Character representing goblins
     */
    public abstract char getGoblinChar();

    /**
     * Get enemy character for this level's wolves
     * @return Character representing wolves
     */
    public abstract char getWolfChar();

    /**
     * Get enemy character for this level's spikes
     * @return Character representing spikes
     */
    public abstract char getSpikeChar();

    /**
     * Check if a position is walkable
     */
    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return false;
        }
        char[][] layout = getLayout();
        return layout[y][x] == getFloorChar();
    }

    /**
     * Get tile at position
     */
    public char getTile(int x, int y) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return getWallChar();
        }
        return getLayout()[y][x];
    }

    /**
     * Get a copy of the map for rendering
     */
    public char[][] getMapCopy() {
        char[][] layout = getLayout();
        char[][] copy = new char[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                copy[y][x] = layout[y][x];
            }
        }
        return copy;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}
