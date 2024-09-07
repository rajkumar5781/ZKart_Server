package servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import connectors.Persistence;
import fetcher.CSVFetcher;
import fetcher.DataFetcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import loader.ProductDetails;
import utilities.JwtUtils;

/**
 * Servlet implementation class Product
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 50)
public class Products extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @param string7
	 * @param string6
	 * @param string5
	 * @param string4
	 * @param string3
	 * @param string2
	 * @param string
	 * @see HttpServlet#HttpServlet()
	 */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		String selectQuery = "SELECT * FROM productDetails WHERE id = '"+request.getParameter("id")+"';";
		JSONArray result = datafetcher.executeSelectQuery("productDetails", selectQuery);
		response.setContentType("application/json");
         response.getWriter().print(result);
         response.setStatus(HttpServletResponse.SC_ACCEPTED);
		}
		catch (Exception e) {
			response.getWriter().print("Error occur");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	private String getFileName(final Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

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
	            }
	            catch (Exception e) {
					// TODO: handle exception
	            	System.out.println(e.getMessage());
				}
	        }
			
			
			String fileName = uploadImage(request);			
			HashMap<String, Object> map = new HashMap<>();
			ProductDetails.productId++;
			map.put("id", ProductDetails.productId);
			map.put("Name", request.getParameter("Name"));
			map.put("Description", request.getParameter("Description"));
			map.put("Category", request.getParameter("Category"));
			map.put("Actual_price", request.getParameter("Actual_price"));
			map.put("Discounts", Integer.parseInt(request.getParameter("Discounts")));
			map.put("Available_count", Integer.parseInt(request.getParameter("productCount")));
			map.put("Product_image", fileName);
			map.put("Likes", request.getParameter("Likes"));
			map.put("sellerId", headerValue.get("id"));
			
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			try {
				if(datafetcher.addRecord("productDetails", map)) {
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().print("The product uploaded successfully...");
				}
				else {
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().print("The product not uploaded successfully...");
				}
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
				e.printStackTrace();
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			String fileNameString = "";
			if(request.getPart("image")!=null) {
				fileNameString = uploadImage(request);
			}
			DataFetcher datafetcher = new Persistence().getDatafetcher();
			String name = request.getParameter("Name");
	        String description = request.getParameter("Description");
	        String category = request.getParameter("Category");
	        String actualPrice = request.getParameter("Actual_price");
	        String discounts = request.getParameter("Discounts");
	        String productCount = request.getParameter("productCount");
	        String likes = request.getParameter("Likes");
	        
	        String upadteQuery = "update productdetails set Name = '"+name+"' , Description='"+description+"' ,Category = '"+category+"', Actual_price="+
	        		actualPrice +", Discounts="+discounts+", Available_count ="+productCount+", Likes ="+likes+"";
	        
	        if(fileNameString.length()>0) {
	        	upadteQuery = upadteQuery+", Product_image = '"+fileNameString+"'";
	        }
	        upadteQuery = upadteQuery+" where id = "+request.getParameter("id")+";";

	        datafetcher.executeUpdateQuery("productdetails", upadteQuery);
	        response.getWriter().print("The product updated successfully...");
			response.setStatus(HttpServletResponse.SC_OK);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		try {
			String queryString = "DELETE FROM productdetails WHERE id = "+request.getParameter("id")+";";
			datafetcher.executeUpdateQuery("user_address", queryString);
			
			CSVFetcher csvFetcher = new CSVFetcher();
			ArrayList<String> csvData = new ArrayList<>();
			csvData.add(""+ headerValue.get("id"));
			csvData.add(""+java.time.LocalDate.now().toString());
			csvData.add(""+System.currentTimeMillis());
			csvData.add("Delete the product.");
			csvData.add("");
		
			csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
			
			response.getWriter().print("Product delete successfully..");
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (Exception e) {
			// TODO: handle exception
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private String uploadImage(HttpServletRequest request) throws Exception {
		ServletContext context = getServletContext();
		

		Part filePart = request.getPart("image");

		String fileName = getFileName(filePart);
		String uploadDirectory = System.getProperty("user.home")+"\\Documents\\zkart\\images\\";
		OutputStream out = null;
		InputStream fileContent = null;
		try {
			out = new FileOutputStream(new File(uploadDirectory, fileName));
			fileContent = filePart.getInputStream();

			int read;
			final byte[] bytes = new byte[1024];
			while ((read = fileContent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			if (fileContent != null) {
				fileContent.close();
			}
		}
		return fileName;
	}
}
