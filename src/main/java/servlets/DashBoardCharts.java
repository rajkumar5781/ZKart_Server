package servlets;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import connectors.Persistence;
import fetcher.DataFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loader.ProductDetails;

/**
 * Servlet implementation class DashBoardCharts
 */
public class DashBoardCharts extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DashBoardCharts() {
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
//		try {
//			String seletcQuery = "SELECT * FROM dashboardcharts ORDER BY orders;";
//			JSONArray result = datafetcher.executeSelectQuery("dashboardcharts", seletcQuery);
//			response.setContentType("application/json");
//			response.getWriter().print(result);
//			response.setStatus(response.SC_ACCEPTED);
//		}
//		catch (Exception e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
//			e.printStackTrace();
//		}
		try {
			String seletcQuery = "SELECT * FROM dashboardcharts where dashboardId = " + request.getParameter("id")
					+ ";";
			JSONArray result = datafetcher.executeSelectQuery("dashboardcharts", seletcQuery);
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
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
		try {
			DataFetcher datafetcher = new Persistence().getDatafetcher();

			JSONArray jsonArray = new JSONArray(request.getParameter("charts"));
			ArrayList<HashMap<String, Object>> bulkColumnValues = new ArrayList<>();

			for (int i = 0; i < jsonArray.length(); i++) {
				HashMap<String, Object> map = new HashMap<>();
				ProductDetails.dashboardChartId++;
				JSONObject object = jsonArray.getJSONObject(i);
				map.put("id", ProductDetails.dashboardChartId);
				map.put("chartName", object.get("chartName"));
				map.put("type", object.get("type"));
				map.put("width", object.get("width"));
				map.put("height", object.get("height"));
				map.put("x", object.get("x"));
				map.put("y", object.get("y"));
				map.put("dashboardId", object.get("dashboardId"));
				map.put("reportId", object.get("reportId"));
				bulkColumnValues.add(map);
			}
			datafetcher.addRecordsinBulk("dashboardcharts", bulkColumnValues);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().print("The dashboard created successfully...");
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataFetcher datafetcher = new Persistence().getDatafetcher();
		try {
			Map<String, String> params = utilities.Util.parseQueryString(request);
			JSONArray jsonArray = new JSONArray(URLDecoder.decode(params.get("charts"), "UTF-8"));
			ArrayList<String> queryList = new ArrayList<>();
			
			for (int i = 0; i < jsonArray.length(); i++) {
				String updateQueryString = "";
				JSONObject object = jsonArray.getJSONObject(i);
				if(String.valueOf(object.get("id")).equals("-1")) {
					ProductDetails.dashboardChartId++;
					updateQueryString = "insert into dashboardcharts (chartName,reportId,dashboardId,width,x,y,id,type,height) values ('"+object.get("chartName")+"',"+object.get("reportId")+","+object.get("dashboardId")+","+object.get("width")+","+object.get("x")+","+object.get("y")+","+ProductDetails.dashboardChartId+",'"+object.get("type")+"',"+object.get("height")+");";                                                   
				}
				else {
					updateQueryString = "update dashboardcharts set chartName='"+object.get("chartName")+"', reportId="+object.get("reportId")+", dashboardId="+object.get("dashboardId")+", width="+object.get("width")+", x="+object.get("x")+", y="+object.get("y")+", type ='"+object.get("type")+"', height="+object.get("height")+" where id ="+object.get("id")+";";                       
				}
				queryList.add(updateQueryString);
			}
			datafetcher.executeBulkUpdateStingQuery("dashboardcharts", queryList);
			response.getWriter().print("Dashboard updated successfully.");
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("An error occurred: " + e.getMessage()); // Send error message in response body
			e.printStackTrace();
		}
	}

}
