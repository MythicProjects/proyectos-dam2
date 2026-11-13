package view.components;

import java.awt.GridLayout;
import java.util.List;
import java.util.function.IntConsumer;

import javax.swing.JPanel;

import view.dto.ProductVM;
import view.theme.Theme;
import view.theme.Themed;

public class ProductsPanel extends JPanel implements Themed {
    private static final int COUNT = 6;

    private final ProductButton[] buttons = new ProductButton[COUNT];
    private final int[] productIDs = new int[COUNT];

    public ProductsPanel(){
        super(new GridLayout(COUNT, 1, 0, 4));

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new  ProductButton();
            buttons[i].setEnabled(false);
            add(buttons[i]);
        }
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setProducts(List<ProductVM> products){
        for (int i = 0; i < buttons.length && i < products.size(); i++) {
            productIDs[i] = products.get(i).id();
            buttons[i].setProduct(products.get(i));
        }
    }

    // ------------------------------------------------------------------
    // Events
    // ------------------------------------------------------------------
    public void onBuyProduct(IntConsumer action){
        for (int i = 0; i < buttons.length; i++) {
            int index = i;
            buttons[i].addActionListener(e -> action.accept(productIDs[index]));
        }
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        setBackground(theme.paper());
        for (ProductButton button : buttons) {
            button.applyTheme(theme);
        }
    }
}
