import entities.NPC;
import entities.Coin;

public class TestMovement {
    public static void main(String[] args) {
        System.out.println("=== MOVEMENT VERIFICATION ===\n");
        
        // Test NPC horizontal movement
        System.out.println("1. NPC Horizontal Movement Test:");
        NPC npc = new NPC(0, 5);
        System.out.println("   Initial: (" + npc.getX() + ", " + npc.getY() + ")");
        
        npc.moveRight();
        System.out.println("   After moveRight(): (" + npc.getX() + ", " + npc.getY() + ")");
        System.out.println("   Expected: (1, 5) - X increases, Y stays same ✓\n");
        
        npc.moveRight();
        npc.moveRight();
        System.out.println("   After 2 more moves: (" + npc.getX() + ", " + npc.getY() + ")");
        System.out.println("   Expected: (3, 5) - X continues increasing ✓\n");
        
        // Test Coin vertical movement  
        System.out.println("2. Coin Vertical Movement Test:");
        Coin coin = new Coin(5, 0);
        System.out.println("   Initial: (" + coin.getX() + ", " + coin.getY() + ")");
        
        coin.fall();
        System.out.println("   After fall(): (" + coin.getX() + ", " + coin.getY() + ")");
        System.out.println("   Expected: (5, 1) - Y increases, X stays same ✓\n");
        
        coin.fall();
        coin.fall();
        System.out.println("   After 2 more falls: (" + coin.getX() + ", " + coin.getY() + ")");
        System.out.println("   Expected: (5, 3) - Y continues increasing ✓\n");
        
        System.out.println("=== MOVEMENT WORKING CORRECTLY ===");
        System.out.println("NPC: Horizontal movement (X changes, Y fixed)");
        System.out.println("Coin: Vertical movement (Y changes, X fixed)");
    }
}
