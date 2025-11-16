package boss;

import entities.Boss;

/**
 * AggressiveState - Boss actively hunts player
 *
 * Week 12-03: STATE PATTERN (SOLUTION)
 *
 * Behavior:
 * - Fast movement (0.4 speed)
 * - Medium damage (15)
 * - Symbol: 'Ḃ' (Aggressive Boss)
 *
 * Transitions:
 * - To DEFENSIVE: When HP < 30%
 * - To ENRAGED: When HP < 20%
 */
public class AggressiveState implements BossState {

    @Override
    public void update(Boss boss, float delta) {
        // Check HP for state transition
        float hpPercent = boss.getHpPercentage();

        if (hpPercent < 0.2f) {
            // Below 20% HP: ENRAGED!
            boss.setState(new EnragedState());
            return;
        } else if (hpPercent < 0.3f) {
            // Below 30% HP: Retreat and heal
            boss.setState(new DefensiveState());
            return;
        }

        // Aggressive behavior: chase player and attack
        boss.moveTowardsTarget(getMovementSpeed());
        boss.attackPlayer();
    }

    @Override
    public void onDamageTaken(Boss boss, int damage) {
        // Stay aggressive, but check HP
        float hpPercent = boss.getHpPercentage();

        if (hpPercent < 0.2f) {
            boss.setState(new EnragedState());
        } else if (hpPercent < 0.3f) {
            boss.setState(new DefensiveState());
        }
    }

    @Override
    public String getStateName() {
        return "AGGRESSIVE";
    }

    @Override
    public float getMovementSpeed() {
        return 0.4f;  // Fast
    }

    @Override
    public int getAttackDamage() {
        return 15;  // Medium damage
    }

    @Override
    public char getSymbol() {
        return 'Ḃ';  // Aggressive Boss (with dot)
    }
}
