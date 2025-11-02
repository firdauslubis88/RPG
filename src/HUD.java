import entities.GameManager;

/**
 * ✅ SOLUTION: HUD now uses Singleton - no constructor parameter!
 *
 * Before (09-02): Received manager parameter but created own instance (bug!)
 * Now (09-03): Uses getInstance() - guaranteed to be the correct instance
 *
 * This fixes the state inconsistency bug.
 */
public class HUD {
    /**
     * ✅ SOLUTION: No constructor parameter needed!
     *
     * Before: HUD(manager) but ignored parameter and created new instance
     * Now: No parameter, uses getInstance() when needed
     */
    public HUD() {
        // Constructor intentionally empty - uses Singleton pattern
    }

    /**
     * ✅ Draws the HUD with game stats from THE instance.
     * Week 10: Added HP display for obstacle damage tracking
     * Uses ANSI cursor positioning to draw HUD at fixed position (no scrolling)
     * HUD is positioned to the right of the 25x25 map grid
     */
    public void draw() {
        // ✅ Reads from THE instance!
        int score = GameManager.getInstance().getScore();
        float time = GameManager.getInstance().getGameTime();
        int level = GameManager.getInstance().getLevel();
        int hp = GameManager.getInstance().getHp();

        // Position HUD to the right of the map
        // Map is 25 chars wide + 2 for borders = 27 chars total
        int startCol = 28;  // Start HUD at column 28 (right of map)
        int startRow = 2;   // Start from top (row 2)

        // Draw HUD using ANSI cursor positioning (no scrolling!)
        System.out.print(String.format("\033[%d;%dH", startRow, startCol));
        System.out.print("╔════════════════════════════╗");

        System.out.print(String.format("\033[%d;%dH", startRow + 1, startCol));
        System.out.print("║       HUD DISPLAY          ║");

        System.out.print(String.format("\033[%d;%dH", startRow + 2, startCol));
        System.out.print("╠════════════════════════════╣");

        System.out.print(String.format("\033[%d;%dH", startRow + 3, startCol));
        System.out.print(String.format("║  Score: %-18d║", score));

        System.out.print(String.format("\033[%d;%dH", startRow + 4, startCol));
        System.out.print(String.format("║  HP: %d / %-16d║", hp, 100));

        System.out.print(String.format("\033[%d;%dH", startRow + 5, startCol));
        System.out.print(String.format("║  Time: %-19ds║", (int)time));

        System.out.print(String.format("\033[%d;%dH", startRow + 6, startCol));
        System.out.print(String.format("║  Level: %-18d║", level));

        System.out.print(String.format("\033[%d;%dH", startRow + 7, startCol));
        System.out.print("╚════════════════════════════╝");

        System.out.flush();
    }
}
