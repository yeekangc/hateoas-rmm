package gcc.demos.rest.resource;

import gcc.demos.rest.model.Account;

public class AccountRequest {

    private String method;
    private Account account;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public Account getAccount() {
        return account;
    }


    public void setAccount(Account account) {
        this.account = account;
    }

}