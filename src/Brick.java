/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE by Mr. Benum
 * Description: The Brick class is a subclass of the GameObject class.
 * It represents the bricks in the game
 * It contains state variables for the its coordinates and its health,
 * and has a method to lower its health or destroy itself that is called whenever a ball collides with the brick
 * Furthermore, it provides methods to change the direction of the ball
 * when it collides with a paddle/wall/brick, etc.
 */

import java.awt.*;

/**
 * Bricks for each level of the game
 */
public class Brick extends GameObject {
    // State Variables:
    private int health;

    private Color c; //Colour for the paint method

    // Constructors
    public Brick (int x, int y, int w, int h, int hp) {
        setSize(w, h);
        setX(x);
        setY(y);
        health = hp;
        this.c = c;
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
     * Overrided method that paints a circle (round rectangle) to display the powerup.
     * @param g The <code>Graphics</code> context in which to paint.
     */
    public void paint(Graphics g) {
        g.setColor(c);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 3, 3);
    }

}
