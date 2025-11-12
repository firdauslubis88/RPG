package input;

import commands.Command;
import java.io.IOException;
import java.util.Map;

/**
 * Week 11-02: Command Pattern Input Handler
 *
 * ✅ SOLUTION: Uses Command Pattern for flexible key bindings
 *
 * Benefits:
 * - Want to remap W to Arrow Up? Just change the HashMap!
 * - Want to add new action? Just create new Command and add to map!
 * - Want different key layouts (WASD vs IJKL)? Create different maps!
 * - InputHandler decoupled from Player - works with any Command
 * - Easy to implement undo, macros, key rebinding
 */
public class CommandInputHandler {
    private Map<Character, Command> keyBindings;

    public CommandInputHandler(Map<Character, Command> keyBindings) {
        this.keyBindings = keyBindings;
    }

    /**
     * ✅ Command Pattern: Clean lookup-based input handling
     *
     * Advantages:
     * - No if-else chain needed!
     * - Adding new action? Just add to keyBindings map
     * - Remapping keys? Just change the map configuration
     * - Same handler works for WASD, IJKL, or any key layout
     */
    public void handleInput() {
        try {
            // Check if input is available (non-blocking)
            if (System.in.available() > 0) {
                int input = System.in.read();
                char key = (char) input;

                // Skip newline/carriage return characters
                if (key == '\n' || key == '\r') {
                    return;
                }

                // ✅ COMMAND PATTERN: Lookup and execute
                // Convert to lowercase for case-insensitive lookup
                Command command = keyBindings.get(Character.toLowerCase(key));
                if (command != null) {
                    command.execute();
                }

                // Clear remaining input buffer after processing command
                while (System.in.available() > 0) {
                    System.in.read();
                }
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    /**
     * ✅ Easy to implement now with Command Pattern!
     */
    public void remapKey(char oldKey, char newKey) {
        Command command = keyBindings.remove(Character.toLowerCase(oldKey));
        if (command != null) {
            keyBindings.put(Character.toLowerCase(newKey), command);
        }
    }

    // Can add undo, macro recording, etc. easily with Command Pattern
}
