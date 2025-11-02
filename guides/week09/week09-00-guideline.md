# Branch 09-00: Implementation Guidelines for Claude Code

## 📁 File Structure
```
src/
├── Main.java                      # Monolithic game loop
├── entities/
│   ├── NPC.java                  # Auto-moving entity
│   └── Coin.java                 # Falling coin
└── utils/
    └── GridRenderer.java         # Static print utilities
```

---

## 🎯 Implementation Requirements

### 1. Entity Classes (Pure Data)

#### NPC.java
**Purpose**: Auto-moving character (no player control yet).

**Fields**: 
- `int x, y` - Position
- `int speed` - Pixels per frame (fixed = 1)

**Methods**: 
- `void moveRight()` - x += speed
- Getters/setters

**Behavior**: Auto-moves right, wraps at edge.

---

#### Coin.java
**Purpose**: Collectible falling item.

**Fields**:
- `int x, y` - Position
- `int fallSpeed` - Pixels per frame (fixed = 1)
- `boolean collected` - State

**Methods**:
- `void fall()` - y += fallSpeed
- `void respawn(int newX)` - Reset to top
- Getters/setters

---

### 2. Main.java (Monolithic Loop)

**INTENTIONALLY BAD DESIGN** untuk demonstration.

#### Key Anti-Patterns
1. **Mixed Concerns**: Update → draw immediately
2. **No Delta Time**: Fixed Thread.sleep()
3. **Global State**: All in main() locals
4. **No Methods**: One giant while loop
5. **Side Effects**: System.out in logic

#### Pseudo-structure
```
main() {
    NPC npc = ...
    Coin coin = ...
    int score = 0
    
    while(true) {
        // ❌ Update NPC
        npc.moveRight()
        
        // ❌ IMMEDIATELY draw (mixed!)
        GridRenderer.drawEntity('N', npc.x, npc.y)
        
        // ❌ Update coin
        coin.fall()
        
        // ❌ IMMEDIATELY draw (mixed!)
        GridRenderer.drawEntity('C', coin.x, coin.y)
        
        // ❌ Collision + score + draw (all mixed!)
        if (collision) {
            score += 10
            coin.respawn()
            System.out.println("Score: " + score)
        }
        
        Thread.sleep(100)
    }
}
```

#### Critical Notes
- **DO NOT** create separate methods
- **DO NOT** use delta time
- **DO** make messy (this is the point!)
- **DO** add `// ❌ PROBLEM:` comments

---

### 3. GridRenderer.java

**Purpose**: Terminal printing utility.

**Methods**:
- `static void clearScreen()` - Newlines or ANSI clear
- `static void drawEntity(char symbol, int x, int y)` - Print at coord
- Grid 10x10, use '░' for empty

**Output**:
```
░░░░░░░░░░
░░░░░C░░░░  ← Coin (5,1)
░░░░░░░░░░
░N░░░░░░░░  ← NPC (1,3)
░░░░░░░░░░

Score: 0
```

---

## 🧪 Testing Requirements

### Demonstrate Untestability

**MainTest.java**:
```java
@Test
void testCollisionDetection() {
    // ❌ Cannot extract logic
    // ❌ Cannot mock terminal
    // ❌ Requires visual check
    
    fail("Impossible to write!");
}
```

**Purpose**: Show testing is impossible.

---

## 🎬 Demonstration Script

### Demo 1: Artificial Lag
1. Add after each draw:
   ```java
   GridRenderer.drawEntity(...);
   Thread.sleep(100);
   ```

2. Run game

3. **Observe**: 
   - Should: 10 px/sec
   - Actually: 5 px/sec
   - 50% slower!

**Discussion**:
- "Ini baru 1 entity. Bayangkan 100?"
- "PC lambat = gameplay lambat?"

### Demo 2: Multiple Entities
1. Create 10 coins
2. Loop all: update → draw → sleep
3. **Observe**: 1 second per frame!

**Discussion**:
- "Real game punya ratusan entities"
- "Bagaimana scale ini?"

---

## 📝 Documentation

### PROBLEM.md
```markdown
# Problems in Branch 09-00

## 1. Frame Rate Coupling
- Render delay = logic slower
- 50% performance loss
- Unfair across hardware

## 2. Cannot Test
- No unit tests possible
- Manual testing only
- No CI/CD

## 3. Poor Maintainability
- 150+ lines in main()
- Logic mixed with view
- Hard to debug

## Next: 09-01-with-game-loop
```

---

## ⚠️ Critical for Claude Code

### DO
1. ✅ Make intentionally messy
2. ✅ Mix update/render
3. ✅ Fixed sleep, no delta
4. ✅ Everything in main()
5. ✅ Add problem comments

### DON'T
1. ❌ Optimize or clean
2. ❌ Separate concerns
3. ❌ Use best practices
4. ❌ Make testable

**Remember**: Show problems, not solutions!
