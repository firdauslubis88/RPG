package ui;

import difficulty.DifficultyStrategy;
import difficulty.DemoDifficulty;
import difficulty.EasyDifficulty;
import difficulty.NormalDifficulty;
import difficulty.HardDifficulty;
import level.LevelLoader;
import level.DungeonLevelLoader;
import level.ForestLevelLoader;
import level.CastleLevelLoader;
import level.BossArenaLoader;

import java.util.Scanner;

/**
 * MainMenu - Menu for difficulty and level selection
 *
 * Week 13: Template Method + Facade Pattern Integration
 *
 * ✅ KEPT: Strategy Pattern for difficulty (from 12-02)
 * ✅ NEW: Template Method Pattern for level loading (13-02)
 *
 * Features:
 * - Difficulty selection (Strategy Pattern)
 * - Level selection (Template Method Pattern)
 * - Level loading uses abstract LevelLoader
 */
public class MainMenu {
    private Scanner scanner;
    private DifficultyStrategy selectedStrategy;
    private LevelLoader selectedLevel;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
        this.selectedStrategy = null;
        this.selectedLevel = null;
    }

    /**
     * Display menu and get difficulty selection
     *
     * Week 13: Template Method + Facade Pattern
     * ✅ STRATEGY PATTERN - Returns strategy object!
     * ✅ TEMPLATE METHOD - Returns level loader!
     *
     * @return Selected difficulty strategy object
     */
    public DifficultyStrategy show() {
        clearScreen();

        // ════════════════════════════════════════════════════════════
        // STEP 1: Select Difficulty (Strategy Pattern)
        // ════════════════════════════════════════════════════════════
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║         DUNGEON ESCAPE                 ║");
        System.out.println("║   Week 13: Template + Facade Pattern   ║");
        System.out.println("║                                        ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║                                        ║");
        System.out.println("║  SELECT DIFFICULTY:                    ║");
        System.out.println("║                                        ║");
        System.out.println("║  0. DEMO (Testing/Demo)                ║");
        System.out.println("║     - Very slow spawns                 ║");
        System.out.println("║     - Boss only defends!               ║");
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
        System.out.print("\nEnter your choice (0-3): ");

        String difficultyChoice = scanner.nextLine().trim();

        // Week 12-02: ✅ STRATEGY PATTERN - Create strategy objects!
        switch (difficultyChoice) {
            case "0":
                selectedStrategy = new DemoDifficulty();
                break;
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

        // ════════════════════════════════════════════════════════════
        // STEP 2: Select Level (Template Method Pattern)
        // ════════════════════════════════════════════════════════════
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║  SELECT LEVEL:                         ║");
        System.out.println("║  (Template Method Pattern Demo)        ║");
        System.out.println("║                                        ║");
        System.out.println("║  1. DARK DUNGEON                       ║");
        System.out.println("║     - Stone walls, torches             ║");
        System.out.println("║     - Skeletons & Goblins              ║");
        System.out.println("║                                        ║");
        System.out.println("║  2. ENCHANTED FOREST                   ║");
        System.out.println("║     - Trees, mystical fog              ║");
        System.out.println("║     - Wolves & Forest Spirits          ║");
        System.out.println("║                                        ║");
        System.out.println("║  3. HAUNTED CASTLE                     ║");
        System.out.println("║     - Gothic architecture              ║");
        System.out.println("║     - Knights & Ghosts                 ║");
        System.out.println("║                                        ║");
        System.out.println("║  4. BOSS ARENA                         ║");
        System.out.println("║     - Final showdown arena             ║");
        System.out.println("║     - Direct boss battle!              ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("\nEnter your choice (1-4): ");

        String levelChoice = scanner.nextLine().trim();

        // Week 13-02: ✅ TEMPLATE METHOD PATTERN - Create level loader!
        switch (levelChoice) {
            case "1":
                selectedLevel = new DungeonLevelLoader();
                break;
            case "2":
                selectedLevel = new ForestLevelLoader();
                break;
            case "3":
                selectedLevel = new CastleLevelLoader();
                break;
            case "4":
                selectedLevel = new BossArenaLoader();
                break;
            default:
                System.out.println("Invalid choice! Defaulting to DARK DUNGEON.");
                selectedLevel = new DungeonLevelLoader();
                break;
        }

        System.out.println("\nPress Enter to start loading level...");
        scanner.nextLine();

        return selectedStrategy;
    }

    /**
     * Get the selected level loader
     * @return Selected LevelLoader (uses Template Method Pattern)
     */
    public LevelLoader getSelectedLevel() {
        return selectedLevel;
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
