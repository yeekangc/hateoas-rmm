package gcc.demos.rest.resource;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import gcc.demos.rest.repository.PersonRepository;
import gcc.demos.rest.model.Address;
import gcc.demos.rest.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.enterprise.context.RequestScoped;


@Path("/service")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonService {

    //@Inject
    PersonRepository personRepo = new PersonRepository();
    
    @POST
    @Path("/")
    public Response process(JsonObject request) {

        String method = request.getString("method");

        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        switch(method) {
            case "getPerson": {
                System.out.println("Getting a customer");
                String id = request.getString("id");
                System.out.println("Id= " + id);
                Person person = personRepo.personWithId(id);
                jsonBuilder.add("id", person.getId());
                jsonBuilder.add("firstName", person.getFirstName());
                jsonBuilder.add("lastName", person.getLastName());
                jsonBuilder.add("dob", person.getDob());
                jsonBuilder.add("addressId", person.getAddressId());
                JsonObject json = jsonBuilder.build();
                System.out.println(person.getFirstName());
                System.out.println(json.toString());

                return Response.status(Response.Status.OK).entity(json).type(MediaType.APPLICATION_JSON).build();

            }
            case "getAddress": {
                System.out.println("Getting an address");
                String id = request.getString("id");
                System.out.println("Id= " + id);
                Address address = personRepo.addressWithId(id);
                jsonBuilder.add("id", address.getId());
                jsonBuilder.add("nameOrNumber", address.getNameOrNumber());
                jsonBuilder.add("line2", address.getLine2());
                jsonBuilder.add("street", address.getStreet());
                jsonBuilder.add("state", address.getState());
                jsonBuilder.add("zip", address.getZip());
                jsonBuilder.add("country", address.getCountry());
                JsonObject json = jsonBuilder.build();
                System.out.println(address.getNameOrNumber());
                System.out.println(json.toString());

                return Response.status(Response.Status.OK).entity(json).type(MediaType.APPLICATION_JSON).build();

            }            
        }

        return Response.serverError().build();

    }
    
    
}