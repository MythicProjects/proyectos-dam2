package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/** Loads one sprite sheet (one animation) and cuts it into frames. */
final class SpriteSheet {

    private SpriteSheet() {}

    /**
     * @param resource   classpath path, e.g. "/sprites/gold.png"
     * @param columns    frames per row
     * @param rows       rows in the sheet
     * @param frameCount real frames: the last row may be incomplete
     */
    static BufferedImage[] cut(String resource, int columns, int rows, int frameCount) {
        if (frameCount < 1 || frameCount > columns * rows) {
            throw new IllegalArgumentException(resource + ": frameCount must be 1.." + columns * rows);
        }
        BufferedImage sheet = load(resource);
        int frameWidth = sheet.getWidth() / columns;
        int frameHeight = sheet.getHeight() / rows;

        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int n = 0; n < frameCount; n++) {
            int row = n / columns;          // full rows before frame n
            int column = n % columns;       // position inside its row
            frames[n] = sheet.getSubimage(column * frameWidth, row * frameHeight, frameWidth, frameHeight);
        }
        return frames;
    }

    private static BufferedImage load(String resource) {
        try (InputStream in = SpriteSheet.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("Resource not found: " + resource);
            }
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + resource, e);
        }
    }
}
