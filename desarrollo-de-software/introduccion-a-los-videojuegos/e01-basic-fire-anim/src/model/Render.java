package model;

import java.awt.image.BufferedImage;

public class Render {
    private final Palette palette;
    private final BufferedImage image;

    public Render (int width, int height, Palette palette){
        this.palette = palette;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getImage(Fire fire){
        int[][] temperatures = fire.getTemperature();

        for(int y = 0; y < fire.getHeight(); y++){
            for(int x = 0; x < fire.getWidth(); x++){
                // pixel por pixel, da temperatura -> recibe color
                image.setRGB(x, y, palette.color(temperatures[y][x]));
            }
        }

        return image;
    }
}
