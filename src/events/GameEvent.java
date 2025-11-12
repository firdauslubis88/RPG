package events;

/**
 * Week 11-04: Base class for all game events (Observer Pattern)
 *
 * ✅ SOLUTION: Event-based communication instead of tight coupling
 *
 * This is the base class for all events in the game.
 * Events represent things that happen in the game (damage taken, coin collected, etc.)
 *
 * Benefits:
 * - Decouples event publishers from event listeners
 * - Easy to add new event types
 * - Easy to add new listeners without modifying existing code
 * - Follows Open/Closed Principle
 */
public abstract class GameEvent {
    private final String eventType;
    private final long timestamp;

    /**
     * Create a new game event
     * @param eventType The type of event (e.g., "DamageTaken", "CoinCollected")
     */
    public GameEvent(String eventType) {
        this.eventType = eventType;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Get the event type
     * @return The event type string
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Get the timestamp when this event was created
     * @return The timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s[type=%s, time=%d]",
            getClass().getSimpleName(), eventType, timestamp);
    }
}
