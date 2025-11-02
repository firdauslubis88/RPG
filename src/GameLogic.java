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

    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;

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

        // Week 10: Static coins placed in dungeon
        this.coins = new ArrayList<>();
        this.coins.add(new Coin(3, 3));
        this.coins.add(new Coin(5, 2));
        this.coins.add(new Coin(7, 5));
        this.coins.add(new Coin(2, 7));
        this.coins.add(new Coin(6, 8));

        // Week 10 Branch 10-01: Initialize WorldController with NPC reference
        this.worldController = new WorldController(npc);

        this.frameCount = 0;
    }

    /**
     * Week 10: NPC no longer auto-moves
     * Movement would be controlled by player input (not implemented in this demo)
     */
    public void updateNPC(float delta) {
        // No auto-movement - coins are static, enemies move
        // In full implementation, this would handle player input
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
