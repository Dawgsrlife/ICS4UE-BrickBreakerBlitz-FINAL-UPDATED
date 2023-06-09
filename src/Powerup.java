/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: The Powerups class extends GameObject.
 * Powerups are intended to positively modify the gameplay experience for the user.
 * These will be represented as balls which drop downwards and can be
 * caught by the paddle for the player to obtain the power up.
 */

import java.awt.Color;
import java.awt.Graphics;

/**
 * Powerups of the game
 */
public class Powerup extends GameObject {
    // State Variables:
    private double yMov = 1.0;

    private Color c; //Colour for the paint method

    // Constructors
    public Powerup (int x, int y, int size, Color colour) {
        setSize(size, size);
        setX(x);
        setY(y);
        c = colour;
    }

    /**
     * Tells the playing field what to do from one moment to the next.
     */
    public void act() {
        setY((int) (getY() + yMov + 0.5));
    }

    /**
     * Sets the y-movement factor of the powerup ball.
     * @param n --> The movement factor (how much for every frame).
     */
    public void setYMov(double n) {
        yMov = n;
    }

    /**
     * Overrided method that paints a circle (round rectangle) to display the powerup.
     * @param g The <code>Graphics</code> context in which to paint.
     */
    public void paint(Graphics g) {
        g.setColor(c);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
    }
}