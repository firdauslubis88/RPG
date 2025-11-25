package level;

/**
 * Week 13-02: Template Method Pattern (SOLUTION)
 *
 * Concrete implementation for Forest level.
 * Only implements the SPECIFIC details, algorithm is in LevelLoader.
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
        System.out.println("  → Generating forest terrain");
        System.out.println("  → Planting trees and bushes (150 trees)");
        System.out.println("  → Creating winding paths");
        System.out.println("  → Adding hidden clearings (3 found)");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("  → Spawning wolf pack (4 wolves)");
        System.out.println("  → Placing bandits near paths (6 bandits)");
        System.out.println("  → Hiding Forest Spirit in clearing");
        System.out.println("  → Total enemies: 11");
    }

    @Override
    protected void playBackgroundMusic() {
        System.out.println("  ♪ Now playing: forest_birds_ambience.mp3");
    }
}
