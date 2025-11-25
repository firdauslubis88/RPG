package level;

/**
 * Week 13-01: Hardcoded Level Loading (ANTI-PATTERN)
 *
 * PROBLEM: Same algorithm structure as DungeonLevelLoader - DUPLICATED!
 *
 * Notice how the steps are exactly the same:
 * 1. Load assets
 * 2. Build world
 * 3. Spawn enemies
 * 4. Play music
 *
 * This is CODE DUPLICATION - the algorithm structure is repeated!
 */
public class ForestLevelLoader {

    public void loadLevel() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     LOADING: ENCHANTED FOREST          ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Step 1: Load assets (SAME STRUCTURE AS DUNGEON!)
        System.out.println("[Step 1] Loading assets...");
        System.out.println("  → Loading tree and leaf textures");
        System.out.println("  → Loading grass and flower sprites");
        System.out.println("  → Loading wolf animations");
        System.out.println("  → Loading bandit animations");
        System.out.println("  ✓ Assets loaded");

        // Step 2: Build world (SAME STRUCTURE!)
        System.out.println("\n[Step 2] Building world...");
        System.out.println("  → Generating forest terrain");
        System.out.println("  → Planting trees and bushes (150 trees)");
        System.out.println("  → Creating winding paths");
        System.out.println("  → Adding hidden clearings (3 found)");
        System.out.println("  ✓ World built");

        // Step 3: Spawn enemies (SAME STRUCTURE!)
        System.out.println("\n[Step 3] Spawning enemies...");
        System.out.println("  → Spawning wolf pack (4 wolves)");
        System.out.println("  → Placing bandits near paths (6 bandits)");
        System.out.println("  → Hiding Forest Spirit in clearing");
        System.out.println("  ✓ Enemies spawned (Total: 11)");

        // Step 4: Play background music (SAME STRUCTURE!)
        System.out.println("\n[Step 4] Starting background music...");
        System.out.println("  ♪ Now playing: forest_birds_ambience.mp3");
        System.out.println("  ✓ Music started");

        System.out.println("\n════════════════════════════════════════");
        System.out.println("✓ Enchanted Forest loaded successfully!");
        System.out.println("════════════════════════════════════════\n");
    }
}
