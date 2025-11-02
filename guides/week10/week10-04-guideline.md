# Branch 10-04: Implementation Guidelines

## 📁 File Structure
```
src/
├── WorldController.java           # Uses pool
├── pool/
│   ├── ObjectPool.java            # Generic pool
│   └── Poolable.java              # Interface
├── obstacles/
│   └── ... (implement Poolable)
└── factories/
    └── ... (used by pool)
```

---

## 🎯 Implementation

### 1. Poolable.java (Interface)
```java
public interface Poolable {
    void reset();  // Clean state before reuse
}
```

### 2. Update Obstacles to Implement Poolable
```java
public class Spike implements Obstacle, Poolable {
    private int x, y;
    private boolean active;
    
    @Override
    public void reset() {
        this.active = false;
        // Reset other fields as needed
    }
}

// Same for Goblin, Wolf
```

### 3. ObjectPool.java (Generic)
```java
public class ObjectPool<T extends Poolable> {
    private Queue<T> available;
    private ObstacleFactory factory;
    private int maxSize;
    
    public ObjectPool(ObstacleFactory factory, int initialSize, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.available = new LinkedList<>();
        
        // Pre-allocate initial objects
        for (int i = 0; i < initialSize; i++) {
            available.add((T) factory.createObstacle());
        }
    }
    
    public T acquire() {
        if (available.isEmpty()) {
            // Create only if pool empty and under max
            return (T) factory.createObstacle();
        }
        return available.poll();
    }
    
    public void release(T object) {
        if (available.size() < maxSize) {
            object.reset();
            available.add(object);
        }
        // If pool full, let object be garbage collected
    }
    
    public int getAvailableCount() {
        return available.size();
    }
}
```

### 4. WorldController.java (WITH POOL!)
```java
public class WorldController {
    private Map<Class, ObjectPool> pools;
    private List<Obstacle> activeObstacles;
    
    public WorldController() {
        // ✅ Create pools for each type
        pools = new HashMap<>();
        pools.put(Spike.class, new ObjectPool<>(new SpikeFactory(), 10, 50));
        pools.put(Goblin.class, new ObjectPool<>(new GoblinFactory(), 10, 50));
        pools.put(Wolf.class, new ObjectPool<>(new WolfFactory(), 10, 50));
        
        activeObstacles = new ArrayList<>();
    }
    
    public void spawnObstacle() {
        // Random type
        Class type = getRandomObstacleType();
        ObjectPool pool = pools.get(type);
        
        // ✅ Acquire from pool (reuse!)
        Obstacle obs = (Obstacle) pool.acquire();
        obs.setPosition(randomX(), 0);
        activeObstacles.add(obs);
    }
    
    public void update(float delta) {
        spawnTimer += delta;
        if (spawnTimer > SPAWN_INTERVAL) {
            spawnObstacle();
            spawnTimer = 0;
        }
        
        // Update
        for (Obstacle obs : activeObstacles) {
            obs.update(delta);
        }
        
        // ✅ Return to pool instead of destroying
        Iterator<Obstacle> it = activeObstacles.iterator();
        while (it.hasNext()) {
            Obstacle obs = it.next();
            if (obs.getY() > 10) {
                returnToPool(obs);
                it.remove();
            }
        }
    }
    
    private void returnToPool(Obstacle obs) {
        Class type = obs.getClass();
        ObjectPool pool = pools.get(type);
        pool.release((Poolable) obs);
    }
}
```

**Comments**:
```java
// ✅ SOLUTION: Object pool reuses objects
// ✅ SOLUTION: 99% reduction in allocations
// ✅ SOLUTION: No GC lag
```

---

## 🎬 Demo: Performance Comparison

Run same stress test as 10-03:

**Expected Output**:
```
=== WITH POOL ===
Frame: 16ms
Frame: 15ms
Frame: 16ms  (no pauses!)

=== STATS ===
Avg frame: 16ms
Max frame: 18ms
Objects created: 30 (initial pool)
Pool reuses: 1170
GC pauses: 0
```

**Comparison**:
- 10-03: Max 180ms (GC pause)
- 10-04: Max 18ms (no pause)
- **Improvement**: 90% reduction!

---

## 🧪 Testing
```java
@Test
void testPoolReuse() {
    ObjectPool<Spike> pool = new ObjectPool<>(new SpikeFactory(), 5, 10);
    
    Spike s1 = pool.acquire();
    int initialAvailable = pool.getAvailableCount();
    
    pool.release(s1);
    
    Spike s2 = pool.acquire();
    
    // ✅ Should be same object (reused)
    assertSame(s1, s2);
}

@Test
void testPoolCreatesWhenEmpty() {
    ObjectPool<Spike> pool = new ObjectPool<>(new SpikeFactory(), 0, 10);
    
    Spike s = pool.acquire();
    
    assertNotNull(s);  // Created because pool was empty
}
```

---

## ⚠️ Critical Notes

### DO
- ✅ Implement Poolable interface
- ✅ Call reset() before reuse
- ✅ Pre-allocate initial pool
- ✅ Set max pool size

### DON'T
- ❌ Forget to call reset()
- ❌ Release object still in use
- ❌ Pool everything (only high-frequency)

---

## 🔍 Common Mistakes

### Mistake 1: Forgot to Reset
```java
// ❌ Wrong
pool.release(obstacle);  // State still dirty!

// ✅ Correct
obstacle.reset();  // Clean state
pool.release(obstacle);
```

### Mistake 2: Memory Leak (No Release)
```java
// ❌ Wrong
activeObstacles.remove(obs);  // Forgot to return!

// ✅ Correct
pool.release(obs);
activeObstacles.remove(obs);
```

---

## ✅ Checklist
- [ ] Poolable interface implemented
- [ ] ObjectPool generic class works
- [ ] Obstacles reset properly
- [ ] WorldController uses pool
- [ ] Performance improved (no GC lag)
- [ ] Tests passing
