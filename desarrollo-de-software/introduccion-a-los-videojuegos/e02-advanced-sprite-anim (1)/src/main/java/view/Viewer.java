package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;
import contract.AnimationView;

final class Viewer extends JPanel {
    private final int scale;
    private BufferedImage background;

    private volatile List<AnimationView> views = List.of();

    Viewer(int scale){
        this.scale = scale;
        setBackground(Color.DARK_GRAY);
    }

    void setViews(List<AnimationView> views){
        this.views = views;
        repaint();
    }

    @Override 
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if(this.background !=null) {
            g2.drawImage(this.background, 0, 0, 
                this.background.getWidth() * this.scale, this.background.getHeight() * this.scale, null);
        }
        List<AnimationView> current = this.views;
        for (AnimationView view : current) {
            BufferedImage image = view.image();
            g2.drawImage(image, 
                view.x() * this.scale, view.y()*this.scale, 
                image.getWidth() * this.scale, image.getHeight() * this.scale,
                null);
            g2.setColor(Color.MAGENTA);
            g2.drawString(view.fps() + " fps",
                view.x() * this.scale,
                view.y() * this.scale - 4);   // just above the sprite
        }
    }

    public  void setBackgroundImage(BufferedImage background){
        this.background = background;
        setPreferredSize(new Dimension(
            background.getWidth() * this.scale, background.getHeight() * this.scale));
    }
}
