package gcc.demos.rest.resource;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAllowedException;
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

import gcc.demos.rest.repository.AccountRepository;
import gcc.demos.rest.model.Address;
import gcc.demos.rest.model.Account;
import gcc.demos.rest.model.AccountWithLinks;
import gcc.demos.rest.model.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;


@Path("/account")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    //@Inject
    AccountRepository accountRepo = new AccountRepository();

    // Inject context
    @Context UriInfo ctxt;

    @GET
    @Path("/")
    public List<AccountWithLinks> accounts() {
        List<AccountWithLinks> awls = convert(accountRepo.allAccounts());

        addAllLinks(awls);

        return awls;
    }

    @GET
    @Path("/{id}")
    public AccountWithLinks accountWithId(@PathParam("id") String id) {
    	Optional<Account> account = Optional.ofNullable(accountRepo.accountWithId(id));
        AccountWithLinks awl = new AccountWithLinks(account.orElseThrow(() -> new NotFoundException("Account does not exist.")));
     
        addAllLinks(awl);
        
        return awl;
    }
    
    @GET
    @Path("/{id}/address")
    public Address addressOfAccountWithId(@PathParam("id") String id) {
    	Optional<Account> optAccount = Optional.ofNullable(accountRepo.accountWithId(id));
    	Account account = optAccount.orElseThrow(() -> new NotFoundException("Account does not exist."));
    	
    	Optional<Address> optAddress = Optional.ofNullable(accountRepo.addressWithId(account.getAddressId()));
    	return optAddress.orElseThrow(() -> new NotFoundException("Account does not have an address."));
    }    
       
    @PUT
    @Path("/{id}/deposit/{amount}")
    public Account deposit(@PathParam("id") String id, @PathParam("amount") double amount) {
    	Optional<Account> optAccount = Optional.ofNullable(accountRepo.accountWithId(id));
    	Account account = optAccount.orElseThrow(() -> new NotFoundException("Account does not exist."));
        
        account.setBalance(account.getBalance() + amount);
    	return updateAccount(id, account);
    } 

    @PUT
    @Path("/{id}/withdraw/{amount}")
    public Account withdraw(@PathParam("id") String id, @PathParam("amount") double amount) {
    	Optional<Account> optAccount = Optional.ofNullable(accountRepo.accountWithId(id));
    	Account account = optAccount.orElseThrow(() -> new NotFoundException("Account does not exist."));
        
        if (account.getBalance() < amount)
            throw new NotAllowedException("Insufficient funds.");

        account.setBalance(account.getBalance() - amount);
    	return updateAccount(id, account);
    }    

    @GET
    @Path("/filter")
    public List<AccountWithLinks> contains(@QueryParam("lastName") String lastName) {
        List<AccountWithLinks> awls = convert(accountRepo.accountWithLastName(lastName));
    	if (awls.isEmpty())
    		throw new NotFoundException("No accounts with matching lastName");
    	else {
            addAllLinks(awls);


            return awls;
        }
    }
    
    @POST
    @Path("/")
    public AccountWithLinks createAccount(Account account) {
    	if ("".equals(account.getId())) {
            AccountWithLinks awl = new AccountWithLinks(accountRepo.save(account));
            addAllLinks(awl);

            return awl;
        }
    	else
    		throw new BadRequestException("Account Id provided on create request.");
    }
    
    @PUT
    @Path("/{accountId}")
    public AccountWithLinks updateAccount(@PathParam("accountId") String accountId, Account account) {
    	account.setId(accountId);
        Optional<Account> optAccount = Optional.ofNullable(accountRepo.update(account));
        AccountWithLinks awl = new AccountWithLinks(optAccount.orElseThrow(() -> new NotFoundException("Account does not exist.")));
         
        addAllLinks(awl);
        
        return awl;
    }    
    
    @DELETE
    @Path("/{accountId}")
    public void deleteAccount(@PathParam("accountId") String accountId) {
    	Account account = accountRepo.delete(accountId);
    	if (account == null)
    		throw new NotFoundException("Account does not exist.");
    }

    private void addSelfLink(AccountWithLinks awl) {
        try {
            awl.addLink(new Link("self", new URI(ctxt.getBaseUri() + "account/" + awl.getId())));
        } catch (URISyntaxException e) {}
    }

    private void addDepositLink(AccountWithLinks awl) {
        try {
            awl.addLink(new Link("deposit", new URI(ctxt.getBaseUri() + "account/" + awl.getId() + "/deposit")));
        } catch (URISyntaxException e) {}
    }

    private void addWithdrawLink(AccountWithLinks awl) {
        if (awl.getBalance() > 0) {
            try {
                awl.addLink(new Link("withdraw", new URI(ctxt.getBaseUri() + "account/" + awl.getId() + "/withdraw")));
            } catch (URISyntaxException e) {}
        }
    }

    private void addAddressLink(AccountWithLinks awl) {
        String addressId = awl.getAddressId();
        if (!"".equals(addressId)) {
            try {
                awl.addLink(new Link("address", new URI(ctxt.getBaseUri() + "address/" + addressId)));
            } catch (URISyntaxException e) {}
        }
    }

    private void addAllLinks(AccountWithLinks awl) {
        addSelfLink(awl);
        addDepositLink(awl);
        addWithdrawLink(awl);
        addAddressLink(awl);
    }    

    private void addAllLinks(List<AccountWithLinks> awls) {
        for (AccountWithLinks awl : awls) {
            addAllLinks(awl);
        }
    }

    private List<AccountWithLinks> convert(List<Account> accounts) {
        return accounts.stream()
                     .map(account -> new AccountWithLinks(account))
                     .collect(Collectors.toList());
    }
}