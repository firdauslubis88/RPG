import level.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Week 13-02: Template Method Pattern (SOLUTION) - Demo
 *
 * This demo shows the SOLUTION using Template Method Pattern:
 * 1. NO code duplication - algorithm defined ONCE in LevelLoader
 * 2. CONSISTENT order - template method guarantees step sequence
 * 3. NO missing steps - abstract methods FORCE implementation
 * 4. POLYMORPHISM enabled - can use LevelLoader as base type
 * 5. EASY to extend - just create new subclass
 *
 * Compare to branch 13-01-hardcoded-level to see the problems solved!
 */
public class LevelDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║                                                      ║");
        System.out.println("║   WEEK 13-02: TEMPLATE METHOD PATTERN                ║");
        System.out.println("║   (SOLUTION DEMONSTRATION)                           ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        System.out.println("\nTemplate Method Pattern SOLVES all previous problems:\n");
        System.out.println("  ✓ NO code duplication - algorithm in LevelLoader");
        System.out.println("  ✓ CONSISTENT order - guaranteed by template method");
        System.out.println("  ✓ NO missing steps - abstract methods enforce it");
        System.out.println("  ✓ POLYMORPHISM - use LevelLoader as base type");
        System.out.println("  ✓ EASY to extend - just create new subclass");

        // ════════════════════════════════════════════════════════════
        // Demonstrate POLYMORPHISM - using base type!
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" BENEFIT: Polymorphism with LevelLoader base type");
        System.out.println("═".repeat(60));

        // Create a registry of level loaders - using base type!
        Map<String, LevelLoader> levelRegistry = new HashMap<>();
        levelRegistry.put("dungeon", new DungeonLevelLoader());
        levelRegistry.put("forest", new ForestLevelLoader());
        levelRegistry.put("castle", new CastleLevelLoader());
        levelRegistry.put("boss", new BossArenaLoader());

        System.out.println("\nLevel Registry created with 4 levels.");
        System.out.println("All stored as LevelLoader base type - POLYMORPHISM!\n");

        // Load levels by name using polymorphism
        String[] levelsToLoad = {"dungeon", "forest", "boss"};

        for (String levelName : levelsToLoad) {
            System.out.println("═".repeat(60));
            System.out.println(" Loading: \"" + levelName + "\" via polymorphism");
            System.out.println("═".repeat(60));

            LevelLoader loader = levelRegistry.get(levelName);
            if (loader != null) {
                loader.loadLevel();  // Template method handles everything!
            }
        }

        // ════════════════════════════════════════════════════════════
        // Show code comparison
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" CODE COMPARISON: Before vs After");
        System.out.println("═".repeat(60));

        System.out.println("\n┌─────────────────────────────────────────────────────┐");
        System.out.println("│ BEFORE (13-01): Hardcoded Level Loading             │");
        System.out.println("├─────────────────────────────────────────────────────┤");
        System.out.println("│ • Each loader: ~50 lines with full algorithm        │");
        System.out.println("│ • 4 loaders × 50 lines = 200 lines (duplicated!)    │");
        System.out.println("│ • BossArena had wrong order and missing music       │");
        System.out.println("│ • No common base type                               │");
        System.out.println("└─────────────────────────────────────────────────────┘");

        System.out.println("\n┌─────────────────────────────────────────────────────┐");
        System.out.println("│ AFTER (13-02): Template Method Pattern              │");
        System.out.println("├─────────────────────────────────────────────────────┤");
        System.out.println("│ • LevelLoader: ~60 lines (algorithm defined ONCE)   │");
        System.out.println("│ • Each concrete loader: ~30 lines (only specifics)  │");
        System.out.println("│ • Total: 60 + (4 × 30) = 180 lines (less code!)     │");
        System.out.println("│ • Correct order GUARANTEED                          │");
        System.out.println("│ • Polymorphism ENABLED                              │");
        System.out.println("│ • Hook methods for customization                    │");
        System.out.println("└─────────────────────────────────────────────────────┘");

        // ════════════════════════════════════════════════════════════
        // Hollywood Principle
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" Hollywood Principle: \"Don't call us, we'll call you!\"");
        System.out.println("═".repeat(60));

        System.out.println("\n  LevelLoader (parent) CALLS subclass methods:");
        System.out.println("  ┌───────────────────────────────────┐");
        System.out.println("  │  loadLevel() {                    │");
        System.out.println("  │      loadAssets();    ←── calls   │");
        System.out.println("  │      buildWorld();    ←── calls   │");
        System.out.println("  │      spawnEnemies();  ←── calls   │");
        System.out.println("  │      if (shouldPlayMusic())       │");
        System.out.println("  │          playBackgroundMusic();   │");
        System.out.println("  │  }                                │");
        System.out.println("  └───────────────────────────────────┘");
        System.out.println("\n  Subclasses DON'T call parent - they WAIT to be called!");

        // ════════════════════════════════════════════════════════════
        // Summary
        // ════════════════════════════════════════════════════════════
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              PROBLEMS SOLVED                         ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  ✓ Code Reuse                                        ║");
        System.out.println("║    Algorithm defined ONCE in abstract class          ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✓ Consistent Order                                  ║");
        System.out.println("║    Template method GUARANTEES step sequence          ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✓ No Missing Steps                                  ║");
        System.out.println("║    Abstract methods FORCE implementation             ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✓ Polymorphism                                      ║");
        System.out.println("║    Can use LevelLoader as common base type           ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✓ Easy Extension                                    ║");
        System.out.println("║    Add new level = create new subclass               ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✓ Hook Methods                                      ║");
        System.out.println("║    Optional customization (e.g., disable music)      ║");
        System.out.println("║                                                      ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  Compare: branch 13-01-hardcoded-level (PROBLEM)     ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
}
