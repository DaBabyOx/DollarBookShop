package Project;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import java.time.*;

public class register {
    public register(Stage primaryStage) {
        Label registerLabel = new Label("Register");
        registerLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setMinHeight(30);
        emailField.setMinWidth(300);

        VBox emailLayout = new VBox(5, emailLabel, emailField);
        emailLayout.setAlignment(Pos.CENTER_LEFT);

        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMinHeight(30);
        usernameField.setMinWidth(300);

        VBox usernameLayout = new VBox(5, usernameLabel, usernameField);
        usernameLayout.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMinHeight(30);
        passwordField.setMinWidth(300);

        VBox passwordLayout = new VBox(5, passwordLabel, passwordField);
        passwordLayout.setAlignment(Pos.CENTER_LEFT);

        Label confirmPasswordLabel = new Label("Confirm Password");
        confirmPasswordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setMinHeight(30);
        confirmPasswordField.setMinWidth(300);

        VBox confirmPasswordLayout = new VBox(5, confirmPasswordLabel, confirmPasswordField);
        confirmPasswordLayout.setAlignment(Pos.CENTER_LEFT);

        Label dobLabel = new Label("Date of Birth");
        dobLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        DatePicker dobField = new DatePicker();
        dobField.maxWidth(100);
        dobField.setMinHeight(30);

        VBox dobLayout = new VBox(5, dobLabel, dobField);
        dobLayout.setAlignment(Pos.CENTER_LEFT);

        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #00CED1");
        signUpButton.setMinHeight(30);
        signUpButton.setMinWidth(300);
        signUpButton.setOnAction(e -> {
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String dob = dobField.getValue().toString();

            if(email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dob.isEmpty()){
                showWarning("All fields must be filled!");}
            else if(!email.contains("@")){
                showWarning("Invalid email address!");}
            else if(!password.equals(confirmPassword)){
                showWarning("Passwords do not match!");}
            else if(password.length() < 8){
                showWarning("Password must be at least 8 characters long!");}
            else if(username.length()<5){
                showWarning("Username must be at least 5 characters long!");}
            else if(!alphanumericval(password)){
                showWarning("Password must contain both letters and numbers! (Alphanumeric)");}
            else if(Period.between(dobField.getValue(), LocalDate.now()).getYears()<18){  //incase gaboleh, pake sql aja, yg di jadiin comments.
                showWarning("You must be at least 18 years old to register!");}
            else{
                try{
                    if(DBConnect.checkEmail(email) && DBConnect.checkUsername(username)){
                        showWarning("Email / Username aready registered!");}
                    else{
                        DBConnect.addUser(email, password, username, dob);
                        showSuccess("Account created successfully!");
                        new login(primaryStage);
                    }}
                catch(SQLException ex){
                    ex.printStackTrace();}}});

        Hyperlink loginLink = new Hyperlink("Already have an account? Login here!");
        loginLink.setVisited(false);
        loginLink.setOnAction(e -> new login(primaryStage));

        VBox registerLayout = new VBox(10, emailLayout, usernameLayout, passwordLayout, confirmPasswordLayout, dobLayout, signUpButton, loginLink);
        registerLayout.setAlignment(Pos.CENTER);

        HBox center = new HBox(registerLayout);
        center.setAlignment(Pos.CENTER);

        VBox formLayout = new VBox(10, registerLabel, center);
        formLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(formLayout, 1920, 1080);
        primaryStage.setTitle("Dollar Book Shop");
        primaryStage.setScene(scene);
        primaryStage.show();}

    private void showWarning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning");
        alert.setContentText(message);
        alert.showAndWait();}

    private void showSuccess(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText(message);
        alert.showAndWait();}

    private boolean alphanumericval(String name){
        boolean isnum = false,islet=false;
        for(int i = 0; i<name.length();i++){
            for(char c : name.toCharArray()){
                if(Character.isLetter(c)){
                    islet = true;}
                else if(Character.isDigit(c)){
                    isnum = true;}}}
        return islet&&isnum;}

}
