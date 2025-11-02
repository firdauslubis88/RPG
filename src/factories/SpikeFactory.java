package factories;

import obstacles.Obstacle;
import obstacles.Spike;

/**
 * ✅ SOLUTION: Concrete factory for Spike obstacles
 *
 * Week 10 Branch 10-02: Factory Method implementation
 *
 * This factory creates Spike instances.
 * WorldController doesn't need to know about Spike class!
 */
public class SpikeFactory extends ObstacleFactory {
    @Override
    public Obstacle createObstacle() {
        return new Spike(0, 0);  // Default position, will be set by createObstacle(x, y)
    }
}
