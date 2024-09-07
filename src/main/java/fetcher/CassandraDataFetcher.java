package fetcher;

import java.util.HashMap;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import connectors.CassandraConnection;

public class CassandraDataFetcher {
	
	private CassandraConnection connection = new CassandraConnection();
	
	public CassandraDataFetcher(){
		this.connection.connect();
	}
	
	public Session getSession() {
		return this.connection.getSession();
	}
	
	public CassandraConnection getConnection() {
		return this.connection;
	}
	
	public void addValuesToActivityLog(HashMap<String, Object> insertValueMap) {
		try {
		String activityLogQuery = "insert into activity_log (userid,date,time,details,subject) values (?,?,?,?,?)";
		PreparedStatement psInsert = this.connection.getSession().prepare(activityLogQuery);
		BoundStatement bsInsert = psInsert.bind(insertValueMap.getOrDefault("userid", null),insertValueMap.getOrDefault("date", null),insertValueMap.getOrDefault("time", null),insertValueMap.getOrDefault("details", null),insertValueMap.getOrDefault("subject", null));
		this.connection.getSession().execute(bsInsert);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
//	public void executeQuery(String statement) {
//		try {
//			connection.getSession().execute(statement);
//			connection.close();
//		}
//		catch (Exception e) {
//			
//		}
//	}
//	public static ResultSet selectQuery(String statement) {
//		try {
//			CassandraConnection connection = new CassandraConnection();
//			connection.connect();
//			ResultSet rs = connection.getSession().execute(statement);
//			connection.close();
//			return rs;
//		}
//		catch (Exception e) {
//			
//		}
//		return null;
//	}
}
