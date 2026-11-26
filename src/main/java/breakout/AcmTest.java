package breakout;

import breakout.model.Ball;

import javax.swing.JFrame;

import acm.program.GraphicsProgram;

import breakout.config.GameConstants;
import breakout.model.Paddle;

import java.awt.Color;
import java.awt.event.KeyEvent;

import breakout.model.Brick;
import breakout.model.BrickField;

import breakout.storage.HighScoreStorage;
import breakout.storage.InMemoryScoreStorage;

import breakout.model.GameState;

import acm.graphics.GCanvas;
import breakout.view.BreakoutCanvas;

public class AcmTest extends GraphicsProgram {

    private Paddle paddle;


    private Ball ball;

    private int lives = GameConstants.INITIAL_LIVES;

    private BrickField brickField;

    private int score = 0;

    private HighScoreStorage scoreStorage;

    private volatile GameState gameState = GameState.READY;

    private BreakoutCanvas breakoutCanvas;

    @Override
    public void run() {

        setSize(
                GameConstants.WINDOW_WIDTH + 16,
                GameConstants.WINDOW_HEIGHT + 39
        );

        scoreStorage = new InMemoryScoreStorage();

        brickField = new BrickField();
        breakoutCanvas.drawBricks(brickField);

        paddle = new Paddle();

        ball = new Ball(paddle);

        breakoutCanvas.drawBall(ball);
        breakoutCanvas.drawPaddle(paddle);

        breakoutCanvas.drawStatusBar();

        breakoutCanvas.updateStatusBar(
                score,
                lives,
                scoreStorage.loadHighScore()
        );

        addKeyListeners();

        while (true) {

            if (gameState == GameState.READY) {

                ball.followPaddle(paddle);

            } else if (gameState == GameState.RUNNING) {

                ball.move();

                handleWallCollisions();

                handleBrickCollisions();

                handlePaddleCollision();

                handleBallLost();

                handleVictory();
            }

            breakoutCanvas.updateBall(ball);

            breakoutCanvas.updateStatusBar(
                    score,
                    lives,
                    scoreStorage.loadHighScore()
            );

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

                    breakoutCanvas.removeBrick(row, column);

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

            if (lives > 0) {

                ball.setVx(0.0);
                ball.setVy(0.0);

                ball.followPaddle(paddle);

                gameState = GameState.READY;

            } else {

                gameState = GameState.GAME_OVER;

                scoreStorage.saveHighScore(score);

                breakoutCanvas.showMessage(
                        "GAME OVER - Press R to Restart"
                );
            }
        }
    }


    private void handleVictory() {

        if (gameState == GameState.RUNNING
                && brickField.getRemainingBrickCount() == 0) {

            gameState = GameState.VICTORY;

            ball.setVx(0.0);
            ball.setVy(0.0);

            scoreStorage.saveHighScore(score);

            breakoutCanvas.showMessage(
                    "VICTORY - Press R to Restart"
            );
        }
    }


    private void restartGame() {

        // Return to READY state
        gameState = GameState.READY;
        breakoutCanvas.clearMessage();

        // Remove existing brick shapes
        breakoutCanvas.clearBricks();

        // Reset paddle and ball
        paddle.reset();
        ball.reset(paddle);

        // Reset bricks
        brickField.reset();
        breakoutCanvas.drawBricks(brickField);

        // Reset player information
        lives = GameConstants.INITIAL_LIVES;
        score = 0;

        // Update graphical objects
        breakoutCanvas.updatePaddle(paddle);

        breakoutCanvas.updateBall(ball);
    }


    @Override
    public void keyPressed(KeyEvent e) {

        // Restart game from any state
        if (e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
            return;
        }

        if (gameState == GameState.READY
                || gameState == GameState.RUNNING) {

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                paddle.moveLeft();

            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                paddle.moveRight();

            } else if (e.getKeyCode() == KeyEvent.VK_SPACE
                    && gameState == GameState.READY
                    && lives > 0) {

                ball.launch();
                gameState = GameState.RUNNING;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {

            if (gameState == GameState.RUNNING) {

                gameState = GameState.PAUSED;

                breakoutCanvas.showMessage(
                        "PAUSED - Press P to Resume"
                );

            } else if (gameState == GameState.PAUSED) {

                gameState = GameState.RUNNING;

                breakoutCanvas.clearMessage();
            }
        }

        breakoutCanvas.updatePaddle(paddle);
    }


    @Override
    protected GCanvas createGCanvas() {

        breakoutCanvas = new BreakoutCanvas();

        return breakoutCanvas;
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