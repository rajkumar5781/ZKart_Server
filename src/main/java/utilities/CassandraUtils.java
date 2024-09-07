package utilities;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class CassandraUtils {

	public static JSONArray resultSetToJSON(ResultSet resultSet) {
		JSONArray jsonArray = new JSONArray();
		try {
			



//			while (resultSet.next()) {
//				JSONObject jsonRow = new JSONObject();
//				for (int i = 1; i <= columnCount; i++) {
//					String columnName = metaData.getColumnLabel(i);
//					Object value = resultSet.getObject(i);
//					
//					jsonRow.put(columnName, value);
//				}
//				jsonArray.put(jsonRow);
//			}
		
			for (Row row : resultSet) {
	            JSONObject jsonObject = new JSONObject();

	            ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
	            for (ColumnDefinitions.Definition definition : columnDefinitions) {
	                String columnName = definition.getName();
	                
	                Object columnValue = row.getObject(columnName);

	                jsonObject.put(columnName, columnValue);
	            }
	            jsonArray.put(jsonObject);
	        }
			
			
			return jsonArray;
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return jsonArray;
	}
	
}
