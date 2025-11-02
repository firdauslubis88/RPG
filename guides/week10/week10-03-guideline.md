# Branch 10-03: Implementation Guidelines

## 📁 File Structure
Same as 10-02, plus performance monitoring.

---

## 🎯 Implementation

### 1. WorldController.java (HIGH SPAWN RATE!)
```java
public class WorldController {
    private List<ObstacleFactory> factories;
    private List<Obstacle> activeObstacles;
    private float spawnTimer = 0;
    private static final float SPAWN_INTERVAL = 0.05f;  // ❌ 20/second!
    
    public void update(float delta) {
        spawnTimer += delta;
        
        // ❌ PROBLEM: Frequent spawning
        if (spawnTimer > SPAWN_INTERVAL) {
            spawnObstacle();
            spawnTimer = 0;
        }
        
        // Update obstacles
        for (Obstacle obs : activeObstacles) {
            obs.update(delta);
        }
        
        // ❌ PROBLEM: Frequent destruction
        activeObstacles.removeIf(obs -> obs.getY() > 10);
    }
    
    private void spawnObstacle() {
        int index = (int)(Math.random() * factories.size());
        Obstacle obs = factories.get(index).createObstacle();  // NEW!
        activeObstacles.add(obs);
    }
}
```

### 2. Performance Monitor
```java
public class PerformanceMonitor {
    private long lastFrameTime;
    private List<Long> frameTimes = new ArrayList<>();
    
    public void startFrame() {
        lastFrameTime = System.nanoTime();
    }
    
    public void endFrame() {
        long elapsed = System.nanoTime() - lastFrameTime;
        long elapsedMs = elapsed / 1_000_000;
        
        frameTimes.add(elapsedMs);
        
        // ❌ Detect lag spike
        if (elapsedMs > 50) {
            System.out.println("⚠️  GC PAUSE: " + elapsedMs + "ms");
        }
    }
    
    public void printStats() {
        long max = frameTimes.stream().max(Long::compare).orElse(0L);
        double avg = frameTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        
        System.out.println("Avg frame: " + avg + "ms");
        System.out.println("Max frame: " + max + "ms");
    }
}
```

### 3. GameEngine Integration
```java
public class GameEngine {
    private PerformanceMonitor perfMon = new PerformanceMonitor();
    
    public void start() {
        while (running) {
            perfMon.startFrame();
            
            update(delta);
            draw();
            
            perfMon.endFrame();
        }
    }
}
```

---

## 🎬 Demo Script

### Stress Test
1. Run game for 60 seconds
2. Observe console output
3. Count GC pause warnings

**Expected Output**:
```
Frame time: 16ms
Frame time: 15ms
⚠️  GC PAUSE: 182ms
Frame time: 16ms
...
⚠️  GC PAUSE: 156ms

=== STATS ===
Avg frame: 18ms
Max frame: 182ms
Objects created: 1200+
```

---

## 📊 Memory Profiling (Optional)

Use VisualVM or JProfiler:
```bash
java -Xmx256m -XX:+PrintGCDetails Main
```

**Observe**:
- Heap sawtooth pattern
- Frequent minor GC
- Periodic major GC (expensive!)

---

## ⚠️ Critical Notes

### DO
- ✅ High spawn rate (20/sec)
- ✅ Log frame times
- ✅ Show GC pauses
- ✅ Demonstrate problem clearly

### DON'T
- ❌ Fix with pooling (next branch!)
- ❌ Reduce spawn rate (defeats demo)

---

## ✅ Checklist
- [ ] Spawn rate: 20/sec
- [ ] Frame time logging works
- [ ] GC pauses detected (>50ms)
- [ ] Students observe stuttering
- [ ] Metrics documented
