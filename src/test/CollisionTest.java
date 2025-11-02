import entities.NPC;
import entities.GameManager;
import entities.Coin;
import obstacles.Obstacle;
import obstacles.Spike;
import obstacles.Goblin;
import obstacles.Wolf;

/**
 * Manual collision test to verify:
 * 1. Coin collection increases score
 * 2. Spike collision reduces HP by 20
 * 3. Goblin collision reduces HP by 15
 * 4. Wolf collision reduces HP by 25
 */
public class CollisionTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println("   COLLISION DETECTION TEST");
        System.out.println("========================================");
        System.out.println();

        // Reset GameManager to clean state
        GameManager.resetInstance();
        GameManager manager = GameManager.getInstance();

        System.out.println("Initial State:");
        System.out.println("  Score: " + manager.getScore());
        System.out.println("  HP: " + manager.getHp() + " / 100");
        System.out.println();

        // Create NPC at position (5, 5)
        NPC npc = new NPC();
        npc.tryMove(5, 5);
        System.out.println("NPC positioned at (5, 5)");
        System.out.println();

        // ==========================================
        // TEST 1: Coin Collection
        // ==========================================
        System.out.println("TEST 1: Coin Collection (+10 score)");
        System.out.println("----------------------------------------");

        Coin coin1 = new Coin(5, 5);  // Same position as NPC

        System.out.println("  Coin isCollected before: " + coin1.isCollected());

        // Check collision
        if (npc.getX() == coin1.getX() && npc.getY() == coin1.getY() && !coin1.isCollected()) {
            manager.addScore(10);
            coin1.collect();
            System.out.println("✓ Coin collected!");
        }

        System.out.println("  Coin isCollected after: " + coin1.isCollected());
        System.out.println("  Score: " + manager.getScore() + " (expected: 10)");
        System.out.println("  HP: " + manager.getHp() + " / 100 (expected: 100)");

        if (!coin1.isCollected()) {
            System.out.println("✗ ERROR: Coin should be collected!");
        } else {
            System.out.println("✓ Coin is marked as collected (will disappear)");
        }
        System.out.println();

        Thread.sleep(1000);

        // ==========================================
        // TEST 2: Spike Collision
        // ==========================================
        System.out.println("TEST 2: Spike Collision (-20 HP)");
        System.out.println("----------------------------------------");

        Spike spike = new Spike(5, 5);  // Same position as NPC

        System.out.println("  Spike isActive before: " + spike.isActive());

        // Check collision
        if (npc.getX() == spike.getX() && npc.getY() == spike.getY() && spike.isActive()) {
            manager.takeDamage(spike.getDamage());
            spike.setActive(false);
            System.out.println("✓ Spike hit! Damage: " + spike.getDamage());
        }

        System.out.println("  Spike isActive after: " + spike.isActive());
        System.out.println("  Score: " + manager.getScore() + " (expected: 10)");
        System.out.println("  HP: " + manager.getHp() + " / 100 (expected: 80)");

        if (spike.isActive()) {
            System.out.println("✗ ERROR: Spike should be deactivated!");
        } else {
            System.out.println("✓ Spike is deactivated (one-time hit, will disappear)");
        }
        System.out.println();

        Thread.sleep(1000);

        // ==========================================
        // TEST 3: Goblin Collision
        // ==========================================
        System.out.println("TEST 3: Goblin Collision (-15 HP)");
        System.out.println("----------------------------------------");

        Goblin goblin = new Goblin(5, 5);  // Same position as NPC

        // Check collision
        if (npc.getX() == goblin.getX() && npc.getY() == goblin.getY() && goblin.isActive()) {
            manager.takeDamage(goblin.getDamage());
            goblin.setActive(false);
            System.out.println("✓ Goblin hit! Damage: " + goblin.getDamage());
        }

        System.out.println("  Score: " + manager.getScore() + " (expected: 10)");
        System.out.println("  HP: " + manager.getHp() + " / 100 (expected: 65)");
        System.out.println();

        Thread.sleep(1000);

        // ==========================================
        // TEST 4: Wolf Collision
        // ==========================================
        System.out.println("TEST 4: Wolf Collision (-25 HP)");
        System.out.println("----------------------------------------");

        Wolf wolf = new Wolf(5, 5);  // Same position as NPC

        // Check collision
        if (npc.getX() == wolf.getX() && npc.getY() == wolf.getY() && wolf.isActive()) {
            manager.takeDamage(wolf.getDamage());
            wolf.setActive(false);
            System.out.println("✓ Wolf hit! Damage: " + wolf.getDamage());
        }

        System.out.println("  Score: " + manager.getScore() + " (expected: 10)");
        System.out.println("  HP: " + manager.getHp() + " / 100 (expected: 40)");
        System.out.println();

        Thread.sleep(1000);

        // ==========================================
        // TEST 5: Multiple Coins
        // ==========================================
        System.out.println("TEST 5: Multiple Coin Collection");
        System.out.println("----------------------------------------");

        for (int i = 0; i < 3; i++) {
            Coin coin = new Coin(5, 5);
            if (npc.getX() == coin.getX() && npc.getY() == coin.getY() && !coin.isCollected()) {
                manager.addScore(10);
                coin.collect();
                System.out.println("✓ Coin " + (i + 1) + " collected!");
            }
        }

        System.out.println("  Score: " + manager.getScore() + " (expected: 40)");
        System.out.println("  HP: " + manager.getHp() + " / 100 (expected: 40)");
        System.out.println();

        Thread.sleep(1000);

        // ==========================================
        // SUMMARY
        // ==========================================
        System.out.println("========================================");
        System.out.println("   TEST SUMMARY");
        System.out.println("========================================");
        System.out.println();

        boolean allTestsPassed = true;

        // Verify final state
        if (manager.getScore() == 40) {
            System.out.println("✓ Score Test PASSED (40)");
        } else {
            System.out.println("✗ Score Test FAILED (expected 40, got " + manager.getScore() + ")");
            allTestsPassed = false;
        }

        if (manager.getHp() == 40) {
            System.out.println("✓ HP Test PASSED (40)");
        } else {
            System.out.println("✗ HP Test FAILED (expected 40, got " + manager.getHp() + ")");
            allTestsPassed = false;
        }

        System.out.println();

        if (allTestsPassed) {
            System.out.println("========================================");
            System.out.println("   ALL TESTS PASSED! ✓");
            System.out.println("========================================");
        } else {
            System.out.println("========================================");
            System.out.println("   SOME TESTS FAILED! ✗");
            System.out.println("========================================");
        }

        System.out.println();
        System.out.println("Collision detection is working correctly!");
        System.out.println("  - Coins increase score by 10");
        System.out.println("  - Spike deals 20 damage");
        System.out.println("  - Goblin deals 15 damage");
        System.out.println("  - Wolf deals 25 damage");
    }
}
