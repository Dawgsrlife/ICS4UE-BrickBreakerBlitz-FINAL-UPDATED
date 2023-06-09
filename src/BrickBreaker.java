/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: This is a class called "BrickBreaker" that extends the "Game" class.
 * It contains variables and methods for the game mechanics of a two-player Pong game.
 * The setup() method initializes the game objects while the act() method controls the game logic.
 * This is also the main-driver code, meaning you would run this class to run the game.
 */

import java.awt.*;
import java.util.Random;


/**
 * The BrickBreaker game.
 */
public class BrickBreaker extends Game {
	// Requires classes for Paddle, Bouncing ball, Power up, Bricks
	// Tracks HP, Level, Current Score, Brick Size with fields
	// Tracks Best Score with an external file so that it can be saved through multiple sessions

	Random rand = new Random();

	// Fields:
	private final int BALL_SIZE = 10;
	private int health = 3, level = 0, curScore = 0, brickSize = 5;
	private double velocity = (rand.nextDouble() + 1) * (rand.nextInt(2) == 1 ? 1 : -1);

	private final int PAD_START_WIDTH = 40, PAD_THICKNESS = 6, PAD_OFFSET = 20;

	private final int BRICK_WIDTH = 20, BRICK_HEIGHT = 10;

	// Paddle:
	private Paddle player;

	// Bricks — Arrays of bricks for each level:
	private Brick[] level1Bricks, level2Bricks, level3Bricks;

	// Ball:
	private Ball b;

	/**
	 * Tells the game what to do before the actual play begins.
	 */
	public void setup() {
		// Setting the game delay, in milliseconds.
		setDelay(10);

		// Adding the ball.
		b = new Ball(getFieldWidth() / 2, getFieldHeight() / 2, BALL_SIZE, Color.CYAN, velocity);
		add(b);

		// Adding the paddle.
		System.out.println(getFieldHeight() +  " " + getFieldWidth());
		player = new Paddle((getFieldWidth() - PAD_START_WIDTH)/2,
				getFieldHeight() - PAD_OFFSET, PAD_THICKNESS, PAD_START_WIDTH);
		add(player);

		// Levels of bricks, which are al
			// Level 1
		Brick l1b1 = new Brick(0, 0, BRICK_WIDTH, BRICK_HEIGHT, 3);

	}

	/**
	 * Tells the playing field what to do from one moment to the next.
	 */
	public void act() {

		// X-collision with the lateral bounds:
		// Left wall collision.
		if (b.getX() <= 0) {
			b.setX(0);
			b.setXMov(b.getXMov()*-1);
		}
		// Right wall collision.
		if (b.getX() >= getFieldWidth() - BALL_SIZE) {
			b.setX(getFieldWidth() - BALL_SIZE);
			b.setXMov(b.getXMov()*-1);
		}

		// Y-collision with the ceiling and disappear at bottom:
		// Collision with the roof.
		if (b.getY() <= 0) {
			b.setY(0);
			b.setYMov(b.getYMov()*-1);
		}
		// Hits the floor case.
		if (b.getY() >= getFieldHeight() - BALL_SIZE) {
			reset();
		}

		// Handling key-presses:
		// Paddle left movement.
		if (AKeyPressed() && player.getX() > 0) {
			player.moveLeft();
		}
		// Paddle right movement.
		if (DKeyPressed() && player.getX() < getFieldWidth() - player.getWidth()) {
			player.moveRight();
		}

		// Collision with paddle:
		if (player.collides(b)) {
			bounceBall(b);
		}

	}


	/**
	 * "Bounces" the ball by manipulating the direction of its x- and/or y-components.
	 */
	public void bounceBall(Ball b) {
		b.setYMov(Math.abs(b.getYMov())*-1);
	}

	public void reset() {
		b.setX(getFieldWidth() / 2);
		b.setY(getFieldHeight() / 2);
		velocity = (rand.nextDouble() + 1) * (rand.nextInt(2) == 1 ? 1 : -1);
		b.setXMov(velocity);
		b.setYMov(velocity);
	}

	/**
	 * The main driver code.
	 */
	public static void main(String[] args) {
		BrickBreaker BB = new BrickBreaker();
		BB.setVisible(true);
		BB.initComponents();
	}
}
