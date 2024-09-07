package connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector implements Connectors {

	@Override
	public Object connect() throws Exception {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "Yoga@11ark");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
		return con;
	}

}
