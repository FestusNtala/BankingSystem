package bankingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String firstname;
    private String surname;
    private String address;
    private String customerId;
    private List<Account> accounts;
    
    public Customer(String firstname, String surname, String address, String customerId) {
        this.firstname = firstname;
        this.surname = surname;
        this.address = address;
        this.customerId = customerId;
        this.accounts = new ArrayList<>();
    }
    
    public boolean addAccount(Account account) {
        return accounts.add(account);
    }
    
    public List<Account> getAccounts() {
        return accounts;
    }
    
    public Account getAccountByNumber(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    
    public String getFirstname() { return firstname; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getCustomerId() { return customerId; }
    
    @Override
    public String toString() {
        return "Customer: " + firstname + " " + surname + " (ID: " + customerId + ")";
    }
}