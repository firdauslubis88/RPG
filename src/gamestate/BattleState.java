package gamestate;

import battle.BattleSystem;
import difficulty.DifficultyStrategy;
import entities.Player;

/**
 * Week 12-05: BattleState (Game State Pattern)
 *
 * Represents the boss battle state.
 * Transitions to:
 * - VictoryState if player wins
 * - DefeatState if player loses
 */
public class BattleState implements GameState {
    private BattleSystem battleSystem;
    private DifficultyStrategy strategy;
    private Player player;
    private boolean battleCompleted;
    private boolean playerWon;

    public BattleState(DifficultyStrategy strategy, Player player) {
        this.strategy = strategy;
        this.player = player;
        this.battleCompleted = false;
    }

    @Override
    public void enter() {
        System.out.println("\n[BattleState] Entering boss battle...");

        // Check if demo mode (boss only defends)
        boolean isDemoMode = strategy.getName().equals("DEMO");
        battleSystem = new BattleSystem(player, isDemoMode);
    }

    @Override
    public GameState update(float deltaTime) {
        if (!battleCompleted) {
            // Run battle (this blocks until battle ends)
            playerWon = battleSystem.startBattle();
            battleCompleted = true;
        }

        // Transition based on battle result
        if (battleCompleted) {
            if (playerWon) {
                return new VictoryState(strategy);
            } else {
                return new DefeatState(strategy);
            }
        }

        return null; // Stay in battle
    }

    @Override
    public void render() {
        // Battle rendering is handled by BattleSystem
    }

    @Override
    public void exit() {
        System.out.println("[BattleState] Exiting boss battle...");
    }

    @Override
    public String getStateName() {
        return "BATTLE";
    }
}
