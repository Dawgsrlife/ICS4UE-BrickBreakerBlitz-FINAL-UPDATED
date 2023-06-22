/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: The Paddle class extends GameObject.
 * It contains a two methods: moveLeft() and moveRight() to adjust the paddle's X-position.
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

public class Paddle extends GameObject {
    // Image:
    private BufferedImage image;

    // Edges:
    public Edge topEdge, leftEdge, rightEdge;

    // Lasers:
    private int LASER_LENGTH = 25, LASER_WIDTH = 5, LASER_SPEED = 4;
    public Laser leftBeam1, rightBeam1, leftBeam2, rightBeam2, leftBeam3, rightBeam3;

    // State Variables:
    private static double speed, exactX;

    /**
     * Constructor to set the position and dimensions of the paddle.
     * @param x --> x-position
     * @param y --> y-position
     * @param h --> paddle height
     * @param w --> paddle width
     * @param spd --> starting speed of the paddle
     */
    public Paddle (int x, int y, int h, int w, double spd) {
        // Routing the fundamental fields:
        setSize(w, h);
        setX(x);
        setY(y);
        speed = spd;
        exactX = x;

        // Creating the edges (each their own object) every time a Brick object is created:
        topEdge = new Edge(x, y, w, 1);
        leftEdge = new Edge(x, y, 1, h);
        rightEdge = new Edge(x + w - 1, y, 1, h);

        leftBeam1 = new Laser(0, 0, LASER_WIDTH, LASER_LENGTH, LASER_SPEED);
        rightBeam1 = new Laser(0, 0, LASER_WIDTH, LASER_LENGTH, LASER_SPEED);
        leftBeam2 = new Laser(0, 0, LASER_WIDTH, LASER_LENGTH, LASER_SPEED);
        rightBeam2 = new Laser(0, 0, LASER_WIDTH, LASER_LENGTH, LASER_SPEED);
        leftBeam3 = new Laser(0, 0, LASER_WIDTH, LASER_LENGTH, LASER_SPEED);
        rightBeam3 = new Laser(0, 0, LASER_WIDTH, LASER_LENGTH, LASER_SPEED);

        // Creating the paddle image:
        try {
            image = ImageIO.read(new File("src/PNG/49-Breakout-Tiles.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overrides the paint method to draw an image instead of filling a rectangle.
     *
     * @param g --> the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }

    /**
     * Tells the playing field what to do from one moment to the next.
     */
    public void act() {
        ;
    }

    /**
     * Moves the paddle left by a set number of pixels.
     */
    public void moveLeft() {
        // The real position is not an integer, but the display must be
        exactX -= speed;
        int displayX = (int)Math.round(exactX);
        setX(displayX);
        // Move the edges
        topEdge.moveToX(displayX);
        leftEdge.moveToX(displayX);
        rightEdge.moveToX(displayX + getWidth() - 1);
    }

    /**
     * Moves the paddle right by a set number of pixels.
     */
    public void moveRight() {
        exactX += speed;
        int displayX = (int)Math.round(exactX);
        setX(displayX);
        // Move the edges
        topEdge.moveToX(displayX);
        leftEdge.moveToX(displayX);
        rightEdge.moveToX(displayX + getWidth() - 1);
    }

    /**
     * setter for the speed of the paddle
     * @param speed the new speed of the paddle
     */
    public void setSpeed(double speed) {
        Paddle.speed = speed;
    }

    /**
     * getter for the speed of the paddle
     * @return the speed of the paddle,
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Creates a pair of lasers at the paddle's current position, up to 3 active pairs
     * @param n the pair that is being created
     */
    public void setLaserPair (int n) {
        switch (n) {
            case 1 ->
                setIndividualLaserPair(leftBeam1, rightBeam1);
            case 2 ->
                setIndividualLaserPair(leftBeam2, rightBeam2);
            case 3 ->
                setIndividualLaserPair(leftBeam3, rightBeam3);
        }
    }

    /**
     * Sets the new coordinates and enables the pair of laser beams designated
     * @param leftBeam --> Left beam
     * @param rightBeam --> Right beam
     */
    public void setIndividualLaserPair(Laser leftBeam, Laser rightBeam) {
        leftBeam.setState(true);
        leftBeam.setX(getX() + (int)(getWidth()/5.0));
        leftBeam.setY(getY() - LASER_LENGTH);
        rightBeam.setState(true);
        rightBeam.setX(getX() + (int)(4 * getWidth()/5.0) - LASER_WIDTH);
        rightBeam.setY(getY() - LASER_LENGTH);
    }

}
