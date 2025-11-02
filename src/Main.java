import entities.GameManager;

/**
 * ❌ PROBLEM: Must create and pass GameManager everywhere!
 *
 * Before (09-01): Simple 3-line main
 * Now (09-02): Must manage GameManager instance
 *
 * This starts the object drilling chain:
 * Main → GameEngine → GameLogic → NPC/Coin (4 levels!)
 */
public class Main {
    public static void main(String[] args) {
        // ❌ PROBLEM: Creates GameManager instance
        // But nothing prevents creating MORE instances elsewhere!
        GameManager manager = new GameManager();

        System.out.println("[Main] Created GameManager instance: " + manager.hashCode());

        // ❌ Must pass manager to GameEngine
        GameEngine engine = new GameEngine(manager);
        engine.start();

        // ❌ PROBLEM: Object drilling has begun!
        // Main → GameEngine (1 level)
        // GameEngine → GameLogic (2 levels)
        // GameEngine → HUD (2 levels)
        // GameLogic → NPC (3 levels)
        // GameLogic → Coin (3 levels)
    }
}
