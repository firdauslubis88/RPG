package battle;

import battle.subsystems.BattleAnimationSystem;
import battle.subsystems.BattleSoundSystem;
import battle.subsystems.BattleUISystem;
import entities.Player;
import entities.GameManager;

/**
 * Week 13-04: Facade Pattern (SOLUTION) - INTEGRATED VERSION
 *
 * BattleFacade provides a simplified interface to the complex
 * battle subsystems (Animation, Sound, UI) AND the BattleSystem.
 *
 * This Facade integrates:
 * - BattleAnimationSystem (sprites, animations)
 * - BattleSoundSystem (audio, music, SFX)
 * - BattleUISystem (health bars, menus)
 * - BattleSystem with State Pattern (actual battle logic)
 *
 * Benefits:
 * 1. Client doesn't need to know subsystem internals
 * 2. Client doesn't manage initialization order
 * 3. Client uses simple, high-level methods
 * 4. Subsystem changes don't affect clients
 * 5. Single point of access to battle functionality
 *
 * The Facade coordinates all subsystems behind simple methods like:
 * - startBattle()
 * - executeBattle()
 * - endBattle()
 */
public class BattleFacade {
    // Subsystems (hidden from client)
    private BattleAnimationSystem animation;
    private BattleSoundSystem sound;
    private BattleUISystem ui;

    // The actual battle system with State Pattern
    private BattleSystem battleSystem;

    // References
    private Player player;
    private boolean isDemoMode;

    /**
     * Create a new BattleFacade.
     * Subsystems are created but NOT initialized yet.
     *
     * @param player The player entity
     * @param isDemoMode If true, boss only defends (for testing)
     */
    public BattleFacade(Player player, boolean isDemoMode) {
        this.player = player;
        this.isDemoMode = isDemoMode;

        // Create subsystems
        this.animation = new BattleAnimationSystem();
        this.sound = new BattleSoundSystem();
        this.ui = new BattleUISystem();

        // Create the actual battle system
        this.battleSystem = new BattleSystem(player, isDemoMode);
    }

    /**
     * Initialize battle - ONE call initializes ALL subsystems!
     * The Facade handles all the complex initialization order internally.
     */
    public void initializeBattle() {
        System.out.println("\n[BattleFacade] ═══ Initializing Battle Systems ═══\n");

        // Facade handles initialization ORDER internally
        // Client doesn't need to know about this!

        // 1. Initialize animation system (2 steps)
        animation.init();
        animation.loadBattleSprites();

        // 2. Initialize sound system (2 steps + play music)
        sound.init();
        sound.loadBattleSounds();
        sound.playBattleMusic("boss_battle.mp3");

        // 3. Initialize UI system (3 steps)
        ui.init();
        ui.createBattleUI();
        ui.showBattleScreen();

        // 4. Show initial state
        ui.updateHealthBars(GameManager.getInstance().getHp(), 200);

        System.out.println("\n[BattleFacade] ═══ All Systems Ready! ═══\n");
    }

    /**
     * Execute the full battle - ONE call runs the entire battle!
     * This delegates to BattleSystem which uses State Pattern.
     *
     * @return true if player wins, false if player loses or runs
     */
    public boolean executeBattle() {
        // The actual battle logic is handled by BattleSystem
        // which internally uses State Pattern for boss AI
        return battleSystem.startBattle();
    }

    /**
     * Cleanup battle - ONE call cleans up ALL subsystems!
     * The Facade handles all cleanup internally.
     */
    public void cleanupBattle() {
        System.out.println("\n[BattleFacade] ═══ Cleaning Up Battle Systems ═══\n");

        // Facade handles cleanup ORDER internally
        sound.stopMusic();
        ui.cleanup();
        sound.cleanup();
        animation.cleanup();

        System.out.println("\n[BattleFacade] ═══ Cleanup Complete ═══");
    }

    /**
     * Full battle sequence - ONE call does everything!
     * Initialize → Execute Battle → Cleanup
     *
     * @return true if player wins, false if player loses or runs
     */
    public boolean runFullBattle() {
        // 1. Initialize all subsystems
        initializeBattle();

        // 2. Execute the battle (uses BattleSystem with State Pattern)
        boolean playerWon = executeBattle();

        // 3. Cleanup all subsystems
        cleanupBattle();

        return playerWon;
    }

    // ═══════════════════════════════════════════════════════════════
    // Individual action methods (for more granular control if needed)
    // ═══════════════════════════════════════════════════════════════

    /**
     * Play attack animation and sound - ONE call!
     */
    public void playAttackEffects(String attacker) {
        animation.playAttackAnimation(attacker);
        sound.playAttackSound();
    }

    /**
     * Play defend animation and sound - ONE call!
     */
    public void playDefendEffects(String defender) {
        animation.playDefendAnimation(defender);
        sound.playDefendSound();
    }

    /**
     * Play magic animation and sound - ONE call!
     */
    public void playMagicEffects(String caster) {
        animation.playMagicAnimation(caster);
        sound.playMagicSound();
    }

    /**
     * Play damage effects - ONE call!
     */
    public void playDamageEffects(String target, int damage) {
        animation.playDamageAnimation(target, damage);
        ui.showDamageNumber(target, damage);
    }

    /**
     * Update health bars display - ONE call!
     */
    public void updateHealthDisplay(int playerHp, int bossHp) {
        ui.updateHealthBars(playerHp, bossHp);
    }

    /**
     * Show victory screen - ONE call!
     */
    public void showVictory() {
        ui.showVictoryScreen();
    }

    /**
     * Show defeat screen - ONE call!
     */
    public void showDefeat() {
        ui.showDefeatScreen();
    }
}
