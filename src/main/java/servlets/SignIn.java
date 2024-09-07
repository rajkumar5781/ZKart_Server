package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilities.JwtUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

import connectors.Persistence;
import fetcher.CSVFetcher;
import fetcher.CassandraDataFetcher;
import fetcher.DataFetcher;
import fetcher.XMLDataFetcher;

/**
 * Servlet implementation class SignIn
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 50)
public class SignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignIn() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		DataFetcher datafetcher = new Persistence().getDatafetcher();
		String queryString = "";
		try {
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			String role = request.getParameter("role");
			if(role == null ||  role.isEmpty()) {
				role = "customer";
			}

			if (datafetcher instanceof XMLDataFetcher) {
				queryString = String.format("/users/userData[userName = '%s' and password = '%s']", userName, password);
			} else {
				queryString = "SELECT * FROM users WHERE userName ='" + userName + "' AND password ='" + password+"' AND role = '"+role+"';";
			}
			JSONArray result = datafetcher.executeSelectQuery("users", queryString);
			if (result != null && result.length() > 0) {
				JSONObject jsonObject = (JSONObject) result.get(0);
				session.setAttribute("userName", jsonObject.get("userName"));
				session.setAttribute("Id", jsonObject.get("userId"));
				
				//store the activity in activity log
//				CassandraDataFetcher connection = new CassandraDataFetcher();
				
				
//				HashMap<String, Object> insertValueMap = new HashMap<>();
//				
//				insertValueMap.put("userid", jsonObject.get("userId"));
//				insertValueMap.put("date", java.time.LocalDate.now().toString());
//				insertValueMap.put("time", System.currentTimeMillis());
//				insertValueMap.put("details", "Signed in.");
//				insertValueMap.put("subject", "");
				
				CSVFetcher csvFetcher = new CSVFetcher();
				ArrayList<String> csvData = new ArrayList<>();
				csvData.add(""+jsonObject.get("userId"));
				csvData.add(""+java.time.LocalDate.now().toString());
				csvData.add(""+System.currentTimeMillis());
				csvData.add("Signed in.");
				csvData.add("");
			
				csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
			
//				connection.addValuesToActivityLog(insertValueMap);
				
				JwtUtils tokenJwtUtils = new JwtUtils();
				String tokenString= tokenJwtUtils.generateJwt(userName,jsonObject.get("role")+"",Integer.parseInt(jsonObject.get("userId")+""));
				JSONObject object = new JSONObject();
				object.put("authentication", tokenString);
				object.put("role", jsonObject.get("role"));
				
				response.setContentType("application/json");
//				jsonObject.remove("lastName");
//				jsonObject.remove("name");
//				jsonObject.remove("password");
//				jsonObject.remove("phone");
//				jsonObject.remove("userName");
				response.getWriter().print(object);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().print(new JSONObject() {{ put("message", "Invalid username or password"); }});
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

}
