package view.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;

public class Swatch extends JComponent {
    private Paint paint;

    public Swatch(){
        setPreferredSize(new Dimension(14, 14));
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setPaint(Paint paint){
        this.paint = paint;
        repaint();
    }

    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        if(paint != null){
            g2.setPaint(paint);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setColor(getBackground());
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2.dispose();
    }
}
