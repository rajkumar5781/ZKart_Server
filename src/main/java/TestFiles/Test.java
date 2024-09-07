package TestFiles;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import fetcher.DataFetcher;
//import fetcher.MySQLDataFetcher;
import fetcher.XMLDataFetcher;
import connectors.*;

public class Test {
	public static void main(String args[]) throws Exception {
		DataFetcher dataFetcher = new XMLDataFetcher();
//		ArrayList<HashMap<String, Object>> list = new ArrayList<>();
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("id", 2);
//		map.put("name", "shobana");
//		map.put("age", 11);
//		
//		list.add(map);
//		map = new HashMap<>();
//		map.put("id", 3);
//		map.put("name", "Amirthini");
//		map.put("age", 2);
//		list.add(map);
//		
//		dataFetcher.addRecordsinBulk("TEST1", list);
//		JSONArray jsonArray = dataFetcher.executeSelectQuery("TEST1",null,null,null,null,null,"testing/test");
//		for(int i=0;i<jsonArray.length();i++) {
//			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//			System.out.print(jsonObject.get("id")+"--->"+"dfgshbknskjn");
//		}
		
		final CassandraConnection client = new CassandraConnection();
		   final String ipAddress = "127.0.0.1";
		   final int port = args.length > 1 ? Integer.parseInt(args[1]) : 9042;
		   System.out.println("Connecting to IP Address " + ipAddress + ":" + port + "...");
//		   client.connectdb(ipAddress, port);
		   client.connect();
		   client.close();
	}
}
