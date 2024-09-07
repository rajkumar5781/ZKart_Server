package constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constants {
	public static final String ROOT_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\ROOT\\WEB-INF\\";
	public static final String XML_ROOT_PATH = ROOT_PATH;
	public static final String IMAGES_ROOT_PATH = ROOT_PATH+"images";
	public static String CURRENT_DATA_RETREIVAL_MODE = "SQL"; //XML or SQL
	public static final String RUN_DEV = "http://localhost:8080";
	public static final String ACTIVITY_LOG_FILE_NAME_STRING = "ActivityLog";
	public static final HashMap<String, String>  COLUMN_DV = new HashMap<String, String>();
	static {
		COLUMN_DV.put("Category", "Category");
		COLUMN_DV.put("Actual_price", "Price");
		COLUMN_DV.put("Star", "Ratings");
	}
	public static final HashMap<String, String>  COLUMN_UI_TYPE = new HashMap<String, String>();
	static {
		COLUMN_UI_TYPE.put("Category", "dropdown");
		COLUMN_UI_TYPE.put("Actual_price", "range");
		COLUMN_UI_TYPE.put("Star", "dropdown");
	}
	public static final HashMap<String, List<String>>  COLUMN_OPTIONS = new HashMap<String, List<String>>();
	static {
		COLUMN_OPTIONS.put("Category", new ArrayList<String>(){{ add("Furnitures"); add("Electronics");  add("Fashion"); add("Mobile Accessories"); add("Food"); }});
		COLUMN_OPTIONS.put("Star", new ArrayList<String>(){{ add("5"); add("4");  add("3");;  add("2");;  add("1"); }});
	}
	public static final HashMap<String, String>  COLUMN_DATATYPE = new HashMap<>();
	static {
		COLUMN_DATATYPE.put("Category", "varchar");
		COLUMN_DATATYPE.put("Actual_price", "int");
		COLUMN_DATATYPE.put("Star", "int");
	}
	public static final HashMap<String, String>  MODULE_TABLE = new HashMap<String, String>();
	static {
		MODULE_TABLE.put("Products", "productDetails");
	}
}