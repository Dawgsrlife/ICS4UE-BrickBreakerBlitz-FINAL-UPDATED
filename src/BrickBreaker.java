/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: This is a class called "BrickBreaker" that extends the "Game" class.
 * It contains variables and methods for the game mechanics of a two-player Pong game.
 * The setup() method initializes the game objects while the act() method controls the game logic.
 * This is also the main-driver code, meaning you would run this class to run the game.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * The BrickBreaker game.
 */
public class BrickBreaker extends Game {
	// Requires classes for Paddle, Bouncing ball, Power up, Bricks
	// Tracks HP, Level, Current Score, Brick Size with fields
	// Tracks Best Score with an external file so that it can be saved through multiple sessions

	// Miscellaneous:
	private Random rand = new Random();
	private BufferedReader br = new BufferedReader(new FileReader("src/highscore.txt"));
	private PrintWriter pw;
	private int announcementLabelCountdown;
	private double labelAdjustment = 0;
	private String labelText;
	private int optionalAdjustment = 10;

	// Player:
	private int health = 1, level = 1, score = 0, highScore = 0;

	// Statistic Labels:
	private JLabel levelLabel, scoreLabel, highScoreLabel, healthLabel;
	private JLabel announcementLabel;
	private int levelX, scoreX, highScoreX, livesX, textY;
	private final int LABEL_SIZE = 12;
	private final int ANNOUNCEMENT_FONT_SIZE_ONE = 35, ANNOUNCEMENT_FONT_SIZE_TWO = 17, LABEL_OFFSET = 37;
	private final double ADJUSTMENT_FACTOR = 3.5;
	private final int ANNOUNCEMENT_LABEL_COUNTDOWN_TIME = 500;

	// Paddle:
	private Paddle player;
	private final int PAD_WIDTH_1 = 65, PAD_WIDTH_2 = 58, PAD_WIDTH_3 = 55, PAD_THICKNESS = 12, PAD_OFFSET = 50, PAD_SPEED_1 = 3, PAD_SPEED_2 = 4, PAD_SPEED_3 = 5;

	// Bricks — Arrays of bricks for each level:
	private Brick[] level1Bricks, level2Bricks, level3Bricks;
	private final int BRICK_WIDTH = 35, BRICK_HEIGHT = 15;
	private int level1BrickCount, level2BrickCount, level3BrickCount,
				level1BrokenCount = 0, level2BrokenCount = 0, level3BrokenCount = 0;

	// Ball:
	private Ball b;
	private final int BALL_SIZE = 14, BALL_SPEED_1 = 2, BALL_SPEED_2 = 3, BALL_SPEED_3 = 4;

	// Powerup:
	private final int POWERUP_SIZE = 10, POWERUP_SPEED_1 = 3, POWERUP_SPEED_2 = 4, POWERUP_SPEED_3 = 5;
	private final double RANDOM_POWERUP_CHANCE = 0.18; // 18% chance that a powerup spawns upon breaking a brick
	private Powerup laserPowerup, doubleDamagePowerup;
	private Powerup[] powerups;
	boolean laserEnabled = false, doubleDamageEnabled = false;
	private int laserCounter = 0, doubleDamageCounter = 0;
	private Laser[] lasers;
	private final int LASER_LIMIT_1 = 60, LASER_LIMIT_2 = 12, LASER_LIMIT_3 = 18;

	// Hearts:
	private final int HEART_SIZE = 15, HEART_OFFSET = 8;
	private Heart heartOne, heartTwo, heartThree;

	// Constructor with throws IOException clause:
	public BrickBreaker() throws IOException {

	}

	/**
	 * Tells the game what to do before the actual play begins.
	 */
	public void setup() {
		// Game is stopped at the start, set up everything before it starts
		stopGame();

		// Setting the announcement label countdown timer:
		announcementLabelCountdown = ANNOUNCEMENT_LABEL_COUNTDOWN_TIME;

		// Instructions dialog
		JDialog instructions = new JDialog((Frame) null, "Instructions", Dialog.ModalityType.APPLICATION_MODAL);
		instructions.setUndecorated(false);

		// Creating the text pane for the dialog
		JTextPane rules = new JTextPane();
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setBold(attributes, true);

		rules.setCharacterAttributes(attributes, true);
		rules.setContentType("text/html");
		rules.setText("<html>" +
				"<span style=\"color:red\"> &emsp Break the bricks!</span><br>" +
				"<span style=\"color:gray\">Use A and D to move</span><br>" +
				"<span style=\"color:gray\">You have 1 life, so don't lose the ball!</span><br>" +
				"<span style=\"color:gray\">You get 1 new life every level you beat</span><br>" +
				"<span style=\"color:blue\"> &emsp Watch out for those powerups!</span><br>" +
				"<span style=\"color:purple\">Purple gives you lasers</span>" +
				"<span style=\"color:gray\"> and </span>" +
				"<span style=\"color:orange\">Orange gives you double damage and points!</span><br><br>" +
				"<span style=\"color:black\">You may quit the game at any time by pressing Esc.</span><br>" +
				"<span style=\"color:black\">Try your best to beat the high score!</span><br>" +
				"</html>");
		rules.setEditable(false);

		// Creating the button panel for the dialog:
		JPanel buttonPanel = new JPanel(new FlowLayout());

		// Creating the buttons for the dialog:
		JButton playButton = new JButton("Play");

		// Adding ActionListener to the play button
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instructions.dispose(); // Close the dialog
			}
		});

		buttonPanel.add(playButton);

		instructions.add(rules, BorderLayout.NORTH);
		instructions.add(buttonPanel, BorderLayout.SOUTH);

		// Making the dialog pack its components and center on the screen
		instructions.pack();
		instructions.setLocationRelativeTo(null);

		// Making the dialog visible:
		instructions.setVisible(true);

		// Setting the game delay, in milliseconds:
		setDelay(10);

		// Creating the ball:
		b = new Ball(getFieldWidth() / 2, getFieldHeight() - PAD_OFFSET - PAD_THICKNESS * 2, BALL_SIZE, BALL_SPEED_1,
				(rand.nextDouble() > 0.5 ? rand.nextInt(20, 70) : rand.nextInt(110, 160)));

		// Creating the paddle:
		player = new Paddle((getFieldWidth() - PAD_WIDTH_1) / 2,
				getFieldHeight() - PAD_OFFSET, PAD_THICKNESS, PAD_WIDTH_1, PAD_SPEED_1);

		// Array of the laser beams from the paddle
		lasers = new Laser[] { player.leftBeam1, player.rightBeam1, player.leftBeam2, player.rightBeam2, player.leftBeam3, player.rightBeam3 };

		// Creating the power-ups:
		laserPowerup = new Powerup(0, 0, POWERUP_SIZE, POWERUP_SPEED_1, getFieldHeight(), Color.MAGENTA, false);
		doubleDamagePowerup = new Powerup(0, 0, POWERUP_SIZE, POWERUP_SPEED_1, getFieldHeight(), Color.YELLOW, false);
		powerups = new Powerup[] {laserPowerup, doubleDamagePowerup};

		// Creating the hearts:
		heartOne = new Heart((int) (getFieldWidth() * 0.8), getFieldHeight() - LABEL_OFFSET + HEART_OFFSET, HEART_SIZE);
		heartTwo = new Heart((int) (getFieldWidth() * 0.8 + HEART_SIZE + 2), getFieldHeight() - LABEL_OFFSET + HEART_OFFSET, HEART_SIZE);
		heartThree = new Heart((int) (getFieldWidth() * 0.8 + (HEART_SIZE + 2) * 2), getFieldHeight() - LABEL_OFFSET + HEART_OFFSET, HEART_SIZE);

		// Adding the game objects:
		add(heartOne);
		add(heartTwo);
		add(heartThree);
		add(b);
		add(player);

		// Getting the high score
		File f = new File("src/highscore.txt");
		if (f.length() > 0) {
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					if (line.equals("")) line = "0";
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				highScore = Integer.parseInt(sb.toString().trim());
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		} else {
			highScore = 0;
		}

		// Levels of bricks, which are all added to their respective arrays below.
		// "lxby" stands for "level #x brick #y".
		// Level 1:
		Brick l1b1 = new Brick(16, 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b2 = new Brick(16 + BRICK_WIDTH + 2, 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b3 = new Brick(16 + 2 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b4 = new Brick(16 + 3 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b5 = new Brick(16 + 4 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b6 = new Brick(16 + 5 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b7 = new Brick(16 + 6 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b8 = new Brick(16 + 7 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b9 = new Brick(16 + 8 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1),
				l1b10 = new Brick(16 + 9 * (BRICK_WIDTH + 2), 3 * (BRICK_HEIGHT + 2), BRICK_WIDTH, BRICK_HEIGHT, 1);
		level1Bricks = new Brick[] {l1b1, l1b2, l1b3, l1b4, l1b5, l1b6, l1b7, l1b8, l1b9, l1b10};
		level1BrickCount = 10;
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
		level2BrickCount = 24;
		//Level 3
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
		level3BrickCount = 27;

		calculateLabelCoordinates();

		// Initializing the label text:
		labelText = "Level: " + level + "!";

		// Customizing the Announcement Label:
		announcementLabel = new JLabel(labelText);
		announcementLabel.setForeground(Color.ORANGE);
		announcementLabel.setFont(new Font("Monospaced", Font.BOLD, ANNOUNCEMENT_FONT_SIZE_ONE));
		announcementLabel.setHorizontalTextPosition(JLabel.CENTER);
		updateAnnouncementText();
		announcementLabel.setBounds((int) (getFieldWidth() / 2 - labelAdjustment), getFieldHeight() / 2 - 40, 500, 80);

		announcementLabel.setVisible(true);
		add(announcementLabel);

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
		highScoreLabel.setBounds(highScoreX, textY, 200, 30);
		add(highScoreLabel);

		// Customizing the Health Label:
		healthLabel = new JLabel("Lives:");
		healthLabel.setForeground(Color.WHITE);
		healthLabel.setFont(new Font("Tahoma", Font.BOLD, LABEL_SIZE));
		healthLabel.setBounds(livesX, textY, 100, 30);
		add(healthLabel);

		startGame();

		// Display bricks
		for (Brick b: level == 1 ? level1Bricks : level == 2 ? level2Bricks : level3Bricks) {
			add(b);
		}
		repaint();

		spawnLaserPowerup(getFieldWidth()/2, player.getY());
	}

	/**
	 * Tells the playing field what to do from one moment to the next.
	 */
	public void act() {
		// Display hearts:
		remove(heartOne);
		remove(heartTwo);
		remove(heartThree);
		switch (health) {
			case 0 -> {
				healthLabel.setText("Lives: 0");
			}
			case 1 -> {
				healthLabel.setText("Lives: ");
				add(heartOne);
			}
			case 2 -> {
				healthLabel.setText("Lives: ");
				add(heartOne);
				add(heartTwo);
			}
			case 3 -> {
				healthLabel.setText("Lives: ");
				add(heartOne);
				add(heartTwo);
				add(heartThree);
			}
		}
		repaint();

		// Making the announcement label only display for a set amount of time:
		if (announcementLabelCountdown > 0) {
			announcementLabelCountdown--;
		} else if (announcementLabelCountdown == 0) {
			announcementLabel.setVisible(false);
		}

		// X-collision with the lateral bounds:
		// Left wall collision.
		if (b.getX() <= 0) {
			b.setX(1); // Technically the ball should be 1 pixel away from the wall for its next movement anyway.
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
			b.setY(1); // To avoid double collision, the ball should be 1 pixel away for its next movement.
			b.bounceOnTop();
		}
		// Hits the floor case.
		if (b.getY() >= getFieldHeight() - BALL_SIZE) {
			if (health > 0) {
				health--;
				labelText = "You have " + health + " lives remaining!";
				if (health == 1) {
					labelText = "You have 1 life remaining!";
				}
				if (health == 0) {
					healthLabel.setText("Lives: 0");
				} else {
					healthLabel.setText("Lives: ");
				}
				announcementLabel.setFont(new Font("Monospaced", Font.BOLD, ANNOUNCEMENT_FONT_SIZE_TWO));
				updateAnnouncementText();
				announcementLabel.setBounds((int) (getFieldWidth() / 2 - labelAdjustment / 2), getFieldHeight() / 2 - LABEL_OFFSET + optionalAdjustment, 500, 80);
				announcementLabel.setVisible(true);
				b.bounceOnBottom();
			}
			else {
				reset();
			}
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
					// currently going down and right, so it bounces back in reverse
					if (b.getAngle() < 90) b.reverseAngle();
					// currently going down and left, so it bounces up
					else if (b.getAngle() < 180) b.bounceOnBottom();
				}
				// Top and Right corner collision
				else if (b.collides(player.rightEdge)) {
					// currently going down and right, so it bounces up
					if (b.getAngle() < 90) b.bounceOnBottom();
						// currently going down and left, so it bounces back in reverse
					else if (b.getAngle() < 180) b.reverseAngle();
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

				double transferX = 2 * relativeVelocityX * ballArea / paddleArea;       // calculate the transfer of the X-speed back to the ball, (2 is an arbitrary number to increase the scale of the effect)
				b.setAngle(b.getAngle() + (int)Math.round(transferX));                  // calculate and set the new angle based off of the transferred speed
			}
			while (player.collides(b))
				b.act();
		}

		// Powerup state check
		if (!laserPowerup.getState()) remove(laserPowerup);
		if (!doubleDamagePowerup.getState()) remove(doubleDamagePowerup);

		// Powerup Collision with paddle
		for (Powerup p: powerups) {
			if (player.collides(p)) {
				// Check which powerup p is, and apply effects based on it
				if (p == laserPowerup) {
					laserEnabled = true;
					labelText = "YOU GET " + (level == 1 ? LASER_LIMIT_1 : level == 2 ? LASER_LIMIT_2 : LASER_LIMIT_3) + " LASER SHOTS!";
					announcementLabel.setFont(new Font("Monospaced", Font.BOLD, ANNOUNCEMENT_FONT_SIZE_TWO));
					updateAnnouncementText();
					announcementLabelCountdown = (level == 1 ? LASER_LIMIT_1 : level == 2 ? LASER_LIMIT_2 : LASER_LIMIT_3) * ANNOUNCEMENT_LABEL_COUNTDOWN_TIME / 10;
					announcementLabel.setBounds((int) (getFieldWidth() / 2 - labelAdjustment / 2), getFieldHeight() / 2 - 40 + optionalAdjustment, 500, 80);
					announcementLabel.setVisible(true);
				}
				if (p == doubleDamagePowerup) {
					doubleDamageEnabled = true;
					labelText = "DOUBLE SCORE for the NEXT 5 HITS!";
					announcementLabel.setFont(new Font("Monospaced", Font.BOLD, ANNOUNCEMENT_FONT_SIZE_TWO));
					updateAnnouncementText();
					announcementLabel.setBounds((int) (getFieldWidth() / 2 - labelAdjustment / 2), getFieldHeight() / 2 - 40 + optionalAdjustment, 500, 80);
					announcementLabel.setVisible(true);
				}
				// Remove p from the screen
				p.reset();
				remove(p);
			}
		}

		// Paddle lasers.
		if (laserEnabled && laserCounter < (level == 1 ? LASER_LIMIT_1 : level == 2 ? LASER_LIMIT_2 : LASER_LIMIT_3)) {
			if (!player.leftBeam1.getState()) remove(player.leftBeam1);
			if (!player.rightBeam1.getState()) remove(player.rightBeam1);
			if (!player.leftBeam2.getState()) remove(player.leftBeam2);
			if (!player.rightBeam2.getState()) remove(player.rightBeam2);
			if (!player.leftBeam3.getState()) remove(player.leftBeam3);
			if (!player.rightBeam3.getState()) remove(player.rightBeam3);
			if ((!player.leftBeam1.getState()) && (!player.rightBeam1.getState()) &&        // all beams must be currently disabled before enabling them
					(!player.leftBeam2.getState()) && (!player.rightBeam2.getState()) &&
					(!player.leftBeam3.getState()) && (!player.rightBeam3.getState())) {
				player.setLaserPair(1);
				add(player.leftBeam1);
				add(player.rightBeam1);
				laserCounter++;
			}
			else if ((!player.leftBeam2.getState()) && (!player.rightBeam2.getState()) &&
					(player.leftBeam1.getState()) && (player.rightBeam1.getState()) &&
					(player.leftBeam1.getY() + Laser.height * 4) <= player.getY()) {
				player.setLaserPair(2);
				add(player.leftBeam2);
				add(player.rightBeam2);
				laserCounter++;
			}
			else if ((!player.leftBeam3.getState()) && (!player.rightBeam3.getState()) &&
					(player.leftBeam2.getState()) && (player.rightBeam2.getState()) &&
					(player.leftBeam2.getY() + Laser.height * 4) <= player.getY()) {
				player.setLaserPair(3);
				add(player.leftBeam3);
				add(player.rightBeam3);
				laserCounter++;
			}
		}
		if (laserCounter == (level == 1 ? LASER_LIMIT_1 : level == 2 ? LASER_LIMIT_2 : LASER_LIMIT_3)) {
			laserEnabled = false;
			laserCounter = 0;
		}

		// Collision with Bricks:
		switch(level) {
			case 1 -> {
				for (Brick brick: level1Bricks) {
					brickCollisionCheck(b, brick);
				}
				if (level1BrokenCount == level1BrickCount) nextLevel();
			}
			case 2 -> {
				for (Brick brick: level2Bricks) {
					brickCollisionCheck(b, brick);
				}
				if (level2BrokenCount == level2BrickCount) nextLevel();
			}
			case 3 -> {
				for (Brick brick: level3Bricks) {
					brickCollisionCheck(b, brick);
				}
				if (level3BrokenCount == level3BrickCount) playerWins();
			}
		}

		repaint();

		// Laser collision with Bricks
		if (laserEnabled) {
			for (Brick brick : level == 1 ? level1Bricks : level == 2 ? level2Bricks : level3Bricks) {
				if (brick.getHealth() > 0) {
					for (Laser l : lasers) {
						if (l.collides(brick) || (l.getY() <= brick.getY() + BRICK_HEIGHT && l.getX() >= brick.getX() && l.getX() < brick.getX() + BRICK_WIDTH)) {
							l.reset();
							remove(l);
							score += (doubleDamageEnabled ? 2 : 1);
							scoreLabel.setText("Score: " + score);
							brick.setHealth(brick.getHealth() - (doubleDamageEnabled ? 2 : 1));
							doubleDamageCounter++;
							if (doubleDamageCounter >= 5) {
								doubleDamageEnabled = false;
								doubleDamageCounter = 0;
							}
							if (brick.getHealth() <= 0) {
								switch (level) {
									case 1 -> {
										level1BrokenCount++;
									}
									case 2 -> {
										level2BrokenCount++;
									}
									case 3 -> {
										level3BrokenCount++;
									}
								}
								remove(brick); // does not spawn a powerup when breaking a brick with laser
							}
						}
					}
				}
			}
		}

		// Pausing the game:
		if (EscapeKeyPressed()) {
			togglePause();
			playerPauses();
		}
		try {													// Print the highscore back to the file every act();
			pw = new PrintWriter("src/highscore.txt");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		pw.print(Math.max(score, highScore));
		pw.close();

		if (score >= highScore) {
			highScore = score;
			highScoreLabel.setText("High score: " + highScore);
		}

	}

	/**
	 * Moves the game to the next level
	 */
	public void nextLevel() {
		health++;
		level++;
		levelLabel.setText("Level: " + level);

		if (level == 3) {
			optionalAdjustment = 10;
		}

		// Displaying the level through the announcement label:
		labelText = "Level: " + level + "!";
		announcementLabel.setFont(new Font("Monospaced", Font.BOLD, ANNOUNCEMENT_FONT_SIZE_ONE));
		updateAnnouncementText();
		announcementLabel.setBounds((int) (getFieldWidth() / 2 - labelAdjustment), getFieldHeight() / 2 - 40 + optionalAdjustment, 500, 80);
		announcementLabel.setVisible(true);

		spawnLaserPowerup(getFieldWidth()/2, player.getY());

		// Reset everything
		laserCounter = 0;
		laserEnabled = false;
		for (Laser l : lasers) {
			if (l.getState()) {
				l.reset();
				remove(l);
			}
		}
		doubleDamageCounter = 0;
		doubleDamageEnabled = false;
		remove(b);
		b = null;           // Old ball and paddle will be garbage collected.
		remove(player);
		player = null;

		switch (level) {
			case 2 -> {
				for (Brick brick: level2Bricks) {
					add(brick);
				}
				repaint();
				// Set up the game with new values for level 2
				b = new Ball(getFieldWidth() / 2, getFieldHeight() - PAD_OFFSET - PAD_THICKNESS * 2, BALL_SIZE, BALL_SPEED_2,
						(rand.nextDouble() > 0.5 ? rand.nextInt(20, 70) : rand.nextInt(110, 160)));
				add(b);
				player = new Paddle((getFieldWidth() - PAD_WIDTH_2) / 2,
						getFieldHeight() - PAD_OFFSET, PAD_THICKNESS, PAD_WIDTH_2, PAD_SPEED_2);
				add(player);
				laserPowerup.setSpeed(POWERUP_SPEED_2);
				doubleDamagePowerup.setSpeed(POWERUP_SPEED_2);
			}
			case 3 -> {
				for (Brick brick: level3Bricks) {
					add(brick);
				}
				repaint();
				// Set up the game with new values for level 3
				b = new Ball(getFieldWidth() / 2, getFieldHeight() - PAD_OFFSET - PAD_THICKNESS * 2, BALL_SIZE, BALL_SPEED_3,
						(rand.nextDouble() > 0.5 ? rand.nextInt(20, 70) : rand.nextInt(110, 160)));
				add(b);
				player = new Paddle((getFieldWidth() - PAD_WIDTH_3) / 2,
						getFieldHeight() - PAD_OFFSET, PAD_THICKNESS, PAD_WIDTH_3, PAD_SPEED_3);
				add(player);
				laserPowerup.setSpeed(POWERUP_SPEED_3);
				doubleDamagePowerup.setSpeed(POWERUP_SPEED_3);
			}
		}
	}

	/**
	 * Resets the game by teleporting the ball to the middle of the screen with a new random velocity.
	 */
	public void reset() {
		b.reset();
		b.setAngle(rand.nextDouble() > 0.5 ? rand.nextInt(20, 70) : rand.nextInt(110, 160));
		repaint();
		stopGame();

		if (playAgainPrompt()) {
			startOver();
		}
	}

	/**
	 * Restarts the game and resets all values
	 */
	public void startOver() {
		// Reset stats
		health = 1;
		level = 1;
		score = 0;
		healthLabel.setText("Health: " + health);
		levelLabel.setText("Level: " + level);
		scoreLabel.setText("Score: " + score);
		highScoreLabel.setText("High Score: " + highScore);

		// Reset powerups
		laserCounter = 0;
		laserEnabled = false;
		for (Laser l : lasers) {
			if (l.getState()) l.reset();
		}
		doubleDamageCounter = 0;
		doubleDamageEnabled = false;

		// Reset Bricks
		level1BrokenCount = 0;
		level2BrokenCount = 0;
		level3BrokenCount = 0;
		for (Brick b : level1Bricks) {
			remove(b);
		}
		for (Brick b : level2Bricks) {
			remove(b);
		}
		for (Brick b : level3Bricks) {
			remove(b);
		}
		repaint();
		for (Brick b : level1Bricks) {
			b.setHealth(1);
			add(b);
		}
		for (Brick b : level2Bricks) {
			b.setHealth(2);
		}
		for (Brick b : level3Bricks) {
			b.setHealth(3);
		}
		repaint();

		// Reset movement
		setPlayerRightFalse();
		setPlayerLeftFalse();
		setPlayerPausedFalse();

		// Reset ball and player
		remove(b);
		b = null;           // Old ball and paddle will be garbage collected.
		remove(player);
		player = null;
		b = new Ball(getFieldWidth() / 2, getFieldHeight() - PAD_OFFSET - PAD_THICKNESS * 2, BALL_SIZE, BALL_SPEED_1,
				(rand.nextDouble() > 0.5 ? rand.nextInt(20, 70) : rand.nextInt(110, 160)));
		add(b);
		player = new Paddle((getFieldWidth() - PAD_WIDTH_1) / 2,
				getFieldHeight() - PAD_OFFSET, PAD_THICKNESS, PAD_WIDTH_1, PAD_SPEED_1);
		add(player);
		laserPowerup.setSpeed(POWERUP_SPEED_1);
		doubleDamagePowerup.setSpeed(POWERUP_SPEED_1);
		startGame();
	}

	/**
	 * Determines the initial label coordinates.
	 */
	public void calculateLabelCoordinates() {
		levelX = (int) (getFieldWidth() * 0.15 - 35);	// 35, 30,  37, 10 are arbitrary numbers to adjust for the text
		scoreX = (int) (getFieldWidth() * 0.3 - 30);	// for a 400x500 game frame size.
		highScoreX = (int) (getFieldWidth() * 0.5 - 37);
		livesX = (int) (getFieldWidth() * 0.7 - 10);
		textY = getFieldHeight() - LABEL_OFFSET;
	}

	/**
	 * Updates the announcement label's text to display the level and resets the countdown variable.
	 */
	public void updateAnnouncementText() {
		announcementLabel.setText(labelText);
		labelAdjustment = (int) (labelText.length() * (ANNOUNCEMENT_FONT_SIZE_ONE / ADJUSTMENT_FACTOR));
		announcementLabelCountdown = ANNOUNCEMENT_LABEL_COUNTDOWN_TIME;
	}

	/**
	 * Possibly spawns either one of the powerups at the specified coordinates, is called whenever a brick is broken
	 * @param x --> x-position to spawn
	 * @param y --> y-position to spawn
	 */
	public void spawnRandomPowerup(int x, int y) {
		if (rand.nextDouble() < 0.7) {     			// 70% chance it will be double damage power up, since laser powerup is much stronger
			if (!doubleDamagePowerup.getState()) {
				spawnDoubleDamagePowerup(x, y);
			}
		}
		else {
			if (!laserPowerup.getState()) {
				spawnLaserPowerup(x, y);
			}
		}
	}

	/**
	 * Spawns a laser powerup at the specified coordinates
	 * @param x --> x-position
	 * @param y --> y-position
	 */
	public void spawnLaserPowerup(int x, int y) {
		add(laserPowerup);
		laserPowerup.setState(true);
		laserPowerup.setX(x);
		laserPowerup.setY(y);
	}

	/**
	 * Spawns a double damage powerup at the specified coordinates
	 * @param x --> x-position
	 * @param y --> y-position
	 */
	public void spawnDoubleDamagePowerup(int x, int y) {
		add(doubleDamagePowerup);
		doubleDamagePowerup.setState(true);
		doubleDamagePowerup.setX(x);
		doubleDamagePowerup.setY(y);
	}

	/**
	 * Method to check for collisions between the specified ball and brick
	 * @param ball --> the ball
	 * @param brick --> the brick
	 */
	public void brickCollisionCheck (Ball ball, Brick brick) {
		if ((ball.collides(brick.topEdge) || ball.collides(brick.bottomEdge) ||                                 // Checks for collisions against the edge objects of the brick, not the actual brick itself
				ball.collides(brick.leftEdge) || ball.collides(brick.rightEdge)) && brick.getHealth() > 0) {
			// Top collision
			if (ball.collides(brick.topEdge)) {
				// Top and Left corner collision
				if (ball.collides(brick.leftEdge)) {
					// currently going up and right, so it bounces left
					if (ball.getAngle() > 270) ball.bounceOnRight();
						// currently going down and right, so it bounces back in reverse
					else if (ball.getAngle() < 90) ball.reverseAngle();
						// currently going down and left, so it bounces up
					else if (ball.getAngle() < 180) ball.bounceOnBottom();
				}
				// Top and Right corner collision
				else if (ball.collides(brick.rightEdge)) {
					// currently going down and right, so it bounces up
					if (ball.getAngle() < 90) ball.bounceOnBottom();
						// currently going down and left, so it bounces back in reverse
					else if (ball.getAngle() < 180) ball.reverseAngle();
						// currently going up and left, so it bounces right
					else if (ball.getAngle() < 270) ball.bounceOnLeft();
				}
				// Top Edge only
				else {
					ball.bounceOnBottom();
				}
			}
			// Bottom collision
			else if (ball.collides(brick.bottomEdge)) {
				// Bottom and Left corner collision
				if (ball.collides(brick.leftEdge)) {
					// currently going down and right, so it bounces left
					if (ball.getAngle() < 90) ball.bounceOnRight();
						// currently going up and right, so it bounces back in reverse
					else if (ball.getAngle() > 270) ball.reverseAngle();
						// currently going up and left, so it bounces down
					else if (ball.getAngle() < 270) ball.bounceOnTop();
				}
				// Bottom and Right corner collision
				else if (ball.collides(brick.rightEdge)) {
					// currently going down and left, so it bounces right
					if (ball.getAngle() < 180) ball.bounceOnLeft();
						// currently going up and left, so it bounces back in reverse
					else if (ball.getAngle() < 270) ball.reverseAngle();
						// currently going up and right, so it bounces down
					else if (ball.getAngle() < 360) ball.bounceOnTop();
				}
				// Bottom Edge only
				else {
					ball.bounceOnTop();
				}
			}
			// Left collision
			else if (ball.collides(brick.leftEdge)) {
				ball.bounceOnRight();
			}
			// Right collision
			else if (ball.collides(brick.rightEdge)) {
				ball.bounceOnLeft();
			}

			while (ball.collides(brick)) ball.act();
			score += (doubleDamageEnabled ? 2 : 1);
			scoreLabel.setText("Score: " + score);
			brick.setHealth(brick.getHealth() - (doubleDamageEnabled ? 2 : 1));
			doubleDamageCounter++;
			if (doubleDamageCounter >= 5) {
				doubleDamageEnabled = false;
				doubleDamageCounter = 0;
			}
			if (brick.getHealth() <= 0) {
				switch(level) {
					case 1 -> {
						level1BrokenCount++;
					}
					case 2 -> {
						level2BrokenCount++;
					}
					case 3 -> {
						level3BrokenCount++;
					}
				}
				remove(brick);
				if (rand.nextDouble() < RANDOM_POWERUP_CHANCE) { // Chance to spawn a random powerup upon breaking a brick
					spawnRandomPowerup(brick.getX() + BRICK_WIDTH / 2,
							brick.getY() + BRICK_HEIGHT - POWERUP_SIZE);
				}
			}
		}
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
