package view.components;

import view.dto.ProductVM;
import view.theme.Icons;
import view.theme.Theme;

public class ProductButton extends RetroButton {
    private ProductVM product;

    public ProductButton(){
        super("", null);
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setProduct(ProductVM product){
        this.product = product;

        if(product.unlocked()){
            setText(String.format("%s, $%,d", product.name(), product.price()));
            setIcon(null);
            setEnabled(true);
        } else {
            setText("Locked");
            setIcon(Icons.LOCK);
            setEnabled(false);
        }
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        super.applyTheme(theme);
        if(product != null) setProduct(product);
    }
}
