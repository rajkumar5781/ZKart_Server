package connectors;

import constants.Constants;
import fetcher.DataFetcher;
import fetcher.MySQLDataFetcher;
import fetcher.XMLDataFetcher;

public class Persistence {
	private DataFetcher datafetcher = null;
	public Persistence() {
		if(Constants.CURRENT_DATA_RETREIVAL_MODE.equals("XML")) {
			datafetcher = new XMLDataFetcher();
		}
		else {
			datafetcher = new MySQLDataFetcher();
		}
	}
	public DataFetcher getDatafetcher() {
		return datafetcher;
	}
}
