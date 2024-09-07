package components;

public class Order {
	private String address;
	private String phone;
	private int totalAmount;
	private long productId;
	private int totalQuentity;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getTotalQuentity() {
		return totalQuentity;
	}
	public void setTotalQuentity(int totalQuentity) {
		this.totalQuentity = totalQuentity;
	}
	public Order(String address, String phone, int totalAmount, long productId, int totalQuentity) {
		this.address = address;
		this.phone = phone;
		this.totalAmount = totalAmount;
		this.productId = productId;
		this.totalQuentity = totalQuentity;
	}
}
