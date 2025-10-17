package bankingsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import bankingsystem.model.Customer;

import java.io.IOException;

public class LoginController {

    @FXML private TextField nationalIdField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheckbox;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button forgotPasswordButton;
    @FXML private Label errorLabel;

    private BankController bankController;

    public LoginController() {
        this.bankController = new BankController();
    }

    @FXML
    public void initialize() {
        // Clear error message when user starts typing
        nationalIdField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
    }

    @FXML
    private void handleLogin() {
        String nationalId = nationalIdField.getText().trim();
        String password = passwordField.getText();

        if (nationalId.isEmpty() || password.isEmpty()) {
            showError("Please enter both National ID and password");
            return;
        }

        try {
            boolean success = bankController.loginCustomer(nationalId, password);
            if (success) {
                showDashboard();
            } else {
                showError("Invalid National ID or password");
            }
        } catch (Exception e) {
            showError("Login failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register - Botswana National Bank");
        } catch (IOException e) {
            showError("Cannot load registration form");
        }
    }

    @FXML
    private void handleForgotPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/password-reset.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) forgotPasswordButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reset Password - Botswana National Bank");
        } catch (IOException e) {
            showError("Cannot load password reset form");
        }
    }

    private void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            
            DashboardController dashboardController = loader.getController();
            dashboardController.setBankController(bankController);
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard - Botswana National Bank");
            stage.setMaximized(true);
        } catch (IOException e) {
            showError("Cannot load dashboard");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void clearError() {
        errorLabel.setVisible(false);
    }
}