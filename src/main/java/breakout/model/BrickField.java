package breakout.model;

import breakout.config.GameConstants;


public class BrickField {
    private final Brick[][] bricks;

    public BrickField() {

        bricks = new Brick[
                GameConstants.BRICK_ROWS
                ][
                GameConstants.BRICK_COLUMNS
                ];

        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {

            for (int column = 0;
                 column < GameConstants.BRICK_COLUMNS;
                 column++) {

                bricks[row][column] = new Brick(row, column);
            }
        }
    }

    public Brick getBrick(int row, int column) {
        return bricks[row][column];
    }

    public int getRemainingBrickCount() {

        int count = 0;

        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {

            for (int column = 0;
                 column < GameConstants.BRICK_COLUMNS;
                 column++) {

                if (!bricks[row][column].isDestroyed()) {
                    count++;
                }
            }
        }

        return count;
    }
}
