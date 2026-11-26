package breakout.controller;

import breakout.config.GameConstants;

import breakout.model.Ball;
import breakout.model.Paddle;
import breakout.model.BrickField;
import breakout.model.GameState;

import breakout.storage.HighScoreStorage;

import breakout.view.BreakoutCanvas;

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

}