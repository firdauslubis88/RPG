# Branch: 10-04-with-pool

## 🎯 Learning Objective
Memahami SOLUSI dengan **Object Pool Pattern**: reuse objects untuk eliminate GC lag.

---

## 📖 Skenario: Object Pool Solution

### Architecture Solution
```java
public class ObjectPool<T> {
    private Queue<T> available;
    private Factory<T> factory;
    
    public T acquire() {
        if (available.isEmpty()) {
            return factory.create();  // Create only if needed
        }
        return available.poll();  // Reuse!
    }
    
    public void release(T object) {
        object.reset();  // Clean state
        available.add(object);  // Return to pool
    }
}

// Usage
Obstacle obs = pool.acquire();  // Reuse or create
// ... use obstacle ...
pool.release(obs);  // Return to pool, NOT destroy!
```

---

## ✅ Solutions

### Solution 1: Eliminate Allocations
**Before**: 1200 `new` calls/minute  
**After**: ~20 `new` calls (initial pool), then 0

**Reduction**: 99% fewer allocations!

### Solution 2: No GC Pauses
**Before**: GC pause every ~20 seconds (150-200ms)  
**After**: GC pause < 10ms (imperceptible)

**Result**: Stable 60 FPS!

### Solution 3: Predictable Performance
**Before**: Sawtooth memory, unpredictable lag  
**After**: Flat memory usage, consistent frame time

---

## 🧪 Demonstration

### Demo: Same Stress Test, No Lag

**Setup**: Same as 10-03 (20 spawns/sec, 60 sec)

**Results**:
```
Frame: 16ms
Frame: 15ms
Frame: 16ms  ← No GC pauses!
...
=== STATS ===
Avg: 16ms
Max: 18ms (vs 180ms before!)
Objects created: 20 (vs 1200 before!)
```

**Visual**: Smooth gameplay, no stuttering!

---

## 📊 Metrics Comparison

| Metric | 10-03 (No Pool) | 10-04 (With Pool) | Improvement |
|--------|-----------------|-------------------|-------------|
| Objects created (1 min) | 1200+ | ~20 | 98% reduction |
| GC pauses | 3-5 (150ms+) | 0-1 (<10ms) | 95% reduction |
| Max frame time | 180ms | 18ms | 90% reduction |
| Memory pattern | ❌ Sawtooth | ✅ Flat | Stable |
| Frame drops | ❌ Frequent | ✅ None | Perfect |

---

## 🎓 Teaching Notes

### Object Pool Pattern
**Intent**: Reuse expensive objects instead of creating new ones.

**Structure**:
1. Pool maintains available objects
2. acquire() = get from pool (or create)
3. release() = return to pool (reset state)

**When to Use**:
- ✅ High-frequency create/destroy
- ✅ Expensive object creation
- ✅ Predictable max count

**When NOT to Use**:
- ❌ Low-frequency objects
- ❌ Cheap objects (int, Point)
- ❌ Objects with complex lifecycle

### Synergy: Factory + Pool
```java
ObjectPool<Obstacle> pool = new ObjectPool<>(
    obstacleFactory  // Pool uses Factory to create!
);
```

**Benefits**: Best of both patterns!

---

## ✅ Success Criteria
- [ ] Pool implementation works
- [ ] 99% reduction in allocations
- [ ] No GC pauses >10ms
- [ ] Stable 60 FPS
- [ ] Memory usage flat

**Next**: 10-analysis compares all approaches! 📊
