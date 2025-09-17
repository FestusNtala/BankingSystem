package bankingsystem.model;

public class InvestmentAccount extends Account {
    public static final double MIN_OPENING_BALANCE = 500.00;
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    
    public InvestmentAccount(String accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);
        if (balance < MIN_OPENING_BALANCE) {
            throw new IllegalArgumentException("Investment account requires minimum opening balance of BWP" + 
                                              String.format("%.2f", MIN_OPENING_BALANCE));
        }
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
        return balance * INTEREST_RATE;
    }
}