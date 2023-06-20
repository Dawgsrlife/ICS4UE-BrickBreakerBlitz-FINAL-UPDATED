/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: This is a class called "BrickBreaker" that extends the "Game" class.
 * It contains variables and methods for the game mechanics of a two-player Pong game.
 * The setup() method initializes the game objects while the act() method controls the game logic.
 * This is also the main-driver code, meaning you would run this class to run the game.
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;


/**
 * The BrickBreaker game.
 */
public class BrickBreaker extends Game {
	// Requires classes for Paddle, Bouncing ball, Power up, Bricks
	// Tracks HP, Level, Current Score, Brick Size with fields
	// Tracks Best Score with an external file so that it can be saved through multiple sessions

	// Miscellaneous:
	private Random rand = new Random();
	private Scanner sc = new Scanner("src/highscore.txt");
	private FileWriter fw = new FileWriter("src/highscore.txt");

	// Player:
	private int health = 3, level = 1, score, highScore = 0;
	private boolean scored = false;

	// Statistic Labels:
	private JLabel levelLabel, scoreLabel, highScoreLabel, healthLabel;
	private int levelX, scoreX, highScoreX, livesX, textY;
	private final int LABEL_SIZE = 10;

	// Paddle:
	private Paddle player;
	private final int PAD_START_WIDTH = 40, PAD_THICKNESS = 6, PAD_OFFSET = 50, PAD_STARTING_SPEED = 3;
	private BufferedImage paddleImage = null;

	// Bricks — Arrays of bricks for each level:
	private Brick[] level1Bricks, level2Bricks, level3Bricks;
	private final int BRICK_WIDTH = 35, BRICK_HEIGHT = 15;

	// Ball:
	private Ball b;
	private final int BALL_SIZE = 10, BALL_STARTING_SPEED = 2;
	private int currBallX, currBallY = 0;

	private Powerup laserPowerup, doubleDamagePowerup;

	// Constructor with throws IOException clause
	public BrickBreaker () throws IOException { }

	/**
	 * Tells the game what to do before the actual play begins.
	 */
	public void setup() {
		// Setting the game delay, in milliseconds.
		setDelay(10);

		// Creating the ball:
		b = new Ball(getFieldWidth() / 2, getFieldHeight() - PAD_OFFSET - PAD_THICKNESS * 2, BALL_SIZE, Color.CYAN, BALL_STARTING_SPEED, (rand.nextDouble() > 0.5 ? rand.nextInt(20, 70) : rand.nextInt(110, 160)));

		// Creating the paddle:
		player = new Paddle((getFieldWidth() - PAD_START_WIDTH) / 2,
				getFieldHeight() - PAD_OFFSET, PAD_THICKNESS * 10, PAD_START_WIDTH, PAD_STARTING_SPEED);
		try {
			paddleImage = ImageIO.read(new File("src/PNG/49-Breakout-Tiles.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creating the power-ups:
		laserPowerup = new Powerup(0, 0, POWERUP_SIZE, POWERUP_STARTING_SPEED, getFieldHeight(), Color.MAGENTA, false);
		doubleDamagePowerup = new Powerup(0, 0, POWERUP_SIZE, POWERUP_STARTING_SPEED, getFieldHeight(), Color.GREEN, false);

		// Adding the game objects:
		add(b);
		add(player);

		// Levels of bricks, which are all added to their respective arrays below.
		// "lxby" stands for "level #x brick #y".
		// Level 1:
		Brick l1b1 = new Brick(0, 0, BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b2 = new Brick(BRICK_WIDTH + 2, 0, BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b3 = new Brick(2 * (BRICK_WIDTH + 2), 0, BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b4 = new Brick(3 * (BRICK_WIDTH + 2), 0, BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b5 = new Brick(4 * (BRICK_WIDTH + 2), 0, BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b6 = new Brick(5 * (BRICK_WIDTH + 2), 0, BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b7 = new Brick(6 * (BRICK_WIDTH + 2), 0, BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b8 = new Brick(7 * (BRICK_WIDTH + 2), 0, BRICK_WIDTH, BRICK_HEIGHT, 1);
		level1Bricks = new Brick[] {l1b1, l1b2, l1b3, l1b4, l1b5, l1b6, l1b7, l1b8};
//		for (Brick b: level1Bricks) {
//			add(b);
//		}
		// Level 2:
		Brick l2b1 = new Brick(BRICK_WIDTH + 2, 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b2 = new Brick(2 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b3 = new Brick(3 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b4 = new Brick(4 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b5 = new Brick(5 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b6 = new Brick(6 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b7 = new Brick(7 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b8 = new Brick(8 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b9 = new Brick(BRICK_WIDTH + 2, 6 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b10 = new Brick(2 * (BRICK_WIDTH + 2), 6 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b11 = new Brick(3 * (BRICK_WIDTH + 2), 6 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b12 = new Brick(4 * (BRICK_WIDTH + 2), 6 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b13 = new Brick(5 * (BRICK_WIDTH + 2), 6 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b14 = new Brick(6 * (BRICK_WIDTH + 2), 6 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b15 = new Brick(7 * (BRICK_WIDTH + 2), 6 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b16 = new Brick(8 * (BRICK_WIDTH + 2), 6 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b17 = new Brick(BRICK_WIDTH + 2, 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b18 = new Brick(2 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b19 = new Brick(3 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b20 = new Brick(4 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b21 = new Brick(5 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b22 = new Brick(6 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b23 = new Brick(7 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2),
				l2b24 = new Brick(8 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 2);
		level2Bricks = new Brick[] {l2b1, l2b2, l2b3, l2b4, l2b5, l2b6, l2b7, l2b8,
				l2b9, l2b10, l2b11, l2b12, l2b13, l2b14, l2b15, l2b16,
				l2b17, l2b18, l2b19, l2b20, l2b21, l2b22, l2b23, l2b24};
//		level = 2;
//		for (Brick b: level2Bricks) {
//			add(b);
//		}
		Brick l3b1 = new Brick(BRICK_WIDTH + 2, 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b2 = new Brick(3 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b3 = new Brick(5 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b4 = new Brick(7 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b5 = new Brick(9 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b6 = new Brick(2 * (BRICK_WIDTH + 2), 5 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b7 = new Brick(4 * (BRICK_WIDTH + 2), 5 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b8 = new Brick(6 * (BRICK_WIDTH + 2), 5 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b9 = new Brick(8 * (BRICK_WIDTH + 2), 5 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b10 = new Brick(BRICK_WIDTH + 2, 7 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b11 = new Brick(3 * (BRICK_WIDTH + 2), 7 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b12 = new Brick(5 * (BRICK_WIDTH + 2), 7 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b13 = new Brick(7 * (BRICK_WIDTH + 2), 7 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b14 = new Brick(9 * (BRICK_WIDTH + 2), 7 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b15 = new Brick(2 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b16 = new Brick(4 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b17 = new Brick(6 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b18 = new Brick(8 * (BRICK_WIDTH + 2), 9 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b19 = new Brick(BRICK_WIDTH + 2, 11 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b20 = new Brick(3 * (BRICK_WIDTH + 2), 11 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b21 = new Brick(5 * (BRICK_WIDTH + 2), 11 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b22 = new Brick(7 * (BRICK_WIDTH + 2), 11 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b23 = new Brick(9 * (BRICK_WIDTH + 2), 11 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b24 = new Brick(2 * (BRICK_WIDTH + 2), 13 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b25 = new Brick(4 * (BRICK_WIDTH + 2), 13 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b26 = new Brick(6 * (BRICK_WIDTH + 2), 13 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3),
				l3b27 = new Brick(8 * (BRICK_WIDTH + 2), 13 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 3);
		level3Bricks = new Brick[] {l3b1, l3b2, l3b3, l3b4, l3b5, l3b6, l3b7, l3b8, l3b9,
				 l3b10, l3b11, l3b12, l3b13, l3b14, l3b15, l3b16, l3b17, l3b18,
				 l3b19, l3b20, l3b21, l3b22, l3b23, l3b24, l3b25, l3b26, l3b27};
//		level = 3;
//		for (Brick b: level3Bricks) {
//			add(b);
//		}

		for (Brick b: level == 1 ? level1Bricks : level == 2 ? level2Bricks : level3Bricks) {
			add(b);
		}
		repaint();

		calculateLabelCoordinates();

		// Customizing the Level Label:
		levelLabel = new JLabel("Level: " + level);
		levelLabel.setForeground(Color.WHITE);
		levelLabel.setFont(new Font("Tahoma", Font.BOLD, LABEL_SIZE));
		levelLabel.setBounds(levelX, textY, 100, 30);
		add(levelLabel);

		// Customizing the Score Label:
		scoreLabel = new JLabel("Score: " + score);
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setFont(new Font("Tahoma", Font.BOLD, LABEL_SIZE));
		scoreLabel.setBounds(scoreX, textY, 100, 30);
		add(scoreLabel);

		// Customizing the High Score Label:
		highScoreLabel = new JLabel("High Score: " + highScore);
		highScoreLabel.setForeground(Color.WHITE);
		highScoreLabel.setFont(new Font("Tahoma", Font.BOLD, LABEL_SIZE));
		highScoreLabel.setBounds(highScoreX, textY, 100, 30);
		add(highScoreLabel);

		// Customizing the Health Label:
		healthLabel = new JLabel("HP/Lives: " + health);
		healthLabel.setForeground(Color.WHITE);
		healthLabel.setFont(new Font("Tahoma", Font.BOLD, LABEL_SIZE));
		healthLabel.setBounds(livesX, textY, 100, 30);
		add(healthLabel);
	}

	/**
	 * Tells the playing field what to do from one moment to the next.
	 */
	public void act() {
		// X-collision with the lateral bounds:
		// Left wall collision.
		if (b.getX() <= 0) {
			b.setX(1);
			b.bounceOnLeft();
		}
		// Right wall collision.
		if (b.getX() >= getFieldWidth() - BALL_SIZE) {
			b.setX(getFieldWidth() - BALL_SIZE);
			b.bounceOnRight();
		}

		// Y-collision with the ceiling and disappear at bottom:
		// Collision with the roof.
		if (b.getY() <= 0) {
			b.setY(1);
			b.bounceOnTop();
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
			// Top collision
			if (b.collides(player.topEdge)) {
				// Top and Left corner collision
				if (b.collides(player.leftEdge)) {
					// currently going up and right, so it bounces left
					if (b.getAngle() > 270) b.bounceOnRight();
						// currently going down and right, so it bounces back in reverse
					else if (b.getAngle() < 90) b.reverseAngle();
						// currently going down and left, so it bounces up
					else if (b.getAngle() < 180) b.bounceOnBottom();
				}
				// Top and Right corner collision
				else if (b.collides(player.rightEdge)) {
					// currently going down and right, so it bounces up
					if (b.getAngle() < 90) b.bounceOnBottom();
						// currently going down and left, so it bounces back in reverse
					else if (b.getAngle() < 180) b.reverseAngle();
						// currently going up and left, so it bounces right
					else if (b.getAngle() < 270) b.bounceOnLeft();
				}
				// Top Edge only
				else
					b.bounceOnBottom();
			}
			// Left collision
			else if (b.collides(player.leftEdge))
				b.bounceOnRight();
			// Right collision
			else if (b.collides(player.rightEdge))
				b.bounceOnLeft();

			// Pseudo physics
			if ((AKeyPressed() && player.getX() > 0) ^ (DKeyPressed() && player.getX() < getFieldWidth() - player.getWidth())) { // use XOR since everything cancels out if both are held
				// Math:
				double ballVelocityX = b.getSpeed() * Math.cos(b.getAngle()),           // get the ball's current speed
						relativeVelocityX;
				if (AKeyPressed() && player.getX() > 0)                                 // get the relative velocity of the paddle (relative to the ball)
					relativeVelocityX = ballVelocityX - player.getSpeed();
				else
					relativeVelocityX = player.getSpeed() - ballVelocityX;

				double ballArea = (Math.PI * BALL_SIZE * BALL_SIZE),                    // get the areas of the ball and paddle
						paddleArea = player.getWidth() * player.getHeight();

				double transferX = 3 * relativeVelocityX * ballArea / paddleArea;       // calculate the transfer of the X-speed back to the ball, (3 is an arbitrary number to increase the scale of the effect)
				b.setAngle(b.getAngle() + (int)Math.round(transferX));                  // calculate and set the new angle based off of the transferred speed
			}
			while (player.collides(b))
				b.act();
		}

		// Collision with Bricks (actually check with the edges of the bricks, which are each their own object):

		for (Brick brick: level == 1 ? level1Bricks : level == 2 ? level2Bricks : level3Bricks) {
			if ((b.collides(brick.topEdge) || b.collides(brick.bottomEdge) ||
					b.collides(brick.leftEdge) || b.collides(brick.rightEdge)) && brick.getHealth() > 0) {
				// Top collision
				if (b.collides(brick.topEdge)) {
					// Top and Left corner collision
					if (b.collides(brick.leftEdge)) {
						// currently going up and right, so it bounces left
						if (b.getAngle() > 270) b.bounceOnRight();
							// currently going down and right, so it bounces back in reverse
						else if (b.getAngle() < 90) b.reverseAngle();
							// currently going down and left, so it bounces up
						else if (b.getAngle() < 180) b.bounceOnBottom();
					}
					// Top and Right corner collision
					else if (b.collides(brick.rightEdge)) {
						// currently going down and right, so it bounces up
						if (b.getAngle() < 90) b.bounceOnBottom();
							// currently going down and left, so it bounces back in reverse
						else if (b.getAngle() < 180) b.reverseAngle();
							// currently going up and left, so it bounces right
						else if (b.getAngle() < 270) b.bounceOnLeft();
					}
					// Top Edge only
					else {
						b.bounceOnBottom();
					}
				}
				// Bottom collision
				else if (b.collides(brick.bottomEdge)) {
					// Bottom and Left corner collision
					if (b.collides(brick.leftEdge)) {
						// currently going down and right, so it bounces left
						if (b.getAngle() < 90) b.bounceOnRight();
							// currently going up and right, so it bounces back in reverse
						else if (b.getAngle() > 270) b.reverseAngle();
							// currently going up and left, so it bounces down
						else if (b.getAngle() < 270) b.bounceOnTop();
					}
					// Bottom and Right corner collision
					else if (b.collides(brick.rightEdge)) {
						// currently going down and left, so it bounces right
						if (b.getAngle() < 180) b.bounceOnLeft();
							// currently going up and left, so it bounces back in reverse
						else if (b.getAngle() < 270) b.reverseAngle();
							// currently going up and right, so it bounces down
						else if (b.getAngle() < 360) b.bounceOnTop();
					}
					// Bottom Edge only
					else {
						b.bounceOnTop();
					}
				}
				// Left collision
				else if (b.collides(brick.leftEdge)) {
					b.bounceOnRight();
				}
				// Right collision
				else if (b.collides(brick.rightEdge)) {
					b.bounceOnLeft();
				}

				while (b.collides(brick)) b.act();
				score++;
				scoreLabel.setText("Score: " + score);
				brick.setHealth(brick.getHealth() - 1);
				if (brick.getHealth() <= 0) remove(brick);
			}
		}
		repaint();

		// Pausing the game:
		if (EscapeKeyPressed()) {
			togglePause();
			playerPauses();
		}
	}

	/**
	 * Resets the game by teleporting the ball to the middle of the screen with a new random velocity.
	 */
	public void reset() {
		b.reset();
		b.setAngle(rand.nextInt(0, 360));
		repaint();
		stopGame();
	}

	/**
	 * Determines the initial label coordinates.
	 */
	public void calculateLabelCoordinates() {
		levelX = (int) (getFieldWidth() * 0.2);
		scoreX = (int) (getFieldWidth() * 0.4);
		highScoreX = (int) (getFieldWidth() * 0.6);
		livesX = (int) (getFieldWidth() * 0.8);
		textY = getFieldHeight() - 37;
	}

	/**
	 * The main driver code.
	 */
	public static void main(String[] args) throws IOException{
		BrickBreaker BB = new BrickBreaker();
		BB.setVisible(true);
		BB.initComponents();
	}
}
