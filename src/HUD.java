import entities.GameManager;
import utils.GridRenderer;

/**
 * ✅ SOLUTION: HUD now uses Singleton - no constructor parameter!
 *
 * Before (09-02): Received manager parameter but created own instance (bug!)
 * Now (09-03): Uses getInstance() - guaranteed to be the correct instance
 *
 * This fixes the state inconsistency bug.
 *
 * Week 11: HUD now uses GridRenderer buffering to prevent screen jumping
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
     * Week 11: Uses GridRenderer buffering to prevent screen jumping
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

        // Week 11: Draw HUD using GridRenderer buffering (prevents jumping!)
        GridRenderer.drawText("╔════════════════════════════╗", startCol - 1, startRow - 1);
        GridRenderer.drawText("║       HUD DISPLAY          ║", startCol - 1, startRow);
        GridRenderer.drawText("╠════════════════════════════╣", startCol - 1, startRow + 1);
        GridRenderer.drawText(String.format("║  Score: %-18d║", score), startCol - 1, startRow + 2);
        GridRenderer.drawText(String.format("║  HP: %d / %-16d║", hp, 100), startCol - 1, startRow + 3);
        GridRenderer.drawText(String.format("║  Time: %-19ds║", (int)time), startCol - 1, startRow + 4);
        GridRenderer.drawText(String.format("║  Level: %-18d║", level), startCol - 1, startRow + 5);
        GridRenderer.drawText("╚════════════════════════════╝", startCol - 1, startRow + 6);
    }
}
