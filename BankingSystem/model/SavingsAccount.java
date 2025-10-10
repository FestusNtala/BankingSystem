package bankingsystem.model;

public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly
    private static final double MINIMUM_BALANCE = 10.00;
    
    public SavingsAccount(String accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);
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
        if (!validateWithdrawal(amount)) {
            return false;
        }
        
        // Check minimum balance requirement
        if ((getBalance() - amount) < MINIMUM_BALANCE) {
            return false;
        }
        
        decreaseBalance(amount);
        return true;
    }
    
    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }
    
    public static double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
    
    public static double getInterestRate() {
        return INTEREST_RATE;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" [Interest Rate: %.4f%%, Min Balance: BWP%.2f]", 
                                              INTEREST_RATE * 100, MINIMUM_BALANCE);
    }
}