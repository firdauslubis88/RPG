package events;

/**
 * Week 11-04: CoinCollectedEvent (Observer Pattern)
 *
 * Published when the player collects a coin.
 *
 * Listeners:
 * - SoundSystem: Plays coin collection sound
 * - AchievementSystem: Tracks coin count for "Coin Collector" achievement
 */
public class CoinCollectedEvent extends GameEvent {
    private final int coinValue;
    private final int totalScore;

    public CoinCollectedEvent(int coinValue, int totalScore) {
        super("CoinCollected");
        this.coinValue = coinValue;
        this.totalScore = totalScore;
    }

    public int getCoinValue() {
        return coinValue;
    }

    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public String toString() {
        return String.format("CoinCollectedEvent[value=%d, totalScore=%d]",
            coinValue, totalScore);
    }
}
