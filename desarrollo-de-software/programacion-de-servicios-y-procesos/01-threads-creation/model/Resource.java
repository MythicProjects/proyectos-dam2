package model;

public class Resource {
    private int product;

    public Resource(int initialProduct){
        this.product = initialProduct;
    }

    synchronized  void  consumeProduct(){
        product--;
    }

    synchronized  void generateNewProduct(){
        product++;
    }

    synchronized int getProduct(){
        return product;
    }
}
