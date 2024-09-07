package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import connectors.Persistence;
import fetcher.DataFetcher;

/**
 * Servlet implementation class ProfitOfTheDayChart
 */
public class ProfitOfTheDayChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfitOfTheDayChart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();			
		String queryString = "SELECT orderDate,SUM(totalAmount) AS totalAmountSum FROM (SELECT DATE(orderDateTime) AS orderDate,SUM(totalAmount) AS totalAmount FROM orderdetails GROUP BY DATE(orderDateTime) ORDER BY MAX(orderDateTime) DESC LIMIT 5) AS lastFiveOrders GROUP BY orderDate ORDER BY orderDate DESC;";
	
		try {
		JSONArray result = datafetcher.executeSelectQuery("orderdetails", queryString);
		response.setContentType("application/json");
		response.getWriter().print(result);
		response.setStatus(response.SC_ACCEPTED);
		}
		catch (Exception e) {
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
