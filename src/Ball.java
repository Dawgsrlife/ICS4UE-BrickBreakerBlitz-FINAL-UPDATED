/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: The Ball class is a subclass of the GameObject class
 * and represents the behaviour of a ball in the game of BrickBreaker.
 * It contains state variables for the ball's horizontal and vertical direction
 * and implements the act() method to update the ball's position on each frame.
 * Furthermore, it provides methods to change the direction of the ball
 * when it collides with a paddle/wall/brick, etc.
 */

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ball extends GameObject {
    // Image:
    private BufferedImage image;

    // State Variables:
    private int xSpawn, ySpawn;
    private int angle;
    private double speed, transferredSpeed = 0;
    private double exactX, exactY;

    // Colour for the paint method:
    private Color c;

    /**
     * Constructor for the Ball class, which sets the position, dimensions, colour, and the starting speed and angle of the ball
     * @param x --> x-position
     * @param y --> y-position
     * @param size --> width and height of the ball
     * @param c --> colour of the ball
     * @param speed --> starting speed
     * @param angle --> starting angle
     */
    public Ball(int x, int y, int size, Color c, double speed, int angle) {
        // Routing the fundamental fields:
        setSize(size, size);
        setX(x);
        setY(y);
        exactX = x;
        exactY = y;
        xSpawn = x;
        ySpawn = y;
        this.c = c;
        this.speed = speed;
        this.angle = angle;

        // Creating the ball image:
        try {
            image = ImageIO.read(new File("src/PNG/58-Breakout-Tiles.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells the playing field what to do from one moment to the next.
     */
    public void act() {
        while (angle >= 360) angle -= 360;
        while (angle < 0) angle += 360;
        exactX += (Math.cos(Math.toRadians(angle)) * speed);
        exactY += (Math.sin(Math.toRadians(angle)) * speed);
        setX((int)Math.round(exactX));
        setY((int)Math.round(exactY));
    }

    /**
     * setter for the angle of the ball's movement (works like the cartesian plane)
     * @param angle the new angle to be set
     */
    public void setAngle(int angle) {
        this.angle = angle;
    }

    /**
     * getter for the angle of the ball's movement
     * @return angle of the ball's movement
     */
    public int getAngle() {
        return angle;
    }

    /**
     * setter for the speed of the ball
     * @param speed the new speed of the ball
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * getter for the speed of the ball
     * @return the speed of the ball, (this is not separated by x and y movements)
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * setter for the  transferred onto the ball
     * @param tSpd the transferred speed on the ball (from the paddle)
     */
    public void setTransferredSpeed(double tSpd) {
        transferredSpeed = tSpd;
    }

    /**
     * getter for the speed of the ball
     * @return the speed of the ball, (this is not separated by x and y movements)
     */
    public double getTransferredSpeed() {
        return transferredSpeed;
    }

    /**
     * Changes the angle of the ball as if it were bouncing off an object to its top
     */
    public void bounceOnTop() {
        // due to the coordinate system of the display, the degrees are upside down (270 on top, 90 below)
        // angle must be in the set (180,360)
        // (270 becomes 90, 210 becomes 150, 315 becomes 45)
        if (angle > 180 && angle < 360)
            setAngle(360 - angle);
    }

    /**
     * Changes the angle of the ball as if it were bouncing off an object to its bottom
     */
    public void bounceOnBottom() {
        // angle must be in the set (0,180)
        // (90 becomes 270, 120 becomes 240, 60 becomes 300)
        // works the same way as bounceBallTop()
        if (angle > 0 && angle < 180)
            setAngle(360 - angle);
    }

    /**
     * Changes the angle of the ball as if it were bouncing off an object to its left
     */
    public void bounceOnLeft() {
        // collision with na object to the left of a ball, causing it to bounce toward the right
        // angle must be in the set (90,270)
        // (180 becomes 0, 240 becomes 300, 150 becomes 30)
        if (angle > 90 && angle < 180)
            setAngle(180 - angle);
        else if (angle > 90 && angle < 270)
            setAngle(540 - angle);
    }

    /**
     * Changes the angle of the ball as if it were bouncing off an object to its right
     */
    public void bounceOnRight() {
        // collision with an object to the right of a ball, causing it to bounce toward the left
        // angle must be in the sets (270,360]∪[0, 90)
        // (0 & 360 become 180, 315 becomes 225, 60 becomes 120)
        if (angle >= 0 && angle < 90)
            setAngle(180 - angle);
        else if (angle > 270 && angle <= 360)
            setAngle(540 - angle);
    }

    /**
     * reverses the angle of the ball, makes it go backward
     */
    public void reverseAngle() {
        setAngle(angle + (angle > 180 ? -180 : 180));
    }

    /**
     * Resets the position and velocity of the ball
     */
    public void reset() {
        angle = 0;
        speed = 2;
        setX(xSpawn);
        setY(ySpawn);
    }

    /**
     * Overrides the paint method to draw an image instead of filling a round-rectangle.
     *
     * @param g --> the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
