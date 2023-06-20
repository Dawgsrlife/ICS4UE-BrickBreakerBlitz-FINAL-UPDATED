/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: The Paddle class extends GameObject.
 * It contains a two methods: moveLeft() and moveRight() to adjust the paddle's X-position.
 */

public class Paddle extends GameObject {
    // Edges
    public Edge topEdge, leftEdge, rightEdge;

    // Add any state variables here
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
        setSize(w, h);
        setX(x);
        setY(y);
        speed = spd;
        exactX = x;

        // Creating the edges (each their own object) every time a Brick object is created
        topEdge = new Edge(x, y, w, 1);
        leftEdge = new Edge(x, y, 1, h);
        rightEdge = new Edge(x + w - 1, y, 1, h);
    }

    public void act() { }

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