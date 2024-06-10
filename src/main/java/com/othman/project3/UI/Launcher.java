package com.othman.project3.UI;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class Launcher extends Application {


    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane;
        try {
            borderPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/othman/project3/main-screen.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        primaryStage.centerOnScreen();
        primaryStage.setTitle("World Map with Capitals");
        primaryStage.setScene(new Scene(borderPane));
        primaryStage.setResizable(false);
        primaryStage.show();

        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
