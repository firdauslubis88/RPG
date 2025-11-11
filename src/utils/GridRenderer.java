package utils;

/**
 * Static utility class for rendering the game grid to terminal.
 * Uses simple character-based representation.
 *
 * Week 10: Grid dimensions inferred from grid array passed in (dynamic sizing)
 * This eliminates hard-coded constants - grid size comes from DungeonMap
 *
 * Week 11: Added double buffering to reduce flickering
 */
public class GridRenderer {
    private static final char EMPTY_CELL = '░';

    // Cache grid dimensions (set when drawGrid is called)
    private static int cachedWidth = 25;
    private static int cachedHeight = 25;

    // Week 11: Double buffering to reduce flickering
    private static StringBuilder renderBuffer = new StringBuilder(1024);
    private static boolean bufferingEnabled = false;

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
     * Week 11: Starts buffering mode - all draw calls are accumulated
     */
    public static void beginFrame() {
        bufferingEnabled = true;
        renderBuffer.setLength(0);  // Clear buffer
    }

    /**
     * Week 11: Flushes buffer to screen - reduces flickering
     */
    public static void endFrame() {
        if (bufferingEnabled && renderBuffer.length() > 0) {
            // Week 11: Hide cursor to prevent input echo from appearing on screen
            renderBuffer.append("\033[?25l");  // Hide cursor

            System.out.print(renderBuffer.toString());
            System.out.flush();
        }
        bufferingEnabled = false;
    }

    /**
     * Clears a specific cell by drawing empty cell character.
     * @param x X coordinate
     * @param y Y coordinate
     */
    public static void clearCell(int x, int y) {
        if (x >= 0 && x < cachedWidth && y >= 0 && y < cachedHeight) {
            if (bufferingEnabled) {
                renderBuffer.append(String.format("\033[%d;%dH", y + 1, x + 1));
                renderBuffer.append(EMPTY_CELL);
            } else {
                moveCursor(x, y);
                System.out.print(EMPTY_CELL);
                System.out.flush();
            }
        }
    }

    /**
     * Draws a character at specific cell position.
     * @param symbol Character to draw
     * @param x X coordinate
     * @param y Y coordinate
     */
    public static void drawCell(char symbol, int x, int y) {
        if (x >= 0 && x < cachedWidth && y >= 0 && y < cachedHeight) {
            if (bufferingEnabled) {
                // Week 11: Buffer the output instead of immediate flush
                renderBuffer.append(String.format("\033[%d;%dH", y + 1, x + 1));
                renderBuffer.append(symbol);
            } else {
                moveCursor(x, y);
                System.out.print(symbol);
                System.out.flush();
            }
        }
    }

    /**
     * Moves cursor to position after the grid for drawing HUD.
     * @param offsetY How many lines below the grid
     */
    public static void moveCursorBelowGrid(int offsetY) {
        System.out.print(String.format("\033[%d;1H", cachedHeight + offsetY + 1));
    }

    /**
     * Week 11: Append text to buffer at specific position
     * Used by HUD to integrate with buffering system
     */
    public static void drawText(String text, int x, int y) {
        if (bufferingEnabled) {
            renderBuffer.append(String.format("\033[%d;%dH", y + 1, x + 1));
            renderBuffer.append(text);
        } else {
            System.out.print(String.format("\033[%d;%dH", y + 1, x + 1));
            System.out.print(text);
            System.out.flush();
        }
    }

    /**
     * Week 11: Draw notification at fixed position (integrated with buffering)
     * Clears the line first, then writes the notification
     * @param notification Message to display
     * @param row Row position for notification
     */
    public static void drawNotification(String notification, int row) {
        if (bufferingEnabled) {
            // Clear notification line first
            renderBuffer.append(String.format("\033[%d;1H", row));
            renderBuffer.append("                                                                  ");  // Clear line
            // Draw notification at same position
            renderBuffer.append(String.format("\033[%d;1H", row));
            renderBuffer.append(notification);
        } else {
            // Clear notification line first
            System.out.print(String.format("\033[%d;1H", row));
            System.out.print("                                                                  ");  // Clear line
            // Draw notification at same position
            System.out.print(String.format("\033[%d;1H", row));
            System.out.print(notification);
            System.out.flush();
        }
    }

    /**
     * Draws the entire grid with entities at their positions.
     * @param entities Array of entity characters and their positions
     */
    public static void drawGrid(char[][] grid) {
        // Update cached dimensions from grid array
        if (grid.length > 0) {
            cachedHeight = grid.length;
            cachedWidth = grid[0].length;
        }

        for (int y = 0; y < cachedHeight; y++) {
            for (int x = 0; x < cachedWidth; x++) {
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
        char[][] grid = new char[cachedHeight][cachedWidth];
        for (int y = 0; y < cachedHeight; y++) {
            for (int x = 0; x < cachedWidth; x++) {
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
        if (x >= 0 && x < cachedWidth && y >= 0 && y < cachedHeight) {
            grid[y][x] = symbol;
        }
    }

    public static int getGridWidth() {
        return cachedWidth;
    }

    public static int getGridHeight() {
        return cachedHeight;
    }
}
