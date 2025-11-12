import world.DungeonMap;

import entities.Player;
import entities.GameManager;
import entities.Coin;
import obstacles.Obstacle;
import input.CommandInputHandler;
import commands.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Week 11-02: GameLogic with Command Pattern
 *
 * ✅ SOLUTION: Using Command Pattern for flexible input handling
 *
 * Compare with Week 11-01:
 * ❌ Before: InputHandler with hardcoded if-else chain
 * ✅ Now: CommandInputHandler with HashMap<Character, Command>
 */
public class GameLogic {
    private Player player;
    private List<Coin> coins;
    private WorldController worldController;
    private CommandInputHandler inputHandler;
    private int frameCount;
    private Random random;

    // Week 11: Track last collision for notification
    private String lastCollisionMessage = "";

    // Removed: Use DungeonMap.getWidth() and DungeonMap.getHeight() instead

    /**
     * Week 11-02: Constructor initializes Player and CommandInputHandler
     *
     * ✅ SOLUTION: Commands are created as objects and bound to keys
     *
     * This allows:
     * - Easy remapping (just change HashMap)
     * - Adding new commands without modifying handler
     * - Reusing commands for multiple keys
     * - Loading key bindings from config file
     */
    public GameLogic() {
        this.random = new Random();

        // Week 11: Player-controlled character
        this.player = new Player(12, 12);

        // Week 10: Static coins placed in dungeon (25x25 map)
        this.coins = new ArrayList<>();
        this.coins.add(new Coin(4, 4));
        this.coins.add(new Coin(10, 3));
        this.coins.add(new Coin(16, 4));
        this.coins.add(new Coin(23, 6));
        this.coins.add(new Coin(7, 10));
        this.coins.add(new Coin(12, 12));
        this.coins.add(new Coin(18, 15));
        this.coins.add(new Coin(4, 18));
        this.coins.add(new Coin(15, 21));
        this.coins.add(new Coin(23, 22));

        // Week 11-02: Create Command objects
        Command moveUpCmd = new MoveUpCommand(player);
        Command moveDownCmd = new MoveDownCommand(player);
        Command moveLeftCmd = new MoveLeftCommand(player);
        Command moveRightCmd = new MoveRightCommand(player);
        Command quitCmd = new QuitCommand();

        // Week 11-02: Configure key bindings (easily changeable!)
        Map<Character, Command> keyBindings = new HashMap<>();
        keyBindings.put('w', moveUpCmd);
        keyBindings.put('s', moveDownCmd);
        keyBindings.put('a', moveLeftCmd);
        keyBindings.put('d', moveRightCmd);
        keyBindings.put('q', quitCmd);

        // Week 11-02: CommandInputHandler with flexible bindings
        this.inputHandler = new CommandInputHandler(keyBindings);

        // Week 11: WorldController tracks Player instead of NPC
        this.worldController = new WorldController(player);

        this.frameCount = 0;
    }

    /**
     * Week 11-02: Handle keyboard input with Command Pattern
     *
     * ✅ SOLUTION: Just delegate to CommandInputHandler
     *
     * The handler looks up the command and executes it.
     * No if-else chain needed here!
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
