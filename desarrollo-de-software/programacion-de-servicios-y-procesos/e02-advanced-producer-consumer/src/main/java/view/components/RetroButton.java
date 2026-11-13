package view.components;

import javax.swing.JButton;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.Icon;

import view.theme.Patterns;
import view.theme.Theme;
import view.theme.Themed;

public class RetroButton extends JButton implements Themed {
    private final RetroBorder border = new RetroBorder (2, 2);
    protected Theme theme = Theme.SNES;

    public RetroButton (String text, Icon icon){
        super(text, icon);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        boolean pressed = getModel().isArmed() && getModel().isPressed();
        g2.setColor(pressed ? theme.accent() : getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        super.paintComponent(g);

        if (!isEnabled()){
            Graphics2D d = (Graphics2D) g.create();
            paintDisabled(d);
            d.dispose();
        }
    }

    protected void paintDisabled(Graphics2D g){
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
        g.setPaint(Patterns.diagonal(theme.paper(), theme.muted(), 3));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        this.theme = theme;
        border.applyTheme(theme);
        setBackground(theme.paper());
        setForeground(theme.ink());
        setFont(theme.uiFont());
        repaint();
    }
}
