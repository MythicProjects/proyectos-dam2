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

/** Draws the last list of animations it was given. */
final class Canvas extends JPanel {

    private volatile List<AnimationView> views = List.of();   // written by Render, read by Swing

    Canvas(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.DARK_GRAY);
    }

    void setViews(List<AnimationView> views) {
        this.views = views;
        repaint();                          // asks Swing to call paintComponent soon
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);            // clears the previous frame
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);   // sharp pixels
        g2.setColor(Color.WHITE);

        for (AnimationView view : this.views) {
            BufferedImage image = view.image();
            int width = image.getWidth() * view.scale();
            int height = image.getHeight() * view.scale();
            g2.drawImage(image, view.x(), view.y(), width, height, null);
            g2.drawString(view.name() + "  " + view.fps() + " fps", view.x(), view.y() - 4);
        }
    }
}
