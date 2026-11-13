package view;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Viewer extends Canvas{
    private BufferedImage image;


    public void setImage(BufferedImage image){
        this.image = image; // Guarda imagen
        
        repaint(); // Vuelve a dibujar -> Canvas
    }

    // Funciones que vienen del Canvas 
    @Override
    public void paint(Graphics g){
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }

    @Override 
    public void update(Graphics g){
        paint(g);
    }
}
