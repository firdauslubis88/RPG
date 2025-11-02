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
     * Uses ANSI cursor positioning to avoid overlapping output
     */
    public void draw() {
        // ✅ Reads from THE instance!
        int score = GameManager.getInstance().getScore();
        float time = GameManager.getInstance().getGameTime();
        int level = GameManager.getInstance().getLevel();
        int hp = GameManager.getInstance().getHp();

        // Use System.out.print to avoid automatic newlines that cause scrolling
        System.out.print("\n╔════════════════════════════════════════╗\n");
        System.out.print("║              HUD DISPLAY               ║\n");
        System.out.print("╠════════════════════════════════════════╣\n");
        System.out.print(String.format("║  Score: %d points%n", score));
        System.out.print(String.format("║  HP: %d / 100%n", hp));
        System.out.print(String.format("║  Time: %ds%n", (int)time));
        System.out.print(String.format("║  Level: %d%n", level));
        System.out.print("╚════════════════════════════════════════╝\n");
        System.out.flush();
    }
}
