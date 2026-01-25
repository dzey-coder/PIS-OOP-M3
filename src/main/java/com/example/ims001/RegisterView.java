package com.example.ims001;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.security.SecureRandom;

public class RegisterView {

    private MainApp mainApp;
    private StackPane root;

    private TextField txtUsername;
    private PasswordField txtPassword, txtConfirm;
    private TextField txtPasswordText, txtConfirmText;
    private Label lblMessage;
    private CheckBox showPassword;

    public RegisterView(MainApp mainApp) {
        this.mainApp = mainApp;

        root = new StackPane();
        root.getStyleClass().add("login-root");

        // ===== Background GIF =====
        Image bgGif = new Image(getClass().getResource("/images/bgleftcard.jpg").toExternalForm());
        ImageView bgView = new ImageView(bgGif);
        bgView.setFitWidth(1920);
        bgView.setFitHeight(1080);
        bgView.setPreserveRatio(false);

        // ===== Main Layout =====
        HBox mainBox = new HBox();
        mainBox.setAlignment(Pos.CENTER);

        VBox leftCard = createLeftCard();
        VBox rightCard = createRegisterCard();

        mainBox.getChildren().addAll(leftCard, rightCard);
        root.getChildren().addAll(bgView, mainBox);
    }

    private VBox createLeftCard() {
        VBox leftCard = new VBox();
        leftCard.setPrefWidth(400);
        leftCard.setAlignment(Pos.CENTER);
        leftCard.getStyleClass().add("left-card");

        Image img = new Image(getClass().getResource("/images/bgleftcard_3.jpg").toExternalForm());
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(300);
        imgView.setPreserveRatio(true);
        imgView.setOpacity(0);

        leftCard.getChildren().add(imgView);
        return leftCard;
    }

    private VBox createRegisterCard() {
        VBox rightCard = new VBox(12);
        rightCard.setPrefWidth(700);
        rightCard.setAlignment(Pos.CENTER_LEFT);
        rightCard.getStyleClass().add("right-card");

        Label title = new Label("Register");
        title.getStyleClass().add("title");

        Label subtitle = new Label("''Create your account to manage inventory efficiently.''");
        subtitle.getStyleClass().add("title_2");
        subtitle.setWrapText(true);

        Label userLbl = new Label("Username");
        userLbl.getStyleClass().add("title_3");

        txtUsername = new TextField();
        txtUsername.setPromptText("Enter your Username");
        txtUsername.setMaxWidth(480);
        txtUsername.getStyleClass().add("input-field");

        Label passLbl = new Label("Password");
        passLbl.getStyleClass().add("title_3");

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter your Password");
        txtPassword.setMaxWidth(480);
        txtPassword.getStyleClass().add("input-field");

        txtPasswordText = new TextField();
        txtPasswordText.setPromptText("Enter your Password");
        txtPasswordText.setMaxWidth(480);
        txtPasswordText.getStyleClass().add("input-field");
        txtPasswordText.setVisible(false);
        txtPasswordText.setManaged(false);

        txtPassword.textProperty().bindBidirectional(txtPasswordText.textProperty());

        txtConfirm = new PasswordField();
        txtConfirm.setPromptText("Confirm Password");
        txtConfirm.setMaxWidth(480);
        txtConfirm.getStyleClass().add("input-field");

        txtConfirmText = new TextField();
        txtConfirmText.setPromptText("Confirm Password");
        txtConfirmText.setMaxWidth(480);
        txtConfirmText.getStyleClass().add("input-field");
        txtConfirmText.setVisible(false);
        txtConfirmText.setManaged(false);

        txtConfirm.textProperty().bindBidirectional(txtConfirmText.textProperty());

        showPassword = new CheckBox("Show Password");
        showPassword.setOnAction(e -> {
            boolean show = showPassword.isSelected();

            txtPassword.setVisible(!show);
            txtPassword.setManaged(!show);
            txtPasswordText.setVisible(show);
            txtPasswordText.setManaged(show);

            txtConfirm.setVisible(!show);
            txtConfirm.setManaged(!show);
            txtConfirmText.setVisible(show);
            txtConfirmText.setManaged(show);
        });

        Button btnSuggest = new Button("Suggest Password");
        btnSuggest.getStyleClass().add("link-button");
        btnSuggest.setOnAction(e -> txtPassword.setText(generatePassword(10)));

        HBox optionRow = new HBox(15, showPassword, btnSuggest);
        optionRow.setAlignment(Pos.CENTER_LEFT);

        Button btnRegister = new Button("Create Account");
        btnRegister.getStyleClass().add("primary-button");
        btnRegister.setOnAction(e -> handleRegister());

        Button btnBack = new Button("Back to Login");
        btnBack.getStyleClass().add("secondary-button");
        btnBack.setOnAction(e -> mainApp.showLoginView());

        lblMessage = new Label();
        lblMessage.getStyleClass().add("message-label");

        rightCard.getChildren().addAll(
                title,
                subtitle,
                userLbl,
                txtUsername,
                passLbl,
                txtPassword,
                txtPasswordText,
                txtConfirm,
                txtConfirmText,
                optionRow,
                btnRegister,
                btnBack,
                lblMessage
        );

        return rightCard;
    }

    // ===== LOGIC (UNCHANGED) =====

    private void handleRegister() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String confirm = txtConfirm.getText();
        String resetCode = generateResetCode();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            lblMessage.setText("All fields are required.");
            lblMessage.getStyleClass().setAll("message-error");
            return;
        }

        if (!password.equals(confirm)) {
            lblMessage.setText("Passwords do not match.");
            lblMessage.getStyleClass().setAll("message-error");
            return;
        }

        if (!isStrongPassword(password)) {
            lblMessage.setText("Password must be 8+ chars with number & symbol.");
            lblMessage.getStyleClass().setAll("message-error");
            return;
        }

        boolean success = UserDAO.register(username, password, resetCode);
        if (success) {
            lblMessage.setText("Registered! Reset code: " + resetCode);
            lblMessage.getStyleClass().setAll("message-success");
        } else {
            lblMessage.setText("Registration failed. Username may exist.");
            lblMessage.getStyleClass().setAll("message-error");
        }
    }

    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()].*");
    }

    private String generateResetCode() {
        SecureRandom rand = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++)
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        return sb.toString();
    }

    private String generatePassword(int length) {
        SecureRandom rand = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        return sb.toString();
    }

    public Parent getView() {
        return root;
    }
}
