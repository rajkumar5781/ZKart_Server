package servlets;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;

import connectors.Persistence;
import fetcher.DataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loader.ProductDetails;

/**
 * Servlet implementation class Dashboard
 */
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Dashboard() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
			String seletcQuery = "SELECT * FROM dashboard where folderId = " + request.getParameter("folderId") + ";";
			JSONArray result = datafetcher.executeSelectQuery("dashboard", seletcQuery);
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HashMap<String, Object> map = new HashMap<>();
			ProductDetails.dashboardId++;
			map.put("id", ProductDetails.dashboardId);
			map.put("dashboardName", request.getParameter("dashboardName"));
			map.put("folderId", request.getParameter("folderId"));
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			if(datafetcher.addRecord("dashboard", map)) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("The Dashboard created successfully...");
			}
			else {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("The Dashboard not created successfully...");
			}
			
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}
}
