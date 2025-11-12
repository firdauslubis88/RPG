import entities.GameManager;
import events.GameEvent;
import events.GameEventListener;
import events.AchievementUnlockedEvent;
import utils.GridRenderer;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 11-04: HUD with Observer Pattern (SOLUTION)
 *
 * ✅ SOLUTION: HUD implements GameEventListener
 *
 * Benefits:
 * - No dependency on AchievementSystem!
 * - Listens to AchievementUnlockedEvent
 * - Stores achievements locally for display
 * - Simple constructor with no parameters
 *
 * Evolution from Week 11-03:
 * ❌ Before: HUD(achievementSystem) - tight coupling!
 * ✅ Now: HUD() - no dependencies, listens to events!
 */
public class HUD implements GameEventListener {
    private List<String> achievements;

    /**
     * Week 11-04: ✅ OBSERVER PATTERN - Simple constructor!
     *
     * Before (11-03): HUD(achievementSystem) - needs parameter!
     * Now (11-04): HUD() - no parameters needed!
     */
    public HUD() {
        this.achievements = new ArrayList<>();
    }

    /**
     * Week 11-04: ✅ OBSERVER PATTERN - Listen to achievement events
     *
     * HUD listens to AchievementUnlockedEvent and stores achievements locally
     */
    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof AchievementUnlockedEvent) {
            AchievementUnlockedEvent achievementEvent = (AchievementUnlockedEvent) event;
            achievements.add(achievementEvent.getAchievementName());
        }
    }

    /**
     * Week 11-04: Draws the HUD with game stats AND achievements
     *
     * ✅ SOLUTION: HUD uses locally stored achievements (from events)
     * No need to query AchievementSystem!
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

        // Week 11-04: Draw achievements section (from locally stored achievements)
        int achievementRow = startRow + 8;
        GridRenderer.drawText("╔════════════════════════════╗", startCol - 1, achievementRow);
        GridRenderer.drawText("║      ACHIEVEMENTS          ║", startCol - 1, achievementRow + 1);
        GridRenderer.drawText("╠════════════════════════════╣", startCol - 1, achievementRow + 2);

        // ✅ OBSERVER PATTERN: Use locally stored achievements (updated via events)
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
