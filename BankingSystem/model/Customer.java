package bankingsystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer implements Serializable {
    private final String firstname;
    private final String surname;
    private final String address;
    private final String customerId;
    private final String nationalId;
    private final List<Account> accounts;
    
    public Customer(String firstname, String surname, String address, 
                   String customerId, String nationalId) {
        this.firstname = Objects.requireNonNull(firstname, "First name cannot be null");
        this.surname = Objects.requireNonNull(surname, "Surname cannot be null");
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.nationalId = Objects.requireNonNull(nationalId, "National ID cannot be null");
        this.accounts = new ArrayList<>();
    }
    
    public boolean addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        
        // Check if account already exists
        if (getAccountByNumber(account.getAccountNumber()) != null) {
            return false;
        }
        
        return accounts.add(account);
    }
    
    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts); // Return immutable view
    }
    
    public Account getAccountByNumber(String accountNumber) {
        return accounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }
    
    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }
    
    public int getAccountCount() {
        return accounts.size();
    }
    
    // Getters
    public String getFirstname() { return firstname; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getCustomerId() { return customerId; }
    public String getNationalId() { return nationalId; }
    
    @Override
    public String toString() {
        return String.format("Customer: %s %s (ID: %s, National ID: %s, Accounts: %d, Total Balance: BWP%.2f)",
                           firstname, surname, customerId, nationalId, getAccountCount(), getTotalBalance());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return nationalId.equals(customer.nationalId) && 
               customerId.equals(customer.customerId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nationalId, customerId);
    }
}