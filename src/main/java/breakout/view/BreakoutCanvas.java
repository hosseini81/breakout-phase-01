package breakout.view;

import acm.graphics.GCanvas;

import acm.graphics.GRect;
import breakout.model.Paddle;

import java.awt.Color;

import acm.graphics.GOval;
import breakout.model.Ball;

import breakout.config.GameConstants;
import breakout.model.Brick;
import breakout.model.BrickField;

import acm.graphics.GLabel;
import java.awt.Font;


public class BreakoutCanvas extends GCanvas {

    private GRect paddleShape;

    private GOval ballShape;

    private GRect[][] brickShapes;

    private GRect statusBarShape;

    private GLabel scoreLabel;
    private GLabel livesLabel;
    private GLabel highScoreLabel;

    private GLabel messageLabel;

    public void drawPaddle(Paddle paddle) {

        paddleShape = new GRect(
                paddle.getX(),
                paddle.getY(),
                paddle.getWidth(),
                paddle.getHeight()
        );

        paddleShape.setFilled(true);
        paddleShape.setFillColor(Color.BLUE);

        add(paddleShape);
    }

    public void updatePaddle(Paddle paddle) {

        paddleShape.setLocation(
                paddle.getX(),
                paddle.getY()
        );
    }

    public void drawBall(Ball ball) {

        ballShape = new GOval(
                ball.getX(),
                ball.getY(),
                ball.getRadius() * 2,
                ball.getRadius() * 2
        );

        ballShape.setFilled(true);
        ballShape.setFillColor(Color.RED);

        add(ballShape);
    }

    public void updateBall(Ball ball) {

        ballShape.setLocation(
                ball.getX(),
                ball.getY()
        );
    }

    public void drawBricks(BrickField brickField) {

        brickShapes = new GRect[
                GameConstants.BRICK_ROWS
                ][
                GameConstants.BRICK_COLUMNS
                ];

        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {

            for (int column = 0;
                 column < GameConstants.BRICK_COLUMNS;
                 column++) {

                Brick brick = brickField.getBrick(row, column);

                GRect shape = new GRect(
                        brick.getX(),
                        brick.getY(),
                        brick.getWidth(),
                        brick.getHeight()
                );

                shape.setFilled(true);
                shape.setFillColor(Color.ORANGE);

                add(shape);

                brickShapes[row][column] = shape;
            }
        }
    }

    public void removeBrick(int row, int column) {

        if (brickShapes[row][column] != null) {
            remove(brickShapes[row][column]);
            brickShapes[row][column] = null;
        }
    }

    public void clearBricks() {

        if (brickShapes == null) {
            return;
        }

        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {

            for (int column = 0;
                 column < GameConstants.BRICK_COLUMNS;
                 column++) {

                if (brickShapes[row][column] != null) {
                    remove(brickShapes[row][column]);
                    brickShapes[row][column] = null;
                }
            }
        }
    }

    public void drawStatusBar() {

        // Create status bar background
        statusBarShape = new GRect(
                0,
                0,
                GameConstants.WINDOW_WIDTH,
                GameConstants.STATUS_BAR_HEIGHT
        );

        statusBarShape.setFilled(true);
        statusBarShape.setFillColor(Color.DARK_GRAY);

        add(statusBarShape);

        // Configure HUD font
        Font hudFont = new Font(
                "SansSerif",
                Font.BOLD,
                14
        );

        // Score label
        scoreLabel = new GLabel("Score: 0", 20, 23);
        scoreLabel.setFont(hudFont);
        scoreLabel.setColor(Color.WHITE);
        add(scoreLabel);

        // Lives label
        livesLabel = new GLabel("Lives: 3", 330, 23);
        livesLabel.setFont(hudFont);
        livesLabel.setColor(Color.WHITE);
        add(livesLabel);

        // High score label
        highScoreLabel = new GLabel("High Score: 0", 580, 23);
        highScoreLabel.setFont(hudFont);
        highScoreLabel.setColor(Color.WHITE);
        add(highScoreLabel);
    }

    public void updateStatusBar(int score, int lives, int highScore) {

        scoreLabel.setLabel("Score: " + score);

        livesLabel.setLabel("Lives: " + lives);

        highScoreLabel.setLabel("High Score: " + highScore);
    }

    public void showMessage(String message) {

        // Remove previous message
        clearMessage();

        messageLabel = new GLabel(message);

        messageLabel.setFont(
                new Font("SansSerif", Font.BOLD, 28)
        );

        messageLabel.setColor(Color.BLACK);

        double x = (GameConstants.WINDOW_WIDTH
                - messageLabel.getWidth()) / 2.0;

        double y = GameConstants.WINDOW_HEIGHT / 2.0;

        messageLabel.setLocation(x, y);

        add(messageLabel);
    }

    public void clearMessage() {

        if (messageLabel != null) {

            remove(messageLabel);

            messageLabel = null;
        }
    }

}