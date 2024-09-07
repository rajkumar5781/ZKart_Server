package loader;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.tomcat.jakartaee.commons.lang3.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import components.Customer;
import components.IdGenerator;
import components.Product;
import constants.Constants;
import constants.XMLFileConstants;
import constants.XMLFileConstants.XMLDeclaration;
import fetcher.CassandraDataFetcher;

public class ProductDetails {
	public static int productId = 10000;
	public static int customerId = 10000;
	public static int orderId = 10000;
	public static int addToCartId = 10000;
	public static int reviewId = 10000;
	public static int orderProductId = 10000;
	public static int addressId = 10000;
	public static int orderProcessId = 10000;
	public static int reportsId = 10000;
	public static int folderId = 10000;
	public static int dashboardId = 10000;
	public static int dashboardChartId = 10000;
	public static ArrayList<Product> productsList = new ArrayList<>();

	private static Transformer loadTransformars() {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return transformer;
	}

	public void setCustomerId(String name) throws Exception {

		int val = (int) loadIds(name);
		if (val > customerId) {
			customerId = val;
		}
	}

	public void setProductId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > productId) {
			productId = val;
		}
	}

	public void setOrderId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > orderId) {
			orderId = val;
		}
	}

	public void setAddToCartId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > addToCartId) {
			addToCartId = val;
		}
	}
	
	public void setReviewId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > reviewId) {
			reviewId = val;
		}
	}
	
	public void setOrderProductId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > orderProductId) {
			orderProductId = val;
		}
	}
	
	public void setAddressId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > addressId) {
			addressId = val;
		}
	}
	
	public void setOrderProcessId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > orderProcessId) {
			orderProcessId = val;
		}
	}
	
	public void setReportsId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > reportsId) {
			reportsId = val;
		}
	}
	
	public void setFolderId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > folderId) {
			folderId = val;
		}
	}
	
	public void setDashboardId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > dashboardId) {
			dashboardId = val;
		}
	}
	
	public void setDashboardChartId(String name) throws Exception {
		int val = (int) loadIds(name);
		if (val > dashboardChartId) {
			dashboardChartId = val;
		}
	}

	private int loadIds(String name) throws Exception {
		XMLDeclaration xmlDelaration = XMLFileConstants.XMLDeclaration.valueOf(name);
		Document document = utilities.XMLUtil.loadDocument(xmlDelaration.getXmlName());
		String primeryKey = xmlDelaration.getPrimaryKeys().get(0).toString();
		String sqlQueryString = "select MAX(" + primeryKey + ") FROM " + name + ";";
		String query = "number(//" + xmlDelaration.getXmlName() + "/" + xmlDelaration.getBaseNodee() + "/@" + primeryKey
				+ "[not(. < //" + xmlDelaration.getXmlName() + "/" + xmlDelaration.getBaseNodee() + "/@" + primeryKey
				+ ")][1])";
//		int orderProcessId = -1;
//		if(name.equals("orderprocess")) {
//			CassandraDataFetcher cassandraDataFetcher = new CassandraDataFetcher();
//			com.datastax.driver.core.ResultSet resultSet = cassandraDataFetcher.getSession().execute(sqlQueryString);
//			orderProcessId = resultSet.one().getInt(0);
//		}
		int max = Math.max((utilities.XMLUtil.getXMLMaxId(document, query)), (getSQLMaxID(sqlQueryString)));
		return max;
	}

	private Connection MySQLConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "Yoga@11ark");
	}

	public int getSQLMaxID(String query) throws Exception {
		try {
			Connection connection = MySQLConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		}
		return 0;
	}
}
