cd BankingSystem

Compile model classes first
javac -d . model/AccountType.java javac -d . model/Account.java javac -d . model/UserCredentials.java javac -d . model/AccountOpeningRequest.java javac -d . model/SavingsAccount.java javac -d . model/InvestmentAccount.java javac -d . model/ChequeAccount.java javac -d . model/Customer.java

Compile service classes
javac -d . service/BankData.java javac -d . service/Bank.java

Compile UI class
javac -d . ui/BankingSystem.java

Run the application
java bankingsystem.ui.BankingSystem
