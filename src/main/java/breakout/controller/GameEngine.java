package breakout.controller;

import breakout.config.GameConstants;

import breakout.model.Ball;
import breakout.model.Paddle;
import breakout.model.BrickField;
import breakout.model.GameState;

import breakout.storage.HighScoreStorage;

import breakout.view.BreakoutCanvas;

import breakout.model.Brick;

import java.awt.event.KeyEvent;

public class GameEngine {

    // View
    private final BreakoutCanvas view;

    // Model
    private final Paddle paddle;
    private final Ball ball;
    private final BrickField brickField;

    // Storage
    private final HighScoreStorage scoreStorage;

    // Game state
    private volatile GameState gameState = GameState.READY;

    private int lives = GameConstants.INITIAL_LIVES;
    private int score = 0;

    private boolean pauseKeyPressed = false;
    private boolean restartKeyPressed = false;


    public GameEngine(
            BreakoutCanvas view,
            HighScoreStorage scoreStorage
    ) {

        // Connect View and Storage
        this.view = view;
        this.scoreStorage = scoreStorage;

        // Create Model objects
        this.paddle = new Paddle();

        this.ball = new Ball(paddle);

        this.brickField = new BrickField();

        // Initialize View
        view.drawBricks(brickField);

        view.drawPaddle(paddle);

        view.drawBall(ball);

        view.drawStatusBar();

        view.updateStatusBar(
                score,
                lives,
                scoreStorage.loadHighScore()
        );
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

                    view.removeBrick(row, column);

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

                ball.reset(paddle);

                gameState = GameState.READY;

            } else {

                gameState = GameState.GAME_OVER;

                scoreStorage.saveHighScore(score);

                view.showMessage(
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

            view.showMessage(
                    "VICTORY - Press R to Restart"
            );
        }
    }

    private void restartGame() {

        // Return to READY state
        gameState = GameState.READY;

        // Clear previous messages and bricks
        view.clearMessage();

        // Reset model objects
        paddle.reset();
        ball.reset(paddle);
        brickField.reset();

        // Reset player information
        lives = GameConstants.INITIAL_LIVES;
        score = 0;

        // Redraw bricks
        view.drawBricks(brickField);

        // Update graphical objects
        view.updatePaddle(paddle);
        view.updateBall(ball);

        // Update status bar
        view.updateStatusBar(
                score,
                lives,
                scoreStorage.loadHighScore()
        );
    }


    public synchronized void handleKeyPressed(KeyEvent e) {

        // Restart from any state
        if (e.getKeyCode() == KeyEvent.VK_R) {

            if (!restartKeyPressed) {
                restartKeyPressed = true;
                restartGame();
            }

            return;
        }

        // Handle movement and launch
        if (gameState == GameState.READY
                || gameState == GameState.RUNNING) {

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {

                paddle.moveLeft();

            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

                paddle.moveRight();

            } else if (e.getKeyCode() == KeyEvent.VK_SPACE
                    && gameState == GameState.READY) {

                ball.launch();
                gameState = GameState.RUNNING;
            }
        }

        // Handle pause and resume
        if (e.getKeyCode() == KeyEvent.VK_P
                && !pauseKeyPressed) {

            pauseKeyPressed = true;

            if (gameState == GameState.RUNNING) {

                gameState = GameState.PAUSED;

                view.showMessage(
                        "PAUSED - Press P to Resume"
                );

            } else if (gameState == GameState.PAUSED) {

                gameState = GameState.RUNNING;

                view.clearMessage();
            }
        }

        // Update paddle display
        view.updatePaddle(paddle);
    }

    public void start() {

        final long frameDurationNanos =
                1_000_000_000L / GameConstants.FPS;

        while (!Thread.currentThread().isInterrupted()) {

            long frameStart = System.nanoTime();

            updateGameFrame();

            // Calculate remaining frame time
            long elapsedNanos =
                    System.nanoTime() - frameStart;

            long remainingNanos =
                    frameDurationNanos - elapsedNanos;

            // Wait only if the frame finished early
            if (remainingNanos > 0) {

                long millis =
                        remainingNanos / 1_000_000L;

                int nanos =
                        (int) (remainingNanos % 1_000_000L);

                try {

                    Thread.sleep(millis, nanos);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private synchronized void updateGameFrame() {

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

        // Update graphical objects
        view.updateBall(ball);

        view.updatePaddle(paddle);

        view.updateStatusBar(
                score,
                lives,
                scoreStorage.loadHighScore()
        );
    }

    public synchronized void handleKeyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_P) {
            pauseKeyPressed = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            restartKeyPressed = false;
        }
    }
}