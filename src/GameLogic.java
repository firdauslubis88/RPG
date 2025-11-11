import world.DungeonMap;

import entities.Player;
import entities.GameManager;
import entities.Coin;
import obstacles.Obstacle;
import input.InputHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Week 11-01: GameLogic with Player Control
 *
 * NEW: Player-controlled character with keyboard input
 * NPC removed, replaced by Player (@)
 * Collision detection for Player vs Coins and Obstacles
 */
public class GameLogic {
    private Player player;
    private List<Coin> coins;
    private WorldController worldController;
    private InputHandler inputHandler;
    private int frameCount;
    private Random random;

    // Week 11: Track last collision for notification
    private String lastCollisionMessage = "";

    // Removed: Use DungeonMap.getWidth() and DungeonMap.getHeight() instead

    /**
     * Week 11-01: Constructor initializes Player and InputHandler
     *
     * Player starts at center of map (12, 12)
     * InputHandler created with hardcoded key bindings (ANTI-PATTERN)
     */
    public GameLogic() {
        this.random = new Random();

        // Week 11: Player-controlled character (spawn at 10, 10)
        this.player = new Player(10, 10);

        // Week 10: Static coins placed in dungeon (25x25 map)
        this.coins = new ArrayList<>();
        this.coins.add(new Coin(4, 4));
        this.coins.add(new Coin(10, 3));
        this.coins.add(new Coin(16, 4));
        this.coins.add(new Coin(23, 6));
        this.coins.add(new Coin(7, 10));
        // Coin at (12, 12) removed - was overlapping with old player spawn
        this.coins.add(new Coin(18, 15));
        this.coins.add(new Coin(4, 18));
        this.coins.add(new Coin(15, 21));
        this.coins.add(new Coin(23, 22));

        // Week 11-01: Hardcoded input handler (ANTI-PATTERN)
        this.inputHandler = new InputHandler(player);

        // Week 11: WorldController tracks Player instead of NPC
        this.worldController = new WorldController(player);

        this.frameCount = 0;
    }

    /**
     * Week 11-01: Handle keyboard input
     *
     * ❌ ANTI-PATTERN: Input handling with hardcoded keys
     */
    public void handleInput() {
        inputHandler.handleInput();
    }

    /**
     * Week 10 Branch 10-01: Update WorldController (spawns and updates obstacles)
     */
    public void updateWorldController(float delta) {
        worldController.update(delta);
    }

    /**
     * Week 11-01: Check collisions - Player vs Coins and Obstacles
     */
    public void checkCollisions() {
        int playerX = player.getX();
        int playerY = player.getY();

        // Check coin collisions
        for (Coin coin : coins) {
            if (coin.isCollected()) continue;

            int coinX = coin.getX();
            int coinY = coin.getY();

            // Simple collision detection
            if (playerX == coinX && playerY == coinY) {
                // Player collects coin
                player.collectCoin(coin.getValue());
                GameManager.getInstance().addScore(coin.getValue());
                coin.collect();
            }
        }

        // Week 11-01: Check obstacle collisions
        for (Obstacle obstacle : worldController.getActiveObstacles()) {
            int obsX = obstacle.getX();
            int obsY = obstacle.getY();

            if (playerX == obsX && playerY == obsY && obstacle.isActive()) {
                // Player takes damage
                player.takeDamage(obstacle.getDamage());
                GameManager.getInstance().takeDamage(obstacle.getDamage());

                // Week 11: Record collision for notification
                lastCollisionMessage = String.format("💥 HIT! -%dHP | Remaining: %d/100",
                    obstacle.getDamage(),
                    GameManager.getInstance().getHp());

                // Remove obstacle after hit
                obstacle.setActive(false);
            }
        }
    }

    public void incrementFrame() {
        frameCount++;
    }

    // Getters
    public Player getPlayer() {
        return player;
    }

    public int getPlayerX() {
        return player.getX();
    }

    public int getPlayerY() {
        return player.getY();
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

    /**
     * Week 11: Get last collision message for display
     */
    public String getLastCollisionMessage() {
        return lastCollisionMessage;
    }

    /**
     * Week 11: Clear collision message after display
     */
    public void clearCollisionMessage() {
        lastCollisionMessage = "";
    }

    /**
     * Print pool statistics (for branch 10-04)
     */
    public void printPoolStats() {
        worldController.printPoolStats();
    }
}
