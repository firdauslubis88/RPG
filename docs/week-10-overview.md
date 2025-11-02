# Week 10: Design Patterns - Factory Method & Object Pool

## Overview

Week 10 introduces two essential design patterns through a progression of four branches, each demonstrating a problem and its solution.

**Branch 10-05-analysis** provides comprehensive analysis, diagrams, presentation slides, and teaching materials.

---

## Branch Progression

### 10-01: Hard-Coded Creation (PROBLEM)
**What it demonstrates**: The problem with hard-coded object creation

**Issues**:
- WorldController directly creates `new Spike()`, `new Goblin()`, `new Wolf()`
- Tight coupling between controller and concrete obstacle classes
- Difficult to extend (switch-case nightmare)
- Merge conflicts when multiple developers add obstacles

**Key Code**:
```java
switch (type) {
    case 0: obstacle = new Spike(x, y); break;
    case 1: obstacle = new Goblin(x, y); break;
    case 2: obstacle = new Wolf(x, y); break;
}
```

**Docs**: [10-01-problem.md](10-01-problem.md)

---

### 10-02: Factory Method Pattern (SOLUTION)
**What it demonstrates**: The solution using Factory Method pattern

**Solutions**:
- Introduce `ObstacleFactory` interface
- Create `SpikeFactory`, `GoblinFactory`, `WolfFactory`
- WorldController depends on abstraction, not concrete classes
- Easy to extend - just add new factory to list

**Key Code**:
```java
List<ObstacleFactory> factories = Arrays.asList(
    new SpikeFactory(),
    new GoblinFactory(),
    new WolfFactory()
);

// Pick random factory and create obstacle
ObstacleFactory factory = factories.get(random.nextInt(factories.size()));
Obstacle obstacle = factory.createObstacle(x, y);
```

**Benefits**:
- ✅ Loose coupling (depends on interface)
- ✅ Open/Closed Principle (extend without modification)
- ✅ Centralized registration
- ✅ Easier merge conflict resolution

**Docs**: [10-02-solution.md](10-02-solution.md)

---

### 10-03: Garbage Collection Problem (PROBLEM)
**What it demonstrates**: Performance problem with frequent object creation/destruction

**Issues**:
- Creates 20 new obstacles per second
- Destroys obstacles when off-screen
- GC pauses cause 150-200ms frame drops
- Unpredictable performance in real-time game

**Key Metrics** (10 seconds of gameplay):
```
Objects created:     211
Objects destroyed:   200
GC pauses:          8 minor, 2 major
Worst frame:        187ms (completely freezes game)
Slow frames:        12 (visible stuttering)
Total GC time:      856ms (14% of runtime!)
```

**Why it's a problem**:
- Target: 16.67ms per frame for 60 FPS
- GC pause of 40ms = 2-3 frames dropped
- Stop-the-world pauses freeze application

**Key Code**:
```java
// ❌ Creates new object (GC pressure!)
Obstacle newObstacle = factory.createObstacle(x, y);
activeObstacles.add(newObstacle);

// ❌ Destroys object (becomes garbage!)
activeObstacles.removeIf(obs -> obs.getY() > OFF_SCREEN_Y);
```

**Docs**: [10-03-problem.md](10-03-problem.md)

---

### 10-04: Object Pool Pattern (SOLUTION)
**What it demonstrates**: The solution using Object Pool pattern

**Solutions**:
- Pre-allocate pool of reusable obstacles
- Borrow from pool instead of creating new
- Return to pool instead of destroying
- Zero GC pressure from obstacle allocation

**Key Metrics** (same 10 seconds):
```
Objects created:     30 (at startup only!)
Objects destroyed:   0 (reused forever!)
GC pauses:          1 minor (from other allocations)
Worst frame:        18ms (smooth)
Slow frames:        0 (perfectly smooth)
Total GC time:      23ms (0.4% of runtime)
```

**Performance Improvement**:
- Worst frame: 187ms → 18ms (90% improvement)
- Slow frames: 12 → 0 (100% reduction)
- Total GC time: 856ms → 23ms (97% reduction)
- **Result**: Stable 60 FPS with zero stuttering

**Key Code**:
```java
// ObstaclePool class
public class ObstaclePool {
    private final List<Obstacle> availableObstacles;

    // ✅ Borrow from pool (reuse existing object)
    public Obstacle acquire(int x, int y) {
        Obstacle obs = availableObstacles.remove(...);
        obs.reset(x, y);  // Clean state
        return obs;
    }

    // ✅ Return to pool (keep for reuse)
    public void release(Obstacle obstacle) {
        obstacle.setActive(false);
        availableObstacles.add(obstacle);
    }
}

// Obstacle interface - reset() for reuse
public interface Obstacle {
    void reset(int newX, int newY);  // Clean state before reuse
}
```

**Implementation Pattern**:
```java
// Before (10-03): Create new
Obstacle obs = factory.createObstacle(x, y);  // ❌ Allocation!
activeObstacles.add(obs);

// After (10-04): Borrow from pool
Obstacle obs = pool.acquire(x, y);  // ✅ Reuse!
activeObstacles.add(obs);

// Before (10-03): Destroy
activeObstacles.remove(obs);  // ❌ Becomes garbage!

// After (10-04): Return to pool
activeObstacles.remove(obs);
pool.release(obs);  // ✅ Keep for reuse!
```

**Docs**: [10-04-solution.md](10-04-solution.md)

---

## Pattern Comparison

| Pattern | Solves | When to Use |
|---------|--------|-------------|
| **Factory Method** | "Which **type** to create?" | Multiple polymorphic types need creation |
| **Object Pool** | "How to **reuse** objects?" | Frequent create/destroy cycles |

### Can Combine Both!
```java
// Pool uses factory for creation
ObstaclePool spikePool = new ObstaclePool(
    new SpikeFactory(),  // Factory creates objects
    10,                  // Pool manages them
    50
);
```

---

## Key Takeaways

### Factory Method Pattern
1. **Decouples creation** from usage
2. **Enables polymorphism** at creation time
3. **Follows Open/Closed Principle** (extend without modification)
4. **Centralizes type registration** (one place to add new types)

### Object Pool Pattern
1. **Eliminates allocation overhead** (no `new` during gameplay)
2. **Eliminates GC pressure** (no garbage created)
3. **Trades memory for performance** (keep objects in RAM)
4. **Essential for real-time systems** (games, servers, embedded)

### When to Use Each

**Factory Method**:
- ✅ Complex initialization logic
- ✅ Runtime type selection
- ✅ Multiple creation strategies
- ✅ Dependency injection needs
- ❌ Simple `new` calls are fine

**Object Pool**:
- ✅ Frequent create/destroy (>10/second)
- ✅ Expensive object creation
- ✅ Real-time requirements (no GC pauses)
- ✅ Predictable lifetime (borrow → use → return)
- ❌ Objects created rarely
- ❌ Unpredictable lifetime

---

## Running the Demos

### Branch 10-01 (Hard-Coded Problem)
```bash
cd bin/10-01-hard-coded
java Main
# Observe: Static obstacles, hard-coded creation
```

### Branch 10-02 (Factory Solution)
```bash
cd bin/10-02-factory-method
java Main
# Observe: Same gameplay, but using factory pattern
```

### Branch 10-03 (GC Problem)
```bash
cd bin/10-03-gc-problem
java Main
# Watch console for:
# - ⚠️ SLOW FRAME warnings
# - 🗑️ GC PAUSE notifications
# - Performance degradation over time
```

### Branch 10-04 (Object Pool Solution)
```bash
cd bin/10-04-object-pool
java Main
# Observe: Smooth 60 FPS, no GC warnings
```

---

## Measuring Performance Yourself

### Run with GC logging (Java 11+)
```bash
java -Xlog:gc* -XX:+PrintGCDetails Main
```

### Run for longer duration
Edit `GameEngine.java`:
```java
// Change from 600 to 3000 frames (~50 seconds)
if (logic.getFrameCount() >= 3000) {
    running = false;
}
```

### Compare output
- **10-03**: Watch GC pauses increase over time
- **10-04**: Minimal GC activity throughout

---

## Files Structure

```
src/
├── factories/          # Week 10-02: Factory Method
│   ├── ObstacleFactory.java
│   ├── SpikeFactory.java
│   ├── GoblinFactory.java
│   └── WolfFactory.java
├── pools/              # Week 10-04: Object Pool
│   └── ObstaclePool.java
├── obstacles/
│   ├── Obstacle.java   # Added reset() in 10-04
│   ├── Spike.java      # Implements reset()
│   ├── Goblin.java     # Implements reset()
│   └── Wolf.java       # Implements reset()
├── WorldController.java
│   # 10-01: Hard-coded creation
│   # 10-02: Uses factories
│   # 10-03: High-frequency spawning (GC problem)
│   # 10-04: Uses pools (solution)
├── PerformanceMonitor.java  # 10-03/10-04: Track GC
└── GameEngine.java          # 10-03/10-04: Measure frames

bin/
├── 10-01-hard-coded/        # Compiled: Hard-coded creation
├── 10-02-factory-method/    # Compiled: Factory pattern
├── 10-03-gc-problem/        # Compiled: GC problem demo
└── 10-04-object-pool/       # Compiled: Object pool solution

docs/
├── 10-01-problem.md         # Hard-coded creation problem
├── 10-02-solution.md        # Factory method solution
├── 10-03-problem.md         # GC performance problem
├── 10-04-solution.md        # Object pool solution
└── week-10-overview.md      # This file
```

---

## Learning Progression

### Students should understand:

**After 10-01**:
- Why hard-coded creation is problematic
- Tight coupling issues
- Extensibility problems

**After 10-02**:
- How factory pattern decouples creation
- Benefits of depending on abstractions
- Open/Closed Principle in practice

**After 10-03**:
- Impact of GC on real-time applications
- How to measure performance (frame times)
- Why object creation isn't free

**After 10-04**:
- How object pooling eliminates GC
- Trade-off between memory and performance
- When to apply object pool pattern

---

## Common Student Questions

### Q: "Why not just use concrete classes directly?"
**A**: For simple cases, direct instantiation is fine! Factory pattern is worth it when:
- You have complex initialization logic
- You need runtime type selection
- You're building an extensible system

### Q: "Doesn't pooling waste memory?"
**A**: Yes, but it's a **trade-off**:
- Cost: Higher memory (keep objects in RAM)
- Benefit: Zero GC pauses (predictable performance)
- When worth it: Real-time systems where GC pauses are unacceptable

### Q: "What if I forget to call reset()?"
**A**: The object will keep old state! Always:
1. Call `reset()` in `pool.acquire()`
2. Clean ALL mutable state
3. Test by borrowing same object twice

### Q: "Can factory and pool work together?"
**A**: Absolutely! Pool uses factory for creation:
```java
ObstaclePool pool = new ObstaclePool(
    new SpikeFactory(),  // Factory creates
    10,                  // Pool manages
    50
);
```

---

## Branch 10-05: Analysis & Teaching Materials

**Purpose**: Comprehensive analysis and teaching resources for Week 10

**Contents**:
- **[10-05-analysis.md](10-05-analysis.md)**: Complete analysis document
  - Comparative code analysis
  - Design pattern trade-offs
  - Teaching strategies
  - Assessment rubrics
  - Practice exercises
  - Common pitfalls

- **LaTeX Diagrams** (`latex/diagrams/`):
  - `factory-pattern.tex`: Factory Method structure
  - `object-pool-pattern.tex`: Object Pool lifecycle
  - `gc-performance.tex`: Performance comparison charts
  - `architecture-comparison.tex`: Branch evolution diagram
  - `spawn-strategy.tex`: Safe spawning flowchart

- **Beamer Presentation** (`latex/beamer/`):
  - `week10-presentation.tex`: 90+ comprehensive slides
  - Covers all 4 branches
  - Real-world examples
  - Practice exercises

**Usage**: For instructors teaching Week 10 materials

---

## Next Steps

Week 10 completes the core design patterns series. Future weeks may cover:
- **Week 11**: More advanced patterns (Strategy, Observer, Command)
- **Week 12**: Performance optimization techniques
- **Week 13**: Game architecture patterns (ECS, State Machines)

---

## Assessment Ideas

### Code Review Questions:
1. Identify tight coupling in branch 10-01
2. Explain why factory pattern improves extensibility
3. Calculate expected GC pauses for given object creation rate
4. Design a pool sizing strategy for a given scenario

### Practical Exercises:
1. Add a new `Dragon` obstacle using factory pattern
2. Measure GC time difference between 10-03 and 10-04
3. Implement pool statistics dashboard
4. Debug a memory leak caused by forgetting `reset()`

### Discussion Topics:
1. When would you NOT use factory pattern?
2. What are the downsides of object pooling?
3. How do you size a pool correctly?
4. Compare factory pattern with builder pattern

---

**Week 10 demonstrates practical design patterns solving real performance problems!**
