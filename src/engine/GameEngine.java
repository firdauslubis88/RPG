package engine;

import world.DungeonMap;
import entities.Coin;
import entities.GameManager;
import entities.Player;
import obstacles.Obstacle;
import utils.GridRenderer;
import difficulty.DifficultyStrategy;
import level.LevelLoader;
import java.util.Map;
import java.util.HashMap;

/**
 * GameEngine - Main game loop with difficulty system
 *
 * Week 13: Template Method + Facade Pattern Integration
 *
 * Design Patterns Used:
 * - Observer Pattern for event systems (from 11-04)
 * - Command Pattern for input handling (from 11-02)
 * - Strategy Pattern for difficulty (from 12-02)
 * - Template Method Pattern for level loading (13-02)
 * - Facade Pattern for battle system (13-04)
 */
public class GameEngine {
    private final GameLogic logic;
    private final HUD hud;
    private final PerformanceMonitor perfMonitor;
    private boolean running;
    private final DifficultyStrategy strategy;
    private final LevelLoader levelLoader;

    // Week 12-05: Game State Pattern - Track game result
    private boolean reachedExit = false;

    // Frame rate control
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

    // Selective rendering to avoid flickering
    private int prevPlayerX = -1;
    private int prevPlayerY = -1;
    private int[] prevCoinX = null;
    private int[] prevCoinY = null;
    private Map<Obstacle, int[]> prevObstaclePositions = new HashMap<>();
    private boolean firstFrame = true;

    // HUD rendering control
    private float hudUpdateTimer = 0;
    private final float hudUpdateInterval = 0.1f;

    /**
     * Constructor with Strategy + Template Method Pattern
     *
     * @param strategy The difficulty strategy to use
     * @param levelLoader The level loader (uses Template Method Pattern)
     */
    public GameEngine(DifficultyStrategy strategy, LevelLoader levelLoader) {
        this.strategy = strategy;
        this.levelLoader = levelLoader;
        this.logic = new GameLogic(strategy);
        this.hud = logic.getHUD();
        this.perfMonitor = new PerformanceMonitor();
        this.running = false;
    }

    /**
     * Main game loop with performance monitoring
     */
    public void start() {
        running = true;
        long lastTime = System.nanoTime();

        System.out.println("\n=================================");
        System.out.println("  DUNGEON ESCAPE");
        System.out.println("  Week 13: Template + Facade");
        System.out.println("=================================");
        System.out.println("Difficulty: " + strategy.getName());
        System.out.println("=================================\n");

        // Template Method Pattern - Load level
        levelLoader.loadLevel();

        System.out.println("Controls: W/A/S/D + Enter to move");
        System.out.println("          Q + Enter to quit");
        System.out.println("Note: Windows requires Enter after each key");
        System.out.println("Features: Template Method, Facade Pattern");
        System.out.println("Legend: D = Dungeon Exit (for boss battle)");
        System.out.println("=================================\n");

        // Hide cursor before game starts
        System.out.print("\033[?25l");
        System.out.flush();

        while (running) {
            long frameStart = System.nanoTime();

            // Calculate delta time
            long currentTime = System.nanoTime();
            float delta = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            // Update game time
            GameManager.getInstance().updateTime(delta);

            // Update phase
            update(delta);

            // Draw phase
            draw();

            // Frame rate control
            sync(frameStart);

            // Stop after demo frames (3000 frames = ~50 seconds at 60 FPS)
            if (logic.getFrameCount() >= 3000) {
                running = false;
            }
        }

        // Show cursor again after game ends
        System.out.print("\033[?25h");
        System.out.flush();

        // Move cursor below the grid and HUD
        System.out.print("\033[35;1H");
        System.out.flush();

        System.out.println("\n\n=================================");
        System.out.println("Game ended after 3000 frames (~50 seconds)");
        System.out.println(String.format("Total GC time: %dms", perfMonitor.getTotalGcTime()));
        System.out.println("=================================");

        logic.printPoolStats();
    }

    private void update(float delta) {
        logic.handleInput();
        logic.updateWorldController(delta);
        logic.checkCollisions();
        logic.incrementFrame();

        // Observer Pattern - Publish GameTimeEvent
        float elapsedTime = GameManager.getInstance().getGameTime();
        events.EventBus.getInstance().publish(new events.GameTimeEvent(elapsedTime));

        hudUpdateTimer += delta;
    }

    private void draw() {
        GridRenderer.beginFrame();

        if (firstFrame) {
            GridRenderer.clearScreen();
            char[][] grid = DungeonMap.getMapCopy();

            for (Coin coin : logic.getCoins()) {
                if (!coin.isCollected()) {
                    grid[coin.getY()][coin.getX()] = coin.getSymbol();
                }
            }

            for (Obstacle obstacle : logic.getWorldController().getActiveObstacles()) {
                grid[obstacle.getY()][obstacle.getX()] = obstacle.getSymbol();
            }

            if (logic.getDungeonExit() != null) {
                grid[logic.getDungeonExit().getY()][logic.getDungeonExit().getX()] = logic.getDungeonExit().getSymbol();
            }

            grid[logic.getPlayerY()][logic.getPlayerX()] = '@';
            GridRenderer.drawGrid(grid);

            prevPlayerX = logic.getPlayerX();
            prevPlayerY = logic.getPlayerY();
            prevCoinX = new int[logic.getCoins().size()];
            prevCoinY = new int[logic.getCoins().size()];
            for (int i = 0; i < logic.getCoins().size(); i++) {
                prevCoinX[i] = logic.getCoins().get(i).getX();
                prevCoinY[i] = logic.getCoins().get(i).getY();
            }

            firstFrame = false;
        } else {
            int currentPlayerX = logic.getPlayerX();
            int currentPlayerY = logic.getPlayerY();
            if (currentPlayerX != prevPlayerX || currentPlayerY != prevPlayerY) {
                char oldTile = DungeonMap.getTile(prevPlayerX, prevPlayerY);
                GridRenderer.drawCell(oldTile, prevPlayerX, prevPlayerY);
                prevPlayerX = currentPlayerX;
                prevPlayerY = currentPlayerY;
            }

            for (int i = 0; i < logic.getCoins().size(); i++) {
                Coin coin = logic.getCoins().get(i);
                if (!coin.isCollected()) {
                    int currentX = coin.getX();
                    int currentY = coin.getY();
                    if (currentX != prevCoinX[i] || currentY != prevCoinY[i]) {
                        GridRenderer.clearCell(prevCoinX[i], prevCoinY[i]);
                        GridRenderer.drawCell(coin.getSymbol(), currentX, currentY);
                        prevCoinX[i] = currentX;
                        prevCoinY[i] = currentY;
                    }
                } else {
                    GridRenderer.drawCell('.', prevCoinX[i], prevCoinY[i]);
                }
            }

            for (Map.Entry<Obstacle, int[]> entry : prevObstaclePositions.entrySet()) {
                int[] oldPos = entry.getValue();
                char mapTile = DungeonMap.getTile(oldPos[0], oldPos[1]);
                GridRenderer.drawCell(mapTile, oldPos[0], oldPos[1]);
            }
            prevObstaclePositions.clear();

            for (Obstacle obstacle : logic.getWorldController().getActiveObstacles()) {
                int x = obstacle.getX();
                int y = obstacle.getY();
                GridRenderer.drawCell(obstacle.getSymbol(), x, y);
                prevObstaclePositions.put(obstacle, new int[]{x, y});
            }

            int playerX = logic.getPlayerX();
            int playerY = logic.getPlayerY();

            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    int x = playerX + dx;
                    int y = playerY + dy;

                    if (x >= 0 && x < 25 && y >= 0 && y < 25) {
                        char symbol = DungeonMap.getTile(x, y);

                        if (x == playerX && y == playerY) {
                            symbol = '@';
                        } else {
                            for (Coin coin : logic.getCoins()) {
                                if (!coin.isCollected() && coin.getX() == x && coin.getY() == y) {
                                    symbol = coin.getSymbol();
                                    break;
                                }
                            }
                            for (Obstacle obstacle : logic.getWorldController().getActiveObstacles()) {
                                if (obstacle.getX() == x && obstacle.getY() == y) {
                                    symbol = obstacle.getSymbol();
                                    break;
                                }
                            }
                            if (logic.getDungeonExit() != null &&
                                logic.getDungeonExit().getX() == x &&
                                logic.getDungeonExit().getY() == y) {
                                symbol = logic.getDungeonExit().getSymbol();
                            }
                        }
                        GridRenderer.drawCell(symbol, x, y);
                    }
                }
            }
        }

        if (firstFrame || hudUpdateTimer >= hudUpdateInterval) {
            hud.draw();
            if (hudUpdateTimer >= hudUpdateInterval) {
                hudUpdateTimer = 0;
            }
        }

        int px = logic.getPlayerX();
        int py = logic.getPlayerY();
        GridRenderer.drawCell('@', px, py);
        GridRenderer.drawCell('@', px, py);
        GridRenderer.drawCell('@', px, py);

        GridRenderer.endFrame();
    }

    private void sync(long cycleStart) {
        long elapsedTime = System.nanoTime() - cycleStart;
        long waitTime = OPTIMAL_TIME - elapsedTime;

        if (waitTime > 0) {
            try {
                Thread.sleep(waitTime / 1_000_000, (int)(waitTime % 1_000_000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // Game State Pattern - Getters for state transitions
    // ═══════════════════════════════════════════════════════════════

    public Player getPlayer() {
        return logic.getPlayer();
    }

    public boolean hasReachedExit() {
        return reachedExit;
    }

    public void setReachedExit(boolean reached) {
        this.reachedExit = reached;
        if (reached) {
            running = false;
        }
    }

    public boolean isPlayerAlive() {
        return logic.getPlayer() != null && logic.getPlayer().isAlive();
    }
}
