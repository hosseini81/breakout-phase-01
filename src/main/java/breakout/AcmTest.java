package breakout;

import acm.program.GraphicsProgram;
import acm.graphics.GOval;

import java.awt.Color;

public class AcmTest extends GraphicsProgram {

    @Override
    public void run() {
        GOval ball = new GOval(100, 100, 40, 40);

        ball.setFilled(true);
        ball.setFillColor(Color.BLUE);

        add(ball);
    }

    public static void main(String[] args) {
        new AcmTest().start(args);
    }
}