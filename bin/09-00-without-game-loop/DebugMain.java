import entities.NPC;
import entities.Coin;
import utils.GridRenderer;
import java.util.Random;

public class DebugMain {
    public static void main(String[] args) throws InterruptedException {
        NPC npc = new NPC(0, 5);
        Random random = new Random();
        Coin coin = new Coin(random.nextInt(GridRenderer.getGridWidth()), 0);
        int score = 0;
        
        System.out.println("DEBUG MODE - Watching first 15 frames:\n");
        
        for (int frame = 1; frame <= 15; frame++) {
            System.out.println("FRAME " + frame + " START:");
            System.out.println("  Before moveRight: NPC at (" + npc.getX() + ", " + npc.getY() + ")");
            
            npc.moveRight();
            System.out.println("  After moveRight:  NPC at (" + npc.getX() + ", " + npc.getY() + ")");
            
            npc.wrapAtEdge(GridRenderer.getGridWidth());
            System.out.println("  After wrap:       NPC at (" + npc.getX() + ", " + npc.getY() + ")");
            
            coin.fall();
            if (coin.isOffScreen(GridRenderer.getGridHeight())) {
                coin.respawn(random.nextInt(GridRenderer.getGridWidth()));
            }
            
            if (npc.getX() == coin.getX() && npc.getY() == coin.getY()) {
                score += 10;
                System.out.println("  >>> COLLISION! Score now: " + score);
                coin.respawn(random.nextInt(GridRenderer.getGridWidth()));
            }
            
            System.out.println("  Final positions - NPC: (" + npc.getX() + ", " + npc.getY() + ")  Coin: (" + coin.getX() + ", " + coin.getY() + ")");
            System.out.println();
            
            Thread.sleep(200);
        }
        
        System.out.println("\nCONCLUSION:");
        System.out.println("NPC should have moved from X=0 to X=15 (with wrapping at 10)");
        System.out.println("If you see X changing, movement is WORKING!");
    }
}
