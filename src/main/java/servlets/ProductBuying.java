package servlets;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import connectors.Persistence;
import fetcher.CSVFetcher;
import fetcher.DataFetcher;
import fetcher.XMLDataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loader.ProductDetails;
import utilities.JwtUtils;

/**
 * Servlet implementation class ProductBuying
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 50)
public class ProductBuying extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductBuying() {
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
			String queryString = "";
			if (datafetcher instanceof XMLDataFetcher) {
			} else {
				queryString = "select * from orderdetails INNER JOIN orderedProductDetails ON orderDetails.id = orderedProductDetails.orderid INNER JOIN productDetails ON productDetails.id = orderedProductDetails.productid LEFT JOIN user_address ON orderDetails.addressId = user_address.id where orderdetails.customerId = "
						+ headerValue.get("id") + ";";
			}
			JSONArray result = datafetcher.executeSelectQuery("cartDetails", queryString);
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.getWriter().print(new JSONObject() {
				{
					put("error", e.getMessage());
				}
			});
			e.printStackTrace();
		}
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			HashMap<String, Object> valuesMap = new HashMap<>();
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

			JSONArray jsonArray = new JSONArray(request.getParameter("productDetails"));
			ProductDetails.orderId++;

			ArrayList<HashMap<String, Object>> bulkColumnValues = new ArrayList<>();
			for (int i = 0; i < jsonArray.length(); i++) {
				ProductDetails.orderProductId++;
				JSONObject product = jsonArray.getJSONObject(i);
				System.out.println(product.get("id") + "productId");
				HashMap<String, Object> map = new HashMap<>();
				map.put("productid", product.get("id"));
				map.put("count", product.get("quantity"));
				map.put("orderid", ProductDetails.orderId);
				map.put("id", ProductDetails.orderProductId);
				map.put("price", product.get("total"));
				bulkColumnValues.add(map);
			}
			valuesMap.put("id", ProductDetails.orderId);
			valuesMap.put("customerId", headerValue.get("id"));
			LocalDateTime now = LocalDateTime.now();
			valuesMap.put("orderDateTime", now);
			valuesMap.put("totalAmount", request.getParameter("totalAmount"));
			valuesMap.put("address", request.getParameter("address"));
			valuesMap.put("paymentWay", request.getParameter("paymentWay"));
			valuesMap.put("addressId", request.getParameter("addressId"));
			valuesMap.put("orderstatus", "process");

			CloseableHttpClient httpclient = HttpClients.createDefault();

			List<BasicNameValuePair> nv = new ArrayList<BasicNameValuePair>();

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nv, Charset.forName("utf-8"));
			HttpPost httpPost = new HttpPost("http://localhost:8001/Payment/Transaction");
			httpPost.setEntity(entity);
			String responseBody = httpclient.execute(httpPost, responseHandler);
			if (responseBody.equals("Payment success") && datafetcher.addRecord("OrderDetails", valuesMap)) {
				datafetcher.addRecordsinBulk("orderedproductdetails", bulkColumnValues);

				// Activity log Buy the products.
				CSVFetcher csvFetcher = new CSVFetcher();
				ArrayList<String> csvData = new ArrayList<>();
				csvData.add("" + headerValue.get("id"));
				csvData.add("" + java.time.LocalDate.now().toString());
				csvData.add("" + System.currentTimeMillis());
				csvData.add("Buy the products.");
				csvData.add("");

				csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));

				HashMap<String, Object> values1Map = new HashMap<>();

				ProductDetails.orderProcessId++;
				values1Map.put("id", ProductDetails.orderProcessId);
				values1Map.put("orderid", ProductDetails.orderId);
				values1Map.put("processtime", LocalDateTime.now());
				values1Map.put("description", "The process is begon.");

				datafetcher.addRecord("orderprocess", values1Map);

				OrderDeliveryProcess orderDeliveryProcess = new OrderDeliveryProcess();
				orderDeliveryProcess.setProcessId(ProductDetails.orderProcessId);
				orderDeliveryProcess.setOrderId(ProductDetails.orderId);
				orderDeliveryProcess.processOrderFlow();

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("The product ordered successfully...");
			} else {
				response.setStatus(Integer.parseInt(responseBody));
				response.getWriter().print("The product not ordered...");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

	ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
		@Override

		public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

			int status = response.getStatusLine().getStatusCode();

			if (status >= 200 && status < 300) {

				HttpEntity entity = response.getEntity();

				return entity != null ? EntityUtils.toString(entity) : null;

			} else {

				return status + "";

			}

		}
	};

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
			String queryString = "DELETE FROM orderprocess WHERE orderid = " + request.getParameter("orderid") + ";";
			datafetcher.executeUpdateQuery("reviewdetails", queryString);

			queryString = "DELETE FROM orderedproductdetails WHERE orderid = " + request.getParameter("orderid") + ";";
			datafetcher.executeUpdateQuery("reviewdetails", queryString);

			queryString = "DELETE FROM orderdetails WHERE id = " + request.getParameter("orderid") + ";";
			datafetcher.executeUpdateQuery("reviewdetails", queryString);

			response.getWriter().print("Review deleted successfully");
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

}
