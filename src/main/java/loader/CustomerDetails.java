package loader;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import components.Customer;
import components.IdGenerator;

public class CustomerDetails {
	public static ArrayList<Customer> customersList = new ArrayList<>();
	private static Document loadDocument() {
		Document document = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			document = builder.parse(new File("C:\\Users\\kumar\\git\\repository\\ChatApplication\\src\\main\\webapp\\users.xml"));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return document;
	}
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
	public void loadCustomerData() {
		Document document= loadDocument();
		if(document!=null) {
			Element roorElement = document.getDocumentElement();
			NodeList customerList = roorElement.getElementsByTagName("userData");
			
			for(int i=0;i<customerList.getLength();i++) {
				customersList.add(new Customer((Element) customerList.item(i)));
			}
		}
	}
	public void addCustomer(Customer customer) {
		Document document= loadDocument();
		
		Element rootElement = document.getDocumentElement();
		Element userDataElement = document.createElement("userData");
		userDataElement.setAttribute("id",String.valueOf(IdGenerator.generateUserPrimaryId()));
		rootElement.appendChild(userDataElement);
		
		Element userNamElement = document.createElement("userName");
		Text userNameText = document.createTextNode(customer.getUserName());
		userNamElement.appendChild(userNameText);
		userDataElement.appendChild(userNamElement);
		
		Element passwordElement = document.createElement("password");
		Text passwordText = document.createTextNode(customer.getPassword());
		passwordElement.appendChild(passwordText);
		userDataElement.appendChild(passwordElement);
		
		Element nameElement = document.createElement("name");
		Text nameText = document.createTextNode(customer.getName());
		nameElement.appendChild(nameText);
		userDataElement.appendChild(nameElement);
		
		if(customer.getLastName()!=null) {
			Element lastNameElement = document.createElement("lastName");
			Text lastNameText = document.createTextNode(customer.getLastName());
			lastNameElement.appendChild(lastNameText);
			userDataElement.appendChild(lastNameElement);
		}
		
		Element phonElement = document.createElement("phone");
		Text phoneText = document.createTextNode(customer.getMobileNumber());
		phonElement.appendChild(phoneText);
		userDataElement.appendChild(phonElement);
		
		Transformer transformer = loadTransformars();
		if(document!=null && transformer!=null) {
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File("C:\\Users\\kumar\\git\\repository\\ChatApplication\\src\\main\\webapp\\users.xml"));
		try {
			transformer.transform(source, result);
			customersList.add(customer);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public boolean validateCustomer(Customer customer) {
		for(Customer cu:customersList) {
			if(cu.getUserName().equals(customer.getUserName())) {
				return false;
			}
		}
		return true;
	}
	public Customer validateCustomerCredentials(String userName,String password) {
		for(Customer cu:customersList) {
			if(cu.getUserName().equals(userName) && cu.getPassword().equals(password)) {
				return cu;
			}
		}
		return null;
	}
	public static Customer getCustomerDetails(String id) {
		System.out.println(id+"--->"+customersList.size());
		for(Customer cu:customersList) {
		}
		return null;
	}
}
