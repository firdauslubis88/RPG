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
     * Clears the terminal screen using ANSI escape codes.
     * This moves cursor to home and clears the entire screen.
     * Only used for initial setup.
     */
    public static void clearScreen() {
        // ANSI escape codes for clearing screen
        // \033[H - Move cursor to home (0,0)
        // \033[2J - Clear entire screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Moves cursor to specific grid position using ANSI escape codes.
     * @param x X coordinate (0-based)
     * @param y Y coordinate (0-based)
     */
    public static void moveCursor(int x, int y) {
        // ANSI escape code: \033[row;colH
        // Note: ANSI uses 1-based indexing, so we add 1
        System.out.print(String.format("\033[%d;%dH", y + 1, x + 1));
    }

    /**
     * Clears a specific cell by drawing empty cell character.
     * @param x X coordinate
     * @param y Y coordinate
     */
    public static void clearCell(int x, int y) {
        if (x >= 0 && x < GRID_WIDTH && y >= 0 && y < GRID_HEIGHT) {
            moveCursor(x, y);
            System.out.print(EMPTY_CELL);
            System.out.flush();
        }
    }

    /**
     * Draws a character at specific cell position.
     * @param symbol Character to draw
     * @param x X coordinate
     * @param y Y coordinate
     */
    public static void drawCell(char symbol, int x, int y) {
        if (x >= 0 && x < GRID_WIDTH && y >= 0 && y < GRID_HEIGHT) {
            moveCursor(x, y);
            System.out.print(symbol);
            System.out.flush();
        }
    }

    /**
     * Moves cursor to position after the grid for drawing HUD.
     * @param offsetY How many lines below the grid
     */
    public static void moveCursorBelowGrid(int offsetY) {
        System.out.print(String.format("\033[%d;1H", GRID_HEIGHT + offsetY + 1));
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

    public static int getGridWidth() {
        return GRID_WIDTH;
    }

    public static int getGridHeight() {
        return GRID_HEIGHT;
    }
}
