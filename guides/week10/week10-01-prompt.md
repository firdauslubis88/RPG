# Kickstart Prompt for Claude Code

```
Saya ingin membuat demo spawning system dengan hard-coded creation untuk mengajarkan masalah tight coupling dan OCP violation. Code ini SENGAJA bad design.

## Context
Week 10 branch 1 - "Dungeon Escape". Game sudah punya game loop + singleton. Sekarang tambah obstacle spawning dengan HARD-CODED creation.

## Obstacle Types
1. **Spike**: Static (x,y fixed)
2. **Goblin**: Patrol (moves left-right)
3. **Wolf**: Chase (follows NPC)

## File Structure
```
src/
├── WorldController.java      # ❌ Switch-case creation
├── obstacles/
│   ├── Obstacle.java         # Interface
│   ├── Spike.java
│   ├── Goblin.java
│   └── Wolf.java
```

## WorldController.java (THE PROBLEM!)
```java
public void spawnRandomObstacle() {
    int type = random(0, 2);
    
    Obstacle obstacle = null;
    
    // ❌ PROBLEM: Hard-coded!
    switch(type) {
        case 0: obstacle = new Spike(x, y); break;
        case 1: obstacle = new Goblin(x, y); break;
        case 2: obstacle = new Wolf(x, y); break;
    }
    
    activeObstacles.add(obstacle);
}
```

## Obstacle Interface
```java
public interface Obstacle {
    void update(float delta);
    int getX();
    int getY();
    int getDamage();
}
```

## Concrete Classes
- **Spike**: No movement, damage=20
- **Goblin**: Patrol (velocity=1.5, reverse at edges)
- **Wolf**: Chase NPC if distance < 3

## Anti-Patterns Required
1. ❌ Switch-case in WorldController
2. ❌ Import all obstacle classes
3. ❌ Cannot extend without modification

## Comments to Add
`// ❌ PROBLEM: Tight coupling to concrete classes`
`// ❌ PROBLEM: Violates Open/Closed Principle`

## Demonstration
Task: Add Boss obstacle
Result: Must modify WorldController switch-case

Implementasikan dengan fokus pada showing OCP violation!
```
