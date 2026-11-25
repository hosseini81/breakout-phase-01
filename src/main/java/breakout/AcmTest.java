package breakout;

import acm.graphics.GOval;
import breakout.model.Ball;

import javax.swing.JFrame;

import acm.program.GraphicsProgram;
import acm.graphics.GRect;

import breakout.config.GameConstants;
import breakout.model.Paddle;

import java.awt.Color;
import java.awt.event.KeyEvent;

import breakout.model.Brick;
import breakout.model.BrickField;

import breakout.storage.HighScoreStorage;
import breakout.storage.InMemoryScoreStorage;

public class AcmTest extends GraphicsProgram {

    private Paddle paddle;
    private GRect paddleShape;

    private Ball ball;
    private GOval ballShape;

    private volatile boolean ballLaunched = false;

    private int lives = GameConstants.INITIAL_LIVES;

    private BrickField brickField;
    private GRect[][] brickShapes;

    private int score = 0;

    private HighScoreStorage scoreStorage;

    private boolean gameWon = false;

    @Override
    public void run() {

        setSize(
                GameConstants.WINDOW_WIDTH + 16,
                GameConstants.WINDOW_HEIGHT + 39
        );

        scoreStorage = new InMemoryScoreStorage();

        brickField = new BrickField();

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

        paddle = new Paddle();

        ball = new Ball(paddle);

        ballShape = new GOval(
                ball.getX(),
                ball.getY(),
                ball.getRadius() * 2,
                ball.getRadius() * 2
        );

        ballShape.setFilled(true);
        ballShape.setFillColor(Color.RED);

        add(ballShape);

        paddleShape = new GRect(
                paddle.getX(),
                paddle.getY(),
                paddle.getWidth(),
                paddle.getHeight()
        );

        paddleShape.setFilled(true);
        paddleShape.setFillColor(Color.BLUE);

        add(paddleShape);

        addKeyListeners();

        while (true) {

            if (ballLaunched) {
                ball.move();
                handleWallCollisions();
                handleBrickCollisions();
                handleVictory();
                handlePaddleCollision();
                handleBallLost();
            } else {
                ball.followPaddle(paddle);
            }

            ballShape.setLocation(ball.getX(), ball.getY());

            pause(GameConstants.FRAME_DELAY_MS);
        }
    }


    private void handleWallCollisions() {

        // Left wall
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.reverseX();
        }

        // Right wall
        else if (ball.getX() + GameConstants.BALL_DIAMETER
                >= GameConstants.WINDOW_WIDTH) {

            ball.setX(
                    GameConstants.WINDOW_WIDTH
                            - GameConstants.BALL_DIAMETER
            );

            ball.reverseX();
        }

        // Top wall
        if (ball.getY() <= GameConstants.STATUS_BAR_HEIGHT) {
            ball.setY(GameConstants.STATUS_BAR_HEIGHT);
            ball.reverseY();
        }
    }

    private void handlePaddleCollision() {

        boolean movingDown = ball.getVy() > 0;

        boolean verticalOverlap =
                ball.getY() + GameConstants.BALL_DIAMETER >= paddle.getY()
                        && ball.getY() <= paddle.getY() + paddle.getHeight();

        boolean horizontalOverlap =
                ball.getX() + GameConstants.BALL_DIAMETER >= paddle.getX()
                        && ball.getX() <= paddle.getX() + paddle.getWidth();

        if (movingDown && verticalOverlap && horizontalOverlap) {

            ball.setY(
                    paddle.getY() - GameConstants.BALL_DIAMETER
            );

            ball.bounceOffPaddle(paddle);
        }
    }

    private void handleBrickCollisions() {

        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {

            for (int column = 0;
                 column < GameConstants.BRICK_COLUMNS;
                 column++) {

                Brick brick = brickField.getBrick(row, column);

                if (brick.isDestroyed()) {
                    continue;
                }

                double overlapX = Math.min(
                        ball.getX() + GameConstants.BALL_DIAMETER,
                        brick.getX() + brick.getWidth()
                ) - Math.max(
                        ball.getX(),
                        brick.getX()
                );

                double overlapY = Math.min(
                        ball.getY() + GameConstants.BALL_DIAMETER,
                        brick.getY() + brick.getHeight()
                ) - Math.max(
                        ball.getY(),
                        brick.getY()
                );

                if (overlapX > 0 && overlapY > 0) {

                    if (overlapX < overlapY) {

                        if (ball.getX() + ball.getRadius()
                                < brick.getX() + brick.getWidth() / 2.0) {

                            ball.setX(
                                    brick.getX() - GameConstants.BALL_DIAMETER
                            );

                        } else {

                            ball.setX(
                                    brick.getX() + brick.getWidth()
                            );
                        }

                        ball.reverseX();

                    } else {

                        if (ball.getY() + ball.getRadius()
                                < brick.getY() + brick.getHeight() / 2.0) {

                            ball.setY(
                                    brick.getY() - GameConstants.BALL_DIAMETER
                            );

                        } else {

                            ball.setY(
                                    brick.getY() + brick.getHeight()
                            );
                        }

                        ball.reverseY();
                    }

                    score += brick.hit();

                    remove(brickShapes[row][column]);

                    System.out.println("Score: " + score);

                    return;
                }
            }
        }
    }

    private void handleBallLost() {

        if (ball.getY() + GameConstants.BALL_DIAMETER
                > GameConstants.WINDOW_HEIGHT) {

            lives--;
            ballLaunched = false;

            if (lives > 0) {
                ball.setVx(0.0);
                ball.setVy(0.0);

                ball.followPaddle(paddle);

            } else {

                scoreStorage.saveHighScore(score);

                System.out.println("GAME OVER!");

                System.out.println(
                        "High Score: " + scoreStorage.loadHighScore()
                );
            }
        }
    }


    private void handleVictory() {

        if (!gameWon && brickField.getRemainingBrickCount() == 0) {

            gameWon = true;
            ballLaunched = false;

            ball.setVx(0.0);
            ball.setVy(0.0);

            scoreStorage.saveHighScore(score);

            System.out.println("VICTORY!");

            System.out.println(
                    "Final Score: " + score
            );

            System.out.println(
                    "High Score: " + scoreStorage.loadHighScore()
            );
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddle.moveLeft();

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.moveRight();

        } else if (e.getKeyCode() == KeyEvent.VK_SPACE
                && !ballLaunched && lives > 0 && !gameWon) {

            ball.launch();
            ballLaunched = true;
        }

        paddleShape.setLocation(
                paddle.getX(),
                paddle.getY()
        );
    }

    @Override
    protected JFrame createProgramFrame() {
        JFrame frame = super.createProgramFrame();
        frame.setResizable(false);
        return frame;
    }

    public static void main(String[] args) {
        new AcmTest().start(args);
    }
}