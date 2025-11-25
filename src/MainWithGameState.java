import gamestate.*;

/**
 * Week 12-05: Main with Game State Pattern
 *
 * ✅ SOLUTION: Game State Pattern for game flow management!
 *
 * Evolution from Week 12-04:
 * ❌ Before: Direct control flow (Main → Menu → GameEngine)
 * ✅ Now: State-based flow (MenuState → PlayingState → BattleState → Victory/DefeatState)
 *
 * Benefits:
 * - Clean separation of game phases
 * - Each state manages its own logic
 * - Easy to add new states (pause, inventory, shop, etc.)
 * - Explicit state transitions
 * - Game flow is self-documenting
 *
 * Game State Flow:
 * MenuState → SimplePlayingState → BattleState → VictoryState/DefeatState
 *     ↓                                              ↓
 * (select difficulty)                          (play again?)
 *                                                    ↓
 *                                              MenuState (loop)
 */
public class MainWithGameState {
    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║     DUNGEON ESCAPE                     ║");
        System.out.println("║     Week 12-05: Game State Pattern     ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");

        // Week 12-05: ✅ GAME STATE PATTERN - Create state context!
        GameStateContext context = new GameStateContext();

        // Start with MenuState
        context.setState(new MenuState());

        // Main game loop - update state machine
        while (context.isRunning() && context.getCurrentState() != null) {
            // Update current state (may trigger transition)
            context.update(0.016f); // ~60 FPS delta time

            // Render current state
            context.render();

            // Small delay to prevent busy loop in simplified version
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("\n[MainWithGameState] Game ended.");
    }
}
