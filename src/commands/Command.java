package commands;

/**
 * Week 11-02: Command Pattern Interface
 *
 * ✅ SOLUTION: Actions as objects!
 *
 * This interface encapsulates actions (like "move up") as objects
 * that can be:
 * - Stored in collections (HashMap, List)
 * - Passed as parameters
 * - Executed later
 * - Undone (if needed)
 *
 * Compare with Week 11-01:
 * ❌ Before: Actions were hardcoded if-else statements
 * ✅ Now: Actions are objects implementing this interface
 *
 * Benefits:
 * - Easy to add new commands (just create new class)
 * - Easy to remap keys (just change HashMap)
 * - Supports undo/redo (store command history)
 * - Supports macros (store list of commands)
 * - Loose coupling (input handler doesn't know action details)
 */
public interface Command {
    /**
     * Execute this command
     *
     * This is where the actual action happens.
     * For movement commands: move the player
     * For other commands: do whatever the command does
     */
    void execute();

    /**
     * Undo this command
     *
     * Week 11-02: Basic undo support (store old position)
     * Week 11-03/04: Can be extended for full undo/redo system
     *
     * Not all commands can be undone (e.g., QuitCommand)
     */
    void undo();

    /**
     * Get command name for debugging/display
     *
     * Useful for:
     * - Showing controls screen ("W: Move Up")
     * - Debugging command execution
     * - Logging/telemetry
     */
    String getName();
}
