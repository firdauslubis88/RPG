package events;

/**
 * Week 11-04: GameTimeEvent (Observer Pattern)
 *
 * Published periodically to update time-based systems.
 *
 * Listeners:
 * - AchievementSystem: Checks for time-based achievements (e.g., "Survivor")
 */
public class GameTimeEvent extends GameEvent {
    private final float elapsedTime;

    public GameTimeEvent(float elapsedTime) {
        super("GameTime");
        this.elapsedTime = elapsedTime;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public String toString() {
        return String.format("GameTimeEvent[elapsedTime=%.2fs]", elapsedTime);
    }
}
