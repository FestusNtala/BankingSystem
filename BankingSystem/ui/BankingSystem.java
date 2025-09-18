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
        
        String customerId = bank.generateCustomerId();
        
        Customer customer = new Customer(firstname, surname, address, customerId);
        bank.addCustomer(customer);
        currentCustomer = customer;
        System.out.println("Customer created successfully! Welcome " + firstname + " " + surname);
        System.out.println("Your Customer ID is: " + customerId);
    }
    
    public void findExistingCustomer() {
        System.out.println("\n--- Find Existing Customer ---");
        System.out.print("Enter your customer ID: ");
        String customerId = scanner.nextLine();
        
        Customer customer = bank.findCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        
        currentCustomer = customer;
        System.out.println("Welcome back " + customer.getFirstname() + " " + customer.getSurname() + "!");
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
        
        if (choice == 1) {
            System.out.print("Enter initial deposit amount: ");
            double initialDeposit = Double.parseDouble(scanner.nextLine());
            Account account = bank.openSavingsAccount(
                currentCustomer.getCustomerId(), 
                initialDeposit,
                "Main"
            );
            if (account != null) {
                System.out.println("Savings account opened successfully! Account #: " + account.getAccountNumber());
            }
        } else if (choice == 2) {
            System.out.print("Enter initial deposit amount (min BWP500.00): ");
            double initialDeposit = Double.parseDouble(scanner.nextLine());
            Account account = bank.openInvestmentAccount(
                currentCustomer.getCustomerId(), 
                initialDeposit,
                "Main"
            );
            if (account != null) {
                System.out.println("Investment account opened successfully! Account #: " + account.getAccountNumber());
            } else {
                System.out.println("Failed to open investment account! Minimum deposit is BWP500.00");
            }
        } else if (choice == 3) {
            System.out.print("Enter initial deposit amount: ");
            double initialDeposit = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Enter employer name: ");
            String employer = scanner.nextLine();
            
            System.out.print("Enter employer address: ");
            String employerAddress = scanner.nextLine();
            
            Account account = bank.openChequeAccount(
                currentCustomer.getCustomerId(), 
                initialDeposit,
                "Main",
                employer,
                employerAddress
            );
            if (account != null) {
                System.out.println("Cheque account opened successfully! Account #: " + account.getAccountNumber());
            }
        } else {
            System.out.println("Invalid choice!");
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
        
        int choice = Integer.parseInt(scanner.nextLine());
        Account account = accounts.get(choice - 1);
        
        System.out.print("Enter deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        
        if (account.deposit(amount)) {
            System.out.println("Deposit successful! New balance: BWP" + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Deposit failed!");
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

        int choice = Integer.parseInt(scanner.nextLine());
        Account account = accounts.get(choice - 1);

        System.out.print("Enter withdrawal amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful! New balance: BWP" 
                               + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Withdrawal failed! Check amount and account type.");
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
            int choice = Integer.parseInt(scanner.nextLine());

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
            System.out.println("Press 2 if you Already Have a Customer Account");
            System.out.println("Press 3 to Exit");

            System.out.print("Enter choice (1-3): ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    createCustomer();
                    customerMenu();
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