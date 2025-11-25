package level;

/**
 * Week 13-01: Hardcoded Level Loading (ANTI-PATTERN)
 *
 * PROBLEM: Yet another copy of the same algorithm!
 *
 * At this point we have 3 classes with identical structure.
 * If we need to add a new step (e.g., "validate level"),
 * we must edit ALL THREE classes!
 */
public class CastleLevelLoader {

    public void loadLevel() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       LOADING: ROYAL CASTLE            ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Step 1: Load assets (COPY-PASTED STRUCTURE!)
        System.out.println("[Step 1] Loading assets...");
        System.out.println("  → Loading stone brick textures");
        System.out.println("  → Loading banner and flag sprites");
        System.out.println("  → Loading knight animations");
        System.out.println("  → Loading royal guard animations");
        System.out.println("  ✓ Assets loaded");

        // Step 2: Build world (COPY-PASTED STRUCTURE!)
        System.out.println("\n[Step 2] Building world...");
        System.out.println("  → Constructing castle walls");
        System.out.println("  → Building throne room");
        System.out.println("  → Adding towers and battlements");
        System.out.println("  → Placing royal decorations");
        System.out.println("  ✓ World built");

        // Step 3: Spawn enemies (COPY-PASTED STRUCTURE!)
        System.out.println("\n[Step 3] Spawning enemies...");
        System.out.println("  → Spawning castle guards (8 guards)");
        System.out.println("  → Placing elite knights (4 knights)");
        System.out.println("  → Summoning the King Boss in throne room");
        System.out.println("  ✓ Enemies spawned (Total: 13)");

        // Step 4: Play background music (COPY-PASTED STRUCTURE!)
        System.out.println("\n[Step 4] Starting background music...");
        System.out.println("  ♪ Now playing: royal_fanfare.mp3");
        System.out.println("  ✓ Music started");

        System.out.println("\n════════════════════════════════════════");
        System.out.println("✓ Royal Castle loaded successfully!");
        System.out.println("════════════════════════════════════════\n");
    }
}
