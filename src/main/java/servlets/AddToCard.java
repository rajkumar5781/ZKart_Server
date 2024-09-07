package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import connectors.Persistence;
import fetcher.CSVFetcher;
import fetcher.CassandraDataFetcher;
import fetcher.DataFetcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loader.ProductDetails;
import utilities.JwtUtils;

/**
 * Servlet implementation class AddToCard
 */
public class AddToCard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToCard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		ServletContext servletContext1 = getServletContext();
		System.out.println(servletContext1.getContextPath().toString());
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
			
			HashMap<String, Object> map = new HashMap<>();
			ProductDetails.addToCartId++;
			map.put("id", ProductDetails.addToCartId);
			map.put("customerId",headerValue.get("id"));
			map.put("productId", request.getParameter("productId"));
			map.put("productName", request.getParameter("product"));
			map.put("productCount", request.getParameter("productCount"));
			map.put("productPrice", request.getParameter("productPrice"));
			map.put("productImage", request.getParameter("image"));
			
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			try {
				String selectString = "select * from cartdetails where customerId = "+headerValue.get("id")+" and productId = "+request.getParameter("productId")+";";
				JSONArray resultArray = datafetcher.executeSelectQuery("cartdetails", selectString);
				if(resultArray.isEmpty()) {
					if(datafetcher.addRecord("cartDetails", map)) {
						//store the activity in activity log
						CassandraDataFetcher connection = new CassandraDataFetcher();				
						HashMap<String, Object> insertValueMap = new HashMap<>();
						
						insertValueMap.put("userid", headerValue.get("id"));
						insertValueMap.put("date", java.time.LocalDate.now().toString());
						insertValueMap.put("time", System.currentTimeMillis());
						insertValueMap.put("details", "The product add addtocart.");
						insertValueMap.put("subject", map.toString());
					
						connection.addValuesToActivityLog(insertValueMap);
						
						//store the activity in activity log
						CSVFetcher csvFetcher = new CSVFetcher();
						ArrayList<String> csvData = new ArrayList<>();
						csvData.add(""+ headerValue.get("id"));
						csvData.add(""+java.time.LocalDate.now().toString());
						csvData.add(""+System.currentTimeMillis());
						csvData.add("The product add addtocart.");
						csvData.add("");
					
						csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
						
						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter().print("The product add addtocart successfully...");
					}
					else {
						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter().print("The product not add addtocart successfully...");
					}
				}
				else {
					int productCount = (int) resultArray.getJSONObject(0).get("productCount");
					int addCount = Integer.parseInt(request.getParameter("productCount"));
					int totalCount = productCount+addCount;
					String queryString = "update cartdetails set productCount = "+totalCount+" where id = "+resultArray.getJSONObject(0).get("id")+";";
					datafetcher.executeUpdateQuery("cartdetails", queryString);
					
					//store the activity in activity log
					CassandraDataFetcher connection = new CassandraDataFetcher();				
					HashMap<String, Object> insertValueMap = new HashMap<>();
					
					insertValueMap.put("userid", headerValue.get("id"));
					insertValueMap.put("date", java.time.LocalDate.now().toString());
					insertValueMap.put("time", System.currentTimeMillis());
					insertValueMap.put("details", "update the addtocart.");
					insertValueMap.put("subject", "");
				
					connection.addValuesToActivityLog(insertValueMap);
					
					//store the activity in activity log
					CSVFetcher csvFetcher = new CSVFetcher();
					ArrayList<String> csvData = new ArrayList<>();
					csvData.add(""+ headerValue.get("id"));
					csvData.add(""+java.time.LocalDate.now().toString());
					csvData.add(""+System.currentTimeMillis());
					csvData.add("update the addtocart.");
					csvData.add("");
				
					csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
					
					response.getWriter().print("The update addtocart successfully...");
					response.setStatus(HttpServletResponse.SC_OK);
				}
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
				e.printStackTrace();
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}	 
	}

}
