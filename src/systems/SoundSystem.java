package systems;

/**
 * Week 11-03: Sound System (TIGHT COUPLING DEMO)
 *
 * Simple sound system using console bell character (\007).
 * In real games, this would use audio libraries.
 *
 * ❌ PROBLEM: Player must know about SoundSystem!
 * - Player constructor needs SoundSystem parameter
 * - Player.takeDamage() must call soundSystem.playHurtSound()
 * - Player.collectCoin() must call soundSystem.playCoinSound()
 *
 * This creates tight coupling!
 */
public class SoundSystem {

    /**
     * Play hurt sound when player takes damage.
     * Uses console bell (beep) character.
     */
    public void playHurtSound() {
        // Console bell - makes system beep
        System.out.print("\007");
        System.out.flush();
    }

    /**
     * Play coin collection sound.
     * Uses double beep for different sound.
     */
    public void playCoinSound() {
        // Double beep for coin collection
        System.out.print("\007\007");
        System.out.flush();
    }

    /**
     * Play achievement unlock sound.
     * Uses triple beep for special events.
     */
    public void playAchievementSound() {
        // Triple beep for achievement
        System.out.print("\007\007\007");
        System.out.flush();
    }
}
