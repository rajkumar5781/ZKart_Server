package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.JwtUtils;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import connectors.Persistence;
import fetcher.DataFetcher;
import fetcher.XMLDataFetcher;

/**
 * Servlet implementation class LoadAddToCartDetails
 */
public class LoadAddToCartDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadAddToCartDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			
			DataFetcher datafetcher = new Persistence().getDatafetcher();			
			String queryString = "";
			if(datafetcher instanceof XMLDataFetcher) {
				queryString = String.format("/productDetails/Card[customerId='%s']", request.getParameter("customerId"));
			}else {
				queryString = "SELECT * FROM cartDetails where customerId = "+headerValue.get("id")+";";
			}
			JSONArray result =  datafetcher.executeSelectQuery("cartDetails",queryString);
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.getWriter().print(new JSONObject() {{ put("error", e.getMessage()); }});
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
