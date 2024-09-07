package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.json.JSONArray;

import connectors.Persistence;
import fetcher.DataFetcher;

/**
 * Servlet implementation class TotalSelleingChart
 */
public class TotalSelleingChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TotalSelleingChart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {		
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			String selectQuery = "select sum(count) AS data,productdetails.id,productdetails.Name from orderedproductdetails inner join productdetails on orderedproductdetails.productid = productdetails.id GROUP BY productid;";
			JSONArray result = datafetcher.executeSelectQuery("orderedproductdetails", selectQuery);
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.setStatus(HttpServletResponse.SC_OK);
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
