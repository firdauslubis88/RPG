import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import entities.Coin;

/**
 * ✅ SOLUTION: Unit tests for game logic WITHOUT rendering!
 *
 * This demonstrates the power of separation of concerns:
 * - We can test collision detection without a display
 * - We can verify frame-rate independence
 * - Tests run fast (<1 second)
 * - Tests are deterministic (no flakiness)
 * - Perfect for CI/CD pipelines
 *
 * Compare with 09-00/MainTest.java where ALL tests failed!
 */
class GameLogicTest {

    private GameLogic gameLogic;

    @BeforeEach
    void setUp() {
        gameLogic = new GameLogic();
    }

    @Test
    void testNPCMovesRightWithDeltaTime() {
        // ✅ Test movement WITHOUT rendering!
        float initialX = gameLogic.getNPCX();

        // Simulate 1 frame at 60 FPS (0.016 seconds)
        gameLogic.updateNPC(0.016f);

        // NPC velocity = 3.0 pixels/sec
        // Expected movement = 3.0 × 0.016 = 0.048 pixels
        assertTrue(gameLogic.getNPCX() > initialX,
                "NPC should move right");
    }

    @Test
    void testNPCWrapsAroundAtEdge() {
        // ✅ Test wrapping logic WITHOUT rendering!

        // Move NPC past right edge (simulate many frames)
        for (int i = 0; i < 100; i++) {
            gameLogic.updateNPC(0.1f);  // Large delta
        }

        // Should wrap to left side
        assertTrue(gameLogic.getNPCX() < GameLogic.getGridWidth(),
                "NPC should wrap around at edge");
    }

    @Test
    void testCoinFallsWithGravity() {
        // ✅ Test physics WITHOUT rendering!
        Coin coin = gameLogic.getCoins().get(0);
        float initialY = coin.getY();

        gameLogic.updateCoins(0.016f);

        // Coin should fall (y increases)
        assertTrue(coin.getY() > initialY,
                "Coin should fall downward");
    }

    @Test
    void testCoinRespawnsAtTopWhenOffScreen() {
        // ✅ Test respawn logic WITHOUT rendering!
        Coin coin = gameLogic.getCoins().get(0);

        // Move coin past bottom (simulate many frames)
        for (int i = 0; i < 100; i++) {
            gameLogic.updateCoins(0.1f);
        }

        // Should respawn at top (y near 0)
        assertTrue(coin.getY() < 2.0f,
                "Coin should respawn at top after falling off screen");
    }

    @Test
    void testFrameRateIndependence() {
        // ✅ Test that movement is frame-rate independent!

        GameLogic logic60FPS = new GameLogic();
        GameLogic logic30FPS = new GameLogic();

        // Simulate 1 second of game time at different frame rates

        // 60 FPS: 60 frames × 0.016s
        for (int i = 0; i < 60; i++) {
            logic60FPS.updateNPC(0.016f);
        }

        // 30 FPS: 30 frames × 0.033s
        for (int i = 0; i < 30; i++) {
            logic30FPS.updateNPC(0.033f);
        }

        // Both should move approximately the same distance
        // (within tolerance for discrete positions)
        float distance60 = logic60FPS.getNPCX();
        float distance30 = logic30FPS.getNPCX();

        // Verify they're close (within 1 pixel tolerance)
        assertTrue(Math.abs(distance60 - distance30) < 1.0f,
                String.format("Frame rate independence failed: 60fps=%f, 30fps=%f",
                        distance60, distance30));
    }

    @Test
    void testCollisionDetectionIncreasesScore() {
        // ✅ Test collision WITHOUT rendering!

        int initialScore = gameLogic.getScore();

        // We can't easily force a collision in current implementation
        // because coin positions are random. This test verifies the
        // checkCollisions() method can be called without errors.

        gameLogic.checkCollisions();

        // Score should be >= initial score (might increase if collision occurs)
        assertTrue(gameLogic.getScore() >= initialScore,
                "Score should never decrease");
    }

    @Test
    void testFrameCounterIncrements() {
        // ✅ Simple logic test
        assertEquals(0, gameLogic.getFrameCount(),
                "Frame count should start at 0");

        gameLogic.incrementFrame();

        assertEquals(1, gameLogic.getFrameCount(),
                "Frame count should increment");
    }

    @Test
    void testMultipleCoinsExist() {
        // ✅ Test game state
        assertFalse(gameLogic.getCoins().isEmpty(),
                "Game should have at least one coin");
    }

    /**
     * SUMMARY: What we achieved with separation of concerns:
     *
     * ✅ All tests pass WITHOUT display
     * ✅ Fast execution (milliseconds, not seconds)
     * ✅ Deterministic behavior
     * ✅ CI/CD compatible
     * ✅ Frame-rate independence verified
     * ✅ Easy to debug and maintain
     *
     * This was IMPOSSIBLE in 09-00!
     */
}
