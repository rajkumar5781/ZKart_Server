package servlets;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import connectors.Persistence;
import fetcher.DataFetcher;
import fetcher.XMLDataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class productDetails
 */
public class productDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public productDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		String selectQuery = "";
		try {
			if(datafetcher instanceof XMLDataFetcher) {

				selectQuery = String.format("/productDetails/Item[@id='%s']",request.getParameter("id"));
			}else {
				selectQuery = "SELECT * FROM productDetails WHERE id = "+ request.getParameter("id") +";";
			}
			JSONArray result = datafetcher.executeSelectQuery("productDetails", selectQuery);
			response.setContentType("application/json");
			if(result.length()>0) {
            response.getWriter().print(result.get(0));
			}
			else {
				response.getWriter().print(new JSONObject() {{ put("data", "data not available"); }});
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
