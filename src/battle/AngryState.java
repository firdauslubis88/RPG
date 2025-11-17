package battle;

import java.util.Random;

/**
 * Week 12-04: AngryState (STATE PATTERN - Concrete State)
 *
 * Boss behavior when 75% >= HP > 50%:
 * - AI: 50% ATTACK, 30% COUNTER, 20% MAGIC (aggressive!)
 * - Rules: ATTACK>DEFEND(35), MAGIC>COUNTER(35), DEFEND>MAGIC(20), COUNTER>ATTACK(50)
 * - Transition: Goes to DefensiveState when HP drops to 50% or below
 *
 * The boss becomes more aggressive, favoring ATTACK and COUNTER.
 * Counter relationship rules are different from NORMAL state!
 */
public class AngryState implements BossState {
    // Singleton instance
    private static AngryState instance = null;

    public static AngryState getInstance() {
        if (instance == null) {
            instance = new AngryState();
        }
        return instance;
    }

    private AngryState() {
        // Private constructor for singleton
    }

    @Override
    public String chooseAction(Random random) {
        // ANGRY state: 50% ATTACK, 30% COUNTER, 20% MAGIC
        int roll = random.nextInt(100);
        if (roll < 50) {
            return BattleContext.ATTACK;
        } else if (roll < 80) {
            return BattleContext.COUNTER;
        } else {
            return BattleContext.MAGIC;
        }
    }

    @Override
    public void resolveActions(String playerAction, String bossAction, BattleContext context) {
        System.out.println("🔥 ANGRY STATE RULES:");
        System.out.println("   ATTACK > DEFEND | MAGIC > COUNTER | DEFEND > MAGIC");
        System.out.println("   COUNTER > ATTACK (devastating!)\n");

        if (playerAction.equals(bossAction)) {
            // Both choose same action - CLASH!
            System.out.println("⚔️ CLASH! Both actions collide!");
            context.dealDamageToPlayer(15);
            context.dealDamageToBoss(15);
        } else if (playerAction.equals(BattleContext.ATTACK) && bossAction.equals(BattleContext.DEFEND)) {
            // Player's attack beats defense
            System.out.println("⚔️ Your attack breaks through!");
            context.dealDamageToBoss(35);
        } else if (playerAction.equals(BattleContext.MAGIC) && bossAction.equals(BattleContext.COUNTER)) {
            // Player's magic beats counter
            System.out.println("✨ Your magic catches the boss off-guard!");
            context.dealDamageToBoss(35);
        } else if (playerAction.equals(BattleContext.DEFEND) && bossAction.equals(BattleContext.MAGIC)) {
            // Player's defense beats magic
            System.out.println("🛡️ You blocked the spell!");
            context.dealDamageToBoss(20);
        } else if (playerAction.equals(BattleContext.COUNTER) && bossAction.equals(BattleContext.ATTACK)) {
            // Player countered attack - big damage!
            System.out.println("✨ PERFECT COUNTER! You read the attack!");
            context.dealDamageToBoss(50);
        } else if (bossAction.equals(BattleContext.COUNTER) && playerAction.equals(BattleContext.ATTACK)) {
            // Boss countered player's attack - devastating!
            System.out.println("💥 BOSS COUNTERED YOUR ATTACK! Massive damage!");
            context.dealDamageToPlayer(50);
        } else if (bossAction.equals(BattleContext.ATTACK) && playerAction.equals(BattleContext.DEFEND)) {
            // Boss attack beats player defense
            System.out.println("💢 Boss's furious attack breaks your defense!");
            context.dealDamageToPlayer(35);
        } else if (bossAction.equals(BattleContext.MAGIC) && playerAction.equals(BattleContext.COUNTER)) {
            // Boss magic beats player counter
            System.out.println("💢 Boss's magic hits you during counter!");
            context.dealDamageToPlayer(35);
        } else if (bossAction.equals(BattleContext.DEFEND) && playerAction.equals(BattleContext.MAGIC)) {
            // Boss defense beats player magic
            System.out.println("💢 Boss blocks your spell!");
            context.dealDamageToPlayer(20);
        } else {
            // Fallback
            System.out.println("💢 Boss's " + bossAction + " beats your " + playerAction + "!");
            context.dealDamageToPlayer(30);
        }
    }

    @Override
    public BossState checkTransition(float hpPercent) {
        // Transition to DefensiveState when HP drops to 50% or below
        if (hpPercent <= 0.50f) {
            System.out.println("\n🛡️ Boss switches to DEFENSIVE mode! HP dropped to 50%!");
            return DefensiveState.getInstance();
        }
        return null; // Stay in current state
    }

    @Override
    public String getStateName() {
        return "😠 ANGRY - Aggressive attacks!";
    }

    @Override
    public String getRulesDescription() {
        return "ATTACK>DEFEND | MAGIC>COUNTER | DEFEND>MAGIC | COUNTER>ATTACK";
    }
}
