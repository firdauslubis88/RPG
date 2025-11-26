package battle.subsystems;

/**
 * Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN)
 *
 * Complex sound subsystem for battle sequences.
 * Client must initialize in CORRECT ORDER:
 * 1. init() - Initialize audio device
 * 2. loadBattleSounds() - Load sound assets
 * 3. Then can play sounds
 *
 * Problem: Client must know these internals!
 */
public class BattleSoundSystem {
    private boolean initialized = false;
    private boolean soundsLoaded = false;
    private String currentMusic = null;

    /**
     * Step 1: Initialize sound system
     * MUST be called first!
     */
    public void init() {
        System.out.println("[SoundSystem] Initializing...");
        System.out.println("  → Opening audio device");
        System.out.println("  → Setting up audio mixer");
        initialized = true;
        System.out.println("  ✓ Sound system ready");
    }

    /**
     * Step 2: Load battle sounds
     * REQUIRES: init() first!
     */
    public void loadBattleSounds() {
        if (!initialized) {
            System.out.println("[SoundSystem] ✗ ERROR: Not initialized!");
            System.out.println("  → Client forgot to call init() first!");
            return;
        }
        System.out.println("[SoundSystem] Loading battle sounds...");
        System.out.println("  → Loading attack sounds");
        System.out.println("  → Loading magic sounds");
        System.out.println("  → Loading UI sounds");
        soundsLoaded = true;
        System.out.println("  ✓ Sounds loaded");
    }

    /**
     * Play battle music
     * REQUIRES: loadBattleSounds() first!
     */
    public void playBattleMusic(String musicFile) {
        if (!soundsLoaded) {
            System.out.println("[SoundSystem] ✗ ERROR: Sounds not loaded!");
            return;
        }
        currentMusic = musicFile;
        System.out.println("[SoundSystem] ♪ Now playing: " + musicFile);
    }

    /**
     * Play attack sound
     */
    public void playAttackSound() {
        if (!soundsLoaded) {
            System.out.println("[SoundSystem] ✗ ERROR: Sounds not loaded!");
            return;
        }
        System.out.println("[SoundSystem] 🔊 *slash*");
    }

    /**
     * Play defend sound
     */
    public void playDefendSound() {
        if (!soundsLoaded) {
            System.out.println("[SoundSystem] ✗ ERROR: Sounds not loaded!");
            return;
        }
        System.out.println("[SoundSystem] 🔊 *clang*");
    }

    /**
     * Play magic sound
     */
    public void playMagicSound() {
        if (!soundsLoaded) {
            System.out.println("[SoundSystem] ✗ ERROR: Sounds not loaded!");
            return;
        }
        System.out.println("[SoundSystem] 🔊 *whoosh*");
    }

    /**
     * Play boss roar
     */
    public void playBossRoar() {
        if (!soundsLoaded) {
            System.out.println("[SoundSystem] ✗ ERROR: Sounds not loaded!");
            return;
        }
        System.out.println("[SoundSystem] 🔊 *ROAR!*");
    }

    /**
     * Stop current music
     */
    public void stopMusic() {
        if (currentMusic != null) {
            System.out.println("[SoundSystem] Stopping: " + currentMusic);
            currentMusic = null;
        }
    }

    /**
     * Cleanup - client must call this!
     */
    public void cleanup() {
        System.out.println("[SoundSystem] Cleaning up...");
        stopMusic();
        if (soundsLoaded) {
            System.out.println("  → Unloading sounds");
            soundsLoaded = false;
        }
        if (initialized) {
            System.out.println("  → Closing audio device");
            initialized = false;
        }
        System.out.println("  ✓ Sound system cleaned up");
    }
}
