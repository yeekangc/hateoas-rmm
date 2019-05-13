package gcc.demos.rest.resource;

import gcc.demos.rest.model.Person;

public class PersonRequest {

    private String method;
    private Person person;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public Person getPerson() {
        return person;
    }


    public void setPerson(Person person) {
        this.person = person;
    }

}