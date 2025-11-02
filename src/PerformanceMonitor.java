import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * PerformanceMonitor - Tracks frame times and detects GC pauses
 *
 * Week 10 Branch 10-03: GC PERFORMANCE PROBLEM DEMONSTRATION
 *
 * This class helps visualize the problem:
 * - Tracks frame rendering time
 * - Detects GC pause events (stop-the-world)
 * - Logs when frames drop below 60 FPS (>16ms)
 * - Shows correlation between GC and frame drops
 *
 * Teaching Points:
 * - How to measure GC impact on application performance
 * - Why 16ms is critical for 60 FPS (1000ms / 60 = 16.67ms)
 * - How to detect stop-the-world pauses
 */
public class PerformanceMonitor {
    private static final float TARGET_FRAME_TIME = 0.016f;  // 16ms for 60 FPS
    private static final float SLOW_FRAME_THRESHOLD = 0.033f;  // 33ms (30 FPS)

    private final List<GarbageCollectorMXBean> gcBeans;
    private long lastGcCount = 0;
    private long lastGcTime = 0;

    // Statistics
    private int frameCount = 0;
    private int slowFrameCount = 0;
    private float totalFrameTime = 0;
    private float worstFrameTime = 0;

    // Warning message area (row 21 onwards)
    private static final int WARNING_START_ROW = 21;

    public PerformanceMonitor() {
        // Get GC monitoring beans
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        // Initialize GC baseline
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            lastGcCount += gcBean.getCollectionCount();
            lastGcTime += gcBean.getCollectionTime();
        }
    }

    /**
     * Record frame time and check for performance issues
     *
     * @param frameTime Time taken to render this frame (in seconds)
     */
    public void recordFrame(float frameTime) {
        frameCount++;
        totalFrameTime += frameTime;

        // Track worst frame
        if (frameTime > worstFrameTime) {
            worstFrameTime = frameTime;
        }

        // Detect slow frames
        if (frameTime > SLOW_FRAME_THRESHOLD) {
            slowFrameCount++;
            // Print warning at row 21 (below performance summary)
            System.out.print("\033[21;1H\033[K");  // Move to row 21, clear line
            System.out.print(String.format(
                "⚠️  SLOW FRAME #%d: %.1fms (%.1f FPS) - %s",
                frameCount,
                frameTime * 1000,
                1.0f / frameTime,
                getSlowFrameReason(frameTime)
            ));
            System.out.flush();
        }

        // Check for GC activity
        checkGarbageCollection();
    }

    /**
     * Check if GC occurred and how long it took
     */
    private void checkGarbageCollection() {
        long currentGcCount = 0;
        long currentGcTime = 0;

        for (GarbageCollectorMXBean gcBean : gcBeans) {
            currentGcCount += gcBean.getCollectionCount();
            currentGcTime += gcBean.getCollectionTime();
        }

        // Detect GC pause
        if (currentGcCount > lastGcCount) {
            long gcPauseMs = currentGcTime - lastGcTime;
            // Print GC warning at row 22 (below slow frame warning)
            System.out.print("\033[22;1H\033[K");  // Move to row 22, clear line
            System.out.print(String.format(
                "🗑️  GC PAUSE: %dms (%d collections so far)",
                gcPauseMs,
                currentGcCount
            ));
            System.out.flush();

            lastGcCount = currentGcCount;
            lastGcTime = currentGcTime;
        }
    }

    /**
     * Determine reason for slow frame
     */
    private String getSlowFrameReason(float frameTime) {
        if (frameTime > 0.150f) {
            return "CRITICAL LAG (likely GC pause)";
        } else if (frameTime > 0.050f) {
            return "Major stutter (GC or CPU spike)";
        } else {
            return "Minor lag";
        }
    }

    /**
     * Print performance summary every N frames
     * Displays below HUD (starting at row 11)
     */
    public void printSummary(int everyNFrames) {
        if (frameCount % everyNFrames == 0) {
            float avgFrameTime = totalFrameTime / frameCount;
            float avgFps = 1.0f / avgFrameTime;
            float slowFramePercent = (slowFrameCount * 100.0f) / frameCount;

            // Move cursor to row 11, column 1 (below HUD which ends at row 9)
            System.out.print("\033[11;1H");

            System.out.println("==============================");
            System.out.println("  PERFORMANCE SUMMARY");
            System.out.println("==============================");
            System.out.println(String.format("  Frame: %d", frameCount));
            System.out.println(String.format("  Avg: %.1fms (%.1f FPS)", avgFrameTime * 1000, avgFps));
            System.out.println(String.format("  Worst: %.1fms (%.1f FPS)", worstFrameTime * 1000, 1.0f / worstFrameTime));
            System.out.println(String.format("  Slow frames: %d (%.1f%%)", slowFrameCount, slowFramePercent));
            System.out.println(String.format("  Target: %.1fms (60 FPS)", TARGET_FRAME_TIME * 1000));
            System.out.println("==============================");
            System.out.flush();
        }
    }

    /**
     * Get current frame count
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * Get total GC time so far
     */
    public long getTotalGcTime() {
        return lastGcTime;
    }
}
