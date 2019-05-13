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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import gcc.demos.rest.repository.PersonRepository;
import gcc.demos.rest.model.Address;
import gcc.demos.rest.model.Person;
import gcc.demos.rest.model.PersonWithLinks;
import gcc.demos.rest.model.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;


@Path("/person")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    //@Inject
    PersonRepository personRepo = new PersonRepository();

    // Inject context
    @Context UriInfo ctxt;

    @GET
    @Path("/")
    public List<PersonWithLinks> people() {
        List<PersonWithLinks> pwls = convert(personRepo.allPeople());

        addSelfLinks(pwls);
        addAddressLinks(pwls);

        return pwls;
    }

    @GET
    @Path("/{id}")
    public PersonWithLinks personWithId(@PathParam("id") String id) {
    	Optional<Person> person = Optional.ofNullable(personRepo.personWithId(id));
        PersonWithLinks pwl = new PersonWithLinks(person.orElseThrow(() -> new NotFoundException("Person does not exist.")));
     
        // Add the HATEOAS links
        addSelfLink(pwl);
        addAddressLink(pwl);
        
        return pwl;
    }
    
    @GET
    @Path("/{id}/address")
    public Address addressOfPersonWithId(@PathParam("id") String id) {
    	Optional<Person> optPerson = Optional.ofNullable(personRepo.personWithId(id));
    	Person person = optPerson.orElseThrow(() -> new NotFoundException("Person does not exist."));
    	
    	Optional<Address> optAddress = Optional.ofNullable(personRepo.addressWithId(person.getAddressId()));
    	return optAddress.orElseThrow(() -> new NotFoundException("Person does not have an address."));
    }    
    
    @GET
    @Path("/filter")
    public List<PersonWithLinks> contains(@QueryParam("lastName") String lastName) {
        List<PersonWithLinks> pwls = convert(personRepo.personWithLastName(lastName));
    	if (pwls.isEmpty())
    		throw new NotFoundException("No people with matching lastName");
    	else {
            addSelfLinks(pwls);
            addAddressLinks(pwls);

            return pwls;
        }
    }
    
    @POST
    @Path("/")
    public PersonWithLinks createPerson(Person person) {
    	if ("".equals(person.getId())) {
            PersonWithLinks pwl = new PersonWithLinks(personRepo.save(person));
            addSelfLink(pwl);
            addAddressLink(pwl);
            return pwl;
        }
    	else
    		throw new BadRequestException("Person Id provided on create request.");
    }
    
    @PUT
    @Path("/{personId}")
    public PersonWithLinks updatePerson(@PathParam("personId") String personId, Person person) {
    	person.setId(personId);
    	Optional<Person> optPerson = Optional.ofNullable(personRepo.update(person));
    	PersonWithLinks pwl = new PersonWithLinks(optPerson.orElseThrow(() -> new NotFoundException("Person does not exist.")));
         
        // Add the HATEOAS links
        addSelfLink(pwl);
        addAddressLink(pwl);
        
        return pwl;
    }    

    
    @DELETE
    @Path("/{personId}")
    public void deletePerson(@PathParam("personId") String personId) {
    	Person person = personRepo.delete(personId);
    	if (person == null)
    		throw new NotFoundException("Person does not exist.");
    }

    private void addSelfLink(PersonWithLinks pwl) {
        try {
            pwl.addLink(new Link("self", new URI(ctxt.getBaseUri() + "person/" + pwl.getId())));
        } catch (URISyntaxException e) {}
    }

    private void addAddressLink(PersonWithLinks pwl) {
        String addressId = pwl.getAddressId();
        if (!"".equals(addressId)) {
            try {
                pwl.addLink(new Link("address", new URI(ctxt.getBaseUri() + "address/" + addressId)));
            } catch (URISyntaxException e) {}
        }
    }

    private void addSelfLinks(List<PersonWithLinks> pwls) {
        for (PersonWithLinks pwl : pwls) {
            addSelfLink(pwl);
        }
    }

    private void addAddressLinks(List<PersonWithLinks> pwls) {
        for (PersonWithLinks pwl : pwls) {
            addAddressLink(pwl);
        }
    }

    private List<PersonWithLinks> convert(List<Person> people) {
        return people.stream()
                     .map(person -> new PersonWithLinks(person))
                     .collect(Collectors.toList());
    }
}