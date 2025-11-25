package gamestate;

/**
 * Week 12-05: GameStateContext (STATE PATTERN Context)
 *
 * This class manages the current game state and handles state transitions.
 * It follows the Context pattern in State Pattern implementations.
 *
 * The game flow:
 * MenuState → PlayingState → BattleState → VictoryState/DefeatState
 *                   ↓                           ↓
 *              DefeatState                  MenuState (restart)
 */
public class GameStateContext {
    private GameState currentState;
    private boolean running;

    public GameStateContext() {
        this.running = true;
    }

    /**
     * Set the current game state
     * Handles exit of old state and enter of new state
     */
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }

        currentState = newState;

        if (currentState != null) {
            currentState.enter();
        }
    }

    /**
     * Get the current game state
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Update the current state and handle transitions
     */
    public void update(float deltaTime) {
        if (currentState != null) {
            GameState nextState = currentState.update(deltaTime);
            if (nextState != null && nextState != currentState) {
                setState(nextState);
            }
        }
    }

    /**
     * Render the current state
     */
    public void render() {
        if (currentState != null) {
            currentState.render();
        }
    }

    /**
     * Check if game is still running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Stop the game
     */
    public void stop() {
        this.running = false;
    }
}
