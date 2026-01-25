package com.example.ims001;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ForgotPasswordView {

    private MainApp mainApp;
    private StackPane root;

    private TextField txtUsername, txtResetCode, txtNewPasswordText;
    private PasswordField txtNewPassword;
    private CheckBox chkShowPassword;
    private Label lblMessage;

    public ForgotPasswordView(MainApp mainApp) {
        this.mainApp = mainApp;

        root = new StackPane();
        root.getStyleClass().add("login-root");

        // ===== Background =====
        Image bgGif = new Image(getClass().getResource("/images/bgleftcard.jpg").toExternalForm());
        ImageView bgView = new ImageView(bgGif);
        bgView.setFitWidth(1920);
        bgView.setFitHeight(1080);
        bgView.setPreserveRatio(false);

        HBox mainBox = new HBox();
        mainBox.setAlignment(Pos.CENTER);

        VBox leftCard = createLeftCard();
        VBox rightCard = createForgotCard();

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

    private VBox createForgotCard() {
        VBox rightCard = new VBox(12);
        rightCard.setPrefWidth(700);
        rightCard.setPrefHeight(520);   // match Login/Register
        rightCard.setAlignment(Pos.TOP_LEFT);
        rightCard.getStyleClass().add("right-card");

        Label title = new Label("Forgot Password");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Reset your password using your permanent reset code.");
        subtitle.getStyleClass().add("title_2");
        subtitle.setWrapText(true);

        Label userLbl = new Label("Username");
        userLbl.getStyleClass().add("title_3");

        txtUsername = new TextField();
        txtUsername.setPromptText("Enter your Username");
        txtUsername.setMaxWidth(480);
        txtUsername.getStyleClass().add("input-field");

        Label codeLbl = new Label("Reset Code");
        codeLbl.getStyleClass().add("title_3");

        txtResetCode = new TextField();
        txtResetCode.setPromptText("Enter your Reset Code");
        txtResetCode.setMaxWidth(480);
        txtResetCode.getStyleClass().add("input-field");

        Label passLbl = new Label("New Password");
        passLbl.getStyleClass().add("title_3");

        txtNewPassword = new PasswordField();
        txtNewPassword.setPromptText("Enter New Password");
        txtNewPassword.setMaxWidth(480);
        txtNewPassword.getStyleClass().add("input-field");

        txtNewPasswordText = new TextField();
        txtNewPasswordText.setPromptText("Enter New Password");
        txtNewPasswordText.setMaxWidth(480);
        txtNewPasswordText.getStyleClass().add("input-field");
        txtNewPasswordText.setVisible(false);
        txtNewPasswordText.setManaged(false);

        txtNewPassword.textProperty().bindBidirectional(txtNewPasswordText.textProperty());

        chkShowPassword = new CheckBox("Show Password");
        chkShowPassword.setOnAction(e -> {
            boolean show = chkShowPassword.isSelected();
            txtNewPassword.setVisible(!show);
            txtNewPassword.setManaged(!show);
            txtNewPasswordText.setVisible(show);
            txtNewPasswordText.setManaged(show);
        });

        Button btnReset = new Button("Reset Password");
        btnReset.getStyleClass().add("primary-button");
        btnReset.setOnAction(e -> handleReset());

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
                codeLbl,
                txtResetCode,
                passLbl,
                txtNewPassword,
                txtNewPasswordText,
                chkShowPassword,
                btnReset,
                btnBack,
                lblMessage
        );

        return rightCard;
    }

    // ===== LOGIC (UNCHANGED) =====

    private void handleReset() {
        String username = txtUsername.getText();
        String resetCode = txtResetCode.getText();
        String newPassword = chkShowPassword.isSelected()
                ? txtNewPasswordText.getText()
                : txtNewPassword.getText();

        if (username.isEmpty() || resetCode.isEmpty() || newPassword.isEmpty()) {
            lblMessage.setText("All fields are required.");
            lblMessage.getStyleClass().setAll("message-error");
            return;
        }

        if (!isStrongPassword(newPassword)) {
            lblMessage.setText("Password must be at least 8 chars, include upper, lower, number & symbol.");
            lblMessage.getStyleClass().setAll("message-error");
            return;
        }

        if (!UserDAO.checkResetCode(username, resetCode)) {
            lblMessage.setText("Invalid username or reset code.");
            lblMessage.getStyleClass().setAll("message-error");
            return;
        }

        if (UserDAO.updatePassword(username, newPassword)) {
            lblMessage.setText("Password reset successful! Go back to login.");
            lblMessage.getStyleClass().setAll("message-success");
        } else {
            lblMessage.setText("Failed to reset password. Try again later.");
            lblMessage.getStyleClass().setAll("message-error");
        }
    }

    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()].*");
    }

    public Parent getView() {
        return root;
    }
}
