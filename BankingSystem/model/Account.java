package bankingsystem.model;

import java.time.LocalDateTime;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected LocalDateTime openingDate;
    
    public Account(String accountNumber, double balance, String branch) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.openingDate = LocalDateTime.now();
    }
    
    public abstract boolean deposit(double amount);
    public abstract boolean withdraw(double amount);
    public abstract double calculateInterest();
    
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public LocalDateTime getOpeningDate() { return openingDate; }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " #" + accountNumber + 
               ": Balance = BWP" + String.format("%.2f", balance);
    }
}