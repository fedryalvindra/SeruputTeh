package model;

import java.util.List;

public class Transaction extends Cart{
    private String transactionId;
    private String userId;
    private List<Product> purchasedProducts;

    

    public Transaction(String productId, String name, String productDescription, Integer price, Integer quantity,
			String transactionId, String userId, List<Product> purchasedProducts) {
		super(productId, name, productDescription, price, quantity);
		this.transactionId = transactionId;
		this.userId = userId;
		this.purchasedProducts = purchasedProducts;
	}

	public String getTransactionId() {
        return transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public List<Product> getPurchasedProducts() {
        return purchasedProducts;
    }
    
    public String toString() {
        return String.format(getTransactionId());
    }
}
