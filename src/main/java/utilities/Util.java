package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

public class Util {

	 public static Map<String, String> parseQueryString(HttpServletRequest request) throws IOException {
		 String body = new BufferedReader(new InputStreamReader(request.getInputStream()))
                 .lines()
                 .collect(Collectors.joining("\n"));
		 
	        Map<String, String> params = new HashMap<>();
	        String[] pairs = body.split("&");
	        for (String pair : pairs) {
	            String[] keyValue = pair.split("=");
	            if (keyValue.length == 2) {
	                params.put(keyValue[0], keyValue[1]);
	            }
	        }
	        return params;
	    }
	
}
