package level;

import world.DungeonMap;
import world.CastleMapLayout;

/**
 * Week 13-02: Template Method Pattern (SOLUTION)
 *
 * Concrete implementation for Castle level.
 * Week 13: Now sets active map with CastleMapLayout!
 */
public class CastleLevelLoader extends LevelLoader {

    @Override
    protected String getLevelName() {
        return "HAUNTED CASTLE";
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
        // Week 13: Set the active map layout!
        DungeonMap.setActiveMap(new CastleMapLayout());
        System.out.println("  → Constructing castle halls (25x25 grid)");
        System.out.println("  → Wall char: '|' and '-' (castle walls)");
        System.out.println("  → Floor char: ':' (stone floor)");
        System.out.println("  → Adding gothic architecture");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("  → Goblin char: 'K' (Knight)");
        System.out.println("  → Wolf char: 'G' (Ghost)");
        System.out.println("  → Spike char: 'X' (Sword trap)");
        System.out.println("  → Summoning Castle Boss in throne room");
    }

    @Override
    protected void playBackgroundMusic() {
        System.out.println("  ♪ Now playing: " + DungeonMap.getMusic());
    }
}
