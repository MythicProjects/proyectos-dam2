package model;

public class Resource {
    private int stock;

    public Resource(int initialProduct){
        this.stock = initialProduct;
    }

    synchronized void  consumeProduct(){
        stock--;
    }

    synchronized void generateNewProduct(){
        stock++;
    }

    public int getStock(){
        return stock;
    }
}
