package engine.physics;

/**
 * Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN)
 *
 * Complex physics subsystem that client must interact with directly.
 * In a real game engine, this would have:
 * - PhysicsWorld (simulation space)
 * - CollisionDetector (collision detection)
 * - RigidBodyManager (physics objects)
 *
 * Client must know the CORRECT ORDER to initialize all these!
 */
public class PhysicsEngine {
    private boolean worldCreated = false;
    private boolean detectorReady = false;
    private float gravity = 0;

    /**
     * Step 1: Create physics world
     * MUST be called first!
     */
    public void createWorld(float gravityX, float gravityY) {
        System.out.println("[PhysicsWorld] Creating physics world...");
        System.out.println("  → Gravity: (" + gravityX + ", " + gravityY + ")");
        this.gravity = gravityY;
        worldCreated = true;
        System.out.println("  ✓ World created");
    }

    /**
     * Step 2: Set world bounds
     * REQUIRES: createWorld() first!
     */
    public void setBounds(int x, int y, int width, int height) {
        if (!worldCreated) {
            System.out.println("[PhysicsWorld] ✗ ERROR: World not created!");
            System.out.println("  → Client forgot to call createWorld() first!");
            return;
        }
        System.out.println("[PhysicsWorld] Setting bounds...");
        System.out.println("  → Bounds: (" + x + "," + y + ") to (" + (x+width) + "," + (y+height) + ")");
        System.out.println("  ✓ Bounds set");
    }

    /**
     * Step 3: Initialize collision detector
     * REQUIRES: createWorld() first!
     */
    public void initCollisionDetector(int layers) {
        if (!worldCreated) {
            System.out.println("[CollisionDetector] ✗ ERROR: World not created!");
            System.out.println("  → Client forgot to call createWorld() first!");
            return;
        }
        System.out.println("[CollisionDetector] Initializing...");
        System.out.println("  → Collision layers: " + layers);
        detectorReady = true;
        System.out.println("  ✓ Collision detector ready");
    }

    /**
     * Simulate one physics step
     * REQUIRES: World and detector ready!
     */
    public void step(float deltaTime) {
        if (!worldCreated) {
            System.out.println("[PhysicsEngine] ✗ ERROR: Cannot step - world not created!");
            return;
        }
        System.out.println("[PhysicsEngine] Physics step: " + deltaTime + "s (gravity: " + gravity + ")");
    }

    /**
     * Check for collisions
     * REQUIRES: initCollisionDetector() first!
     */
    public void checkCollisions() {
        if (!detectorReady) {
            System.out.println("[CollisionDetector] ✗ ERROR: Detector not initialized!");
            return;
        }
        System.out.println("[CollisionDetector] Checking collisions...");
    }

    /**
     * Cleanup - client must call this!
     */
    public void cleanup() {
        System.out.println("[PhysicsEngine] Cleaning up...");
        if (detectorReady) {
            System.out.println("  → Destroying collision detector");
            detectorReady = false;
        }
        if (worldCreated) {
            System.out.println("  → Destroying physics world");
            worldCreated = false;
        }
        System.out.println("  ✓ Physics engine cleaned up");
    }
}
