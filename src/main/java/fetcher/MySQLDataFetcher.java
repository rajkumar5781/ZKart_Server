package fetcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class MySQLDataFetcher implements DataFetcher {

	@Override
	public boolean addRecord(String tableName, HashMap<String, Object> columnValues) throws Exception {

		MySQLHolder mySQLHolder = new MySQLHolder();
		mySQLHolder.connect(tableName);
		mySQLHolder.addRecord(columnValues);
		mySQLHolder.executeStatement();
		mySQLHolder.closeConnection();
		return true;
	}

	@Override
	public void addRecordsinBulk(String tableName, ArrayList<HashMap<String, Object>> bulkColumnValues)
			throws Exception {
		MySQLHolder mySQLHolder = new MySQLHolder();
		mySQLHolder.connect(tableName);
		bulkColumnValues.forEach(columnValues -> {
			mySQLHolder.addRecord(columnValues);
		});
		mySQLHolder.bulkExecute();
		mySQLHolder.closeConnection();
	}

	public JSONObject countRecords(String tableName, String query) throws Exception {
		MySQLHolder mySQLHolder = new MySQLHolder();
		mySQLHolder.connect(tableName);

//		return mySQLHolder.recordCount(query);
		
		JSONObject returnObject = new JSONObject() {{put("count",mySQLHolder.recordCount(query)); }};
		mySQLHolder.closeConnection();
		return returnObject;
	}

	@Override
	public boolean validRecord(String tableName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public String selectQuery(String tableName, String[] columns, String[] subqueries) {
		StringBuilder query = new StringBuilder("SELECT ");

		if (columns != null && columns.length > 0) {
			for (int i = 0; i < columns.length; i++) {
				query.append(columns[i]);
				if (i < columns.length - 1) {
					query.append(", ");
				}
			}
		} else {
			query.append("*");
		}
		query.append(" FROM ").append(tableName);
		if (subqueries != null && subqueries.length > 0) {
			query.append(" WHERE ");
			for (int i = 0; i < subqueries.length; i++) {
				query.append("(").append(subqueries[i]).append(")");
				if (i < subqueries.length - 1) {
					query.append(" AND ");
				}
			}
		}

		return query.append(";").toString();
	}

	public String insertQuery(String tableName, String[] columns, String[] values) {
		if (columns.length != values.length) {
			throw new IllegalArgumentException("Columns and values length mismatch");
		}

		StringBuilder query = new StringBuilder("INSERT INTO ").append(tableName).append(" (");

		for (int i = 0; i < columns.length; i++) {
			query.append(columns[i]);
			if (i < columns.length - 1) {
				query.append(", ");
			}
		}
		query.append(") VALUES (");

		for (int i = 0; i < values.length; i++) {
			query.append("'").append(values[i]).append("'");
			if (i < values.length - 1) {
				query.append(", ");
			}
		}

		query.append(")");

		return query.toString();
	}

	public String updateQuery(String tableName, String[] columns, String[] values, String[] conditions) {
		if (columns.length != values.length || columns.length == 0) {
			throw new IllegalArgumentException("Columns and values length mismatch or empty");
		}

		StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");

		// Append column-value pairs
		for (int i = 0; i < columns.length; i++) {
			query.append(columns[i]).append(" = '").append(values[i]).append("'");
			if (i < columns.length - 1) {
				query.append(", ");
			}
		}

		// Append conditions
		if (conditions != null && conditions.length > 0) {
			query.append(" WHERE ");
			for (int i = 0; i < conditions.length; i++) {
				query.append(conditions[i]);
				if (i < conditions.length - 1) {
					query.append(" AND ");
				}
			}
		}

		return query.toString();
	}

	public String deleteQuery(String tableName, String[] conditions) {
		StringBuilder query = new StringBuilder("DELETE FROM ").append(tableName);

		// Append conditions
		if (conditions != null && conditions.length > 0) {
			query.append(" WHERE ");
			for (int i = 0; i < conditions.length; i++) {
				query.append(conditions[i]);
				if (i < conditions.length - 1) {
					query.append(" AND ");
				}
			}
		}

		return query.toString();
	}

	public void executeQuery(String query) {

	}

//	public void executeUpdateQuery(String query) {
//
//	}

	public void executeDeleteQuery(String query) {

	}

	@Override
	public JSONArray executeSelectQuery(String tableName, String query) throws Exception {
		MySQLHolder mySQLHolder = new MySQLHolder();
		mySQLHolder.connect(tableName);
		JSONArray returnJsonArray =  mySQLHolder.executeSelectQuery(query);
		mySQLHolder.closeConnection();
		return returnJsonArray;
	}
	
	public void executeUpdateQuery(String tableName,String updateQuery) throws Exception {
		MySQLHolder mySQLHolder = new MySQLHolder();
		mySQLHolder.connect(tableName);
		mySQLHolder.addQueryStatements(updateQuery);
		mySQLHolder.bulkExecute();
		mySQLHolder.closeConnection();
	}
	
	public void executeBulkUpdateStingQuery(String tableName,ArrayList<String> querys) throws ClassNotFoundException, SQLException {
		final MySQLHolder mySQLHolder = new MySQLHolder();
		try {
			mySQLHolder.connect(tableName);
			querys.forEach(columnValues -> {
				System.out.print(columnValues);
				mySQLHolder.addQueryStatements(columnValues);
			});
			mySQLHolder.bulkExecute();
			mySQLHolder.closeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void executeBulkInsertHashMap(String tableName,ArrayList<HashMap<String, Object>> list)throws ClassNotFoundException, SQLException {
		final MySQLHolder mySQLHolder = new MySQLHolder();
		try {
			mySQLHolder.connect(tableName);
			list.forEach(columnValues -> {
				System.out.print(columnValues);
				mySQLHolder.addRecord(columnValues);
			});
			mySQLHolder.bulkExecute();
			mySQLHolder.closeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
