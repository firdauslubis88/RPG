package gamestate;

import difficulty.DifficultyStrategy;
import java.util.Scanner;

/**
 * Week 12-05: DefeatState (Game State Pattern)
 *
 * Represents the game over screen when player dies.
 * Transitions to:
 * - MenuState if player chooses to try again
 * - null (exit game) if player chooses to quit
 */
public class DefeatState implements GameState {
    private DifficultyStrategy strategy;
    private Scanner scanner;
    private boolean inputProcessed;

    public DefeatState(DifficultyStrategy strategy) {
        this.strategy = strategy;
        this.scanner = new Scanner(System.in);
        this.inputProcessed = false;
    }

    @Override
    public void enter() {
        System.out.println("\n[DefeatState] Player defeated!");
        showDefeatScreen();
    }

    @Override
    public GameState update(float deltaTime) {
        if (!inputProcessed) {
            System.out.print("\nTry again? (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            inputProcessed = true;

            if (input.equals("Y")) {
                return new MenuState(); // Return to menu
            } else {
                // Exit game
                System.out.println("\nThanks for playing!");
                return null;
            }
        }

        return null; // Stay in defeat state
    }

    @Override
    public void render() {
        // Defeat screen already rendered in enter()
    }

    @Override
    public void exit() {
        System.out.println("[DefeatState] Exiting defeat screen...");
    }

    @Override
    public String getStateName() {
        return "DEFEAT";
    }

    private void showDefeatScreen() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║        💀  GAME OVER  💀               ║");
        System.out.println("║                                        ║");
        System.out.println("║   You were defeated in the dungeon...  ║");
        System.out.println("║                                        ║");
        System.out.println("║   Difficulty: " + String.format("%-24s", strategy.getName()) + " ║");
        System.out.println("║                                        ║");
        System.out.println("║   Don't give up! Try again!            ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
}
