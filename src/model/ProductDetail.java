package model;

public class ProductDetail extends Product{

	public ProductDetail(String productID, String name, String productDescription, Integer price) {
		super(productID, name, productDescription, price);
	}
	public String toString() {
        return String.format(getName() + " " + getPrice());
}
}
