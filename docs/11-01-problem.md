# Branch 11-01: Hardcoded Input (ANTI-PATTERN)

## Overview
This branch demonstrates the **problem with hardcoded input handling** before introducing the Command Pattern.

## What's New in Week 11

### Major Changes
- **Player-Controlled Character**: NPC is replaced by Player (@)
- **Keyboard Input**: WASD controls for player movement
- **Entity Interface**: Common interface for Player and NPC
- **Player Stats**: Health, score, collision detection
- **Double Buffering**: Reduces flickering during rendering
- **Obstacle Collision**: Obstacles cannot overlap each other
- **Balanced Gameplay**: Wolf speed 1.0, spawn rate 2/sec

### New Files
- `src/entities/Player.java` - Player character with health and score
- `src/entities/Entity.java` - Interface for positioned entities
- `src/input/InputHandler.java` - ❌ ANTI-PATTERN: Hardcoded input handling

### Modified Files
- `src/GameLogic.java` - Uses Player, collision detection
- `src/GameEngine.java` - Double buffering, player input
- `src/WorldController.java` - Obstacle collision detection
- `src/obstacles/Wolf.java` - Chases Entity (reduced speed 1.0)
- `src/entities/Coin.java` - Added value and symbol
- `src/utils/GridRenderer.java` - Double buffering support
- `src/HUD.java` - Integrated with buffering system

### Windows Console Limitations
- **Buffered Input**: Requires Enter key after WASD (System.in limitation)
- **Input Echo**: May be visible depending on terminal
- **No Raw Input**: Cannot achieve unbuffered input without JNI
- **Real Games**: Use native libraries or game engines for input

## The Problem: Hardcoded Input

### InputHandler.java (ANTI-PATTERN)
```java
public void handleInput() {
    // ❌ HARDCODED KEY BINDINGS
    if (key == 'w' || key == 'W') {
        player.moveUp();        // W hardcoded to moveUp
    }
    else if (key == 's' || key == 'S') {
        player.moveDown();      // S hardcoded to moveDown
    }
    else if (key == 'a' || key == 'A') {
        player.moveLeft();      // A hardcoded to moveLeft
    }
    else if (key == 'd' || key == 'D') {
        player.moveRight();     // D hardcoded to moveRight
    }
}
```

### Problems Demonstrated

#### 1. **Tight Coupling**
```java
// InputHandler MUST know about Player class
private Player player;

// InputHandler MUST know all Player methods
player.moveUp();
player.moveDown();
player.moveLeft();
player.moveRight();
```

**Issue**: If Player interface changes, InputHandler must change too.

#### 2. **Cannot Remap Keys**
```java
// User wants to remap W to Space?
// Must modify handleInput() method!

// Want IJKL layout instead of WASD?
// Duplicate entire if-else chain!

// Want Arrow keys option?
// More duplication and complexity!
```

**Issue**: Configuration becomes exponentially complex.

#### 3. **Cannot Undo/Redo**
```java
// How to implement undo?
// No command history available!

// How to replay player actions?
// No way to store/serialize actions!
```

**Issue**: No command history means no undo capability.

#### 4. **God Class Problem**
```java
// Future additions make it worse:
if (key == SPACE) player.attack();
if (key == E) player.interact();
if (key == I) uiManager.openInventory();  // Now knows UI!
if (key == M) mapSystem.toggleMap();      // Now knows map!
if (key == ESC) menuSystem.pause();       // Now knows menu!
// 50+ lines of if-else!
```

**Issue**: InputHandler becomes god class that knows everything.

#### 5. **Testing Difficulty**
```java
// How to test moveUp action independently?
// Must mock entire InputHandler!

// How to test with different key bindings?
// Must modify source code!
```

**Issue**: Actions cannot be tested in isolation.

## Demonstration Tasks

### Task 1: Try to Add Key Remapping
**Goal**: Allow user to remap W key to Space for jump.

**Current Approach (Complex)**:
1. Add configuration storage
2. Check config in EVERY if statement
3. Handle conflicts (what if Space already used?)
4. Exponential complexity with number of actions

**What You'll Realize**: This design doesn't scale!

### Task 2: Try to Implement Undo
**Goal**: Add undo feature (Ctrl+Z).

**Current Approach (Impossible)**:
1. No command history
2. Actions execute immediately
3. No way to store "what was done"

**What You'll Realize**: Need Command Pattern!

### Task 3: Try to Add Macro
**Goal**: Record sequence "WWAD" and replay with one key.

**Current Approach (Very Complex)**:
1. Record key presses (not actions!)
2. Replay might fail if context changed
3. No way to compose actions

**What You'll Realize**: Need command objects!

## How to Run

### Compile
```bash
cd c:\repos\oop\rpg
javac -d bin/11-01-hardcoded-input -cp src src/*.java src/entities/*.java src/obstacles/*.java src/factories/*.java src/pools/*.java src/utils/*.java src/world/*.java src/input/*.java
```

### Run
```bash
cd bin/11-01-hardcoded-input
java Main
```

### Controls
- **W + Enter**: Move up
- **S + Enter**: Move down
- **A + Enter**: Move left
- **D + Enter**: Move right
- **Q + Enter**: Quit

**Note**: Windows console requires Enter key after each command.

### Gameplay
- Control the player (@)
- Collect coins ($) for points (10 points each)
- Avoid obstacles:
  - **^** Spike (10 damage, stationary)
  - **G** Goblin (15 damage, patrols)
  - **W** Wolf (25 damage, chases player)
- Wolves chase you within 5-tile detection range!
- Watch health in HUD (starts at 100 HP)
- Game over when HP reaches 0

## Teaching Points

### For Students

**Before Trying Task 1 (Remapping)**:
"How would you implement key remapping with this design?"

**After Seeing the Complexity**:
"This is why we need Command Pattern - actions as objects!"

### Key Questions to Ask

1. **Q**: "What if different players want different key layouts?"
   **A**: Current design requires code modification for each layout.

2. **Q**: "How would you implement undo?"
   **A**: Impossible without command history.

3. **Q**: "What if we want AI to use same actions as player?"
   **A**: AI would need to simulate key presses (awkward!).

4. **Q**: "How to test player.moveUp() independently?"
   **A**: Cannot test without InputHandler.

### Learning Outcomes

After this branch, students should understand:
- ✅ Why hardcoded input handling is inflexible
- ✅ Why tight coupling makes extension difficult
- ✅ Why god classes violate Single Responsibility
- ✅ Why we need to encapsulate actions as objects
- ✅ The motivation for Command Pattern

## Metrics

### Code Complexity
- **InputHandler**: 50+ lines of if-else (will grow)
- **Cyclomatic Complexity**: High (many branches)
- **Coupling**: Tight (knows Player, will know more)

### Extensibility Issues
- **Add action**: Modify InputHandler ❌
- **Remap key**: Modify InputHandler ❌
- **Add layout**: Duplicate code ❌
- **Undo/Redo**: Not possible ❌

## Next: Branch 11-02 (Solution)

Branch 11-02 will solve these problems with **Command Pattern**:
- ✅ Actions as objects
- ✅ Easy key remapping
- ✅ Command history for undo
- ✅ Loose coupling
- ✅ Easy testing

---

**Key Takeaway**: Hardcoded input = inflexible, tightly coupled, untestable. Command Pattern solves all these issues!
