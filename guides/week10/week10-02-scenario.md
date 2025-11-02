# Branch: 10-02-with-factory

## 🎯 Learning Objective
Memahami SOLUSI dengan **Factory Method Pattern**: delegasi object creation untuk extensibility.

---

## 📖 Skenario: Factory-Based Spawning

### Architecture Solution
```java
// Abstract factory
public abstract class ObstacleFactory {
    public abstract Obstacle createObstacle();
}

// Concrete factories
public class SpikeFactory extends ObstacleFactory {
    public Obstacle createObstacle() {
        return new Spike();
    }
}

// WorldController uses factories
List<ObstacleFactory> factories = List.of(
    new SpikeFactory(),
    new GoblinFactory(),
    new WolfFactory()
);
Obstacle obs = factories.get(random).createObstacle();
```

---

## ✅ Solutions

### Solution 1: Decoupling
**Fix**: WorldController doesn't know concrete obstacle classes!

**Before**: `import Spike, Goblin, Wolf` (tight coupling)  
**After**: `import ObstacleFactory` only (loose coupling)

### Solution 2: OCP Compliance
**Fix**: Add new obstacle = create 2 NEW files, modify ZERO files.

**Adding Boss**:
1. Boss.java (new)
2. BossFactory.java (new)
3. WorldController.java (unchanged!)

### Solution 3: Team Collaboration
**Fix**: No merge conflicts - each developer works on own factory.

---

## 🧪 Demonstration

### Demo: Add Boss Obstacle
**Files Created**: Boss.java, BossFactory.java  
**Files Modified**: 0 (just register factory in config)

**Discussion**: "Extension without modification!"

---

## 📊 Metrics Comparison

| Aspect | 10-01 (Hard-Coded) | 10-02 (Factory) |
|--------|-------------------|-----------------|
| Coupling | ❌ High | ✅ Low |
| OCP | ❌ Violates | ✅ Follows |
| Files modified | 2+ | 0 |
| Merge conflicts | ❌ High | ✅ Low |

---

## 🎓 Teaching Notes

### Factory Method Pattern
**Intent**: Define interface for creating objects, let subclasses decide which class to instantiate.

**When to Use**:
- ✅ When exact type is unknown at compile time
- ✅ When new types added frequently
- ✅ When OCP compliance needed

**When NOT to Use**:
- ❌ Only 1-2 types (overkill)
- ❌ Types never change (YAGNI)

---

## ✅ Success Criteria
- [ ] WorldController independent of concrete types
- [ ] Adding Boss = 2 new files, 0 modified
- [ ] OCP satisfied
- [ ] All tests passing

**Next**: 10-03 shows GC performance problem! 🚀
