package validation;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.DatabaseManager;

public class RegisterValidation {
	
	public String generateUserID() {
	    String countQuery = "SELECT COUNT(*) FROM user";
	    ResultSet countResult = DatabaseManager.getInstance().execQuery(countQuery);

	    try {
	        if (countResult.next()) {
	            int userCount = countResult.getInt(1) + 1; // Increment by one for the new user
	            return String.format("CU%03d", userCount);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return "CU000";
	}
	
	public boolean isValidUsername(String username) {
	    if (username.length() < 5 || username.length() > 20) {
	        return false;
	    }

	    String checkUniqueQuery = "SELECT COUNT(*) FROM user WHERE username='" + username + "'";
	    ResultSet uniqueResult = DatabaseManager.getInstance().execQuery(checkUniqueQuery);

	    try {
	        if (uniqueResult.next() && uniqueResult.getInt(1) > 0) {
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return true;
	}

	public boolean isValidEmail(String email) {
	    return email.endsWith("@gmail.com");
	}

	public boolean isValidPassword(String password) {
	    return password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{5,}$");
	}

	public boolean isValidPhoneNumber(String phoneNumber) {
	    return phoneNumber.matches("^\\+62[0-9]+$");
	}

	public void showAlert(String title, String message) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
}

