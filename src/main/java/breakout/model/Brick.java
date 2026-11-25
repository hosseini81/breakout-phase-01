package breakout.model;

import breakout.config.GameConstants;

public class Brick {

    // Position of the brick
    private int x;
    private int y;

    // Destruction state
    private boolean destroyed;


    public Brick(int row, int column) {

        this.x = GameConstants.BRICK_LEFT_MARGIN
                + column * (GameConstants.BRICK_WIDTH + GameConstants.BRICK_GAP);

        this.y = GameConstants.BRICK_TOP_MARGIN
                + row * (GameConstants.BRICK_HEIGHT + GameConstants.BRICK_GAP);

        this.destroyed = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return GameConstants.BRICK_WIDTH;
    }

    public int getHeight() {
        return GameConstants.BRICK_HEIGHT;
    }

    public boolean isDestroyed() {
        return destroyed;
    }


    public int hit() {

        if (destroyed) {
            return 0;
        }

        destroyed = true;

        return GameConstants.BRICK_POINTS;
    }
}