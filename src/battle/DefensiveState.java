package battle;

import java.util.Random;

/**
 * Week 12-04: DefensiveState (STATE PATTERN - Concrete State)
 *
 * Boss behavior when 50% >= HP > 25%:
 * - AI: 60% DEFEND, 30% COUNTER, 10% MAGIC (defensive!)
 * - Rules: MAGIC>DEFEND(40), DEFEND>ATTACK(25), COUNTER>MAGIC(45), ATTACK>COUNTER(30)
 * - Transition: Goes to EnragedState when HP drops to 25% or below
 *
 * The boss becomes defensive, favoring DEFEND and COUNTER.
 * MAGIC is the best choice to break through the defense!
 */
public class DefensiveState implements BossState {
    // Singleton instance
    private static DefensiveState instance = null;

    public static DefensiveState getInstance() {
        if (instance == null) {
            instance = new DefensiveState();
        }
        return instance;
    }

    private DefensiveState() {
        // Private constructor for singleton
    }

    @Override
    public String chooseAction(Random random) {
        // DEFENSIVE state: 60% DEFEND, 30% COUNTER, 10% MAGIC
        int roll = random.nextInt(100);
        if (roll < 60) {
            return BattleContext.DEFEND;
        } else if (roll < 90) {
            return BattleContext.COUNTER;
        } else {
            return BattleContext.MAGIC;
        }
    }

    @Override
    public void resolveActions(String playerAction, String bossAction, BattleContext context) {
        System.out.println("🛡️ DEFENSIVE STATE RULES:");
        System.out.println("   MAGIC > DEFEND | DEFEND > ATTACK | COUNTER > MAGIC");
        System.out.println("   ATTACK > COUNTER (risky!)\n");

        if (playerAction.equals(bossAction)) {
            // Both choose same action - CLASH!
            System.out.println("⚔️ CLASH! Both actions collide!");
            context.dealDamageToPlayer(12);
            context.dealDamageToBoss(12);
        } else if (playerAction.equals(BattleContext.MAGIC) && bossAction.equals(BattleContext.DEFEND)) {
            // Player's magic pierces defense - best choice!
            System.out.println("✨ Your magic shatters the defense!");
            context.dealDamageToBoss(40);
        } else if (playerAction.equals(BattleContext.DEFEND) && bossAction.equals(BattleContext.ATTACK)) {
            // Player's defense beats attack
            System.out.println("🛡️ You blocked the attack!");
            context.dealDamageToBoss(25);
        } else if (playerAction.equals(BattleContext.COUNTER) && bossAction.equals(BattleContext.MAGIC)) {
            // Player countered magic
            System.out.println("✨ PERFECT COUNTER! You nullified the spell!");
            context.dealDamageToBoss(45);
        } else if (playerAction.equals(BattleContext.ATTACK) && bossAction.equals(BattleContext.COUNTER)) {
            // Player's attack beats counter - risky but effective!
            System.out.println("⚔️ Your attack catches the counter off-balance!");
            context.dealDamageToBoss(30);
        } else if (bossAction.equals(BattleContext.DEFEND) && playerAction.equals(BattleContext.MAGIC)) {
            // Boss defense beats player magic (shouldn't happen - see above)
            System.out.println("💢 Boss's shield holds!");
            context.dealDamageToPlayer(20);
        } else if (bossAction.equals(BattleContext.ATTACK) && playerAction.equals(BattleContext.DEFEND)) {
            // Boss attack beats player defense
            System.out.println("💢 Boss's attack breaks through!");
            context.dealDamageToPlayer(25);
        } else if (bossAction.equals(BattleContext.MAGIC) && playerAction.equals(BattleContext.COUNTER)) {
            // Boss magic beats player counter
            System.out.println("💢 Boss's magic overwhelms your counter!");
            context.dealDamageToPlayer(45);
        } else if (bossAction.equals(BattleContext.COUNTER) && playerAction.equals(BattleContext.ATTACK)) {
            // Boss counter beats player attack
            System.out.println("💢 Boss counters your attack!");
            context.dealDamageToPlayer(30);
        } else {
            // Fallback
            System.out.println("💢 Boss's " + bossAction + " beats your " + playerAction + "!");
            context.dealDamageToPlayer(28);
        }
    }

    @Override
    public BossState checkTransition(float hpPercent) {
        // Transition to EnragedState when HP drops to 25% or below
        if (hpPercent <= 0.25f) {
            System.out.println("\n😡 Boss enters ENRAGED mode! HP dropped to 25%!");
            return EnragedState.getInstance();
        }
        return null; // Stay in current state
    }

    @Override
    public String getStateName() {
        return "🛡️ DEFENSIVE - Shields up!";
    }

    @Override
    public String getRulesDescription() {
        return "MAGIC>DEFEND | DEFEND>ATTACK | COUNTER>MAGIC | ATTACK>COUNTER";
    }
}
