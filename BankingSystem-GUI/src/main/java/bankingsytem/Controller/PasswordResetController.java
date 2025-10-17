package bankingsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class PasswordResetController {

    @FXML private TextField nationalIdField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button resetButton;
    @FXML private Button backButton;
    @FXML private Label successLabel;
    @FXML private Label errorLabel;

    private BankController bankController;

    public PasswordResetController() {
        this.bankController = new BankController();
    }

    @FXML
    public void initialize() {
        // Clear messages when user starts typing
        nationalIdField.textProperty().addListener((obs, oldVal, newVal) -> clearMessages());
        newPasswordField.textProperty().addListener((obs, oldVal, newVal) -> clearMessages());
    }

    @FXML
    private void handleResetPassword() {
        if (!validateForm()) {
            return;
        }

        String nationalId = nationalIdField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        try {
            boolean success = bankController.resetPassword(nationalId, newPassword, confirmPassword);
            if (success) {
                showSuccess("Password reset successfully! You can now login with your new password.");
                clearForm();
            } else {
                showError("Password reset failed. Please check your National ID or ensure passwords match.");
            }
        } catch (Exception e) {
            showError("Password reset failed: " + e.getMessage());
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
        if (nationalIdField.getText().trim().isEmpty() ||
            newPasswordField.getText().isEmpty() ||
            confirmPasswordField.getText().isEmpty()) {
            
            showError("Please fill in all fields");
            return false;
        }

        if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
            showError("Passwords do not match");
            return false;
        }

        if (newPasswordField.getText().length() < 4) {
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
        nationalIdField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }
}