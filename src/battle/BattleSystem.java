package battle;

import entities.Player;
import entities.GameManager;
import java.util.Scanner;
import java.util.Random;

/**
 * BattleSystem - Turn-based battle with HARDCODED boss AI
 *
 * Week 12-03: HARDCODED BOSS AI (ANTI-PATTERN)
 *
 * ❌ ANTI-PATTERN: Giant switch-case for boss AI
 * ❌ PROBLEM: Hardcoded HP thresholds
 * ❌ PROBLEM: Difficult to add new boss behaviors
 * ❌ PROBLEM: Boss logic scattered in one giant method
 *
 * This will be refactored in Week 12-04 using State Pattern!
 */
public class BattleSystem {
    private Player player;
    private int bossHp;
    private int bossMaxHp;
    private int bossDamage;
    private boolean playerDefending;
    private Scanner scanner;
    private Random random;

    // Boss stats
    private static final int BOSS_MAX_HP = 200;
    private static final int BOSS_BASE_DAMAGE = 15;
    private static final int PLAYER_MAX_HP = 100;  // GameManager max HP

    public BattleSystem(Player player) {
        this.player = player;
        this.bossHp = BOSS_MAX_HP;
        this.bossMaxHp = BOSS_MAX_HP;
        this.bossDamage = BOSS_BASE_DAMAGE;
        this.playerDefending = false;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    /**
     * Start turn-based battle
     * @return true if player wins, false if player loses
     */
    public boolean startBattle() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║        ⚔️  BOSS BATTLE!  ⚔️           ║");
        System.out.println("║                                        ║");
        System.out.println("║   A fearsome boss blocks your exit!    ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        while (bossHp > 0 && GameManager.getInstance().getHp() > 0) {
            // Display battle status
            displayBattleStatus();

            // Player turn
            boolean playerRan = playerTurn();
            if (playerRan) {
                System.out.println("\n💨 You fled from the battle!");
                return false;
            }

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

            // Boss turn (with delay for dramatic effect)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            bossTurn();

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

            // Reset defending status after boss turn
            playerDefending = false;
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
            return "😐 NORMAL";
        } else if (hpPercent > 0.50f) {
            return "😠 ANGRY";
        } else if (hpPercent > 0.25f) {
            return "🛡️ DEFENSIVE";
        } else {
            return "😡 ENRAGED";
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
     * @return true if player ran away
     */
    private boolean playerTurn() {
        System.out.println("⚔️  YOUR TURN!");
        System.out.println("────────────────────────────────────────");
        System.out.println("1. ⚔️  ATTACK  - Deal 20-30 damage");
        System.out.println("2. 🛡️  DEFEND  - Reduce next damage by 50%");
        System.out.println("3. ✨ MAGIC   - Deal 40 damage (risky!)");
        System.out.println("4. 💨 RUN     - Flee from battle");
        System.out.println("────────────────────────────────────────");
        System.out.print("Choose action (1-4): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                playerAttack();
                return false;
            case "2":
                playerDefend();
                return false;
            case "3":
                playerMagic();
                return false;
            case "4":
                return true;  // Run away
            default:
                System.out.println("❌ Invalid choice! Defaulting to ATTACK");
                playerAttack();
                return false;
        }
    }

    /**
     * Player attacks boss
     */
    private void playerAttack() {
        int damage = 20 + random.nextInt(11);  // 20-30 damage
        bossHp -= damage;
        if (bossHp < 0) bossHp = 0;

        System.out.println("\n⚔️  You attack the boss!");
        System.out.println("💥 Dealt " + damage + " damage!");
        System.out.println("🔴 Boss HP: " + bossHp + "/" + bossMaxHp);
    }

    /**
     * Player defends (reduces next damage)
     */
    private void playerDefend() {
        playerDefending = true;
        System.out.println("\n🛡️  You take a defensive stance!");
        System.out.println("💪 Next damage will be reduced by 50%");
    }

    /**
     * Player uses magic attack
     */
    private void playerMagic() {
        int damage = 40;
        bossHp -= damage;
        if (bossHp < 0) bossHp = 0;

        System.out.println("\n✨ You cast a powerful spell!");
        System.out.println("💥 Dealt " + damage + " damage!");
        System.out.println("🔴 Boss HP: " + bossHp + "/" + bossMaxHp);
    }

    /**
     * Week 12-03: ❌ ANTI-PATTERN - Giant switch-case for boss AI!
     *
     * Boss's turn - AI decision based on HP
     * This is the ANTI-PATTERN we'll refactor in Week 12-04!
     */
    private void bossTurn() {
        System.out.println("\n👹 BOSS TURN!");
        System.out.println("────────────────────────────────────────");

        float hpPercent = (float) bossHp / bossMaxHp;

        // ❌ ANTI-PATTERN: Giant switch-case based on HP percentage!
        // This is hardcoded and difficult to maintain!

        if (hpPercent > 0.75f) {
            // NORMAL state: Simple attacks
            bossNormalAttack();

        } else if (hpPercent > 0.50f) {
            // ANGRY state: Random between attack and power attack
            if (random.nextBoolean()) {
                bossNormalAttack();
            } else {
                bossPowerAttack();
            }

        } else if (hpPercent > 0.25f) {
            // DEFENSIVE state: Heal or weak attack
            if (random.nextBoolean()) {
                bossHeal();
            } else {
                bossWeakAttack();
            }

        } else {
            // ENRAGED state: Always power attack!
            bossPowerAttack();
        }
    }

    /**
     * Boss normal attack
     */
    private void bossNormalAttack() {
        int damage = bossDamage + random.nextInt(6);  // 15-20 damage

        if (playerDefending) {
            damage = damage / 2;
            System.out.println("🛡️  Your defense reduced the damage!");
        }

        player.takeDamage(damage);
        GameManager.getInstance().takeDamage(damage);

        System.out.println("👹 Boss attacks!");
        System.out.println("💥 You took " + damage + " damage!");
        System.out.println("💚 Your HP: " + GameManager.getInstance().getHp() + "/" + PLAYER_MAX_HP);
    }

    /**
     * Boss weak attack (defensive state)
     */
    private void bossWeakAttack() {
        int damage = 8 + random.nextInt(5);  // 8-12 damage

        if (playerDefending) {
            damage = damage / 2;
            System.out.println("🛡️  Your defense reduced the damage!");
        }

        player.takeDamage(damage);
        GameManager.getInstance().takeDamage(damage);

        System.out.println("👹 Boss attacks weakly (defensive)");
        System.out.println("💥 You took " + damage + " damage");
        System.out.println("💚 Your HP: " + GameManager.getInstance().getHp() + "/" + PLAYER_MAX_HP);
    }

    /**
     * Boss power attack (angry/enraged state)
     */
    private void bossPowerAttack() {
        int damage = 25 + random.nextInt(11);  // 25-35 damage

        if (playerDefending) {
            damage = damage / 2;
            System.out.println("🛡️  Your defense reduced the damage!");
        }

        player.takeDamage(damage);
        GameManager.getInstance().takeDamage(damage);

        System.out.println("👹 Boss unleashes a POWER ATTACK!");
        System.out.println("💥 You took " + damage + " damage!");
        System.out.println("💚 Your HP: " + GameManager.getInstance().getHp() + "/" + PLAYER_MAX_HP);
    }

    /**
     * Boss heals (defensive state)
     */
    private void bossHeal() {
        int healAmount = 15 + random.nextInt(11);  // 15-25 HP
        bossHp += healAmount;
        if (bossHp > bossMaxHp) bossHp = bossMaxHp;

        System.out.println("👹 Boss heals itself!");
        System.out.println("💚 Boss recovered " + healAmount + " HP!");
        System.out.println("🔴 Boss HP: " + bossHp + "/" + bossMaxHp);
    }
}
