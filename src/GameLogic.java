import entities.NPC;
import entities.GameManager;
import entities.Coin;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ❌ PROBLEM: GameLogic now requires GameManager parameter!
 *
 * Before (09-01): GameLogic managed its own state
 * Now (09-02): Must receive and pass GameManager everywhere
 *
 * This demonstrates object drilling - passing parameters through
 * multiple levels just to reach the final destination.
 *
 * This is INTENTIONALLY BAD code for educational purposes!
 */
public class GameLogic {
    private final GameManager manager;  // ❌ Dependency
    private NPC npc;
    private List<Coin> coins;
    private int frameCount;
    private Random random;

    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;

    /**
     * ❌ PROBLEM: Constructor requires GameManager parameter!
     *
     * This starts the object drilling chain:
     * Main → GameEngine → GameLogic → NPC/Coin
     *
     * @param manager The GameManager instance (to be passed down further)
     */
    public GameLogic(GameManager manager) {
        this.manager = manager;
        this.random = new Random();

        // ❌ Must pass manager to NPC constructor
        this.npc = new NPC(manager);

        // ❌ Must pass manager to each Coin constructor
        this.coins = new ArrayList<>();
        // Add more coins to increase collision chances
        this.coins.add(new Coin(manager));
        this.coins.add(new Coin(manager));
        this.coins.add(new Coin(manager));
        this.coins.add(new Coin(manager));
        this.coins.add(new Coin(manager));

        this.frameCount = 0;

        System.out.println("[GameLogic] Using manager instance: " + manager.hashCode());
    }

    /**
     * Update NPC position using delta time.
     */
    public void updateNPC(float delta) {
        float newX = npc.getX() + npc.getVelocity() * delta;

        // Wrap around at edge
        if (newX >= GRID_WIDTH) {
            newX = newX - GRID_WIDTH;
        }

        npc.setX(newX);
    }

    /**
     * Update coins with gravity.
     */
    public void updateCoins(float delta) {
        for (Coin coin : coins) {
            float newY = coin.getY() + coin.getFallSpeed() * delta;

            if (newY >= GRID_HEIGHT) {
                coin.respawn();
            } else {
                coin.setY(newY);
            }
        }
    }

    /**
     * Check collisions and update score in GameManager.
     */
    public void checkCollisions() {
        int npcX = (int)npc.getX();
        int npcY = (int)npc.getY();

        for (Coin coin : coins) {
            int coinX = (int)coin.getX();
            int coinY = (int)coin.getY();

            // Simple collision detection
            if (npcX == coinX && npcY == coinY) {
                // ✅ Update score in manager (THIS instance)
                manager.addScore(10);

                System.out.println("[GameLogic] Collision detected!");
                System.out.println("[GameLogic] Manager instance: " + manager.hashCode());

                coin.respawn();
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

    public GameManager getManager() {
        return manager;
    }
}
