package loader;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public class StartupServlet extends HttpServlet {
	public void init() {
//		CustomerDetails cDetails = new CustomerDetails();
		ProductDetails productDetails = new ProductDetails();
		try {
			productDetails.setCustomerId("users");
			productDetails.setProductId("productDetails");
			productDetails.setAddToCartId("cartDetails");
			productDetails.setOrderId("OrderDetails");
			productDetails.setReviewId("ReviewDetails");
			productDetails.setOrderProductId("orderedProductDetails");
			productDetails.setAddressId("user_address");
			productDetails.setOrderProcessId("orderprocess");
			productDetails.setReportsId("reports");
			productDetails.setFolderId("folder");
			productDetails.setDashboardId("dashboard");
			productDetails.setDashboardChartId("dashboardcharts");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e.getMessage());
		}

	}
}
