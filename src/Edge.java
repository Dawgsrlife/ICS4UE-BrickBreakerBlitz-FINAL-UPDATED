/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: The Edge class is a subclass of the GameObject class
 * and is used to represent the edges of any object that may collide with the ball,
 * including the bricks and the paddle.
 * It contains methods to be moved like most other GameObjects.
 */

public class Edge extends GameObject {
    /**
     * Constructor for the Edge class, which sets its position and dimensions
     * @param x --> x-position
     * @param y --> y-position
     * @param h --> Edge height
     * @param w --> Edge width
     */
    public Edge (int x, int y, int w, int h) {
        setSize(w, h);
        setX(x);
        setY(y);
    }

    public void act () { }

    /**
     * Sets the new position for the Edge object
     * @param x --> new x-position
     * @param y --> new y-position
     */
    public void moveTo (int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Sets the new x position for the Edge object
     * @param x --> new x-position
     */
    public void moveToX (int x) {
        setX(x);
    }

    /**
     * Sets the new y position for the Edge object
     * @param y --> new y-position
     */
    public void moveToY (int y) {
        setY(y);
    }
}
