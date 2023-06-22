/**
 * Authors: Alexander Meng & Anton Lee
 * Course: ICS4UE Mr. Benum
 * Description: A representation of the heart image to depict the number of lives or HP the player has.
 */

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Heart extends GameObject {
    // Image:
    private BufferedImage image;

    /**
     * Constructor to set the dimensions and position of a heart.
     */
    public Heart(int x, int y, int size) {
        setX(x);
        setY(y);
        setSize(size, size);
        // Creating the heart image:
        try {
            image = ImageIO.read(new File("src/PNG/60-Breakout-Tiles.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells the playing field what to do from one moment to the next.
     */
    public void act() {

    }
    /**
     * Overrides the paint method to draw an image.
     *
     * @param g --> the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}