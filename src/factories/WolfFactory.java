package factories;

import obstacles.Obstacle;
import obstacles.Wolf;

/**
 * ✅ SOLUTION: Concrete factory for Wolf obstacles
 *
 * Week 10 Branch 10-02: Factory Method implementation
 *
 * This factory creates Wolf instances.
 * WorldController doesn't need to know about Wolf class!
 */
public class WolfFactory extends ObstacleFactory {
    @Override
    public Obstacle createObstacle() {
        return new Wolf(0, 0);  // Default position, will be set by createObstacle(x, y)
    }
}
