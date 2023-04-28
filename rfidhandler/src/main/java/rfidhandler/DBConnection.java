package rfidhandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	
	private static Connection connection = null;
	
	public static Connection getConnection() {
		try {
			if(connection == null || connection.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intelligent_systems", "root", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return connection;
	}
	
}
