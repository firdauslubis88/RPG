# Week 11-02: Command Pattern (SOLUTION)

**Branch:** `11-02-with-command`

**Teaching Focus:** Command Pattern as solution to hardcoded input handling

---

## ✅ SOLUTION: Command Pattern for Flexible Input

This branch demonstrates the **Command Pattern** as the solution to the hardcoded input problem from branch 11-01.

### The Command Pattern

**Definition:** Encapsulate a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations.

**Key Components:**
1. **Command Interface** - Defines `execute()` and `undo()` methods
2. **Concrete Commands** - Implement specific actions (MoveUpCommand, MoveDownCommand, etc.)
3. **Invoker** - InputHandler that executes commands
4. **Receiver** - Player that performs the actual action

### Problem Solved

**Before (11-01):**
```java
// ❌ Hardcoded if-else chain
if (key == 'w') {
    player.moveUp();
} else if (key == 's') {
    player.moveDown();
}
// ... 52 lines of hardcoded keys!
```

**After (11-02):**
```java
// ✅ HashMap-based lookup
Command command = keyBindings.get(key);
if (command != null) {
    command.execute();
}
// Just 5 lines!
```

### Implementation

#### Command Interface (`commands/Command.java`)
```java
public interface Command {
    void execute();
    void undo();
    String getName();
}
```

#### Concrete Command Example (`commands/MoveUpCommand.java`)
```java
public class MoveUpCommand implements Command {
    private Player player;
    private int oldX, oldY;

    public MoveUpCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        oldX = player.getX();
        oldY = player.getY();
        player.moveUp();
    }

    @Override
    public void undo() {
        player.setPosition(oldX, oldY);
    }

    @Override
    public String getName() {
        return "MoveUp";
    }
}
```

#### InputHandler with Command Pattern (`input/InputHandler.java`)
```java
public class InputHandler {
    private Map<Character, Command> keyBindings;

    public InputHandler(Map<Character, Command> keyBindings) {
        this.keyBindings = keyBindings;
    }

    public void handleInput() {
        // ... read input ...
        Command command = keyBindings.get(Character.toLowerCase(key));
        if (command != null) {
            command.execute();
        }
    }

    // Easy to add key remapping!
    public void remapKey(char oldKey, char newKey) {
        Command command = keyBindings.remove(oldKey);
        if (command != null) {
            keyBindings.put(newKey, command);
        }
    }
}
```

#### Configuration in GameLogic
```java
// Create command objects
Command moveUpCmd = new MoveUpCommand(player);
Command moveDownCmd = new MoveDownCommand(player);
Command moveLeftCmd = new MoveLeftCommand(player);
Command moveRightCmd = new MoveRightCommand(player);
Command quitCmd = new QuitCommand();

// Configure key bindings in HashMap
Map<Character, Command> keyBindings = new HashMap<>();
keyBindings.put('w', moveUpCmd);
keyBindings.put('s', moveDownCmd);
keyBindings.put('a', moveLeftCmd);
keyBindings.put('d', moveRightCmd);
keyBindings.put('q', quitCmd);

// Create InputHandler with configuration
this.inputHandler = new InputHandler(keyBindings);
```

### Benefits Demonstrated

1. **Flexible Key Bindings**
   - Want IJKL instead of WASD? Just change the HashMap!
   - No code modification needed

2. **Decoupling**
   - InputHandler doesn't know about Player
   - InputHandler works with any Command

3. **Easy Extension**
   - Want to add attack? Create `AttackCommand` and add to map
   - No modification to InputHandler

4. **Undo Support**
   - Every command can be undone
   - Foundation for implementing undo/redo system

5. **Macro Recording**
   - Commands can be stored in a list
   - Can replay command sequences

### Comparison with 11-01

| Aspect | 11-01 (Hardcoded) | 11-02 (Command Pattern) |
|--------|-------------------|-------------------------|
| Lines of code | 52 lines if-else | 5 lines lookup |
| Key remapping | Modify code | Change HashMap |
| Add new action | Modify if-else chain | Add to HashMap |
| Undo support | Impossible | Built-in |
| Coupling | Tight (knows Player) | Loose (knows Command) |

### Future Extensions Made Easy

With Command Pattern, these features become trivial:

```java
// Key rebinding
inputHandler.remapKey('w', 'i');  // Change W to I

// Command history for undo
List<Command> history = new ArrayList<>();
history.add(command);  // Store each executed command

// Macro recording
List<Command> macro = new ArrayList<>();
// ... record commands ...
// Replay macro
for (Command cmd : macro) {
    cmd.execute();
}
```

### Teaching Points

1. **Open/Closed Principle**
   - Open for extension (add new commands)
   - Closed for modification (InputHandler unchanged)

2. **Single Responsibility**
   - Command: Encapsulates one action
   - InputHandler: Manages input and executes commands
   - Player: Performs movement

3. **Dependency Inversion**
   - InputHandler depends on Command interface, not concrete Player
   - High-level modules don't depend on low-level modules

### Running the Demo

```bash
cd bin/11-02-with-command
java Main
```

**Try it:**
- Move with WASD + Enter
- Notice the same functionality as 11-01
- But now the code is flexible and extensible!

---

**Next:** Branch 11-03 demonstrates tight coupling for event systems (ANTI-PATTERN)
