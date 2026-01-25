package com.example.ims001;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private Scene mainScene;

    // Figma-based size
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Prestige Inventory Suite");

        // Initialize empty scene once
        mainScene = new Scene(new javafx.scene.layout.StackPane(), WIDTH, HEIGHT);

        // Load global CSS once
        mainScene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        primaryStage.setScene(mainScene);
        showLoginView();
        primaryStage.show();
    }

    /* ================= VIEW SWITCHERS ================= */

    public void showLoginView() {
        LoginView view = new LoginView(this);
        mainScene.setRoot(view.getView());
    }

    public void showRegisterView() {
        RegisterView view = new RegisterView(this);
        mainScene.setRoot(view.getView());
    }

    public void showForgotPasswordView() {
        ForgotPasswordView view = new ForgotPasswordView(this);
        mainScene.setRoot(view.getView());
    }

    public void showDashboardView(String username) {
        DashboardView view = new DashboardView(this, username);
        mainScene.setRoot(view.getView());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
