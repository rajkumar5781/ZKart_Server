package fetcher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DataFetcher {
	//public void addRecord(String tableName,String[] columnNames,Object[] columnValues) throws Exception;
	public boolean addRecord(String tableName,HashMap<String, Object> columnValues) throws Exception;
	//public void addRecordsinBulk(String tableName,String[] columnNames,ArrayList<Object[]> bulkColumnValues) throws Exception;
	public void addRecordsinBulk(String tableName, ArrayList<HashMap<String, Object>> bulkColumnValues)throws Exception;
	
	public JSONObject countRecords(String tableName,String query)throws Exception;
	
	public boolean validRecord(String tableName)throws Exception;
	
	public String selectQuery(String tableName, String[] columns, String[] subqueries);
	
	public String insertQuery(String tableName, String[] columns, String[] values);
	
	public String updateQuery(String tableName, String[] columns, String[] values, String[] conditions);
	
	public String deleteQuery(String tableName, String[] conditions);
	
	public void executeQuery(String query);
	
	public JSONArray executeSelectQuery(String tableName,String query)throws Exception;
	
	//public JSONArray executeSelectQuery(String tableName,String query,String[] columnNames, Object[] columnValues, String[] conditions,String[] operators,String xMLQuery) throws Exception;
	
	public void executeUpdateQuery(String query,String updateQuery) throws Exception;
	
	public void executeDeleteQuery(String query);
	
	public void executeBulkUpdateStingQuery(String query,ArrayList<String> querys) throws Exception;
	
}
