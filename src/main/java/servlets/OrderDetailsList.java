package servlets;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import connectors.Persistence;
import fetcher.DataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.JwtUtils;

/**
 * Servlet implementation class OrderDetailsList
 */
public class OrderDetailsList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderDetailsList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
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
			
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			String selectQuery = "SELECT id as orderid from orderdetails where orderstatus ='process' and customerId = "+headerValue.get("id")+";";
			JSONArray result = datafetcher.executeSelectQuery("orderprocess", selectQuery);
			
			for (int i = 0; i < result.length(); i++) {  
				
	            // store each object in JSONObject  
	            JSONObject explrObject = result.getJSONObject(i);
	            
	            selectQuery = "SELECT * from orderprocess where orderid = "+explrObject.get("orderid")+";";
	            JSONArray orderProcess = datafetcher.executeSelectQuery("orderedproductdetails", selectQuery);
	            
	            explrObject.put("process", orderProcess.getJSONObject(0));
	            
	            selectQuery = "SELECT * from orderedproductdetails where orderid="+explrObject.get("orderid")+";";
	            JSONArray result1 = datafetcher.executeSelectQuery("orderedproductdetails", selectQuery);
	            
	            explrObject.put("products", result1);
	            
	            JSONObject fromAddress = new JSONObject();
	            fromAddress.put("Name", "ZKart");
	            fromAddress.put("Address", "no:111,dubai,chennai");
	            fromAddress.put("postal", 111111);
	            
	            explrObject.put("fromAddress", fromAddress);
	            
	           String addressQuery = "SELECT user_address.default_address,user_address.address,user_address.city,user_address.state,postalCode,user_address.country,user_address.mobile FROM orderdetails INNER JOIN user_address on user_address.id = orderdetails.addressId where orderdetails.id = "+explrObject.get("orderid")+";";
	           JSONArray result2 = datafetcher.executeSelectQuery("orderdetails", addressQuery);
	           
	           explrObject.put("address", result2.get(0));
	        }   
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		}
		catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
