package gamestate;

import ui.MainMenu;
import difficulty.DifficultyStrategy;

/**
 * Week 12-05: MenuState (Game State Pattern)
 *
 * Represents the main menu state where player selects difficulty.
 * Transitions to PlayingState after difficulty selection.
 */
public class MenuState implements GameState {
    private DifficultyStrategy selectedStrategy;
    private boolean strategySelected;

    @Override
    public void enter() {
        System.out.println("\n[MenuState] Entering main menu...");
        strategySelected = false;
    }

    @Override
    public GameState update(float deltaTime) {
        // Show menu and get difficulty selection
        if (!strategySelected) {
            MainMenu menu = new MainMenu();
            selectedStrategy = menu.show();
            strategySelected = true;
        }

        // Transition to SimplePlayingState with selected strategy
        if (strategySelected && selectedStrategy != null) {
            return new SimplePlayingState(selectedStrategy);
        }

        return null; // Stay in menu
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
