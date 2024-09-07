package components;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.w3c.dom.Element;

public class Customer {
	private String userName;
	private String password;
	private String name;
	private String lastName;
	private String mobileNumber;
	private Long id;
	public Customer(String userName, String password, String name, String lastName, String mobileNumber) {
		this.userName = userName;
		this.password = password;
		this.name = name;
		if(lastName!=null) {
		this.lastName = lastName;}
		this.mobileNumber = mobileNumber;
	}
	public Customer(Element customerElement) {
		if(customerElement!=null) {
//			this.id=Long.parseLong(customerElement.getAttribute("id"));
			this.id=Long.parseLong(customerElement.getAttribute("userId"));
			this.userName = customerElement.getElementsByTagName("userName").item(0).getTextContent();
			this.password = customerElement.getElementsByTagName("password").item(0).getTextContent();
			this.name = customerElement.getElementsByTagName("name").item(0).getTextContent();
			if(customerElement.getElementsByTagName("lastName").item(0).getTextContent()!=null) {
			this.lastName = customerElement.getElementsByTagName("lastName").item(0).getTextContent();}
			this.mobileNumber = customerElement.getElementsByTagName("phone").item(0).getTextContent();
		}
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
}
