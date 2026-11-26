package breakout;

import acm.graphics.GCanvas;
import acm.program.GraphicsProgram;

import breakout.config.GameConstants;
import breakout.controller.GameEngine;
import breakout.storage.HighScoreStorage;
import breakout.storage.InMemoryScoreStorage;
import breakout.view.BreakoutCanvas;

import javax.swing.JFrame;
import java.awt.event.KeyEvent;

public class AcmTest extends GraphicsProgram {

    private BreakoutCanvas breakoutCanvas;
    private GameEngine gameEngine;

    @Override
    public void run() {

        setSize(
                GameConstants.WINDOW_WIDTH
                        + GameConstants.WINDOW_FRAME_EXTRA_WIDTH,

                GameConstants.WINDOW_HEIGHT
                        + GameConstants.WINDOW_FRAME_EXTRA_HEIGHT
        );

        HighScoreStorage scoreStorage =
                new InMemoryScoreStorage();

        gameEngine = new GameEngine(
                breakoutCanvas,
                scoreStorage
        );

        addKeyListeners();

        gameEngine.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (gameEngine != null) {
            gameEngine.handleKeyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (gameEngine != null) {
            gameEngine.handleKeyReleased(e);
        }
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