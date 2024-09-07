package servlets;

import java.io.IOException;
import java.util.HashMap;

import connectors.Persistence;
import fetcher.DataFetcher;
import fetcher.XMLDataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.JwtUtils;

/**
 * Servlet implementation class ProductCount
 */
public class ProductCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductCount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		String queryString = "";
		try {
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
	        
			if (datafetcher instanceof XMLDataFetcher) {
				queryString = String.format("/productDetails/Item[contains(Name, '%s')]"+utilities.XMLUtil.getWhereXMLQueryString(request), request.getParameter("searchWord"));
			} else {
				String searchWordString = (request.getParameter("searchWord")!=null ) ? request.getParameter("searchWord") : "";
				queryString = "SELECT COUNT(Name) FROM productDetails WHERE Name LIKE '%"+ searchWordString +"%'";
				if(!headerValue.isEmpty() && headerValue.get("type").equals("vender")) {
					queryString = "SELECT COUNT(Name) FROM productDetails WHERE Name LIKE '%"+ searchWordString +"%' and sellerid = "+headerValue.get("id");
		        }
				String filterQueryString = utilities.MySQLUtilitys.getWhereSQLQueryString(request);
				if(filterQueryString.length()>0) {
					queryString = queryString + " and " + filterQueryString;
				}
				queryString = queryString+";";
			}
			response.getWriter().print(datafetcher.countRecords("productDetails", queryString));
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			response.getWriter().print(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
