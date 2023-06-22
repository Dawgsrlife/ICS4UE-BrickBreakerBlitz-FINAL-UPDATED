/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: The Powerups class extends GameObject.
 * Powerups are intended to positively modify the gameplay experience for the user.
 * These will be represented as balls which drop downwards and can be
 * caught by the paddle for the player to obtain the power up.
 */

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Powerup extends GameObject {
    // State Variables:
    private double speed;
    private int fieldHeight;
    private boolean state; // true = enabled, false = disabled state
    private enum POWERUP_TYPE {LASER, DOUBLE_DAMAGE}
    private POWERUP_TYPE powerUpType;
    private BufferedImage image;

    /**
     * Constructor for the Powerup class, sets the position, dimensions, speed,
     * field height, colour, and starting state of the powerup
     * @param x --> x-position
     * @param y --> y-position
     * @param size --> width and height of the powerup (displayed as a ball)
     * @param speed --> speed at which the powerup falls down
     * @param fieldHeight --> height at which the ball stops falling
     * @param colour --> colour of the ball
     * @param state --> starting state (enabled or disabled) of the ball
     */
    public Powerup (int x, int y, int size, double speed, int fieldHeight, Color colour, boolean state) {
        setSize(size, size);
        setX(x);
        setY(y);
        this.speed = speed;
        this.fieldHeight = fieldHeight;
        this.state = state;

        if (colour == Color.YELLOW)
            powerUpType = POWERUP_TYPE.DOUBLE_DAMAGE;
        else if (colour == Color.MAGENTA)
            powerUpType = POWERUP_TYPE.LASER;
    }

    /**
     * Tells the playing field what to do from one moment to the next.
     */
    public void act() {
        if (state) {
            setY((int) (getY() + speed + 0.5));
            if (getY() >= fieldHeight)
                setState(false);
        }
        switch(powerUpType) {
            case LASER -> {
                try {
                    image = ImageIO.read(new File("src/PNG/Powerup_Laser.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case DOUBLE_DAMAGE -> {
                try {
                    image = ImageIO.read(new File("src/PNG/Powerup_DoubleDamage.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * setter for the speed of the powerup
     * @param speed the new speed of the powerup
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * getter for the speed of the powerup
     * @return the speed of the powerup,
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * getter for the state of the powerup
     * @return state of the powerup
     */
    public boolean getState() {
        return state;
    }

    /**
     * setter for the state of the powerup
     * @param state the new state
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * Moves the powerup off the screen
     */
    public void reset() {
        setState(false);
        setX(-1 - getWidth()); // Move it out of the screen
        setY(-1 - getHeight());
    }

    /**
     * Overrided method that paints an image rather than filling a shape.
     * @param g The <code>Graphics</code> context in which to paint.
     */
    public void paint(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
