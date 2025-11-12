package input;

import commands.Command;
import java.io.IOException;
import java.util.Map;

/**
 * Week 11-02: InputHandler with Command Pattern
 *
 * ✅ SOLUTION: Uses Command Pattern for flexible key bindings
 *
 * Evolution from Week 11-01:
 * ❌ Before (11-01): Hardcoded if-else chain, tight coupling to Player
 * ✅ Now (11-02): HashMap-based lookup, decoupled from Player
 *
 * Benefits:
 * - Want to remap W to Arrow Up? Just change the HashMap!
 * - Want to add new action? Just create new Command and add to map!
 * - Want different key layouts (WASD vs IJKL)? Create different maps!
 * - InputHandler decoupled from Player - works with any Command
 * - Easy to implement undo, macros, key rebinding
 */
public class InputHandler {
    private Map<Character, Command> keyBindings;

    /**
     * Week 11-02: Constructor now accepts Map of key bindings
     *
     * Before: InputHandler(Player player) - tight coupling!
     * Now: InputHandler(Map<Character, Command> keyBindings) - flexible!
     */
    public InputHandler(Map<Character, Command> keyBindings) {
        this.keyBindings = keyBindings;
    }

    /**
     * Week 11-02: ✅ Command Pattern - Clean lookup-based input handling
     *
     * Evolution from Week 11-01:
     * ❌ Before: Giant if-else chain (52 lines!)
     * ✅ Now: Simple HashMap lookup (5 lines!)
     *
     * Advantages:
     * - No if-else chain needed!
     * - Adding new action? Just add to keyBindings map
     * - Remapping keys? Just change the map configuration
     * - Same handler works for WASD, IJKL, or any key layout
     *
     * Note: Windows console requires Enter key for input (buffered).
     * This is a limitation of System.in on Windows - real games use
     * native libraries or game engines for raw input.
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
                // Compare with 11-01: No more giant if-else chain!
                // Convert to lowercase for case-insensitive lookup
                Command command = keyBindings.get(Character.toLowerCase(key));
                if (command != null) {
                    command.execute();
                }

                // Clear remaining input buffer after processing command
                while (System.in.available() > 0) {
                    System.in.read();
                }

                // Future features are now EASY:
                // - Space for attack? keyBindings.put(' ', new AttackCommand(player));
                // - E for interact? keyBindings.put('e', new InteractCommand(player));
                // - I for inventory? keyBindings.put('i', new InventoryCommand());
                // - M for map? keyBindings.put('m', new MapCommand());
                // Each new action = just add to map, NO modification to this method!
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    /**
     * Week 11-02: ✅ Easy to implement now with Command Pattern!
     *
     * In 11-01, this was impossible without major refactoring.
     * Now it's trivial!
     */
    public void remapKey(char oldKey, char newKey) {
        Command command = keyBindings.remove(Character.toLowerCase(oldKey));
        if (command != null) {
            keyBindings.put(Character.toLowerCase(newKey), command);
        }
    }

    // Can add undo, macro recording, etc. easily with Command Pattern
    // These were impossible in 11-01's hardcoded design!
}
