import ui.MainMenu;
import difficulty.DifficultyStrategy;
import level.LevelLoader;

/**
 * Week 13: Main with Template Method + Facade Pattern
 *
 * ✅ KEPT: Strategy Pattern for flexible difficulty (from 12-02)
 * ✅ NEW: Template Method Pattern for level loading (13-02)
 * ✅ NEW: Facade Pattern for battle system (13-04)
 *
 * Flow:
 * 1. MainMenu shows difficulty selection (Strategy Pattern)
 * 2. MainMenu shows level selection (Template Method Pattern)
 * 3. GameEngine loads level using LevelLoader
 * 4. When player reaches exit, BattleFacade runs battle (Facade Pattern)
 */
public class Main {
    public static void main(String[] args) {
        // Show menu and get selections
        MainMenu menu = new MainMenu();
        DifficultyStrategy strategy = menu.show();
        LevelLoader levelLoader = menu.getSelectedLevel();

        // Create game engine with selected strategy and level loader
        GameEngine engine = new GameEngine(strategy, levelLoader);
        engine.start();
    }
}
