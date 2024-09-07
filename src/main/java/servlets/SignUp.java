package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loader.CustomerDetails;
import loader.ProductDetails;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import components.Customer;
import connectors.Persistence;
import constants.Constants;
import fetcher.CassandraDataFetcher;
import fetcher.DataFetcher;

/**
 * Servlet implementation class SignUp
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
maxFileSize = 1024 * 1024 * 10, // 10 MB
maxRequestSize = 1024 * 1024 * 50)
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			HashMap<String, Object> map = new HashMap<>();
			ProductDetails.customerId++;
			String role = request.getParameter("role");
			if(role == null ||  role.isEmpty()) {
				role = "customer";
			}
			
//			"Id",Long.toString((Long)  session.getAttribute("Id"))
			map.put("userId", ProductDetails.customerId);
			map.put("userName", request.getParameter("userName"));
			map.put("password", request.getParameter("password"));
			map.put("name", request.getParameter("name"));
			map.put("lastName", request.getParameter("lastName"));
			map.put("phone", request.getParameter("phone"));
			map.put("role", role);
			
			DataFetcher datafetcher = new Persistence().getDatafetcher();
//			String selectQuery = datafetcher.selectQuery("productDetails", null, null);
			try {
				if(datafetcher.addRecord("users", map)) {
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().print("Setup signup successfully...");
					//store the activity in activity log
					CassandraDataFetcher connection = new CassandraDataFetcher();				
					HashMap<String, Object> insertValueMap = new HashMap<>();
					
					insertValueMap.put("userid", ProductDetails.customerId);
					insertValueMap.put("date", java.time.LocalDate.now().toString());
					insertValueMap.put("time", System.currentTimeMillis());
					insertValueMap.put("details", "signup successfully.");
					insertValueMap.put("subject", "");
				
					connection.addValuesToActivityLog(insertValueMap);
//					response.sendRedirect(Constants.RUN_DEV+"/ZKart/jsp/authentication/SignIn.jsp");
				}
				else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().print("Setup signup not successfully......");
				}
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
		
	}
}




























