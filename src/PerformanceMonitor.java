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
            System.out.println(String.format(
                "⚠️  SLOW FRAME #%d: %.1fms (%.1f FPS) - %s",
                frameCount,
                frameTime * 1000,
                1.0f / frameTime,
                getSlowFrameReason(frameTime)
            ));
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
            System.out.println(String.format(
                "🗑️  GC PAUSE: %dms (%d collections so far)",
                gcPauseMs,
                currentGcCount
            ));

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
     */
    public void printSummary(int everyNFrames) {
        if (frameCount % everyNFrames == 0) {
            float avgFrameTime = totalFrameTime / frameCount;
            float avgFps = 1.0f / avgFrameTime;
            float slowFramePercent = (slowFrameCount * 100.0f) / frameCount;

            System.out.println(String.format(
                "\n📊 Performance Summary (Frame %d):\n" +
                "   Average: %.1fms (%.1f FPS)\n" +
                "   Worst: %.1fms (%.1f FPS)\n" +
                "   Slow frames: %d (%.1f%%)\n" +
                "   Target: %.1fms (60 FPS)\n",
                frameCount,
                avgFrameTime * 1000, avgFps,
                worstFrameTime * 1000, 1.0f / worstFrameTime,
                slowFrameCount, slowFramePercent,
                TARGET_FRAME_TIME * 1000
            ));
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
