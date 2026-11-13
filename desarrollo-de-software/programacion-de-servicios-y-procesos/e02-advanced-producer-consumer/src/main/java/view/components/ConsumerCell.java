package view.components;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import view.dto.ConsumerSlotVM;
import view.theme.Icons;
import view.theme.PixelIcon;
import view.theme.Theme;
import view.theme.Themed;

public class ConsumerCell extends JComponent implements Themed {
    private static final int BAR_HEIGHT = 4;

    private boolean busy;
    private Paint food;
    private float progress;
    private Theme theme = Theme.SNES;
    private ConsumerSlotVM slot;

    public ConsumerCell(){
        setPreferredSize(new Dimension(24, 30));
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setSlot(ConsumerSlotVM slot){
        this.slot = slot;
        busy = slot.busy();
        food = theme.productPaint(slot.productID());
        progress = slot.progress();
        repaint();
    }

    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();

        PixelIcon person = Icons.PERSON.withColor(busy ? theme.ink() : theme.muted());
        int x = (getWidth() - person.getIconWidth()) / 2;
        int y = (getHeight() - BAR_HEIGHT - person.getIconHeight()) / 2;
        person.paintIcon(this, g2, x, y);

        if(busy) {
            int barY = getHeight() - BAR_HEIGHT;
            g2.setColor(theme.muted());
            g2.drawRect(0, barY, getWidth() - 1, BAR_HEIGHT -1);
            g2.setPaint(food);
            g2.fillRect(0, barY, Math.round(getWidth() * progress), BAR_HEIGHT);
        }

        g2.dispose();
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        this.theme = theme;
        if (slot != null) setSlot(slot);
        repaint();
    }
}
