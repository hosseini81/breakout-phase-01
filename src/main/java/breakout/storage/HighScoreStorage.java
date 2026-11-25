package breakout.storage;

public interface HighScoreStorage {

    int loadHighScore();

    void saveHighScore(int score);
}