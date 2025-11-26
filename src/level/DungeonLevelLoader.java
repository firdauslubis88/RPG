package level;

import world.DungeonMap;
import world.DungeonMapLayout;
import systems.SoundSystem;

/**
 * Week 13-02: Template Method Pattern (SOLUTION)
 *
 * Concrete implementation for Dungeon level.
 * Only implements the SPECIFIC details, algorithm is in LevelLoader.
 *
 * Week 13: Now sets active map with DungeonMapLayout!
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
        // Week 13: Set the active map layout!
        DungeonMap.setActiveMap(new DungeonMapLayout());
        System.out.println("  → Generating dungeon layout (25x25 grid)");
        System.out.println("  → Wall char: '#' (stone blocks)");
        System.out.println("  → Floor char: '.' (cobblestone)");
        System.out.println("  → Adding torch lighting effects");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("  → Goblin char: 'g' (standard goblin)");
        System.out.println("  → Wolf char: 'w' (dungeon wolf)");
        System.out.println("  → Spike char: '^' (floor spikes)");
        System.out.println("  → Placing Dungeon Boss in final room");
    }

    @Override
    protected void playBackgroundMusic() {
        // Week 13: Play actual background music!
        String musicPath = "assets/music/" + DungeonMap.getMusic().replace(".ogg", ".wav");
        SoundSystem.playBackgroundMusic(musicPath);
    }
}
