# Kickstart Prompt for Claude Code

```
Saya ingin implement Object Pool pattern untuk solve GC lag problem dari branch 10-03.

## Context
Week 10 branch 4. Tambah object pooling untuk eliminate GC pauses.

## Object Pool Pattern
```java
public class ObjectPool<T extends Poolable> {
    private Queue<T> available;
    private Factory<T> factory;
    
    public T acquire() {
        if (available.isEmpty()) {
            return factory.create();  // Create if needed
        }
        return available.poll();  // Reuse!
    }
    
    public void release(T object) {
        object.reset();
        available.add(object);
    }
}

// Usage
Obstacle obs = pool.acquire();  // Reuse or create
// ... use ...
pool.release(obs);  // Return, NOT destroy!
```

## File Structure
```
src/
├── pool/
│   ├── ObjectPool.java       # Generic pool
│   └── Poolable.java         # reset() interface
├── obstacles/
│   └── ... (implement Poolable)
└── WorldController.java      # Uses pools
```

## Poolable Interface
```java
public interface Poolable {
    void reset();  // Clean state for reuse
}
```

## WorldController Changes
```java
Map<Class, ObjectPool> pools;

void init() {
    pools.put(Spike.class, new ObjectPool<>(new SpikeFactory(), 10, 50));
}

void spawn() {
    Obstacle obs = pool.acquire();  // Reuse!
}

void destroy(Obstacle obs) {
    pool.release(obs);  // Return!
}
```

## Expected Results
- Objects created: ~30 (vs 1200 before)
- GC pauses: 0 (vs 3-5 before)
- Max frame: 18ms (vs 180ms before)
- Smooth 60 FPS!

## Comments
`// ✅ SOLUTION: Pool reuses objects`
`// ✅ SOLUTION: 99% reduction in allocations`

## Testing
Test acquire/release, reuse behavior

Implementasikan dengan fokus pada demonstrating performance improvement!
```
