package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import connectors.Persistence;
import fetcher.CSVFetcher;
import fetcher.DataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loader.ProductDetails;
import utilities.JwtUtils;

/**
 * Servlet implementation class AddressBook
 */
public class AddressBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddressBook() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
			//Get the HeaderValues
			HashMap<String, Object> headerValue = new HashMap<>();
			String authHeader = request.getHeader("Authorization");
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            try {
	            	JwtUtils jwtUtils = new JwtUtils();
	            	headerValue = jwtUtils.getHeaderValues(token);
	            }
	            catch (Exception e) {
					// TODO: handle exception
	            	System.out.println(e.getMessage());
				}
	        }
			
			String selectQuery = "select * from user_address where customerId = "+headerValue.get("id")+" ORDER BY default_address DESC;";
			JSONArray result = datafetcher.executeSelectQuery("user_address", selectQuery);
			
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().print(result);
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		HashMap<String, Object> map = new HashMap<>();
		//Get the HeaderValues
		HashMap<String, Object> headerValue = new HashMap<>();
		String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
            	JwtUtils jwtUtils = new JwtUtils();
            	headerValue = jwtUtils.getHeaderValues(token);
            }
            catch (Exception e) {
				// TODO: handle exception
            	System.out.println(e.getMessage());
			}
        }
        
		if(request.getParameter("actionType").equals("edit")) {
			map.put("id", request.getParameter("actionType"));
		}
		
		try {
			if(request.getParameter("actionType").equals("add")) {
				ProductDetails.addressId++;
				map.put("id", ProductDetails.addressId);
				map.put("name",request.getParameter("name"));
				map.put("customerId",headerValue.get("id"));
				map.put("address", request.getParameter("address"));
				map.put("city", request.getParameter("city"));
				map.put("postalCode", request.getParameter("postalCode"));
				map.put("country", request.getParameter("country"));
				map.put("mobile", request.getParameter("mobile"));
				map.put("state", request.getParameter("state"));
				map.put("default_address", request.getParameter("default"));
				if(request.getParameter("default").equals("true")) {
					changeTheDefaultAddress(headerValue.get("id")+"");
				}
			if(datafetcher.addRecord("user_address", map)) {
				//store the activity in activity log address created
				CSVFetcher csvFetcher = new CSVFetcher();
				ArrayList<String> csvData = new ArrayList<>();
				csvData.add(""+ headerValue.get("id"));
				csvData.add(""+java.time.LocalDate.now().toString());
				csvData.add(""+System.currentTimeMillis());
				csvData.add("Add the new Address.");
				csvData.add("");
			
				csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
				
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("Add the Address successfully...");
			}
			else {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("The Address not added successfully...");
			}
			}
			else if(request.getParameter("actionType").equals("edit")) {
				map.put("name",request.getParameter("name"));
				map.put("customerId",headerValue.get("id"));
				map.put("address", request.getParameter("address"));
				map.put("city", request.getParameter("city"));
				map.put("postalCode", request.getParameter("postalCode"));
				map.put("country", request.getParameter("country"));
				map.put("mobile", request.getParameter("mobile"));
				map.put("state", request.getParameter("state"));
				map.put("default_address", request.getParameter("default"));
				if(request.getParameter("default").equals("true")) {
					changeTheDefaultAddress(headerValue.get("id")+"");
				}
				String queryString = "update user_address set default_address = '"+request.getParameter("default")+"', name = '"+request.getParameter("name")+"', address = '"+request.getParameter("address")+"', city = '"+request.getParameter("city")+"', postalCode = '"+request.getParameter("postalCode")+"', country = '"+request.getParameter("country")+"', mobile = "+request.getParameter("mobile")+", state ='"+request.getParameter("state")+"' where id = "+(Integer.parseInt(request.getParameter("id")))+";";
				datafetcher.executeUpdateQuery("user_address", queryString);
				response.setStatus(HttpServletResponse.SC_OK);
				
				//store the activity in activity log address Update.
				CSVFetcher csvFetcher = new CSVFetcher();
				ArrayList<String> csvData = new ArrayList<>();
				csvData.add(""+ headerValue.get("id"));
				csvData.add(""+java.time.LocalDate.now().toString());
				csvData.add(""+System.currentTimeMillis());
				csvData.add("Update the address.");
				csvData.add("");
			
				csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
			}
			else if(request.getParameter("actionType").equals("set")) {
				String queryString ="update user_address set default_address = '"+true+"' WHERE id = "+request.getParameter("id")+";";
				changeTheDefaultAddress(headerValue.get("id")+"");
				datafetcher.executeUpdateQuery("user_address", queryString);
				
				//store the activity in activity log address Update has default
				CSVFetcher csvFetcher = new CSVFetcher();
				ArrayList<String> csvData = new ArrayList<>();
				csvData.add(""+ headerValue.get("id"));
				csvData.add(""+java.time.LocalDate.now().toString());
				csvData.add(""+System.currentTimeMillis());
				csvData.add("Update the address has default.");
				csvData.add("");
			
				csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
				
				response.setStatus(HttpServletResponse.SC_OK);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
			//Get the HeaderValues
			HashMap<String, Object> headerValue = new HashMap<>();
			String authHeader = request.getHeader("Authorization");
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            try {
	            	JwtUtils jwtUtils = new JwtUtils();
	            	headerValue = jwtUtils.getHeaderValues(token);
	            }
	            catch (Exception e) {
					// TODO: handle exception
	            	System.out.println(e.getMessage());
				}
	        }
	        
			String queryString = "DELETE FROM user_address WHERE id = "+request.getParameter("id")+";";
			datafetcher.executeUpdateQuery("user_address", queryString);
			
			//store the activity in activity log address deleted
			CSVFetcher csvFetcher = new CSVFetcher();
			ArrayList<String> csvData = new ArrayList<>();
			csvData.add(""+ headerValue.get("id"));
			csvData.add(""+java.time.LocalDate.now().toString());
			csvData.add(""+System.currentTimeMillis());
			csvData.add("Delete the address.");
			csvData.add("");
		
			csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
			
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void changeTheDefaultAddress(String userId) throws Exception {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		String selectQuery = "select * from user_address where customerId = "+userId+" and default_address = 'true';";
		JSONArray result = datafetcher.executeSelectQuery("user_address", selectQuery);
		int id = -1;
		if (result != null && result.length() > 0) {
			if(result.getJSONObject(0).has("id") && !result.getJSONObject(0).isNull("id")) {
				id = (int) result.getJSONObject(0).get("id");
			}
		}
		if(id!=-1) {
		String default_addressString = "UPDATE user_address SET default_address = 'false' WHERE id = "+id+";";
		datafetcher.executeUpdateQuery("user_address", default_addressString);
		}
	}
	
}
