package view.theme;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public final class Patterns {
    private Patterns() {}

    // ------------------------------------------------------------------
    // Texture factories
    // ------------------------------------------------------------------
    public static TexturePaint checker(Color a, Color b, int cell){
        BufferedImage tile = new BufferedImage(cell*2, cell*2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = tile.createGraphics();
        g.setColor(a);
        g.fillRect(0, 0, cell*2, cell*2);
        g.setColor(b);
        g.fillRect(cell, 0, cell, cell);
        g.fillRect(0, cell, cell, cell);
        g.dispose();

        return new TexturePaint(tile, new Rectangle(0, 0, cell * 2, cell * 2));
    }

    public static  TexturePaint stripes(Color a, Color b, int height){
        BufferedImage tile = new BufferedImage(1, height * 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = tile.createGraphics();
        g.setColor(a);
        g.fillRect(0, 0, 1, height);
        g.setColor(b);
        g.fillRect(0, height, 1, height);
        g.dispose();

        return new TexturePaint(tile, new Rectangle(0, 0, 1, height * 2));
    }

    public static TexturePaint diagonal(Color a, Color b, int width){
        int size = width * 2;
        BufferedImage tile = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++){
                tile.setRGB(x, y, ((x + y) % size) < width ? a.getRGB() : b.getRGB());
            }
        }

        return new TexturePaint(tile, new Rectangle(0, 0, size, size));
    }
}
