package gamestate;

import difficulty.DifficultyStrategy;

/**
 * Week 12-05: SimplePlayingState (Game State Pattern - Simplified)
 *
 * This is a simplified version that demonstrates the concept.
 * In a full implementation, GameEngine would be refactored to support
 * state-based updates instead of a blocking game loop.
 *
 * For now, this state just holds the strategy and immediately
 * transitions to show the concept of state flow.
 */
public class SimplePlayingState implements GameState {
    private DifficultyStrategy strategy;
    private boolean transitioned;

    public SimplePlayingState(DifficultyStrategy strategy) {
        this.strategy = strategy;
        this.transitioned = false;
    }

    @Override
    public void enter() {
        System.out.println("\n[SimplePlayingState] Would start dungeon exploration here...");
        System.out.println("[SimplePlayingState] Difficulty: " + strategy.getName());
        System.out.println("[SimplePlayingState] (In full implementation, GameEngine would run here)");
    }

    @Override
    public GameState update(float deltaTime) {
        if (!transitioned) {
            // Simulate game completion after first update
            System.out.println("[SimplePlayingState] Simulating game completion...");
            transitioned = true;

            // For demonstration, transition to victory
            return new VictoryState(strategy);
        }

        return null;
    }

    @Override
    public void render() {
        // In full implementation, would delegate to GameEngine
    }

    @Override
    public void exit() {
        System.out.println("[SimplePlayingState] Exiting dungeon exploration...");
    }

    @Override
    public String getStateName() {
        return "PLAYING (SIMPLIFIED)";
    }
}
