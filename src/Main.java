/**
 * ✅ SOLUTION: Minimal entry point - delegates to GameEngine.
 *
 * Compare this with 09-00 where main() had 150+ lines!
 * Now it's just 3 lines. Clean, simple, professional.
 */
public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start();
    }
}
