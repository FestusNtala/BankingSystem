package bankingsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import bankingsystem.model.Customer;

import java.io.IOException;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label totalBalanceLabel;
    @FXML private Button logoutButton;
    @FXML private Button accountsButton;
    @FXML private Button openAccountButton;
    @FXML private Button depositButton;
    @FXML private Button withdrawButton;
    @FXML private Button changePasswordButton;
    @FXML private StackPane contentArea;

    private BankController bankController;

    public void setBankController(BankController bankController) {
        this.bankController = bankController;
        updateDashboard();
    }

    @FXML
    public void initialize() {
        // Initialization that doesn't depend on bankController
    }

    private void updateDashboard() {
        Customer currentCustomer = bankController.getCurrentCustomer();
        if (currentCustomer != null) {
            welcomeLabel.setText("Welcome, " + currentCustomer.getFirstname() + " " + currentCustomer.getSurname());
            updateTotalBalance();
        }
    }

    private void updateTotalBalance() {
        double totalBalance = bankController.getTotalBalance();
        totalBalanceLabel.setText(String.format("Total Balance: BWP%.2f", totalBalance));
    }

    @FXML
    private void handleViewAccounts() {
        // Load accounts view into content area
        showMessage("Accounts view will be implemented here");
        updateTotalBalance();
    }

    @FXML
    private void handleOpenAccount() {
        // Load open account view into content area
        showMessage("Open account form will be implemented here");
    }

    @FXML
    private void handleDeposit() {
        // Load deposit view into content area
        showMessage("Deposit form will be implemented here");
    }

    @FXML
    private void handleWithdraw() {
        // Load withdraw view into content area
        showMessage("Withdraw form will be implemented here");
    }

    @FXML
    private void handleChangePassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/password-reset.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) changePasswordButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Change Password - Botswana National Bank");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Cannot load password reset form");
        }
    }

    @FXML
    private void handleLogout() {
        bankController.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Botswana National Bank");
            stage.setMaximized(false);
            stage.setWidth(600);
            stage.setHeight(500);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Cannot load login form");
        }
    }

    private void showMessage(String message) {
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("sub-message");
        contentArea.getChildren().setAll(messageLabel);
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}