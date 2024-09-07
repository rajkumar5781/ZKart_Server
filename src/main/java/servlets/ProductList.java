package servlets;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;

import connectors.Persistence;
import fetcher.DataFetcher;
import fetcher.XMLDataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.JwtUtils;

/**
 * Servlet implementation class ProductList
 */
@WebServlet("/ProductList")
public class ProductList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		String selectQuery = "";
		int pageSize = 10;
		int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		int offset = (pageNumber - 1) * pageSize;
		try {
			if(datafetcher instanceof XMLDataFetcher) {
				int startIndex = (pageNumber - 1) * pageSize + 1;
				int endIndex = startIndex + pageSize - 1;
				
				selectQuery = String.format("/productDetails/Item[contains(translate(Name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')]"+utilities.XMLUtil.getWhereXMLQueryString(request)+"[position() >= %d and position() <= %d]", request.getParameter("searchWord").toLowerCase(),startIndex, endIndex);
			}else {
				String searchWordString = (request.getParameter("searchWord")!=null ) ? request.getParameter("searchWord") : "";
				
				selectQuery = "SELECT * FROM productDetails WHERE Name LIKE '%"+ searchWordString +"%'";
				if(!headerValue.isEmpty() && headerValue.get("type").equals("vender")) {
					selectQuery = "SELECT * FROM productDetails WHERE Name LIKE '%"+ searchWordString +"%' and sellerid = "+headerValue.get("id");
		        }
				String filterQueryString = utilities.MySQLUtilitys.getWhereSQLQueryString(request);
				if(filterQueryString.length()>0) {
					selectQuery = selectQuery + " and " + filterQueryString;
				}
				selectQuery = selectQuery+" LIMIT "+pageSize+" OFFSET "+offset +";";
			}
			JSONArray result = datafetcher.executeSelectQuery("productDetails", selectQuery);
				response.setContentType("application/json");
	             response.getWriter().print(result);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
