package view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Viewer extends JPanel{
    // Buffered Image
    private volatile BufferedImage render;

    // ------------------------------------------------------------------
    // Update viewer
    // ------------------------------------------------------------------
    public void updateRender(BufferedImage render){
        this.render = render;
        repaint();
    }
    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        BufferedImage image = render;
        if(image != null){
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);        }
    }
}