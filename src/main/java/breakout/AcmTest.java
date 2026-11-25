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

public class AcmTest extends GraphicsProgram {

    private Paddle paddle;
    private GRect paddleShape;

    private Ball ball;
    private GOval ballShape;

    private volatile boolean ballLaunched = false;

    private int lives = GameConstants.INITIAL_LIVES;

    @Override
    public void run() {

        setSize(
                GameConstants.WINDOW_WIDTH + 16,
                GameConstants.WINDOW_HEIGHT + 39
        );


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
                System.out.println("GAME OVER!");
            }
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddle.moveLeft();

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.moveRight();

        } else if (e.getKeyCode() == KeyEvent.VK_SPACE
                && !ballLaunched && lives > 0) {

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