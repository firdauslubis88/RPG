package battle;

import entities.Player;
import entities.GameManager;

/**
 * Week 12-04: BattleContext (STATE PATTERN)
 *
 * This class provides context information and operations for boss states.
 * It acts as a bridge between the BattleSystem and individual state classes,
 * allowing states to apply damage and query battle information without
 * directly accessing BattleSystem internals.
 *
 * This follows the Context pattern in State Pattern implementations.
 */
public class BattleContext {
    private Player player;
    private int bossHp;
    private int bossMaxHp;

    // Battle action constants (shared with states)
    public static final String ATTACK = "ATTACK";
    public static final String DEFEND = "DEFEND";
    public static final String MAGIC = "MAGIC";
    public static final String COUNTER = "COUNTER";

    public BattleContext(Player player, int bossHp, int bossMaxHp) {
        this.player = player;
        this.bossHp = bossHp;
        this.bossMaxHp = bossMaxHp;
    }

    /**
     * Deal damage to the player
     */
    public void dealDamageToPlayer(int damage) {
        player.takeDamage(damage);
        GameManager.getInstance().takeDamage(damage);
        System.out.println("💢 You take " + damage + " damage!");
    }

    /**
     * Deal damage to the boss
     */
    public void dealDamageToBoss(int damage) {
        bossHp -= damage;
        if (bossHp < 0) bossHp = 0;
        System.out.println("⚔️ Boss takes " + damage + " damage!");
    }

    /**
     * Get current boss HP
     */
    public int getBossHp() {
        return bossHp;
    }

    /**
     * Get boss max HP
     */
    public int getBossMaxHp() {
        return bossMaxHp;
    }

    /**
     * Get boss HP percentage (0.0 to 1.0)
     */
    public float getBossHpPercent() {
        return (float) bossHp / bossMaxHp;
    }

    /**
     * Get player current HP
     */
    public int getPlayerHp() {
        return GameManager.getInstance().getHp();
    }
}
