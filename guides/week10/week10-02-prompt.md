# Kickstart Prompt for Claude Code

```
Saya ingin refactor branch 10-01 dengan Factory Method pattern untuk solve tight coupling dan OCP violation.

## Context
Week 10 branch 2. Refactor hard-coded spawning menjadi factory-based.

## Factory Method Pattern
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

## File Structure
```
src/
├── WorldController.java       # ✅ Uses factories
├── obstacles/
│   ├── Obstacle.java
│   ├── Spike.java
│   ├── Goblin.java
│   └── Wolf.java
└── factories/
    ├── ObstacleFactory.java   # Abstract
    ├── SpikeFactory.java
    ├── GoblinFactory.java
    └── WolfFactory.java
```

## WorldController.java
```java
public class WorldController {
    private List<ObstacleFactory> factories;
    
    public WorldController() {
        factories = List.of(
            new SpikeFactory(),
            new GoblinFactory(),
            new WolfFactory()
        );
    }
    
    public void spawnRandomObstacle() {
        int index = random(0, factories.size()-1);
        Obstacle obs = factories.get(index).createObstacle();
        activeObstacles.add(obs);
    }
}
```

## Key Benefits
- ✅ WorldController independent of concrete types
- ✅ OCP satisfied
- ✅ Add Boss = 2 new files, 0 modified

## Comments to Add
`// ✅ SOLUTION: Delegates creation to factories`
`// ✅ SOLUTION: Open for extension, closed for modification`

## Testing
Test factory creation, spawning system

Implementasikan dengan fokus pada clean factory implementation!
```
