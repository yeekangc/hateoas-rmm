package gcc.demos.rest.model;

public class Address {

	private String id = "";
	private String nameOrNumber = "";
	private String street = "";
	private String line2 = "";
	private String state = "";
	private String zip = "";
	private String country = "";
	
	public Address(String nameOrNumber, String street, String line2, String state, String zip, String country) {
		this.nameOrNumber = nameOrNumber;
		this.street = street;
		this.line2 = line2;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}
	
	public Address() {
		
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setNameOrNumber(String nameOrNumber) {
		this.nameOrNumber = nameOrNumber;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNameOrNumber() {
		return nameOrNumber;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getLine2() {
		return line2;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZip() {
		return zip;
	}
	
	public String getCountry() {
		return country;
	}
}
