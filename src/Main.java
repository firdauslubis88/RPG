/**
 * ✅ SOLUTION: Clean main with no object drilling!
 *
 * Before (09-02): Had to create and pass GameManager
 * Now (09-03): GameEngine handles everything via Singleton
 *
 * This is how it should be - simple, clean, no parameter pollution!
 */
public class Main {
    public static void main(String[] args) {
        // ✅ SOLUTION: No GameManager creation/passing needed!
        // GameEngine will access it via getInstance()
        GameEngine engine = new GameEngine();
        engine.start();

        // ✅ Clean! No object drilling!
        // Compare with 09-02:
        //   GameManager manager = new GameManager();
        //   GameEngine engine = new GameEngine(manager);
    }
}
