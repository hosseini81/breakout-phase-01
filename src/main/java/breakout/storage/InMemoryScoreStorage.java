package breakout.storage;

public class InMemoryScoreStorage implements HighScoreStorage {

    private int highScore = 0;

    @Override
    public int loadHighScore() {
        return highScore;
    }

    @Override
    public void saveHighScore(int score) {

        if (score > highScore) {
            highScore = score;
        }
    }
}