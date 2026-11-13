package view.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class PixelIcon implements Icon{
    private final String[] mask;
    private final int scale;
    private final Color color;

    public PixelIcon(String[] mask, int scale, Color color){
        this.mask = mask.clone();
        this.scale = scale;
        this.color = color;
    }

    // ------------------------------------------------------------------
    // Icon
    // ------------------------------------------------------------------
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y){
        g.setColor(color != null ? color : c.getForeground());

        for (int row = 0; row < mask.length; row++){
            for (int col = 0; col <mask[row].length(); col++){
                if (mask [row].charAt(col) == '#'){
                    g.fillRect(x + col * scale, y + row * scale, scale, scale);
                }
            }
        }
    }

    @Override
    public int getIconWidth(){
        return mask[0].length() * scale;
    }

    @Override
    public int getIconHeight(){
        return mask.length * scale;
    }

    // ------------------------------------------------------------------
    // Variants
    // ------------------------------------------------------------------
    public PixelIcon withColor(Color color){
        return new PixelIcon(mask, scale, color);
    }
}
