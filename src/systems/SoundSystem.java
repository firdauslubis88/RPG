package systems;

import events.GameEvent;
import events.GameEventListener;
import events.DamageTakenEvent;
import events.CoinCollectedEvent;
import events.AchievementUnlockedEvent;

/**
 * Week 11-04: Sound System with Observer Pattern (SOLUTION)
 *
 * ✅ SOLUTION: SoundSystem implements GameEventListener
 *
 * Simple sound system using console bell character (\007).
 * In real games, this would use audio libraries.
 *
 * Benefits:
 * - Player doesn't know about SoundSystem!
 * - SoundSystem listens to events and reacts
 * - Easy to add new sounds (just listen to new event types)
 * - Can be enabled/disabled by registering/unregistering from EventBus
 *
 * Evolution from Week 11-03:
 * ❌ Before: Player calls soundSystem.playHurtSound() directly
 * ✅ Now: SoundSystem listens to DamageTakenEvent and plays sound automatically
 */
public class SoundSystem implements GameEventListener {

    /**
     * Week 11-04: ✅ OBSERVER PATTERN - Listen to all game events
     *
     * SoundSystem reacts to different event types:
     * - DamageTakenEvent → playHurtSound()
     * - CoinCollectedEvent → playCoinSound()
     * - AchievementUnlockedEvent → playAchievementSound()
     */
    @Override
    public void onEvent(GameEvent event) {
        // Filter events by type and play appropriate sound
        if (event instanceof DamageTakenEvent) {
            playHurtSound();
        } else if (event instanceof CoinCollectedEvent) {
            playCoinSound();
        } else if (event instanceof AchievementUnlockedEvent) {
            playAchievementSound();
        }
    }

    /**
     * Play hurt sound when player takes damage.
     * Uses console bell (beep) character.
     *
     * Week 11-04: Called automatically when DamageTakenEvent is published
     */
    private void playHurtSound() {
        // Console bell - makes system beep
        System.out.print("\007");
        System.out.flush();
    }

    /**
     * Play coin collection sound.
     * Uses double beep for different sound.
     *
     * Week 11-04: Called automatically when CoinCollectedEvent is published
     */
    private void playCoinSound() {
        // Double beep for coin collection
        System.out.print("\007\007");
        System.out.flush();
    }

    /**
     * Play achievement unlock sound.
     * Uses triple beep for special events.
     *
     * Week 11-04: Called automatically when AchievementUnlockedEvent is published
     */
    private void playAchievementSound() {
        // Triple beep for achievement
        System.out.print("\007\007\007");
        System.out.flush();
    }
}
