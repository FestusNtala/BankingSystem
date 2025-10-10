package bankingsystem.model;

public enum AccountType {
    SAVINGS("Savings Account", SavingsAccount.class),
    INVESTMENT("Investment Account", InvestmentAccount.class),
    CHEQUE("Cheque Account", ChequeAccount.class);
    
    private final String displayName;
    private final Class<? extends Account> accountClass;
    
    AccountType(String displayName, Class<? extends Account> accountClass) {
        this.displayName = displayName;
        this.accountClass = accountClass;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Class<? extends Account> getAccountClass() {
        return accountClass;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}