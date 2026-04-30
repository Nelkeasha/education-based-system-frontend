package com.nelly.education_based;

import com.nelly.education_based.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class
LoginController implements Initializable {

    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passwordField.setOnAction(e -> handleLogin());
        usernameField.setOnAction(e -> passwordField.requestFocus());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter your username and password.");
            return;
        }

        Optional<User> result = HelloApplication.getUserService().login(username, password);
        if (result.isPresent()) {
            errorLabel.setText("");
            HelloApplication.showMainView();
        } else {
            showError("Invalid username or password. Please try again.");
            passwordField.clear();
            passwordField.requestFocus();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
