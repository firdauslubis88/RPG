import world.DungeonMap;

import entities.Coin;
import entities.GameManager;
import obstacles.Obstacle;
import utils.GridRenderer;
import difficulty.DifficultyStrategy;
import level.LevelLoader;

/**
 * GameEngine - Main game loop with difficulty system
 *
 * Week 13: Template Method + Facade Pattern Integration
 *
 * ✅ KEPT: Observer Pattern for event systems (from 11-04)
 * ✅ KEPT: Command Pattern for input handling (from 11-02)
 * ✅ KEPT: Strategy Pattern for difficulty (from 12-02)
 * ✅ NEW: Template Method Pattern for level loading (13-02)
 * ✅ NEW: Facade Pattern for battle system (13-04)
 */
public class GameEngine {
    private final GameLogic logic;
    private final HUD hud;
    private final PerformanceMonitor perfMonitor;  // Week 10: Track GC impact
    private boolean running;
    private final DifficultyStrategy strategy;  // Week 12-02: Store strategy for display
    private final LevelLoader levelLoader;  // Week 13-02: Template Method for level loading

    // Frame rate control
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

    // Week 11: Selective rendering to avoid flickering
    private int prevPlayerX = -1;
    private int prevPlayerY = -1;
    private int[] prevCoinX = null;
    private int[] prevCoinY = null;
    private java.util.Map<obstacles.Obstacle, int[]> prevObstaclePositions = new java.util.HashMap<>();
    private boolean firstFrame = true;

    // HUD rendering control
    private float hudUpdateTimer = 0;
    private final float hudUpdateInterval = 0.1f;  // Update HUD every 0.1 seconds (more responsive)

    /**
     * Week 13: Constructor with Strategy + Template Method Pattern
     *
     * ✅ KEPT: Strategy Pattern for difficulty
     * ✅ NEW: Template Method Pattern for level loading
     *
     * @param strategy The difficulty strategy to use
     * @param levelLoader The level loader (uses Template Method Pattern)
     */
    public GameEngine(DifficultyStrategy strategy, LevelLoader levelLoader) {
        this.strategy = strategy;
        this.levelLoader = levelLoader;
        this.logic = new GameLogic(strategy);  // Week 12-02: Pass strategy to GameLogic
        this.hud = logic.getHUD();  // Week 11-04: Get HUD from GameLogic for rendering
        this.perfMonitor = new PerformanceMonitor();

        this.running = false;
    }

    /**
     * Main game loop with performance monitoring
     *
     * ❌ PROBLEM: Watch for GC pauses slowing down the game!
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

        // Week 13-02: ✅ TEMPLATE METHOD PATTERN - Load level using abstract loader!
        // The loadLevel() template method ensures consistent loading sequence:
        // 1. Load assets → 2. Build world → 3. Spawn enemies → 4. Play music
        // Note: levelLoader is passed from Main via MainMenu selection
        levelLoader.loadLevel();

        System.out.println("Controls: W/A/S/D + Enter to move");
        System.out.println("          Q + Enter to quit");
        System.out.println("Note: Windows requires Enter after each key");
        System.out.println("Features: Template Method, Facade Pattern");
        System.out.println("Legend: D = Dungeon Exit (for boss battle)");
        System.out.println("=================================\n");

        // Week 11: Hide cursor before game starts
        System.out.print("\033[?25l");
        System.out.flush();

        while (running) {
            long frameStart = System.nanoTime();

            // Calculate delta time
            long currentTime = System.nanoTime();
            float delta = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            // Update game time in THE instance
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

        // Week 11: Show cursor again after game ends
        System.out.print("\033[?25h");
        System.out.flush();

        // Move cursor far below the grid and HUD before printing final results
        System.out.print("\033[35;1H");
        System.out.flush();

        System.out.println("\n\n=================================");
        System.out.println("Game ended after 3000 frames (~50 seconds)");
        System.out.println(String.format("Total GC time: %dms", perfMonitor.getTotalGcTime()));
        System.out.println("=================================");

        // Print pool statistics for branch 10-04
        logic.printPoolStats();
    }

    private void update(float delta) {
        // Week 11-02: Handle player input (Command Pattern)
        logic.handleInput();

        logic.updateWorldController(delta);  // Update obstacles
        logic.checkCollisions();  // Check Player vs Coins and Obstacles
        logic.incrementFrame();

        // Week 11-04: ✅ OBSERVER PATTERN - Publish GameTimeEvent for time-based systems
        // Before (11-03): logic.getAchievementSystem().checkSurvivor(elapsedTime) - tight coupling!
        // Now (11-04): Publish event, AchievementSystem listens automatically!
        float elapsedTime = GameManager.getInstance().getGameTime();
        events.EventBus.getInstance().publish(new events.GameTimeEvent(elapsedTime));

        // Update HUD timer
        hudUpdateTimer += delta;
    }

    private void draw() {
        // Week 11: Begin frame buffering to reduce flickering
        GridRenderer.beginFrame();

        // ✅ CARRIED FROM 09-01: Selective rendering
        if (firstFrame) {
            // First frame: Draw everything
            GridRenderer.clearScreen();

            // Week 10: Start with dungeon map
            char[][] grid = DungeonMap.getMapCopy();

            // Draw coins (only if not collected)
            for (Coin coin : logic.getCoins()) {
                if (!coin.isCollected()) {
                    grid[coin.getY()][coin.getX()] = coin.getSymbol();
                }
            }

            // Week 10: Draw obstacles
            for (Obstacle obstacle : logic.getWorldController().getActiveObstacles()) {
                grid[obstacle.getY()][obstacle.getX()] = obstacle.getSymbol();
            }

            // Week 12-01: Draw dungeon exit (after obstacles so it's always visible)
            if (logic.getDungeonExit() != null) {
                grid[logic.getDungeonExit().getY()][logic.getDungeonExit().getX()] = logic.getDungeonExit().getSymbol();
            }

            // Draw Player last (on top of everything)
            grid[logic.getPlayerY()][logic.getPlayerX()] = '@';

            GridRenderer.drawGrid(grid);

            // Initialize previous positions
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
            // Week 11: Selective rendering - only update changed cells
            int currentPlayerX = logic.getPlayerX();
            int currentPlayerY = logic.getPlayerY();
            if (currentPlayerX != prevPlayerX || currentPlayerY != prevPlayerY) {
                // Week 11: Cover input echo at old position FIRST
                char oldTile = DungeonMap.getTile(prevPlayerX, prevPlayerY);
                GridRenderer.drawCell(oldTile, prevPlayerX, prevPlayerY);

                // Update tracking variables
                prevPlayerX = currentPlayerX;
                prevPlayerY = currentPlayerY;
            }

            for (int i = 0; i < logic.getCoins().size(); i++) {
                Coin coin = logic.getCoins().get(i);

                // Only draw if not collected
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
                    // Coin was collected, clear it
                    GridRenderer.drawCell('.', prevCoinX[i], prevCoinY[i]);
                }
            }

            // Week 10: Render obstacles with proper trail clearing
            // Clear old positions first
            for (java.util.Map.Entry<obstacles.Obstacle, int[]> entry : prevObstaclePositions.entrySet()) {
                int[] oldPos = entry.getValue();
                // Clear old position by redrawing floor/wall from map
                char mapTile = DungeonMap.getTile(oldPos[0], oldPos[1]);
                GridRenderer.drawCell(mapTile, oldPos[0], oldPos[1]);
            }
            prevObstaclePositions.clear();

            // Draw obstacles at new positions
            for (Obstacle obstacle : logic.getWorldController().getActiveObstacles()) {
                int x = obstacle.getX();
                int y = obstacle.getY();
                GridRenderer.drawCell(obstacle.getSymbol(), x, y);
                prevObstaclePositions.put(obstacle, new int[]{x, y});
            }

            // Week 11-01: Redraw 3x3 area around player to cover input echo
            // Windows console echoes input at cursor position. Combined with cursor
            // repositioning (GridRenderer.endFrame() moves cursor to row 27), this
            // ensures any stray echo near player is covered by correct symbols.
            // This is part of our input echo mitigation strategy
            int playerX = logic.getPlayerX();
            int playerY = logic.getPlayerY();

            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    int x = playerX + dx;
                    int y = playerY + dy;

                    if (x >= 0 && x < 25 && y >= 0 && y < 25) {
                        // Get correct symbol for this cell
                        char symbol = DungeonMap.getTile(x, y);

                        // Check if player is at this position
                        if (x == playerX && y == playerY) {
                            symbol = '@';
                        } else {
                            // Check for coins
                            for (Coin coin : logic.getCoins()) {
                                if (!coin.isCollected() && coin.getX() == x && coin.getY() == y) {
                                    symbol = coin.getSymbol();
                                    break;
                                }
                            }

                            // Check for obstacles
                            for (Obstacle obstacle : logic.getWorldController().getActiveObstacles()) {
                                if (obstacle.getX() == x && obstacle.getY() == y) {
                                    symbol = obstacle.getSymbol();
                                    break;
                                }
                            }

                            // Week 12-01: Check for dungeon exit
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

        // Draw HUD (only on first frame or periodically)
        if (firstFrame || hudUpdateTimer >= hudUpdateInterval) {
            hud.draw();  // HUD positions itself at column 28 (right of map)
            if (hudUpdateTimer >= hudUpdateInterval) {
                hudUpdateTimer = 0;  // Reset timer
            }
        }

        // Week 11: Force redraw player position multiple times to overwrite input echo
        // Windows console input echo cannot be fully disabled, so we aggressively
        // redraw the player character to ensure it stays visible
        int px = logic.getPlayerX();
        int py = logic.getPlayerY();

        // Redraw player 3 times to ensure visibility (overwrites any input echo)
        GridRenderer.drawCell('@', px, py);
        GridRenderer.drawCell('@', px, py);
        GridRenderer.drawCell('@', px, py);

        // Week 11: End frame buffering - flush all updates at once
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
}
