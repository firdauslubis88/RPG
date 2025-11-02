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

    // Warning message area (row 36 onwards, below performance summary)
    private static final int WARNING_START_ROW = 36;
    private static final int WARNING_START_COL = 28;  // Same column as HUD

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
            // Print warning below performance summary (row 36, col 28)
            System.out.print(String.format("\033[%d;%dH\033[K", WARNING_START_ROW, WARNING_START_COL));
            System.out.print(String.format(
                "⚠️  SLOW FRAME #%d: %.1fms (%.1f FPS)",
                frameCount,
                frameTime * 1000,
                1.0f / frameTime
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
            // Print GC warning below slow frame warning (row 37, col 28)
            System.out.print(String.format("\033[%d;%dH\033[K", WARNING_START_ROW + 1, WARNING_START_COL));
            System.out.print(String.format(
                "🗑️  GC PAUSE: %dms (%d collections)",
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
     * Displays below HUD (at column 28, starting at row 27)
     */
    public void printSummary(int everyNFrames) {
        if (frameCount % everyNFrames == 0) {
            float avgFrameTime = totalFrameTime / frameCount;
            float avgFps = 1.0f / avgFrameTime;
            float slowFramePercent = (slowFrameCount * 100.0f) / frameCount;

            int startCol = 28;  // Same column as HUD
            int startRow = 27;  // Below map (which ends at row 25)

            // Move cursor to row 27, column 28 (below HUD, same column alignment)
            System.out.print(String.format("\033[%d;%dH", startRow, startCol));
            System.out.println("╔════════════════════════════╗");

            System.out.print(String.format("\033[%d;%dH", startRow + 1, startCol));
            System.out.println("║   PERFORMANCE SUMMARY      ║");

            System.out.print(String.format("\033[%d;%dH", startRow + 2, startCol));
            System.out.println("╠════════════════════════════╣");

            System.out.print(String.format("\033[%d;%dH", startRow + 3, startCol));
            System.out.println(String.format("║  Frame: %-19d║", frameCount));

            System.out.print(String.format("\033[%d;%dH", startRow + 4, startCol));
            System.out.println(String.format("║  Avg: %.1fms (%.1f FPS)%s║",
                avgFrameTime * 1000, avgFps,
                getPadding(avgFrameTime * 1000, avgFps)));

            System.out.print(String.format("\033[%d;%dH", startRow + 5, startCol));
            System.out.println(String.format("║  Worst: %.1fms (%.1f FPS)%s║",
                worstFrameTime * 1000, 1.0f / worstFrameTime,
                getPadding(worstFrameTime * 1000, 1.0f / worstFrameTime)));

            System.out.print(String.format("\033[%d;%dH", startRow + 6, startCol));
            System.out.println(String.format("║  Slow: %d (%.1f%%)%s║",
                slowFrameCount, slowFramePercent,
                getPaddingForSlow(slowFrameCount, slowFramePercent)));

            System.out.print(String.format("\033[%d;%dH", startRow + 7, startCol));
            System.out.println(String.format("║  Target: %.1fms (60 FPS)   ║", TARGET_FRAME_TIME * 1000));

            System.out.print(String.format("\033[%d;%dH", startRow + 8, startCol));
            System.out.println("╚════════════════════════════╝");

            System.out.flush();
        }
    }

    /**
     * Calculate padding for frame time display to maintain box alignment
     */
    private String getPadding(float timeMs, float fps) {
        // Format: "Avg: 2.3ms (434.8 FPS)"
        String content = String.format("%.1fms (%.1f FPS)", timeMs, fps);
        int contentLen = content.length();
        int targetLen = 22; // Space available in the box
        int padding = targetLen - contentLen;
        return " ".repeat(Math.max(0, padding));
    }

    /**
     * Calculate padding for slow frame display
     */
    private String getPaddingForSlow(int count, float percent) {
        String content = String.format("%d (%.1f%%)", count, percent);
        int contentLen = content.length();
        int targetLen = 16; // Space available
        int padding = targetLen - contentLen;
        return " ".repeat(Math.max(0, padding));
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
