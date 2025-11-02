# Branch 10-01: Implementation Guidelines

## 📁 File Structure
```
src/
├── WorldController.java           # ❌ Hard-coded creation
├── obstacles/
│   ├── Obstacle.java              # Base interface
│   ├── Spike.java
│   ├── Goblin.java
│   └── Wolf.java
└── ... (game engine from Week 9)
```

---

## 🎯 Implementation Requirements

### 1. Obstacle.java (Interface)
```java
public interface Obstacle {
    void update(float delta);
    int getX();
    int getY();
    int getDamage();
    boolean isActive();
}
```

### 2. Concrete Obstacles

**Spike.java** (Static):
```java
public class Spike implements Obstacle {
    private int x, y;
    private int damage = 20;
    
    public Spike(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void update(float delta) {
        // Static - no movement
    }
    
    // Getters...
}
```

**Goblin.java** (Patrol):
```java
public class Goblin implements Obstacle {
    private float x, y;
    private float velocity = 1.5f;
    private int direction = 1;  // 1=right, -1=left
    
    public void update(float delta) {
        x += velocity * direction * delta;
        if (x > 9 || x < 0) direction *= -1;  // Reverse
    }
}
```

**Wolf.java** (Chase):
```java
public class Wolf implements Obstacle {
    private float x, y;
    private float speed = 2.0f;
    
    public void update(float delta) {
        // Chase logic (simplified)
        NPC npc = getNPCPosition();
        if (distanceTo(npc) < 3) {
            moveTowards(npc, delta);
        }
    }
}
```

---

### 3. WorldController.java (HARD-CODED!)

```java
public class WorldController {
    private List<Obstacle> activeObstacles;
    
    public void spawnRandomObstacle() {
        int type = (int)(Math.random() * 3);
        int x = (int)(Math.random() * 10);
        int y = 0;
        
        Obstacle obstacle = null;
        
        // ❌ PROBLEM: Hard-coded creation!
        switch(type) {
            case 0: 
                obstacle = new Spike(x, y); 
                break;
            case 1: 
                obstacle = new Goblin(x, y); 
                break;
            case 2: 
                obstacle = new Wolf(x, y); 
                break;
            // Want to add Missile? MUST MODIFY HERE!
        }
        
        activeObstacles.add(obstacle);
    }
}
```

**Comments to Add**:
```java
// ❌ PROBLEM: WorldController knows all obstacle types
// ❌ PROBLEM: Adding new type requires modifying this file
// ❌ PROBLEM: Violates Open/Closed Principle
```

---

## 🎬 Demonstration Script

### Demo: Add Boss Obstacle

**Step 1**: Create Boss.java
```java
public class Boss implements Obstacle {
    // ... implementation
}
```

**Step 2**: Modify WorldController
```java
switch(type) {
    case 0: obstacle = new Spike(x, y); break;
    case 1: obstacle = new Goblin(x, y); break;
    case 2: obstacle = new Wolf(x, y); break;
    case 3: obstacle = new Boss(x, y); break;  // NEW LINE
}
```

**Count**: 2 files modified (Boss.java + WorldController.java)

**Discussion**: "What if 5 developers add obstacles? All modify same file!"

---

## ⚠️ Critical Notes

### DO (For Problem Demo)
- ✅ Use switch-case (show the problem!)
- ✅ Import all obstacle classes
- ✅ Make adding new type painful
- ✅ Add comments marking problems

### DON'T
- ❌ Use Factory pattern (that's next branch!)
- ❌ Make it extensible
- ❌ Solve the problem

---

## ✅ Success Checklist
- [ ] WorldController has switch-case
- [ ] All obstacle classes imported
- [ ] Adding Boss requires modification
- [ ] Comments mark OCP violation

**Next**: 10-02 fixes with Factory Method! 🏭
