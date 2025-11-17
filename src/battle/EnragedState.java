package battle;

import java.util.Random;

/**
 * Week 12-04: EnragedState (STATE PATTERN - Concrete State)
 *
 * Boss behavior when HP <= 25%:
 * - AI: 70% ATTACK, 20% MAGIC, 10% COUNTER (chaotic!)
 * - Rules: DEFEND>ATTACK(35), ATTACK>MAGIC(45), MAGIC>COUNTER(40), COUNTER>DEFEND(60)
 * - Transition: None (final state until death)
 *
 * The boss goes berserk, spamming ATTACK with high damage.
 * DEFEND is crucial to survive this phase!
 */
public class EnragedState implements BossState {
    // Singleton instance
    private static EnragedState instance = null;

    public static EnragedState getInstance() {
        if (instance == null) {
            instance = new EnragedState();
        }
        return instance;
    }

    private EnragedState() {
        // Private constructor for singleton
    }

    @Override
    public String chooseAction(Random random) {
        // ENRAGED state: 70% ATTACK, 20% MAGIC, 10% COUNTER
        int roll = random.nextInt(100);
        if (roll < 70) {
            return BattleContext.ATTACK;
        } else if (roll < 90) {
            return BattleContext.MAGIC;
        } else {
            return BattleContext.COUNTER;
        }
    }

    @Override
    public void resolveActions(String playerAction, String bossAction, BattleContext context) {
        System.out.println("😡 ENRAGED STATE RULES:");
        System.out.println("   DEFEND > ATTACK | ATTACK > MAGIC | MAGIC > COUNTER");
        System.out.println("   COUNTER > DEFEND (ultra risky!)\n");

        if (playerAction.equals(bossAction)) {
            // Both choose same action - CLASH!
            System.out.println("⚔️ CLASH! Both actions collide with fury!");
            context.dealDamageToPlayer(20);
            context.dealDamageToBoss(20);
        } else if (playerAction.equals(BattleContext.DEFEND) && bossAction.equals(BattleContext.ATTACK)) {
            // Player's defense blocks berserk attack - critical!
            System.out.println("🛡️ You withstand the berserk assault!");
            context.dealDamageToBoss(35);
        } else if (playerAction.equals(BattleContext.ATTACK) && bossAction.equals(BattleContext.MAGIC)) {
            // Player's attack interrupts magic
            System.out.println("⚔️ Your attack disrupts the desperate spell!");
            context.dealDamageToBoss(45);
        } else if (playerAction.equals(BattleContext.MAGIC) && bossAction.equals(BattleContext.COUNTER)) {
            // Player's magic beats counter
            System.out.println("✨ Your magic strikes true!");
            context.dealDamageToBoss(40);
        } else if (playerAction.equals(BattleContext.COUNTER) && bossAction.equals(BattleContext.DEFEND)) {
            // Player countered defense - ultra risky, huge damage!
            System.out.println("✨ LEGENDARY COUNTER! You predicted the defense!");
            context.dealDamageToBoss(60);
        } else if (bossAction.equals(BattleContext.ATTACK) && playerAction.equals(BattleContext.DEFEND)) {
            // Boss's berserk attack beats player defense (shouldn't happen - see above)
            System.out.println("💢 Boss's BERSERK ATTACK breaks your shield!");
            context.dealDamageToPlayer(35);
        } else if (bossAction.equals(BattleContext.MAGIC) && playerAction.equals(BattleContext.ATTACK)) {
            // Boss magic beats player attack
            System.out.println("💢 Boss's desperate magic catches you!");
            context.dealDamageToPlayer(45);
        } else if (bossAction.equals(BattleContext.COUNTER) && playerAction.equals(BattleContext.MAGIC)) {
            // Boss counter beats player magic
            System.out.println("💢 Boss counters your spell!");
            context.dealDamageToPlayer(40);
        } else if (bossAction.equals(BattleContext.DEFEND) && playerAction.equals(BattleContext.COUNTER)) {
            // Boss defense beats player counter
            System.out.println("💢 Boss blocks your counter attempt!");
            context.dealDamageToPlayer(60);
        } else {
            // Fallback
            System.out.println("💢 Boss's enraged " + bossAction + " overwhelms you!");
            context.dealDamageToPlayer(38);
        }
    }

    @Override
    public BossState checkTransition(float hpPercent) {
        // ENRAGED is the final state - no more transitions
        return null;
    }

    @Override
    public String getStateName() {
        return "😡 ENRAGED - Chaos unleashed!";
    }

    @Override
    public String getRulesDescription() {
        return "DEFEND>ATTACK | ATTACK>MAGIC | MAGIC>COUNTER | COUNTER>DEFEND";
    }
}
