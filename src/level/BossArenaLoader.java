package level;

/**
 * Week 13-02: Template Method Pattern (SOLUTION)
 *
 * Concrete implementation for Boss Arena.
 *
 * NOW FIXED! The Template Method Pattern GUARANTEES:
 * - Correct step order (can't spawn before loading assets!)
 * - All steps are executed (music won't be forgotten!)
 *
 * Also demonstrates HOOK METHOD: disables music during loading,
 * because the epic boss music should start when battle begins.
 */
public class BossArenaLoader extends LevelLoader {

    @Override
    protected String getLevelName() {
        return "BOSS ARENA";
    }

    @Override
    protected void loadAssets() {
        System.out.println("  → Loading Ancient Dragon sprite");
        System.out.println("  → Loading arena textures");
        System.out.println("  → Loading fire breath animations");
        System.out.println("  → Loading epic boss music track");
    }

    @Override
    protected void buildWorld() {
        System.out.println("  → Creating circular battle arena");
        System.out.println("  → Adding pillars for cover");
        System.out.println("  → Setting up dramatic lighting");
        System.out.println("  → Placing health potions in corners");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("  → Summoning ANCIENT DRAGON!");
        System.out.println("  → Dragon HP: 500");
        System.out.println("  → Dragon Level: LEGENDARY");
        System.out.println("  → Total enemies: 1 (but it's a BIG one!)");
    }

    @Override
    protected void playBackgroundMusic() {
        System.out.println("  → Music will start when battle begins...");
    }

    /**
     * HOOK METHOD: Override to disable music during loading
     * Epic boss music should only play when the battle starts!
     */
    @Override
    protected boolean shouldPlayMusic() {
        return false;  // Music disabled during loading
    }

    /**
     * HOOK METHOD: Custom post-load setup for boss arena
     */
    @Override
    protected void afterLoad() {
        System.out.println("\n[Boss Arena Special Setup]");
        System.out.println("  → Initializing boss health bar UI");
        System.out.println("  → Preparing dramatic entrance cutscene");
        System.out.println("  → Epic music ready to play on battle start");
    }
}
