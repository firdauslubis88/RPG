package level;

/**
 * Week 13-02: Template Method Pattern (SOLUTION)
 *
 * Concrete implementation for Castle level.
 * Only implements the SPECIFIC details, algorithm is in LevelLoader.
 */
public class CastleLevelLoader extends LevelLoader {

    @Override
    protected String getLevelName() {
        return "ROYAL CASTLE";
    }

    @Override
    protected void loadAssets() {
        System.out.println("  → Loading stone brick textures");
        System.out.println("  → Loading banner and flag sprites");
        System.out.println("  → Loading knight animations");
        System.out.println("  → Loading royal guard animations");
    }

    @Override
    protected void buildWorld() {
        System.out.println("  → Constructing castle walls");
        System.out.println("  → Building throne room");
        System.out.println("  → Adding towers and battlements");
        System.out.println("  → Placing royal decorations");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("  → Spawning castle guards (8 guards)");
        System.out.println("  → Placing elite knights (4 knights)");
        System.out.println("  → Summoning the King Boss in throne room");
        System.out.println("  → Total enemies: 13");
    }

    @Override
    protected void playBackgroundMusic() {
        System.out.println("  ♪ Now playing: royal_fanfare.mp3");
    }
}
