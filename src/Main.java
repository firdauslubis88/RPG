import ui.MainMenu;

/**
 * Week 12-01: Main with Menu System
 *
 * New Features:
 * - Main Menu for difficulty selection
 * - Pass difficulty to GameEngine
 */
public class Main {
    public static void main(String[] args) {
        // Week 12-01: Show main menu and get difficulty selection
        MainMenu menu = new MainMenu();
        String difficulty = menu.show();

        // Create game engine with selected difficulty
        GameEngine engine = new GameEngine(difficulty);
        engine.start();
    }
}
