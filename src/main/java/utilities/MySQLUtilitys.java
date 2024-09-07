package utilities;

import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import constants.Constants;
import jakarta.servlet.http.HttpServletRequest;

public class MySQLUtilitys {

	public static String getInsertValueString(HashMap<String, Object> columnValues,
			HashMap<String, String> tableHeaderDetails) {
		String columnValue = "";
		int count = 0;

		for (Map.Entry columnEntry : columnValues.entrySet()) {
			String columnNameValue = (String) columnEntry.getKey();
			String dataType = tableHeaderDetails.getOrDefault(columnNameValue, null);

			if (dataType != null) {
				if (dataType.equalsIgnoreCase("INT") || dataType.equalsIgnoreCase("DECIMAL") || dataType.equalsIgnoreCase("BOOLEAN")
						|| dataType.equalsIgnoreCase("BIGINT")) {
					if (count > 0) {
						columnValue = columnValue + ",";
					} else {

					}
					Object valueObject = columnEntry.getValue();
					if (columnEntry.getValue()==null || columnEntry.getValue().equals("")) {
						valueObject = 0;
					}
					columnValue = columnValue + valueObject;
					count++;
				} else if (dataType.equalsIgnoreCase("DATE") || dataType.equalsIgnoreCase("TIME") || dataType.equalsIgnoreCase("DATETIME")
						|| dataType.equalsIgnoreCase("TIMESTAMP") || dataType.equalsIgnoreCase("VARCHAR")) {
					if (count > 0) {
						columnValue = columnValue + ",";
					}
					Object valueObject = columnEntry.getValue();
					if (columnEntry.getValue()==null || columnEntry.getValue().equals("")) {
						valueObject = 0;
					}
					columnValue = columnValue + "'" + valueObject + "'";

					count++;
				}
			}
		}
		return columnValue;
	}

	public static String getColumnNames(HashMap<String, Object> columnValues) {
		return String.join(",", columnValues.keySet());
	}

	public static String getInsertQuery(String columnNames, String columnValues, String tableName) {
		return "insert into " + tableName + " (" + columnNames + ") values (" + columnValues + ");";
	}
	
	public static String getUpdateQuery(String tableName, String columnValues, String conditionString) {
		return "update "+tableName+" set "+columnValues+" where "+conditionString+";";
	}

	public static String getUpdateValueString(HashMap<String, Object> columnValues,
			HashMap<String, String> tableHeaderDetails) {
		String columnValue = "";
		int count = 0;

		for (Map.Entry columnEntry : columnValues.entrySet()) {
			String columnNameValue = (String) columnEntry.getKey();
			String dataType = tableHeaderDetails.getOrDefault(columnNameValue, null);
			if (dataType != null) {
				if (dataType.equalsIgnoreCase("INT") || dataType.equalsIgnoreCase("DECIMAL") || dataType.equalsIgnoreCase("BOOLEAN")) {
					if (count > 0) {
						columnValue = columnValue + ",";
					}
					Object valueObject = columnEntry.getValue();
					if (columnEntry.getValue().equals("")) {
						valueObject = 0;
					}
					columnValue = columnValue + columnNameValue + " = " + valueObject;
					count++;
				} else if (dataType.equalsIgnoreCase("DATE") || dataType.equalsIgnoreCase("TIME") || dataType.equalsIgnoreCase("DATETIME")
						|| dataType.equalsIgnoreCase("TIMESTAMP") || dataType.equalsIgnoreCase("VARCHAR")) {
					if (count > 0) {
						columnValue = columnValue + ",";
					}
					Object valueObject = columnEntry.getValue();
					if (columnEntry.getValue().equals("")) {
						valueObject = 0;
					}
					columnValue = columnValue + columnNameValue + " = " + "'" + valueObject + "'";

					count++;
				}
			}
		}
		return columnValue;
	}
	
	public static String getWhereSQLQueryString(HttpServletRequest request) {
		String queryString = "";
		for(Map.Entry rEntry : request.getParameterMap().entrySet()) {
			if(!((String)rEntry.getKey()).equals("searchWord") && !((String)rEntry.getKey()).equals("pageNumber")) {
				
				if(Constants.COLUMN_UI_TYPE.get((String)rEntry.getKey()).equals("range")) {
					 List<String> stringList = Arrays.asList(((String[]) rEntry.getValue())[0].split(","));
				        List<Integer> values = stringList.stream()
				                                          .map(Integer::parseInt)
				                                          .collect(Collectors.toList());
					if(values.size()>1) {
						if(queryString.length()>1) {
							queryString=queryString+" and "+" ";
						}
					queryString=queryString+rEntry.getKey() + " >= "+ values.get(0) +" "+" and "+" "+rEntry.getKey()+" <= "+values.get(1);
					}
				}
				else {
					if(queryString.length()>1) {
						queryString=queryString+" and "+" ";
					}
					queryString=queryString+rEntry.getKey() + "  = ";
					if(Constants.COLUMN_DATATYPE.get(rEntry.getKey()).equals("varchar")) {
						queryString=queryString+"'"+((String[]) rEntry.getValue())[0] +"'";
					}
					else {
						queryString=queryString+((String[]) rEntry.getValue())[0];
					}
				}
			}
		}
		return queryString;
	}

}
