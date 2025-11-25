package gamestate;

/**
 * Week 12-05: GameState interface (STATE PATTERN for Game Flow)
 *
 * This interface defines the contract for game states.
 * Each state represents a different phase of the game:
 * - MenuState: Main menu and difficulty selection
 * - PlayingState: Dungeon exploration
 * - BattleState: Boss battle
 * - VictoryState: Win screen
 * - DefeatState: Game over screen
 *
 * Benefits:
 * - Clean separation of game phases
 * - Each state manages its own input, update, and rendering
 * - Easy to add new game states (pause, inventory, etc.)
 * - State transitions are explicit and manageable
 */
public interface GameState {
    /**
     * Initialize this state when entered
     * Called once when transitioning to this state
     */
    void enter();

    /**
     * Update game logic for this state
     * Called every frame while in this state
     *
     * @param deltaTime Time since last frame in seconds
     * @return Next state if transition needed, or null to stay in current state
     */
    GameState update(float deltaTime);

    /**
     * Render this state
     * Called every frame after update
     */
    void render();

    /**
     * Cleanup when leaving this state
     * Called once when transitioning away from this state
     */
    void exit();

    /**
     * Get the name of this state for debugging
     */
    String getStateName();
}
