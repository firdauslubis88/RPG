package gamestate;

import difficulty.DifficultyStrategy;
import java.util.Scanner;

/**
 * Week 12-05: VictoryState (Game State Pattern)
 *
 * Represents the victory screen after defeating the boss.
 * Transitions to:
 * - MenuState if player chooses to play again
 * - null (exit game) if player chooses to quit
 */
public class VictoryState implements GameState {
    private DifficultyStrategy strategy;
    private Scanner scanner;
    private boolean inputProcessed;

    public VictoryState(DifficultyStrategy strategy) {
        this.strategy = strategy;
        this.scanner = new Scanner(System.in);
        this.inputProcessed = false;
    }

    @Override
    public void enter() {
        System.out.println("\n[VictoryState] Player won!");
        showVictoryScreen();
    }

    @Override
    public GameState update(float deltaTime) {
        if (!inputProcessed) {
            System.out.print("\nPlay again? (Y/N): ");
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

        return null; // Stay in victory state
    }

    @Override
    public void render() {
        // Victory screen already rendered in enter()
    }

    @Override
    public void exit() {
        System.out.println("[VictoryState] Exiting victory screen...");
    }

    @Override
    public String getStateName() {
        return "VICTORY";
    }

    private void showVictoryScreen() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║        🎉  VICTORY!  🎉                ║");
        System.out.println("║                                        ║");
        System.out.println("║   You defeated the boss and escaped   ║");
        System.out.println("║   the dungeon!                         ║");
        System.out.println("║                                        ║");
        System.out.println("║   Difficulty: " + String.format("%-24s", strategy.getName()) + " ║");
        System.out.println("║                                        ║");
        System.out.println("║   Congratulations, brave adventurer!   ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
}
