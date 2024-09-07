package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import loader.ProductDetails;
import utilities.CassandraUtils;
import utilities.JwtUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import connectors.Persistence;
import fetcher.CassandraDataFetcher;
import fetcher.DataFetcher;
import fetcher.XMLDataFetcher;

/**
 * Servlet implementation class Reviews
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 50)
public class Reviews extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Reviews() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
			String productId = request.getParameter("productId");
			JSONArray jsonResult = new JSONArray();
			if(datafetcher instanceof XMLDataFetcher) {
				// to fetch the ReviewsDetails to match the productID
				String queryString = String.format("//Review[productId='%s']", productId);
				JSONArray reviewXLMJson = datafetcher.executeSelectQuery("ReviewDetails", queryString);
				JSONArray usersXMLJson = new JSONArray();

				for(int index=0;index<reviewXLMJson.length();index++) {
					JSONObject jsonObject = (JSONObject) reviewXLMJson.get(index);
					
					//To fetch the reviewed user details
					String.format("//userData[@userId='%s']", jsonObject.get("customerId"));
					usersXMLJson = datafetcher.executeSelectQuery("users", String.format("//userData[@userId='%s']", jsonObject.get("customerId")));
					
					//Add the data's to jsonObject
					for(Map.Entry userEntry : ((JSONObject) usersXMLJson.get(0)).toMap().entrySet()) {
						if((String) userEntry.getKey()=="name" || (String) userEntry.getKey()=="lastName") {
						jsonObject.put((String) userEntry.getKey(), userEntry.getValue());
						}
					}
					jsonResult.put(jsonObject);
				}
			}
			else {
				String queryString = "SELECT reviewdetails.id,reviewdetails.customerId,reviewdetails.comment,reviewdetails.star,reviewdetails.datetime,users.name FROM reviewdetails left JOIN users ON reviewdetails.customerId = users.userId WHERE reviewdetails.productId = "+productId+";";
//				CassandraDataFetcher cassandraDataFetcher = new CassandraDataFetcher();
//				PreparedStatement ps = cassandraDataFetcher.getSession().prepare(reviewQuery);
//				BoundStatement bs = ps.bind(ProductDetails.reviewId,request.getParameter("comment"), headerValue.get("id"),datetime,Integer.parseInt(request.getParameter("productId")),Integer.parseInt(request.getParameter("star")));
//				cassandraDataFetcher.getSession().execute(bs);
				System.out.println(queryString);
				queryString = "SELECT id,customerid,comment,star,datetime FROM reviewdetails WHERE productId = ?;";
				CassandraDataFetcher cassandraDataFetcher = new CassandraDataFetcher();
				jsonResult = CassandraUtils.resultSetToJSON(cassandraDataFetcher.getSession().execute(queryString,Integer.parseInt(productId)));
//				cassandraDataFetcher.getSession().execute(queryString,Integer.parseInt(productId));
				
//				jsonResult = datafetcher.executeSelectQuery("ReviewDetails", queryString);
				
				for(int i=0;i<jsonResult.length();i++) {
					JSONObject object = jsonResult.getJSONObject(i);
					queryString = "select name from users where userId = "+object.get("customerid")+";";
					System.out.println(object.get("customerid")+"-->"+queryString);
					JSONArray jsonResult1 = datafetcher.executeSelectQuery("users", queryString);
					object.put("name", jsonResult1.getJSONObject(0).get("name"));
				}
				
			}
			response.getWriter().print(jsonResult);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.getWriter().print("Error occur");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
		HttpSession session = request.getSession();
//		session.getAttribute("Id")
		
		//Get the Headervalues
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
		
		
		HashMap<String, Object> map = new HashMap<>();
		ProductDetails.reviewId++;
		map.put("id", ProductDetails.reviewId);
		map.put("productId", request.getParameter("productId"));
		map.put("customerId", headerValue.get("id"));
		map.put("comment", request.getParameter("comment"));
		map.put("star", request.getParameter("star"));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		Date datetime = new Date();
		map.put("dateTime", now);
		
		String reviewQuery = "insert into reviewdetails (id,comment,customerid,datetime,productid,star) values (?,?,?,?,?,?)";
		CassandraDataFetcher cassandraDataFetcher = new CassandraDataFetcher();
		PreparedStatement ps = cassandraDataFetcher.getSession().prepare(reviewQuery);
		BoundStatement bs = ps.bind(ProductDetails.reviewId,request.getParameter("comment"), headerValue.get("id"),datetime,Integer.parseInt(request.getParameter("productId")),Integer.parseInt(request.getParameter("star")));
		cassandraDataFetcher.getSession().execute(bs);

		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
			if (datafetcher.addRecord("ReviewDetails", map)) {
				
				response.getWriter().print("product reviewed successfully...");
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().print("Somethings went worng......");
			}
			Map<String, String> updatedParams = new HashMap<>();
            updatedParams.put("star", request.getParameter("star"));
            updatedParams.put("productId", request.getParameter("productId"));
			updateProductStarRating(updatedParams,response,"add");
			
			//store the activity in activity log
			CassandraDataFetcher connection = new CassandraDataFetcher();				
			HashMap<String, Object> insertValueMap = new HashMap<>();
			
			insertValueMap.put("userid", headerValue.get("id"));
			insertValueMap.put("date", java.time.LocalDate.now().toString());
			insertValueMap.put("time", System.currentTimeMillis());
			insertValueMap.put("details", "product review add successfully.");
			insertValueMap.put("subject", "");
		
			connection.addValuesToActivityLog(insertValueMap);
			
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		//Get the Headervalues
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
		        
		try {
			
			String body = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));

			Map<String, String> params = parseQueryString(body);
			
			String comment =  URLDecoder.decode(params.get("comment"), StandardCharsets.UTF_8.name());
            String star = params.get("star");
            String reviewId = params.get("reviewId");
            String productId = params.get("productId");
            
            Map<String, String> updatedParams = new HashMap<>();
            updatedParams.put("star", star);
            updatedParams.put("productId", productId);
            String selectQuery = "SELECT star FROM reviewdetails WHERE id = "+reviewId+";";
			JSONArray result = datafetcher.executeSelectQuery("reviewdetails", selectQuery);
			if (result != null && result.length() > 0) {
				if(result.getJSONObject(0).has("star") && !result.getJSONObject(0).isNull("star")){
					updatedParams.put("oldStar",result.getJSONObject(0).get("star").toString());
				}
			}
			
			//update the customer review in cassandra 
			String reviewQuery = "update reviewdetails SET comment=?,datetime=?,star=? where productid = ? and id = ?";
			CassandraDataFetcher cassandraDataFetcher = new CassandraDataFetcher();
			cassandraDataFetcher.getSession().execute(reviewQuery,comment,new Date(),Integer.parseInt(star),Integer.parseInt(productId),Integer.parseInt(reviewId));
			
			updateProductStarRating(updatedParams,response,"update");
			String updateQuery = "update reviewdetails SET comment = '"+comment+"', star = "+star+" where id = "+reviewId+";";
			datafetcher.executeUpdateQuery("ReviewDetails", updateQuery);
			response.getWriter().print("product reviewed updated successfully...");
			
			//store the activity in activity log
			CassandraDataFetcher connection = new CassandraDataFetcher();				
			HashMap<String, Object> insertValueMap = new HashMap<>();
			
			insertValueMap.put("userid", headerValue.get("id"));
			insertValueMap.put("date", java.time.LocalDate.now().toString());
			insertValueMap.put("time", System.currentTimeMillis());
			insertValueMap.put("details", "product reviewed updated successfully.");
			insertValueMap.put("subject", "");
		
			connection.addValuesToActivityLog(insertValueMap);
			
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
			//Get the Headervalues
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
			
			String selectQuery = "SELECT star, productId FROM reviewdetails WHERE id = "+request.getParameter("reviewId")+";";
			JSONArray result = datafetcher.executeSelectQuery("reviewdetails", selectQuery);
			
			Map<String, String> updatedParams = new HashMap<>();
			if (result != null && result.length() > 0) {
				if(result.getJSONObject(0).has("star") && !result.getJSONObject(0).isNull("star")) {
					updatedParams.put("star",result.getJSONObject(0).get("star").toString());
				}
				if(result.getJSONObject(0).has("productId") && !result.getJSONObject(0).isNull("productId")){
					updatedParams.put("productId",result.getJSONObject(0).get("productId").toString());
				}
			}
			updateProductStarRating(updatedParams,response,"remove");
			String queryString = "DELETE FROM reviewdetails WHERE id = "+request.getParameter("reviewId")+";";
			
			String reviewQuery = "DELETE FROM reviewdetails where id = ?";
			CassandraDataFetcher cassandraDataFetcher = new CassandraDataFetcher();
			cassandraDataFetcher.getSession().execute(reviewQuery,Integer.parseInt(request.getParameter("reviewId")));
			
			System.out.println(queryString);
			datafetcher.executeUpdateQuery("reviewdetails", queryString);
			response.getWriter().print("Review deleted successfully");
			
			//store the activity in activity log
			CassandraDataFetcher connection = new CassandraDataFetcher();				
			HashMap<String, Object> insertValueMap = new HashMap<>();
			
			insertValueMap.put("userid", headerValue.get("id"));
			insertValueMap.put("date", java.time.LocalDate.now().toString());
			insertValueMap.put("time", System.currentTimeMillis());
			insertValueMap.put("details", "product reviewed updated successfully.");
			insertValueMap.put("subject", "");
		
			connection.addValuesToActivityLog(insertValueMap);
			
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	 private Map<String, String> parseQueryString(String queryString) {
	        Map<String, String> params = new HashMap<>();
	        String[] pairs = queryString.split("&");
	        for (String pair : pairs) {
	            String[] keyValue = pair.split("=");
	            if (keyValue.length == 2) {
	                params.put(keyValue[0], keyValue[1]);
	            }
	        }
	        return params;
	    }
	
	
	
	public void updateProductStarRating(Map<String, String> updatedParams, HttpServletResponse response,String option) throws Exception {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
		String selectQuery = "SELECT Star, rating_count FROM productDetails WHERE id = "+updatedParams.get("productId")+";";
		JSONArray result = datafetcher.executeSelectQuery("productDetails", selectQuery);
		 int newRatingCount,currentStar,newStar = 0;
		 int newRating = Integer.parseInt(updatedParams.get("star"));
		if (result != null && result.length() > 0) {
			currentStar = 0;
			newRatingCount = 0;
			if(result.getJSONObject(0).has("Star") && !result.getJSONObject(0).get("Star").toString().isEmpty() && !result.getJSONObject(0).isNull("Star")) {
				currentStar = Integer.parseInt(result.getJSONObject(0).get("Star").toString());
			}
			if(result.getJSONObject(0).has("rating_count") && !result.getJSONObject(0).get("rating_count").toString().isEmpty() && !result.getJSONObject(0).isNull("rating_count")){
				newRatingCount=((int) result.getJSONObject(0).get("rating_count"));
			}
			if(option.equals("add")) {
				newStar = (currentStar * newRatingCount + newRating) / (newRatingCount+1);
				newRatingCount = newRatingCount+1;
			}
			else if(option.equals("update")) {
				int oldRating = Integer.parseInt(updatedParams.get("oldStar"));
				newStar = (((currentStar * newRatingCount)-oldRating) + newRating) / (newRatingCount);
			}
			else if(option.equals("remove")) {
				int val = (newRatingCount-1)==0 ? 1 : newRatingCount-1;
				newStar = ((currentStar * newRatingCount) - newRating) / (val);
				newRatingCount = newRatingCount-1;
			}
			
			String updateQueryString = "UPDATE productDetails SET star = "+newStar+", rating_count = "+(newRatingCount) +" WHERE id = "+updatedParams.get("productId")+";";
			datafetcher.executeUpdateQuery("productDetails", updateQueryString);
		}
		}
		catch (Exception e) {
			throw e;
		}
	}

}
