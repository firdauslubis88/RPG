# Week 10: World Building & Object Management

## 🎯 Week Goals

### Primary Learning Objectives
1. **Understand Factory Method Pattern**
   - Flexible object creation
   - Delegation of instantiation
   - Open/Closed Principle in practice

2. **Understand Object Pool Pattern**
   - Performance optimization through reuse
   - Memory management in games
   - GC pause reduction

### Secondary Learning Objectives
- Dynamic spawning systems
- Enemy behavior patterns (static, patrol, chase)
- Performance profiling and metrics
- Memory leak prevention

---

## 🎮 Game State at End of Week 10

### What Works
✅ All Week 9 features (game loop, singleton)
✅ **NEW**: Dynamic obstacle spawning system
✅ **NEW**: Multiple enemy types:
   - Spike: Static obstacle (damage on touch)
   - Goblin: Patrol pattern (moves left-right)
   - Wolf: Chase behavior (follows NPC if in range)
✅ **NEW**: Object pooling for performance
✅ **NEW**: Smooth 60 FPS even with many entities

### What Still Doesn't Work
❌ No player control (NPC still automated)
❌ No input handling system
❌ No event system
❌ No difficulty scaling
❌ No boss battle

**Why**: Player control requires Command pattern (Week 11). This week focuses on world building and performance.

---

## 📚 Patterns Covered

### 1. Factory Method Pattern

**Category**: Creational Pattern
**GoF Classification**: Class Creational

#### The Problem
```java
// ❌ Hard-coded obstacle creation
switch(type) {
    case 0: obstacle = new Spike();
    case 1: obstacle = new Goblin();
    case 2: obstacle = new Wolf();
}
// WorldController terikat pada semua concrete classes!
```

**Pain Points**:
- WorldController harus tahu semua obstacle types
- Adding new obstacle = modify WorldController
- Violates Open/Closed Principle
- Merge conflicts in team development

#### The Solution
```java
// ✅ Delegated creation
public abstract class ObstacleFactory {
    public abstract Obstacle createObstacle();
}

public class SpikeFactory extends ObstacleFactory {
    public Obstacle createObstacle() {
        return new Spike();
    }
}
```

#### Why This Matters
- **Extensibility**: Add new obstacles without modifying existing code
- **Testability**: Can mock factories easily
- **Team collaboration**: No merge conflicts
- **Industry standard**: Used in all major game engines

#### Real-World Examples
- Unity Prefab system
- Unreal Engine Blueprint spawning
- Minecraft block/entity factories

---

### 2. Object Pool Pattern

**Category**: Creational Pattern (not in GoF, but essential for games)
**Origin**: Game programming community

#### The Problem
```java
// ❌ Frequent allocation/deallocation
for (int i = 0; i < 100; i++) {
    Bullet bullet = new Bullet();  // Expensive!
    activeBullets.add(bullet);
}
// Later: bullets go offscreen
activeBullets.remove(bullet);  // Now it's garbage!
```

**Pain Points**:
- 100 bullets × 60 FPS = 6000 allocations/second
- Garbage collection pauses (50-200ms)
- Frame rate stuttering
- Memory fragmentation

#### The Solution
```java
// ✅ Reuse instead of recreate
ObjectPool<Bullet> bulletPool = new ObjectPool<>(bulletFactory);

// Acquire from pool (reuse if available)
Bullet bullet = bulletPool.acquire();

// Return to pool when done (not destroyed!)
bulletPool.release(bullet);
```

#### Why This Matters
- **Performance**: 99% reduction in allocations
- **Smooth gameplay**: No GC pauses
- **Predictable behavior**: Consistent frame time
- **Industry requirement**: All AAA games use pooling

#### Real-World Examples
- Unity Object Pooling
- Unreal Engine Object Pool
- Android Bitmap recycling
- Database connection pools

---

## 🌳 Branch Roadmap

### Branch 10-01: hardcoded-spawning
**Type**: Problem demonstration
**Purpose**: Show why hard-coded creation is bad

**What You'll See**:
```java
// Switch-case hell
switch(randomType) {
    case 0: obstacle = new Spike(); break;
    case 1: obstacle = new Goblin(); break;
    case 2: obstacle = new Wolf(); break;
    // Want to add Missile? MODIFY THIS FILE!
}
```

**Pain Points**:
- WorldController knows ALL obstacle types
- Adding Missile = modify WorldController
- 2 developers adding obstacles = merge conflict
- Violates OCP

**Demonstration**:
- Task: "Add Boss obstacle"
- Count files modified: WorldController + Boss class
- Risk: Typo in switch breaks existing obstacles

---

### Branch 10-02: with-factory
**Type**: Solution
**Purpose**: Show Factory Method implementation

**What You'll See**:
```java
// Clean delegation
List<ObstacleFactory> factories = List.of(
    new SpikeFactory(),
    new GoblinFactory(),
    new WolfFactory()
);

Obstacle obs = factories.get(random).createObstacle();
```

**Benefits**:
- WorldController independent of concrete types
- Adding Missile = 2 NEW files (no modification!)
- No merge conflicts
- OCP satisfied

**Demonstration**:
- Task: "Add Boss obstacle"
- Count files modified: ZERO in WorldController!
- Create: Boss.java + BossFactory.java only

---

### Branch 10-03: gc-performance-issue
**Type**: Problem demonstration
**Purpose**: Show GC lag problem

**What You'll See**:
- High spawn rate (20 obstacles/second)
- Obstacles destroyed when offscreen
- After 1 minute: FREEZE for 150ms (GC pause!)

**Metrics Demonstrated**:
```
Frame 100: 45 obstacles, 25MB memory
Frame 200: 90 obstacles, 48MB memory
Frame 300: 135 obstacles, 72MB memory
Frame 350: FREEZE 180ms! ← GC cleanup
Frame 351: 15 obstacles, 12MB memory
```

**Pain Points**:
- Game stutters unpredictably
- Player dies during GC pause
- Unplayable experience
- Professional game = unacceptable

**Real-World Parallel**:
- Mobile games on low-end devices
- VR games (GC pause = motion sickness!)
- Competitive games (lag = death)

---

### Branch 10-04: with-pool
**Type**: Solution
**Purpose**: Show Object Pool + Factory synergy

**What You'll See**:
```java
// Pool uses Factory to create when needed
ObjectPool<Obstacle> pool = new ObjectPool<>(factory);

// Acquire (reuse or create)
Obstacle obs = pool.acquire();

// Release (return to pool, not destroy)
pool.release(obs);
```

**Benefits**:
- 99% reduction in allocations
- No GC pauses (< 10ms)
- Stable frame time
- Professional performance

**Metrics After Pooling**:
```
Frame 100-1000: Stable 60 FPS
Memory: Flat at ~15MB
GC pauses: 0ms-5ms (imperceptible)
Objects created: ~20 (startup), then 0
```

---

### Branch 10-analysis
**Type**: Comparison & metrics
**Purpose**: Data-driven analysis

**Contents**:
1. **Factory Method Analysis**
   - LOC comparison (with vs without)
   - Extensibility metrics
   - Team collaboration benefits
   - When to use / when not to

2. **Object Pool Analysis**
   - Performance metrics (frame time)
   - Memory usage graphs
   - GC pause comparison
   - Trade-offs (complexity vs performance)

3. **Synergy Analysis**
   - How Pool + Factory work together
   - Why both needed
   - Real-world examples

4. **Migration Guides**
   - Step-by-step refactoring
   - Testing strategies
   - Common pitfalls

---

## 📊 Success Metrics

### For Students
By end of Week 10, students should:
- [ ] Explain WHY Factory Method is needed
- [ ] Implement Factory Method correctly
- [ ] Explain performance implications of `new`
- [ ] Understand GC pause problem
- [ ] Implement Object Pool correctly
- [ ] Understand Pool + Factory synergy
- [ ] Discuss trade-offs of both patterns

### For Code
- [ ] Spawning system extensible (add obstacle without modifying WorldController)
- [ ] Stable 60 FPS with 50+ obstacles
- [ ] No GC pauses > 10ms
- [ ] Memory usage stable (no leaks)
- [ ] All tests passing

---

## 🧪 Demonstration Scenarios

### Demo 1: Extensibility Problem
**Setup**: Branch 10-01
**Task**: "Add Missile obstacle"
**Observe**: Must modify WorldController switch-case
**Learning**: "Every new type = modify existing code!"

### Demo 2: Factory Solution
**Setup**: Branch 10-02
**Task**: "Add Boss obstacle"
**Observe**: Create 2 new files, WorldController unchanged
**Learning**: "Extension without modification!"

### Demo 3: GC Lag Problem
**Setup**: Branch 10-03
**Action**: Run stress test (high spawn rate)
**Observe**: Periodic freezes (150ms+), frame drops
**Metrics**: Log frame times, show spikes
**Learning**: "Frequent new/delete = GC lag!"

### Demo 4: Pool Solution
**Setup**: Branch 10-04
**Action**: Same stress test
**Observe**: Smooth 60 FPS, no freezes
**Metrics**: Flat frame time graph
**Learning**: "Reuse = performance!"

---

## 🎓 Teaching Notes

### Key Concepts to Emphasize

#### 1. Open/Closed Principle (OCP)
"Software entities should be open for extension, but closed for modification."

**Before Factory**:
```
Add obstacle → Modify WorldController → Risk breaking existing
```

**After Factory**:
```
Add obstacle → Create new files only → Existing code untouched
```

#### 2. Object Creation Cost
Not all `new` calls are equal:

**Cheap**:
- Primitives (int, float)
- Small objects (Point, Vector2)

**Expensive**:
- Large objects (Game entities)
- Objects with resources (textures, sounds)
- Objects with complex initialization

#### 3. Garbage Collection Basics
```
Young Generation → Minor GC (fast, frequent)
Old Generation → Major GC (slow, rare)
```

Problem: Many short-lived objects = frequent minor GC = frame drops

#### 4. Pool Design Considerations
- **Initial size**: Pre-allocate common cases
- **Max size**: Prevent unlimited growth
- **Reset**: Must clear state when released
- **Memory leak**: Forgot to release = leak!

---

### Common Student Questions

**Q**: "Kenapa tidak pakai C++ manual memory management?"
**A**: Java = automatic GC (safer, easier). Tapi dengan pooling, kita get performance benefit without C++ complexity. Best of both worlds.

**Q**: "Apakah semua object harus di-pool?"
**A**: Tidak! Only high-frequency create/destroy. Contoh: bullets yes, player no (created once).

**Q**: "Bagaimana jika lupa call release()?"
**A**: Memory leak! Pool will keep creating new objects. Need discipline atau use weak references (advanced).

**Q**: "Factory Method vs Abstract Factory?"
**A**: Factory Method = satu produk. Abstract Factory = family of products (Week 12 bonus jika ada waktu).

**Q**: "Apakah Factory Pattern sama dengan Factory Method Pattern?"
**A**: Factory Method = GoF pattern (subclass decides). Factory Pattern = informal term (any creation encapsulation).

---

## 📦 Deliverables

### Student Deliverables
- [ ] Factory-based spawning system
- [ ] Object pool implementation
- [ ] Performance benchmark (before/after)
- [ ] Unit tests for factories and pools
- [ ] Reflection document on trade-offs

### Teaching Material Deliverables
- [ ] All 5 branches implemented
- [ ] SCENARIO/GUIDELINE/PROMPT for each
- [ ] Performance profiling screenshots
- [ ] Analysis with metrics
- [ ] Common mistakes FAQ

---

## ⏱️ Time Allocation

### Lecture (2 hours)
- 20 min: Factory Method explanation
- 10 min: Demo problem (10-01)
- 10 min: Demo solution (10-02)
- 20 min: Object Pool explanation
- 15 min: Demo GC problem (10-03)
- 15 min: Demo Pool solution (10-04)
- 10 min: Synergy discussion
- 20 min: Q&A and trade-offs

### Lab (3 hours)
- 1.5 hour: Implement Factory Method
- 1.5 hour: Implement Object Pool
- Testing and benchmarking throughout

### Homework (6 hours)
- Profile existing code for GC issues
- Implement pooling where appropriate
- Write performance analysis report
- Prepare optimization suggestions

---

## 🔗 Connections to Other Weeks

### Builds on Week 9
- Uses GameManager (Singleton) for spawning
- Uses game loop for entity updates
- Uses separated update/render

### Prepares for Week 11
- Spawned obstacles will interact with Player (Command)
- Collisions will trigger events (Observer)
- Foundation for complex interactions

### Prepares for Week 12
- Factory will be used by Difficulty strategies
- Pool will be used across all game states
- Performance optimization enables complex features

---

## 📚 Additional Resources

### Recommended Reading
- "Game Programming Patterns" - Object Pool chapter
- "Effective Java" - Item 1: Static factory methods
- Unity Manual: Object Pooling
- "Java Performance" by Scott Oaks - GC tuning

### Profiling Tools
- VisualVM (heap dump analysis)
- JProfiler (commercial, comprehensive)
- Java Mission Control (included with JDK)
- Simple logging (System.currentTimeMillis)

### Industry Examples
- Unity Particle System pooling
- Android RecyclerView ViewHolder pattern
- Apache Commons Pool
- HikariCP (database connection pool)

---

## ✅ Week 10 Checklist

Before moving to Week 11:
- [ ] Spawning system works with factories
- [ ] Can add new obstacle without modifying WorldController
- [ ] Object pool reduces GC pauses measurably
- [ ] No memory leaks (monitor with profiler)
- [ ] Performance metrics documented
- [ ] Students understand WHY (not just HOW)
- [ ] Students can explain trade-offs
- [ ] All tests passing
- [ ] All documentation complete

---

**Note**: Week 10 is performance-focused. Make sure students see ACTUAL performance improvements with metrics, not just "trust me it's faster"! 📊🚀
