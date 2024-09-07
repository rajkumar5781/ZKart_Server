package utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import constants.Constants;
import constants.XMLFileConstants.XMLDeclaration;
import fetcher.DocumentHolder;
import jakarta.servlet.http.HttpServletRequest;

public class XMLUtil {
	public static Element createChildElement(Document document,XMLDeclaration xmlEnum, HashMap<String, Object> columnValues) {
		Element rootElement = document.getDocumentElement();
		Element childElement = document.createElement(xmlEnum.getBaseNodee());
		for(Map.Entry columnEntry : columnValues.entrySet()) {
			if((xmlEnum.getForeignKeys()!=null && xmlEnum.getForeignKeys().contains(columnEntry.getKey())) || (xmlEnum.getPrimaryKeys()!=null && xmlEnum.getPrimaryKeys().contains(columnEntry.getKey()))) {
				childElement.setAttribute((String) columnEntry.getKey(),  String.valueOf(columnEntry.getValue()));
			}
			else {
//				Element element = (Element) document.createElement((String) columnEntry.getKey()).appendChild(document.createTextNode((String) columnEntry.getValue()));
				Element innerElement = document.createElement((String) columnEntry.getKey());
				Text innerText = document.createTextNode(String.valueOf(columnEntry.getValue()));
				innerElement.appendChild(innerText);
				childElement.appendChild(innerElement);
			}
		}
		return childElement;
	}
	public static Transformer loadTransformars() {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		 Transformer transformer = null;
		 try {
		transformer = transformerFactory.newTransformer();
		} catch (Exception e) {
			// TODO: handle exception
		}
		 return transformer;
	}
	
	public static int getXMLMaxId(Document document,String query) {
		if (document != null) {
			document.getDocumentElement().normalize();
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			try {
				Double double1 = (Double) (xpath.evaluate(query, document, XPathConstants.NUMBER));
				return ((Double) double1).intValue();

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	
	public static Document loadDocument(String name) {
		Document document = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			document = builder.parse(new File(Constants.XML_ROOT_PATH + name + ".xml"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return document;
	}
	
	public static ArrayList<String> getXMLTagNames(XMLDeclaration xmlDeclaration) throws XPathExpressionException{
		
		Document doc = loadDocument(xmlDeclaration.getXmlName());
		 XPath xpath = XPathFactory.newInstance().newXPath();
		 String queryString = "//" + xmlDeclaration.getXmlName() + "/" + xmlDeclaration.getBaseNodee() + "/*";
         XPathExpression expr = xpath.compile(queryString);

         // Evaluate the XPath expression
         NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
         ArrayList<String> xmlTagNames = new ArrayList<>(); 
         for (int i = 0; i < nodeList.getLength(); i++) {
             Element element = (Element) nodeList.item(i);
             String attributeName = element.getNodeValue();
             xmlTagNames.add(attributeName);
             System.out.println(attributeName);
         }
		return null;
	}
	
	public static JSONArray getFilterFieldDetails(String tableName) {
		JSONArray result = new JSONArray();
		Document document = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			document = builder.parse(DocumentHolder.class.getResourceAsStream("/xmldata/metadata.xml"));
			document.getDocumentElement().normalize();
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			String query = String.format("/tables/table[@name='%s']", tableName);
			NodeList nodeList = (NodeList) xpath.evaluate(query, document, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node itemNode = nodeList.item(i);
				if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
					Element itemElement = (Element) itemNode;
					NodeList childNodes = itemElement.getChildNodes();
					for (int j = 0; j < childNodes.getLength(); j++) {
						Node childNode = childNodes.item(j);
						if (childNode.getNodeType() == Node.ELEMENT_NODE) {
							String columnnameString="";
							if(childNode.getAttributes().getNamedItem("toshow")!=null) {
							columnnameString = childNode.getAttributes().getNamedItem("toshow").getNodeValue();
							}
							if(columnnameString.equals("1")) {
							JSONObject field_Object = new JSONObject();
							field_Object.put("name", childNode.getAttributes().getNamedItem("name").getNodeValue());
							field_Object.put("type", childNode.getAttributes().getNamedItem("datatype").getNodeValue());
							field_Object.put("dv", Constants.COLUMN_DV.get(childNode.getAttributes().getNamedItem("name").getNodeValue()));
							field_Object.put("ui_type", Constants.COLUMN_UI_TYPE.get(childNode.getAttributes().getNamedItem("name").getNodeValue()));
							if(Constants.COLUMN_OPTIONS.containsKey(childNode.getAttributes().getNamedItem("name").getNodeValue())) {
								field_Object.put("options",Constants.COLUMN_OPTIONS.get(childNode.getAttributes().getNamedItem("name").getNodeValue()));
							}
							result.put(field_Object);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.print(e);
		}
		return result;
	}
	
	public static String getWhereXMLQueryString(HttpServletRequest request) {
		String queryString = "[";
		for(Map.Entry rEntry : request.getParameterMap().entrySet()) {
			if(!((String)rEntry.getKey()).equals("searchWord") && !((String)rEntry.getKey()).equals("pageNumber")) {
			if(Constants.COLUMN_UI_TYPE.get((String)rEntry.getKey()).equals("range")) {
				 List<String> stringList = Arrays.asList(((String[]) rEntry.getValue())[0].split(","));
			        List<Integer> values = stringList.stream()
			                                          .map(Integer::parseInt)
			                                          .collect(Collectors.toList());
				if(values.size()>1) {
					if(queryString.length()>1) {
						queryString=queryString+" and "+" ";
					}
				queryString=queryString+rEntry.getKey() + " >= "+ values.get(0) +" "+" and "+" "+rEntry.getKey()+" <= "+values.get(1);
				}
			}
			else {
				if(queryString.length()>1) {
					queryString=queryString+" and "+" ";
				}
				queryString=queryString+rEntry.getKey() + "  = ";
				if(Constants.COLUMN_DATATYPE.get(rEntry.getKey()).equals("varchar")) {
					queryString=queryString+"'"+((String[]) rEntry.getValue())[0] +"'";
				}
				else {
					queryString=queryString+((String[]) rEntry.getValue())[0];
				}
			}
			}
		}
		queryString = queryString+"]";
		
		if(queryString.length()==2){
			queryString="";
		}
		return queryString;
	}
	
}
