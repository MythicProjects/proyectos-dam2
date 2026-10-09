package model;

public class Resource {
    private int product;

    public Resource(int initialProduct){
        this.product = initialProduct;
    }

    public void  consumeProduct(){
        product--;
    }

    public void generateNewProduct(){
        product++;
    }

    public int getProduct(){
        return product;
    }
}
