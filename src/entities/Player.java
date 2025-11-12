package entities;

import world.DungeonMap;
import systems.SoundSystem;
import systems.AchievementSystem;

/**
 * Week 11-03: Player with Tight Coupling (ANTI-PATTERN)
 *
 * ❌ PROBLEM: Player is tightly coupled to multiple systems!
 *
 * Issues demonstrated:
 * 1. Constructor Nightmare: Player needs HUD, SoundSystem, AchievementSystem
 * 2. Object Drilling: All these systems must be passed from GameLogic
 * 3. Violates SRP: Player manages state + notifies UI + plays sounds + tracks achievements
 * 4. Hard to Test: Must mock SoundSystem, AchievementSystem to test Player
 * 5. Hard to Extend: Want to add ParticleSystem? Modify Player constructor & methods!
 *
 * This is the ANTI-PATTERN we want to avoid!
 * Branch 11-04 will show the solution using Observer Pattern.
 */
public class Player implements Entity {
    private int x;
    private int y;
    private int health;
    private int maxHealth;
    private int score;
    private char symbol;

    // Week 11-03: ❌ TIGHT COUPLING - Player knows about all these systems!
    private SoundSystem soundSystem;
    private AchievementSystem achievementSystem;

    /**
     * Week 11-03: ❌ ANTI-PATTERN - Constructor Nightmare!
     *
     * Before: Player(x, y) - simple!
     * Now: Player(x, y, soundSystem, achievementSystem) - complex!
     *
     * This demonstrates object drilling and constructor explosion.
     */
    public Player(int startX, int startY, SoundSystem soundSystem, AchievementSystem achievementSystem) {
        this.x = startX;
        this.y = startY;
        this.maxHealth = 100;
        this.health = maxHealth;
        this.score = 0;
        this.symbol = '@';  // Player symbol

        // ❌ TIGHT COUPLING: Player must store references to all systems
        this.soundSystem = soundSystem;
        this.achievementSystem = achievementSystem;
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

    // Week 11-03: Damage and scoring with TIGHT COUPLING

    /**
     * Week 11-03: ❌ ANTI-PATTERN - takeDamage manually notifies all systems!
     *
     * Problems:
     * - Player must call soundSystem.playHurtSound()
     * - Player must call achievementSystem.onDamageTaken()
     * - Want to add ParticleSystem? Must modify this method!
     * - Violates Single Responsibility Principle
     *
     * This is tight coupling!
     */
    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }

        // ❌ TIGHT COUPLING: Player manually notifies all systems
        soundSystem.playHurtSound();
        achievementSystem.onDamageTaken(amount);
    }

    public void addScore(int points) {
        score += points;
    }

    /**
     * Week 11-03: ❌ ANTI-PATTERN - collectCoin manually notifies all systems!
     *
     * Problems:
     * - Player must call soundSystem.playCoinSound()
     * - Player must call achievementSystem.onCoinCollected()
     * - Want to add UI notification? Must modify this method!
     * - Violates Single Responsibility Principle
     *
     * This is tight coupling!
     */
    public void collectCoin(int value) {
        score += value;

        // ❌ TIGHT COUPLING: Player manually notifies all systems
        soundSystem.playCoinSound();
        achievementSystem.onCoinCollected();
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
