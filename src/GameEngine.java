import world.DungeonMap;

import entities.Coin;
import entities.GameManager;
import obstacles.Obstacle;
import utils.GridRenderer;

/**
 * GameEngine - Main game loop with performance monitoring
 *
 * Week 10 Branch 10-03: GC PERFORMANCE PROBLEM DEMONSTRATION
 *
 * ❌ PROBLEM: You'll see GC pauses causing frame drops!
 * ❌ PROBLEM: High object creation rate (20/second) triggers frequent GC
 * ❌ PROBLEM: GC pause = stop-the-world = visible lag spikes
 *
 * Watch the console output for:
 * - "⚠️ SLOW FRAME" messages when FPS drops below 30
 * - "🗑️ GC PAUSE" messages showing how long GC took
 *
 * This demonstrates WHY we need Object Pool pattern!
 */
public class GameEngine {
    private final GameLogic logic;
    private final HUD hud;
    private final PerformanceMonitor perfMonitor;  // ❌ Track GC impact!
    private boolean running;

    // Frame rate control
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

    // ✅ CARRIED FROM 09-01: Selective rendering to avoid flickering
    private int prevNPCX = -1;
    private int prevNPCY = -1;
    private int[] prevCoinX = null;
    private int[] prevCoinY = null;
    private java.util.Map<obstacles.Obstacle, int[]> prevObstaclePositions = new java.util.HashMap<>();
    private boolean firstFrame = true;

    // HUD rendering control
    private float hudUpdateTimer = 0;
    private final float hudUpdateInterval = 0.5f;  // Update HUD every 0.5 seconds

    /**
     * Constructor with performance monitoring
     */
    public GameEngine() {
        this.logic = new GameLogic();
        this.hud = new HUD();
        this.perfMonitor = new PerformanceMonitor();  // ❌ Monitor GC impact!

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
        System.out.println("  DUNGEON ESCAPE - GC PROBLEM DEMO");
        System.out.println("  Watch for GC pauses and lag!");
        System.out.println("=================================\n");

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

            // ❌ MEASURE: Calculate frame time
            long frameEnd = System.nanoTime();
            float frameTime = (frameEnd - frameStart) / 1_000_000_000.0f;
            perfMonitor.recordFrame(frameTime);

            // Print performance summary every 60 frames (~1 second)
            perfMonitor.printSummary(60);

            // Frame rate control
            sync(frameStart);

            // Stop after demo frames (3000 frames = ~50 seconds at 60 FPS)
            if (logic.getFrameCount() >= 3000) {
                running = false;
            }
        }

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
        logic.updateNPC(delta);
        logic.updateWorldController(delta);  // Week 10: Update obstacles
        logic.checkCollisions();
        logic.incrementFrame();

        // Update HUD timer
        hudUpdateTimer += delta;
    }

    private void draw() {
        // ✅ CARRIED FROM 09-01: Selective rendering
        if (firstFrame) {
            // First frame: Draw everything
            GridRenderer.clearScreen();

            // Week 10: Start with dungeon map
            char[][] grid = DungeonMap.getMapCopy();

            // Draw coins (only if not collected)
            for (Coin coin : logic.getCoins()) {
                if (!coin.isCollected()) {
                    grid[coin.getY()][coin.getX()] = 'C';
                }
            }

            // Week 10: Draw obstacles
            for (Obstacle obstacle : logic.getWorldController().getActiveObstacles()) {
                grid[obstacle.getY()][obstacle.getX()] = obstacle.getSymbol();
            }

            // Draw NPC last (on top)
            grid[logic.getNPCY()][logic.getNPCX()] = 'N';

            GridRenderer.drawGrid(grid);

            // Initialize previous positions
            prevNPCX = logic.getNPCX();
            prevNPCY = logic.getNPCY();
            prevCoinX = new int[logic.getCoins().size()];
            prevCoinY = new int[logic.getCoins().size()];
            for (int i = 0; i < logic.getCoins().size(); i++) {
                prevCoinX[i] = logic.getCoins().get(i).getX();
                prevCoinY[i] = logic.getCoins().get(i).getY();
            }

            firstFrame = false;
        } else {
            // ✅ Selective rendering - only update changed cells
            int currentNPCX = logic.getNPCX();
            int currentNPCY = logic.getNPCY();
            if (currentNPCX != prevNPCX || currentNPCY != prevNPCY) {
                // Clear old NPC position by restoring map tile
                char oldTile = DungeonMap.getTile(prevNPCX, prevNPCY);
                GridRenderer.drawCell(oldTile, prevNPCX, prevNPCY);
                // Draw NPC at new position
                GridRenderer.drawCell('N', currentNPCX, currentNPCY);
                prevNPCX = currentNPCX;
                prevNPCY = currentNPCY;
            }

            for (int i = 0; i < logic.getCoins().size(); i++) {
                Coin coin = logic.getCoins().get(i);

                // Only draw if not collected
                if (!coin.isCollected()) {
                    int currentX = coin.getX();
                    int currentY = coin.getY();

                    if (currentX != prevCoinX[i] || currentY != prevCoinY[i]) {
                        GridRenderer.clearCell(prevCoinX[i], prevCoinY[i]);
                        GridRenderer.drawCell('C', currentX, currentY);
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
        }

        // Draw HUD (only on first frame or periodically)
        if (firstFrame || hudUpdateTimer >= hudUpdateInterval) {
            hud.draw();  // HUD positions itself at column 28 (right of map)
            if (hudUpdateTimer >= hudUpdateInterval) {
                hudUpdateTimer = 0;  // Reset timer
            }
        }
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
