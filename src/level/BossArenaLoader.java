package level;

/**
 * Week 13-01: Hardcoded Level Loading (ANTI-PATTERN)
 *
 * PROBLEM: INCONSISTENT algorithm order and MISSING steps!
 *
 * This class demonstrates what happens when there's no enforced structure:
 * - Steps are in WRONG ORDER (spawning before loading assets!)
 * - Step 4 (music) is MISSING entirely!
 *
 * Without a template method enforcing the algorithm, developers can
 * accidentally create inconsistent or buggy implementations.
 */
public class BossArenaLoader {

    public void loadLevel() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       LOADING: BOSS ARENA              ║");
        System.out.println("║       ⚠️  WARNING: BUGGY LOADER!        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // BUG: WRONG ORDER! Spawning enemies BEFORE loading assets!
        System.out.println("[Step ???] Spawning boss FIRST (BUG: Wrong order!)...");
        System.out.println("  → Attempting to spawn Ancient Dragon...");
        System.out.println("  ⚠️  WARNING: Dragon texture not loaded yet!");
        System.out.println("  ⚠️  WARNING: Dragon appears as pink square!");
        System.out.println("  ✗ Enemies spawned with missing textures");

        // Assets loaded TOO LATE
        System.out.println("\n[Step ???] Loading assets (TOO LATE!)...");
        System.out.println("  → Loading dragon sprite (should have been first!)");
        System.out.println("  → Loading arena textures");
        System.out.println("  → Loading fire effect animations");
        System.out.println("  ⚠️  Assets loaded but enemies already spawned wrong");

        // World building
        System.out.println("\n[Step ???] Building arena...");
        System.out.println("  → Creating circular battle arena");
        System.out.println("  → Adding pillars for cover");
        System.out.println("  → Setting up dramatic lighting");
        System.out.println("  ✓ Arena built");

        // BUG: FORGOT to play music!
        // (Developer forgot to add Step 4!)
        System.out.println("\n[Step 4] MISSING! (Developer forgot to add music!)");
        System.out.println("  ⚠️  No background music playing...");
        System.out.println("  ⚠️  Boss fight feels awkwardly silent!");

        System.out.println("\n════════════════════════════════════════");
        System.out.println("⚠️  Boss Arena loaded with ISSUES:");
        System.out.println("   - Wrong step order caused texture bugs");
        System.out.println("   - Missing background music");
        System.out.println("════════════════════════════════════════\n");
    }
}
