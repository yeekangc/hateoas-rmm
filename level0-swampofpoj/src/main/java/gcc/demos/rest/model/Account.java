package gcc.demos.rest.model;


public class Account {

	private String id = "";
	private String firstName = "";
	private String lastName = "";
	private double balance = 0.0;
	private String currency = "USD";
	private String addressId = "";
	
	public Account(String firstName, String lastName, double balance, String currency) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.balance = balance;
		this.currency = currency;
	}
	
	public Account() {
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}	

	public void setAddressId(String addressId) {
		System.out.println("Setting address id: " + addressId);
		this.addressId = addressId;
	}
	
	public String getAddressId() {
		return addressId;
	}
	
	public String getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public String getCurrency() {
		return currency;
	}	
		

}
