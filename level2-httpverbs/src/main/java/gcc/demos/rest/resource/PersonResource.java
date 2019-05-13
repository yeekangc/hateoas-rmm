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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import gcc.demos.rest.repository.PersonRepository;
import gcc.demos.rest.model.Address;
import gcc.demos.rest.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;


@Path("/person")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    //@Inject
    PersonRepository personRepo = new PersonRepository();

    @GET
    @Path("/")
    public List<Person> people() {
        return personRepo.allPeople();
    }

    @GET
    @Path("/{id}")
    public Person personWithId(@PathParam("id") String id) {
    	Optional<Person> person = Optional.ofNullable(personRepo.personWithId(id));
    	return person.orElseThrow(() -> new NotFoundException("Person does not exist."));
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
    public List<Person> contains(@QueryParam("lastName") String lastName) {
    	List<Person> matches = personRepo.personWithLastName(lastName);
    	if (matches.isEmpty())
    		throw new NotFoundException("No people with matching lastName");
    	else
    		return matches;
    }
    
    @POST
    @Path("/")
    public Person createPerson(Person person) {
    	if ("".equals(person.getId()))
    		return personRepo.save(person);
    	else
    		throw new BadRequestException("Person Id provided on create request.");
    }
    
    @PUT
    @Path("/{personId}")
    public Person updatePerson(@PathParam("personId") String personId, Person person) {
    	person.setId(personId);
    	Optional<Person> optPerson = Optional.ofNullable(personRepo.update(person));
    	return optPerson.orElseThrow(() -> new NotFoundException("Person does not exist."));
    }    

    
    @DELETE
    @Path("/{personId}")
    public void deletePerson(@PathParam("personId") String personId) {
    	Person person = personRepo.delete(personId);
    	if (person == null)
    		throw new NotFoundException("Person does not exist.");
    }
    

    
}