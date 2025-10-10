package bankingsystem.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Account implements Serializable {
    private final String accountNumber;
    private double balance;
    private final String branch;
    private final LocalDateTime openingDate;
    
    public Account(String accountNumber, double balance, String branch) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.openingDate = LocalDateTime.now();
    }
    
    // Abstract methods - must be implemented by subclasses
    public abstract boolean deposit(double amount);
    public abstract boolean withdraw(double amount);
    public abstract double calculateInterest();
    
    // Template method for standardized deposit validation
    protected final boolean validateDeposit(double amount) {
        return amount > 0;
    }
    
    // Template method for standardized withdrawal validation
    protected final boolean validateWithdrawal(double amount) {
        return amount > 0 && amount <= balance;
    }
    
    // Protected methods for subclasses to modify balance safely
    protected final void increaseBalance(double amount) {
        this.balance += amount;
    }
    
    protected final void decreaseBalance(double amount) {
        this.balance -= amount;
    }
    
    // Getters - no setters to maintain immutability where appropriate
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public LocalDateTime getOpeningDate() { return openingDate; }
    
    @Override
    public String toString() {
        return String.format("%s #%s: Balance = BWP%.2f (Opened: %s)", 
                           getClass().getSimpleName(), 
                           accountNumber, 
                           balance, 
                           openingDate.toLocalDate());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account account = (Account) obj;
        return accountNumber.equals(account.accountNumber);
    }
    
    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }
}