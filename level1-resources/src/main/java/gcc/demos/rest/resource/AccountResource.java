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
public class AccountResource {

    //@Inject
    AccountRepository accountRepo = new AccountRepository();

 
    
    @POST
    @Path("/")
    public Account personRequests(AccountRequest request) {

        Account response=null;
        switch(request.getMethod()) {
            case "getAccount": {
                response = accountRepo.accountWithId(request.getAccount().getId());
            }
        }
        return response;
    }
}