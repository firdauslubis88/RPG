package level;

/**
 * Week 13-02: Template Method Pattern (SOLUTION)
 *
 * Abstract class defining the SKELETON of level loading algorithm.
 * The loadLevel() method is the TEMPLATE METHOD - it defines the
 * fixed sequence of steps. Subclasses implement the specific steps.
 *
 * Benefits:
 * - Algorithm defined ONCE (no duplication)
 * - Consistent order GUARANTEED (subclasses can't change sequence)
 * - Easy to add new levels (just extend this class)
 * - Polymorphism enabled (can use LevelLoader as base type)
 * - Hook methods allow optional customization
 *
 * Hollywood Principle: "Don't call us, we'll call you!"
 * - The superclass calls subclass methods, not the other way around
 */
public abstract class LevelLoader {

    /**
     * TEMPLATE METHOD - defines the algorithm skeleton
     *
     * This method is FINAL to prevent subclasses from changing
     * the algorithm structure. They can only implement the steps.
     */
    public final void loadLevel() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       LOADING: " + String.format("%-23s", getLevelName()) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Step 1: Load assets (MUST be implemented)
        System.out.println("[Step 1] Loading assets...");
        loadAssets();
        System.out.println("  ✓ Assets loaded");

        // Step 2: Build world (MUST be implemented)
        System.out.println("\n[Step 2] Building world...");
        buildWorld();
        System.out.println("  ✓ World built");

        // Step 3: Spawn enemies (MUST be implemented)
        System.out.println("\n[Step 3] Spawning enemies...");
        spawnEnemies();
        System.out.println("  ✓ Enemies spawned");

        // Step 4: Play music (uses HOOK method)
        System.out.println("\n[Step 4] Starting background music...");
        if (shouldPlayMusic()) {
            playBackgroundMusic();
            System.out.println("  ✓ Music started");
        } else {
            System.out.println("  → Music disabled for this level");
        }

        // Optional hook for post-load setup
        afterLoad();

        System.out.println("\n════════════════════════════════════════");
        System.out.println("✓ " + getLevelName() + " loaded successfully!");
        System.out.println("════════════════════════════════════════\n");
    }

    // ═══════════════════════════════════════════════════════════════
    // PRIMITIVE OPERATIONS (abstract - MUST be implemented)
    // ═══════════════════════════════════════════════════════════════

    /**
     * Get the level name for display
     */
    protected abstract String getLevelName();

    /**
     * Load level-specific assets (textures, sounds, animations)
     */
    protected abstract void loadAssets();

    /**
     * Build the world (terrain, structures, decorations)
     */
    protected abstract void buildWorld();

    /**
     * Spawn enemies for this level
     */
    protected abstract void spawnEnemies();

    /**
     * Play background music for this level
     */
    protected abstract void playBackgroundMusic();

    // ═══════════════════════════════════════════════════════════════
    // HOOK METHODS (optional - can be overridden)
    // ═══════════════════════════════════════════════════════════════

    /**
     * Hook: Should background music play?
     * Default is true. Override to disable music (e.g., boss intro)
     */
    protected boolean shouldPlayMusic() {
        return true;  // Default: play music
    }

    /**
     * Hook: Called after level is fully loaded
     * Default does nothing. Override for custom setup.
     */
    protected void afterLoad() {
        // Default: do nothing
        // Subclasses can override for custom post-load logic
    }
}
