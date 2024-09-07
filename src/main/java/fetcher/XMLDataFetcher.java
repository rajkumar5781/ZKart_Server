package fetcher;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class XMLDataFetcher implements DataFetcher {

	@Override
	public boolean addRecord(String tableName, HashMap<String, Object> columnValues) throws Exception {
		DocumentHolder documentHolder = new DocumentHolder();
		documentHolder.connect(tableName);
		documentHolder.addRecord(columnValues);
		return documentHolder.updateDocument();
	}

	@Override
	public void addRecordsinBulk(String tableName, ArrayList<HashMap<String, Object>> bulkColumnValues)
			throws Exception {
		DocumentHolder documentHolder = new DocumentHolder();
		documentHolder.connect(tableName);
		bulkColumnValues.forEach(columnValues -> {
			documentHolder.addRecord(columnValues);
		});
		documentHolder.updateDocument();
	}

	@Override
	public JSONObject countRecords(String tableName, String query) throws Exception {
		DocumentHolder documentHolder = new DocumentHolder();
		documentHolder.connect(tableName);

		return new JSONObject() {{put("count", documentHolder.totalRecordCount(query));}};
	}

	public boolean validRecord(String tableName) throws Exception {
		DocumentHolder documentHolder = new DocumentHolder();
		documentHolder.connect(tableName);

		return false;
	}

	public String selectQuery(String tableName, String[] columns, String[] subqueries) {
		return null;
	}

	public String insertQuery(String tableName, String[] columns, String[] values) {
		return null;
	}

	public String updateQuery(String tableName, String[] columns, String[] values, String[] conditions) {
		return null;
	}

	public String deleteQuery(String tableName, String[] conditions) {
		return null;
	}

	public void executeQuery(String query) {

	}

	public void executeUpdateQuery(String query,String updateQuery) {

	}

	public void executeDeleteQuery(String query) {

	}

	@Override
	public JSONArray executeSelectQuery(String tableName, String query) throws Exception {
		DocumentHolder documentHolder = new DocumentHolder();
		documentHolder.connect(tableName);
		return documentHolder.jsonValues(query);
	}

	public void executeBulkUpdateStingQuery(String tableName,ArrayList<String> querys) {
		
		
	}
}
