package events;

import java.util.ArrayList;
import java.util.List;

/**
 * Week 11-04: EventBus - Central event dispatcher (Observer Pattern)
 *
 * ✅ SOLUTION: Decouples event publishers from listeners
 *
 * The EventBus is the central hub for all game events.
 * - Publishers call publish(event) to broadcast events
 * - Listeners register via subscribe() to receive events
 * - EventBus notifies all registered listeners when events occur
 *
 * Benefits:
 * - Complete decoupling: Publishers don't know about listeners
 * - Easy to add new event types (just create new GameEvent subclass)
 * - Easy to add new listeners (just implement GameEventListener)
 * - Follows Observer Pattern and Open/Closed Principle
 *
 * Implementation Note:
 * Uses Singleton pattern for global access throughout the game.
 */
public class EventBus {
    private static EventBus instance;
    private List<GameEventListener> listeners;

    /**
     * Private constructor for Singleton pattern
     */
    private EventBus() {
        this.listeners = new ArrayList<>();
    }

    /**
     * Get the singleton instance of EventBus
     * @return The EventBus instance
     */
    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    /**
     * Subscribe a listener to receive all events
     * @param listener The listener to subscribe
     */
    public void subscribe(GameEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Unsubscribe a listener from receiving events
     * @param listener The listener to unsubscribe
     */
    public void unsubscribe(GameEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Publish an event to all registered listeners
     *
     * ✅ OBSERVER PATTERN: All listeners are notified automatically
     *
     * @param event The event to publish
     */
    public void publish(GameEvent event) {
        // Notify all listeners
        for (GameEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    /**
     * Clear all listeners (useful for testing or reset)
     */
    public void clearListeners() {
        listeners.clear();
    }

    /**
     * Get the number of registered listeners (for debugging)
     * @return The number of listeners
     */
    public int getListenerCount() {
        return listeners.size();
    }
}
