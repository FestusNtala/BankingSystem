package bankingsystem.service;

import bankingsystem.model.*;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private List<Customer> customers;
    private int accountCounter;
    private int customerCounter;
    
    public Bank(String name) {
        this.name = name;
        this.customers = new ArrayList<>();
        this.accountCounter = 1000;
        this.customerCounter = 1000;
    }
    
    public boolean addCustomer(Customer customer) {
        return customers.add(customer);
    }
    
    public Customer findCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }
    
    public String generateAccountNumber() {
        return "ACC" + (++accountCounter);
    }
    
    public String generateCustomerId() {
        return "CUST" + (++customerCounter);
    }
    
    public Account openSavingsAccount(String customerId, double initialDeposit, String branch) {
        Customer customer = findCustomer(customerId);
        if (customer == null) return null;
        
        String accountNumber = generateAccountNumber();
        Account account = new SavingsAccount(accountNumber, initialDeposit, branch);
        customer.addAccount(account);
        return account;
    }
    
    public Account openInvestmentAccount(String customerId, double initialDeposit, String branch) {
        if (initialDeposit < InvestmentAccount.MIN_OPENING_BALANCE) {
            return null;
        }
        
        Customer customer = findCustomer(customerId);
        if (customer == null) return null;
        
        String accountNumber = generateAccountNumber();
        Account account = new InvestmentAccount(accountNumber, initialDeposit, branch);
        customer.addAccount(account);
        return account;
    }
    
    public Account openChequeAccount(String customerId, double initialDeposit, String branch, 
                                   String employer, String employerAddress) {
        Customer customer = findCustomer(customerId);
        if (customer == null) return null;
        
        String accountNumber = generateAccountNumber();
        Account account = new ChequeAccount(accountNumber, initialDeposit, branch, employer, employerAddress);
        customer.addAccount(account);
        return account;
    }
    
    public void processMonthlyInterest() {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                double interest = account.calculateInterest();
                if (interest > 0) {
                    account.deposit(interest);
                }
            }
        }
    }
    
    public String getName() { return name; }
    public List<Customer> getCustomers() { return customers; }
}