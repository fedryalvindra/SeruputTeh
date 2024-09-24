package validation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import model.Product;
import util.DatabaseManager;

public class ViewProduct {

	public void viewProduct(ObservableList<Product> products, ListView<Product> listProduct) {
		products.clear();
		DatabaseManager dbManager = DatabaseManager.getInstance();
	    String query = "SELECT * FROM product";
	    ResultSet resultSet = dbManager.execQuery(query);

	    try {
	        while (resultSet.next()) {
	        	String productId = resultSet.getString("productID");
	            String productName = resultSet.getString("product_name");
	            String productDescription = resultSet.getString("product_des");
	            Integer productPrice = resultSet.getInt("product_price");

	            Product product = new Product(productId, productName, productDescription, productPrice);
	            products.add(product);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    listProduct.setItems(products);
	}
	
	public void showProduct() {
		Alert newProduct = new Alert(AlertType.INFORMATION);
		newProduct.setTitle("Message");
		newProduct.setHeaderText("Inserted new product");
		newProduct.showAndWait();
	}
	
	public void removeProduct() {
		Alert removeProduct = new Alert(AlertType.INFORMATION);
		removeProduct.setTitle("Message");
		removeProduct.setHeaderText("Product removed!");
		removeProduct.showAndWait();
	}
	
	public void showError() {
		Alert inputError = new Alert(AlertType.INFORMATION);
		inputError.setTitle("Message");
		inputError.setHeaderText("Error!");
		inputError.showAndWait();
	}
	
	public void showUpdate() {
		Alert updateProduct = new Alert(AlertType.INFORMATION);
		updateProduct.setTitle("Message");
		updateProduct.setHeaderText("Product price updated!");
		updateProduct.showAndWait();
	}
	
	public void insertedValidation(String productName, String productPriceText, String productDescription, ObservableList<Product> products) {
		if (productName.isEmpty() || productPriceText.isEmpty() || productDescription.isEmpty()) {
            showError();
            return;
        }

        try {
            double productPrice = Double.parseDouble(productPriceText);
            if (productPrice <= 0) {
            	showError();
                return;
            }
        } catch (NumberFormatException ex) {
        	showError();;
            return;
        }

        if (isProductNameExists(productName, products)) {
        	showError();
            return;
        }
        showProduct();

	}
	
	public boolean isProductNameExists(String productName, ObservableList<Product> products) {
	    for (Product product : products) {
	        if (product.getName().equalsIgnoreCase(productName)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public String generateProductID(ObservableList<Product> products) {
	    int productCount = products.size() + 1;
	    return String.format("TE%03d", productCount);
	}
	
	public boolean isValidNumber(String input) {
	    try {
	        int number = Integer.parseInt(input);
	        return number > 0;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	public void updateProductPrice(String productID, int newPrice, Label productPriceUpdate) {
	    String query = "UPDATE product SET product_price = ? WHERE productID = ?";
		DatabaseManager dbManager = DatabaseManager.getInstance();
	    try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(query)) {
	        preparedStatement.setInt(1, newPrice);
	        preparedStatement.setString(2, productID);
	        preparedStatement.executeUpdate();
	        productPriceUpdate.setText("Price: Rp. " + String.valueOf(newPrice));
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void removeProduct(String productID) {
		DatabaseManager dbManager = DatabaseManager.getInstance();
	    String query = "DELETE FROM product WHERE productID = ?";
	    try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(query)) {
	        preparedStatement.setString(1, productID);
	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
}
