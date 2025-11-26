package systems;

import events.GameEvent;
import events.GameEventListener;
import events.DamageTakenEvent;
import events.CoinCollectedEvent;
import events.AchievementUnlockedEvent;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Week 11-04: Sound System with Observer Pattern (SOLUTION)
 * Week 13: Added background music support for different levels
 *
 * ✅ SOLUTION: SoundSystem implements GameEventListener
 *
 * Features:
 * - Console beep for game events (damage, coins, achievements)
 * - Background music playback from WAV files (Week 13)
 *
 * Benefits:
 * - Player doesn't know about SoundSystem!
 * - SoundSystem listens to events and reacts
 * - Easy to add new sounds (just listen to new event types)
 * - Can be enabled/disabled by registering/unregistering from EventBus
 */
public class SoundSystem implements GameEventListener {

    // Week 13: Background music clip
    private static Clip backgroundMusic;
    private static String currentMusicFile = "";

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

    // ════════════════════════════════════════════════════════════════
    // Week 13: Background Music Methods
    // ════════════════════════════════════════════════════════════════

    /**
     * Play background music from a WAV file
     * Week 13: Each level can have different music
     *
     * @param musicFile Path to WAV file (e.g., "assets/music/dungeon.wav")
     */
    public static void playBackgroundMusic(String musicFile) {
        // Don't restart if same music is already playing
        if (musicFile.equals(currentMusicFile) && backgroundMusic != null && backgroundMusic.isRunning()) {
            return;
        }

        // Stop current music first
        stopBackgroundMusic();

        try {
            File audioFile = new File(musicFile);
            if (!audioFile.exists()) {
                System.out.println("  [Sound] Music file not found: " + musicFile);
                System.out.println("  [Sound] (Playing in silent mode)");
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);  // Loop the music
            backgroundMusic.start();
            currentMusicFile = musicFile;
            System.out.println("  [Sound] ♪ Playing: " + musicFile);

        } catch (UnsupportedAudioFileException e) {
            System.out.println("  [Sound] Unsupported format: " + musicFile);
        } catch (IOException e) {
            System.out.println("  [Sound] Error loading: " + musicFile);
        } catch (LineUnavailableException e) {
            System.out.println("  [Sound] Audio line unavailable");
        }
    }

    /**
     * Stop background music
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
            backgroundMusic = null;
            currentMusicFile = "";
        }
    }

    /**
     * Check if music is currently playing
     */
    public static boolean isMusicPlaying() {
        return backgroundMusic != null && backgroundMusic.isRunning();
    }
}
