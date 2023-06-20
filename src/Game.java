/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;

/**
 * An abstract Game class which can be built into Pong.
 * The default controls are for the player to move left and right with the
 * 'A' and 'D' keys, respectively.
 *
 * Before the Game begins, the <code>setup</code> method is executed. This will
 * allow the programmer to add any objects to the game and set them up. When the
 * game begins, the <code>act</code> method is executed every millisecond. This
 * will allow the programmer to check for user input and respond to it.
 *
 *  @see GameObject
 */
public abstract class Game extends JFrame {
	private boolean _isSetup = false;
	private boolean _initialized = false;
	private boolean isPaused = true;
	private ArrayList _ObjectList = new ArrayList();
	private Timer _t;
	private JDialog pauseDialog;

	/**
	 * <code>true</code> if the 'D' key is being held down
	 */
	private boolean playerRight = false;

	/**
	 * <code>true</code> if the 'A' key is being held down.
	 */
	private boolean playerLeft = false;

	/**
	 * <code>true</code> if the Escape key is being held down.
	 */
	private boolean playerPaused = false;

	/**
	 * Returns <code>true</code> if the 'D' key is being pressed down
	 *
	 * @return <code>true</code> if the 'D' key is being pressed down
	 */
	public boolean DKeyPressed() {
		return playerRight;
	}

	/**
	 * Returns <code>true</code> if the 'A' key is being pressed down
	 *
	 * @return <code>true</code> if the 'A' key is being pressed down
	 */
	public boolean AKeyPressed() {
		return playerLeft;
	}

	/**
	 * Returns <code>true</code> if the Escape key is being pressed down
	 *
	 * @return <code>true</code> if the Escape key is being pressed down
	 */
	public boolean EscapeKeyPressed() {
		return playerPaused;
	}

	/**
	 * When implemented, this will allow the programmer to initialize the game
	 * before it begins running
	 *
	 * Adding objects to the game and setting their initial positions should be
	 * done here.
	 *
	 * @see GameObject
	 */
	public abstract void setup();

	/**
	 * When the game begins, this method will automatically be executed every
	 * millisecond
	 *
	 * This may be used as a control method for checking user input and
	 * collision between any game objects
	 */
	public abstract void act();

	/**
	 * Sets up the game and any objects.
	 *
	 * This method should never be called by anything other than a <code>main</code>
	 * method after the frame becomes visible.
	 */
	public void initComponents() {
		getContentPane().setBackground(Color.black);
		setup();
		for (int i = 0; i < _ObjectList.size(); i++) {
			GameObject o = (GameObject)_ObjectList.get(i);
			o.repaint();
		}
		_t.start();
	}

	/**
	 * Adds a game object to the screen
	 *
	 * Any added objects will have their <code>act</code> method called every
	 * millisecond
	 *
	 * @param o		the <code>GameObject</code> to add.
	 * @see	GameObject#act()
	 */
	public void add(GameObject o) {
		_ObjectList.add(o);
		getContentPane().add(o);
	}

	/**
	 * Removes a game object from the screen
	 *
	 * @param o		the <code>GameObject</code> to remove
	 * @see	GameObject
	 */
	public void remove(GameObject o) {
		_ObjectList.remove(o);
		getContentPane().remove(o);
	}

	/**
	 * Sets the millisecond delay between calls to <code>act</code> methods.
	 *
	 * Increasing the delay will make the game run "slower." The default delay
	 * is 1 millisecond.
	 *
	 * @param delay	the number of milliseconds between calls to <code>act</code>
	 * @see Game#act()
	 * @see GameObject#act()
	 */
	public void setDelay(int delay) {
		_t.setDelay(delay);
	}

	/**
	 * Sets the background color of the playing field
	 *
	 * The default color is black
	 *
	 * @see java.awt.Color
	 */
	public void setBackground(Color c) {
		getContentPane().setBackground(c);
	}

	/**
	 * The default constructor for the game.
	 *
	 * The default window size is 400x400
	 */
	public Game() {
		setSize(400, 500);
		getContentPane().setBackground(Color.BLACK);
		getContentPane().setLayout(null);
//		JMenuBar menuBar = new JMenuBar();
//		JMenu menuFile = new JMenu("File");
//		JMenuItem menuFileExit = new JMenuItem("Exit");
//		menuBar.add(menuFile);
//		menuFile.add(menuFileExit);
//		setJMenuBar(menuBar);
		setTitle("BrickBreaker Blitz");

		// Add window listener.
		addWindowListener (
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				}
		);
//		menuFileExit.addActionListener(
//				new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						System.exit(0);
//					}
//				}
//		);
		_t = new Timer(1, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				act();
				for (int i = 0; i < _ObjectList.size(); i++) {
					GameObject o = (GameObject)_ObjectList.get(i);
					o.act();
				}
			}
		});
		addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				char pressed = Character.toUpperCase(e.getKeyChar());
				switch (pressed) {
					case 'D':
						playerRight = true;
						break;
					case 'A':
						playerLeft = true;
						break;
					case KeyEvent.VK_ESCAPE:
						playerPaused = true;
						break;
				}
			}

			public void keyReleased(KeyEvent e) {
				char released = Character.toUpperCase(e.getKeyChar());
				switch (released) {
					case 'D':
						playerRight = false;
						break;
					case 'A':
						playerLeft = false;
						break;
				}
			}
		});
	}

	/**
	 * Starts updates to the game
	 *
	 * The game should automatically start.
	 *
	 * @see Game#stopGame()
	 */
	public void startGame() {
		_t.start();
	}

	/**
	 * Stops updates to the game
	 *
	 * This can act like a "pause" method
	 *
	 * @see Game#startGame()
	 */
	public void stopGame() {
		_t.stop();
	}

	/**
	 * Displays a dialog that says "Game Paused"
	 *
	 */
	public void playerPauses() {
		// Creating the pause dialog:
		pauseDialog = new JDialog((Frame) null, "Game Paused", Dialog.ModalityType.APPLICATION_MODAL);
		pauseDialog.setUndecorated(true);

		// Setting the layout for the dialog content pane:
		pauseDialog.setLayout(new BorderLayout());

		// Creating the buttons for the pause dialog:
		JPanel buttonPanel = new JPanel(new FlowLayout());

		// Creating the buttons for the pause dialog:
		JButton resumeButton = new JButton("Return to Game");
		JButton soundButton = new JButton("Toggle Sound ON/OFF");
		JButton quitButton = new JButton("Quit Game");

		// Adding action listeners to the buttons:
		resumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playerPaused) {
					playerPaused = false;
					togglePause();
				}
				pauseDialog.dispose(); // Close the dialog
			}
		});

		// Adding buttons to the button panel
		buttonPanel.add(resumeButton);
		buttonPanel.add(soundButton);
		buttonPanel.add(quitButton);

		// Adding the button panel to the centre of the dialog
		pauseDialog.add(buttonPanel, BorderLayout.CENTER);

		// Making the dialog pack its components and center on the screen
		pauseDialog.pack();
		pauseDialog.setLocationRelativeTo(null);

		// Making the dialog visible:
		pauseDialog.setVisible(true);
	}



	/**
	 * Pauses or resumes the game based on its state.
	 */
	public void togglePause() {
		if (playerPaused) {
			stopGame();
		} else {
			startGame();
		}

	}

	/**
	 * Gets the pixel width of the visible playing field
	 *
	 * @return	a width in pixels
	 */
	public int getFieldWidth() {
		return getContentPane().getBounds().width;
	}

	/**
	 * Gets the pixel height of the visible playing field
	 *
	 * @return a height in pixels
	 */
	public int getFieldHeight() {
		return getContentPane().getBounds().height;
	}

	class _WinDialog extends JDialog {
		JButton ok = new JButton("OK");
		_WinDialog(JFrame owner, String title) {
			super(owner, title);
			Rectangle r = owner.getBounds();
			setSize(200, 100);
			setLocation(r.x + r.width / 2 - 100, r.y + r.height / 2 - 50);
			getContentPane().add(ok);
			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					_WinDialog.this.setVisible(false);
				}
			});
		}
	}
}
