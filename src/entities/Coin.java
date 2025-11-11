package entities;

/**
 * Week 10: Static collectible coin placed in dungeon.
 * Coins no longer fall - they are statically placed on the map.
 */
public class Coin {
    private int x;
    private int y;
    private boolean collected;
    private int value;
    private char symbol;

    /**
     * Creates a coin at specific position
     */
    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
        this.collected = false;
        this.value = 10;  // Each coin worth 10 points
        this.symbol = '$';  // Coin symbol
    }

    /**
     * Marks coin as collected
     */
    public void collect() {
        this.collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

    public char getSymbol() {
        return symbol;
    }
}
