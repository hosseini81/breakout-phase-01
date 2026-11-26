package breakout.config;

import java.awt.Color;

public final class GameConstants {

    // Window dimensions
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int STATUS_BAR_HEIGHT = 35;

    // ACM window frame adjustments
    public static final int WINDOW_FRAME_EXTRA_WIDTH = 16;
    public static final int WINDOW_FRAME_EXTRA_HEIGHT = 39;

    // Game loop
    public static final int FPS = 60;

    // Paddle dimensions and movement
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 15;
    public static final int PADDLE_Y = 545;
    public static final int PADDLE_SPEED = 8;

    // Ball dimensions and movement
    public static final int BALL_RADIUS = 8;
    public static final int BALL_DIAMETER = BALL_RADIUS * 2;
    public static final double BALL_INITIAL_SPEED = 6.0;
    public static final double MAX_PADDLE_BOUNCE_ANGLE_DEGREES = 60.0;

    // Bricks configuration
    public static final int BRICK_ROWS = 5;
    public static final int BRICK_COLUMNS = 8;
    public static final int BRICK_WIDTH = 85;
    public static final int BRICK_HEIGHT = 25;
    public static final int BRICK_GAP = 6;
    public static final int BRICK_TOP_MARGIN = 60;
    public static final int BRICK_POINTS = 100;
    public static final int BRICK_LEFT_MARGIN = 39;

    // Player configuration
    public static final int INITIAL_LIVES = 3;

    // HUD configuration
    public static final int HUD_SCORE_X = 20;
    public static final int HUD_LIVES_X = 330;
    public static final int HUD_HIGH_SCORE_X = 580;
    public static final int HUD_BASELINE_Y = 23;

    public static final int HUD_FONT_SIZE = 14;
    public static final int MESSAGE_FONT_SIZE = 28;

    // Game colors
    public static final Color PADDLE_COLOR = Color.BLUE;
    public static final Color BALL_COLOR = Color.RED;
    public static final Color BRICK_COLOR = Color.ORANGE;

    public static final Color STATUS_BAR_COLOR = Color.DARK_GRAY;
    public static final Color HUD_TEXT_COLOR = Color.WHITE;
    public static final Color MESSAGE_TEXT_COLOR = Color.BLACK;

    private GameConstants() {
        // Prevent creating objects of this class
    }
}