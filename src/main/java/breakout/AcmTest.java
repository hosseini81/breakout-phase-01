package breakout;

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

    @Override
    public void run() {

        setSize(
                GameConstants.WINDOW_WIDTH + 16,
                GameConstants.WINDOW_HEIGHT + 39
        );


        paddle = new Paddle();

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
    }


    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddle.moveLeft();
        }

        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.moveRight();
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