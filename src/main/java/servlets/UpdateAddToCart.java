package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loader.ProductDetails;
import utilities.JwtUtils;

import java.io.IOException;
import java.util.HashMap;

import connectors.Persistence;
import fetcher.CassandraDataFetcher;
import fetcher.DataFetcher;
import fetcher.MySQLDataFetcher;
import fetcher.XMLDataFetcher;

/**
 * Servlet implementation class UpdateAddToCart
 */
public class UpdateAddToCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateAddToCart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	        
			String actionTypeString = request.getParameter("actionType"),queryString = "";
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			if(actionTypeString.equals("update")) {
				int count = Integer.parseInt(request.getParameter("count"));
				queryString = "update cartdetails set productCount = "+count+" where id = "+request.getParameter("id")+";";
			}
			else if(actionTypeString.equals("delete")) {
				queryString = "DELETE FROM cartdetails WHERE id = "+request.getParameter("id")+";";
			}
			System.out.println(queryString);
			if (datafetcher instanceof MySQLDataFetcher) {
				datafetcher.executeUpdateQuery("cartdetails", queryString);
				
				//store the activity in activity log
				CassandraDataFetcher connection = new CassandraDataFetcher();				
				HashMap<String, Object> insertValueMap = new HashMap<>();
				
				insertValueMap.put("userid",headerValue.get("id"));
				insertValueMap.put("date", java.time.LocalDate.now().toString());
				insertValueMap.put("time", System.currentTimeMillis());
				insertValueMap.put("details", actionTypeString+" the addtocart.");
				insertValueMap.put("subject", "");
			
				connection.addValuesToActivityLog(insertValueMap);
				
				response.setStatus(HttpServletResponse.SC_OK);
			}
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
			
	}

}
