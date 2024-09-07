package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import components.Product;
import connectors.Persistence;
import fetcher.DataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import loader.ProductDetails;

//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Servlet implementation class BulkProductUpdate
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50)
public class BulkProductUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BulkProductUpdate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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
		// TODO Auto-generated method stub
		Part filePart = request.getPart("fileInput");

		// Reading file
		InputStream fileContent = filePart.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileContent));
		String headerLineString = bufferedReader.readLine();

		if (headerLineString != null) {

			// validate the column count and names
			if (validateHeader(headerLineString.split(","))) {
				String[] arr = headerLineString.split(",");

				// Header positions
				int[] position = positionOrder(arr);
				String Line;
				ArrayList<HashMap<String, Object>> bulkColumnValues = new ArrayList<>();
				while ((Line = bufferedReader.readLine()) != null) {
					String[] productDetailsArray = Line.split(",");
					try {
						HashMap<String, Object> map = new HashMap<>();
						ProductDetails.productId++;
						map.put("id", ProductDetails.productId);
						map.put("Name", productDetailsArray[position[0]]);
						map.put("Description", productDetailsArray[position[1]]);
						map.put("Category", productDetailsArray[position[2]]);
						map.put("Actual_price", productDetailsArray[position[3]]);
						map.put("Discounts", Integer.parseInt(productDetailsArray[position[4]]));
						map.put("Likes", productDetailsArray[position[5]]);
						map.put("Available_count", productDetailsArray[position[6]]);
						map.put("Product_image", productDetailsArray[position[7]]);
						bulkColumnValues.add(map);
					} catch (Exception e) {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in
																							// response body
						e.printStackTrace();
					}
				}
				DataFetcher datafetcher = new Persistence().getDatafetcher();
				try {
					datafetcher.addRecordsinBulk("productDetails", bulkColumnValues);
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().print("The product uploaded successfully...");
				} catch (Exception e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in
																						// response body
					e.printStackTrace();
				}
			}
		}

		response.getWriter().print("The product uploaded successfully...");
	}

	public static boolean validateHeader(String[] headerArr) {
		if (Product.productHeader.size() == headerArr.length) {
			for (int header = 0; header < headerArr.length; header++) {
				if (!Product.productHeader.contains(headerArr[header])) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public static int[] positionOrder(String[] headerArr) {
		int[] arr = new int[headerArr.length];
		for (int header = 0; header < headerArr.length; header++) {
			arr[header] = Product.productHeader.indexOf(headerArr[header]);
		}
		return arr;
	}
}
