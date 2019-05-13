package gcc.demos.rest.model;


public class Person {

	private String id = "";
	private String firstName = "";
	private String lastName = "";
	private String dob = null;
	private String addressId = "";
	
	public Person(String firstName, String lastName, String dob) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
	}
	
	public Person() {
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

	public void setDob(String dob) {
		this.dob = dob;
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
	
	public String getDob() {
		return dob;
	}
		

}
