package engine.audio;

/**
 * Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN)
 *
 * Complex audio subsystem that client must interact with directly.
 * In a real game engine, this would have:
 * - AudioCodec (decoding formats)
 * - AudioBuffer (sample storage)
 * - AudioMixer (channel mixing)
 * - AudioPlayer (playback control)
 *
 * Client must know the CORRECT ORDER to initialize all these!
 */
public class AudioSystem {
    private boolean codecInitialized = false;
    private boolean bufferAllocated = false;
    private boolean mixerReady = false;
    private String currentMusic = null;

    /**
     * Step 1: Initialize audio codec
     * MUST be called first!
     */
    public void initCodec(int sampleRate, int bitDepth) {
        System.out.println("[AudioCodec] Initializing...");
        System.out.println("  → Sample rate: " + sampleRate + " Hz");
        System.out.println("  → Bit depth: " + bitDepth + " bit");
        codecInitialized = true;
        System.out.println("  ✓ Codec initialized");
    }

    /**
     * Step 2: Allocate audio buffer
     * REQUIRES: initCodec() first!
     */
    public void allocateBuffer(int bufferSize) {
        if (!codecInitialized) {
            System.out.println("[AudioBuffer] ✗ ERROR: Codec not initialized!");
            System.out.println("  → Client forgot to call initCodec() first!");
            return;
        }
        System.out.println("[AudioBuffer] Allocating " + bufferSize + " bytes...");
        bufferAllocated = true;
        System.out.println("  ✓ Buffer allocated");
    }

    /**
     * Step 3: Setup audio mixer
     * REQUIRES: allocateBuffer() first!
     */
    public void setupMixer(float masterVolume) {
        if (!bufferAllocated) {
            System.out.println("[AudioMixer] ✗ ERROR: Buffer not allocated!");
            System.out.println("  → Client forgot to call allocateBuffer() first!");
            return;
        }
        System.out.println("[AudioMixer] Setting up mixer...");
        System.out.println("  → Master volume: " + (int)(masterVolume * 100) + "%");
        mixerReady = true;
        System.out.println("  ✓ Mixer ready");
    }

    /**
     * Play background music
     * REQUIRES: setupMixer() first!
     */
    public void playMusic(String musicFile) {
        if (!mixerReady) {
            System.out.println("[AudioPlayer] ✗ ERROR: Mixer not ready!");
            System.out.println("  → Client forgot to initialize audio properly!");
            return;
        }
        currentMusic = musicFile;
        System.out.println("[AudioPlayer] ♪ Now playing: " + musicFile);
    }

    /**
     * Play sound effect
     */
    public void playSound(String soundFile) {
        if (!mixerReady) {
            System.out.println("[AudioPlayer] ✗ ERROR: Cannot play sound!");
            return;
        }
        System.out.println("[AudioPlayer] Playing SFX: " + soundFile);
    }

    /**
     * Stop current music
     */
    public void stopMusic() {
        if (currentMusic != null) {
            System.out.println("[AudioPlayer] Stopping: " + currentMusic);
            currentMusic = null;
        }
    }

    /**
     * Cleanup - client must call this!
     */
    public void cleanup() {
        System.out.println("[AudioSystem] Cleaning up...");
        stopMusic();
        if (mixerReady) {
            System.out.println("  → Releasing mixer");
            mixerReady = false;
        }
        if (bufferAllocated) {
            System.out.println("  → Freeing buffer");
            bufferAllocated = false;
        }
        if (codecInitialized) {
            System.out.println("  → Closing codec");
            codecInitialized = false;
        }
        System.out.println("  ✓ Audio system cleaned up");
    }
}
