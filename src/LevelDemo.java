import level.*;

/**
 * Week 13-01: Hardcoded Level Loading (ANTI-PATTERN) - Demo
 *
 * This demo shows the problems with hardcoded level loading:
 * 1. Code duplication across loaders (same algorithm, copy-pasted)
 * 2. Inconsistent algorithm order (BossArena has wrong order)
 * 3. Missing steps (BossArena forgot music)
 * 4. No polymorphism (can't use common base type)
 * 5. Hard to modify algorithm (must edit ALL loaders)
 *
 * SOLUTION: Template Method Pattern (see branch 13-02-template-method)
 */
public class LevelDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║                                                      ║");
        System.out.println("║   WEEK 13-01: HARDCODED LEVEL LOADING                ║");
        System.out.println("║   (ANTI-PATTERN DEMONSTRATION)                       ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        System.out.println("\nThis demo shows PROBLEMS with hardcoded level loading:\n");
        System.out.println("  1. Code duplication - same algorithm in every loader");
        System.out.println("  2. Inconsistent order - BossArena has bugs!");
        System.out.println("  3. Missing steps - BossArena forgot music!");
        System.out.println("  4. No polymorphism - can't use common type");
        System.out.println("  5. Hard to modify - must edit ALL loaders");

        // ════════════════════════════════════════════════════════════
        // Load Dungeon (correct implementation)
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" LOADING LEVEL 1: Dark Dungeon (Correct Implementation)");
        System.out.println("═".repeat(60));

        DungeonLevelLoader dungeonLoader = new DungeonLevelLoader();
        dungeonLoader.loadLevel();

        // ════════════════════════════════════════════════════════════
        // Load Forest (correct, but duplicated code!)
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" LOADING LEVEL 2: Enchanted Forest (Duplicated Code!)");
        System.out.println("═".repeat(60));

        ForestLevelLoader forestLoader = new ForestLevelLoader();
        forestLoader.loadLevel();

        // ════════════════════════════════════════════════════════════
        // Load Castle (correct, but MORE duplicated code!)
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" LOADING LEVEL 3: Royal Castle (More Duplicated Code!)");
        System.out.println("═".repeat(60));

        CastleLevelLoader castleLoader = new CastleLevelLoader();
        castleLoader.loadLevel();

        // ════════════════════════════════════════════════════════════
        // Load Boss Arena (BUGGY implementation!)
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" LOADING LEVEL 4: Boss Arena (⚠️  BUGGY IMPLEMENTATION!)");
        System.out.println("═".repeat(60));

        BossArenaLoader bossLoader = new BossArenaLoader();
        bossLoader.loadLevel();

        // ════════════════════════════════════════════════════════════
        // Show the polymorphism problem
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" PROBLEM: No Polymorphism!");
        System.out.println("═".repeat(60));

        System.out.println("\nWithout a common base class, we can't do this:\n");
        System.out.println("  // ✗ This doesn't work!");
        System.out.println("  // LevelLoader loader = getLevelByName(\"dungeon\");");
        System.out.println("  // loader.loadLevel();");
        System.out.println("\nInstead we need ugly if-else chains:\n");
        System.out.println("  if (name.equals(\"dungeon\")) {");
        System.out.println("      new DungeonLevelLoader().loadLevel();");
        System.out.println("  } else if (name.equals(\"forest\")) {");
        System.out.println("      new ForestLevelLoader().loadLevel();");
        System.out.println("  } else if (name.equals(\"castle\")) {");
        System.out.println("      new CastleLevelLoader().loadLevel();");
        System.out.println("  } else if (name.equals(\"boss\")) {");
        System.out.println("      new BossArenaLoader().loadLevel();");
        System.out.println("  }");
        System.out.println("\n  // Adding new level = adding more if-else!");

        // Demonstrate the if-else chain
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" Demo: Loading by name (ugly if-else)");
        System.out.println("═".repeat(60));

        String[] levelNames = {"dungeon", "forest"};
        for (String name : levelNames) {
            System.out.println("\nLoading level by name: \"" + name + "\"");
            loadLevelByName(name);
        }

        // ════════════════════════════════════════════════════════════
        // Summary
        // ════════════════════════════════════════════════════════════
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                  PROBLEMS IDENTIFIED                 ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Code Duplication                                  ║");
        System.out.println("║    Same 4-step algorithm copy-pasted 4 times!        ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Inconsistent Order (BossArena)                    ║");
        System.out.println("║    Spawned enemies before loading textures!          ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Missing Steps (BossArena)                         ║");
        System.out.println("║    Developer forgot to add background music!         ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ No Polymorphism                                   ║");
        System.out.println("║    Can't use common base type for all loaders!       ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Hard to Modify                                    ║");
        System.out.println("║    Adding Step 5 requires editing ALL 4 loaders!     ║");
        System.out.println("║                                                      ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  SOLUTION: Template Method Pattern                   ║");
        System.out.println("║  See branch: 13-02-template-method                   ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    /**
     * Ugly if-else chain required without polymorphism
     */
    private static void loadLevelByName(String name) {
        // This is what we're forced to do without a common base class!
        if (name.equals("dungeon")) {
            System.out.println("  → Using DungeonLevelLoader");
            // Not actually loading to keep output clean
        } else if (name.equals("forest")) {
            System.out.println("  → Using ForestLevelLoader");
        } else if (name.equals("castle")) {
            System.out.println("  → Using CastleLevelLoader");
        } else if (name.equals("boss")) {
            System.out.println("  → Using BossArenaLoader");
        } else {
            System.out.println("  ✗ Unknown level: " + name);
        }
        // Every new level type = another else-if!
    }
}
