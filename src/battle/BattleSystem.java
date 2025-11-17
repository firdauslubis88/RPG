package battle;

import entities.Player;
import entities.GameManager;
import java.util.Scanner;
import java.util.Random;

/**
 * BattleSystem - Strategic turn-based battle with HARDCODED state rules
 *
 * Week 12-03: HARDCODED BOSS AI & RULES (ANTI-PATTERN)
 *
 * ❌ ANTI-PATTERN: Giant switch-case for boss AI
 * ❌ ANTI-PATTERN: Hardcoded HP thresholds
 * ❌ ANTI-PATTERN: Hardcoded counter relationship rules per state
 * ❌ PROBLEM: Difficult to add new boss behaviors
 * ❌ PROBLEM: Rules scattered in giant methods
 *
 * Battle Mechanics:
 * - Player and boss choose actions simultaneously
 * - Actions: ATTACK, DEFEND, MAGIC, COUNTER
 * - Counter relationships change based on boss state!
 * - State determined by boss HP percentage (HARDCODED!)
 *
 * This will be refactored in Week 12-04 using State Pattern!
 */
public class BattleSystem {
    private Player player;
    private int bossHp;
    private int bossMaxHp;
    private Scanner scanner;
    private Random random;

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
        this.player = player;
        this.bossHp = BOSS_MAX_HP;
        this.bossMaxHp = BOSS_MAX_HP;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
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

            // Boss turn - AI chooses action (HARDCODED!)
            String bossAction = bossTurn();

            // Resolve actions based on counter relationships
            resolveActions(playerAction, bossAction);

            // Check if boss is defeated
            if (bossHp <= 0) {
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║                                        ║");
                System.out.println("║        🎉 VICTORY! 🎉                 ║");
                System.out.println("║                                        ║");
                System.out.println("║     You defeated the boss!             ║");
                System.out.println("║                                        ║");
                System.out.println("╚════════════════════════════════════════╝\n");
                return true;
            }

            // Check if player is defeated
            if (GameManager.getInstance().getHp() <= 0) {
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║                                        ║");
                System.out.println("║        ☠️  DEFEAT  ☠️                 ║");
                System.out.println("║                                        ║");
                System.out.println("║     You were defeated by the boss...   ║");
                System.out.println("║                                        ║");
                System.out.println("╚════════════════════════════════════════╝\n");
                return false;
            }

            // Delay before next round
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return bossHp <= 0;
    }

    /**
     * Display current battle status
     */
    private void displayBattleStatus() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("📊 BATTLE STATUS");
        System.out.println("════════════════════════════════════════");

        // Boss HP bar
        int bossHpPercent = (bossHp * 100) / bossMaxHp;
        String bossHpBar = getHpBar(bossHpPercent, 20);
        System.out.println("🔴 BOSS HP: " + bossHpBar + " " + bossHp + "/" + bossMaxHp);

        // Boss state indicator (based on HP - HARDCODED!)
        String bossState = getBossState();
        System.out.println("   State: " + bossState);

        System.out.println();

        // Player HP bar
        int playerHp = GameManager.getInstance().getHp();
        int playerHpPercent = (playerHp * 100) / PLAYER_MAX_HP;
        String playerHpBar = getHpBar(playerHpPercent, 20);
        System.out.println("💚 YOU HP:  " + playerHpBar + " " + playerHp + "/" + PLAYER_MAX_HP);
        System.out.println("════════════════════════════════════════\n");
    }

    /**
     * Week 12-03: ❌ ANTI-PATTERN - Hardcoded state check!
     * This should use State Pattern (Week 12-04)
     */
    private String getBossState() {
        float hpPercent = (float) bossHp / bossMaxHp;

        // ❌ HARDCODED thresholds!
        if (hpPercent > 0.75f) {
            return "😐 NORMAL - Balanced combat";
        } else if (hpPercent > 0.50f) {
            return "😠 ANGRY - Aggressive attacks!";
        } else if (hpPercent > 0.25f) {
            return "🛡️ DEFENSIVE - Shields up!";
        } else {
            return "😡 ENRAGED - Chaos unleashed!";
        }
    }

    /**
     * Generate HP bar visualization
     */
    private String getHpBar(int percent, int length) {
        int filled = (percent * length) / 100;
        StringBuilder bar = new StringBuilder("[");

        for (int i = 0; i < length; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }

        bar.append("]");
        return bar.toString();
    }

    /**
     * Player's turn - choose action
     * @return chosen action (ATTACK, DEFEND, MAGIC, COUNTER, or RUN)
     */
    private String playerTurn() {
        System.out.println("⚔️  YOUR TURN!");
        System.out.println("────────────────────────────────────────");
        System.out.println("Choose your action:");
        System.out.println("1. ⚔️  ATTACK  - Physical strike");
        System.out.println("2. 🛡️  DEFEND  - Block incoming attack");
        System.out.println("3. ✨ MAGIC   - Cast powerful spell");
        System.out.println("4. 🔄 COUNTER - Perfect timing counter");
        System.out.println("5. 💨 RUN     - Flee from battle");
        System.out.println("────────────────────────────────────────");
        System.out.print("Choose action (1-5): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                return ATTACK;
            case "2":
                return DEFEND;
            case "3":
                return MAGIC;
            case "4":
                return COUNTER;
            case "5":
                return RUN;
            default:
                System.out.println("❌ Invalid choice! Defaulting to ATTACK");
                return ATTACK;
        }
    }

    /**
     * Week 12-03: ❌ ANTI-PATTERN - Giant switch-case for boss AI!
     *
     * Boss's turn - AI decision based on HP state
     * This is the ANTI-PATTERN we'll refactor in Week 12-04!
     */
    private String bossTurn() {
        float hpPercent = (float) bossHp / bossMaxHp;

        // ❌ ANTI-PATTERN: Giant if-else based on HP percentage!
        // This is hardcoded and difficult to maintain!

        if (hpPercent > 0.75f) {
            // NORMAL state: Random 25% each
            int choice = random.nextInt(4);
            switch (choice) {
                case 0: return ATTACK;
                case 1: return DEFEND;
                case 2: return MAGIC;
                case 3: return COUNTER;
            }

        } else if (hpPercent > 0.50f) {
            // ANGRY state: 50% ATTACK, 30% COUNTER, 20% MAGIC
            int roll = random.nextInt(100);
            if (roll < 50) return ATTACK;
            else if (roll < 80) return COUNTER;
            else return MAGIC;

        } else if (hpPercent > 0.25f) {
            // DEFENSIVE state: 60% DEFEND, 30% COUNTER, 10% MAGIC
            int roll = random.nextInt(100);
            if (roll < 60) return DEFEND;
            else if (roll < 90) return COUNTER;
            else return MAGIC;

        } else {
            // ENRAGED state: 70% ATTACK, 20% MAGIC, 10% COUNTER
            int roll = random.nextInt(100);
            if (roll < 70) return ATTACK;
            else if (roll < 90) return MAGIC;
            else return COUNTER;
        }

        return ATTACK; // Default
    }

    /**
     * Week 12-03: ❌ ANTI-PATTERN - Giant method with hardcoded rules per state!
     *
     * Resolve player and boss actions based on counter relationships.
     * Counter relationships CHANGE based on boss state (HARDCODED!)
     */
    private void resolveActions(String playerAction, String bossAction) {
        System.out.println("\n⚡ ACTIONS REVEALED!");
        System.out.println("────────────────────────────────────────");
        System.out.println("💚 YOU chose:  " + getActionEmoji(playerAction) + " " + playerAction);
        System.out.println("🔴 BOSS chose: " + getActionEmoji(bossAction) + " " + bossAction);
        System.out.println("────────────────────────────────────────\n");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        float hpPercent = (float) bossHp / bossMaxHp;

        // ❌ ANTI-PATTERN: Giant if-else chains for each state's rules!
        // This is EXTREMELY hard to maintain and extend!

        if (hpPercent > 0.75f) {
            // ========================================
            // NORMAL STATE RULES (>75% HP)
            // ========================================
            resolveNormalState(playerAction, bossAction);

        } else if (hpPercent > 0.50f) {
            // ========================================
            // ANGRY STATE RULES (50-75% HP)
            // ========================================
            resolveAngryState(playerAction, bossAction);

        } else if (hpPercent > 0.25f) {
            // ========================================
            // DEFENSIVE STATE RULES (25-50% HP)
            // ========================================
            resolveDefensiveState(playerAction, bossAction);

        } else {
            // ========================================
            // ENRAGED STATE RULES (<25% HP)
            // ========================================
            resolveEnragedState(playerAction, bossAction);
        }
    }

    /**
     * ❌ ANTI-PATTERN: Hardcoded rules for NORMAL state
     *
     * Rules:
     * - ATTACK > MAGIC (interrupt)
     * - MAGIC > DEFEND (pierce)
     * - DEFEND > ATTACK (block)
     * - COUNTER > all (perfect timing)
     * - TIE > both take small damage
     */
    private void resolveNormalState(String playerAction, String bossAction) {
        System.out.println("📘 NORMAL STATE RULES:");
        System.out.println("   ATTACK > MAGIC | MAGIC > DEFEND | DEFEND > ATTACK");
        System.out.println("   COUNTER > ALL (risky!)\n");

        if (playerAction.equals(bossAction)) {
            // TIE - both take small damage
            System.out.println("⚔️ CLASH! Both actions collide!");
            dealDamageToPlayer(10);
            dealDamageToBoss(10);

        } else if (playerAction.equals(COUNTER)) {
            // COUNTER beats all
            System.out.println("✨ PERFECT COUNTER! Critical hit!");
            dealDamageToBoss(40);

        } else if (bossAction.equals(COUNTER)) {
            // Boss COUNTER beats all
            System.out.println("💥 BOSS COUNTERED! You're hit hard!");
            dealDamageToPlayer(40);

        } else if (playerAction.equals(ATTACK) && bossAction.equals(MAGIC)) {
            System.out.println("⚔️ Your attack interrupts the spell!");
            dealDamageToBoss(25);

        } else if (playerAction.equals(MAGIC) && bossAction.equals(DEFEND)) {
            System.out.println("✨ Your magic pierces the defense!");
            dealDamageToBoss(30);

        } else if (playerAction.equals(DEFEND) && bossAction.equals(ATTACK)) {
            System.out.println("🛡️ You blocked the attack!");
            dealDamageToBoss(15); // Counter damage

        } else {
            // Player loses
            System.out.println("💢 Boss's " + bossAction + " beats your " + playerAction + "!");
            dealDamageToPlayer(25);
        }
    }

    /**
     * ❌ ANTI-PATTERN: Hardcoded rules for ANGRY state
     *
     * Rules change!
     * - MAGIC > ATTACK (only magic works!)
     * - ATTACK > COUNTER (aggressive beats counter)
     * - COUNTER > DEFEND (bypass defense)
     * - DEFEND > MAGIC (absorb magic)
     * - TIE > both take damage
     */
    private void resolveAngryState(String playerAction, String bossAction) {
        System.out.println("📕 ANGRY STATE RULES (CHANGED!):");
        System.out.println("   MAGIC > ATTACK | ATTACK > COUNTER | COUNTER > DEFEND");
        System.out.println("   DEFEND > MAGIC\n");

        if (playerAction.equals(bossAction)) {
            // TIE - both take damage
            System.out.println("💥 FIERCE CLASH! Heavy damage!");
            dealDamageToPlayer(15);
            dealDamageToBoss(15);

        } else if (playerAction.equals(MAGIC) && bossAction.equals(ATTACK)) {
            System.out.println("✨ Magic overpowers brute force!");
            dealDamageToBoss(35);

        } else if (playerAction.equals(ATTACK) && bossAction.equals(COUNTER)) {
            System.out.println("⚔️ Aggressive attack overwhelms counter!");
            dealDamageToBoss(30);

        } else if (playerAction.equals(COUNTER) && bossAction.equals(DEFEND)) {
            System.out.println("🔄 Counter bypasses defense!");
            dealDamageToBoss(25);

        } else if (playerAction.equals(DEFEND) && bossAction.equals(MAGIC)) {
            System.out.println("🛡️ Defense absorbs magic!");
            dealDamageToBoss(20);

        } else {
            // Player loses
            System.out.println("💢 Boss's rage overwhelms you!");
            dealDamageToPlayer(30);
        }
    }

    /**
     * ❌ ANTI-PATTERN: Hardcoded rules for DEFENSIVE state
     *
     * Rules change again!
     * - MAGIC > DEFEND (pierce shield)
     * - DEFEND > ATTACK (wall up)
     * - ATTACK > COUNTER (overwhelm)
     * - COUNTER > MAGIC (reflect)
     * - TIE > minimal damage
     */
    private void resolveDefensiveState(String playerAction, String bossAction) {
        System.out.println("📗 DEFENSIVE STATE RULES (CHANGED AGAIN!):");
        System.out.println("   MAGIC > DEFEND | DEFEND > ATTACK | ATTACK > COUNTER");
        System.out.println("   COUNTER > MAGIC\n");

        if (playerAction.equals(bossAction)) {
            // TIE - minimal damage
            System.out.println("🛡️ Defensive stalemate...");
            dealDamageToPlayer(5);
            dealDamageToBoss(5);

        } else if (playerAction.equals(MAGIC) && bossAction.equals(DEFEND)) {
            System.out.println("✨ Magic pierces the shield!");
            dealDamageToBoss(35);

        } else if (playerAction.equals(DEFEND) && bossAction.equals(ATTACK)) {
            System.out.println("🛡️ Solid defense blocks attack!");
            dealDamageToBoss(20);

        } else if (playerAction.equals(ATTACK) && bossAction.equals(COUNTER)) {
            System.out.println("⚔️ Overwhelming attack breaks counter!");
            dealDamageToBoss(25);

        } else if (playerAction.equals(COUNTER) && bossAction.equals(MAGIC)) {
            System.out.println("🔄 Counter reflects magic!");
            dealDamageToBoss(30);

        } else {
            // Player loses
            System.out.println("💢 Boss's defense prevails!");
            dealDamageToPlayer(20);
        }
    }

    /**
     * ❌ ANTI-PATTERN: Hardcoded rules for ENRAGED state
     *
     * Chaos rules!
     * - COUNTER > all except MAGIC
     * - MAGIC > COUNTER (only magic pierces)
     * - ATTACK > DEFEND
     * - DEFEND > MAGIC
     * - TIE > massive damage both
     */
    private void resolveEnragedState(String playerAction, String bossAction) {
        System.out.println("📙 ENRAGED STATE RULES (CHAOS!):");
        System.out.println("   COUNTER > ALL (except MAGIC!)");
        System.out.println("   MAGIC > COUNTER | ATTACK > DEFEND | DEFEND > MAGIC\n");

        if (playerAction.equals(bossAction)) {
            // TIE - massive damage
            System.out.println("💥💥 EXPLOSIVE CLASH! Massive damage!");
            dealDamageToPlayer(20);
            dealDamageToBoss(20);

        } else if (playerAction.equals(COUNTER) && !bossAction.equals(MAGIC)) {
            System.out.println("✨✨ PERFECT COUNTER IN CHAOS! Huge damage!");
            dealDamageToBoss(50);

        } else if (bossAction.equals(COUNTER) && !playerAction.equals(MAGIC)) {
            System.out.println("💥💥 BOSS COUNTER IN RAGE! Devastating!");
            dealDamageToPlayer(50);

        } else if (playerAction.equals(MAGIC) && bossAction.equals(COUNTER)) {
            System.out.println("✨ Magic is the only way through rage!");
            dealDamageToBoss(40);

        } else if (playerAction.equals(ATTACK) && bossAction.equals(DEFEND)) {
            System.out.println("⚔️ Attack breaks desperate defense!");
            dealDamageToBoss(30);

        } else if (playerAction.equals(DEFEND) && bossAction.equals(MAGIC)) {
            System.out.println("🛡️ Defense holds against chaos magic!");
            dealDamageToBoss(25);

        } else {
            // Player loses
            System.out.println("💢💢 Enraged boss overwhelms you!");
            dealDamageToPlayer(35);
        }
    }

    /**
     * Deal damage to player
     */
    private void dealDamageToPlayer(int damage) {
        player.takeDamage(damage);
        GameManager.getInstance().takeDamage(damage);
        System.out.println("💔 You took " + damage + " damage!");
        System.out.println("💚 Your HP: " + GameManager.getInstance().getHp() + "/" + PLAYER_MAX_HP);
    }

    /**
     * Deal damage to boss
     */
    private void dealDamageToBoss(int damage) {
        bossHp -= damage;
        if (bossHp < 0) bossHp = 0;
        System.out.println("💥 Boss took " + damage + " damage!");
        System.out.println("🔴 Boss HP: " + bossHp + "/" + bossMaxHp);
    }

    /**
     * Get emoji for action
     */
    private String getActionEmoji(String action) {
        switch (action) {
            case ATTACK: return "⚔️";
            case DEFEND: return "🛡️";
            case MAGIC: return "✨";
            case COUNTER: return "🔄";
            default: return "❓";
        }
    }
}
