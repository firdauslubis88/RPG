package gamestate;

import difficulty.DifficultyStrategy;
import level.LevelLoader;
import entities.Player;
import engine.GameEngine;

/**
 * PlayingState - Dungeon exploration state (Game State Pattern)
 *
 * Represents the dungeon exploration state where player navigates the level.
 * This state runs the GameEngine with the selected difficulty and level.
 *
 * Transitions to:
 * - BattleState when player reaches the dungeon exit
 * - DefeatState if player HP reaches 0 during exploration
 */
public class PlayingState implements GameState {
    private DifficultyStrategy strategy;
    private LevelLoader levelLoader;
    private GameEngine engine;
    private boolean gameCompleted;
    private boolean reachedExit;
    private Player player;

    public PlayingState(DifficultyStrategy strategy, LevelLoader levelLoader) {
        this.strategy = strategy;
        this.levelLoader = levelLoader;
        this.gameCompleted = false;
        this.reachedExit = false;
    }

    @Override
    public void enter() {
        System.out.println("\n[PlayingState] Starting dungeon exploration...");
        System.out.println("[PlayingState] Difficulty: " + strategy.getName());
        System.out.println("[PlayingState] Level: " + levelLoader.getClass().getSimpleName());

        // Create GameEngine with selected strategy and level loader
        engine = new GameEngine(strategy, levelLoader);
    }

    @Override
    public GameState update(float deltaTime) {
        if (!gameCompleted) {
            // Run the game engine (blocking until game ends)
            engine.start();

            // Get results
            gameCompleted = true;
            player = engine.getPlayer();
            reachedExit = engine.hasReachedExit();
        }

        // Transition based on result
        if (gameCompleted) {
            if (reachedExit && player != null && player.isAlive()) {
                // Player reached exit alive - transition to battle
                return new BattleState(strategy, player);
            } else {
                // Player died or quit - transition to defeat
                return new DefeatState(strategy);
            }
        }

        return null; // Stay in playing state
    }

    @Override
    public void render() {
        // Rendering is handled by GameEngine
    }

    @Override
    public void exit() {
        System.out.println("[PlayingState] Exiting dungeon exploration...");
    }

    @Override
    public String getStateName() {
        return "PLAYING";
    }
}
