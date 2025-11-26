package engine;

import entities.GameManager;
import events.GameEvent;
import events.GameEventListener;
import events.AchievementUnlockedEvent;
import utils.GridRenderer;
import java.util.ArrayList;
import java.util.List;

/**
 * HUD - Heads-Up Display with Observer Pattern
 *
 * Listens to AchievementUnlockedEvent and stores achievements locally for display.
 */
public class HUD implements GameEventListener {
    private List<String> achievements;

    public HUD() {
        this.achievements = new ArrayList<>();
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof AchievementUnlockedEvent) {
            AchievementUnlockedEvent achievementEvent = (AchievementUnlockedEvent) event;
            achievements.add(achievementEvent.getAchievementName());
        }
    }

    public void draw() {
        int score = GameManager.getInstance().getScore();
        float time = GameManager.getInstance().getGameTime();
        int level = GameManager.getInstance().getLevel();
        int hp = GameManager.getInstance().getHp();

        int startCol = 28;
        int startRow = 2;

        // Draw main HUD box
        GridRenderer.drawText("╔════════════════════════════╗", startCol - 1, startRow - 1);
        GridRenderer.drawText("║       HUD DISPLAY          ║", startCol - 1, startRow);
        GridRenderer.drawText("╠════════════════════════════╣", startCol - 1, startRow + 1);
        GridRenderer.drawText(String.format("║  Score: %-18d║", score), startCol - 1, startRow + 2);
        GridRenderer.drawText(String.format("║  HP: %d / %-16d║", hp, 100), startCol - 1, startRow + 3);
        GridRenderer.drawText(String.format("║  Time: %-19ds║", (int)time), startCol - 1, startRow + 4);
        GridRenderer.drawText(String.format("║  Level: %-18d║", level), startCol - 1, startRow + 5);
        GridRenderer.drawText("╚════════════════════════════╝", startCol - 1, startRow + 6);

        // Draw achievements section
        int achievementRow = startRow + 8;
        GridRenderer.drawText("╔════════════════════════════╗", startCol - 1, achievementRow);
        GridRenderer.drawText("║      ACHIEVEMENTS          ║", startCol - 1, achievementRow + 1);
        GridRenderer.drawText("╠════════════════════════════╣", startCol - 1, achievementRow + 2);

        if (achievements.isEmpty()) {
            GridRenderer.drawText("║  No achievements yet...    ║", startCol - 1, achievementRow + 3);
            GridRenderer.drawText("╚════════════════════════════╝", startCol - 1, achievementRow + 4);
        } else {
            int row = achievementRow + 3;
            for (int i = 0; i < Math.min(3, achievements.size()); i++) {
                String achievement = achievements.get(i);
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
