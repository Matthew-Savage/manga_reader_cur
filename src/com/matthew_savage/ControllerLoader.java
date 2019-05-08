package com.matthew_savage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ControllerLoader {

    @FXML
    private AnchorPane testPane;
    @FXML
    TextField preloadProgressTop;
    @FXML
    TextField preloadProgressCenter;
    @FXML
    TextField preloadProgressBottom;

    private Database database = new Database();
    private PopulateMangaCatalog populate = new PopulateMangaCatalog(this);
    private Executor executor = Executors.newSingleThreadExecutor();

    public void initialize() {

        if (InternetConnection.checkIfConnected()) {
            preloadProgressCenter.setText(Values.DIR_ROOT.getValue());
//            executor.execute(this::fetchNewTitles);
//            executor.execute(this::checkForUpdates);
        }
        executor.execute(this::launchMainApp);
    }


//    private Runnable prelaunchTasks = () -> {
//        fetchNewTitles();
//        checkForUpdates();
//        switchStage();
//    };

    public void launchMainApp() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switchStage();
            }
        });
    }

    private void fetchNewTitles() {
        preloadProgressCenter.setText("Checking For New Manga");
        populate.findStartingPage();
    }

    public void clearProgressText() {
        preloadProgressTop.clear();
        preloadProgressCenter.clear();
        preloadProgressBottom.clear();
    }


    public void switchStage() {
        Stage primaryStage = (Stage) testPane.getScene().getWindow();
        primaryStage.hide();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.getIcons().add(new Image("assets/ico.png"));
        primaryStage.setTitle("Cupcaked Manga Reader");
        primaryStage.setScene(new Scene(root, 1920, 1034, Color.TRANSPARENT));  //1026
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);
//        primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ESCAPE));
        primaryStage.show();
        root.requestFocus();


    }
}