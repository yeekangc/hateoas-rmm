package gcc.demos.rest.resource;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import gcc.demos.rest.repository.AccountRepository;
import gcc.demos.rest.model.Address;
import gcc.demos.rest.model.AddressWithLinks;
import gcc.demos.rest.model.Link;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;



@Path("/address")
@RequestScoped
public class AddressResource {

    //@Inject
    AccountRepository accountRepo = new AccountRepository();

    // Inject context
    @Context UriInfo ctxt;
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AddressWithLinks> addresses() {
        List<AddressWithLinks> awls = convert(accountRepo.allAddresses());
        addAllLinks(awls);
        return awls;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AddressWithLinks addressWithId(@PathParam("id") String id) {
    	Optional<Address> address = Optional.ofNullable(accountRepo.addressWithId(id));
        AddressWithLinks awl = new AddressWithLinks(address.orElseThrow(() -> new NotFoundException("Address does not exist.")));
     
        addAllLinks(awl);
        
        return awl;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Address createAddress(Address address) {
    	if ("".equals(address.getId())) {
            AddressWithLinks awl = new AddressWithLinks(accountRepo.save(address));
            addAllLinks(awl);

            return awl;
        }
    	else
    		throw new BadRequestException("Account Id provided on create request.");
    }


    private void addSelfLink(AddressWithLinks awl) {
        try {
            awl.addLink(new Link("self", new URI(ctxt.getBaseUri() + "account/" + awl.getId())));
        } catch (URISyntaxException e) {}
    }

    private void addAllLinks(AddressWithLinks awl) {
        addSelfLink(awl);
    }    

    private void addAllLinks(List<AddressWithLinks> awls) {
        for (AddressWithLinks awl : awls) {
            addAllLinks(awl);
        }
    }

    private List<AddressWithLinks> convert(List<Address> addresses) {
        return addresses.stream()
                        .map(address -> new AddressWithLinks(address))
                        .collect(Collectors.toList());
    }
}