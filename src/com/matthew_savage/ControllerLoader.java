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
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ControllerLoader {

    @FXML
    private AnchorPane loadingPane;
    @FXML
    TextField preloadProgressTop;
    @FXML
    TextField preloadProgressCenter;
    @FXML
    TextField preloadProgressBottom;

    private Executor executor = Executors.newFixedThreadPool(1, Executors.defaultThreadFactory());
//    private static ArrayList<MangaArrayList> available = new ArrayList<>();
//    private static ArrayList<MangaArrayList> blacklist = new ArrayList<>();
//    private static ArrayList<MangaArrayList> completed = new ArrayList<>();
//    private static ArrayList<MangaArrayList> reading = new ArrayList<>();
//    private static ArrayList<MangaArrayList> history = new ArrayList<>();
//    private static ArrayList<MangaArrayList> bookmark = new ArrayList<>();
//    private static ArrayList<MangaArrayList> downloading = new ArrayList<>();
//    private static ArrayList<StatsArrayList> stats = new ArrayList<>();
    private static boolean online = false;
//
//    public static ArrayList<MangaArrayList> getAvailable() {
//        return available;
//    }
//
//    public static ArrayList<MangaArrayList> getBlacklist() {
//        return blacklist;
//    }
//
//    public static ArrayList<MangaArrayList> getCompleted() {
//        return completed;
//    }
//
//    public static ArrayList<MangaArrayList> getReading() {
//        return reading;
//    }
//
//    public static ArrayList<MangaArrayList> getHistory() {
//        return history;
//    }
//
//    public static ArrayList<MangaArrayList> getBookmark() {
//        return bookmark;
//    }
//
//    public static ArrayList<MangaArrayList> getDownloading() {
//        return downloading;
//    }
//
//    public static ArrayList<StatsArrayList> getStats() {
//        return stats;
//    }

    public static boolean isOnline() {
        return online;
    }

    public void initialize() {
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
//        preloadProgressCenter.setText("Populating Manga Data...");
        CategoryMangaLists.notCollectedMangaList.addAll(
                initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_AVAILABLE.getValue()));
        CategoryMangaLists.rejectedMangaList.addAll(
                initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_NOT_INTERESTED.getValue()));
        CategoryMangaLists.completedMangaList.addAll(
                initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_COMPLETED.getValue()));
        CategoryMangaLists.collectedMangaList.addAll(
                initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_READING.getValue()));
        CategoryMangaLists.bookmark.addAll(
                initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_BOOKMARK.getValue()));
        CategoryMangaLists.downloading.addAll(
                initializeArray(Values.DB_NAME_DOWNLOADING.getValue(), Values.DB_TABLE_DOWNLOAD.getValue()));
        CategoryMangaLists.fiveNewestTitles.addAll(
                initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_FIVE_NEWEST.getValue()));
        CategoryMangaLists.history.addAll(HistoryPane.retrieveStoredHistory());
        CategoryMangaLists.stats.addAll(StatsPane.retrieveStoredStats());

//
//        blacklist = initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_NOT_INTERESTED.getValue());
//        completed = initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_COMPLETED.getValue());
//        reading = initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_READING.getValue());
//        bookmark = initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_BOOKMARK.getValue());
//        downloading = initializeArray(Values.DB_NAME_DOWNLOADING.getValue(), Values.DB_TABLE_DOWNLOAD.getValue());
//        history = HistoryPane.retrieveStoredHistory();
//        stats = StatsPane.retrieveStoredStats();
    }

    private static ArrayList<MangaArrayList> initializeArray(String fileName, String tableName) {
        return Startup.buildArray(fileName, tableName);
    }

    public void launchMainApp() {
        Platform.runLater(this::switchStage);
    }

    private void boot() {
//        preloadProgressCenter.setText("Starting...");
        launchMainApp();
    }

    private void fetchNewTitles() {
//        preloadProgressCenter.setText("Checking For New Manga");
//        populate.findStartingPage();
        SourceWebsite.indexTitles();
        System.out.println("the break worked lol?");
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