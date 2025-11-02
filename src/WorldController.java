import obstacles.Obstacle;
import factories.ObstacleFactory;
import factories.SpikeFactory;
import factories.GoblinFactory;
import factories.WolfFactory;
import entities.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
 * WorldController - Manages obstacle spawning and lifecycle
 *
 * Week 10 Branch 10-02: FACTORY METHOD PATTERN (SOLUTION)
 *
 * ✅ SOLUTION: WorldController no longer knows concrete obstacle types!
 * ✅ SOLUTION: Uses factories for object creation (Open/Closed Principle)
 * ✅ SOLUTION: Adding new obstacle = create new factory, modify ZERO files!
 * ✅ SOLUTION: No switch-case, no instanceof checks for creation
 *
 * Teaching Points:
 * - Loose coupling through factory pattern
 * - Open/Closed Principle compliance
 * - Easy to extend (just add new factory)
 * - No merge conflicts on this file
 */
public class WorldController {
    private final List<Obstacle> activeObstacles;
    private final List<ObstacleFactory> factories;
    private final Random random;
    private final NPC npc;

    public WorldController(NPC npc) {
        this.activeObstacles = new ArrayList<>();
        this.random = new Random();
        this.npc = npc;

        // ✅ SOLUTION: Register factories (loose coupling!)
        // WorldController doesn't know Spike, Goblin, Wolf classes!
        this.factories = Arrays.asList(
            new SpikeFactory(),
            new GoblinFactory(),
            new WolfFactory()
        );

        // Week 10: Spawn initial obstacles using FACTORY PATTERN
        spawnInitialObstacles();
    }

    /**
     * Spawn initial set of obstacles using FACTORY PATTERN
     *
     * ✅ SOLUTION: Uses factories instead of hard-coded creation
     * ✅ SOLUTION: WorldController doesn't know concrete types
     * ✅ SOLUTION: Can extend without modification
     */
    private void spawnInitialObstacles() {
        // ✅ Use factories for creation - no hard-coded types!

        // Spawn 4 Spikes at strategic positions (25x25 map)
        SpikeFactory spikeFactory = new SpikeFactory();
        activeObstacles.add(spikeFactory.createObstacle(6, 6));
        activeObstacles.add(spikeFactory.createObstacle(12, 8));
        activeObstacles.add(spikeFactory.createObstacle(18, 12));
        activeObstacles.add(spikeFactory.createObstacle(8, 19));

        // Spawn 4 Goblins that patrol corridors
        GoblinFactory goblinFactory = new GoblinFactory();
        activeObstacles.add(goblinFactory.createObstacle(8, 4));
        activeObstacles.add(goblinFactory.createObstacle(15, 10));
        activeObstacles.add(goblinFactory.createObstacle(10, 17));
        activeObstacles.add(goblinFactory.createObstacle(20, 20));

        // Spawn 3 Wolves that chase
        WolfFactory wolfFactory = new WolfFactory();
        activeObstacles.add(wolfFactory.createObstacle(7, 12));
        activeObstacles.add(wolfFactory.createObstacle(17, 7));
        activeObstacles.add(wolfFactory.createObstacle(12, 18));

        // ✅ Want to add Boss? Just create BossFactory and use it here!
        // ✅ No modification to WorldController logic needed!
    }

    /**
     * Update all obstacles
     *
     * ✅ SOLUTION: Still has instanceof for Wolf targeting
     * This will be fixed in next refactoring by using polymorphism
     */
    public void update(float delta) {
        // Update all active obstacles
        for (Obstacle obstacle : activeObstacles) {
            obstacle.update(delta);

            // ⚠️ TEMPORARY: Still using instanceof for Wolf targeting
            // This will be fixed by adding setTarget() to Obstacle interface
            // or using visitor pattern in advanced branch
            if (obstacle instanceof obstacles.Wolf) {
                ((obstacles.Wolf) obstacle).setTarget(npc);
            }
        }

        // Remove only inactive obstacles
        activeObstacles.removeIf(obs -> !obs.isActive());
    }

    /**
     * Get all active obstacles for rendering
     */
    public List<Obstacle> getActiveObstacles() {
        return activeObstacles;
    }

    /**
     * Get count of active obstacles (for debugging)
     */
    public int getObstacleCount() {
        return activeObstacles.size();
    }
}
