# Branch 10-01: Hard-Coded Spawning (Problem Demonstration)

## Purpose
This branch intentionally demonstrates **hard-coded object creation** - using switch-case statements to spawn obstacles. This is the PROBLEM that the Factory Method pattern solves.

**⚠️ This code is intentionally bad for educational purposes!**

---

## What This Branch Demonstrates

### ❌ Problem 1: Hard-Coded Switch-Case Creation

**Location**: [WorldController.java:78-110](../src/WorldController.java#L78-L110)

```java
private void spawnRandomObstacle() {
    int type = random.nextInt(3);  // 0, 1, or 2
    int x = random.nextInt(10);
    int y = 0;

    Obstacle obstacle = null;

    // ❌ HARD-CODED CREATION PROBLEM!
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
        // ❌ Want to add Boss? Must modify here!
    }

    if (obstacle != null) {
        activeObstacles.add(obstacle);
    }
}
```

**Why This Is Bad:**
1. **Violates Open/Closed Principle** - Must modify WorldController to add new obstacle types
2. **Tight Coupling** - WorldController knows ALL concrete obstacle classes
3. **Merge Conflict Hotspot** - Multiple developers adding obstacles = conflicts
4. **Switch-Case Hell** - Every new type requires case addition

---

### ❌ Problem 2: Instanceof Type Checking

**Location**: [WorldController.java:59-64](../src/WorldController.java#L59-L64)

```java
for (Obstacle obstacle : activeObstacles) {
    obstacle.update(delta);

    // ❌ ANOTHER PROBLEM: instanceof checks (type checking)
    if (obstacle instanceof Wolf) {
        ((Wolf) obstacle).setTarget(npc);
    }
}
```

**Why This Is Bad:**
1. **Type Checking** - Defeats purpose of polymorphism
2. **Fragile** - Must add new instanceof for each special behavior
3. **Violates OCP** - Adding Boss with targeting requires modification

---

### ❌ Problem 3: More Instanceof in GameLogic

**Location**: [GameLogic.java:130-136](../src/GameLogic.java#L130-L136)

```java
// Deactivate obstacle (one-time hit)
if (obstacle instanceof obstacles.Spike) {
    ((obstacles.Spike) obstacle).setActive(false);
} else if (obstacle instanceof obstacles.Goblin) {
    ((obstacles.Goblin) obstacle).setActive(false);
} else if (obstacle instanceof obstacles.Wolf) {
    ((obstacles.Wolf) obstacle).setActive(false);
}
```

**Why This Is Bad:**
1. **Repetitive Code** - Same setActive() pattern for all
2. **Must Extend** - Adding new obstacle requires new else-if
3. **Should Use Polymorphism** - Interface should have setActive()

---

## Implementation Details

### New Files Created

1. **`src/obstacles/Obstacle.java`** - Interface for all obstacles
   - Methods: `update()`, `getX()`, `getY()`, `getDamage()`, `isActive()`, `getSymbol()`

2. **`src/obstacles/Spike.java`** - Static obstacle
   - Damage: 20 HP
   - Symbol: `^`
   - Behavior: No movement

3. **`src/obstacles/Goblin.java`** - Patrol obstacle
   - Damage: 15 HP
   - Symbol: `G`
   - Behavior: Horizontal patrol (reverses at edges)
   - Velocity: 3.0 units/second

4. **`src/obstacles/Wolf.java`** - Chase obstacle
   - Damage: 25 HP
   - Symbol: `W`
   - Behavior: Chases NPC within detection range
   - Speed: 2.5 units/second
   - Detection range: 5.0 units

5. **`src/WorldController.java`** - Manages obstacle spawning (**ANTI-PATTERN**)
   - **❌ Hard-coded switch-case** for spawning
   - **❌ Instanceof checks** for Wolf targeting
   - Spawn interval: 2 seconds

### Modified Files

1. **`src/entities/GameManager.java`**
   - Added `hp` field (starts at 100)
   - Added `getHp()` method
   - Added `takeDamage(int damage)` method
   - Game over when HP reaches 0

2. **`src/GameLogic.java`**
   - Added `WorldController worldController` field
   - Added `updateWorldController(float delta)` method
   - Extended `checkCollisions()` to handle obstacle damage
   - **❌ More instanceof checks** for deactivating obstacles

3. **`src/GameEngine.java`**
   - Added obstacle rendering in `draw()` method
   - Added `updateWorldController()` call in `update()` method
   - Display obstacle count in debug output

4. **`src/HUD.java`**
   - Added HP display (`HP: 100 / 100`)

---

## How to Run

```bash
# Compile
javac -d bin src/**/*.java src/*.java

# Run
cd bin
java Main
```

**Expected Behavior:**
- Obstacles spawn every 2 seconds at random X positions
- Spikes (`^`) stay static
- Goblins (`G`) patrol horizontally
- Wolves (`W`) chase the NPC
- NPC loses HP on collision
- Game over when HP reaches 0

---

## Problems Summary

| Problem | Location | Impact |
|---------|----------|--------|
| Hard-coded switch-case | WorldController:78-110 | Violates OCP, merge conflicts |
| Instanceof type checking | WorldController:59-64 | Defeats polymorphism |
| More instanceof checks | GameLogic:130-136 | Repetitive, fragile |
| Tight coupling | WorldController imports | Knows all concrete types |
| Manual type management | Switch case numbers | Error-prone |

---

## What's Next?

**Branch 10-02** will solve these problems using the **Factory Method Pattern**:
- ✅ No switch-case in WorldController
- ✅ Easy to add new obstacle types
- ✅ No modification needed (Open/Closed Principle)
- ✅ Factory classes handle creation
- ✅ Polymorphism without instanceof

---

## Key Learning Points

1. **Hard-coded creation is bad** - Tight coupling, violates OCP
2. **Switch-case hell** - Merge conflict hotspot, manual numbering
3. **Instanceof indicates design problem** - Should use polymorphism
4. **Open/Closed Principle** - Open for extension, closed for modification
5. **Factory Method pattern** - Delegates object creation to subclasses

---

## Code Statistics

- **Files Modified**: 5
- **Files Created**: 5
- **Lines of Switch-Case**: 17
- **Instanceof Checks**: 7
- **Concrete Classes WorldController Knows**: 3 (Spike, Goblin, Wolf)

**Adding a 4th obstacle (Boss) would require:**
- ❌ Modifying WorldController.spawnRandomObstacle() (case 3)
- ❌ Changing `random.nextInt(3)` to `random.nextInt(4)`
- ❌ Adding instanceof for Boss targeting (if needed)
- ❌ Adding instanceof for Boss collision (if needed)
- ❌ Risk of merge conflicts with teammates

**This demonstrates why Factory Method pattern is needed!**
