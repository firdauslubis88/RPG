package battle;

import battle.subsystems.BattleAnimationSystem;
import battle.subsystems.BattleSoundSystem;
import battle.subsystems.BattleUISystem;

/**
 * Week 13-04: Facade Pattern (SOLUTION)
 *
 * BattleFacade provides a simplified interface to the complex
 * battle subsystems (Animation, Sound, UI).
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
 * - playerAttack()
 * - endBattle()
 */
public class BattleFacade {
    // Subsystems (hidden from client)
    private BattleAnimationSystem animation;
    private BattleSoundSystem sound;
    private BattleUISystem ui;

    // Battle state
    private int playerHp = 100;
    private int bossHp = 500;

    /**
     * Create a new BattleFacade.
     * Subsystems are created but NOT initialized yet.
     */
    public BattleFacade() {
        this.animation = new BattleAnimationSystem();
        this.sound = new BattleSoundSystem();
        this.ui = new BattleUISystem();
    }

    /**
     * Start a battle - ONE call initializes EVERYTHING!
     * The Facade handles all the complex initialization order internally.
     */
    public void startBattle() {
        System.out.println("\n[BattleFacade] ═══ Starting Battle ═══\n");

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
        ui.updateHealthBars(playerHp, bossHp);

        System.out.println("\n[BattleFacade] ═══ Battle Started! ═══\n");
    }

    /**
     * Show action menu to player.
     */
    public void showActions() {
        ui.showActionMenu();
    }

    /**
     * Player performs an attack - ONE call coordinates ALL subsystems!
     */
    public void playerAttack() {
        System.out.println("\n[BattleFacade] → Player Attack\n");

        // Facade coordinates all subsystems internally
        animation.playAttackAnimation("Player");
        sound.playAttackSound();

        int damage = 45;
        bossHp -= damage;

        animation.playDamageAnimation("Boss", damage);
        ui.showDamageNumber("Boss", damage);
        ui.updateHealthBars(playerHp, bossHp);
    }

    /**
     * Player performs a defend action - ONE call!
     */
    public void playerDefend() {
        System.out.println("\n[BattleFacade] → Player Defend\n");

        animation.playDefendAnimation("Player");
        sound.playDefendSound();
        System.out.println("[BattleFacade] Player's defense increased!");
    }

    /**
     * Player casts magic - ONE call!
     */
    public void playerMagic() {
        System.out.println("\n[BattleFacade] → Player Magic\n");

        animation.playMagicAnimation("Player");
        sound.playMagicSound();

        int damage = 80;
        bossHp -= damage;

        animation.playDamageAnimation("Boss", damage);
        ui.showDamageNumber("Boss", damage);
        ui.updateHealthBars(playerHp, bossHp);
    }

    /**
     * Execute boss's turn - ONE call!
     */
    public void bossTurn() {
        System.out.println("\n[BattleFacade] → Boss Turn\n");

        sound.playBossRoar();
        animation.playAttackAnimation("Boss");
        sound.playAttackSound();

        int damage = 30;
        playerHp -= damage;

        animation.playDamageAnimation("Player", damage);
        ui.showDamageNumber("Player", damage);
        ui.updateHealthBars(playerHp, bossHp);
    }

    /**
     * Check if battle is over.
     */
    public boolean isBattleOver() {
        return playerHp <= 0 || bossHp <= 0;
    }

    /**
     * Check if player won.
     */
    public boolean isPlayerVictorious() {
        return bossHp <= 0;
    }

    /**
     * Show victory screen.
     */
    public void showVictory() {
        ui.showVictoryScreen();
    }

    /**
     * Show defeat screen.
     */
    public void showDefeat() {
        ui.showDefeatScreen();
    }

    /**
     * End battle - ONE call cleans up EVERYTHING!
     * The Facade handles all cleanup internally.
     */
    public void endBattle() {
        System.out.println("\n[BattleFacade] ═══ Ending Battle ═══\n");

        // Facade handles cleanup ORDER internally
        sound.stopMusic();
        ui.cleanup();
        sound.cleanup();
        animation.cleanup();

        System.out.println("\n[BattleFacade] ═══ Battle Ended ═══");
    }
}
