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

import gcc.demos.rest.repository.AccountRepository;
import gcc.demos.rest.model.Address;
import gcc.demos.rest.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;


@Path("/account")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    //@Inject
    AccountRepository accountRepo = new AccountRepository();

    @GET
    @Path("/")
    public List<Account> accounts() {
        return accountRepo.allAccounts();
    }

    @GET
    @Path("/{id}")
    public Account accountWithId(@PathParam("id") String id) {
    	Optional<Account> account = Optional.ofNullable(accountRepo.accountWithId(id));
    	return account.orElseThrow(() -> new NotFoundException("Account does not exist."));
    }
    
    @GET
    @Path("/{id}/address")
    public Address addressOfAccountWithId(@PathParam("id") String id) {
    	Optional<Account> optAccount = Optional.ofNullable(accountRepo.accountWithId(id));
    	Account account = optAccount.orElseThrow(() -> new NotFoundException("Account does not exist."));
    	
    	Optional<Address> optAddress = Optional.ofNullable(accountRepo.addressWithId(account.getAddressId()));
    	return optAddress.orElseThrow(() -> new NotFoundException("Account does not have an address."));
    }    
    
    @GET
    @Path("/filter")
    public List<Account> contains(@QueryParam("lastName") String lastName) {
    	List<Account> matches = accountRepo.accountWithLastName(lastName);
    	if (matches.isEmpty())
    		throw new NotFoundException("No people with matching lastName");
    	else
    		return matches;
    }
    
    @POST
    @Path("/")
    public Account createAccount(Account account) {
    	if ("".equals(account.getId()))
    		return accountRepo.save(account);
    	else
    		throw new BadRequestException("Account Id provided on create request.");
    }
    
    @PUT
    @Path("/{accountId}")
    public Account updateAccount(@PathParam("accountId") String accountId, Account account) {
    	account.setId(accountId);
    	Optional<Account> optAccount = Optional.ofNullable(accountRepo.update(account));
    	return optAccount.orElseThrow(() -> new NotFoundException("Account does not exist."));
    }    

    
    @DELETE
    @Path("/{accountId}")
    public void deleteAccount(@PathParam("accountId") String accountId) {
    	Account account = accountRepo.delete(accountId);
    	if (account == null)
    		throw new NotFoundException("Account does not exist.");
    }
    

    
}