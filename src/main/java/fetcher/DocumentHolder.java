package fetcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.util.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import connectors.XMLConnector;
import constants.Constants;
import constants.XMLFileConstants;
import constants.XMLFileConstants.XMLDeclaration;

public class DocumentHolder {
	private DocumentBuilder documentBuilder;
	private Document document;
	private XMLDeclaration xmlDelaration;
	private Transformer transformer;

	public DocumentHolder() throws Exception {
		documentBuilder = (DocumentBuilder) new XMLConnector().connect();
	}

	public void connect(String xmlName) throws SAXException, IOException, Exception {
		xmlDelaration = XMLFileConstants.XMLDeclaration.valueOf(xmlName);
		String string = Constants.XML_ROOT_PATH + (xmlDelaration.getXmlName()) + ".xml";
		document = documentBuilder.parse(new File(string));
	}

	public void addRecord(HashMap<String, Object> columnValues) {
		document.getDocumentElement()
				.appendChild(utilities.XMLUtil.createChildElement(document, xmlDelaration, columnValues));
	}

	public boolean updateDocument() throws Exception {
		try {
		loadTransformers();
		String string = Constants.XML_ROOT_PATH + (xmlDelaration.getXmlName()) + ".xml";
		transformer.transform(new DOMSource(document),
				new StreamResult(new File(string)));
		return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	private void loadTransformers() throws Exception {
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (Exception e) {
			System.out.print("Transformer exc " + e.getMessage());
			throw e;
		}
	}

	public JSONArray getRecordsJSON() throws SQLException {

		NodeList nodeList = document.getDocumentElement().getElementsByTagName(xmlDelaration.getBaseNodee());
		JSONArray jsonList = new JSONArray();

		for (int i = 0; i < nodeList.getLength(); i++) {
			JSONObject item = new JSONObject();
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element DataElement = (Element) nodeList.item(i);
				for (int k = 0; k < DataElement.getAttributes().getLength(); k++) {
					item.put(DataElement.getAttributes().item(k).getNodeName(),
							DataElement.getAttributes().item(k).getNodeValue());
				}
				NodeList childNodes = DataElement.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node childNode = childNodes.item(j);
					if (childNode.getNodeType() == Node.ELEMENT_NODE) {
						item.put(childNode.getNodeName(), childNode.getTextContent());
					}
				}
			}
			jsonList.put(item);
		}
		return jsonList;
	}

	public NodeList executeSelectQuery(String query) throws XPathExpressionException {
		document.getDocumentElement().normalize();
		// Get XPath
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();

		NodeList nodeList = (NodeList) xpath.evaluate(query, document, XPathConstants.NODESET);
		return nodeList;
	}
	
	public JSONArray jsonValues(String query) throws XPathExpressionException {
		NodeList nodeList = (NodeList) executeSelectQuery(query);
		JSONArray resultSet = new JSONArray();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node itemNode = nodeList.item(i);
			if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
				Element itemElement = (Element) itemNode;

				NamedNodeMap attributes = itemElement.getAttributes();
				JSONObject jsonRow = new JSONObject();
				for (int k = 0; k < attributes.getLength(); k++) {
					Node attribute = attributes.item(k);
					jsonRow.put(attribute.getNodeName(), attribute.getNodeValue());
				}

				NodeList childNodes = itemElement.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node childNode = childNodes.item(j);
					if (childNode.getNodeType() == Node.ELEMENT_NODE) {
						jsonRow.put(childNode.getNodeName(), childNode.getTextContent());
					}
				}
				resultSet.put(jsonRow);
			}
		}

		return resultSet;
	}

	public int totalRecordCount(String query) throws XPathExpressionException {
		NodeList nodeList = (NodeList) executeSelectQuery(query);
		return nodeList.getLength();
	}
}
