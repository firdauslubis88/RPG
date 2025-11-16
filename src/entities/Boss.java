package entities;

import boss.*;
import events.*;

/**
 * Boss - Final boss entity with State Pattern AI
 *
 * Week 12-03: STATE PATTERN (SOLUTION)
 *
 * The boss uses State Pattern for AI behavior:
 * - Different states: Idle, Aggressive, Defensive, Enraged
 * - State transitions based on HP, damage, distance
 * - Each state has different movement, damage, appearance
 *
 * Evolution:
 * ❌ Without State Pattern: Giant switch-case for boss AI
 * ✅ With State Pattern: Clean, maintainable state classes
 */
public class Boss implements Entity {
    private int x;
    private int y;
    private BossState currentState;
    private int hp;
    private int maxHp;
    private int damageDealt;  // Total damage dealt to player
    private float timeSinceLastAttack;
    private static final float ATTACK_COOLDOWN = 1.0f;  // 1 second between attacks

    // Boss positioning
    private Entity target;  // Player to attack

    /**
     * Create boss at dungeon exit position
     * @param x Boss X position
     * @param y Boss Y position
     */
    public Boss(int x, int y) {
        this.x = x;
        this.y = y;
        this.maxHp = 200;  // Boss has 200 HP
        this.hp = maxHp;
        this.damageDealt = 0;
        this.timeSinceLastAttack = 0;

        // Week 12-03: Start in IDLE state
        this.currentState = new IdleState();

        System.out.println("🔥 BOSS SPAWNED at (" + x + ", " + y + ") with " + hp + " HP!");
    }

    /**
     * Set player target for boss AI
     * @param target The player entity
     */
    public void setTarget(Entity target) {
        this.target = target;
    }

    /**
     * Update boss AI based on current state
     * Week 12-03: STATE PATTERN - Delegate to current state!
     */
    public void update(float delta) {
        timeSinceLastAttack += delta;

        // STATE PATTERN: Delegate behavior to current state
        currentState.update(this, delta);
    }

    /**
     * Boss takes damage
     * Week 12-03: STATE PATTERN - Notify state of damage!
     */
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) this.hp = 0;

        System.out.println("💥 BOSS HIT! -" + damage + " HP | Remaining: " + hp + "/" + maxHp);

        // Publish event (boss damage event for sound system)
        EventBus.getInstance().publish(new DamageTakenEvent(damage, hp));

        // STATE PATTERN: Let state handle damage and potentially transition
        currentState.onDamageTaken(this, damage);

        // Check if boss is defeated
        if (hp <= 0) {
            System.out.println("🎉 BOSS DEFEATED!");
        }
    }

    /**
     * Boss attacks player
     */
    public void attackPlayer() {
        if (timeSinceLastAttack < ATTACK_COOLDOWN) return;
        if (target == null) return;

        // Check if boss is adjacent to player
        int dx = Math.abs(x - target.getX());
        int dy = Math.abs(y - target.getY());

        if (dx <= 1 && dy <= 1 && (dx + dy) > 0) {  // Adjacent but not same position
            int damage = currentState.getAttackDamage();

            // Player takes damage (handled by GameLogic)
            if (target instanceof Player) {
                ((Player) target).takeDamage(damage);
                GameManager.getInstance().takeDamage(damage);
            }

            damageDealt += damage;
            timeSinceLastAttack = 0;

            System.out.println("💀 BOSS ATTACKS! -" + damage + " HP to player!");
        }
    }

    /**
     * Move boss towards target
     */
    public void moveTowardsTarget(float speed) {
        if (target == null) return;

        // Simple pathfinding: move one step closer to target
        int targetX = target.getX();
        int targetY = target.getY();

        int dx = Integer.compare(targetX, x);
        int dy = Integer.compare(targetY, y);

        // Try to move (speed affects how often, not how far)
        if (Math.random() < speed) {
            // Prefer moving in direction with larger difference
            if (Math.abs(targetX - x) > Math.abs(targetY - y)) {
                // Move horizontally first
                if (dx != 0) {
                    x += dx;
                } else if (dy != 0) {
                    y += dy;
                }
            } else {
                // Move vertically first
                if (dy != 0) {
                    y += dy;
                } else if (dx != 0) {
                    x += dx;
                }
            }
        }
    }

    /**
     * Move boss away from target (retreat)
     */
    public void moveAwayFromTarget(float speed) {
        if (target == null) return;

        int targetX = target.getX();
        int targetY = target.getY();

        int dx = -Integer.compare(targetX, x);  // Opposite direction
        int dy = -Integer.compare(targetY, y);

        if (Math.random() < speed) {
            if (Math.abs(targetX - x) > Math.abs(targetY - y)) {
                if (dx != 0) {
                    x += dx;
                } else if (dy != 0) {
                    y += dy;
                }
            } else {
                if (dy != 0) {
                    y += dy;
                } else if (dx != 0) {
                    x += dx;
                }
            }
        }
    }

    /**
     * Heal boss (defensive state)
     */
    public void heal(int amount) {
        this.hp += amount;
        if (this.hp > maxHp) this.hp = maxHp;
    }

    /**
     * Get distance to target
     */
    public int getDistanceToTarget() {
        if (target == null) return 999;
        return Math.abs(x - target.getX()) + Math.abs(y - target.getY());
    }

    /**
     * Week 12-03: STATE PATTERN - Change boss state
     * This is the key method for state transitions!
     */
    public void setState(BossState newState) {
        System.out.println("🔄 BOSS STATE: " + currentState.getStateName() + " → " + newState.getStateName());
        this.currentState = newState;
    }

    /**
     * Get boss symbol based on current state
     * Week 12-03: STATE PATTERN - Symbol changes per state!
     */
    public char getSymbol() {
        return currentState.getSymbol();
    }

    // Getters from Entity interface
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public BossState getCurrentState() {
        return currentState;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public float getHpPercentage() {
        return (float) hp / maxHp;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public Entity getTarget() {
        return target;
    }

    public boolean isDefeated() {
        return hp <= 0;
    }
}
