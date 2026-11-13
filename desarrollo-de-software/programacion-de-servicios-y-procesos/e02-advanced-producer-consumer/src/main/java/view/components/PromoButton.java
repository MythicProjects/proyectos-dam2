package view.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.SwingConstants;

import view.dto.PromoVM;
import view.theme.Icons;
import view.theme.Patterns;
import view.theme.Theme;

public class PromoButton extends RetroButton {
    private static final int BAND = 12;
    private PromoVM promo;

    public PromoButton(){
        super("Promo", Icons.MEGAPHONE);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setEnabled(false);
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setPromo(PromoVM promo){
        this.promo = promo;
        setText(String.format("<html><center>Promo  $%,d<br>%d s</center></html>", promo.price(), promo.durationSec()));
        setEnabled(promo.available());
        updateColors();
    }

    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isEnabled()) {
            Graphics2D g2 = (Graphics2D) g.create();
            Insets in = getInsets();
            int h = getHeight() - in.top - in.bottom;
            g2.setPaint(Patterns.stripes(theme.accentText(), theme.accent(), 2));
            g2.fillRect(in.left, in.top, BAND, h);
            g2.fillRect(getWidth() - in.right - BAND, in.top, BAND, h);
            g2.dispose();
        }
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        updateColors();
    }

    private void updateColors(){
        boolean on = isEnabled();
        setBackground(on ? theme.accent() : theme.paper());
        setForeground(on ? theme.accentText() : theme.ink());
    }
}
