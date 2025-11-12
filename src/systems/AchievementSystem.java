package systems;

import java.util.ArrayList;
import java.util.List;

/**
 * Week 11-03: Achievement System (TIGHT COUPLING DEMO)
 *
 * Tracks player achievements like:
 * - "First Blood" - took damage for the first time
 * - "Coin Collector" - collected 5 coins
 * - "Survivor" - survived for 30 seconds
 *
 * ❌ PROBLEM: Player must know about AchievementSystem!
 * - Player constructor needs AchievementSystem parameter
 * - Player.takeDamage() must call achievementSystem.onDamageTaken()
 * - Player.collectCoin() must call achievementSystem.onCoinCollected()
 *
 * This creates tight coupling and violates Single Responsibility!
 */
public class AchievementSystem {
    private List<String> unlockedAchievements;
    private SoundSystem soundSystem;

    // Tracking variables
    private boolean firstBloodUnlocked = false;
    private int coinsCollected = 0;
    private boolean coinCollectorUnlocked = false;
    private boolean survivorUnlocked = false;

    /**
     * ❌ ANTI-PATTERN: AchievementSystem also needs SoundSystem!
     * This creates a chain of dependencies.
     */
    public AchievementSystem(SoundSystem soundSystem) {
        this.soundSystem = soundSystem;
        this.unlockedAchievements = new ArrayList<>();
    }

    /**
     * Called when player takes damage.
     * Checks for "First Blood" achievement.
     */
    public void onDamageTaken(int damage) {
        if (!firstBloodUnlocked) {
            firstBloodUnlocked = true;
            unlockAchievement("First Blood - Took damage for the first time!");
        }
    }

    /**
     * Called when player collects a coin.
     * Checks for "Coin Collector" achievement.
     */
    public void onCoinCollected() {
        coinsCollected++;

        if (coinsCollected >= 5 && !coinCollectorUnlocked) {
            coinCollectorUnlocked = true;
            unlockAchievement("Coin Collector - Collected 5 coins!");
        }
    }

    /**
     * Called each frame to check time-based achievements.
     * Checks for "Survivor" achievement.
     */
    public void checkSurvivor(float elapsedTime) {
        if (elapsedTime >= 30.0f && !survivorUnlocked) {
            survivorUnlocked = true;
            unlockAchievement("Survivor - Survived for 30 seconds!");
        }
    }

    /**
     * Unlock an achievement.
     * Plays sound and adds to list.
     */
    private void unlockAchievement(String achievement) {
        unlockedAchievements.add(achievement);
        soundSystem.playAchievementSound();

        // Print notification (will be displayed in HUD later)
        System.out.println("\n*** ACHIEVEMENT UNLOCKED: " + achievement + " ***");
    }

    /**
     * Get all unlocked achievements.
     */
    public List<String> getUnlockedAchievements() {
        return unlockedAchievements;
    }

    /**
     * Get count of unlocked achievements.
     */
    public int getAchievementCount() {
        return unlockedAchievements.size();
    }
}
