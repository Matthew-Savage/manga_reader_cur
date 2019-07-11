package com.matthew_savage;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ControllerLoader {

    @FXML
    private AnchorPane loadingPane;
    @FXML
    TextField preloadProgressTop;
    @FXML
    TextField preloadProgressCenter;
    @FXML
    TextField preloadProgressBottom;

    private Executor executor = Executors.newSingleThreadExecutor();
    private static boolean online = false;
    public static boolean updatable = true;
    public static StringProperty update = new SimpleStringProperty();

    public static boolean isOnline() {
        return online;
    }
    public static boolean isUpdatable() {
        return updatable;
    }

    public void initialize() {
        update.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                preloadProgressCenter.setText(newValue);
            }
        });
        executor.execute(this::preload);
    }

    private void preload() {
        Startup.implementDatabaseChanges();
        initializeArrays();
        if (InternetConnection.checkIfConnected()) {
            online = true;
            fetchNewTitles();
        }
        boot();
    }

    private void initializeArrays() {
        update.set("Building Manga cache ...");
        CategoryMangaLists.notCollectedMangaList.addAll(
                initializeArray(StaticStrings.DB_NAME_MANGA.getValue(), StaticStrings.DB_TABLE_AVAILABLE.getValue()));
        CategoryMangaLists.rejectedMangaList.addAll(
                initializeArray(StaticStrings.DB_NAME_MANGA.getValue(), StaticStrings.DB_TABLE_NOT_INTERESTED.getValue()));
        CategoryMangaLists.completedMangaList.addAll(
                initializeArray(StaticStrings.DB_NAME_MANGA.getValue(), StaticStrings.DB_TABLE_COMPLETED.getValue()));
        CategoryMangaLists.collectedMangaList.addAll(
                initializeArray(StaticStrings.DB_NAME_MANGA.getValue(), StaticStrings.DB_TABLE_READING.getValue()));
        CategoryMangaLists.bookmark.addAll(
                initializeArray(StaticStrings.DB_NAME_MANGA.getValue(), StaticStrings.DB_TABLE_BOOKMARK.getValue()));
        CategoryMangaLists.downloading.addAll(
                initializeArray(StaticStrings.DB_NAME_DOWNLOADING.getValue(), StaticStrings.DB_TABLE_DOWNLOAD.getValue()));
        CategoryMangaLists.fiveNewestTitles.addAll(
                initializeArray(StaticStrings.DB_NAME_MANGA.getValue(), StaticStrings.DB_TABLE_FIVE_NEWEST.getValue()));
        CategoryMangaLists.history.addAll(HistoryPane.retrieveStoredHistory());
        CategoryMangaLists.stats.addAll(StatsPane.retrieveStoredStats());
    }

    private static ArrayList<Manga> initializeArray(String fileName, String tableName) {
        return Startup.buildArray(fileName, tableName);
    }

    private void boot() {
        update.set("Launching Reader ...");
        Platform.runLater(this::switchStage);
    }

    private void fetchNewTitles() {
        SourceWebsite.indexTitles();
    }

    private void switchStage() {
        Stage primaryStage = (Stage) loadingPane.getScene().getWindow();

        primaryStage.hide();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.getIcons().add(new Image("assets/ico.png"));
        primaryStage.setTitle("Cupcaked Manga Reader");
        primaryStage.setScene(new Scene(Objects.requireNonNull(root), 1920, 1034, Color.TRANSPARENT));  //1026
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);
//        primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ESCAPE));
        primaryStage.show();
        root.requestFocus();
    }
}