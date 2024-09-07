package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import connectors.Persistence;
import fetcher.CSVFetcher;
import fetcher.DataFetcher;
import fetcher.XMLDataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.JwtUtils;

/**
 * Servlet implementation class CutomerDetail
 */
public class CutomerDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CutomerDetail() {
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
		        
		int userId =(int) headerValue.get("id");
		try {
			String query = "";
			if (datafetcher instanceof XMLDataFetcher) {
				query = String.format("/users/userData[@userId = '%s']", request.getParameter("customerId"));
			} else {
				query = "select * from users where userId = " + userId + ";";
			}
			JSONArray result = datafetcher.executeSelectQuery("users", query);
			if (result != null && result.length() > 0) {
				response.setContentType("application/json");
				result.getJSONObject(0).remove("password");
				result.getJSONObject(0).remove("userId");
				result.getJSONObject(0).remove("userName");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print(result);
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("No products available....");
			}
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
        
		int userId =(int) headerValue.get("id");
		String name = request.getParameter("name");
		String lastName = request.getParameter("lastName");
		String phone = request.getParameter("phone");
		try {
			String query = "";
			if (datafetcher instanceof XMLDataFetcher) {
//				query = String.format("/users/userData[@userId = '%s']", request.getParameter("customerId"));
			} else {
				query = "update users set name = '"+name+"', lastName = '"+lastName+"', phone = '"+phone+"' where userId = "+userId+";";
			}
			System.out.println(query);		
			datafetcher.executeUpdateQuery("users", query);
			
			
			//log customerDetails updated.
			CSVFetcher csvFetcher = new CSVFetcher();
			ArrayList<String> csvData = new ArrayList<>();
			csvData.add(""+ headerValue.get("id"));
			csvData.add(""+java.time.LocalDate.now().toString());
			csvData.add(""+System.currentTimeMillis());
			csvData.add("The details user updated.");
			csvData.add("");
		
			csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
			
			response.getWriter().print("The details user updated successfully");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

}
