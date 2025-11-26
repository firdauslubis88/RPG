package battle.subsystems;

/**
 * Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN)
 *
 * Complex animation subsystem for battle sequences.
 * Client must initialize in CORRECT ORDER:
 * 1. init() - Initialize animation system
 * 2. loadBattleSprites() - Load sprite assets
 * 3. Then can play animations
 *
 * Problem: Client must know these internals!
 */
public class BattleAnimationSystem {
    private boolean initialized = false;
    private boolean spritesLoaded = false;

    /**
     * Step 1: Initialize animation system
     * MUST be called first!
     */
    public void init() {
        System.out.println("[AnimationSystem] Initializing...");
        System.out.println("  → Setting up sprite renderer");
        System.out.println("  → Configuring animation timings");
        initialized = true;
        System.out.println("  ✓ Animation system ready");
    }

    /**
     * Step 2: Load battle sprites
     * REQUIRES: init() first!
     */
    public void loadBattleSprites() {
        if (!initialized) {
            System.out.println("[AnimationSystem] ✗ ERROR: Not initialized!");
            System.out.println("  → Client forgot to call init() first!");
            return;
        }
        System.out.println("[AnimationSystem] Loading battle sprites...");
        System.out.println("  → Loading player sprites");
        System.out.println("  → Loading boss sprites");
        System.out.println("  → Loading effect sprites");
        spritesLoaded = true;
        System.out.println("  ✓ Sprites loaded");
    }

    /**
     * Play attack animation
     * REQUIRES: loadBattleSprites() first!
     */
    public void playAttackAnimation(String attacker) {
        if (!spritesLoaded) {
            System.out.println("[AnimationSystem] ✗ ERROR: Sprites not loaded!");
            return;
        }
        System.out.println("[AnimationSystem] ⚔ " + attacker + " attack animation");
    }

    /**
     * Play defend animation
     */
    public void playDefendAnimation(String defender) {
        if (!spritesLoaded) {
            System.out.println("[AnimationSystem] ✗ ERROR: Sprites not loaded!");
            return;
        }
        System.out.println("[AnimationSystem] 🛡 " + defender + " defend animation");
    }

    /**
     * Play magic animation
     */
    public void playMagicAnimation(String caster) {
        if (!spritesLoaded) {
            System.out.println("[AnimationSystem] ✗ ERROR: Sprites not loaded!");
            return;
        }
        System.out.println("[AnimationSystem] ✨ " + caster + " magic animation");
    }

    /**
     * Play damage animation
     */
    public void playDamageAnimation(String target, int damage) {
        if (!spritesLoaded) {
            System.out.println("[AnimationSystem] ✗ ERROR: Sprites not loaded!");
            return;
        }
        System.out.println("[AnimationSystem] 💥 " + target + " takes " + damage + " damage!");
    }

    /**
     * Cleanup - client must call this!
     */
    public void cleanup() {
        System.out.println("[AnimationSystem] Cleaning up...");
        if (spritesLoaded) {
            System.out.println("  → Unloading sprites");
            spritesLoaded = false;
        }
        if (initialized) {
            System.out.println("  → Shutting down renderer");
            initialized = false;
        }
        System.out.println("  ✓ Animation system cleaned up");
    }
}
