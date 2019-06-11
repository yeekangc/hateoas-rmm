package gcc.demos.rest.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

import gcc.demos.rest.model.Address;
import gcc.demos.rest.model.Account;

@ApplicationScoped
public class AccountRepository {
	
	private static final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<String, Account>();
	private static final ConcurrentHashMap<String, Address> addresses = new ConcurrentHashMap<String, Address>();

	private static long accountId = 1;
	private static long addressId = 1;

	// To drive initialization at startup
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

    }
 
    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
	}
	
	private static String newAccountId() {
		return String.valueOf(accountId++);
	}	

	private static String newAddressId() {
		return String.valueOf(addressId++);
	}

	static {
		Account account = new Account("Bob", "Bobson", 100.0, "USD");
		account.setId(newAccountId());
		Address address = new Address("14", "Wibble Road", "", "Hampshire", "14311", "England");
		address.setId(newAddressId());
		addresses.put(address.getId(), address);
		account.setAddressId(address.getId());
		accounts.put(account.getId(), account);
		
		System.out.println("Account id: " + account.getId());
		System.out.println("Address id: " + address.getId());

		
		account = new Account("Jane", "Janely", 0.0, "GBP");
		account.setId(newAccountId());
		address = new Address("The Hovel", "Wobble Road", "", "Old Hampshire", "43354", "Someland");
		address.setId(newAddressId());		
		addresses.put(address.getId(), address);
		account.setAddressId(address.getId());
		accounts.put(account.getId(), account);

		System.out.println("Account id: " + account.getId());
		System.out.println("Address id: " + address.getId());
	}
	
	public List<Account> allAccounts() {
		return new ArrayList<Account>(accounts.values());
	}
	
	public Account save(Account person) {
		if (person.getId() == "")
			person.setId(UUID.randomUUID().toString());
		accounts.put(person.getId(), person);
		return person;
	}
	
	
	public Account update(Account person) {
		if (accountWithId(person.getId()) == null)
			return null;
		
		accounts.put(person.getId(), person);
		return person;
	}
	

	public Account accountWithId(String id) {
		return accounts.values().stream()
				.filter(person -> person.getId().equals(id))
				.findFirst()
				.orElse(null);
	}
	
	public List<Account> accountWithLastName(String lastName) {
		return accounts.values().stream()
				.filter(account -> account.getLastName().equals(lastName))
				.collect(Collectors.toList());
	}

	public Account delete(String acountId) {
		return accounts.remove(accountId);
	}
	
	public List<Address> allAddresses() {
		return new ArrayList<Address>(addresses.values());
	}
	
	public Address addressWithId(String id) {
		System.out.println("looking for id: " + id);
		return addresses.values().stream()
				.filter(address -> address.getId().equals(id))
				.findFirst()
				.orElse(null);
	}
	
	public Address save(Address address) {
		if (address.getId() == "")
			address.setId(newAddressId());
		addresses.put(address.getId(), address);
		return address;
	}
	
}
