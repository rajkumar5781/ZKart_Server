package migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import connectors.MySQLConnector;
import constants.XMLFileConstants.XMLDeclaration;
import fetcher.DataFetcher;
import fetcher.MySQLDataFetcher;
import fetcher.XMLDataFetcher;
import utilities.MySQLUtilitys;

public class MigrationXLMToSQL {

	private static ArrayList<String> xmlTagName = new ArrayList<>();

	private HashMap<String, String> clHashMap = new HashMap<>() {
		{
			put("", "");
		}
	};

	private static HashMap<String, HashMap<String, String>> XMLTagsAndDataTypes = new HashMap<>();

	private static ArrayList<String> addColumnArrayList = new ArrayList<>();

	private static ArrayList<String> removeColumnArrayList = new ArrayList<>();

	private static String addColumnsString = "";

	private static String removeColumnString = "";

	static {
		XMLTagsAndDataTypes.put("productDetails", new HashMap<>() {
			{
				put("id", "int");
				put("Name", "varchar(100)");
				put("Description", "varchar(2000)");
				put("Category", "varchar(400)");
				put("Actual_price", "int");
				put("Discounts", "int");
				put("Likes", "int");
				put("Available_count", "int");
				put("Product_image", "varchar(200)");
				put("Star", "int");
			}
		});

		XMLTagsAndDataTypes.put("users", new HashMap<>() {
			{
				put("userId", "int");
				put("lastName", "varchar(100)");
				put("password", "varchar(100)");
				put("phone", "int");
				put("name", "varchar(200)");
				put("userName", "varchar(100)");
			}
		});
	}

	public static void migration() throws Exception {
		LinkedHashMap<String, ArrayList<String>> tableMap = new LinkedHashMap<>();
		tableMap.put("users", new ArrayList<>() {
			{
				add("userName");
				add("password");
			}
		});
		tableMap.put("productDetails", new ArrayList<>() {
			{
				add("Name");
				add("Category");
			}
		});
		HashMap<String, ArrayList<String>> tableMetaData = new HashMap<>();
		tableMetaData.put("users", new ArrayList<>() {
			{
				add("userName");
				add("password");
			}
		});
		tableMetaData.put("productDetails", new ArrayList<>() {
			{
				add("Name");
				add("Category");
			}
		});
		HashMap<String, ArrayList<String>> validateConditionOperaterHashMap = new HashMap<>();
		validateConditionOperaterHashMap.put("users", new ArrayList<>() {
			{
				add("AND");
			}
		});
		validateConditionOperaterHashMap.put("productDetails", new ArrayList<>() {
			{
				add("AND");
			}
		});
		HashMap<String, ArrayList<String>> tablePrimaryKeyHashMap = new HashMap<>();
		tablePrimaryKeyHashMap.put("users", new ArrayList<>() {
			{
				add("userId");
			}
		});
		tablePrimaryKeyHashMap.put("productDetails", new ArrayList<>() {
			{
				add("id");
			}
		});

		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "Yoga@11ark");
		Statement statement = con.createStatement();

		DataFetcher xmlDataFetcher = new XMLDataFetcher();

		DataFetcher sqlDataFetcher = new MySQLDataFetcher();
		XMLDeclaration xmlDelaration = null;

		for (Map.Entry cEntry : tableMap.entrySet()) {
			ArrayList<String> queryList = new ArrayList<>();
			xmlDelaration = XMLDeclaration.valueOf((String) cEntry.getKey());
			HashMap<String, String> tableHeaderDetails = new HashMap<>();
			loadHeaderDetails((String) cEntry.getKey(), statement, tableHeaderDetails);

			// load the all available xmltag names
			xmlTagName = utilities.XMLUtil.getXMLTagNames(xmlDelaration);

			// check missing column will add or remove
			validate_update_columns(xmlDelaration, statement, tableHeaderDetails);

			// fetch the data to the xml file data
			JSONArray xmlArray = xmlDataFetcher.executeSelectQuery((String) cEntry.getKey(),
					XMLSelectQuery(xmlDelaration));

			for (int index = 0; index < xmlArray.length(); index++) {
				JSONObject jsonObject = xmlArray.getJSONObject(index);

				// take a column check based on condition
				HashMap<String, Object> metaKeyHashMap = new HashMap<>();
				ArrayList<String> primaryKeysList = tablePrimaryKeyHashMap.get(cEntry.getKey());
				tableMetaData.get(cEntry.getKey()).forEach((key) -> {
					metaKeyHashMap.put(key, jsonObject.get(key));
				});

				// XML file one row data
				HashMap<String, Object> columnValueHashMap = new HashMap<>();
				jsonObject.keySet().forEach((key) -> {
					columnValueHashMap.put(key, jsonObject.get(key));
				});

				// check the XML data are present or not in table
				String sqlQueryString = "SELECT * FROM " + cEntry.getKey() + " where " + getWhereString(metaKeyHashMap,
						validateConditionOperaterHashMap.get(cEntry.getKey()), tableHeaderDetails) + ";";
				JSONArray sqlJsonArray = null;
				try {
					sqlJsonArray = sqlDataFetcher.executeSelectQuery((String) cEntry.getKey(), sqlQueryString);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// insert query implementation
				if (sqlJsonArray == null) {
					try {

						// fetch insert query
						String insertQueryString = MySQLUtilitys.getInsertQuery(
								MySQLUtilitys.getColumnNames(columnValueHashMap),
								MySQLUtilitys.getInsertValueString(columnValueHashMap, tableHeaderDetails),
								(String) cEntry.getKey());
						statement.addBatch(insertQueryString);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				// update query implementation
				else {
					JSONObject jsonObject1 = (JSONObject) sqlJsonArray.get(0);
					HashMap<String, Object> conditionValuesHashMap = new HashMap<>();

					// implement update query to update the table primary key
					jsonObject1.keySet().forEach((key) -> {
						if (primaryKeysList.contains(key)) {
							conditionValuesHashMap.put(key, jsonObject1.get(key));
						}
					});

					// fetch update query
					String updateQueryString = MySQLUtilitys.getUpdateQuery((String) cEntry.getKey(),
							MySQLUtilitys.getUpdateValueString(columnValueHashMap, tableHeaderDetails),
							getWhereString(conditionValuesHashMap, null, tableHeaderDetails));
					try {
						statement.addBatch(updateQueryString);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			int[] updateCounts = statement.executeBatch();
			System.out.println("Batch executed successfully.");
		}
		if (con != null) {
			con.close(); // Close the connection
			System.out.println("Connection closed.");
		}
	}

	public static ResultSet getResultSet(Connection connection, XMLDeclaration xmlDelaration) throws SQLException {
		return connection.getMetaData().getColumns(null, null, xmlDelaration.getXmlName(), null);
	}

	public static void loadHeaderDetails(String table_name, Statement statement,
			HashMap<String, String> tableHeaderDetails) throws SQLException {
		String meta_data_queryString = "SELECT COLUMN_NAME,DATA_TYPE from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA=\"ecommerce\" and TABLE_NAME=\""
				+ table_name + "\";";
		ResultSet resultSet = statement.executeQuery(meta_data_queryString);
		while (resultSet.next()) {
			tableHeaderDetails.put(resultSet.getString("COLUMN_NAME"), resultSet.getString("DATA_TYPE"));
		}
	}

	public static String XMLSelectQuery(XMLDeclaration xmlDelaration) {
		return "/" + xmlDelaration.getXmlName() + "/" + xmlDelaration.getBaseNodee();
	}

	public static String getWhereString(HashMap<String, Object> columnValues,
			ArrayList<String> validateConditionOperaterHashMap, HashMap<String, String> tableHeaderDetails) {
		String columnValue = "";
		int count = 0;

		for (Map.Entry columnEntry : columnValues.entrySet()) {
			String columnNameValue = (String) columnEntry.getKey();
			String dataType = tableHeaderDetails.getOrDefault(columnNameValue, null);
			if (dataType != null) {
				if (dataType.equalsIgnoreCase("INT") || dataType.equalsIgnoreCase("DECIMAL")
						|| dataType.equalsIgnoreCase("BOOLEAN") || dataType.equalsIgnoreCase("BIGINT")) {
					if (count > 0) {
						String conditionOperatorString = "AND";
						if (validateConditionOperaterHashMap != null && validateConditionOperaterHashMap.size() > 0
								&& validateConditionOperaterHashMap.get(count - 1) != null) {
							conditionOperatorString = validateConditionOperaterHashMap.get(count - 1);
						}

						columnValue = columnValue + " " + conditionOperatorString + " ";
					}
					columnValue = columnValue + columnNameValue + " = " + columnEntry.getValue();
					count++;
				} else if (dataType.equalsIgnoreCase("DATE") || dataType.equalsIgnoreCase("TIME")
						|| dataType.equalsIgnoreCase("DATETIME") || dataType.equalsIgnoreCase("TIMESTAMP")
						|| dataType.equalsIgnoreCase("VARCHAR")) {
					if (count > 0) {
						String conditionOperatorString = "AND";
						if (validateConditionOperaterHashMap != null && validateConditionOperaterHashMap.size() > 0
								&& validateConditionOperaterHashMap.get(count - 1) != null) {
							conditionOperatorString = validateConditionOperaterHashMap.get(count - 1);
						}
						columnValue = columnValue + " " + conditionOperatorString + " ";
					}
					columnValue = columnValue + columnNameValue + " = " + "'" + columnEntry.getValue() + "'";
					count++;
				}
			}
		}
		return columnValue;
	}

	private static void validate_update_columns(XMLDeclaration xmlDelaration, Statement statement,
			HashMap<String, String> tableHeaderDetails) throws SQLException {

		int count = 0;
		for (Map.Entry xmlColumnEntry : XMLTagsAndDataTypes.get(xmlDelaration.getXmlName()).entrySet()) {
			if (tableHeaderDetails.getOrDefault(xmlColumnEntry.getKey(), null) == null) {
				if (count > 0) {
					addColumnsString = addColumnsString + ",";
				}
				addColumnsString = addColumnsString + "ADD COLUMN  " + xmlColumnEntry.getKey() + " "
						+ XMLTagsAndDataTypes.get(xmlDelaration.getXmlName()).get(xmlColumnEntry.getKey());
				count++;
			}
		}
		if (addColumnsString.length() > 0) {
			addColumnsString = "ALTER TABLE " + xmlDelaration.getXmlName() + " " + addColumnsString + ";";
		}
		count = 0;
		for (Map.Entry sqlColumnEntry : tableHeaderDetails.entrySet()) {
			if (XMLTagsAndDataTypes.get(xmlDelaration.getXmlName()).getOrDefault(sqlColumnEntry.getKey(),
					null) == null) {
				System.out.println(sqlColumnEntry.getValue() + "sqlColumnEntry.getKey()" + "--->");
				if (count > 0) {
					removeColumnString = removeColumnString + ",";
				}
				removeColumnString = removeColumnString + "DROP COLUMN " + sqlColumnEntry.getKey();
				count++;
			}
		}
		if (removeColumnString.length() > 0) {
			removeColumnString = "ALTER TABLE " + xmlDelaration.getXmlName() + " " + removeColumnString + ";";
		}
		if (addColumnsString.length() > 0) {
			statement.addBatch(addColumnsString);
		}
		if (removeColumnString.length() > 0) {
			statement.addBatch(removeColumnString);
		}
		int[] updateCounts = statement.executeBatch();
	}
}
