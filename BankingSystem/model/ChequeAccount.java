package bankingsystem.model;

public class ChequeAccount extends Account {
    private final String employer;
    private final String employerAddress;
    private static final double OVERDRAFT_LIMIT = 10000.00;
    private static final double MONTHLY_FEE = 50.00;
    
    public ChequeAccount(String accountNumber, double balance, String branch, 
                        String employer, String employerAddress) {
        super(accountNumber, balance, branch);
        this.employer = employer;
        this.employerAddress = employerAddress;
    }
    
    @Override
    public boolean deposit(double amount) {
        if (!validateDeposit(amount)) {
            return false;
        }
        increaseBalance(amount);
        return true;
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        
        // Cheque accounts allow overdraft up to a limit
        double availableBalance = getBalance() + OVERDRAFT_LIMIT;
        if (amount > availableBalance) {
            return false;
        }
        
        decreaseBalance(amount);
        return true;
    }
    
    @Override
    public double calculateInterest() {
        // Cheque accounts typically don't earn interest
        return 0.0;
    }
    
    public void applyMonthlyFee() {
        if (getBalance() >= MONTHLY_FEE) {
            decreaseBalance(MONTHLY_FEE);
        }
        // If balance is insufficient, fee accumulates (could be handled differently)
    }
    
    // Getters
    public String getEmployer() { return employer; }
    public String getEmployerAddress() { return employerAddress; }
    public static double getOverdraftLimit() { return OVERDRAFT_LIMIT; }
    public static double getMonthlyFee() { return MONTHLY_FEE; }
    
    @Override
    public String toString() {
        return super.toString() + 
               String.format(" [Overdraft Limit: BWP%.2f, Monthly Fee: BWP%.2f, Employer: %s]", 
                           OVERDRAFT_LIMIT, MONTHLY_FEE, employer);
    }
}