package gcc.demos.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import gcc.demos.rest.repository.AccountRepository;
import gcc.demos.rest.model.Address;

import java.util.List;
import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;


@Path("/address")
@RequestScoped
public class AddressResource {

    //@Inject
    AccountRepository accountRepo = new AccountRepository();

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Address> addresses() {
        return accountRepo.allAddresses();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Address addressWithId(@PathParam("id") String id) {
        return accountRepo.addressWithId(id);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Address createAddress(Address address) {
    	return accountRepo.save(address);
    }

}