package validation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import model.Cart;
import model.User;
import util.DatabaseManager;

public class CartValidation {
	
	public void showCart(DatabaseManager dbManager, User loggedInUser, ObservableList<Cart> carts) {
		String query = "SELECT product.productID, product.product_name, product.product_price, product.product_des, cart.quantity " +
                "FROM product " +
                "JOIN cart ON product.productID = cart.productID " +
                "WHERE cart.userID = ?";

		 try {
		     PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(query);
		     preparedStatement.setString(1, loggedInUser.getUserID());
		     ResultSet resultSet = preparedStatement.executeQuery();
		     while (resultSet.next()) {
		         String productID = resultSet.getString("productID");
		         String productName = resultSet.getString("product_name");
		         Integer productPrice = resultSet.getInt("product_price");
		         String productDescription = resultSet.getString("product_des");
		         Integer quantity = resultSet.getInt("quantity");
		         Cart cartItem = new Cart(productID, productName, productDescription, productPrice, quantity);
		         carts.add(cartItem); 
		     }
		 } catch (SQLException ex) {
		     ex.printStackTrace();
		 }
	}

	public void insertTransactionHeader(String transactionId, String userId) {
	    String query = "INSERT INTO transaction_header (transactionID, userID) VALUES (?, ?)";
	    try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
	        PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(query);
	        preparedStatement.setString(1, transactionId);
	        preparedStatement.setString(2, userId);
	        preparedStatement.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	public void insertTransactionDetail(String transactionId, String productId, int quantity) {
	    String query = "INSERT INTO transaction_detail (transactionID, productID, quantity) VALUES (?, ?, ?)";
	    try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
	        PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(query);
	        preparedStatement.setString(1, transactionId);
	        preparedStatement.setString(2, productId);
	        preparedStatement.setInt(3, quantity);
	        preparedStatement.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	public void clearUserCart(String userId) {
	    String query = "DELETE FROM cart WHERE userID = ?";
	    try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
	        PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(query);
	        preparedStatement.setString(1, userId);
	        preparedStatement.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	
	public void updateCart(int newQuantity, Cart selectCart, User loggedInUser, SpinnerValueFactory<Integer> valueFactory, String updateQuery) throws SQLException {
		DatabaseManager dbManager = DatabaseManager.getInstance();
        PreparedStatement updateStatement = dbManager.getConnection().prepareStatement(updateQuery);
        updateStatement.setInt(1, newQuantity);
        updateStatement.setString(2, selectCart.getProductID());
        updateStatement.setString(3, loggedInUser.getUserID());
        updateStatement.executeUpdate();

        selectCart.setQuantity(newQuantity);
        ((IntegerSpinnerValueFactory) valueFactory).setMax(newQuantity);
	}
	
	public void deleteCart(Cart selectCart, User loggedInUser) throws SQLException {
		String deleteQuery = "DELETE FROM cart WHERE productID = ? AND userID = ?";
        DatabaseManager dbManager = DatabaseManager.getInstance();
        PreparedStatement deleteStatement = dbManager.getConnection().prepareStatement(deleteQuery);
        deleteStatement.setString(1, selectCart.getProductID());
        deleteStatement.setString(2, loggedInUser.getUserID());
        deleteStatement.executeUpdate();
	}
	
	public void showUpdated() {
		Alert updateCart = new Alert(AlertType.INFORMATION);
        updateCart.setTitle("Message");
        updateCart.setHeaderText("Updated Cart");
        updateCart.showAndWait();
	}
	
	public void showRemoved() {
		Alert removeCart = new Alert(AlertType.INFORMATION);
        removeCart.setTitle("Message");
        removeCart.setHeaderText("Deleted from Cart");
        removeCart.showAndWait();
	}

	public void showEmpty() {
		Alert emptyCartAlert = new Alert(AlertType.ERROR);
        emptyCartAlert.setTitle("Error");
        emptyCartAlert.setHeaderText("Empty Cart");
        emptyCartAlert.setContentText("Your cart is empty. Add items before making a purchase.");
        emptyCartAlert.showAndWait();
	}
	
	public void showSuccess() {
		Alert successfulPurchase = new Alert(AlertType.INFORMATION);
        successfulPurchase.setTitle("Message");
        successfulPurchase.setHeaderText("Successfully Purchased");
        successfulPurchase.showAndWait();
	}
	
}
