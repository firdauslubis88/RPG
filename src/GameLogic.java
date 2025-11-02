import entities.NPC;
import entities.Coin;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ✅ SOLUTION: Pure game logic with NO rendering code.
 *
 * This class contains all game state and update logic, completely
 * separated from rendering. This enables:
 * - Unit testing without display
 * - Frame-rate independent movement (delta time)
 * - Clean separation of concerns
 * - Easy debugging and maintenance
 */
public class GameLogic {
    private NPC npc;
    private List<Coin> coins;
    private int score;
    private int frameCount;
    private Random random;

    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    private static final float NPC_VELOCITY = 3.0f;  // 3 pixels per second
    private static final float COIN_FALL_SPEED = 2.0f;  // 2 pixels per second

    /**
     * Initializes game logic with starting state.
     */
    public GameLogic() {
        this.random = new Random();
        this.npc = new NPC(0, 5, NPC_VELOCITY);
        this.coins = new ArrayList<>();
        this.coins.add(new Coin(random.nextFloat() * GRID_WIDTH, 0, COIN_FALL_SPEED));
        this.score = 0;
        this.frameCount = 0;
    }

    /**
     * ✅ SOLUTION: Update NPC position using delta time.
     * Movement is frame-rate independent!
     *
     * @param delta Time elapsed since last frame (in seconds)
     */
    public void updateNPC(float delta) {
        // ✅ Delta time based movement: distance = velocity × time
        float newX = npc.getX() + npc.getVelocity() * delta;

        // Wrap around at edges
        if (newX >= GRID_WIDTH) {
            newX = newX - GRID_WIDTH;
        }

        npc.setX(newX);
    }

    /**
     * ✅ SOLUTION: Update coins using delta time.
     * Gravity simulation is frame-rate independent!
     *
     * @param delta Time elapsed since last frame (in seconds)
     */
    public void updateCoins(float delta) {
        for (Coin coin : coins) {
            // ✅ Delta time based falling: distance = speed × time
            float newY = coin.getY() + coin.getFallSpeed() * delta;

            if (newY >= GRID_HEIGHT) {
                // Respawn at top with random X
                coin.setY(0);
                coin.setX(random.nextFloat() * GRID_WIDTH);
            } else {
                coin.setY(newY);
            }
        }
    }

    /**
     * ✅ SOLUTION: Pure collision detection logic.
     * No rendering, no side effects (except score update).
     */
    public void checkCollisions() {
        int npcX = (int)npc.getX();
        int npcY = (int)npc.getY();

        for (Coin coin : coins) {
            int coinX = (int)coin.getX();
            int coinY = (int)coin.getY();

            if (npcX == coinX && npcY == coinY) {
                score += 10;
                // Respawn coin
                coin.setY(0);
                coin.setX(random.nextFloat() * GRID_WIDTH);
            }
        }
    }

    /**
     * Increments frame counter.
     */
    public void incrementFrame() {
        frameCount++;
    }

    // ✅ SOLUTION: Getters for rendering (read-only access)
    public int getNPCX() {
        return (int)npc.getX();
    }

    public int getNPCY() {
        return (int)npc.getY();
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public int getScore() {
        return score;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public static int getGridWidth() {
        return GRID_WIDTH;
    }

    public static int getGridHeight() {
        return GRID_HEIGHT;
    }
}
