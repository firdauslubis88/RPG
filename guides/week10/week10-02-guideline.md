# Branch 10-02: Implementation Guidelines

## 📁 File Structure
```
src/
├── WorldController.java
├── obstacles/
│   ├── Obstacle.java
│   ├── Spike.java
│   ├── Goblin.java
│   └── Wolf.java
└── factories/
    ├── ObstacleFactory.java      # Abstract
    ├── SpikeFactory.java
    ├── GoblinFactory.java
    └── WolfFactory.java
```

---

## 🎯 Implementation

### 1. ObstacleFactory.java (Abstract)
```java
public abstract class ObstacleFactory {
    public abstract Obstacle createObstacle();
    
    // Optional: Factory method with parameters
    public Obstacle createObstacle(int x, int y) {
        Obstacle obs = createObstacle();
        obs.setPosition(x, y);
        return obs;
    }
}
```

### 2. Concrete Factories
```java
public class SpikeFactory extends ObstacleFactory {
    @Override
    public Obstacle createObstacle() {
        return new Spike();
    }
}

public class GoblinFactory extends ObstacleFactory {
    @Override
    public Obstacle createObstacle() {
        return new Goblin();
    }
}

public class WolfFactory extends ObstacleFactory {
    @Override
    public Obstacle createObstacle() {
        return new Wolf();
    }
}
```

### 3. WorldController.java (CLEAN!)
```java
public class WorldController {
    private List<ObstacleFactory> factories;
    private List<Obstacle> activeObstacles;
    
    public WorldController() {
        // ✅ Register factories
        this.factories = List.of(
            new SpikeFactory(),
            new GoblinFactory(),
            new WolfFactory()
        );
        this.activeObstacles = new ArrayList<>();
    }
    
    public void spawnRandomObstacle() {
        int index = (int)(Math.random() * factories.size());
        int x = (int)(Math.random() * 10);
        int y = 0;
        
        // ✅ Delegate creation to factory
        Obstacle obstacle = factories.get(index).createObstacle(x, y);
        activeObstacles.add(obstacle);
    }
}
```

**Comments**:
```java
// ✅ SOLUTION: WorldController independent of concrete types
// ✅ SOLUTION: Add new type = create new factory only
// ✅ SOLUTION: OCP satisfied (extension without modification)
```

---

## 🎬 Demo: Add Boss

**Step 1**: Boss.java
```java
public class Boss implements Obstacle {
    // ... implementation
}
```

**Step 2**: BossFactory.java
```java
public class BossFactory extends ObstacleFactory {
    public Obstacle createObstacle() {
        return new Boss();
    }
}
```

**Step 3**: Register (config/main)
```java
factories.add(new BossFactory());
```

**Files Modified in WorldController**: 0!

---

## 🧪 Testing
```java
@Test
void testFactoryCreation() {
    ObstacleFactory factory = new SpikeFactory();
    Obstacle obs = factory.createObstacle();
    
    assertTrue(obs instanceof Spike);
}

@Test
void testSpawningUsesFactory() {
    WorldController controller = new WorldController();
    controller.spawnRandomObstacle();
    
    assertEquals(1, controller.getActiveObstacles().size());
}
```

---

## ⚠️ Critical Notes

### DO
- ✅ Use abstract factory
- ✅ Delegate creation
- ✅ Make WorldController independent

### DON'T
- ❌ Put concrete types in WorldController
- ❌ Use switch-case

---

## ✅ Checklist
- [ ] Abstract ObstacleFactory exists
- [ ] Each obstacle has factory
- [ ] WorldController uses List<Factory>
- [ ] Adding Boss = 2 new files only
- [ ] Tests passing
