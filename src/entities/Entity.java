package entities;

/**
 * Week 11: Entity interface
 *
 * Common interface for both Player and NPC
 * Allows WorldController to work with either
 */
public interface Entity {
    int getX();
    int getY();
}
