import entities.NPC;
import entities.GameManager;
import entities.Coin;
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

        // ✅ No manager parameter for coins either!
        this.coins = new ArrayList<>();
        // Add more coins to increase collision chances
        this.coins.add(new Coin());
        this.coins.add(new Coin());
        this.coins.add(new Coin());
        this.coins.add(new Coin());
        this.coins.add(new Coin());

        this.frameCount = 0;

        System.out.println("[GameLogic] Using Singleton - no parameters needed!");
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
     * ✅ Check collisions and update score in THE GameManager instance.
     */
    public void checkCollisions() {
        int npcX = (int)npc.getX();
        int npcY = (int)npc.getY();

        for (Coin coin : coins) {
            int coinX = (int)coin.getX();
            int coinY = (int)coin.getY();

            // Simple collision detection
            if (npcX == coinX && npcY == coinY) {
                // ✅ Update score in THE instance
                GameManager.getInstance().addScore(10);

                System.out.println("[GameLogic] Collision detected!");
                System.out.println("[GameLogic] Singleton instance: " + GameManager.getInstance().hashCode());

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
}
