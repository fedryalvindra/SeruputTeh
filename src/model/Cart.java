package model;


public class Cart extends Product {
    private Integer quantity;

    public Cart(String productId, String name, String productDescription, Integer price, Integer quantity) {
        super(productId, name, productDescription, price);
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return String.format(String.valueOf(getQuantity()) + "x " + getName() + " (Rp." + String.valueOf(getQuantity() * getPrice()) + ")");
    }


}
