package bankingsystem.model;

public class ChequeAccount extends Account {
    private String employer;
    private String employerAddress;
    
    public ChequeAccount(String accountNumber, double balance, String branch, 
                        String employer, String employerAddress) {
        super(accountNumber, balance, branch);
        this.employer = employer;
        this.employerAddress = employerAddress;
    }
    
    @Override
    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        balance += amount;
        return true;
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) return false;
        balance -= amount;
        return true;
    }
    
    @Override
    public double calculateInterest() {
        return 0.0; // Cheque accounts typically don't earn interest
    }
    
    public String getEmployer() { return employer; }
    public String getEmployerAddress() { return employerAddress; }
}