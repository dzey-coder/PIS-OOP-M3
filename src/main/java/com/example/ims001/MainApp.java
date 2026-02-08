package com.example.ims001;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private Scene mainScene;


    private static final int WIDTH = 1100;
    private static final int HEIGHT = 700;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Prestige Inventory Suites");


        mainScene = new Scene(new StackPane(), WIDTH, HEIGHT);


        mainScene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        primaryStage.setScene(mainScene);


        showLoginView();

        primaryStage.show();
    }


    private void setRoot(Parent root) {
        mainScene.setRoot(root);
    }


    public void showLoginView() {
        Session.clear();
        LoginView view = new LoginView(this);
        setRoot(view.getView());
    }


    public void showRegisterView() {
        RegisterView view = new RegisterView(this);
        setRoot(view.getView());
    }


    public void showForgotPasswordView() {
        ForgotPasswordView view = new ForgotPasswordView(this);
        setRoot(view.getView());
    }


    public void showDashboardView(String username) {
        Session.setUsername(username);
        DashboardView view = new DashboardView(this, username);
        setRoot(view.getView());
    }


    public void showInventoryView() {
        InventoryView view = new InventoryView(this);
        setRoot(view.getView());
    }


    public void showHistoryView() {
        HistoryView view = new HistoryView(this);
        setRoot(view.getView());
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showTransactionView() {
        TransactionView view = new TransactionView(this, Session.getUsername());
        setRoot(view.getView());
    }



}
