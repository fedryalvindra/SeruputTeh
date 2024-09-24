package validation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Cart;
import model.Transaction;
import model.User;
import util.DatabaseManager;

public class TransactionValidation {

	public String generateTransactionId() {
	    String query = "SELECT COUNT(*) FROM transaction_header";
	    try {
	        DatabaseManager dbManager = DatabaseManager.getInstance();
	        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            int transactionCount = resultSet.getInt(1) + 1;
	            return String.format("TR%03d", transactionCount);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public ObservableList<Transaction> fetchUserTransactions(User loggedInUser) {
	    ObservableList<Transaction> transactions = FXCollections.observableArrayList();

	    String query = "SELECT transaction_header.transactionID, user.userID, product.productID, product.product_name, product.product_price, product.product_des, transaction_detail.quantity " +
	            "FROM transaction_header " +
	            "JOIN transaction_detail ON transaction_header.transactionID = transaction_detail.transactionID " +
	            "JOIN user ON transaction_header.userID = user.userID " +
	            "JOIN product ON transaction_detail.productID = product.productID " +
	            "WHERE user.userID = '" + loggedInUser.getUserID() + "'";

	    ResultSet resultSet = DatabaseManager.getInstance().execQueryWithJoin(query);

	    try {
	        while (resultSet.next()) {
	            String transactionId = resultSet.getString("transactionID");
	            String userId = resultSet.getString("userID");
	            String productId = resultSet.getString("productID");
	            String productName = resultSet.getString("product_name");
	            int productPrice = resultSet.getInt("product_price");
	            int quantity = resultSet.getInt("quantity");
	            String productDescription = resultSet.getString("product_des");

	            Cart product = new Cart(productId, productName, productDescription, productPrice, quantity);

	            Transaction transaction = transactions.stream()
	                    .filter(t -> t.getTransactionId().equals(transactionId))
	                    .findFirst()
	                    .orElseGet(() -> {
	                        Transaction newTransaction = new Transaction(productId, productName, productDescription, productPrice, quantity, transactionId, userId, new ArrayList<>());
	                        transactions.add(newTransaction);
	                        return newTransaction;
	                    });

	            transaction.getPurchasedProducts().add(product);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return transactions;
	}

	public void setupTransactionDetailListView(ListView<Transaction> listTransaction, Label transactionValue, Label totalAllPrice, ListView<Cart> listTransactionDetail) {
	    listTransaction.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue != null) {
	            String selectedTransactionId = newValue.getTransactionId();
	            Integer selectedTransactionPrice = newValue.getPrice();
	            Integer selectedTransactionQty = newValue.getQuantity();
	            transactionValue.setText(selectedTransactionId);
	            totalAllPrice.setText(String.valueOf(selectedTransactionPrice * selectedTransactionQty));
	            ObservableList<Cart> transactionDetails = fetchTransactionDetails(selectedTransactionId);

	            listTransactionDetail.setItems(transactionDetails);
	        }
	    });
	}
	
	public ObservableList<Cart> fetchTransactionDetails(String transactionId) {
	    ObservableList<Cart> transactionDetails = FXCollections.observableArrayList();

	    String query = "SELECT product.productID, product.product_name, product.product_price, product.product_des, transaction_detail.quantity " +
	            "FROM transaction_detail " +
	            "JOIN product ON transaction_detail.productID = product.productID " +
	            "WHERE transaction_detail.transactionID = '" + transactionId + "'";

	    ResultSet resultSet = DatabaseManager.getInstance().execQuery(query);

	    try {
	        while (resultSet.next()) {
	            String productId = resultSet.getString("productID");
	            String productName = resultSet.getString("product_name");
	            int productPrice = resultSet.getInt("product_price");
	            int quantity = resultSet.getInt("quantity");
	            String productDescription = resultSet.getString("product_des");

	            Cart product = new Cart(productId, productName, productDescription, productPrice, quantity);
	            transactionDetails.add(product);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return transactionDetails;
	}

	public void insertTransaction(String transactionId, User loggedInUser, ObservableList<Cart> carts, ListView<Cart> listCart) throws SQLException {
		String insertTransactionQuery = "INSERT INTO transaction_header (transactionID, userID) VALUES (?, ?)";
        DatabaseManager dbManager = DatabaseManager.getInstance();
        PreparedStatement insertTransactionStatement = dbManager.getConnection().prepareStatement(insertTransactionQuery);
        insertTransactionStatement.setString(1, transactionId);
        insertTransactionStatement.setString(2, loggedInUser.getUserID());
        insertTransactionStatement.executeUpdate();

        String insertDetailQuery = "INSERT INTO transaction_detail (transactionID, productID, quantity) VALUES (?, ?, ?)";
        PreparedStatement insertDetailStatement = dbManager.getConnection().prepareStatement(insertDetailQuery);

        for (Cart cartItem : carts) {
            insertDetailStatement.setString(1, transactionId);
            insertDetailStatement.setString(2, cartItem.getProductID());
            insertDetailStatement.setInt(3, cartItem.getQuantity());
            insertDetailStatement.addBatch();
        }

        insertDetailStatement.executeBatch();

        String clearCartQuery = "DELETE FROM cart WHERE userID = ?";
        PreparedStatement clearCartStatement = dbManager.getConnection().prepareStatement(clearCartQuery);
        clearCartStatement.setString(1, loggedInUser.getUserID());
        clearCartStatement.executeUpdate();

        carts.clear();

        listCart.refresh();
	}
	
}
