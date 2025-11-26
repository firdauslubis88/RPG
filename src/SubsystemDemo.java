import battle.subsystems.BattleAnimationSystem;
import battle.subsystems.BattleSoundSystem;
import battle.subsystems.BattleUISystem;

/**
 * Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN) - Demo
 *
 * This demo shows PROBLEMS with tightly coupled subsystems:
 * 1. Client must know ALL subsystem internals
 * 2. Client must manage initialization ORDER
 * 3. Client must coordinate actions across subsystems
 * 4. Client must handle cleanup in correct order
 * 5. Any subsystem change affects ALL clients
 * 6. Code is duplicated across multiple clients
 *
 * SOLUTION: Facade Pattern (see branch 13-04-facade-pattern)
 */
public class SubsystemDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║                                                      ║");
        System.out.println("║   WEEK 13-03: TIGHTLY COUPLED SUBSYSTEMS             ║");
        System.out.println("║   (ANTI-PATTERN DEMONSTRATION)                       ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        System.out.println("\nThis demo shows PROBLEMS with tightly coupled subsystems:\n");
        System.out.println("  1. Client knows ALL subsystem internals");
        System.out.println("  2. Client manages initialization ORDER");
        System.out.println("  3. Client coordinates actions across subsystems");
        System.out.println("  4. Client handles cleanup order");
        System.out.println("  5. Subsystem changes affect ALL clients");

        // ════════════════════════════════════════════════════════════
        // Client creates ALL subsystem objects directly
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" CLIENT CREATES ALL SUBSYSTEMS DIRECTLY");
        System.out.println("═".repeat(60));

        System.out.println("\n// Client code must create each subsystem:");
        System.out.println("BattleAnimationSystem animation = new BattleAnimationSystem();");
        System.out.println("BattleSoundSystem sound = new BattleSoundSystem();");
        System.out.println("BattleUISystem ui = new BattleUISystem();\n");

        BattleAnimationSystem animation = new BattleAnimationSystem();
        BattleSoundSystem sound = new BattleSoundSystem();
        BattleUISystem ui = new BattleUISystem();

        // ════════════════════════════════════════════════════════════
        // PROBLEM: Client must know correct initialization ORDER
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" PROBLEM: Client must know CORRECT initialization order!");
        System.out.println("═".repeat(60));

        System.out.println("\n┌─────────────────────────────────────────────────┐");
        System.out.println("│ Animation Subsystem (2 steps in specific order!)│");
        System.out.println("└─────────────────────────────────────────────────┘\n");

        animation.init();
        animation.loadBattleSprites();

        System.out.println("\n┌─────────────────────────────────────────────────┐");
        System.out.println("│ Sound Subsystem (2 steps in specific order!)    │");
        System.out.println("└─────────────────────────────────────────────────┘\n");

        sound.init();
        sound.loadBattleSounds();
        sound.playBattleMusic("boss_battle.mp3");

        System.out.println("\n┌─────────────────────────────────────────────────┐");
        System.out.println("│ UI Subsystem (3 steps in specific order!)       │");
        System.out.println("└─────────────────────────────────────────────────┘\n");

        ui.init();
        ui.createBattleUI();
        ui.showBattleScreen();

        // ════════════════════════════════════════════════════════════
        // Show what happens with WRONG order
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" DEMO: What happens if order is WRONG?");
        System.out.println("═".repeat(60));

        System.out.println("\n// Creating a new UI system with WRONG order:\n");

        BattleUISystem badUI = new BattleUISystem();
        System.out.println("// Oops! Trying to show battle screen before creating UI...\n");
        badUI.showBattleScreen();  // ERROR!
        System.out.println("\n// Client made a mistake - hard to debug!\n");

        // ════════════════════════════════════════════════════════════
        // PROBLEM: Client must coordinate battle actions manually
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" PROBLEM: Client coordinates battle actions manually");
        System.out.println("═".repeat(60));

        System.out.println("\n// For EACH action, client must call ALL systems:\n");

        // Show initial state
        ui.updateHealthBars(100, 500);
        ui.showActionMenu();

        // --- Player Attack ---
        System.out.println("\n┌─── Player chooses: ATTACK ────────────────────────┐\n");

        // Client must coordinate ALL THREE subsystems!
        animation.playAttackAnimation("Player");
        sound.playAttackSound();
        animation.playDamageAnimation("Boss", 45);
        ui.showDamageNumber("Boss", 45);
        ui.updateHealthBars(100, 455);

        System.out.println("\n// That was 5 method calls just for ONE attack!\n");

        // --- Boss Turn ---
        System.out.println("┌─── Boss Turn ─────────────────────────────────────┐\n");

        sound.playBossRoar();
        animation.playAttackAnimation("Boss");
        sound.playAttackSound();
        animation.playDamageAnimation("Player", 30);
        ui.showDamageNumber("Player", 30);
        ui.updateHealthBars(70, 455);

        System.out.println("\n└────────────────────────────────────────────────────┘");

        // --- Player Magic ---
        System.out.println("\n┌─── Player chooses: MAGIC ─────────────────────────┐\n");

        animation.playMagicAnimation("Player");
        sound.playMagicSound();
        animation.playDamageAnimation("Boss", 80);
        ui.showDamageNumber("Boss", 80);
        ui.updateHealthBars(70, 375);

        System.out.println("\n└────────────────────────────────────────────────────┘");

        // ════════════════════════════════════════════════════════════
        // PROBLEM: Client must handle cleanup in correct order
        // ════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println(" PROBLEM: Client must cleanup in CORRECT order!");
        System.out.println("═".repeat(60));

        System.out.println("\n// Cleanup must be done for ALL subsystems:\n");

        sound.stopMusic();
        ui.cleanup();
        sound.cleanup();
        animation.cleanup();

        // ════════════════════════════════════════════════════════════
        // Summary
        // ════════════════════════════════════════════════════════════
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                  PROBLEMS IDENTIFIED                 ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Client Complexity                                 ║");
        System.out.println("║    Must know internals of 3 subsystems!              ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Initialization Order                              ║");
        System.out.println("║    7+ method calls in specific sequence!             ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Coordination Burden                               ║");
        System.out.println("║    5+ calls for each battle action!                  ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Error Prone                                       ║");
        System.out.println("║    Wrong order = silent failures!                    ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Tight Coupling                                    ║");
        System.out.println("║    Any subsystem change breaks ALL clients!          ║");
        System.out.println("║                                                      ║");
        System.out.println("║  ✗ Code Duplication                                  ║");
        System.out.println("║    Same coordination code everywhere!                ║");
        System.out.println("║                                                      ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  SOLUTION: Facade Pattern                            ║");
        System.out.println("║  See branch: 13-04-facade-pattern                    ║");
        System.out.println("║                                                      ║");
        System.out.println("║  With BattleFacade:                                  ║");
        System.out.println("║    BattleFacade battle = new BattleFacade();         ║");
        System.out.println("║    battle.startBattle();    // ONE call!             ║");
        System.out.println("║    battle.playerAttack();   // ONE call!             ║");
        System.out.println("║    battle.endBattle();      // ONE call!             ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
}
