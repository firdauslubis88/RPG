package level;

/**
 * Week 13-01: Hardcoded Level Loading (ANTI-PATTERN)
 *
 * PROBLEM: Entire algorithm is copy-pasted in each loader.
 * This class has the same structure as ForestLevelLoader and CastleLevelLoader,
 * causing massive code duplication.
 *
 * Issues:
 * - Code duplication across all loaders
 * - No guarantee of consistent algorithm order
 * - Hard to add new steps (must edit ALL loaders)
 * - No polymorphism possible
 */
public class DungeonLevelLoader {

    public void loadLevel() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       LOADING: DARK DUNGEON            ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Step 1: Load assets
        System.out.println("[Step 1] Loading assets...");
        System.out.println("  → Loading stone wall textures");
        System.out.println("  → Loading torch flame sprites");
        System.out.println("  → Loading skeleton animations");
        System.out.println("  → Loading goblin animations");
        System.out.println("  ✓ Assets loaded");

        // Step 2: Build world
        System.out.println("\n[Step 2] Building world...");
        System.out.println("  → Generating dungeon layout (10x10 rooms)");
        System.out.println("  → Placing wall tiles");
        System.out.println("  → Adding torch lighting effects");
        System.out.println("  → Placing treasure chests (5 found)");
        System.out.println("  ✓ World built");

        // Step 3: Spawn enemies
        System.out.println("\n[Step 3] Spawning enemies...");
        System.out.println("  → Spawning 5 Skeletons in corridors");
        System.out.println("  → Spawning 3 Goblins near treasure");
        System.out.println("  → Placing Dungeon Boss in final room");
        System.out.println("  ✓ Enemies spawned (Total: 9)");

        // Step 4: Play background music
        System.out.println("\n[Step 4] Starting background music...");
        System.out.println("  ♪ Now playing: eerie_dungeon_ambience.mp3");
        System.out.println("  ✓ Music started");

        System.out.println("\n════════════════════════════════════════");
        System.out.println("✓ Dark Dungeon loaded successfully!");
        System.out.println("════════════════════════════════════════\n");
    }
}
