# Branch: 10-03-gc-performance-issue

## 🎯 Learning Objective
Memahami MASALAH dari frequent object allocation/deallocation: **Garbage Collection lag** dan **frame drops**.

---

## 📖 Skenario: GC Performance Problem

### Context
Game sudah punya Factory (10-02). Sekarang spawn rate tinggi:
- 20 obstacles per second
- Obstacles destroyed when off-screen
- After 1 minute: 1200+ objects created/destroyed

**Problem**: GC pauses cause frame drops!

### The Performance Bug
```java
// High spawn rate
void update() {
    if (spawnTimer > 0.05f) {  // 20/second
        Obstacle obs = factory.createObstacle();  // NEW object
        activeObstacles.add(obs);
    }
    
    // Destroy off-screen
    activeObstacles.removeIf(obs -> obs.getY() > 10);  // GARBAGE!
}
```

**Result**: After 1 minute
- 1200 objects created
- 1200 objects garbage collected
- GC pause: 150-200ms
- Game freezes!

---

## 🔴 Problems

### Problem 1: Frequent Allocations
**Metrics**:
- Spawn: 20 objects/second
- Frame rate: 60 FPS
- Total: 20 × 60 = 1200 `new` calls per minute

**Impact**: Memory churn, GC pressure

### Problem 2: GC Pause Lag
**Observed**:
```
Frame 0-500: Smooth 60 FPS
Frame 501-502: FREEZE 180ms (GC major collection)
Frame 503+: Smooth again (until next GC)
```

**Player Experience**: Unpredictable stuttering!

### Problem 3: Memory Fragmentation
**Long-term**: Heap fragmented with short-lived objects.

---

## 🧪 Demonstration

### Demo: GC Lag Stress Test

**Setup**:
1. High spawn rate (20/sec)
2. Log frame times
3. Run for 60 seconds

**Expected Results**:
```
Frame 100: 16ms
Frame 200: 16ms
Frame 300: 16ms
Frame 350: 180ms  ← GC PAUSE!
Frame 351: 16ms
...
Frame 700: 150ms  ← GC PAUSE!
```

**Visual**: Game freezes periodically!

**Real-World Parallel**:
- Mobile games on low-end devices
- VR games (GC pause = motion sickness!)
- Competitive games (lag = death)

---

## 📊 Metrics

| Metric | After 1 Minute |
|--------|----------------|
| Objects created | 1200+ |
| Memory allocated | ~75 MB |
| GC pauses | 3-5 times |
| Max pause | 180ms |
| Frame drops | ❌ Unacceptable |

---

## 🎓 Teaching Notes

### Understanding Garbage Collection
**Java GC**:
1. Young Generation (Eden, Survivor) - minor GC
2. Old Generation (Tenured) - major GC

**Problem**: Many short-lived objects → frequent minor GC → promotion to old → expensive major GC

### Why Games Are Sensitive
- Target: 16ms/frame (60 FPS)
- GC pause: 150ms = 9 frames missed!
- Player notices: Any lag > 33ms

### Industry Solutions
- Object Pooling (reuse objects)
- Pre-allocation (allocate upfront)
- GC tuning (advanced)

---

## ✅ Success Criteria
- [ ] Demonstrate GC pauses
- [ ] Log frame time spikes
- [ ] Show memory growth
- [ ] Students observe stuttering

**Next**: 10-04 solves with Object Pool! ♻️
