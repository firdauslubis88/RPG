package level;

/**
 * Week 13-02: Template Method Pattern (SOLUTION)
 *
 * Concrete implementation for Dungeon level.
 * Only implements the SPECIFIC details, algorithm is in LevelLoader.
 *
 * Compare to 13-01: No more duplicated algorithm structure!
 */
public class DungeonLevelLoader extends LevelLoader {

    @Override
    protected String getLevelName() {
        return "DARK DUNGEON";
    }

    @Override
    protected void loadAssets() {
        System.out.println("  → Loading stone wall textures");
        System.out.println("  → Loading torch flame sprites");
        System.out.println("  → Loading skeleton animations");
        System.out.println("  → Loading goblin animations");
    }

    @Override
    protected void buildWorld() {
        System.out.println("  → Generating dungeon layout (10x10 rooms)");
        System.out.println("  → Placing wall tiles");
        System.out.println("  → Adding torch lighting effects");
        System.out.println("  → Placing treasure chests (5 found)");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("  → Spawning 5 Skeletons in corridors");
        System.out.println("  → Spawning 3 Goblins near treasure");
        System.out.println("  → Placing Dungeon Boss in final room");
        System.out.println("  → Total enemies: 9");
    }

    @Override
    protected void playBackgroundMusic() {
        System.out.println("  ♪ Now playing: eerie_dungeon_ambience.mp3");
    }
}
