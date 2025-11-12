import entities.GameManager;
import systems.AchievementSystem;
import utils.GridRenderer;
import java.util.List;

/**
 * Week 11-03: HUD with Achievement Display (TIGHT COUPLING DEMO)
 *
 * ❌ PROBLEM: HUD now needs AchievementSystem!
 * - HUD constructor needs AchievementSystem parameter
 * - This creates tight coupling between HUD and AchievementSystem
 * - Want to add another system? Modify HUD constructor again!
 *
 * This demonstrates the tight coupling problem.
 */
public class HUD {
    private AchievementSystem achievementSystem;

    /**
     * Week 11-03: ❌ ANTI-PATTERN - Constructor now needs AchievementSystem!
     *
     * Before: No parameters (used Singleton only)
     * Now: Needs AchievementSystem parameter - tight coupling!
     */
    public HUD(AchievementSystem achievementSystem) {
        this.achievementSystem = achievementSystem;
    }

    /**
     * Week 11-03: Draws the HUD with game stats AND achievements
     *
     * ❌ PROBLEM: HUD must query AchievementSystem directly
     * This creates tight coupling - HUD depends on AchievementSystem
     */
    public void draw() {
        // Read from THE instance!
        int score = GameManager.getInstance().getScore();
        float time = GameManager.getInstance().getGameTime();
        int level = GameManager.getInstance().getLevel();
        int hp = GameManager.getInstance().getHp();

        // Position HUD to the right of the map
        // Map is 25 chars wide + 2 for borders = 27 chars total
        int startCol = 28;  // Start HUD at column 28 (right of map)
        int startRow = 2;   // Start from top (row 2)

        // Draw main HUD box
        GridRenderer.drawText("╔════════════════════════════╗", startCol - 1, startRow - 1);
        GridRenderer.drawText("║       HUD DISPLAY          ║", startCol - 1, startRow);
        GridRenderer.drawText("╠════════════════════════════╣", startCol - 1, startRow + 1);
        GridRenderer.drawText(String.format("║  Score: %-18d║", score), startCol - 1, startRow + 2);
        GridRenderer.drawText(String.format("║  HP: %d / %-16d║", hp, 100), startCol - 1, startRow + 3);
        GridRenderer.drawText(String.format("║  Time: %-19ds║", (int)time), startCol - 1, startRow + 4);
        GridRenderer.drawText(String.format("║  Level: %-18d║", level), startCol - 1, startRow + 5);
        GridRenderer.drawText("╚════════════════════════════╝", startCol - 1, startRow + 6);

        // Week 11-03: Draw achievements section
        int achievementRow = startRow + 8;
        GridRenderer.drawText("╔════════════════════════════╗", startCol - 1, achievementRow);
        GridRenderer.drawText("║      ACHIEVEMENTS          ║", startCol - 1, achievementRow + 1);
        GridRenderer.drawText("╠════════════════════════════╣", startCol - 1, achievementRow + 2);

        List<String> achievements = achievementSystem.getUnlockedAchievements();
        if (achievements.isEmpty()) {
            GridRenderer.drawText("║  No achievements yet...    ║", startCol - 1, achievementRow + 3);
            GridRenderer.drawText("╚════════════════════════════╝", startCol - 1, achievementRow + 4);
        } else {
            int row = achievementRow + 3;
            for (int i = 0; i < Math.min(3, achievements.size()); i++) {
                String achievement = achievements.get(i);
                // Truncate if too long
                if (achievement.length() > 26) {
                    achievement = achievement.substring(0, 23) + "...";
                }
                GridRenderer.drawText(String.format("║ %-26s ║", achievement), startCol - 1, row++);
            }

            if (achievements.size() > 3) {
                GridRenderer.drawText(String.format("║ ... and %d more           ║", achievements.size() - 3), startCol - 1, row++);
            }

            GridRenderer.drawText("╚════════════════════════════╝", startCol - 1, row);
        }
    }
}
