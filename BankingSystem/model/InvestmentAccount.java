package bankingsystem.model;

public class InvestmentAccount extends Account {
    public static final double MIN_OPENING_BALANCE = 500.00;
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double WITHDRAWAL_PENALTY_RATE = 0.02; // 2% penalty
    
    public InvestmentAccount(String accountNumber, double balance, String branch) {
        super(accountNumber, Math.max(balance, MIN_OPENING_BALANCE), branch);
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
        
        double penalty = amount * WITHDRAWAL_PENALTY_RATE;
        double totalDeduction = amount + penalty;
        
        if (totalDeduction > getBalance()) {
            return false; // Insufficient funds including penalty
        }
        
        decreaseBalance(totalDeduction);
        return true;
    }
    
    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }
    
    public static double getMinOpeningBalance() {
        return MIN_OPENING_BALANCE;
    }
    
    public static double getInterestRate() {
        return INTEREST_RATE;
    }
    
    public static double getWithdrawalPenaltyRate() {
        return WITHDRAWAL_PENALTY_RATE;
    }
    
    @Override
    public String toString() {
        return super.toString() + 
               String.format(" [Interest Rate: %.2f%%, Min Opening: BWP%.2f, Withdrawal Penalty: %.2f%%]", 
                           INTEREST_RATE * 100, MIN_OPENING_BALANCE, WITHDRAWAL_PENALTY_RATE * 100);
    }
}