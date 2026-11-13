package view.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

import view.theme.Theme;
import view.theme.Themed;

public class RetroBorder extends AbstractBorder implements Themed {
    private final int lineWidth;
    private final int shadowOffset;
    private Color ink = Theme.SNES.ink();
    private Color shadow = Theme.SNES.shadow();

    public RetroBorder(int lineWidth, int shadowOffset){
        this.lineWidth = lineWidth;
        this.shadowOffset = shadowOffset;
    }

    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        g.setColor(shadow);
        g.fillRect(x + shadowOffset, y + h - shadowOffset, w - shadowOffset, shadowOffset);
        g.fillRect(x + w - shadowOffset, y + shadowOffset, shadowOffset, h - shadowOffset);

        g.setColor(ink);
        int bw = w - shadowOffset;
        int bh = h - shadowOffset;
        g.fillRect(x, y, bw, lineWidth);
        g.fillRect(x, y + bh - lineWidth, bw, lineWidth);
        g.fillRect(x, y, lineWidth, bh);
        g.fillRect(x + bw - lineWidth, y, lineWidth, bh);
    }

    // ------------------------------------------------------------------
    // Insets
    // ------------------------------------------------------------------
    @Override
    public Insets getBorderInsets(Component c){
        return new Insets(lineWidth, lineWidth, lineWidth + shadowOffset, lineWidth + shadowOffset);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets){
        insets.set(lineWidth, lineWidth, lineWidth + shadowOffset, lineWidth + shadowOffset);
        return insets;
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        ink = theme.ink();
        shadow = theme.shadow();
    }
}
