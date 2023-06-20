import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: The Paddle class extends GameObject.
 * It contains a two methods: moveLeft() and moveRight() to adjust the paddle's X-position.
 */

public class Paddle extends GameObject {
    // Image:
    private BufferedImage image;

    // Edges:
    public Edge topEdge, leftEdge, rightEdge;

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
     * setter for the speed of the ball
     * @param speed the new speed of the paddle
     */
    public void setSpeed(double speed) {
        Paddle.speed = speed;
    }

    /**
     * getter for the speed of the ball
     * @return the speed of the ball, (this is not separated by x and y movements)
     */
    public double getSpeed() {
        return speed;
    }

}