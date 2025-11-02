package factories;

import obstacles.Obstacle;

/**
 * ✅ SOLUTION: Factory Method Pattern
 *
 * Week 10 Branch 10-02: Factory-based spawning
 *
 * Abstract factory defines interface for creating obstacles.
 * Concrete factories (SpikeFactory, GoblinFactory, WolfFactory) decide
 * which class to instantiate.
 *
 * Benefits:
 * - WorldController doesn't know concrete obstacle types (loose coupling)
 * - Adding new obstacle = create new factory, modify ZERO files (OCP)
 * - No switch-case (no merge conflicts)
 * - Each developer works on own factory (parallel development)
 */
public abstract class ObstacleFactory {
    /**
     * Factory method - subclasses implement to create specific obstacle type
     * @return Obstacle instance
     */
    public abstract Obstacle createObstacle();

    /**
     * Convenience method - create obstacle with position
     * @param x X coordinate
     * @param y Y coordinate
     * @return Obstacle instance at specified position
     */
    public Obstacle createObstacle(int x, int y) {
        Obstacle obs = createObstacle();
        obs.setPosition(x, y);
        return obs;
    }
}
