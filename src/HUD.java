import entities.GameManager;

/**
 * ❌ PROBLEM: HUD creates its OWN GameManager instance!
 *
 * This class INTENTIONALLY demonstrates a common bug:
 * - Constructor receives a GameManager parameter
 * - But IGNORES it and creates a new instance
 * - Result: HUD reads from wrong instance, shows wrong data
 *
 * This simulates what happens when:
 * - Developer doesn't understand shared state
 * - Copy-paste error from template code
 * - Refactoring mistake
 *
 * This is INTENTIONALLY BAD code for educational purposes!
 */
public class HUD {
    // ❌ BUG: Creates NEW instance instead of using passed one!
    private final GameManager manager = new GameManager();

    /**
     * ❌ PROBLEM: Receives manager parameter but IGNORES IT!
     *
     * This is the bug! Should use passedManager, but doesn't.
     */
    public HUD(GameManager passedManager) {
        // ❌ Intentionally ignore the parameter!
        // This simulates a developer mistake.
        //
        // The correct code would be:
        //   this.manager = passedManager;
        //
        // But we want to demonstrate the bug, so we DON'T do that.

        System.out.println("[HUD] Created with manager instance: " + manager.hashCode());
        System.out.println("[HUD] Received manager instance: " + passedManager.hashCode());
        System.out.println("[HUD] ❌ BUG: Using own instance, not received instance!");
    }

    /**
     * Draws the HUD with game stats.
     * Reads from WRONG instance!
     */
    public void draw() {
        // ❌ Reads from its own instance, not the shared one!
        int score = manager.getScore();
        float time = manager.getGameTime();
        int level = manager.getLevel();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║              HUD DISPLAY               ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("  Score: %d points%n", score);
        System.out.printf("  Time: %ds%n", (int)time);
        System.out.printf("  Level: %d%n", level);
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("  ❌ BUG: Reading from wrong instance!");
        System.out.printf("  Manager hashCode: %d%n", manager.hashCode());
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
