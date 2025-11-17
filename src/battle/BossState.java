package battle;

import java.util.Random;

/**
 * Week 12-04: BossState interface (STATE PATTERN)
 *
 * This interface defines the contract for boss states.
 * Each state encapsulates:
 * 1. AI behavior (which action to choose)
 * 2. Counter relationship rules (how actions resolve)
 * 3. State transition logic (when to change states)
 *
 * Benefits over hardcoded if/else chains (from 12-03):
 * - State behavior is encapsulated in one class
 * - Easy to add new states (just create new class)
 * - Each state is independently testable
 * - No magic numbers scattered everywhere
 */
public interface BossState {
    /**
     * Choose boss action based on state's AI strategy
     *
     * @param random Random number generator for AI decisions
     * @return The action chosen by the boss (ATTACK, DEFEND, MAGIC, COUNTER)
     */
    String chooseAction(Random random);

    /**
     * Resolve player and boss actions based on this state's counter relationship rules
     *
     * @param playerAction The action chosen by the player
     * @param bossAction The action chosen by the boss
     * @param context Battle context for applying damage and getting state info
     */
    void resolveActions(String playerAction, String bossAction, BattleContext context);

    /**
     * Check if boss should transition to a different state
     *
     * @param hpPercent Current boss HP percentage (0.0 to 1.0)
     * @return New state if transition needed, or null to keep current state
     */
    BossState checkTransition(float hpPercent);

    /**
     * Get the name and description of this state
     *
     * @return State name with emoji and description
     */
    String getStateName();

    /**
     * Get the counter relationship rules for display
     *
     * @return Rules description for this state
     */
    String getRulesDescription();
}
