package boss;

import entities.Boss;

/**
 * BossState - State Pattern interface for Boss AI
 *
 * Week 12-03: STATE PATTERN (SOLUTION)
 *
 * Each state implements different boss behavior:
 * - Idle: Passive, slow movement
 * - Aggressive: Active pursuit of player
 * - Defensive: Retreating, healing
 * - Enraged: Fast, powerful attacks
 *
 * State transitions based on:
 * - Boss HP
 * - Damage taken
 * - Distance to player
 * - Time in current state
 */
public interface BossState {
    /**
     * Update boss behavior based on current state
     * @param boss The boss entity
     * @param delta Time since last update
     */
    void update(Boss boss, float delta);

    /**
     * Handle boss taking damage
     * May trigger state transition
     * @param boss The boss entity
     * @param damage Amount of damage taken
     */
    void onDamageTaken(Boss boss, int damage);

    /**
     * Get state name for display/debugging
     * @return State name (e.g., "IDLE", "AGGRESSIVE")
     */
    String getStateName();

    /**
     * Get boss movement speed in this state
     * @return Movement speed multiplier
     */
    float getMovementSpeed();

    /**
     * Get boss attack damage in this state
     * @return Damage value
     */
    int getAttackDamage();

    /**
     * Get boss symbol/appearance in this state
     * @return Character symbol for rendering
     */
    char getSymbol();
}
