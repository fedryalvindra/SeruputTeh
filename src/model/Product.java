package model;

public class Product {
	private String productID, name, productDescription;
	private Integer price;
	public Product(String productID, String name, String productDescription, Integer price) {
		super();
		this.productID = productID;
		this.name = name;
		this.productDescription = productDescription;
		this.price = price;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}

	public String toString() {
	        return String.format(name);
	}
	
	
}

