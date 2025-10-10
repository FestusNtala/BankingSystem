package bankingsystem.ui;

import bankingsystem.model.*;
import bankingsystem.service.Bank;
import java.util.Scanner;
import java.util.List;

public class BankingSystem {
    private Bank bank;
    private Customer currentCustomer;
    private Scanner scanner;
    
    public BankingSystem() {
        bank = new Bank("Botswana National Bank");
        scanner = new Scanner(System.in);
    }
    
    public void createCustomer() {
        System.out.println("\n--- Create New Customer ---");
        System.out.print("Enter first name: ");
        String firstname = scanner.nextLine();
        
        System.out.print("Enter surname: ");
        String surname = scanner.nextLine();
        
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        
        System.out.print("Enter National ID: ");
        String nationalId = scanner.nextLine();
        
        // Check if National ID already exists
        if (bank.nationalIdExists(nationalId)) {
            System.out.println("Error: A customer with this National ID already exists!");
            return;
        }
        
        String customerId = bank.generateCustomerId();
        
        Customer customer = new Customer(firstname, surname, address, customerId, nationalId);
        boolean success = bank.addCustomer(customer);
        
        if (success) {
            currentCustomer = customer;
            System.out.println("Customer created successfully! Welcome " + firstname + " " + surname);
            System.out.println("Your Customer ID is: " + customerId);
            System.out.println("Your National ID is: " + nationalId);
            System.out.println("Please remember your National ID for future logins.");
        } else {
            System.out.println("Failed to create customer. National ID might already exist.");
        }
    }
    
    public void findExistingCustomer() {
        System.out.println("\n--- Customer Login ---");
        System.out.print("Enter your National ID: ");
        String nationalId = scanner.nextLine();
        
        Customer customer = bank.findCustomerByNationalId(nationalId);
        if (customer == null) {
            System.out.println("Customer not found! Please check your National ID or create a new account.");
            return;
        }
        
        currentCustomer = customer;
        System.out.println("Welcome back " + customer.getFirstname() + " " + customer.getSurname() + "!");
        System.out.println("Customer ID: " + customer.getCustomerId());
    }
    
    public void openAccount() {
        if (currentCustomer == null) {
            System.out.println("Please first create a customer or log in.");
            return;
        }
        
        System.out.println("\n--- Open New Account ---");
        System.out.println("1. Savings Account");
        System.out.println("2. Investment Account");
        System.out.println("3. Cheque Account");
        
        System.out.print("Select account type (1-3): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
            return;
        }
        
        AccountType accountType = null;
        switch (choice) {
            case 1: accountType = AccountType.SAVINGS; break;
            case 2: accountType = AccountType.INVESTMENT; break;
            case 3: accountType = AccountType.CHEQUE; break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        System.out.print("Enter initial deposit amount: ");
        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount!");
            return;
        }
        
        AccountOpeningRequest.Builder requestBuilder = new AccountOpeningRequest.Builder(
            currentCustomer.getCustomerId(), accountType, initialDeposit
        ).branch("Main");
        
        // Additional information for cheque accounts
        if (accountType == AccountType.CHEQUE) {
            System.out.print("Enter employer name: ");
            String employer = scanner.nextLine();
            
            System.out.print("Enter employer address: ");
            String employerAddress = scanner.nextLine();
            
            requestBuilder.employer(employer).employerAddress(employerAddress);
        }
        
        Account account = bank.openAccount(requestBuilder.build());
        
        if (account != null) {
            System.out.println(accountType.getDisplayName() + " opened successfully! Account #: " + account.getAccountNumber());
        } else {
            System.out.println("Failed to open account! Please check the requirements.");
            if (accountType == AccountType.INVESTMENT) {
                System.out.println("Investment accounts require minimum deposit of BWP" + 
                                 InvestmentAccount.getMinOpeningBalance());
            }
        }
    }
    
    public void viewAccounts() {
        if (currentCustomer == null) {
            System.out.println("Please first create a customer or log in.");
            return;
        }
        
        List<Account> accounts = currentCustomer.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("You don't have any accounts yet.");
            return;
        }
        
        System.out.println("\n--- Your Accounts ---");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i));
        }
        System.out.printf("Total Balance: BWP%.2f\n", currentCustomer.getTotalBalance());
    }
    
    public void makeDeposit() {
        if (currentCustomer == null) {
            System.out.println("Please first create a customer or log in.");
            return;
        }
        
        List<Account> accounts = currentCustomer.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("You don't have any accounts yet.");
            return;
        }
        
        System.out.println("\n--- Select Account to Deposit Into ---");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i));
        }
        
        System.out.print("Select account (1-" + accounts.size() + "): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > accounts.size()) {
                System.out.println("Invalid choice!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
            return;
        }
        
        Account account = accounts.get(choice - 1);
        
        System.out.print("Enter deposit amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount!");
            return;
        }
        
        if (account.deposit(amount)) {
            System.out.println("Deposit successful! New balance: BWP" + String.format("%.2f", account.getBalance()));
            bank.saveData(); // Save after transaction
        } else {
            System.out.println("Deposit failed! Please check the amount.");
        }
    }
    
    public void makeWithdrawal() {
        if (currentCustomer == null) {
            System.out.println("Please first create a customer or log in.");
            return;
        }

        List<Account> accounts = currentCustomer.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("You don't have any accounts yet.");
            return;
        }

        System.out.println("\n--- Select Account to Withdraw From ---");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i));
        }

        System.out.print("Select account (1-" + accounts.size() + "): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > accounts.size()) {
                System.out.println("Invalid choice!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
            return;
        }

        Account account = accounts.get(choice - 1);

        System.out.print("Enter withdrawal amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount!");
            return;
        }

        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful! New balance: BWP" 
                               + String.format("%.2f", account.getBalance()));
            bank.saveData(); // Save after transaction
        } else {
            System.out.println("Withdrawal failed! Check amount and account type.");
            if (account instanceof InvestmentAccount) {
                System.out.println("Investment accounts have a 2% withdrawal penalty.");
            } else if (account instanceof SavingsAccount) {
                System.out.println("Savings accounts require minimum balance of BWP" + 
                                 SavingsAccount.getMinimumBalance());
            }
        }
    }
    
    private void customerMenu() {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Open New Bank Account");
            System.out.println("2. View My Accounts");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Process Monthly Interest");
            System.out.println("6. Logout");

            System.out.print("Enter choice (1-6): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice!");
                continue;
            }

            switch (choice) {
                case 1: openAccount(); break;
                case 2: viewAccounts(); break;
                case 3: makeDeposit(); break;
                case 4: makeWithdrawal(); break;
                case 5: 
                    bank.processMonthlyInterest();
                    System.out.println("Monthly interest processed successfully!");
                    break;
                case 6:
                    currentCustomer = null;
                    System.out.println("You have logged out.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    public void run() {
        System.out.println("Welcome to Botswana National Bank!");

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("Press 1 to Create a New Customer");
            System.out.println("Press 2 to Login with National ID");
            System.out.println("Press 3 to Exit");

            System.out.print("Enter choice (1-3): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    createCustomer();
                    if (currentCustomer != null) {
                        customerMenu();
                    }
                    break;
                case 2:
                    findExistingCustomer();
                    if (currentCustomer != null) {
                        customerMenu();
                    }
                    break;
                case 3:
                    System.out.println("Thank you for banking with us! Goodbye.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please select 1, 2, or 3.");
            }
        }
    }
    
    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        bankingSystem.run();
    }
}