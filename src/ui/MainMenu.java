package ui;

import java.util.Scanner;

/**
 * MainMenu - Simple text-based menu for difficulty selection
 *
 * Week 12-01: HARDCODED DIFFICULTY (ANTI-PATTERN)
 *
 * This menu allows player to select difficulty before starting the game.
 * Difficulty cannot be changed during gameplay.
 */
public class MainMenu {
    private Scanner scanner;
    private String selectedDifficulty;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
        this.selectedDifficulty = null;
    }

    /**
     * Display menu and get difficulty selection
     * @return Selected difficulty: "EASY", "NORMAL", or "HARD"
     */
    public String show() {
        clearScreen();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║         DUNGEON ESCAPE                 ║");
        System.out.println("║     Week 12-01: Difficulty System      ║");
        System.out.println("║                                        ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║                                        ║");
        System.out.println("║  SELECT DIFFICULTY:                    ║");
        System.out.println("║                                        ║");
        System.out.println("║  1. EASY                               ║");
        System.out.println("║     - Only Spikes and Goblins          ║");
        System.out.println("║     - Slow spawn rate                  ║");
        System.out.println("║                                        ║");
        System.out.println("║  2. NORMAL                             ║");
        System.out.println("║     - Spikes, Goblins, and Wolves      ║");
        System.out.println("║     - Medium spawn rate                ║");
        System.out.println("║                                        ║");
        System.out.println("║  3. HARD                               ║");
        System.out.println("║     - All enemy types                  ║");
        System.out.println("║     - Fast spawn + auto-spawn enemies  ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("\nEnter your choice (1-3): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                selectedDifficulty = "EASY";
                break;
            case "2":
                selectedDifficulty = "NORMAL";
                break;
            case "3":
                selectedDifficulty = "HARD";
                break;
            default:
                System.out.println("Invalid choice! Defaulting to NORMAL difficulty.");
                selectedDifficulty = "NORMAL";
                break;
        }

        System.out.println("\nDifficulty set to: " + selectedDifficulty);
        System.out.println("Press Enter to start...");
        scanner.nextLine();

        return selectedDifficulty;
    }

    public String getSelectedDifficulty() {
        return selectedDifficulty;
    }

    private void clearScreen() {
        // ANSI escape code to clear screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
