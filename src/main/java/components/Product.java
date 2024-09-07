package components;

import java.util.ArrayList;

import org.w3c.dom.Element;

public class Product {
	private long id;
	private String Name;
	private String Description;
	private String Category;
	private String Actual_price;
	private int Discounts = 0;
	private long Likes = 0;
	private int Available_count;
	private String Product_image;
	public static ArrayList<String> productHeader = new ArrayList<>() {
		{
			add("Name");
			add("Description");
			add("Category");
			add("Actual_price");
			add("Discounts");
			add("Likes");
			add("Available_count");
			add("Product_image");
		}
	};

//	public Product(Element productElement) {
//		if (productElement != null) {
//			this.id = Long.parseLong(productElement.getAttribute("id"));
//			this.Name = productElement.getElementsByTagName("Name").item(0).getTextContent();
//			this.Description = productElement.getElementsByTagName("Description").item(0).getTextContent();
//			this.Category = productElement.getElementsByTagName("Category").item(0).getTextContent();
//			this.Actual_price = productElement.getElementsByTagName("Actual_price").item(0).getTextContent();
//			this.Discounts = Utilities
//					.isValuePresent(productElement.getElementsByTagName("Discounts").item(0).getTextContent())
//							? Integer.parseInt(
//									productElement.getElementsByTagName("Discounts").item(0).getTextContent())
//							: 0;
//			if (productElement.getElementsByTagName("Likes").item(0).getTextContent() != null
//					&& !(productElement.getElementsByTagName("Likes").item(0).getTextContent().isEmpty())) {
//				this.Likes = Long.parseLong(productElement.getElementsByTagName("Likes").item(0).getTextContent());
//			}
//			this.Available_count = Integer
//					.parseInt(productElement.getElementsByTagName("Available_count").item(0).getTextContent());
//			this.Product_image = productElement.getElementsByTagName("Product_image").item(0).getTextContent();
//		}
//	}

	public Product(String name, String description, String category, String actual_price, int discounts, long likes,
			int available_count, String product_image) {
		this.id = -1;
		this.Name = name;
		this.Description = description;
		this.Category = category;
		this.Actual_price = actual_price;
		this.Discounts = discounts;
		this.Likes = likes;
		this.Available_count = available_count;
		this.Product_image = product_image;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public String getActual_price() {
		return Actual_price;
	}

	public void setActual_price(String actual_price) {
		Actual_price = actual_price;
	}

	public int getDiscounts() {
		return Discounts;
	}

	public void setDiscounts(int discounts) {
		Discounts = discounts;
	}

	public long getLikes() {
		return Likes;
	}

	public void setLikes(long likes) {
		Likes = likes;
	}

	public int getAvailable_count() {
		return Available_count;
	}

	public void setAvailable_count(int available_count) {
		Available_count = available_count;
	}

	public String getProduct_image() {
		return Product_image;
	}

	public void setProduct_image(String product_image) {
		Product_image = product_image;
	}
}
