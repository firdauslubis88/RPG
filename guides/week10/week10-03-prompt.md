# Kickstart Prompt for Claude Code

```
Saya ingin demonstrate GC performance problem dengan high spawn rate untuk mengajarkan WHY object pooling diperlukan.

## Context
Week 10 branch 3. Game pakai Factory (10-02), sekarang tambah stress test dengan high spawn rate.

## The Problem
```java
// High spawn rate
final float SPAWN_INTERVAL = 0.05f;  // 20/second

void update(float delta) {
    if (spawnTimer > SPAWN_INTERVAL) {
        Obstacle obs = factory.createObstacle();  // NEW!
        activeObstacles.add(obs);
    }
    
    // Destroy off-screen
    activeObstacles.removeIf(obs -> obs.getY() > 10);  // GARBAGE!
}

// Result: 1200+ objects/minute → GC lag!
```

## Performance Monitor
```java
public class PerformanceMonitor {
    public void endFrame() {
        long elapsedMs = calculateFrameTime();
        
        if (elapsedMs > 50) {
            System.out.println("⚠️  GC PAUSE: " + elapsedMs + "ms");
        }
    }
}
```

## Expected Output
```
Frame: 16ms
Frame: 15ms
⚠️  GC PAUSE: 180ms  ← Problem!
Frame: 16ms
```

## Stress Test
- Run 60 seconds
- Spawn rate: 20/second
- Log GC pauses
- Show frame drops

## Comments to Add
`// ❌ PROBLEM: 20 spawns/second = 1200 allocations/minute`
`// ❌ PROBLEM: GC pause causes frame drops`

Implementasikan dengan fokus pada demonstrating GC lag!
```
