# Week 10 Branch 10-03: Garbage Collection Performance Problem

## Overview

Branch 10-03 demonstrates the **PROBLEM** with frequent object creation and destruction: **Garbage Collection (GC) lag**. This branch shows what happens when we don't reuse objects in a real-time application like a game.

## The Problem: GC Stop-the-World Pauses

### What We Changed

Starting from the Factory Method solution (10-02), we modified the game to create performance stress:

1. **High Spawn Rate**: 20 obstacles/second (vs 11 static obstacles before)
2. **Off-Screen Destruction**: Objects destroyed when they leave the visible area
3. **No Reuse**: Every obstacle is a brand new object
4. **Performance Monitoring**: Track frame times and GC pauses

### Why This Is a Problem

```
After 10 seconds: 200 obstacles created + destroyed
After 1 minute: 1200 obstacles created + destroyed
After 5 minutes: 6000 obstacles created + destroyed
```

All these destroyed objects become **garbage** that the JVM must collect. When GC runs, it **stops the world** (pauses your application) to clean up memory.

## Key Code Changes

### 1. WorldController: High-Frequency Spawning

**File**: [src/WorldController.java](../src/WorldController.java)

```java
public class WorldController {
    // ❌ PROBLEM: High spawn rate causing GC pressure!
    private float spawnTimer = 0;
    private static final float SPAWN_INTERVAL = 0.05f;  // 20 obstacles/second!
    private static final int OFF_SCREEN_Y = 25;

    public void update(float delta) {
        // ❌ Spawn obstacles at high rate (20/second)
        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnRandomObstacle();  // Creates NEW object!
            spawnTimer = 0;
        }

        // Update obstacles...
        for (Obstacle obstacle : activeObstacles) {
            obstacle.update(delta);
        }

        // ❌ PROBLEM: Frequent destruction when off-screen
        activeObstacles.removeIf(obs ->
            !obs.isActive() || obs.getY() > OFF_SCREEN_Y
        );
    }

    private void spawnRandomObstacle() {
        ObstacleFactory factory = factories.get(random.nextInt(factories.size()));

        // Try to find safe spawn position (max 10 attempts)
        int x = -1, y = -1;
        int attempts = 0;
        while (attempts < 10) {
            int tryX = 1 + random.nextInt(23);  // 1-23 (avoid borders)
            int tryY = 1 + random.nextInt(23);  // 1-23 (avoid borders)

            // ✅ Check if position is SAFE
            if (isSafePosition(tryX, tryY)) {
                x = tryX;
                y = tryY;
                break;
            }
            attempts++;
        }

        // Only spawn if we found a safe position
        if (x != -1 && y != -1) {
            // ❌ Create new object (no pooling!)
            Obstacle newObstacle = factory.createObstacle(x, y);
            activeObstacles.add(newObstacle);
        }
    }

    private boolean isSafePosition(int x, int y) {
        // Check 1: Must be walkable floor (not wall)
        if (!DungeonMap.isWalkable(x, y)) return false;

        // Check 2: Must not be too close to NPC (minimum 3 tiles)
        int distance = Math.abs(x - npc.getX()) + Math.abs(y - npc.getY());
        if (distance < 3) return false;

        // Check 3: Must not overlap with existing obstacles
        for (Obstacle obs : activeObstacles) {
            if (obs.getX() == x && obs.getY() == y) return false;
        }

        return true;  // Safe to spawn here!
    }
}
```

**Problems**:
- `spawnRandomObstacle()` called 20 times per second
- Every call creates a **new** `Spike`, `Goblin`, or `Wolf` object
- When obstacle goes off-screen (Y > 25), it's destroyed
- Destroyed objects become garbage for GC to collect

**Safe Spawning** (to maintain playability):
- Checks if position is walkable floor (not wall)
- Maintains minimum 3-tile distance from NPC (Manhattan distance)
- Avoids overlapping with existing obstacles
- Tries up to 10 random positions before giving up

### 2. PerformanceMonitor: Tracking GC Impact

**File**: [src/PerformanceMonitor.java](../src/PerformanceMonitor.java)

```java
public class PerformanceMonitor {
    private static final float TARGET_FRAME_TIME = 0.016f;  // 16ms for 60 FPS
    private static final float SLOW_FRAME_THRESHOLD = 0.033f;  // 33ms (30 FPS)

    private final List<GarbageCollectorMXBean> gcBeans;

    public void recordFrame(float frameTime) {
        // Track slow frames
        if (frameTime > SLOW_FRAME_THRESHOLD) {
            System.out.println(String.format(
                "⚠️  SLOW FRAME #%d: %.1fms (%.1f FPS)",
                frameCount, frameTime * 1000, 1.0f / frameTime
            ));
        }

        // Check for GC activity
        checkGarbageCollection();
    }

    private void checkGarbageCollection() {
        // Detect GC pause
        if (currentGcCount > lastGcCount) {
            long gcPauseMs = currentGcTime - lastGcTime;
            System.out.println(String.format(
                "🗑️  GC PAUSE: %dms",
                gcPauseMs
            ));
        }
    }
}
```

**What It Monitors**:
- Frame time: How long each frame takes to render
- Slow frames: Frames taking >33ms (below 30 FPS)
- GC pauses: When stop-the-world GC occurs and how long it takes

### 3. GameEngine: Integration

**File**: [src/GameEngine.java](../src/GameEngine.java)

```java
public class GameEngine {
    private final PerformanceMonitor perfMonitor;

    public void start() {
        while (running) {
            long frameStart = System.nanoTime();

            // ... update and draw ...

            // ❌ MEASURE: Calculate frame time
            long frameEnd = System.nanoTime();
            float frameTime = (frameEnd - frameStart) / 1_000_000_000.0f;
            perfMonitor.recordFrame(frameTime);

            // Print performance summary every 60 frames (~1 second)
            perfMonitor.printSummary(60);
        }

        System.out.println(String.format("Total GC time: %dms",
            perfMonitor.getTotalGcTime()));
    }
}
```

## Expected Behavior When Running

When you run the game, you should see console output like this:

```
=================================
  DUNGEON ESCAPE - GC PROBLEM DEMO
  Watch for GC pauses and lag!
=================================

[Game rendering...]

📊 Performance Summary (Frame 60):
   Average: 2.3ms (434.8 FPS)
   Worst: 18.5ms (54.0 FPS)
   Slow frames: 0 (0.0%)
   Target: 16.0ms (60 FPS)

🗑️  GC PAUSE: 12ms (1 collections so far)

📊 Performance Summary (Frame 120):
   Average: 3.1ms (322.6 FPS)
   Worst: 35.2ms (28.4 FPS)
   Slow frames: 2 (1.7%)
   Target: 16.0ms (60 FPS)

⚠️  SLOW FRAME #147: 45.3ms (22.1 FPS) - Major stutter (GC or CPU spike)
🗑️  GC PAUSE: 38ms (2 collections so far)

[more lag spikes...]

=================================
Game ended after 600 frames
Total GC time: 125ms
=================================
```

**What You'll See**:
- Initially smooth performance
- As garbage accumulates, GC pauses increase
- Occasional "SLOW FRAME" warnings when GC hits
- Visible stuttering/lag in game rendering

## Why 60 FPS Matters

```
60 FPS = 1000ms / 60 = 16.67ms per frame
30 FPS = 1000ms / 30 = 33.33ms per frame
```

- **Target**: Each frame must complete in <16ms for 60 FPS
- **Problem**: GC pause of 40ms = **2-3 frames dropped**
- **Result**: Visible stuttering and lag

## The Math Behind the Problem

### Object Creation Rate

```
20 objects/second × 10 seconds = 200 objects
```

Each object:
- `Spike`: ~40 bytes
- `Goblin`: ~60 bytes (with AI state)
- `Wolf`: ~80 bytes (with target tracking)

Average: ~60 bytes per object

### Memory Pressure

```
200 objects × 60 bytes = 12 KB garbage per 10 seconds
1200 objects × 60 bytes = 72 KB garbage per minute
```

This might seem small, but remember:
1. Java creates intermediate objects during rendering
2. String concatenation creates temporary objects
3. ArrayList resizing creates new arrays
4. Each frame creates `int[]` arrays for position tracking

**Real memory pressure**: ~500 KB - 1 MB per minute

### GC Triggering

When young generation (Eden space) fills up:
- Minor GC triggers (fast, 5-15ms)
- If objects survive, promoted to Old Gen
- Eventually triggers Major GC (slow, 50-200ms)

**Major GC = Stop-the-World = Game Freeze**

## Teaching Points

### 1. Real-Time Applications Have Strict Timing Requirements

Games need consistent frame times. A 40ms GC pause is **catastrophic** for user experience.

### 2. Object Creation Isn't Free

Every `new` keyword has a cost:
- Memory allocation
- Object initialization
- Eventually, garbage collection

### 3. GC Is Non-Deterministic

You can't predict **when** GC will run or **how long** it will take. This is dangerous for real-time systems.

### 4. The Solution: Object Pooling

Instead of creating/destroying objects:
1. Create a **pool** of reusable objects
2. **Borrow** from pool when needed
3. **Return** to pool when done
4. **Reuse** the same objects over and over

**Branch 10-04 will demonstrate this solution!**

## Comparison: Before vs Now

| Aspect | Branch 10-02 (Factory) | Branch 10-03 (GC Problem) |
|--------|------------------------|---------------------------|
| **Object Creation** | 11 obstacles (once at start) | 20 obstacles/second |
| **Object Destruction** | Never (static obstacles) | When off-screen (Y > 25) |
| **GC Pressure** | Minimal | High |
| **Frame Rate** | Stable 60 FPS | Occasional drops to <30 FPS |
| **GC Pauses** | Rare | Frequent (every 5-10 seconds) |
| **Total Objects** | 11 | 1200+ after 1 minute |

## How to Observe the Problem

### Run the Demo

```bash
cd bin
java Main
```

Watch the console output for:
- ⚠️ SLOW FRAME warnings
- 🗑️ GC PAUSE notifications
- Increasing "Worst" frame time in summaries

### Run Longer for More GC

Modify `GameEngine.java` to run longer:

```java
// Stop after 3000 frames = ~50 seconds
if (logic.getFrameCount() >= 3000) {
    running = false;
}
```

More objects = more garbage = more GC pauses!

### Monitor with JVM Tools

```bash
# Run with GC logging
java -Xlog:gc* -XX:+PrintGCDetails -XX:+PrintGCTimeStamps Main
```

You'll see detailed GC logs showing:
- Young generation collections
- Old generation collections
- Pause times
- Memory usage

## Next Steps

Branch 10-03 **demonstrates the problem**. The next branch will show the **solution**:

**Branch 10-04**: Object Pool Pattern
- Pre-allocate a pool of obstacles
- Reuse objects instead of creating new ones
- Eliminate GC pressure
- Achieve stable 60 FPS

## Files Modified in This Branch

1. [src/WorldController.java](../src/WorldController.java) - High-frequency spawning
2. [src/PerformanceMonitor.java](../src/PerformanceMonitor.java) - NEW: GC tracking
3. [src/GameEngine.java](../src/GameEngine.java) - Performance monitoring integration

## Key Takeaways

1. **Frequent object creation/destruction causes GC pressure**
2. **GC stop-the-world pauses freeze your application**
3. **Real-time applications cannot tolerate unpredictable pauses**
4. **Object pooling is the solution for high-allocation scenarios**
5. **Measure performance to identify problems** (PerformanceMonitor class)

## Questions for Students

1. Why does GC cause "stop-the-world" pauses?
2. How many objects are created after 1 minute of gameplay?
3. What is the target frame time for 60 FPS?
4. Why is a 40ms GC pause problematic for a game?
5. How would object pooling solve this problem?

---

**Remember**: This branch intentionally demonstrates BAD performance. It's educational! The next branch will fix it with Object Pool pattern.
