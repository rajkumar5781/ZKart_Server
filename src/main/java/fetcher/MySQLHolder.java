package fetcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import utilities.MySQLUtilitys;

public class MySQLHolder {

	private Connection connection;
	private HashMap<String, String> tableHeaderDetails = new HashMap<>();
	private ResultSet resultSet;
	private Statement statement;
	private String tableName;
	private ArrayList<String> queryStatements = new ArrayList<>();
	
	
	public HashMap<String, String> getTableHeaderDetails() {
		return tableHeaderDetails;
	}

	public MySQLHolder() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "Yoga@11ark");
	}

	public void connect(String tableName) throws Exception {
		try {
			this.tableName = tableName;
			statement = connection.createStatement();
			loadHeaderDetails();
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		}
	}

	private void getResultSet() throws SQLException {
		resultSet = connection.getMetaData().getColumns(null, null, this.tableName, null);
	}

	private void loadHeaderDetails() throws SQLException {
		getResultSet();
		while (resultSet.next()) {
			tableHeaderDetails.put(resultSet.getString("COLUMN_NAME"), resultSet.getString("TYPE_NAME"));
		}
	}

	public void addRecord(HashMap<String, Object> columnValues) {
		String queryString = MySQLUtilitys.getInsertQuery(MySQLUtilitys.getColumnNames(columnValues), MySQLUtilitys.getInsertValueString(columnValues,tableHeaderDetails),this.tableName);
		System.out.print(queryString);
		addQueryStatements(MySQLUtilitys.getInsertQuery(MySQLUtilitys.getColumnNames(columnValues), MySQLUtilitys.getInsertValueString(columnValues,tableHeaderDetails),this.tableName));
	}
	
	public void closeConnection() throws SQLException {
		if(connection!=null) {
			connection.close();
		}
	}
	
	public void updateRecord() {
		
	}
	
	private String getUpdateValueString(HashMap<String, Object> columnValues) {
		String columnValue = "";
		int count = 0;

		for (Map.Entry columnEntry : columnValues.entrySet()) {
			String columnNameValue = (String) columnEntry.getKey();
			String dataType = tableHeaderDetails.getOrDefault(columnNameValue, null);
			if (dataType != null) {
				if (dataType.equals("INT") || dataType.equals("DECIMAL") || dataType.equals("BOOLEAN")) {
					if (count > 0) {
						columnValue = columnValue + ",";
					} else {

					}
					columnValue = columnValue + columnNameValue +" = "+ columnEntry.getValue();
					count++;
				} else if (dataType.equals("DATE") || dataType.equals("TIME") || dataType.equals("DATETIME")
						|| dataType.equals("TIMESTAMP") || dataType.equals("VARCHAR")) {
					if (count > 0) {
						columnValue = columnValue + ",";
					}
					columnValue = columnValue + columnNameValue +" = "+ "'" + columnEntry.getValue() + "'";

					count++;
				}
			}
		}
		return columnValue;
	}

	public void executeStatement() {
		try {
			bulkExecute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	private String getInsertQuery(String columnNames, String columnValues) {
//		return "insert into " + this.tableName + " (" + columnNames + ") values (" + columnValues + ");";
//	}
	
	private String getUpdateQuery(String columnValues) {
		return "update " + this.tableName + " set " + "(" + columnValues + ");";
	}

	public void addQueryStatements(String query) {
		queryStatements.add(query);
	}

	public void bulkExecute() throws SQLException {
		queryStatements.forEach(querystatement -> {
			try {
				statement.addBatch(querystatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		int[] executedHistory = statement.executeBatch();
		int failedQueries = 0;
		for (int i = 0; i < executedHistory.length; i++) {
			if (executedHistory[i] == Statement.EXECUTE_FAILED) {
				failedQueries++;
			}
		}
		if (failedQueries > 0) {
			System.out.println("failedQueries : " + failedQueries);
		}
	}

	public int recordCount(String query) throws SQLException {
		
		if (getRecords(query).next()) {
			return resultSet.getInt(1);
		}
		return 0;
	}

	public JSONArray getRecordsJSON(String query) throws SQLException {
		JSONArray jsonList = new JSONArray();
		getRecords(query);
		while (resultSet.next()) {

			for (Map.Entry columnNamEntry : tableHeaderDetails.entrySet()) {
				JSONObject item = new JSONObject();
				String dataType = (String) columnNamEntry.getValue();
				if (dataType.equals("INT") || dataType.equals("DECIMAL")) {
					item.put((String) columnNamEntry.getKey(), resultSet.getInt((String) columnNamEntry.getKey()));
				} else if (dataType.equals("BOOLEAN")) {
					item.put((String) columnNamEntry.getKey(), resultSet.getBoolean((String) columnNamEntry.getKey()));
				} else if (dataType.equals("DATE")) {
					item.put((String) columnNamEntry.getKey(), resultSet.getDate((String) columnNamEntry.getKey()));
				} else if (dataType.equals("VARCHAR")) {
					item.put((String) columnNamEntry.getKey(), resultSet.getString((String) columnNamEntry.getKey()));
				} else if (dataType.equals("TIME")) {
					item.put((String) columnNamEntry.getKey(), resultSet.getTime((String) columnNamEntry.getKey()));
				}
				jsonList.put(item);
			}
		}
		return jsonList;
	}

	public ResultSet getRecords(String query) throws SQLException {
		resultSet = statement.executeQuery(query);
		return resultSet;
	}

	public void executeInsertQuery(String query) {
		try {
			statement.execute(query);
			System.out.println("Query executed successfully.");
		} catch (SQLException e) {
			System.err.println("Error executing query: " + e.getMessage());
		}
	}

	public JSONArray executeSelectQuery(String query) {
		try {
			resultSet = statement.executeQuery(query);

			JSONArray jsonArray = new JSONArray();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (resultSet.next()) {
				JSONObject jsonRow = new JSONObject();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnLabel(i);
					Object value = resultSet.getObject(i);
					if(value == null) {
						jsonRow.put(columnName, "");
					}
					else {
					jsonRow.put(columnName, value);
					}
				}
				jsonArray.put(jsonRow);
			}
			return jsonArray;
		} catch (SQLException e) {
			System.err.println("Error executing select query: " + e.getMessage());
			return null;
		}
	}

	public void executeUpdateQuery(String query) {
		try {
			int rowsAffected = statement.executeUpdate(query);
			System.out.println("Update query executed successfully. " + rowsAffected + " rows affected.");
		} catch (SQLException e) {
			System.err.println("Error executing update query: " + e.getMessage());
		}
	}

	public void executeDeleteQuery(String query) {
		try {
			int rowsAffected = statement.executeUpdate(query);
			System.out.println("Delete query executed successfully. " + rowsAffected + " rows deleted.");
		} catch (SQLException e) {
			System.err.println("Error executing delete query: " + e.getMessage());
		}
	}

}
