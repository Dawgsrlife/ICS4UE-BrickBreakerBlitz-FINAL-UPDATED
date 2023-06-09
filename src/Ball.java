/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE by Mr. Benum
 * Description: The Ball class is a subclass of the GameObject class
 * and represents the behaviour of a ball in the game of BrickBreaker.
 * It contains state variables for the ball's horizontal and vertical direction
 * and implements the act() method to update the ball's position on each frame.
 * Furthermore, it provides methods to change the direction of the ball
 * when it collides with a paddle/wall/brick, etc.
 */
import java.awt.Color;
import java.awt.Graphics;

public class Ball extends GameObject {
    // State Variables
    private int xSpawn, ySpawn;
    private double xMov, yMov;

    // Colour for the paint method:
    private Color c;

    /**
     * Constructor to set the size, position, and colour of the ball.
     */
    public Ball(int x, int y, int size, Color c, double velocity) {
        setSize(size, size);
        setX(x);
        setY(y);
        xSpawn = x;
        ySpawn = y;
        this.c = c;
        xMov = velocity;
        yMov = velocity;
    }

    /**
     * Tells the game what to do before the actual play begins.
     */
    public void act() {
        setX((int) (getX() + xMov + 0.5));
        setY((int) (getY() + yMov + 0.5));
    }

    /**
     * Sets the x-movement factor of the ball.
     * @param n --> The movement factor (how much for every frame).
     */
    public void setXMov(double n) {
        xMov = n;
    }

    /**
     * Sets the y-movement factor of the ball.
     * @param n --> The movement factor (how much for every frame).
     */
    public void setYMov(double n) {
        yMov = n;
    }

    /**
     * Gets the x-movement factor of the ball.
     */
    public double getXMov() {
        return xMov;
    }

    /**
     * Gets the y-movement factor of the ball.
     */
    public double getYMov() {
        return yMov;
    }

    /**
     * Resets the position and velocity of the ball
     */
    public void reset() {
        xMov = 0;
        yMov = 0;
        setX(xSpawn);
        setY(ySpawn);
    }

    /**
     * Overrided method that paints a circle (round rectangle) for the ball rather than a rectangle.
     * @param g --> The <code>Graphics</code> context in which to paint.
     */
    public void paint(Graphics g) {
        g.setColor(c);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
    }
}
