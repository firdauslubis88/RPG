package events;

/**
 * Week 11-04: DamageTakenEvent (Observer Pattern)
 *
 * Published when the player takes damage.
 *
 * Listeners:
 * - SoundSystem: Plays hurt sound
 * - AchievementSystem: Checks for "First Blood" achievement
 */
public class DamageTakenEvent extends GameEvent {
    private final int damage;
    private final int remainingHealth;

    public DamageTakenEvent(int damage, int remainingHealth) {
        super("DamageTaken");
        this.damage = damage;
        this.remainingHealth = remainingHealth;
    }

    public int getDamage() {
        return damage;
    }

    public int getRemainingHealth() {
        return remainingHealth;
    }

    @Override
    public String toString() {
        return String.format("DamageTakenEvent[damage=%d, remainingHP=%d]",
            damage, remainingHealth);
    }
}
