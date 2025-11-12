package entities;

import world.DungeonMap;

/**
 * Week 11-01: Player class
 *
 * Player character controlled by keyboard input.
 * Has health, position, score, and can move/take damage.
 */
public class Player implements Entity {
    private int x;
    private int y;
    private int health;
    private int maxHealth;
    private int score;
    private char symbol;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.maxHealth = 100;
        this.health = maxHealth;
        this.score = 0;
        this.symbol = '@';  // Player symbol
    }

    // Movement methods
    public void moveUp() {
        int newY = y - 1;
        if (DungeonMap.isWalkable(x, newY)) {
            y = newY;
        }
    }

    public void moveDown() {
        int newY = y + 1;
        if (DungeonMap.isWalkable(x, newY)) {
            y = newY;
        }
    }

    public void moveLeft() {
        int newX = x - 1;
        if (DungeonMap.isWalkable(newX, y)) {
            x = newX;
        }
    }

    public void moveRight() {
        int newX = x + 1;
        if (DungeonMap.isWalkable(newX, y)) {
            x = newX;
        }
    }

    // Damage and scoring
    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    public void addScore(int points) {
        score += points;
    }

    public void collectCoin(int value) {
        score += value;
    }

    // Week 11-02: For Command Pattern undo functionality
    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getScore() { return score; }
    public char getSymbol() { return symbol; }

    public boolean isAlive() {
        return health > 0;
    }

    public void reset(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.health = maxHealth;
        this.score = 0;
    }
}
