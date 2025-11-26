package level;

import world.DungeonMap;
import world.ForestMapLayout;
import systems.SoundSystem;

/**
 * Week 13-02: Template Method Pattern (SOLUTION)
 *
 * Concrete implementation for Forest level.
 * Week 13: Now sets active map with ForestMapLayout!
 */
public class ForestLevelLoader extends LevelLoader {

    @Override
    protected String getLevelName() {
        return "ENCHANTED FOREST";
    }

    @Override
    protected void loadAssets() {
        System.out.println("  → Loading tree and leaf textures");
        System.out.println("  → Loading grass and flower sprites");
        System.out.println("  → Loading wolf animations");
        System.out.println("  → Loading bandit animations");
    }

    @Override
    protected void buildWorld() {
        // Week 13: Set the active map layout!
        DungeonMap.setActiveMap(new ForestMapLayout());
        System.out.println("  → Generating forest terrain (25x25 grid)");
        System.out.println("  → Wall char: 'T' (trees)");
        System.out.println("  → Floor char: ',' (grass)");
        System.out.println("  → Adding hidden clearings");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("  → Goblin char: 'S' (Forest Spirit)");
        System.out.println("  → Wolf char: 'W' (Wild Wolf)");
        System.out.println("  → Spike char: '*' (Thorny bushes)");
        System.out.println("  → Hiding Forest Boss in clearing");
    }

    @Override
    protected void playBackgroundMusic() {
        // Week 13: Play actual background music!
        String musicPath = "assets/music/" + DungeonMap.getMusic().replace(".ogg", ".wav");
        SoundSystem.playBackgroundMusic(musicPath);
    }
}
