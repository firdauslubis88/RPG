import world.DungeonMap;

import entities.Player;
import entities.GameManager;
import entities.Coin;
import obstacles.Obstacle;
import input.InputHandler;
import systems.SoundSystem;
import systems.AchievementSystem;
import commands.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Week 11-03: GameLogic with Tight Coupling (ANTI-PATTERN)
 *
 * ✅ KEPT: Command Pattern for input handling (from 11-02)
 * ❌ NEW PROBLEM: Object Drilling for event systems!
 *
 * Issues demonstrated:
 * 1. Must create SoundSystem, AchievementSystem, HUD here
 * 2. Must pass all systems to Player (object drilling)
 * 3. Must pass AchievementSystem to HUD
 * 4. Complex initialization chain
 * 5. Hard to add new systems (must modify this constructor)
 */
public class GameLogic {
    private Player player;
    private List<Coin> coins;
    private WorldController worldController;
    private InputHandler inputHandler;
    private int frameCount;
    private Random random;

    // Week 11-03: ❌ TIGHT COUPLING - GameLogic must manage all systems
    private SoundSystem soundSystem;
    private AchievementSystem achievementSystem;
    private HUD hud;

    // Week 11: Track last collision for notification
    private String lastCollisionMessage = "";

    // Removed: Use DungeonMap.getWidth() and DungeonMap.getHeight() instead

    /**
     * Week 11-03: ❌ ANTI-PATTERN - Constructor with Object Drilling
     *
     * Problems demonstrated:
     * 1. Must create ALL systems here (SoundSystem, AchievementSystem)
     * 2. Must pass systems to Player (object drilling)
     * 3. Must pass systems to HUD (more object drilling)
     * 4. Complex initialization order dependencies
     * 5. Want to add ParticleSystem? Modify this constructor again!
     */
    public GameLogic() {
        this.random = new Random();

        // Week 11-03: ❌ OBJECT DRILLING - Create all systems first
        this.soundSystem = new SoundSystem();
        this.achievementSystem = new AchievementSystem(soundSystem);  // AchievementSystem needs SoundSystem!
        this.hud = new HUD(achievementSystem);  // HUD needs AchievementSystem!

        // Week 11-03: ❌ OBJECT DRILLING - Pass all systems to Player
        // Before: new Player(10, 10) - simple!
        // Now: new Player(10, 10, soundSystem, achievementSystem) - complex!
        this.player = new Player(10, 10, soundSystem, achievementSystem);

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

        // Week 11-02: ✅ COMMAND PATTERN - Create command objects
        Command moveUpCmd = new MoveUpCommand(player);
        Command moveDownCmd = new MoveDownCommand(player);
        Command moveLeftCmd = new MoveLeftCommand(player);
        Command moveRightCmd = new MoveRightCommand(player);
        Command quitCmd = new QuitCommand();

        // Week 11-02: ✅ COMMAND PATTERN - Configure key bindings
        // Want to remap keys? Just change this HashMap!
        // Want IJKL instead of WASD? Easy:
        //   keyBindings.put('i', moveUpCmd);
        //   keyBindings.put('k', moveDownCmd);
        //   keyBindings.put('j', moveLeftCmd);
        //   keyBindings.put('l', moveRightCmd);
        Map<Character, Command> keyBindings = new HashMap<>();
        keyBindings.put('w', moveUpCmd);
        keyBindings.put('s', moveDownCmd);
        keyBindings.put('a', moveLeftCmd);
        keyBindings.put('d', moveRightCmd);
        keyBindings.put('q', quitCmd);

        // Week 11-02: InputHandler with Command Pattern
        this.inputHandler = new InputHandler(keyBindings);

        // Week 11: WorldController tracks Player instead of NPC
        this.worldController = new WorldController(player);

        this.frameCount = 0;
    }

    /**
     * Week 11-02: Handle keyboard input
     *
     * ✅ COMMAND PATTERN: Input handler uses command lookup
     * No hardcoded keys - all configured in HashMap!
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
     * Week 11-03: Get HUD (for GameEngine)
     */
    public HUD getHUD() {
        return hud;
    }

    /**
     * Week 11-03: Get AchievementSystem (for GameEngine to check time-based achievements)
     */
    public AchievementSystem getAchievementSystem() {
        return achievementSystem;
    }

    /**
     * Print pool statistics (for branch 10-04)
     */
    public void printPoolStats() {
        worldController.printPoolStats();
    }
}
