package boss;

import entities.Boss;

/**
 * IdleState - Boss is passive and slow
 *
 * Week 12-03: STATE PATTERN (SOLUTION)
 *
 * Behavior:
 * - Slow movement (0.1 speed)
 * - Low damage (5)
 * - Symbol: 'B' (Boss)
 *
 * Transitions:
 * - To AGGRESSIVE: When player gets close (distance < 5)
 * - To AGGRESSIVE: When taking damage
 */
public class IdleState implements BossState {

    @Override
    public void update(Boss boss, float delta) {
        // Check distance to player
        int distance = boss.getDistanceToTarget();

        // Transition to AGGRESSIVE if player is close
        if (distance < 5) {
            boss.setState(new AggressiveState());
            return;
        }

        // Idle behavior: slow wandering
        boss.moveTowardsTarget(getMovementSpeed());
    }

    @Override
    public void onDamageTaken(Boss boss, int damage) {
        // Taking damage makes boss AGGRESSIVE
        boss.setState(new AggressiveState());
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }

    @Override
    public float getMovementSpeed() {
        return 0.1f;  // Very slow
    }

    @Override
    public int getAttackDamage() {
        return 5;  // Low damage
    }

    @Override
    public char getSymbol() {
        return 'B';  // Normal Boss
    }
}
