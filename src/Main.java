import gamestate.*;

/**
 * Week 13: Main with Game State Pattern (FULLY INTEGRATED)
 *
 * Design Patterns Used:
 * - Game State Pattern: Game flow management (Menu → Playing → Battle → Victory/Defeat)
 * - Strategy Pattern: Difficulty selection (EASY, NORMAL, HARD)
 * - Template Method Pattern: Level loading
 * - Facade Pattern: Battle system
 * - Observer Pattern: Event system
 * - Command Pattern: Input handling
 * - Factory Pattern: Obstacle creation
 * - Object Pool Pattern: Obstacle reuse
 * - Singleton Pattern: GameManager, EventBus
 *
 * Game State Flow:
 * MenuState → PlayingState → BattleState → VictoryState/DefeatState
 *     ↓                                              ↓
 * (select difficulty & level)                    (play again?)
 *                                                    ↓
 *                                              MenuState (loop)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║         DUNGEON ESCAPE                 ║");
        System.out.println("║   A Design Pattern Learning Game       ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");

        // Week 12-05: Game State Pattern - Create state context
        GameStateContext context = new GameStateContext();

        // Start with MenuState
        context.setState(new MenuState());

        // Main game loop - update state machine
        while (context.isRunning() && context.getCurrentState() != null) {
            // Update current state (may trigger transition)
            context.update(0.016f); // ~60 FPS delta time

            // Render current state
            context.render();

            // Small delay to prevent busy loop in menu states
            // (PlayingState has its own 60 FPS loop)
            if (!(context.getCurrentState() instanceof PlayingState)) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        System.out.println("\n[Game] Thanks for playing Dungeon Escape!");
        System.out.println("[Game] Design Patterns: Game State, Strategy, Template Method,");
        System.out.println("       Facade, Observer, Command, Factory, Object Pool, Singleton");
    }
}
