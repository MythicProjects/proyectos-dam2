package view.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import view.theme.Theme;
import view.theme.Themed;

public class HistogramCanvas extends JComponent implements Themed {
    private int[][] columns = new int[0][];
    private final int cellSize = 8;
    private final int gap = 2;
    private Theme theme = Theme.SNES;

    public HistogramCanvas(){
        setPreferredSize(new Dimension(0, 0));
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setColumns(int[][] columns){
        this.columns = columns;
        repaint();
    }

    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        int step = cellSize + gap;
        int baseline = getHeight() - 2;

        int visible = Math.min(columns.length, getWidth() / step);
        int first = columns.length - visible;

        for (int c = 0; c < visible; c++) {
            int[] column = columns[first + c];
            int x = c * step;

            for (int r = 0; r < column.length; r++) {
                int y = baseline - (r + 1) * step;
                if (y < 0) break;
                g2.setPaint(theme.productPaint(column[r]));
                g2.fillRect(x, y, cellSize, cellSize);
            }
        }

        g2.setColor(theme.ink());
        g2.fillRect(0, baseline, getWidth(), 2);
        g2.dispose();
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        this.theme = theme;
        repaint();
    }
}
