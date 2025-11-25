import gamestate.*;
import difficulty.*;

/**
 * Week 12-05: Game State Pattern Demo
 *
 * This demo shows how Game State Pattern works by simulating
 * state transitions without requiring user input.
 *
 * It demonstrates:
 * 1. State transitions (Menu → Playing → Battle → Victory/Defeat)
 * 2. State lifecycle (enter, update, exit)
 * 3. State context management
 */
public class GameStateDemo {
    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║   GAME STATE PATTERN DEMONSTRATION     ║");
        System.out.println("║   Week 12-05                           ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("This demo simulates game state transitions:\n");

        // Create state context
        GameStateContext context = new GameStateContext();

        // ========================================
        // STATE 1: Menu State (Simulated)
        // ========================================
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("STEP 1: Starting with MenuState");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Manually create a demo menu state that transitions immediately
        DifficultyStrategy demoStrategy = new DemoDifficulty();
        GameState menuState = new DemoMenuState(demoStrategy);

        context.setState(menuState);
        System.out.println("✓ Current State: " + context.getCurrentState().getStateName());

        // ========================================
        // STATE 2: Transition to Playing State
        // ========================================
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("STEP 2: Updating state (triggers transition)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        context.update(0.016f);
        System.out.println("✓ Current State: " + context.getCurrentState().getStateName());

        // ========================================
        // STATE 3: Transition to Battle/Victory
        // ========================================
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("STEP 3: Updating again (Playing → Victory)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        context.update(0.016f);
        System.out.println("✓ Current State: " + context.getCurrentState().getStateName());

        // ========================================
        // STATE 4: Transition to final state
        // ========================================
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("STEP 4: Simulating 'play again? No' → null state");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Manually transition to null (exit game)
        context.setState(null);
        boolean isRunning = context.getCurrentState() != null;
        System.out.println("✓ Game State: " + (isRunning ? "Running" : "Exited"));

        // ========================================
        // Summary
        // ========================================
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║   STATE PATTERN DEMONSTRATION          ║");
        System.out.println("║   COMPLETED SUCCESSFULLY!              ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("\nState Transition Flow Demonstrated:");
        System.out.println("1. [DemoMenuState] enter() → update() → exit()");
        System.out.println("2. [SimplePlayingState] enter() → update() → exit()");
        System.out.println("3. [VictoryState] enter() → (simulated)");
        System.out.println("4. [null] Game ended");

        System.out.println("\n✓ Each state managed its own lifecycle");
        System.out.println("✓ Transitions were explicit and controlled");
        System.out.println("✓ Context handled enter/exit automatically");

        System.out.println("\nKey Benefits Demonstrated:");
        System.out.println("• Clean separation: Each state is independent");
        System.out.println("• Easy transitions: Just return next state");
        System.out.println("• Extensible: Add new states without modifying existing ones");
    }

    /**
     * Demo Menu State that auto-transitions
     * (avoids Scanner input issues in non-interactive mode)
     */
    static class DemoMenuState implements GameState {
        private DifficultyStrategy strategy;

        public DemoMenuState(DifficultyStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public void enter() {
            System.out.println("  [DemoMenuState.enter()] Entering menu (DEMO mode, auto-selecting DEMO difficulty)");
        }

        @Override
        public GameState update(float deltaTime) {
            System.out.println("  [DemoMenuState.update()] Auto-selecting DEMO difficulty");
            System.out.println("  [DemoMenuState.update()] Transitioning to SimplePlayingState");
            return new SimplePlayingState(strategy);
        }

        @Override
        public void render() {
            // No rendering needed for demo
        }

        @Override
        public void exit() {
            System.out.println("  [DemoMenuState.exit()] Exiting menu");
        }

        @Override
        public String getStateName() {
            return "DEMO_MENU";
        }
    }
}
