import ui.MainMenu;
import difficulty.DifficultyStrategy;

/**
 * Week 12-02: Main with Strategy Pattern
 *
 * ✅ SOLUTION: Strategy Pattern for flexible difficulty!
 *
 * Evolution from Week 12-01:
 * ❌ Before: MainMenu returns String difficulty
 * ✅ Now: MainMenu returns DifficultyStrategy object
 *
 * Benefits:
 * - Type-safe difficulty selection
 * - No string comparison needed
 * - Easy to extend with new difficulties
 */
public class Main {
    public static void main(String[] args) {
        // Week 12-02: ✅ STRATEGY PATTERN - Get strategy object from menu!
        MainMenu menu = new MainMenu();
        DifficultyStrategy strategy = menu.show();

        // Create game engine with selected strategy
        GameEngine engine = new GameEngine(strategy);
        engine.start();
    }
}
