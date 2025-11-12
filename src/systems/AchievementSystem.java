package systems;

import events.EventBus;
import events.GameEvent;
import events.GameEventListener;
import events.DamageTakenEvent;
import events.CoinCollectedEvent;
import events.GameTimeEvent;
import events.AchievementUnlockedEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 11-04: Achievement System with Observer Pattern (SOLUTION)
 *
 * ✅ SOLUTION: AchievementSystem implements GameEventListener
 *
 * Tracks player achievements like:
 * - "First Blood" - took damage for the first time
 * - "Coin Collector" - collected 5 coins
 * - "Survivor" - survived for 30 seconds
 *
 * Benefits:
 * - No dependency on SoundSystem!
 * - Listens to game events and checks achievements
 * - Publishes AchievementUnlockedEvent when achievement unlocked
 * - SoundSystem and HUD listen to AchievementUnlockedEvent
 *
 * Evolution from Week 11-03:
 * ❌ Before: Player calls achievementSystem.onDamageTaken(), needs SoundSystem
 * ✅ Now: AchievementSystem listens to DamageTakenEvent, publishes AchievementUnlockedEvent
 */
public class AchievementSystem implements GameEventListener {
    private List<String> unlockedAchievements;

    // Tracking variables
    private boolean firstBloodUnlocked = false;
    private int coinsCollected = 0;
    private boolean coinCollectorUnlocked = false;
    private boolean survivorUnlocked = false;

    /**
     * Week 11-04: ✅ OBSERVER PATTERN - No dependencies!
     *
     * Before (11-03): AchievementSystem(soundSystem) - dependency!
     * Now (11-04): AchievementSystem() - no dependencies!
     */
    public AchievementSystem() {
        this.unlockedAchievements = new ArrayList<>();
    }

    /**
     * Week 11-04: ✅ OBSERVER PATTERN - Listen to all game events
     *
     * AchievementSystem reacts to different event types:
     * - DamageTakenEvent → Check "First Blood" achievement
     * - CoinCollectedEvent → Check "Coin Collector" achievement
     * - GameTimeEvent → Check "Survivor" achievement
     */
    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof DamageTakenEvent) {
            onDamageTaken((DamageTakenEvent) event);
        } else if (event instanceof CoinCollectedEvent) {
            onCoinCollected();
        } else if (event instanceof GameTimeEvent) {
            GameTimeEvent timeEvent = (GameTimeEvent) event;
            checkSurvivor(timeEvent.getElapsedTime());
        }
    }

    /**
     * Check for "First Blood" achievement when damage is taken.
     *
     * Week 11-04: Called automatically when DamageTakenEvent is received
     */
    private void onDamageTaken(DamageTakenEvent event) {
        if (!firstBloodUnlocked) {
            firstBloodUnlocked = true;
            unlockAchievement("First Blood - Took damage for the first time!");
        }
    }

    /**
     * Check for "Coin Collector" achievement when coin is collected.
     *
     * Week 11-04: Called automatically when CoinCollectedEvent is received
     */
    private void onCoinCollected() {
        coinsCollected++;

        if (coinsCollected >= 5 && !coinCollectorUnlocked) {
            coinCollectorUnlocked = true;
            unlockAchievement("Coin Collector - Collected 5 coins!");
        }
    }

    /**
     * Check for "Survivor" achievement based on elapsed time.
     *
     * Week 11-04: Called automatically when GameTimeEvent is received
     */
    private void checkSurvivor(float elapsedTime) {
        if (elapsedTime >= 30.0f && !survivorUnlocked) {
            survivorUnlocked = true;
            unlockAchievement("Survivor - Survived for 30 seconds!");
        }
    }

    /**
     * Week 11-04: ✅ OBSERVER PATTERN - Publish AchievementUnlockedEvent
     *
     * Before (11-03): Calls soundSystem.playAchievementSound() directly
     * Now (11-04): Publishes event, SoundSystem and HUD listen automatically
     */
    private void unlockAchievement(String achievement) {
        unlockedAchievements.add(achievement);

        // ✅ OBSERVER PATTERN: Publish event instead of calling systems
        EventBus.getInstance().publish(new AchievementUnlockedEvent(achievement));

        // Print notification
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
