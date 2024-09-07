package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loader.ProductDetails;
import utilities.JwtUtils;	

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import connectors.Persistence;
import fetcher.DataFetcher;

/**
 * Servlet implementation class Folders
 */
public class Folders extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Folders() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Get the HeaderValues
			HashMap<String, Object> headerValue = new HashMap<>();
			String authHeader = request.getHeader("Authorization");
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);
				try {
					JwtUtils jwtUtils = new JwtUtils();
					headerValue = jwtUtils.getHeaderValues(token);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
			}

			DataFetcher datafetcher = new Persistence().getDatafetcher();
			String selectQuery = "SELECT * from folder where userId = " + headerValue.get("id") + ";";
			
			//get all dashboard folder
			if(request.getParameter("type").equals("dashboard")) {
				selectQuery = "SELECT * from folder where userId = " + headerValue.get("id") + " and folderType = 'dashboard';";
				if(request.getParameter("id")!=null && !request.getParameter("id").isEmpty()) {
					selectQuery = "SELECT * from folder where userId = " + headerValue.get("id") + " and id = "+request.getParameter("id")+" and folderType = 'dashboard';";
				}
			}
			//get all analytics folder
			else if(request.getParameter("type").equals("analytics")) {
				selectQuery = "SELECT * from folder where userId = " + headerValue.get("id") + " and folderType = 'analytics';";
			}
			JSONArray result = datafetcher.executeSelectQuery("folder", selectQuery);
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.setStatus(response.SC_ACCEPTED);	
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

			HashMap<String, Object> headerValue = new HashMap<>();
			String authHeader = request.getHeader("Authorization");
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);
				try {
					JwtUtils jwtUtils = new JwtUtils();
					headerValue = jwtUtils.getHeaderValues(token);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
			}
			ProductDetails.folderId++;
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", ProductDetails.folderId);
			map.put("folderName", request.getParameter("folderName"));
			map.put("userId", headerValue.get("id"));
			map.put("folderType", request.getParameter("folderType"));
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			if(datafetcher.addRecord("folder", map)) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("The folder created successfully...");
			}
			else {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("The folder not created successfully...");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			Map<String, String> params = utilities.Util.parseQueryString(request);
			String upadteQuery = "UPDATE folder set folderName = '"
					+ URLDecoder.decode(params.get("folderName"), "UTF-8") + "' where id = "
					+ URLDecoder.decode(params.get("id"), "UTF-8") + ";";

			datafetcher.executeUpdateQuery("folder", upadteQuery);
			response.getWriter().print("The folder updated successfully...");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			String queryString = "DELETE FROM folder WHERE id = " + request.getParameter("id") + ";";
			datafetcher.executeUpdateQuery("folder", queryString);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
