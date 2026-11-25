package breakout.config;

public final class GameConstants {

    // Window dimensions
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int STATUS_BAR_HEIGHT = 35;

    // Game loop
    public static final int FPS = 60;
    public static final double FRAME_DELAY_MS = 1000.0 / FPS;

    // Paddle dimensions and movement
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 15;
    public static final int PADDLE_Y = 545;
    public static final int PADDLE_SPEED = 8;

    // Ball dimensions and movement
    public static final int BALL_RADIUS = 8;
    public static final int BALL_DIAMETER = BALL_RADIUS * 2;
    public static final double BALL_INITIAL_SPEED = 6.0;

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

    private GameConstants() {
        // Prevent creating objects of this class
    }
}