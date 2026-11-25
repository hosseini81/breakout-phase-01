package breakout.model;

import breakout.config.GameConstants;

public class Paddle {

    // Position of the paddle
    private int x;
    private final int y;

    // Constructor
    public Paddle() {
        this.x = (GameConstants.WINDOW_WIDTH
                - GameConstants.PADDLE_WIDTH) / 2;

        this.y = GameConstants.PADDLE_Y;
    }

    // Move paddle to the left
    public void moveLeft() {
        x = Math.max(0, x - GameConstants.PADDLE_SPEED);
    }

    // Move paddle to the right
    public void moveRight() {
        int maxX = GameConstants.WINDOW_WIDTH
                - GameConstants.PADDLE_WIDTH;

        x = Math.min(maxX, x + GameConstants.PADDLE_SPEED);
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return GameConstants.PADDLE_WIDTH;
    }

    public int getHeight() {
        return GameConstants.PADDLE_HEIGHT;
    }
}