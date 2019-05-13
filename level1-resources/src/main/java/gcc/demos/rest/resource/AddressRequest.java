package gcc.demos.rest.resource;

import gcc.demos.rest.model.Address;

public class AddressRequest {

    private String method;
    private Address address;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public Address getAddress() {
        return address;
    }


    public void setAddress(Address address) {
        this.address = address;
    }

}