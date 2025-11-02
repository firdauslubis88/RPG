import entities.Coin;
import entities.GameManager;
import utils.GridRenderer;

/**
 * ✅ SOLUTION: GameEngine no longer needs GameManager parameter!
 *
 * Before (09-02): Required manager parameter for object drilling
 * Now (09-03): Uses GameManager.getInstance() directly
 *
 * This eliminates object drilling problem.
 */
public class GameEngine {
    private final GameLogic logic;
    private final HUD hud;
    private boolean running;

    // Frame rate control
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

    // ✅ CARRIED FROM 09-01: Selective rendering to avoid flickering
    private int prevNPCX = -1;
    private int prevNPCY = -1;
    private int[] prevCoinX = null;
    private int[] prevCoinY = null;
    private boolean firstFrame = true;

    /**
     * ✅ SOLUTION: Constructor no longer needs GameManager parameter!
     *
     * Components will access GameManager via getInstance().
     */
    public GameEngine() {
        // ✅ No manager parameter needed!
        this.logic = new GameLogic();
        this.hud = new HUD();

        this.running = false;

        System.out.println("[GameEngine] Using Singleton - no object drilling!");
    }

    /**
     * Main game loop.
     */
    public void start() {
        running = true;
        long lastTime = System.nanoTime();

        System.out.println("\n=================================");
        System.out.println("  DUNGEON ESCAPE - WITH SINGLETON");
        System.out.println("=================================");
        System.out.println("✅ Single GameManager instance!");
        System.out.println("✅ No object drilling!");
        System.out.println("✅ Score consistency guaranteed!");
        System.out.println("=================================\n");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        while (running) {
            long cycleStart = System.nanoTime();

            // Calculate delta time
            long currentTime = System.nanoTime();
            float delta = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            // ✅ Update game time in THE instance
            GameManager.getInstance().updateTime(delta);

            // Update phase
            update(delta);

            // Draw phase
            draw();

            // Frame rate control
            sync(cycleStart);

            // Stop after demo frames (600 frames = ~10 seconds at 60 FPS)
            if (logic.getFrameCount() >= 600) {
                running = false;
            }
        }

        System.out.println("\n=================================");
        System.out.println("Demo ended after 600 frames (~10 seconds)");
        System.out.println("=================================");
        System.out.println("\n✅ PROBLEMS SOLVED:");
        System.out.println("1. HUD shows correct score (same instance!)");
        System.out.println("2. No parameter passing needed");
        System.out.println("3. Single instance guaranteed");
        System.out.println("\nSingleton pattern success!");
    }

    private void update(float delta) {
        logic.updateNPC(delta);
        logic.updateCoins(delta);
        logic.checkCollisions();
        logic.incrementFrame();
    }

    private void draw() {
        // ✅ CARRIED FROM 09-01: Selective rendering
        if (firstFrame) {
            // First frame: Draw everything
            GridRenderer.clearScreen();

            char[][] grid = GridRenderer.createEmptyGrid();
            GridRenderer.drawEntity(grid, 'N', logic.getNPCX(), logic.getNPCY());

            for (Coin coin : logic.getCoins()) {
                GridRenderer.drawEntity(grid, 'C', (int)coin.getX(), (int)coin.getY());
            }

            GridRenderer.drawGrid(grid);

            // Initialize previous positions
            prevNPCX = logic.getNPCX();
            prevNPCY = logic.getNPCY();
            prevCoinX = new int[logic.getCoins().size()];
            prevCoinY = new int[logic.getCoins().size()];
            for (int i = 0; i < logic.getCoins().size(); i++) {
                prevCoinX[i] = (int)logic.getCoins().get(i).getX();
                prevCoinY[i] = (int)logic.getCoins().get(i).getY();
            }

            firstFrame = false;
        } else {
            // ✅ Selective rendering - only update changed cells
            int currentNPCX = logic.getNPCX();
            int currentNPCY = logic.getNPCY();
            if (currentNPCX != prevNPCX || currentNPCY != prevNPCY) {
                GridRenderer.clearCell(prevNPCX, prevNPCY);
                GridRenderer.drawCell('N', currentNPCX, currentNPCY);
                prevNPCX = currentNPCX;
                prevNPCY = currentNPCY;
            }

            for (int i = 0; i < logic.getCoins().size(); i++) {
                Coin coin = logic.getCoins().get(i);
                int currentX = (int)coin.getX();
                int currentY = (int)coin.getY();

                if (currentX != prevCoinX[i] || currentY != prevCoinY[i]) {
                    GridRenderer.clearCell(prevCoinX[i], prevCoinY[i]);
                    GridRenderer.drawCell('C', currentX, currentY);
                    prevCoinX[i] = currentX;
                    prevCoinY[i] = currentY;
                }
            }
        }

        // ✅ Draw HUD (will show CORRECT score now!)
        GridRenderer.moveCursorBelowGrid(1);
        hud.draw();

        // ✅ Show score from THE instance
        System.out.println("[GameEngine] Score from Singleton: " + GameManager.getInstance().getScore());
        System.out.println("[GameEngine] Frame: " + logic.getFrameCount());
        System.out.println();
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
