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

import java.awt.*;

public class Brick extends GameObject {
    // Edges (used in collision detection)
    public Edge topEdge, bottomEdge, leftEdge, rightEdge;

    // State Variables:
    private int health;
    private Color c; //Colour for the paint method

    /**
     * Constructor for the brick class, sets the position, dimensions, and health of the brick, also creates Edge objects for use in collision detection
     * @param x --> x-position
     * @param y --> y-position
     * @param h --> paddle height
     * @param w --> paddle width
     * @param hp --> starting health of the brick
     */
    public Brick (int x, int y, int w, int h, int hp) {
        setSize(w, h);
        setX(x);
        setY(y);
        health = hp;

        // Creating the edges (each their own object) every time a Brick object is created
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
                c = Color.RED;
            }
            case 2 -> {
                c = Color.ORANGE;
            }
            case 1 -> {
                c = Color.YELLOW;
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
        health = n;
    }

    /**
     * getter for the hp of the brick
     */
    public int getHealth() {
        return health;
    }

    /**
     * Overrided method that paints a circle (round rectangle) to display the powerup.
     * @param g The <code>Graphics</code> context in which to paint.
     */
    public void paint(Graphics g) {
        g.setColor(c);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
    }

}