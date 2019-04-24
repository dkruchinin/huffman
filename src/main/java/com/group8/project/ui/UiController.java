package com.group8.project.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UiController extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().
                getResource("/UiController.fxml"));
        primaryStage.setTitle("Welcome to JHufCompress!");
        primaryStage.setScene(new Scene(root,700,320));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
