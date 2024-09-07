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
 * Servlet implementation class TopDiscountProducts
 */
public class TopDiscountProducts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopDiscountProducts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();			
		String queryString = "SELECT Name,Discounts FROM productdetails order by Discounts DESC limit 5;";
	
		try {
		JSONArray result = datafetcher.executeSelectQuery("productdetails", queryString);
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
		
	}

}
