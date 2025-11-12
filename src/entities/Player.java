package entities;

import world.DungeonMap;
import events.EventBus;
import events.DamageTakenEvent;
import events.CoinCollectedEvent;

/**
 * Week 11-04: Player with Observer Pattern (SOLUTION)
 *
 * ✅ SOLUTION: Player publishes events instead of calling systems directly
 *
 * Benefits:
 * 1. Simple Constructor: Player(x, y) - no system dependencies!
 * 2. No Object Drilling: No need to pass systems through constructors
 * 3. Follows SRP: Player only manages its own state
 * 4. Easy to Test: No need to mock systems
 * 5. Easy to Extend: Want ParticleSystem? Just make it listen to events!
 *
 * Evolution from Week 11-03:
 * ❌ Before: Player manually calls soundSystem.play() and achievementSystem.on()
 * ✅ Now: Player publishes events, observers react automatically
 */
public class Player implements Entity {
    private int x;
    private int y;
    private int health;
    private int maxHealth;
    private int score;
    private char symbol;

    /**
     * Week 11-04: ✅ OBSERVER PATTERN - Simple constructor!
     *
     * Before (11-03): Player(x, y, soundSystem, achievementSystem) - complex!
     * Now (11-04): Player(x, y) - simple!
     *
     * No system dependencies needed!
     */
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

    // Week 11-04: Damage and scoring with OBSERVER PATTERN

    /**
     * Week 11-04: ✅ OBSERVER PATTERN - takeDamage publishes event!
     *
     * Benefits:
     * - Player just publishes DamageTakenEvent
     * - SoundSystem listens and plays hurt sound
     * - AchievementSystem listens and checks achievements
     * - Want ParticleSystem? Just make it listen to the event!
     * - No need to modify Player code!
     *
     * This is loose coupling via Observer Pattern!
     */
    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }

        // ✅ OBSERVER PATTERN: Publish event, let observers react
        EventBus.getInstance().publish(new DamageTakenEvent(amount, health));
    }

    public void addScore(int points) {
        score += points;
    }

    /**
     * Week 11-04: ✅ OBSERVER PATTERN - collectCoin publishes event!
     *
     * Benefits:
     * - Player just publishes CoinCollectedEvent
     * - SoundSystem listens and plays coin sound
     * - AchievementSystem listens and tracks coin count
     * - Want UI notification? Just make UI listen to the event!
     * - No need to modify Player code!
     *
     * This is loose coupling via Observer Pattern!
     */
    public void collectCoin(int value) {
        score += value;

        // ✅ OBSERVER PATTERN: Publish event, let observers react
        EventBus.getInstance().publish(new CoinCollectedEvent(value, score));
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
