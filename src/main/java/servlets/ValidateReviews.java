package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.CassandraUtils;
import utilities.JwtUtils;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import connectors.Persistence;
import fetcher.CassandraDataFetcher;
import fetcher.DataFetcher;

/**
 * Servlet implementation class ValidateReviews
 */
public class ValidateReviews extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ValidateReviews() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		
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
		
		
		String reviewDetailsSelectQuery = "select * from reviewdetails where productId = "+request.getParameter("productId")+" and customerId = "+headerValue.get("id")+";";
		String queryString = "SELECT * FROM orderDetails inner join orderedProductDetails on (orderdetails.id = orderedProductDetails.orderid and orderedProductDetails.productid = "+request.getParameter("productId")+") where customerId = "+headerValue.get("id")+";";
		try {
			System.out.println(reviewDetailsSelectQuery+"--->"+queryString);
			JSONArray resultArray = datafetcher.executeSelectQuery("reviewDetails",reviewDetailsSelectQuery);
			
			CassandraDataFetcher cassandraDataFetcher = new CassandraDataFetcher();
			resultArray = CassandraUtils.resultSetToJSON(cassandraDataFetcher.getSession().execute(reviewDetailsSelectQuery));
			
			JSONArray orderResultArray = datafetcher.executeSelectQuery("orderdetails", queryString);
			JSONObject object = new JSONObject();
			object.put("buy", false);
			object.put("review", false);
			if(resultArray!=null && (!resultArray.isEmpty())) {
				object.put("review", true);
				if (resultArray != null && resultArray.length() > 0) {
					if(resultArray.getJSONObject(0).has("id") && !resultArray.getJSONObject(0).isNull("id")) {
						object.put("reviewId",resultArray.getJSONObject(0).get("id").toString());
					}
				}
			}
			if(orderResultArray!=null && !orderResultArray.isEmpty()) {
				object.put("buy", true);
			}

			response.getWriter().print(object);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
