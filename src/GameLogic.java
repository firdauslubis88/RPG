import entities.NPC;
import entities.GameManager;
import entities.Coin;
import obstacles.Obstacle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ✅ SOLUTION: GameLogic no longer needs GameManager parameter!
 *
 * Before (09-02): Required GameManager in constructor, passed to entities
 * Now (09-03): Uses getInstance(), entities do the same
 *
 * This eliminates object drilling.
 */
public class GameLogic {
    private NPC npc;
    private List<Coin> coins;
    private WorldController worldController;
    private int frameCount;
    private Random random;

    // Removed: Use DungeonMap.getWidth() and DungeonMap.getHeight() instead

    /**
     * ✅ SOLUTION: Constructor no longer needs GameManager parameter!
     *
     * Before: GameLogic(manager) → NPC(manager) → Coin(manager)
     * Now: No parameters needed! Clean constructors!
     */
    public GameLogic() {
        this.random = new Random();

        // ✅ No manager parameter needed!
        this.npc = new NPC();

        // Week 10: Static coins placed in dungeon (30x30 map)
        this.coins = new ArrayList<>();
        this.coins.add(new Coin(5, 5));
        this.coins.add(new Coin(12, 3));
        this.coins.add(new Coin(20, 5));
        this.coins.add(new Coin(28, 8));
        this.coins.add(new Coin(8, 12));
        this.coins.add(new Coin(15, 15));
        this.coins.add(new Coin(22, 18));
        this.coins.add(new Coin(5, 22));
        this.coins.add(new Coin(18, 25));
        this.coins.add(new Coin(28, 27));

        // Week 10 Branch 10-01: Initialize WorldController with NPC reference
        this.worldController = new WorldController(npc);

        this.frameCount = 0;
    }

    private float npcMoveTimer = 0;
    private final float npcMoveInterval = 1.0f;  // NPC moves every 1 second

    /**
     * Week 10: NPC moves randomly in dungeon (respects walls)
     */
    public void updateNPC(float delta) {
        npcMoveTimer += delta;

        if (npcMoveTimer >= npcMoveInterval) {
            npcMoveTimer = 0;

            // Try random movement direction
            int direction = random.nextInt(4);  // 0=up, 1=right, 2=down, 3=left
            int newX = npc.getX();
            int newY = npc.getY();

            switch(direction) {
                case 0: newY--; break;  // Up
                case 1: newX++; break;  // Right
                case 2: newY++; break;  // Down
                case 3: newX--; break;  // Left
            }

            // Only move if target is walkable (not wall)
            if (DungeonMap.isWalkable(newX, newY)) {
                npc.tryMove(newX, newY);
            }
        }
    }

    /**
     * Week 10 Branch 10-01: Update WorldController (spawns and updates obstacles)
     */
    public void updateWorldController(float delta) {
        worldController.update(delta);
    }

    /**
     * ✅ Check collisions and update score in THE GameManager instance.
     * Week 10 Branch 10-01: Also check obstacle collisions (damage NPC)
     */
    public void checkCollisions() {
        int npcX = (int)npc.getX();
        int npcY = (int)npc.getY();

        // Check coin collisions
        for (Coin coin : coins) {
            if (coin.isCollected()) continue;

            int coinX = coin.getX();
            int coinY = coin.getY();

            // Simple collision detection
            if (npcX == coinX && npcY == coinY) {
                // ✅ Update score in THE instance
                GameManager.getInstance().addScore(10);
                coin.collect();
            }
        }

        // Week 10 Branch 10-01: Check obstacle collisions
        for (Obstacle obstacle : worldController.getActiveObstacles()) {
            int obsX = obstacle.getX();
            int obsY = obstacle.getY();

            if (npcX == obsX && npcY == obsY && obstacle.isActive()) {
                // NPC takes damage
                GameManager.getInstance().takeDamage(obstacle.getDamage());

                // Deactivate obstacle (one-time hit)
                if (obstacle instanceof obstacles.Spike) {
                    ((obstacles.Spike) obstacle).setActive(false);
                } else if (obstacle instanceof obstacles.Goblin) {
                    ((obstacles.Goblin) obstacle).setActive(false);
                } else if (obstacle instanceof obstacles.Wolf) {
                    ((obstacles.Wolf) obstacle).setActive(false);
                }
            }
        }
    }

    public void incrementFrame() {
        frameCount++;
    }

    // Getters
    public int getNPCX() {
        return (int)npc.getX();
    }

    public int getNPCY() {
        return (int)npc.getY();
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public WorldController getWorldController() {
        return worldController;
    }
}
