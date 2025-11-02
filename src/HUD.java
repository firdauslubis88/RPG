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
     */
    public void draw() {
        // ✅ Reads from THE instance!
        int score = GameManager.getInstance().getScore();
        float time = GameManager.getInstance().getGameTime();
        int level = GameManager.getInstance().getLevel();
        int hp = GameManager.getInstance().getHp();

        // Get the grid height to position HUD below it
        int startRow = 27;  // Row 27 is below 25x25 grid + 1 line gap

        // Draw HUD using ANSI cursor positioning (no scrolling!)
        System.out.print(String.format("\033[%d;1H", startRow));
        System.out.print("╔════════════════════════════════════════╗");

        System.out.print(String.format("\033[%d;1H", startRow + 1));
        System.out.print("║              HUD DISPLAY               ║");

        System.out.print(String.format("\033[%d;1H", startRow + 2));
        System.out.print("╠════════════════════════════════════════╣");

        System.out.print(String.format("\033[%d;1H", startRow + 3));
        System.out.print(String.format("║  Score: %-29d║", score));

        System.out.print(String.format("\033[%d;1H", startRow + 4));
        System.out.print(String.format("║  HP: %d / %-27d║", hp, 100));

        System.out.print(String.format("\033[%d;1H", startRow + 5));
        System.out.print(String.format("║  Time: %-30ds║", (int)time));

        System.out.print(String.format("\033[%d;1H", startRow + 6));
        System.out.print(String.format("║  Level: %-29d║", level));

        System.out.print(String.format("\033[%d;1H", startRow + 7));
        System.out.print("╚════════════════════════════════════════╝");

        System.out.flush();
    }
}
