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
 * Servlet implementation class SingleOrderDetails
 */
public class SingleOrderDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleOrderDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
			String queryString = "SELECT * FROM orderprocess where orderid = "+request.getParameter("orderid")+";";
			JSONArray result = datafetcher.executeSelectQuery(queryString, queryString);
			
			JSONObject explrObject = result.getJSONObject(0);
			
			String productDetailsQuery = "SELECT orderedproductdetails.count,orderedproductdetails.price as totalPrice,productdetails.Name,productdetails.Product_image,productdetails.Category,productdetails.id from orderedproductdetails INNER JOIN productdetails on productdetails.id = orderedproductdetails.productid where orderid="+explrObject.get("orderid")+";";
            JSONArray result1 = datafetcher.executeSelectQuery("orderedproductdetails", productDetailsQuery);
            explrObject.put("products", result1);
            
            JSONObject fromAddress = new JSONObject();
            fromAddress.put("Name", "ZKart");
            fromAddress.put("Address", "no:111,Madavithi street,Athipati");
            fromAddress.put("postal", 111111);
            fromAddress.put("state", "Tamil nadu");
            fromAddress.put("district", "Chennai");
            fromAddress.put("mail", "kumarraj77279@gmail.com");
            fromAddress.put("phone", "9080151413");
            
            explrObject.put("fromAddress", fromAddress);
            
	           String addressQuery = "SELECT user_address.default_address,user_address.address,user_address.city,user_address.state,postalCode,user_address.country,user_address.mobile FROM orderdetails INNER JOIN user_address on user_address.id = orderdetails.addressId where orderdetails.id = "+explrObject.get("orderid")+";";
	           JSONArray result2 = datafetcher.executeSelectQuery("orderdetails", addressQuery);
	           explrObject.put("address", result2.get(0));
			
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.setStatus(response.SC_ACCEPTED);
		}
		catch (Exception e) {
			// TODO: handle exception
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
