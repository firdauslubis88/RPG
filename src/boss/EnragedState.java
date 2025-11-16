package boss;

import entities.Boss;

/**
 * EnragedState - Boss final phase, extremely dangerous
 *
 * Week 12-03: STATE PATTERN (SOLUTION)
 *
 * Behavior:
 * - Very fast movement (0.6 speed)
 * - High damage (25)
 * - Symbol: '𝐁' (Enraged Boss - bold)
 *
 * Transitions:
 * - None (final state until death or victory)
 */
public class EnragedState implements BossState {

    @Override
    public void update(Boss boss, float delta) {
        // ENRAGED is the final state - no transitions
        // Just attack relentlessly!

        // Enraged behavior: aggressive pursuit and powerful attacks
        boss.moveTowardsTarget(getMovementSpeed());
        boss.attackPlayer();
    }

    @Override
    public void onDamageTaken(Boss boss, int damage) {
        // ENRAGED state doesn't transition - fight to the death!
        // Boss is in berserk mode
    }

    @Override
    public String getStateName() {
        return "ENRAGED";
    }

    @Override
    public float getMovementSpeed() {
        return 0.6f;  // Very fast!
    }

    @Override
    public int getAttackDamage() {
        return 25;  // High damage!
    }

    @Override
    public char getSymbol() {
        return '𝐁';  // Enraged Boss (bold/different)
    }
}
