package src.model;

import java.awt.image.BufferedImage;

public class Render {
    private final Fire fire;
    private final Palette palette;
    private final BufferedImage image;

    public Render (Fire fire, Palette palette){
        this.fire = fire;
        this.palette = palette;
        this.image = new BufferedImage(fire.getWidth(), fire.getWidth(), BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getImage(){
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
