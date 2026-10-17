package view;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Viewer extends Canvas{
    private volatile BufferedImage render;

    public void updateRender(BufferedImage render){
        this.render = render;
        repaint();
    }


    @Override
    public void paint(Graphics g) {
        if (render != null){
            g.drawImage(render, 0, 0, getWidth(), getHeight(), null);
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}