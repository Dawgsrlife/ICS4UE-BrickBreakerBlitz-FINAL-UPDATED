/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: The Brick class is a subclass of the GameObject class.
 * It represents the bricks in the game
 * It contains state variables for its coordinates and its health,
 * and has a method to lower its health or destroy itself that is called whenever a ball collides with the brick
 * Furthermore, it provides methods to change the direction of the ball
 * when it collides with a paddle/wall/brick, etc.
 */

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Brick extends GameObject {
    // Image:
    private BufferedImage image;
    private boolean read = true;

    // Edges (used in collision detection):
    public Edge topEdge, bottomEdge, leftEdge, rightEdge;

    // State Variables:
    private int health, prevHealth;

    /**
     * Constructor for the brick class, sets the position, dimensions, and health of the brick, also creates Edge objects for use in collision detection
     * @param x --> x-position
     * @param y --> y-position
     * @param h --> paddle height
     * @param w --> paddle width
     * @param hp --> starting health of the brick
     */
    public Brick (int x, int y, int w, int h, int hp) {
        // Routing the fundamental fields:
        setSize(w, h);
        setX(x);
        setY(y);
        health = hp;

        // Creating the edges (each their own object) every time a Brick object is created:
        topEdge = new Edge(x, y, w, 1);
        bottomEdge = new Edge(x, y + h - 1, w, 1);
        leftEdge = new Edge(x, y, 1, h);
        rightEdge = new Edge(x + w - 1, y, 1, h);
    }

    /**
     * Tells the playing field what to do from one moment to the next.
     */
    public void act() {
        switch (health) {
            case 3 -> {
                if (read) {
                    try {
                        image = ImageIO.read(new File("src/PNG/17-Breakout-Tiles.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    read = false;
                    prevHealth = health;
                }
            }
            case 2 -> {
                if (prevHealth != health) {
                    read = true;
                }
                if (read) {
                    try {
                        image = ImageIO.read(new File("src/PNG/07-Breakout-Tiles.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    read = false;
                    prevHealth = health;
                }
            }
            case 1 -> {
                if (prevHealth != health) {
                    read = true;
                }
                if (read) {
                    try {
                        image = ImageIO.read(new File("src/PNG/08-Breakout-Tiles.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    read = false;
                    prevHealth = health;
                }
            }
            case 0 -> {
                remove(this);
            }
        }
    }

    /**
     * Sets the health of the brick
     * @param n --> The health of the brick
     */
    public void setHealth(int n) {
        prevHealth = health;
        health = n;
    }

    /**
     * getter for the hp of the brick
     */
    public int getHealth() {
        return health;
    }

    /**
     * Overrides the paint method to draw an image instead of filling a rectangle.
     * @param g --> the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
