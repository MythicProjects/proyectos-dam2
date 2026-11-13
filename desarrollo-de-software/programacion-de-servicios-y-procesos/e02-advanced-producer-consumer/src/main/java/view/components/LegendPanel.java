package view.components;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.dto.ProductVM;
import view.theme.Theme;
import view.theme.Themed;

public class LegendPanel extends JPanel implements Themed{
    private final List<Swatch> swatches = new ArrayList<>();
    private final List<JLabel> labels = new ArrayList<>();
    private Theme theme = Theme.SNES;
    private List<ProductVM> products = List.of();

    public LegendPanel() {
        super(new FlowLayout(FlowLayout.LEFT, 6, 2));
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setEntries(List<ProductVM> products, int[] percents) {
        this.products = products;
        if(swatches.size() != products.size()){
            rebuild(products.size());
        }

        for (int i = 0; i < percents.length; i++) {
            ProductVM product = products.get(i);
            int percent = product.id() < percents.length ? percents[product.id()] : 0;
            swatches.get(i).setPaint(theme.productPaint(product.id()));
            labels.get(i).setText(product.name() + " " + percent + "%");
            swatches.get(i).setVisible(product.unlocked());
            labels.get(i).setVisible(product.unlocked());
        }
    }

    // ------------------------------------------------------------------
    // Layout
    // ------------------------------------------------------------------
    private void rebuild(int count){
        removeAll();
        swatches.clear();
        labels.clear();
        for (int i = 0; i < count; i++) {
            Swatch swatch = new Swatch();
            JLabel label = new JLabel();
            swatches.add(swatch);
            labels.add(label);
            add(swatch);
            add(label);
        }

        applyTheme(theme);
        revalidate();
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    public void applyTheme(Theme theme){
        this.theme = theme;
        setBackground(theme.paper());
        for (int i = 0; i < swatches.size(); i++) {
            swatches.get(i).setForeground(theme.ink());
            labels.get(i).setForeground(theme.ink());
            labels.get(i).setFont(theme.uiFont());
            if (i < products.size()){
                swatches.get(i).setPaint(theme.productPaint(products.get(i).id()));
            }
        }
    }
}
