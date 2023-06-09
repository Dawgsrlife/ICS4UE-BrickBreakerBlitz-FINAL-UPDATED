/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: The Paddle class extends GameObject.
 * It contains a two methods: moveLeft() and moveRight() to adjust the paddle's X-position.
 */

public class Paddle extends GameObject {
    // Add any state variables here
    private static double speed = 1.0;

    /**
     * Constructor to set the position and dimensions of the paddle.
     * @param x --> x-position
     * @param y --> y-position
     * @param h --> paddle height
     * @param w --> paddle width
     */
    public Paddle (int x, int y, int h, int w) {
        setSize(w, h);
        setX(x);
        setY(y);
    }

    /**
     * Tells the playing field what to do from one moment to the next.
     */
    public void act() {
        ;
    }

    /**
     * Translates the paddle left by a set number of pixels.
     */
    public void moveLeft() {
        setX(getX() - 5);
    }

    /**
     * Translates the paddle right by a set number of pixels.
     */
    public void moveRight() {
        setX(getX() + 5);
    }

}