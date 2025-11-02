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
        System.out.println("[HUD] Using Singleton - no parameters needed!");
        System.out.println("[HUD] Singleton instance: " + GameManager.getInstance().hashCode());
    }

    /**
     * ✅ Draws the HUD with game stats from THE instance.
     * Week 10: Added HP display for obstacle damage tracking
     */
    public void draw() {
        // ✅ Reads from THE instance!
        int score = GameManager.getInstance().getScore();
        float time = GameManager.getInstance().getGameTime();
        int level = GameManager.getInstance().getLevel();
        int hp = GameManager.getInstance().getHp();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║              HUD DISPLAY               ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║  Score: %d points%n", score);
        System.out.printf("║  HP: %d / 100%n", hp);
        System.out.printf("║  Time: %ds%n", (int)time);
        System.out.printf("║  Level: %d%n", level);
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  ✅ FIXED: Reading from THE instance!  ║");
        System.out.printf("║  Singleton hashCode: %d%n", GameManager.getInstance().hashCode());
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
