package bankingsystem.model;

import java.util.Objects;

public class AccountOpeningRequest {
    private final String customerId;
    private final AccountType accountType;
    private final double initialDeposit;
    private final String branch;
    private final String employer; // For cheque accounts
    private final String employerAddress; // For cheque accounts
    
    // Private constructor - use builder pattern
    private AccountOpeningRequest(Builder builder) {
        this.customerId = builder.customerId;
        this.accountType = builder.accountType;
        this.initialDeposit = builder.initialDeposit;
        this.branch = builder.branch;
        this.employer = builder.employer;
        this.employerAddress = builder.employerAddress;
    }
    
    // Builder pattern for complex object creation
    public static class Builder {
        private final String customerId;
        private final AccountType accountType;
        private final double initialDeposit;
        private String branch = "Main";
        private String employer = "";
        private String employerAddress = "";
        
        public Builder(String customerId, AccountType accountType, double initialDeposit) {
            this.customerId = Objects.requireNonNull(customerId);
            this.accountType = Objects.requireNonNull(accountType);
            this.initialDeposit = initialDeposit;
        }
        
        public Builder branch(String branch) {
            this.branch = branch;
            return this;
        }
        
        public Builder employer(String employer) {
            this.employer = employer;
            return this;
        }
        
        public Builder employerAddress(String employerAddress) {
            this.employerAddress = employerAddress;
            return this;
        }
        
        public AccountOpeningRequest build() {
            return new AccountOpeningRequest(this);
        }
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public AccountType getAccountType() { return accountType; }
    public double getInitialDeposit() { return initialDeposit; }
    public String getBranch() { return branch; }
    public String getEmployer() { return employer; }
    public String getEmployerAddress() { return employerAddress; }
}