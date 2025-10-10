package bankingsystem.service;

import bankingsystem.model.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Bank {
    private String name;
    private List<Customer> customers;
    private int accountCounter;
    private int customerCounter;
    private static final String DATA_FILE = "bank_data.ser";
    
    public Bank(String name) {
        this.name = name;
        this.customers = new ArrayList<>();
        this.accountCounter = 1000;
        this.customerCounter = 1000;
        loadData();
    }
    
    // Data persistence methods
    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            if (Files.exists(Paths.get(DATA_FILE))) {
                FileInputStream fileIn = new FileInputStream(DATA_FILE);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                BankData data = (BankData) in.readObject();
                in.close();
                fileIn.close();
                
                this.customers = data.getCustomers();
                this.accountCounter = data.getAccountCounter();
                this.customerCounter = data.getCustomerCounter();
                System.out.println("Data loaded successfully. Customers: " + customers.size());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing data found or error loading data. Starting fresh.");
        }
    }
    
    public void saveData() {
        try {
            BankData data = new BankData(customers, accountCounter, customerCounter);
            FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(data);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    
    // Customer management
    public boolean addCustomer(Customer customer) {
        // Check for duplicate national ID
        if (nationalIdExists(customer.getNationalId())) {
            return false;
        }
        boolean added = customers.add(customer);
        if (added) {
            saveData();
        }
        return added;
    }
    
    public Customer findCustomerByNationalId(String nationalId) {
        return customers.stream()
                .filter(customer -> customer.getNationalId().equals(nationalId))
                .findFirst()
                .orElse(null);
    }
    
    public Customer findCustomer(String customerId) {
        return customers.stream()
                .filter(customer -> customer.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }
    
    public boolean nationalIdExists(String nationalId) {
        return findCustomerByNationalId(nationalId) != null;
    }
    
    // Account management with new DTO pattern
    public Account openAccount(AccountOpeningRequest request) {
        Customer customer = findCustomer(request.getCustomerId());
        if (customer == null) return null;
        
        String accountNumber = generateAccountNumber();
        Account account = createAccount(accountNumber, request);
        
        if (account != null) {
            customer.addAccount(account);
            saveData();
        }
        return account;
    }
    
    private Account createAccount(String accountNumber, AccountOpeningRequest request) {
        switch (request.getAccountType()) {
            case SAVINGS:
                return new SavingsAccount(accountNumber, request.getInitialDeposit(), request.getBranch());
            case INVESTMENT:
                if (request.getInitialDeposit() < InvestmentAccount.getMinOpeningBalance()) {
                    return null;
                }
                return new InvestmentAccount(accountNumber, request.getInitialDeposit(), request.getBranch());
            case CHEQUE:
                return new ChequeAccount(accountNumber, request.getInitialDeposit(), request.getBranch(),
                                       request.getEmployer(), request.getEmployerAddress());
            default:
                return null;
        }
    }
    
    // Legacy methods for backward compatibility
    public Account openSavingsAccount(String customerId, double initialDeposit, String branch) {
        AccountOpeningRequest request = new AccountOpeningRequest.Builder(customerId, AccountType.SAVINGS, initialDeposit)
                .branch(branch)
                .build();
        return openAccount(request);
    }
    
    public Account openInvestmentAccount(String customerId, double initialDeposit, String branch) {
        AccountOpeningRequest request = new AccountOpeningRequest.Builder(customerId, AccountType.INVESTMENT, initialDeposit)
                .branch(branch)
                .build();
        return openAccount(request);
    }
    
    public Account openChequeAccount(String customerId, double initialDeposit, String branch, 
                                   String employer, String employerAddress) {
        AccountOpeningRequest request = new AccountOpeningRequest.Builder(customerId, AccountType.CHEQUE, initialDeposit)
                .branch(branch)
                .employer(employer)
                .employerAddress(employerAddress)
                .build();
        return openAccount(request);
    }
    
    // Utility methods
    public String generateAccountNumber() {
        String accountNumber = "ACC" + (++accountCounter);
        saveData();
        return accountNumber;
    }
    
    public String generateCustomerId() {
        String customerId = "CUST" + (++customerCounter);
        saveData();
        return customerId;
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
        saveData();
    }
    
    // Getters
    public String getName() { return name; }
    public List<Customer> getCustomers() { return customers; }
}

// Data storage class
class BankData implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Customer> customers;
    private int accountCounter;
    private int customerCounter;
    
    public BankData(List<Customer> customers, int accountCounter, int customerCounter) {
        this.customers = customers;
        this.accountCounter = accountCounter;
        this.customerCounter = customerCounter;
    }
    
    public List<Customer> getCustomers() { return customers; }
    public int getAccountCounter() { return accountCounter; }
    public int getCustomerCounter() { return customerCounter; }
}