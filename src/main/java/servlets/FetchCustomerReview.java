package servlets;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import fetcher.CassandraDataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.CassandraUtils;
import utilities.JwtUtils;

/**
 * Servlet implementation class FetchCustomerReview
 */
public class FetchCustomerReview extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchCustomerReview() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	            	System.out.println(e.getMessage());
				}
	        }
	        JSONArray result = null;
	        
	        //fetch the customer single reviews
			if(request.getParameter("actionType")!=null && request.getParameter("actionType").equals("single")) {
				 String reviewQuery = String.format("SELECT * FROM reviewdetails WHERE productid = %d AND id = %d", Integer.parseInt(request.getParameter("productid")), Integer.parseInt(request.getParameter("id")));
				CassandraDataFetcher cassandraDataFetcher = new CassandraDataFetcher();
				result = CassandraUtils.resultSetToJSON(cassandraDataFetcher.getSession().execute(reviewQuery));
			}
			//fetch the customer all reviews
			else {
				String reviewQuery = "select * from reviewdetails where customerid = ?";
				CassandraDataFetcher cassandraDataFetcher = new CassandraDataFetcher();
				int id = (int) headerValue.get("id");
				System.out.println(id);
				result = CassandraUtils.resultSetToJSON(cassandraDataFetcher.getSession().execute(reviewQuery,id));
			}
			response.getWriter().print(result);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			response.getWriter().print("Error occur");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
