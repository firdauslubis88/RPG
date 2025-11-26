package engine;

import world.DungeonMap;
import entities.Player;
import entities.GameManager;
import entities.Coin;
import entities.DungeonExit;
import obstacles.Obstacle;
import input.InputHandler;
import systems.SoundSystem;
import systems.AchievementSystem;
import events.EventBus;
import difficulty.DifficultyStrategy;
import battle.BattleFacade;
import commands.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * GameLogic - Game rules and collision handling
 *
 * Design Patterns Used:
 * - Observer Pattern for event systems
 * - Command Pattern for input handling
 * - Strategy Pattern for difficulty
 * - Facade Pattern for battle subsystems
 */
public class GameLogic {
    private Player player;
    private List<Coin> coins;
    private DungeonExit dungeonExit;
    private WorldController worldController;
    private InputHandler inputHandler;
    private int frameCount;
    private Random random;

    private SoundSystem soundSystem;
    private AchievementSystem achievementSystem;
    private HUD hud;

    private String lastCollisionMessage = "";
    private DifficultyStrategy strategy;

    public GameLogic(DifficultyStrategy strategy) {
        this.random = new Random();
        this.strategy = strategy;

        // Observer Pattern - Create systems independently
        this.soundSystem = new SoundSystem();
        this.achievementSystem = new AchievementSystem();
        this.hud = new HUD();

        // Register observers with EventBus
        EventBus eventBus = EventBus.getInstance();
        eventBus.subscribe(soundSystem);
        eventBus.subscribe(achievementSystem);
        eventBus.subscribe(hud);

        this.player = new Player(10, 10);

        // Static coins placed in dungeon
        this.coins = new ArrayList<>();
        this.coins.add(new Coin(4, 4));
        this.coins.add(new Coin(10, 3));
        this.coins.add(new Coin(16, 4));
        this.coins.add(new Coin(23, 6));
        this.coins.add(new Coin(7, 10));
        this.coins.add(new Coin(18, 15));
        this.coins.add(new Coin(4, 18));
        this.coins.add(new Coin(15, 21));
        this.coins.add(new Coin(23, 22));

        this.dungeonExit = new DungeonExit(23, 23);

        // Command Pattern - Create command objects
        Command moveUpCmd = new MoveUpCommand(player);
        Command moveDownCmd = new MoveDownCommand(player);
        Command moveLeftCmd = new MoveLeftCommand(player);
        Command moveRightCmd = new MoveRightCommand(player);
        Command quitCmd = new QuitCommand();

        Map<Character, Command> keyBindings = new HashMap<>();
        keyBindings.put('w', moveUpCmd);
        keyBindings.put('s', moveDownCmd);
        keyBindings.put('a', moveLeftCmd);
        keyBindings.put('d', moveRightCmd);
        keyBindings.put('q', quitCmd);

        this.inputHandler = new InputHandler(keyBindings);
        this.worldController = new WorldController(player, strategy);
        this.frameCount = 0;
    }

    public void handleInput() {
        inputHandler.handleInput();
    }

    public void updateWorldController(float delta) {
        worldController.update(delta);
    }

    public void checkCollisions() {
        int playerX = player.getX();
        int playerY = player.getY();

        // Check if player reached dungeon exit - trigger boss battle
        if (playerX == dungeonExit.getX() && playerY == dungeonExit.getY()) {
            boolean isDemoMode = strategy.getName().equals("DEMO");

            // Facade Pattern - One call to run full battle
            BattleFacade battleFacade = new BattleFacade(player, isDemoMode);
            boolean playerWon = battleFacade.runFullBattle();

            if (playerWon) {
                System.out.println("\n CONGRATULATIONS! You escaped the dungeon!");
                System.out.println("Final Score: " + GameManager.getInstance().getScore());
                System.exit(0);
            } else {
                if (GameManager.getInstance().getHp() <= 0) {
                    System.out.println("\n GAME OVER - Defeated by the boss");
                    System.exit(0);
                } else {
                    System.out.println("\n You fled back into the dungeon...");
                    player.moveUp();
                }
            }
        }

        // Check coin collisions
        for (Coin coin : coins) {
            if (coin.isCollected()) continue;

            if (playerX == coin.getX() && playerY == coin.getY()) {
                player.collectCoin(coin.getValue());
                GameManager.getInstance().addScore(coin.getValue());
                coin.collect();
            }
        }

        // Check obstacle collisions
        for (Obstacle obstacle : worldController.getActiveObstacles()) {
            int obsX = obstacle.getX();
            int obsY = obstacle.getY();

            if (playerX == obsX && playerY == obsY && obstacle.isActive()) {
                player.takeDamage(obstacle.getDamage());
                GameManager.getInstance().takeDamage(obstacle.getDamage());

                lastCollisionMessage = String.format("HIT! -%dHP | Remaining: %d/100",
                    obstacle.getDamage(),
                    GameManager.getInstance().getHp());

                obstacle.setActive(false);
            }
        }
    }

    public void incrementFrame() {
        frameCount++;
    }

    // Getters
    public Player getPlayer() { return player; }
    public int getPlayerX() { return player.getX(); }
    public int getPlayerY() { return player.getY(); }
    public List<Coin> getCoins() { return coins; }
    public int getFrameCount() { return frameCount; }
    public WorldController getWorldController() { return worldController; }
    public String getLastCollisionMessage() { return lastCollisionMessage; }
    public void clearCollisionMessage() { lastCollisionMessage = ""; }
    public HUD getHUD() { return hud; }
    public DungeonExit getDungeonExit() { return dungeonExit; }

    public void printPoolStats() {
        worldController.printPoolStats();
    }
}
