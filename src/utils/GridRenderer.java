package utils;

/**
 * Static utility class for rendering the game grid to terminal.
 * Uses simple character-based representation.
 */
public class GridRenderer {
    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    private static final char EMPTY_CELL = '░';

    /**
     * Clears the terminal screen using newlines.
     * Note: In real implementation, could use ANSI escape codes.
     */
    public static void clearScreen() {
        // Simple approach: print many newlines
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
        // Alternative ANSI approach (commented out for compatibility):
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
    }

    /**
     * Draws the entire grid with entities at their positions.
     * @param entities Array of entity characters and their positions
     */
    public static void drawGrid(char[][] grid) {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }

    /**
     * Creates an empty grid filled with EMPTY_CELL character.
     * @return 2D char array representing empty grid
     */
    public static char[][] createEmptyGrid() {
        char[][] grid = new char[GRID_HEIGHT][GRID_WIDTH];
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                grid[y][x] = EMPTY_CELL;
            }
        }
        return grid;
    }

    /**
     * Places an entity symbol on the grid at specified position.
     * @param grid The grid to modify
     * @param symbol The character representing the entity
     * @param x X coordinate
     * @param y Y coordinate
     */
    public static void drawEntity(char[][] grid, char symbol, int x, int y) {
        if (x >= 0 && x < GRID_WIDTH && y >= 0 && y < GRID_HEIGHT) {
            grid[y][x] = symbol;
        }
    }

    /**
     * Draws a single entity directly to console (for mixed update/render demo).
     * This is intentionally bad practice for demonstration purposes.
     * @param symbol The character representing the entity
     * @param x X coordinate
     * @param y Y coordinate
     */
    public static void drawEntityDirect(char symbol, int x, int y) {
        // ❌ PROBLEM: Direct rendering mixed with game logic
        System.out.println("Drew " + symbol + " at (" + x + ", " + y + ")");
    }

    public static int getGridWidth() {
        return GRID_WIDTH;
    }

    public static int getGridHeight() {
        return GRID_HEIGHT;
    }
}
