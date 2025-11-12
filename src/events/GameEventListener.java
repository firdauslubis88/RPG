package events;

/**
 * Week 11-04: GameEventListener interface (Observer Pattern)
 *
 * ✅ SOLUTION: Observers implement this interface to receive events
 *
 * Any class that wants to listen to game events implements this interface.
 * The EventBus will notify all registered listeners when events occur.
 *
 * Benefits:
 * - Decouples event publishers from listeners
 * - Easy to add new listeners
 * - Listeners can filter events they care about
 */
public interface GameEventListener {
    /**
     * Called when an event is published to the EventBus
     * @param event The game event that occurred
     */
    void onEvent(GameEvent event);
}
