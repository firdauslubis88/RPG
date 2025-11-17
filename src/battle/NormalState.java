package battle;

import java.util.Random;

/**
 * Week 12-04: NormalState (STATE PATTERN - Concrete State)
 *
 * Boss behavior when HP > 75%:
 * - AI: Random 25% each action (balanced)
 * - Rules: ATTACK>MAGIC(25), MAGIC>DEFEND(30), DEFEND>ATTACK(15), COUNTER>ALL(40)
 * - Transition: Goes to AngryState when HP drops to 75% or below
 *
 * This encapsulates all NORMAL state behavior in one class!
 * Compare to 12-03 where this logic was scattered across:
 * - bossTurn() for AI decision
 * - resolveNormalState() for counter rules
 * - Multiple hardcoded 0.75f threshold checks
 */
public class NormalState implements BossState {
    // Singleton instance (optional optimization - states are stateless)
    private static NormalState instance = null;

    public static NormalState getInstance() {
        if (instance == null) {
            instance = new NormalState();
        }
        return instance;
    }

    private NormalState() {
        // Private constructor for singleton
    }

    @Override
    public String chooseAction(Random random) {
        // NORMAL state: Random 25% each action
        int choice = random.nextInt(4);
        switch (choice) {
            case 0: return BattleContext.ATTACK;
            case 1: return BattleContext.DEFEND;
            case 2: return BattleContext.MAGIC;
            case 3: return BattleContext.COUNTER;
            default: return BattleContext.ATTACK;
        }
    }

    @Override
    public void resolveActions(String playerAction, String bossAction, BattleContext context) {
        System.out.println("📘 NORMAL STATE RULES:");
        System.out.println("   ATTACK > MAGIC | MAGIC > DEFEND | DEFEND > ATTACK");
        System.out.println("   COUNTER > ALL (risky!)\n");

        if (playerAction.equals(bossAction)) {
            // Both choose same action - CLASH!
            System.out.println("⚔️ CLASH! Both actions collide!");
            context.dealDamageToPlayer(10);
            context.dealDamageToBoss(10);
        } else if (playerAction.equals(BattleContext.COUNTER)) {
            // Player countered successfully!
            System.out.println("✨ PERFECT COUNTER! Critical hit!");
            context.dealDamageToBoss(40);
        } else if (bossAction.equals(BattleContext.COUNTER)) {
            // Boss countered player!
            System.out.println("💥 BOSS COUNTERED! You're hit hard!");
            context.dealDamageToPlayer(40);
        } else if (playerAction.equals(BattleContext.ATTACK) && bossAction.equals(BattleContext.MAGIC)) {
            // Player's attack interrupts magic
            System.out.println("⚔️ Your attack interrupts the spell!");
            context.dealDamageToBoss(25);
        } else if (playerAction.equals(BattleContext.MAGIC) && bossAction.equals(BattleContext.DEFEND)) {
            // Player's magic pierces defense
            System.out.println("✨ Your magic pierces the defense!");
            context.dealDamageToBoss(30);
        } else if (playerAction.equals(BattleContext.DEFEND) && bossAction.equals(BattleContext.ATTACK)) {
            // Player blocks attack
            System.out.println("🛡️ You blocked the attack!");
            context.dealDamageToBoss(15);
        } else {
            // Boss wins the exchange
            System.out.println("💢 Boss's " + bossAction + " beats your " + playerAction + "!");
            context.dealDamageToPlayer(25);
        }
    }

    @Override
    public BossState checkTransition(float hpPercent) {
        // Transition to AngryState when HP drops to 75% or below
        if (hpPercent <= 0.75f) {
            System.out.println("\n🔥 Boss is getting ANGRY! HP dropped to 75%!");
            return AngryState.getInstance();
        }
        return null; // Stay in current state
    }

    @Override
    public String getStateName() {
        return "😐 NORMAL - Balanced combat";
    }

    @Override
    public String getRulesDescription() {
        return "ATTACK>MAGIC | MAGIC>DEFEND | DEFEND>ATTACK | COUNTER>ALL";
    }
}
