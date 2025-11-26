package level;

import world.DungeonMap;
import world.BossArenaLayout;

/**
 * Week 13-02: Template Method Pattern (SOLUTION)
 *
 * Concrete implementation for Boss Arena.
 * Week 13: Now sets active map with BossArenaLayout!
 *
 * Demonstrates HOOK METHOD: disables music during loading,
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
        // Week 13: Set the active map layout!
        DungeonMap.setActiveMap(new BossArenaLayout());
        System.out.println("  → Creating battle arena (25x25 grid)");
        System.out.println("  → Wall char: '=' (arena barriers)");
        System.out.println("  → Floor char: ' ' (smooth arena floor)");
        System.out.println("  → Adding central pillar for cover");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("  → (Arena has no minion spawns)");
        System.out.println("  → Summoning ANCIENT DRAGON!");
        System.out.println("  → Dragon HP: 500");
        System.out.println("  → Dragon Level: LEGENDARY");
    }

    @Override
    protected void playBackgroundMusic() {
        System.out.println("  → " + DungeonMap.getMusic() + " ready for battle start");
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
