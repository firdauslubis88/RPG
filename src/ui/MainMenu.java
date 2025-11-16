package ui;

import difficulty.DifficultyStrategy;
import difficulty.EasyDifficulty;
import difficulty.NormalDifficulty;
import difficulty.HardDifficulty;

import java.util.Scanner;

/**
 * MainMenu - Simple text-based menu for difficulty selection
 *
 * Week 12-02: STRATEGY PATTERN (SOLUTION)
 *
 * ✅ SOLUTION: Returns DifficultyStrategy objects instead of strings!
 *
 * Evolution from Week 12-01:
 * ❌ Before: return "EASY", "NORMAL", or "HARD" strings
 * ✅ Now: return new EasyDifficulty(), new NormalDifficulty(), or new HardDifficulty()
 *
 * Benefits:
 * - Compile-time safety (no string typos!)
 * - Each difficulty is a proper strategy object
 * - Can be easily extended with new difficulties
 */
public class MainMenu {
    private Scanner scanner;
    private DifficultyStrategy selectedStrategy;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
        this.selectedStrategy = null;
    }

    /**
     * Display menu and get difficulty selection
     *
     * Week 12-02: ✅ STRATEGY PATTERN - Returns strategy object!
     *
     * @return Selected difficulty strategy object
     */
    public DifficultyStrategy show() {
        clearScreen();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║         DUNGEON ESCAPE                 ║");
        System.out.println("║     Week 12-02: Strategy Pattern       ║");
        System.out.println("║                                        ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║                                        ║");
        System.out.println("║  SELECT DIFFICULTY:                    ║");
        System.out.println("║                                        ║");
        System.out.println("║  1. EASY                               ║");
        System.out.println("║     - Only Spikes and Goblins          ║");
        System.out.println("║     - No continuous spawning           ║");
        System.out.println("║                                        ║");
        System.out.println("║  2. NORMAL                             ║");
        System.out.println("║     - Spikes, Goblins, and Wolves      ║");
        System.out.println("║     - No continuous spawning           ║");
        System.out.println("║                                        ║");
        System.out.println("║  3. HARD                               ║");
        System.out.println("║     - All enemy types (Wolf emphasis)  ║");
        System.out.println("║     - Continuous auto-spawning!        ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("\nEnter your choice (1-3): ");

        String choice = scanner.nextLine().trim();

        // Week 12-02: ✅ STRATEGY PATTERN - Create strategy objects instead of strings!
        switch (choice) {
            case "1":
                selectedStrategy = new EasyDifficulty();
                break;
            case "2":
                selectedStrategy = new NormalDifficulty();
                break;
            case "3":
                selectedStrategy = new HardDifficulty();
                break;
            default:
                System.out.println("Invalid choice! Defaulting to NORMAL difficulty.");
                selectedStrategy = new NormalDifficulty();
                break;
        }

        System.out.println("\nDifficulty set to: " + selectedStrategy.getName());
        System.out.println("Press Enter to start...");
        scanner.nextLine();

        return selectedStrategy;
    }

    public DifficultyStrategy getSelectedStrategy() {
        return selectedStrategy;
    }

    private void clearScreen() {
        // ANSI escape code to clear screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
