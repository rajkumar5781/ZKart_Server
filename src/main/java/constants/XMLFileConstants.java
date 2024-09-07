package constants;

import java.util.ArrayList;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import servlets.Dashboard;
import servlets.LoadAddToCartDetails;

//This file maintains the key value pair based on SQLTableName=XMLFileName
public class XMLFileConstants {
	public final static String PRODUCTS = "productsDetails";
	public final static String CUSTOMERS = "users"; 
	public final static String TEST1 = "test1";
	
	public enum XMLDeclaration{
		productDetails("productDetails",new ArrayList<String>(){{add("id");}},null,"Item"),
		users("users",new ArrayList<String>(){{add("userId");}},null,"userData"),
		TEST1("test1",new ArrayList<String>(){{add("id");}},null,"test"),
		cartDetails("AddToCardXML",new ArrayList<String>(){{add("id");}} ,null,"Card"),
		OrderDetails("BuyingOrder",new ArrayList<String>(){{add("id");}},new ArrayList<String>(){{add("CustomerId");add("ProductId");}},"order"),
		ReviewDetails("ReviewDetails",new ArrayList<String>(){{add("id");}},new ArrayList<String>(){{add("CustomerId");add("ProductId");}},"Review"),
		orderedProductDetails("orderedProductDetails",new ArrayList<String>(){{add("id");}},new ArrayList<String>(){{add("orderId");}},"orderDetails"),
		user_address("user_address",new ArrayList<String>(){{add("id");}},new ArrayList<String>(){{add("customerId");}},"address"),
		orderprocess("orderprocess",new ArrayList<String>() {{ add("id"); }},new ArrayList<String>(){{add("orderid");}},"process"),
		reports("reports",new ArrayList<String>() {{ add("id"); }},null,"report"),
		folder("folders",new ArrayList<String>() {{ add("id"); }},null,"folder"),
		dashboard("dashboards",new ArrayList<String>() {{ add("id"); }},new ArrayList<String>(){{add("folderId");}},"dashboard"),
		dashboardcharts("dashboardcharts",new ArrayList<String>() {{ add("id"); }},new ArrayList<String>(){{add("dashboardId");add("reportId");}},"dashboardchart");
		private String xmlName;
		private ArrayList<String> primaryKeys;
		private ArrayList<String> foreignKeys;
		private String baseNode;
		XMLDeclaration(String xmlName,ArrayList<String> primaryKey,ArrayList<String> foreignKey,String baseNode) {
			this.xmlName = xmlName;
			this.primaryKeys = primaryKey;
			this.foreignKeys = foreignKey;
			this.baseNode = baseNode;
		}
		public String getXmlName() {
			return xmlName;
		}
		public ArrayList<String> getPrimaryKeys() {
			return primaryKeys;
		}
		public ArrayList<String> getForeignKeys() {
			return foreignKeys;
		}
		public String getBaseNodee() {
			return baseNode;
		}
	}
}
