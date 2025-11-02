import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ❌ DEMONSTRATION OF UNTESTABILITY
 *
 * This test class shows why the monolithic Main.java is impossible to test.
 * All tests fail because we cannot separate game logic from rendering.
 */
public class MainTest {

    @Test
    void testCollisionDetection() {
        // ❌ PROBLEM: Cannot extract collision logic
        // The collision check is buried inside main() method
        // We cannot call it separately to test

        // ❌ PROBLEM: Cannot mock the terminal
        // Testing requires actual System.out which makes tests flaky

        // ❌ PROBLEM: Cannot control timing
        // Thread.sleep() makes tests slow and unreliable

        fail("Impossible to test! Collision logic is inside main() method " +
             "and mixed with rendering. We need separation of concerns!");
    }

    @Test
    void testNPCMovement() {
        // ❌ PROBLEM: Cannot test movement speed
        // Movement is coupled with rendering delays

        // We could test the NPC class itself, but that doesn't test
        // the actual game logic which includes wrapping, timing, etc.

        fail("Cannot test actual game movement! It's coupled with " +
             "Thread.sleep() and rendering. Speed varies based on render time!");
    }

    @Test
    void testScoring() {
        // ❌ PROBLEM: Cannot verify score increases on collision
        // Score is a local variable in main()
        // No getter, no way to access

        fail("Cannot access score! It's a local variable in main(). " +
             "No encapsulation, no testability!");
    }

    @Test
    void testCoinRespawn() {
        // ❌ PROBLEM: Cannot test respawn logic in isolation
        // Respawn happens inside main() loop
        // Cannot trigger it separately

        fail("Cannot test respawn logic! It's mixed with collision detection " +
             "and rendering in the main loop!");
    }

    @Test
    void testFrameRate() {
        // ❌ PROBLEM: Cannot measure actual frame rate
        // Multiple Thread.sleep() calls scattered throughout
        // No delta time calculation
        // Cannot run without rendering

        fail("Cannot measure frame rate! Update timing is coupled with " +
             "rendering delays. No delta time, no way to verify!");
    }

    @Test
    void testGameStateAfter10Frames() {
        // ❌ PROBLEM: Cannot run game for specific number of frames
        // while (true) loop runs forever
        // No way to step through frames
        // No way to capture state at specific frame

        fail("Cannot control frame progression! Infinite loop, no pause, " +
             "no way to check state at specific frames!");
    }

    /**
     * SUMMARY OF PROBLEMS:
     *
     * 1. ALL LOGIC IN MAIN() - Cannot extract methods to test
     * 2. LOCAL VARIABLES - Cannot access game state
     * 3. INFINITE LOOP - Cannot control execution
     * 4. RENDERING COUPLED - Cannot test without terminal
     * 5. THREAD.SLEEP - Tests would be slow and flaky
     * 6. NO ENCAPSULATION - Everything is private to main()
     * 7. NO DELTA TIME - Cannot verify timing logic
     * 8. SIDE EFFECTS - System.out makes tests non-deterministic
     *
     * NEXT: See 09-01-with-game-loop where we separate
     * update() and draw() to make everything testable!
     */
}
