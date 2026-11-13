package view.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;

public class SegmentedBar extends JComponent {
    private float progress;
    private Paint fill;
    private final int segment;
    private final int gap;
    private final boolean vertical;

    public SegmentedBar(int segment, int gap, boolean vertical){
        this.segment = segment;
        this.gap = gap;
        this.vertical = vertical;
        setPreferredSize(vertical ? new Dimension(10, 40) : new Dimension(60, 10));
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setProgress(float progress){
        this.progress = Math.max(0f, Math.min(1f, progress));
        repaint();
    }

    public void setFill(Paint fill){
        this.fill = fill;
        repaint();
    }

    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int length = vertical ? getHeight() : getWidth();
        int thickness = vertical ? getWidth() : getHeight();
        int count = Math.max(1, (length + gap) / (segment + gap));
        int filled = Math.round(progress * count);

        for (int i = 0; i < count; i++) {
            int pos = i * (segment + gap);
            int x = vertical ? 0 : pos;
            int y = vertical ? length - pos - segment : 0;
            int w = vertical ? thickness : segment;
            int h = vertical ? segment : thickness;

            if (i < filled && fill != null) {
                g2.setPaint(fill);
                g2.fillRect(x, y, w, h);
            } else {
                g2.setColor(getBackground());
                g2.drawRect(x, y, w - 1, h - 1);
            }
        }
        g2.dispose();
    }
}
