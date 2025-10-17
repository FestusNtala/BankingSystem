package bankingsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {

    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private TextField nationalIdField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    @FXML private Label successLabel;
    @FXML private Label errorLabel;

    private BankController bankController;

    public RegistrationController() {
        this.bankController = new BankController();
    }

    @FXML
    public void initialize() {
        // Clear messages when user starts typing
        firstNameField.textProperty().addListener((obs, oldVal, newVal) -> clearMessages());
        nationalIdField.textProperty().addListener((obs, oldVal, newVal) -> clearMessages());
    }

    @FXML
    private void handleRegister() {
        if (!validateForm()) {
            return;
        }

        String firstname = firstNameField.getText().trim();
        String surname = surnameField.getText().trim();
        String nationalId = nationalIdField.getText().trim();
        String address = addressField.getText().trim();
        String password = passwordField.getText();

        try {
            boolean success = bankController.createCustomer(firstname, surname, address, nationalId, password);
            if (success) {
                showSuccess("Account created successfully! You can now login.");
                // Clear form
                clearForm();
            } else {
                showError("Registration failed. National ID might already exist.");
            }
        } catch (Exception e) {
            showError("Registration failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Botswana National Bank");
        } catch (IOException e) {
            showError("Cannot load login form");
        }
    }

    private boolean validateForm() {
        if (firstNameField.getText().trim().isEmpty() ||
            surnameField.getText().trim().isEmpty() ||
            nationalIdField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty() ||
            passwordField.getText().isEmpty()) {
            
            showError("Please fill in all fields");
            return false;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Passwords do not match");
            return false;
        }

        if (passwordField.getText().length() < 4) {
            showError("Password must be at least 4 characters long");
            return false;
        }

        return true;
    }

    private void showSuccess(String message) {
        successLabel.setText(message);
        successLabel.setVisible(true);
        errorLabel.setVisible(false);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        successLabel.setVisible(false);
    }

    private void clearMessages() {
        successLabel.setVisible(false);
        errorLabel.setVisible(false);
    }

    private void clearForm() {
        firstNameField.clear();
        surnameField.clear();
        nationalIdField.clear();
        addressField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}