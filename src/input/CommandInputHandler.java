package input;

import commands.Command;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Week 11-02: Command Pattern Input Handler
 *
 * ✅ SOLUTION: Flexible key binding with Command Pattern!
 *
 * Compare with InputHandler (11-01):
 * ❌ Before: Giant if-else chain, hardcoded keys
 * ✅ Now: HashMap<Character, Command>, easy remapping
 *
 * Benefits:
 * - Easy to remap keys: just change HashMap entries
 * - Easy to add new commands: no code modification needed
 * - Commands can be reused (same command for multiple keys)
 * - Loose coupling: doesn't know Player or action details
 *
 * Example key remapping (Week 11-03 will show this):
 *   keyBindings.put('w', moveUpCommand);    // Default
 *   keyBindings.put('i', moveUpCommand);    // Also map I to up
 *   keyBindings.put(' ', jumpCommand);      // Space for jump
 */
public class CommandInputHandler {
    private Map<Character, Command> keyBindings;

    /**
     * ✅ Constructor takes pre-configured key bindings
     *
     * This allows external configuration (e.g., from config file)
     * Compare with InputHandler (11-01): hardcoded in method!
     */
    public CommandInputHandler(Map<Character, Command> keyBindings) {
        this.keyBindings = keyBindings;
    }

    /**
     * Handle input using Command Pattern
     *
     * ✅ Clean: Just lookup and execute!
     * ❌ Before (11-01): 50+ lines of if-else
     */
    public void handleInput() {
        try {
            if (System.in.available() > 0) {
                int input = System.in.read();
                char key = Character.toLowerCase((char) input);

                // Skip newline/carriage return
                if (key == '\n' || key == '\r') {
                    return;
                }

                // ✅ SOLUTION: Simple lookup and execute!
                Command command = keyBindings.get(key);
                if (command != null) {
                    command.execute();
                } else {
                    // Unknown key - could log or ignore
                    // System.err.println("Unknown key: " + key);
                }

                // Clear remaining input buffer
                while (System.in.available() > 0) {
                    System.in.read();
                }
            }
        } catch (IOException e) {
            // Ignore IO exceptions for now
        }
    }

    /**
     * ✅ NEW: Easy key remapping!
     *
     * Week 11-01: Impossible to do without modifying source code
     * Week 11-02: Just call this method!
     */
    public void remapKey(char key, Command command) {
        keyBindings.put(Character.toLowerCase(key), command);
    }

    /**
     * ✅ NEW: Get current key binding
     *
     * Useful for displaying controls to player
     */
    public Command getBinding(char key) {
        return keyBindings.get(Character.toLowerCase(key));
    }

    /**
     * ✅ NEW: Clear all bindings
     *
     * Useful for switching between control schemes
     */
    public void clearBindings() {
        keyBindings.clear();
    }

    /**
     * ✅ NEW: Get all bindings
     *
     * Useful for displaying controls screen
     */
    public Map<Character, Command> getAllBindings() {
        return new HashMap<>(keyBindings);  // Return copy for safety
    }
}
