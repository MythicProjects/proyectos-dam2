package view.components;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import view.theme.Patterns;
import view.theme.Theme;
import view.theme.Themed;

public class TitleBar extends JComponent implements Themed {
    private final String title;
    private Theme theme = Theme.SNES;

    public TitleBar(String title) {
        this.title = title;
        setPreferredSize(new Dimension(0, 22));
    }

    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g2.setPaint(Patterns.stripes(theme.stripeA(), theme.stripeB(), 2));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setFont(theme.uiFont());
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(title);
        int boxW = textW + 16;
        int boxX = (getWidth() - boxW) / 2;
        g2.setColor(theme.paper());
        g2.fillRect(boxX, 0, boxW, getHeight());
        g2.setColor(theme.ink());
        g2.drawString(title, boxX + 8, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());

        g2.fillRect(0, getHeight() - 2, getWidth(), 2);
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
