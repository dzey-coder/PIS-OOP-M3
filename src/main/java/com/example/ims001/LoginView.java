package com.example.ims001;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginView {

    private MainApp mainApp;
    private StackPane root;

    private TextField txtUsername;
    private PasswordField txtPassword;
    private TextField txtPasswordText;
    private Label lblMessage;
    private CheckBox showPassword;

    public LoginView(MainApp mainApp) {
        this.mainApp = mainApp;

        root = new StackPane();
        root.getStyleClass().add("login-root");

        // ===== Background GIF =====
        Image bgGif = new Image(getClass().getResource("/images/bgleftcard.jpg").toExternalForm());
        ImageView bgView = new ImageView(bgGif);
        bgView.setFitWidth(1920);
        bgView.setFitHeight(1080);
        bgView.setPreserveRatio(false);
        bgView.setOpacity(1);

        // ===== Main HBox with left and right cards =====
        HBox mainBox = new HBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(0); // no gap between cards

        VBox leftCard = createLeftCard();
        VBox rightCard = createLoginCard();

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
        imgView.setSmooth(true);
        imgView.setCache(true);

        leftCard.getChildren().add(imgView);
        return leftCard;
    }

    private VBox createLoginCard() {
        VBox rightCard = new VBox(12);
        rightCard.setPrefWidth(700);
        rightCard.setAlignment(Pos.CENTER_LEFT);
        rightCard.getStyleClass().add("right-card");

        Label title = new Label("Login form");
        title.getStyleClass().add("title");

        Label quote = new Label("“Know what you have, sell with confidence, and never run out unexpectedly.”");
        quote.getStyleClass().add("title_2");
        quote.setWrapText(true);

        Label user = new Label("Username");
        user.getStyleClass().add("title_3");

        txtUsername = new TextField();
        txtUsername.setPromptText("Enter your Username");
        txtUsername.setMaxWidth(480);
        txtUsername.getStyleClass().add("input-field");

        Label pass = new Label("Password");
        pass.getStyleClass().add("title_3");

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

        showPassword = new CheckBox("Show Password");
        showPassword.setOnAction(e -> {
            boolean show = showPassword.isSelected();
            txtPassword.setVisible(!show);
            txtPassword.setManaged(!show);
            txtPasswordText.setVisible(show);
            txtPasswordText.setManaged(show);
        });

        Button btnForgot = new Button("Forgot Password?");
        btnForgot.getStyleClass().add("link-button");
        btnForgot.setOnAction(e -> mainApp.showForgotPasswordView());

        // Show password + forgot password on same row
        HBox passwordRow = new HBox(10);
        passwordRow.setAlignment(Pos.CENTER_LEFT);
        passwordRow.getChildren().addAll(showPassword, btnForgot);

        Button btnLogin = new Button("Get Started");
        btnLogin.getStyleClass().add("primary-button");
        btnLogin.setOnAction(e -> handleLogin());

        Button btnRegister = new Button("Sign up");
        btnRegister.getStyleClass().add("secondary-button");
        btnRegister.setOnAction(e -> mainApp.showRegisterView());

        lblMessage = new Label();
        lblMessage.getStyleClass().add("message-label");

        rightCard.getChildren().addAll(
                title,
                quote,
                user,
                txtUsername,
                pass,
                txtPassword,
                txtPasswordText,
                passwordRow,
                btnLogin,
                btnRegister,
                lblMessage
        );

        return rightCard;
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Username and password cannot be empty.");
            lblMessage.getStyleClass().setAll("message-error");
            return;
        }

        if (UserDAO.login(username, password)) {
            lblMessage.setText("Login successful!");
            lblMessage.getStyleClass().setAll("message-success");
            mainApp.showDashboardView(username);
        } else {
            lblMessage.setText("Invalid username or password.");
            lblMessage.getStyleClass().setAll("message-error");
        }
    }

    public Parent getView() {
        return root;
    }
}
