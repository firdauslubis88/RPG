package boss;

import entities.Boss;

/**
 * DefensiveState - Boss retreats and heals
 *
 * Week 12-03: STATE PATTERN (SOLUTION)
 *
 * Behavior:
 * - Slow retreat (0.2 speed away from player)
 * - Low damage (8)
 * - Healing over time (2 HP per second)
 * - Symbol: 'Ɓ' (Defensive Boss)
 *
 * Transitions:
 * - To AGGRESSIVE: When HP > 50% (healed enough)
 * - To ENRAGED: When HP < 15% (critical)
 */
public class DefensiveState implements BossState {
    private float healTimer = 0;
    private static final float HEAL_INTERVAL = 0.5f;  // Heal every 0.5 seconds
    private static final int HEAL_AMOUNT = 1;  // Heal 1 HP per tick (2 HP/sec)

    @Override
    public void update(Boss boss, float delta) {
        float hpPercent = boss.getHpPercentage();

        // Check for state transitions
        if (hpPercent < 0.15f) {
            // Critical HP: Go ENRAGED
            boss.setState(new EnragedState());
            return;
        } else if (hpPercent > 0.5f) {
            // Healed enough: Return to AGGRESSIVE
            boss.setState(new AggressiveState());
            return;
        }

        // Defensive behavior: retreat and heal
        boss.moveAwayFromTarget(getMovementSpeed());

        // Healing over time
        healTimer += delta;
        if (healTimer >= HEAL_INTERVAL) {
            boss.heal(HEAL_AMOUNT);
            healTimer = 0;
        }
    }

    @Override
    public void onDamageTaken(Boss boss, int damage) {
        float hpPercent = boss.getHpPercentage();

        // If taking damage while defensive and HP is critical, go ENRAGED
        if (hpPercent < 0.15f) {
            boss.setState(new EnragedState());
        }
    }

    @Override
    public String getStateName() {
        return "DEFENSIVE";
    }

    @Override
    public float getMovementSpeed() {
        return 0.2f;  // Slow retreat
    }

    @Override
    public int getAttackDamage() {
        return 8;  // Low damage (focused on healing)
    }

    @Override
    public char getSymbol() {
        return 'Ɓ';  // Defensive Boss (with hook)
    }
}
