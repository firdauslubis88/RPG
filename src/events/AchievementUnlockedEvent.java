package events;

/**
 * Week 11-04: AchievementUnlockedEvent (Observer Pattern)
 *
 * Published when an achievement is unlocked.
 *
 * Listeners:
 * - SoundSystem: Plays achievement sound
 * - HUD: Updates achievement display
 */
public class AchievementUnlockedEvent extends GameEvent {
    private final String achievementName;

    public AchievementUnlockedEvent(String achievementName) {
        super("AchievementUnlocked");
        this.achievementName = achievementName;
    }

    public String getAchievementName() {
        return achievementName;
    }

    @Override
    public String toString() {
        return String.format("AchievementUnlockedEvent[name='%s']", achievementName);
    }
}
