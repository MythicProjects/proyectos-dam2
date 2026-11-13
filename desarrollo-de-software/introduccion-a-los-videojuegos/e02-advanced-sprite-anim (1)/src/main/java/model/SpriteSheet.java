package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

final class SpriteSheet {
    private final BufferedImage[] frames;

    /**
     * @param resource classpath path, e.g. "/sprites/knight_walk_down.png"
     * @param columns frames per row
     * @param rows rows in the sheet
     * @param frameCount real frames; the last row may be incomplete
     */
    SpriteSheet(String resource, int columns, int rows, int frameCount){
        if (columns <= 0 || rows <= 0){
            throw new IllegalArgumentException("Columns and rows must be posirive: " + resource);
        }

        if(frameCount <= 0 || frameCount > columns * rows) {
            throw new IllegalArgumentException("frameCount must be between 1 and " + columns * rows + ": " + resource);
        }

        BufferedImage sheet = loadImage(resource);

        if(sheet.getWidth() % columns != 0 || sheet.getHeight() % rows != 0){
            throw new IllegalArgumentException(
                    "Sheet " + sheet.getWidth() + "x" + sheet.getHeight()
                    + " does not divide into " + columns + "x" + rows + ": " + resource);
        }

        int frameWidth = sheet.getWidth() / columns;
        int frameHeight = sheet.getHeight() / rows;

        this.frames = new BufferedImage[frameCount];
        for (int n = 0; n < frameCount; n++) {
            int row = n / columns;
            int column = n % columns;
            this.frames[n] = sheet.getSubimage(column * frameWidth, row * frameHeight, frameWidth, frameHeight);
        }
    }

    BufferedImage[] frames(){
        return this.frames.clone();
    }

    private static BufferedImage loadImage(String resource){
        try (InputStream in = SpriteSheet.class.getResourceAsStream(resource)) {
            if(in == null){
                throw new IllegalArgumentException("Resource not found: " + resource);
            }
            
            BufferedImage image = ImageIO.read(in);
            if(image == null){
                throw new IllegalArgumentException("Unsupported image format: " + resource);                
            }

            return image;

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + resource, e);
        }
    }

    static BufferedImage peek(String resource) {
        return loadImage(resource);
    }
}
