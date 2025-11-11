package input;

import entities.Player;
import java.io.IOException;

/**
 * Week 11-01: ANTI-PATTERN - Hardcoded Input Handler
 *
 * ❌ PROBLEMS:
 * 1. Tight coupling - InputHandler knows ALL about Player methods
 * 2. Cannot remap keys - Keys hardcoded to specific actions
 * 3. Cannot undo/redo - No command history
 * 4. Cannot test actions independently - Must test through InputHandler
 * 5. God class - InputHandler must know every possible action
 *
 * Try to add key remapping and you'll see the problem!
 */
public class InputHandler {
    private Player player;

    public InputHandler(Player player) {
        this.player = player;
    }

    /**
     * ❌ ANTI-PATTERN: Giant if-else chain with hardcoded keys
     *
     * Problems:
     * - Want to remap W to Arrow Up? Must modify this method!
     * - Want to add new action? Must modify this method!
     * - Want different key layouts (WASD vs IJKL)? Duplicate code!
     * - InputHandler tightly coupled to Player
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

                // ❌ HARDCODED KEY BINDINGS
                // Try to make this remappable and you'll see why this is bad!
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
                else if (key == 'q' || key == 'Q') {
                    System.out.println("\n\nGame ended by player");
                    System.exit(0);
                }

                // Clear remaining input buffer after processing command
                while (System.in.available() > 0) {
                    System.in.read();
                }

                // Future features will make this even worse:
                // - Space for attack?
                // - E for interact?
                // - I for inventory?
                // - M for map?
                // Each new action = modify this method!
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    /**
     * ❌ Problem: Want to remap keys?
     * You'd need to add configuration and MORE if-else!
     *
     * Example of what you'd have to do:
     * - Store key mappings in config
     * - Check config in EVERY if statement
     * - Exponential complexity with number of actions
     */

    // ❌ Can't implement these easily with current design:
    // public void remapKey(char oldKey, char newKey) { ??? }
    // public void undo() { ??? }
    // public void recordMacro() { ??? }
}
