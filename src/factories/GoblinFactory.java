package factories;

import obstacles.Obstacle;
import obstacles.Goblin;

/**
 * ✅ SOLUTION: Concrete factory for Goblin obstacles
 *
 * Week 10 Branch 10-02: Factory Method implementation
 *
 * This factory creates Goblin instances.
 * WorldController doesn't need to know about Goblin class!
 */
public class GoblinFactory extends ObstacleFactory {
    @Override
    public Obstacle createObstacle() {
        return new Goblin(0, 0);  // Default position, will be set by createObstacle(x, y)
    }
}
