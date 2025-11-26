package battle.subsystems;

/**
 * Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN)
 *
 * Complex UI subsystem for battle sequences.
 * Client must initialize in CORRECT ORDER:
 * 1. init() - Initialize UI framework
 * 2. createBattleUI() - Create UI components
 * 3. showBattleScreen() - Display the UI
 * 4. Then can update UI elements
 *
 * Problem: Client must know these internals!
 */
public class BattleUISystem {
    private boolean initialized = false;
    private boolean uiCreated = false;
    private boolean screenVisible = false;

    /**
     * Step 1: Initialize UI system
     * MUST be called first!
     */
    public void init() {
        System.out.println("[UISystem] Initializing...");
        System.out.println("  → Setting up UI framework");
        System.out.println("  → Loading UI assets");
        initialized = true;
        System.out.println("  ✓ UI system ready");
    }

    /**
     * Step 2: Create battle UI components
     * REQUIRES: init() first!
     */
    public void createBattleUI() {
        if (!initialized) {
            System.out.println("[UISystem] ✗ ERROR: Not initialized!");
            System.out.println("  → Client forgot to call init() first!");
            return;
        }
        System.out.println("[UISystem] Creating battle UI...");
        System.out.println("  → Creating health bars");
        System.out.println("  → Creating action menu");
        System.out.println("  → Creating status panel");
        uiCreated = true;
        System.out.println("  ✓ UI components created");
    }

    /**
     * Step 3: Show battle screen
     * REQUIRES: createBattleUI() first!
     */
    public void showBattleScreen() {
        if (!uiCreated) {
            System.out.println("[UISystem] ✗ ERROR: UI not created!");
            System.out.println("  → Client forgot to call createBattleUI() first!");
            return;
        }
        System.out.println("[UISystem] Showing battle screen...");
        screenVisible = true;
        System.out.println("  ✓ Battle screen visible");
    }

    /**
     * Update health bars
     * REQUIRES: showBattleScreen() first!
     */
    public void updateHealthBars(int playerHp, int bossHp) {
        if (!screenVisible) {
            System.out.println("[UISystem] ✗ ERROR: Screen not visible!");
            return;
        }
        System.out.println("[UISystem] Health - Player: " + playerHp + " HP | Boss: " + bossHp + " HP");
    }

    /**
     * Show action menu
     */
    public void showActionMenu() {
        if (!screenVisible) {
            System.out.println("[UISystem] ✗ ERROR: Screen not visible!");
            return;
        }
        System.out.println("[UISystem] ┌─────────────────┐");
        System.out.println("[UISystem] │ 1. Attack       │");
        System.out.println("[UISystem] │ 2. Defend       │");
        System.out.println("[UISystem] │ 3. Magic        │");
        System.out.println("[UISystem] └─────────────────┘");
    }

    /**
     * Show damage number
     */
    public void showDamageNumber(String target, int damage) {
        if (!screenVisible) {
            System.out.println("[UISystem] ✗ ERROR: Screen not visible!");
            return;
        }
        System.out.println("[UISystem] " + target + ": -" + damage + " HP");
    }

    /**
     * Show victory screen
     */
    public void showVictoryScreen() {
        if (!screenVisible) {
            System.out.println("[UISystem] ✗ ERROR: Screen not visible!");
            return;
        }
        System.out.println("[UISystem] ╔═══════════════════╗");
        System.out.println("[UISystem] ║    VICTORY!       ║");
        System.out.println("[UISystem] ╚═══════════════════╝");
    }

    /**
     * Show defeat screen
     */
    public void showDefeatScreen() {
        if (!screenVisible) {
            System.out.println("[UISystem] ✗ ERROR: Screen not visible!");
            return;
        }
        System.out.println("[UISystem] ╔═══════════════════╗");
        System.out.println("[UISystem] ║    GAME OVER      ║");
        System.out.println("[UISystem] ╚═══════════════════╝");
    }

    /**
     * Cleanup - client must call this!
     */
    public void cleanup() {
        System.out.println("[UISystem] Cleaning up...");
        if (screenVisible) {
            System.out.println("  → Hiding screen");
            screenVisible = false;
        }
        if (uiCreated) {
            System.out.println("  → Destroying UI components");
            uiCreated = false;
        }
        if (initialized) {
            System.out.println("  → Shutting down UI framework");
            initialized = false;
        }
        System.out.println("  ✓ UI system cleaned up");
    }
}
