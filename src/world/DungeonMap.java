package world;

/**
 * Week 13: DungeonMap as Static Manager (Option B+ Hybrid Approach)
 *
 * ✅ KEPT: All static methods for backward compatibility
 * ✅ NEW: Delegates to activeMap (polymorphic behavior)
 *
 * This allows different map layouts without changing 20+ files
 * that use DungeonMap.isWalkable(), DungeonMap.getTile(), etc.
 *
 * Usage:
 * - LevelLoader calls DungeonMap.setActiveMap(new ForestMapLayout())
 * - All other code continues using DungeonMap.isWalkable() etc.
 */
public class DungeonMap {
    private static final int WIDTH = 25;
    private static final int HEIGHT = 25;

    // Week 13: Active map instance (default to Dungeon)
    private static GameMap activeMap = new DungeonMapLayout();

    /**
     * Set the active map layout
     * Called by LevelLoader subclasses during level loading
     *
     * @param map The GameMap instance to use
     */
    public static void setActiveMap(GameMap map) {
        activeMap = map;
        System.out.println("  [Map] Loaded: " + map.getLevelName());
        System.out.println("  [Music] " + map.getMusic());
    }

    /**
     * Get the current active map
     * @return The active GameMap instance
     */
    public static GameMap getActiveMap() {
        return activeMap;
    }

    /**
     * Check if a position is walkable (not a wall)
     * Delegates to active map
     */
    public static boolean isWalkable(int x, int y) {
        return activeMap.isWalkable(x, y);
    }

    /**
     * Get map tile at position
     * Delegates to active map
     */
    public static char getTile(int x, int y) {
        return activeMap.getTile(x, y);
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    /**
     * Get a copy of the map for rendering
     * Delegates to active map
     */
    public static char[][] getMapCopy() {
        return activeMap.getMapCopy();
    }

    // ════════════════════════════════════════════════════════════
    // Week 13: Enemy character getters (delegate to active map)
    // ════════════════════════════════════════════════════════════

    /**
     * Get goblin character for current map
     */
    public static char getGoblinChar() {
        return activeMap.getGoblinChar();
    }

    /**
     * Get wolf character for current map
     */
    public static char getWolfChar() {
        return activeMap.getWolfChar();
    }

    /**
     * Get spike character for current map
     */
    public static char getSpikeChar() {
        return activeMap.getSpikeChar();
    }

    /**
     * Get wall character for current map
     */
    public static char getWallChar() {
        return activeMap.getWallChar();
    }

    /**
     * Get floor character for current map
     */
    public static char getFloorChar() {
        return activeMap.getFloorChar();
    }

    /**
     * Get level name
     */
    public static String getLevelName() {
        return activeMap.getLevelName();
    }

    /**
     * Get music filename
     */
    public static String getMusic() {
        return activeMap.getMusic();
    }
}
