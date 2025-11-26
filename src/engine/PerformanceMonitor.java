package engine;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * PerformanceMonitor - Tracks frame times and detects GC pauses
 */
public class PerformanceMonitor {
    private static final float TARGET_FRAME_TIME = 0.016f;
    private static final float SLOW_FRAME_THRESHOLD = 0.033f;

    private final List<GarbageCollectorMXBean> gcBeans;
    private long lastGcCount = 0;
    private long lastGcTime = 0;

    private int frameCount = 0;
    private int slowFrameCount = 0;
    private float totalFrameTime = 0;
    private float worstFrameTime = 0;

    private static final int WARNING_START_ROW = 20;
    private static final int WARNING_START_COL = 28;

    public PerformanceMonitor() {
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gcBean : gcBeans) {
            lastGcCount += gcBean.getCollectionCount();
            lastGcTime += gcBean.getCollectionTime();
        }
    }

    public void recordFrame(float frameTime) {
        frameCount++;
        totalFrameTime += frameTime;

        if (frameTime > worstFrameTime) {
            worstFrameTime = frameTime;
        }

        if (frameTime > SLOW_FRAME_THRESHOLD) {
            slowFrameCount++;
            System.out.print(String.format("\033[%d;%dH\033[K", WARNING_START_ROW, WARNING_START_COL));
            System.out.print(String.format(
                "SLOW FRAME #%d: %.1fms (%.1f FPS)",
                frameCount, frameTime * 1000, 1.0f / frameTime));
            System.out.flush();
        }

        checkGarbageCollection();
    }

    private void checkGarbageCollection() {
        long currentGcCount = 0;
        long currentGcTime = 0;

        for (GarbageCollectorMXBean gcBean : gcBeans) {
            currentGcCount += gcBean.getCollectionCount();
            currentGcTime += gcBean.getCollectionTime();
        }

        if (currentGcCount > lastGcCount) {
            long gcPauseMs = currentGcTime - lastGcTime;
            System.out.print(String.format("\033[%d;%dH\033[K", WARNING_START_ROW + 1, WARNING_START_COL));
            System.out.print(String.format("GC PAUSE: %dms (%d collections)", gcPauseMs, currentGcCount));
            System.out.flush();

            lastGcCount = currentGcCount;
            lastGcTime = currentGcTime;
        }
    }

    public void printSummary(int everyNFrames) {
        if (frameCount % everyNFrames == 0) {
            float avgFrameTime = totalFrameTime / frameCount;
            float avgFps = 1.0f / avgFrameTime;
            float slowFramePercent = (slowFrameCount * 100.0f) / frameCount;

            int startCol = 28;
            int startRow = 11;

            System.out.print(String.format("\033[%d;%dH", startRow, startCol));
            System.out.print("╔════════════════════════════╗");
            System.out.print(String.format("\033[%d;%dH", startRow + 1, startCol));
            System.out.print("║   PERFORMANCE SUMMARY      ║");
            System.out.print(String.format("\033[%d;%dH", startRow + 2, startCol));
            System.out.print("╠════════════════════════════╣");
            System.out.print(String.format("\033[%d;%dH", startRow + 3, startCol));
            System.out.print(String.format("║  Frame: %-19d║", frameCount));
            System.out.print(String.format("\033[%d;%dH", startRow + 4, startCol));
            System.out.print(String.format("║  Avg: %.1fms (%.1f FPS)%s║",
                avgFrameTime * 1000, avgFps, getPadding(avgFrameTime * 1000, avgFps)));
            System.out.print(String.format("\033[%d;%dH", startRow + 5, startCol));
            System.out.print(String.format("║  Worst: %.1fms (%.1f FPS)%s║",
                worstFrameTime * 1000, 1.0f / worstFrameTime,
                getPadding(worstFrameTime * 1000, 1.0f / worstFrameTime)));
            System.out.print(String.format("\033[%d;%dH", startRow + 6, startCol));
            System.out.print(String.format("║  Slow: %d (%.1f%%)%s║",
                slowFrameCount, slowFramePercent, getPaddingForSlow(slowFrameCount, slowFramePercent)));
            System.out.print(String.format("\033[%d;%dH", startRow + 7, startCol));
            System.out.print(String.format("║  Target: %.1fms (60 FPS)   ║", TARGET_FRAME_TIME * 1000));
            System.out.print(String.format("\033[%d;%dH", startRow + 8, startCol));
            System.out.print("╚════════════════════════════╝");
            System.out.flush();
        }
    }

    private String getPadding(float timeMs, float fps) {
        String content = String.format("%.1fms (%.1f FPS)", timeMs, fps);
        int padding = 22 - content.length();
        return " ".repeat(Math.max(0, padding));
    }

    private String getPaddingForSlow(int count, float percent) {
        String content = String.format("%d (%.1f%%)", count, percent);
        int padding = 16 - content.length();
        return " ".repeat(Math.max(0, padding));
    }

    public int getFrameCount() { return frameCount; }
    public long getTotalGcTime() { return lastGcTime; }
}
