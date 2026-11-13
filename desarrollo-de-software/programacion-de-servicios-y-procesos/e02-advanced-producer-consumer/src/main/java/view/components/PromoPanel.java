package view.components;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import view.dto.PromoVM;
import view.theme.Theme;
import view.theme.Themed;

public class PromoPanel extends JPanel implements Themed {
    private final PromoButton button = new PromoButton();

    public PromoPanel(){
        super(new BorderLayout());
        add(button, BorderLayout.CENTER);
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setPromo(PromoVM promo) { button.setPromo(promo); }

    // ------------------------------------------------------------------
    // Events
    // ------------------------------------------------------------------
    public void onPromo(Runnable action) { button.addActionListener(e -> action.run()); }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    public void applyTheme(Theme theme) {
        setBackground(theme.paper());
        button.applyTheme(theme);
    }
}
