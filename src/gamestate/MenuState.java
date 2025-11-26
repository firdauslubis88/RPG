package gamestate;

import ui.MainMenu;
import difficulty.DifficultyStrategy;
import level.LevelLoader;

/**
 * Week 12-05 + Week 13: MenuState (Game State Pattern)
 *
 * Represents the main menu state where player selects difficulty and level.
 * Transitions to PlayingState after selections are made.
 *
 * Week 13 Update:
 * - Now also handles level selection (Template Method Pattern)
 * - Passes both strategy and levelLoader to PlayingState
 */
public class MenuState implements GameState {
    private DifficultyStrategy selectedStrategy;
    private LevelLoader selectedLevel;
    private boolean selectionComplete;

    @Override
    public void enter() {
        System.out.println("\n[MenuState] Entering main menu...");
        selectionComplete = false;
    }

    @Override
    public GameState update(float deltaTime) {
        // Show menu and get selections
        if (!selectionComplete) {
            MainMenu menu = new MainMenu();
            selectedStrategy = menu.show();       // Shows difficulty selection
            selectedLevel = menu.getSelectedLevel();  // Gets level selection
            selectionComplete = true;
        }

        // Transition to PlayingState with selected strategy and level
        if (selectionComplete && selectedStrategy != null && selectedLevel != null) {
            return new PlayingState(selectedStrategy, selectedLevel);
        }

        return null; // Stay in menu (shouldn't happen normally)
    }

    @Override
    public void render() {
        // Menu rendering is handled by MainMenu.show()
    }

    @Override
    public void exit() {
        System.out.println("[MenuState] Exiting main menu...");
    }

    @Override
    public String getStateName() {
        return "MENU";
    }
}
