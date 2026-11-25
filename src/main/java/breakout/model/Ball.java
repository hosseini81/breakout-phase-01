package breakout.model;

import breakout.config.GameConstants;

public class Ball {

    // Ball position (top-left corner of its bounding box)
    private double x;
    private double y;

    // Ball velocity (pixels per tick)
    private double vx;
    private double vy;


    public Ball(Paddle paddle) {
        followPaddle(paddle);

        this.vx = 0.0;
        this.vy = 0.0;
    }

    public void followPaddle(Paddle paddle) {

        this.x = paddle.getX()
                + (paddle.getWidth() - GameConstants.BALL_DIAMETER) / 2.0;

        this.y = paddle.getY() - GameConstants.BALL_DIAMETER;
    }

    public void move() {
        x += vx;
        y += vy;
    }

    public void reverseX() {
        vx = -vx;
    }

    public void reverseY() {
        vy = -vy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public int getRadius() {
        return GameConstants.BALL_RADIUS;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void launch() {
        vx = 0.0;
        vy = -GameConstants.BALL_INITIAL_SPEED;
    }

    public void bounceOffPaddle(Paddle paddle) {

        double paddleCenter = paddle.getX()
                + paddle.getWidth() / 2.0;

        double ballCenter = getX() + getRadius();

        double deltaX = ballCenter - paddleCenter;

        double halfWidth = paddle.getWidth() / 2.0;

        double offset = deltaX / halfWidth;

        offset = Math.max(-1.0, Math.min(1.0, offset));

        double angle = offset * Math.toRadians(60.0);

        double speed = Math.hypot(vx, vy);

        vx = speed * Math.sin(angle);

        vy = -Math.abs(speed * Math.cos(angle));
    }
}

