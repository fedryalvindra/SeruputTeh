package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

	private static DatabaseManager dbManager;
	private Statement st;
	private Connection conn;
	private final String HOST = "localhost:3306";
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DBNAME = "seruput_teh";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DBNAME);
	
	private DatabaseManager() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static DatabaseManager getInstance() {
		if(dbManager == null) {
			dbManager = new DatabaseManager();
		}
		
		return dbManager;
	}
	
	//Ngebalikin hasil Query (return data)
	public ResultSet execQuery(String query) {
		try {
			st.executeQuery(query);
			return st.getResultSet();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void execUpdate(String query) {
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
	    return conn;
	}

	// Add this method to the DatabaseManager class
	public ResultSet execQueryWithJoin(String query) {
	    try {
	        return st.executeQuery(query);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}


}


