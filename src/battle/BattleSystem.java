package battle;

import entities.Player;
import entities.GameManager;
import java.util.Scanner;
import java.util.Random;

/**
 * BattleSystem - Strategic turn-based battle with STATE PATTERN
 *
 * Week 12-04: STATE PATTERN (SOLUTION)
 *
 * ✅ SOLUTION: Each boss state is a separate class
 * ✅ SOLUTION: State transitions handled by state objects
 * ✅ SOLUTION: AI and rules encapsulated together
 * ✅ BENEFIT: Easy to add new boss behaviors (just create new state class)
 * ✅ BENEFIT: Each state is independently testable
 *
 * Battle Mechanics:
 * - Player and boss choose actions simultaneously
 * - Actions: ATTACK, DEFEND, MAGIC, COUNTER
 * - Counter relationships change based on boss state!
 * - States: NORMAL (>75%), ANGRY (>50%), DEFENSIVE (>25%), ENRAGED (≤25%)
 *
 * Evolution from Week 12-03:
 * - Replaced giant if/else chains with polymorphic state objects
 * - Removed hardcoded HP thresholds from BattleSystem
 * - Each state encapsulates both AI and counter rules
 */
public class BattleSystem {
    private Player player;
    private int bossHp;
    private int bossMaxHp;
    private Scanner scanner;
    private Random random;
    private boolean isDemoMode;  // Demo mode: boss only defends

    // Week 12-04: ✅ STATE PATTERN - Current boss state!
    private BossState currentState;

    // Boss stats
    private static final int BOSS_MAX_HP = 200;
    private static final int PLAYER_MAX_HP = 100;  // GameManager max HP

    // Battle action constants
    private static final String ATTACK = "ATTACK";
    private static final String DEFEND = "DEFEND";
    private static final String MAGIC = "MAGIC";
    private static final String COUNTER = "COUNTER";
    private static final String RUN = "RUN";

    public BattleSystem(Player player) {
        this(player, false);  // Default: not demo mode
    }

    public BattleSystem(Player player, boolean isDemoMode) {
        this.player = player;
        this.bossHp = BOSS_MAX_HP;
        this.bossMaxHp = BOSS_MAX_HP;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.isDemoMode = isDemoMode;

        // Week 12-04: ✅ STATE PATTERN - Initialize with NORMAL state!
        this.currentState = NormalState.getInstance();
    }

    /**
     * Start turn-based battle
     * @return true if player wins, false if player loses or runs
     */
    public boolean startBattle() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║        ⚔️  BOSS BATTLE!  ⚔️           ║");
        System.out.println("║                                        ║");
        System.out.println("║   A fearsome boss blocks your exit!    ║");
        System.out.println("║                                        ║");
        System.out.println("║   Predict boss actions to counter!     ║");
        System.out.println("║   Rules change as boss HP drops!       ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        while (bossHp > 0 && GameManager.getInstance().getHp() > 0) {
            // Display battle status
            displayBattleStatus();

            // Player turn - choose action
            String playerAction = playerTurn();
            if (playerAction.equals(RUN)) {
                System.out.println("\n💨 You fled from the battle!");
                return false;
            }

            // Week 12-04: ✅ STATE PATTERN - Boss chooses action via state object!
            // Before (12-03): bossTurn() had giant if/else chain
            // Now (12-04): currentState.chooseAction(random) - polymorphism!
            String bossAction = isDemoMode ? DEFEND : currentState.chooseAction(random);

            System.out.println("\n🤖 Boss chose: " + bossAction);

            // Week 12-04: ✅ STATE PATTERN - State resolves actions with its own rules!
            // Before (12-03): resolveActions() had giant if/else chain
            // Now (12-04): currentState.resolveActions() - each state knows its rules!
            BattleContext context = new BattleContext(player, bossHp, bossMaxHp);
            currentState.resolveActions(playerAction, bossAction, context);

            // Update boss HP from context
            bossHp = context.getBossHp();

            // Week 12-04: ✅ STATE PATTERN - Check for state transitions!
            // Before (12-03): State determined by recalculating HP% every turn
            // Now (12-04): currentState.checkTransition() - state manages its own transitions!
            float hpPercent = (float) bossHp / bossMaxHp;
            BossState newState = currentState.checkTransition(hpPercent);
            if (newState != null) {
                currentState = newState;
            }

            // Check if boss is defeated
            if (bossHp <= 0) {
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║                                        ║");
                System.out.println("║        🎉  VICTORY!  🎉                ║");
                System.out.println("║                                        ║");
                System.out.println("║   You defeated the boss!               ║");
                System.out.println("║   The exit is now open!                ║");
                System.out.println("║                                        ║");
                System.out.println("╚════════════════════════════════════════╝");
                return true;
            }

            // Check if player is defeated
            if (GameManager.getInstance().getHp() <= 0) {
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║                                        ║");
                System.out.println("║        💀  DEFEAT!  💀                 ║");
                System.out.println("║                                        ║");
                System.out.println("║   You were defeated by the boss...     ║");
                System.out.println("║                                        ║");
                System.out.println("╚════════════════════════════════════════╝");
                return false;
            }

            // Pause between turns
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return false;
    }

    /**
     * Display current battle status
     */
    private void displayBattleStatus() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("📊 BATTLE STATUS");
        System.out.println("═══════════════════════════════════════════");

        // Week 12-04: ✅ STATE PATTERN - State name from state object!
        // Before (12-03): getBossState() had hardcoded if/else chain
        // Now (12-04): currentState.getStateName() - polymorphism!
        System.out.println("Boss State: " + currentState.getStateName());
        System.out.println("Boss HP: " + bossHp + "/" + bossMaxHp + " [" + getHealthBar(bossHp, bossMaxHp) + "]");
        System.out.println("\nYour HP: " + GameManager.getInstance().getHp() + "/" + PLAYER_MAX_HP +
                           " [" + getHealthBar(GameManager.getInstance().getHp(), PLAYER_MAX_HP) + "]");
        System.out.println("\n" + currentState.getRulesDescription());
        System.out.println("═══════════════════════════════════════════\n");
    }

    /**
     * Generate health bar visualization
     */
    private String getHealthBar(int current, int max) {
        int barLength = 20;
        int filled = (int) ((float) current / max * barLength);
        if (filled < 0) filled = 0;

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < filled; i++) {
            bar.append("█");
        }
        for (int i = filled; i < barLength; i++) {
            bar.append("░");
        }
        return bar.toString();
    }

    /**
     * Player's turn - choose action
     */
    private String playerTurn() {
        System.out.println("YOUR TURN - Choose your action:");
        System.out.println("1. ATTACK - Standard damage");
        System.out.println("2. DEFEND - Block and counter");
        System.out.println("3. MAGIC - Piercing spell");
        System.out.println("4. COUNTER - Risky, high damage");
        System.out.println("5. RUN - Flee from battle");
        System.out.print("\nEnter choice (1-5): ");

        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
                System.out.println("\n⚔️ You chose: ATTACK");
                return ATTACK;
            case "2":
                System.out.println("\n🛡️ You chose: DEFEND");
                return DEFEND;
            case "3":
                System.out.println("\n✨ You chose: MAGIC");
                return MAGIC;
            case "4":
                System.out.println("\n🎯 You chose: COUNTER");
                return COUNTER;
            case "5":
                return RUN;
            default:
                System.out.println("Invalid choice! Defaulting to ATTACK.");
                return ATTACK;
        }
    }

    // Week 12-04: REMOVED bossTurn() method!
    // Before (12-03): Giant if/else chain with hardcoded HP thresholds
    // Now (12-04): Replaced with currentState.chooseAction(random)

    // Week 12-04: REMOVED getBossState() method!
    // Before (12-03): Hardcoded if/else to determine state name
    // Now (12-04): Replaced with currentState.getStateName()

    // Week 12-04: REMOVED resolveActions() method!
    // Before (12-03): Giant if/else dispatching to resolveXxxState() methods
    // Now (12-04): Replaced with currentState.resolveActions()

    // Week 12-04: REMOVED resolveNormalState() method!
    // Now (12-04): Logic moved to NormalState.resolveActions()

    // Week 12-04: REMOVED resolveAngryState() method!
    // Now (12-04): Logic moved to AngryState.resolveActions()

    // Week 12-04: REMOVED resolveDefensiveState() method!
    // Now (12-04): Logic moved to DefensiveState.resolveActions()

    // Week 12-04: REMOVED resolveEnragedState() method!
    // Now (12-04): Logic moved to EnragedState.resolveActions()

    // Week 12-04: REMOVED dealDamageToPlayer() method!
    // Now (12-04): Logic moved to BattleContext.dealDamageToPlayer()

    // Week 12-04: REMOVED dealDamageToBoss() method!
    // Now (12-04): Logic moved to BattleContext.dealDamageToBoss()
}
