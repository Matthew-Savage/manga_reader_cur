package com.matthew_savage;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;


public class ControllerMain {

    @FXML
    private ImageView imageView;
    @FXML
    private ImageView thumbImage;
    @FXML
    private ImageView favoriteOverlay;
    @FXML
    private ImageView newChaptersOverlay;
    @FXML
    private ImageView mangaThumb;
    @FXML
    private ImageView noInternet;
    @FXML
    private AnchorPane main;
    @FXML
    private Pane launchPane;
    @FXML
    private Pane catalogPane;
    @FXML
    private Pane thumbPane;
    @FXML
    private Pane favoriteOverlayPane;
    @FXML
    private Pane newChaptersOverlayPane;
    @FXML
    private Pane popupPane;
    @FXML
    private Pane innerPopup;
    @FXML
    private Pane innerPopupNumbox;
    @FXML
    private Pane repairPopup;
    @FXML
    private Pane genrePane;
    @FXML
    private Pane pageNumTextPane;
    @FXML
    private Pane sidebarPane;
    @FXML
    private Pane error;
    @FXML
    private Pane historyPaneContent;
    @FXML
    private Pane historyClosePane;
    @FXML
    private Pane statsPane;
    @FXML
    private ScrollPane scrollImagePane;
    @FXML
    private BorderPane readMangaPane;
    @FXML
    private CheckBox ascendDescend;
    @FXML
    private RadioButton haveNotRead;
    @FXML
    private RadioButton haveRead;
    @FXML
    private TextField mangaTitle;
    @FXML
    private TextField mangaGenres;
    @FXML
    private TextField mangaAuthors;
    @FXML
    private TextField lastChapterRead;
    @FXML
    private TextField sidebarTotalChapters;
    @FXML
    private TextField searchBox;
    @FXML
    private TextField currentPageNumberDisplay;
    @FXML
    private TextField lastPageNumberDisplay;
    @FXML
    private TextField enterPageNumber;
    @FXML
    private TextField sidebarChapterNumber;
    @FXML
    private TextField sidebarPageNumber;
    @FXML
    private TextField gotoChapter;
    @FXML
    private TextField scrollSpeedText;
    @FXML
    private TextField statTitlesTotal;
    @FXML
    private TextField statTitlesReading;
    @FXML
    private TextField statTitlesFinished;
    @FXML
    private TextField statPagesRead;
    @FXML
    private TextField statTitlesFavorite;
    @FXML
    private TextField statTitlesIgnore;
    @FXML
    private TextField statPagesDaily;
    @FXML
    private TextField statGenreOne;
    @FXML
    private TextField statGenreTwo;
    @FXML
    private TextField statGenreThree;
    @FXML
    private TextArea mangaSummary;
    @FXML
    private ChoiceBox<String> currentActivity;
    @FXML
    private Button pageFirst;
    @FXML
    private Button pagePrevious;
    @FXML
    private Button pageNext;
    @FXML
    private Button pageLast;
    @FXML
    private Button appMinimize;
    @FXML
    private Button appClose;
    @FXML
    private Button startReading;
    @FXML
    private Button ignoreManga;
    @FXML
    private Button getInnerPopup;
    @FXML
    private Button getIssuePopup;
    @FXML
    private Button ignoreMangaShort;
    @FXML
    private Button rereadManga;
    @FXML
    private Button favorite;
    @FXML
    private Button readManga;
    @FXML
    private Button unignore;
    @FXML
    private Button sidebarPaneSizeDecrease;
    @FXML
    private Button sidebarPaneSizeIncrease;


    private Database database = new Database();
//    private Changes changes = new Changes();

    //--------------------------
    private static ArrayList<MangaArrayList> available = new ArrayList<>();
    private static ArrayList<MangaArrayList> completed = new ArrayList<>();
    private static ArrayList<MangaArrayList> reading = new ArrayList<>();
    private static ArrayList<MangaArrayList> history = new ArrayList<>();
    private static ArrayList<MangaArrayList> bookmark = new ArrayList<>();
    private static ArrayList<MangaArrayList> downloading = new ArrayList<>();
    private static ArrayList<StatsArrayList> stats = new ArrayList<>();

    //--------------------------

    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private ArrayList<ImageView> historyThumbViews = new ArrayList<>();
    private ArrayList<TextField> historyTitleFields = new ArrayList<>();
    private ArrayList<Button> historyReadButtons = new ArrayList<>();
    private ArrayList<TextArea> historySummaryFields = new ArrayList<>();
    private ArrayList<ImageView> favoriteOverlayViews = new ArrayList<>();
    private ArrayList<ImageView> newChaptersOverlayViews = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private ArrayList<String> genreStrings = new ArrayList<>();
    private ArrayList<MangaListView> mangaId = new ArrayList<>();
    private ArrayList<File> mangaPages = new ArrayList<>();
    private File thumbsPath = new File(Values.DIR_ROOT.getValue() + File.separator + Values.DIR_THUMBS.getValue());
    private StringJoiner joiner = new StringJoiner(" AND ");
    private int startingChapter;
    private int pageNumber = 0;
    private int selectedManga = -1;
    private int lastChapterReadNumber;
    private int totalChaptersNumber;
    private int currentPageNumber;
    private static Executor executor = Executors.newFixedThreadPool(10);
    private static Executor modifyThread = Executors.newSingleThreadExecutor();
    static ScheduledExecutorService downloadThread = Executors.newScheduledThreadPool(1);
    private static ScheduledExecutorService checkIfUpdated = Executors.newScheduledThreadPool(1);
    private DownloadMangaPages downloadMangaPages = new DownloadMangaPages(this);
    private int thumbNumber;
    private int lastChapterDownloaded;
    private int scrollSpeed = 5;

    public void initialize() {
        GenreMap.createGenreString();

        //own thread, semaphore
        available = initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_AVAILABLE.getValue());
        completed = initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_COMPLETED.getValue());
        reading = initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_READING.getValue());
        bookmark = initializeArray(Values.DB_NAME_MANGA.getValue(), Values.DB_TABLE_BOOKMARK.getValue());
        downloading = initializeArray(Values.DB_NAME_DOWNLOADING.getValue(), Values.DB_TABLE_DOWNLOAD.getValue());
        history = HistoryPane.retrieveStoredHistory();

        currentActivity.setItems(FXCollections.observableArrayList("Available Manga", "My Library", "Finished Reading", "Not Interested"));

        noInternet.setSmooth(true);
        noInternet.setImage(new Image("assets/no_internet.png"));

        //we can get rid of genrestrings I.. think? lol
        //the rest of these arraybuilders need to move to a method and run in thier own thread minus the ones that will display instantly I guess huh?
        for (int i = 0; i < 39; i++) {
            genreStrings.add(i, "");
        }
        for (Node thumbs : thumbPane.getChildren()) {
            imageViews.add((ImageView) thumbs);
        }
        for (Node favorites : favoriteOverlayPane.getChildren()) {
            favoriteOverlayViews.add((ImageView) favorites);
        }
        for (Node newChapters : newChaptersOverlayPane.getChildren()) {
            newChaptersOverlayViews.add((ImageView) newChapters);
        }
        for (Node genres : genrePane.getChildren()) {
            checkBoxes.add((CheckBox) genres);
        }
        for (Node historySummaries : historyPaneContent.getChildren().filtered(TextArea.class::isInstance)) {
            historySummaryFields.add((TextArea) historySummaries);
        }
        for (Node historyThumbs : historyPaneContent.getChildren().filtered(ImageView.class::isInstance)) {
            historyThumbViews.add((ImageView) historyThumbs);
        }
        for (Node historyButtons : historyPaneContent.getChildren().filtered(Button.class::isInstance)) {
            historyReadButtons.add((Button) historyButtons);
        }
        for (Node historyTitles : historyPaneContent.getChildren().filtered(TextField.class::isInstance)) {
            historyTitleFields.add((TextField) historyTitles);
        }

        try {
            checkForHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.execute(this::fetchStats);

        lastChapterRead.lengthProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                forceNumbersOnly(oldValue, newValue, lastChapterRead);
            }
        });

        enterPageNumber.lengthProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                forceNumbersOnly(oldValue, newValue, enterPageNumber);
            }
        });

        gotoChapter.lengthProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                forceNumbersOnly(oldValue, newValue, gotoChapter);
            }
        });

        main.widthProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                imageView.setFitWidth(newValue.doubleValue());
                scrollImagePane.setMinWidth(newValue.doubleValue());
            }
        });

        main.heightProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollImagePane.setMinHeight(newValue.doubleValue());
            }
        });

        if (InternetConnection.checkIfConnected()) {
            noInternet.setVisible(false);
            //checkforupdates will launch download
//            checkIfUpdated.scheduleWithFixedDelay(updateShit, 0, 30, TimeUnit.MINUTES);
//            downloadThread.scheduleWithFixedDelay(downloadManga, 120, 10, TimeUnit.SECONDS);


        }
    }

    private Runnable updateShit = UpdateCollectedMangas::seeIfUpdated;

    private static ArrayList<MangaArrayList> initializeArray(String fileName, String tableName) {
        return Startup.buildArray(fileName, tableName);
    }

    public void appClose() {
        downloadThread.shutdownNow();
        Stage stage = (Stage) appClose.getScene().getWindow();
        stage.close();
    }

    public void appMinimize() {
        Stage stage = (Stage) appMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    private void checkForHistory() {

        System.out.println("is history empty? " + history.isEmpty());
        System.out.println("history size is " + history.size());


        if (history.size() > 0) {
            try {
                populateHistory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void populateHistory() throws Exception {

        for (int i = 0; i < history.size(); i++) {
            ImageView historyThumb = historyThumbViews.get(i);
            TextField historyTitle = historyTitleFields.get(i);
            Button historyButton = historyReadButtons.get(i);
            TextArea historySummary = historySummaryFields.get(i);
            historyThumb.setSmooth(true);
            historyThumb.setImage(new Image(new FileInputStream(thumbsPath + File.separator + history.get(i).getTitleId() + ".jpg")));
            historyTitle.setText(history.get(i).getTitle());
            historyButton.setVisible(true);
            historySummary.setText(history.get(i).getSummary());
        }
    }

    private void fetchStats() {
        stats = StatsPane.retrieveStoredStats();
        populateStats();
    }

    private void populateStats() {
        statTitlesTotal.setText(Values.STAT_PRE_TITLE_TOT.getValue() + stats.get(0).getTitlesTotal() + Values.STAT_SUF_TITLE_TOT.getValue());
        statTitlesReading.setText(Values.STAT_PRE_READING_TOT.getValue() + stats.get(0).getTitlesReading() + Values.STAT_SUF_READING_TOT.getValue());
        statTitlesFinished.setText(Values.STAT_PRE_FIN_TOT.getValue() + stats.get(0).getTitlesFinished() + Values.STAT_SUF_FIN_TOT.getValue());
        statPagesRead.setText(Values.STAT_PRE_PAGES_TOT.getValue() + stats.get(0).getPagesRead() + Values.STAT_SUF_PAGES_TOT.getValue());
        statTitlesFavorite.setText(Values.STAT_PRE_FAVE_TOT.getValue() + stats.get(0).getFavorites() + Values.STAT_SUF_FAVE_TOT.getValue());
        statTitlesIgnore.setText(Values.STAT_PRE_BL_TOT.getValue() + stats.get(0).getBlasklisted() + Values.STAT_SUF_BL_TOT.getValue());
        statPagesDaily.setText(Values.STAT_PRE_PAGES_DAY.getValue() + stats.get(0).getDailyPages() + Values.STAT_SUF_PAGES_DAY.getValue());
        statGenreOne.setText(Values.STAT_PRE_GEN_ONE.getValue() + stats.get(0).getGenreOne() + ".");
        statGenreTwo.setText(Values.STAT_PRE_GEN_TWO.getValue() + stats.get(0).getGenreTwo() + ".");
        statGenreThree.setText(Values.STAT_PRE_GEN_THREE.getValue() + stats.get(0).getGenreThree() + ".");
    }

    public void readFromHistory(ActionEvent event) {
        historyPaneVisibility(false);
        Button button = (Button) event.getSource();
        System.out.println(history.get(Integer.parseInt(button.getId().substring(11))).getTitleId());
        System.out.println(history.get(Integer.parseInt(button.getId().substring(11))).getTitle());
    }

    public void historyShow() {
        historyPaneVisibility(true);
    }

    public void historyHide() {
        historyPaneVisibility(false);
    }

    public void statsShow() {
        statsPane.setVisible(true);
    }

    public void statsHide() {
        statsPane.setVisible(false);
    }

    void errorShow() {
        error.setVisible(true);
    }

    public void errorHide() {
        error.setVisible(false);
    }

    private void historyPaneVisibility(boolean visible) {
        historyPaneContent.setVisible(visible);
        historyClosePane.setVisible(visible);
    }

    public void mangaProblem() {

        int newLastChapRead = lastChapterReadNumber - 3;
        int newLastChapDl = getFirstChapter(selectedManga) - 1;

        if (newLastChapRead < 0) {
            newLastChapRead = 0;
        }
        if (newLastChapDl < 0) {
            newLastChapDl = 0;
        }

        database.openDb(Values.DB_NAME_DOWNLOADING.getValue());
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.modifyManga(Values.DB_TABLE_READING.getValue(), selectedManga, "last_chapter_read", newLastChapRead);
        database.modifyManga(Values.DB_TABLE_READING.getValue(), selectedManga, "last_chapter_downloaded", newLastChapDl);
        database.modifyManga(Values.DB_TABLE_READING.getValue(), selectedManga, "current_page", -1);
        database.downloadDbAttach();
        database.moveManga(Values.DB_ATTACHED_READING.getValue(), Values.DB_ATTACHED_DOWNLOADING.getValue(), selectedManga);
        database.deleteManga(Values.DB_ATTACHED_READING.getValue(), selectedManga);
        database.downloadDbDetach();
        database.closeDb();
        popupClose();
        databaseInit();
    }

    public void readManga() {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.createBookmark("resume_last_manga", selectedManga, totalChaptersNumber, lastChapterReadNumber, currentPageNumber);
        database.closeDb();
        readMangaBook();
        history = HistoryPane.storeHistory(history, selectedManga);
    }

    public void changeCompletedToReading() {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.modifyManga(Values.DB_TABLE_COMPLETED.getValue(), selectedManga, "last_chapter_read", getFirstChapter(selectedManga));
        database.moveManga(Values.DB_TABLE_COMPLETED.getValue(), Values.DB_TABLE_READING.getValue(), selectedManga);
        database.deleteManga(Values.DB_TABLE_COMPLETED.getValue(), selectedManga);
        database.closeDb();
        popupClose();
        lastChapterReadNumber = 0;
        currentPageNumber = 0;
        readMangaBook();
    }

    private int getFirstChapter(int mangaDir) {
        int dirNumber = 0;

        while (dirNumber < 9999) {
            if (Files.notExists(Paths.get(Values.DIR_ROOT.getValue() + File.separator + Values.DIR_MANGA.getValue() + File.separator + mangaDir + File.separator + dirNumber))) {
                dirNumber++;
            }
        }
        return dirNumber;
    }

    private void setNewChaptersFlagFalse() {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.modifyManga(Values.DB_TABLE_READING.getValue(), selectedManga, Values.DB_TABLE_NEW_CHAPTERS.getValue(), 0);
    }

    private void fetchManga(String pathToManga) {
        mangaPages.clear();
        File file = new File(pathToManga);
        mangaPages.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
    }

    private void readerRequestFocus() {
        scrollImagePane.requestFocus();
    }

    private void readMangaBook() {
        try {
            readMangaPane.setVisible(true);
            sidebarPane.setVisible(true);
            catalogPane.setVisible(false);
            popupClose();
            readerRequestFocus();
            fetchManga(Values.DIR_ROOT.getValue() + File.separator + Values.DIR_MANGA.getValue() + File.separator + selectedManga + File.separator + lastChapterReadNumber);
            if (currentPageNumber == -1) {
                currentPageNumber = mangaPages.size() - 1;
            }
            imageView.setFitWidth(920);
            imageView.setImage(new Image(new FileInputStream(mangaPages.get(currentPageNumber))));
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            sidebarChapterNumber.setText(Integer.toString(lastChapterReadNumber + 1));
            sidebarPageNumber.setText(Integer.toString(currentPageNumber + 1));
            sidebarTotalChapters.setText(Integer.toString(totalChaptersNumber));
            scrollSpeedText.setText(Integer.toString(scrollSpeed));
            setNewChaptersFlagFalse();
        } catch (Exception e) {
            //not possible
        }
    }

    public void scrollSpeedDown() {
        if (scrollSpeed > 1) {
            scrollSpeed--;
            displayScrollSpeed();
        }
    }

    public void scrollSpeedUp() {
        if (scrollSpeed < 10) {
            scrollSpeed++;
            displayScrollSpeed();
        }
    }

    private void displayScrollSpeed() {
        scrollSpeedText.setText(Integer.toString(scrollSpeed));
    }

    private void scrollUpDown(int direction) {
        Robot ghostMouse;

        try {
            ghostMouse = new Robot();

        if (direction == 1) {
            ghostMouse.mouseWheel(scrollSpeed);
        } else if (direction == -1) {
            ghostMouse.mouseWheel(-scrollSpeed);
        }
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }


    public void scrollWheel(ScrollEvent event) {

        if (event.getDeltaY() == 40.0) {
            event.consume();
            scrollUpDown(-1);
        } if (event.getDeltaY() == -40.0) {
            event.consume();
            scrollUpDown(1);
        }
    }

    public void turnMangaBookPage(KeyEvent event) {
        readerRequestFocus();
//        Robot ghostMouse = null;
//
//        try {
//            ghostMouse = new Robot();
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }

        if (event.getCode() == KeyCode.D && currentPageNumber < mangaPages.size() || event.getCode() == KeyCode.RIGHT && currentPageNumber < mangaPages.size()) {
            currentPageNumber++;
            displayNextOrPreviousMangaBookPage(currentPageNumber);
            scrollImagePane.setVvalue(0.0);
//            modifyThread.execute(this::calculateChapterNumber);
//            new Thread(mangaPageTurned).start();
            calculateChapterNumber();
            insertMangaBookmark();
        } else if (event.getCode() == KeyCode.A && currentPageNumber > 0 || event.getCode() == KeyCode.LEFT && currentPageNumber > 0) {
            currentPageNumber--;
            displayNextOrPreviousMangaBookPage(currentPageNumber);
            insertMangaBookmark();
        } else if (event.getCode() == KeyCode.A && currentPageNumber == 0 && lastChapterReadNumber > 0 || event.getCode() == KeyCode.LEFT && currentPageNumber == 0 && lastChapterReadNumber > 0) {
            lastChapterReadNumber--;
            currentPageNumber = -1;
            readMangaBook();
            // change current folder to lower and current page to highest available
            insertMangaBookmark();
        } else if (event.getCode() == KeyCode.W && scrollImagePane.getVvalue() > 0.0 || event.getCode() == KeyCode.UP && scrollImagePane.getVvalue() > 0.0) {
            scrollUpDown(-1);
//            ghostMouse.mouseWheel(-3);
        } else if (event.getCode() == KeyCode.S && scrollImagePane.getVvalue() < 1.0 || event.getCode() == KeyCode.DOWN && scrollImagePane.getVvalue() < 1.0) {
            scrollUpDown(1);
//            ghostMouse.mouseWheel(3);
        }
    }

    private void calculateChapterNumber() {
        int totalPages = mangaPages.size();
        if (currentPageNumber == totalPages) {
            lastChapterReadNumber++;
            currentPageNumber = 0;
            readMangaBook();
        }

        if (lastChapterReadNumber == totalChaptersNumber) {
            finishedReading();
        }
    }

    private void insertMangaBookmark() {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.modifyManga("currently_reading", selectedManga, "last_chapter_read", lastChapterReadNumber);
        database.modifyManga("currently_reading", selectedManga, "current_page", currentPageNumber);
        database.createBookmark("resume_last_manga", selectedManga, totalChaptersNumber, lastChapterReadNumber, currentPageNumber);
        database.closeDb();
    }

    private void finishedReading() {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.moveManga(Values.DB_TABLE_READING.getValue(), Values.DB_TABLE_COMPLETED.getValue(), selectedManga);
        database.deleteManga(Values.DB_TABLE_READING.getValue(), selectedManga);
        database.createBookmark(Values.DB_TABLE_BOOKMARK.getValue(), -1, -1, -1, -1);
        database.closeDb();
        imageView.setImage(null);
        readMangaPane.setVisible(false);
        catalogPane.setVisible(true);
        sidebarPane.setVisible(false);
        currentActivity.setValue("My Library");
        databaseInit();
    }

    public void sidebarVisible() {
        sidebarPane.setOpacity(1);
    }

    public void sidebarInvisible() {
        sidebarPane.setOpacity(0);
        readerRequestFocus();
    }

    public void sidebarFavorite() {
        if (currentActivity.getValue() != null) {
            MangaListView view = mangaId.get(thumbNumber);
            view.setFavorite(true);
            populateDisplay();
        }

        MangaListView view = mangaId.get(thumbNumber);
        view.setFavorite(true);
        populateDisplay();

        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.modifyManga("currently_reading", selectedManga, "favorite", 1);
        database.closeDb();
        // modify the manga list itself to change favorite value from 0 to 1 and then run populate display. allows us to add a favorite without querying the database
    }

    public void mangaBookPageZoomInOrOut(ActionEvent event) {
        Button button = (Button) event.getSource();

        if (button.getId().equals("sidebarPane_size_decrease")) {
            switch (String.valueOf(imageView.getFitWidth())) {
                case "1920.0":
                    imageView.setFitWidth(1720);
                    sidebarPaneSizeIncrease.setVisible(true);
                    break;
                case "1720.0":
                    imageView.setFitWidth(1520);
                    break;
                case "1520.0":
                    imageView.setFitWidth(1320);
                    break;
                case "1320.0":
                    imageView.setFitWidth(1120);
                    break;
                case "1120.0":
                    imageView.setFitWidth(920);
                    sidebarPaneSizeDecrease.setVisible(false);
                    break;
            }
        } else if (button.getId().equals("sidebarPane_size_increase")) {
            switch (String.valueOf(imageView.getFitWidth())) {
                case "920.0":
                    imageView.setFitWidth(1120);
                    sidebarPaneSizeDecrease.setVisible(true);
                    break;
                case "1120.0":
                    imageView.setFitWidth(1320);
                    break;
                case "1320.0":
                    imageView.setFitWidth(1520);
                    break;
                case "1520.0":
                    imageView.setFitWidth(1720);
                    break;
                case "1720.0":
                    imageView.setFitWidth(1920);
                    sidebarPaneSizeIncrease.setVisible(false);
                    break;
            }
        }
    }

    public void sidebarGoBack() {
        imageView.setImage(null);
        readMangaPane.setVisible(false);
        sidebarPane.setVisible(false);
        catalogPane.setVisible(true);
        if (currentActivity.getValue() != null) {
            databaseInit();
        }
    }

    public void sidebarGotoChapter() {
        if (!gotoChapter.getText().equals("")) {
            lastChapterReadNumber = (Integer.parseInt(gotoChapter.getText()) - 1);
            currentPageNumber = 0;
            gotoChapter.clear();
            readMangaBook();
            insertMangaBookmark();
        }
    }

    private void forceNumbersOnly(Number oldValue, Number newValue, TextField textField) {
        if (newValue.intValue() > oldValue.intValue()) {
            char character = textField.getText().charAt(oldValue.intValue());
            if (!(character >= '0' && character <= '9')) {
                textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
            }
        }
        if (gotoChapter.getText().length() > 0) {
            minMaxNumber();
        }
    }

    private void minMaxNumber() {
        try {
            if (Integer.parseInt(gotoChapter.getText()) <= 0 && gotoChapter.getText().length() > 0) {
                gotoChapter.setText("1");
            }
            if (Integer.parseInt(gotoChapter.getText()) > totalChaptersNumber) {
                gotoChapter.setText(Integer.toString(totalChaptersNumber));
            }
        } catch (NumberFormatException e) {
            Platform.runLater(() -> gotoChapter.clear());
        }
    }

    public void goToLastMangaBookPageViewed() {
        currentActivity.setValue("My Library");
        database.openDb(Values.DB_NAME_MANGA.getValue());
        ResultSet resultSet = database.fetchTableData("resume_last_manga");

        try {
            if (resultSet.next()) {
                selectedManga = resultSet.getInt("title_id");
                totalChaptersNumber = resultSet.getInt("total_chapters");
                lastChapterReadNumber = resultSet.getInt("last_chapter_read");
                currentPageNumber = resultSet.getInt("current_page");
                database.closeDb();
                readMangaBook();
            } else {
                launchPane.setVisible(false);
                catalogPane.setVisible(true);
                databaseInit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private Runnable mangaPageTurned = () -> {
////        database.modifyManga("currently_reading", selectedManga, "current_page", currentPageNumber);
//        calculateChapterNumber();
//    };

    private void displayNextOrPreviousMangaBookPage(int currentPage) {
        try {
            FileInputStream inputStream = new FileInputStream(mangaPages.get(currentPage));
            Image mangaPage = new Image(inputStream);
            imageView.setImage(mangaPage);
            sidebarPageNumber.setText(Integer.toString(currentPage + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAvailableMangaBooksPane() {
        popupClose();
        launchPane.setVisible(false);
        currentActivity.setValue("Available Manga");
        catalogPane.setVisible(true);
    }

    public void showOwnedMangaBooksPane() {
        popupClose();
        launchPane.setVisible(false);
        currentActivity.setValue("My Library");
        catalogPane.setVisible(true);
//        databaseInit();
    }

    public void genreIncludeExclude(ActionEvent event) {
        joiner = new StringJoiner(" AND ");
        CheckBox checkBox = (CheckBox) event.getSource();
        int indexNumber = (Integer.parseInt(checkBox.getId().substring(5)));
//        StringJoiner joiner = new StringJoiner(" AND ");
//        GenreMap genreMap = new GenreMap();
//        Map<String, String> genres = genreMap.getGenreMap();

        popupClose();
        pageNumber = 0;

        if (checkBox.isSelected()) {
            genreStrings.set(indexNumber, "genre" + indexNumber + ".include");
        } else if (!checkBox.isSelected() && !checkBox.isIndeterminate()) {
            genreStrings.set(indexNumber, "genre" + indexNumber + ".exclude");
        } else if (checkBox.isIndeterminate()) {
            genreStrings.set(indexNumber, "");
        }
        searchStringBuilder();
        databaseInit();
    }

    private void searchStringBuilder() {
        Map<String, String> genres = GenreMap.getGenreMap();

        for (int i = 0; i < 39; i++) {
            if (genreStrings.get(i).length() > 0) {
                joiner.add(genres.get(genreStrings.get(i)));
            }
        }
    }

    public void ignoreManga() {
        if (currentActivity.getValue().equals("Not Interested")) {
            if (lastChapterDownloaded == totalChaptersNumber) {
                modifyThread.execute(this::moveToReading);
            } else {
                modifyThread.execute(this::moveToDownloading);
            }
        } else {
            database.openDb(Values.DB_NAME_MANGA.getValue());
            database.moveManga(currentDatabase(), "not_interested", selectedManga);
            database.deleteManga(currentDatabase(), selectedManga);
            database.closeDb();
        }
        popupClose();
        modifyThread.execute(this::databaseInit);
        navButtonVisibility();
    }

    public void ascendDescend() {
        popupClose();
        databaseInit();
    }

    public void genreReset() {
        popupClose();

        for (int i = 0; i < 39; i++) {
            CheckBox genres = checkBoxes.get(i);
            genres.setSelected(false);
            genres.setIndeterminate(true);
            genreStrings.add(i, "");
        }
        joiner = new StringJoiner(" AND ");
        databaseInit();
    }

    private String currentDatabase() {
        String database = "";

        switch (currentActivity.getValue()) {
            case "Available Manga":
                database = Values.DB_TABLE_AVAILABLE.getValue();
                break;
            case "My Library":
                database = Values.DB_TABLE_READING.getValue();
                break;
            case "Not Interested":
                database = Values.DB_TABLE_NOT_INTERESTED.getValue();
                break;
            case "Finished Reading":
                database = Values.DB_TABLE_COMPLETED.getValue();
                break;
        }
        return database;
    }

    public void searchBox() {
        popupClose();
        if (searchBox.getText().equals("")) {
            //future popup maybe
        } else {
            joiner = new StringJoiner(" AND ");
            searchStringBuilder();
            joiner.add("summary LIKE '%" + searchBox.getText() + "%' OR title LIKE '%" + searchBox.getText() + "%'");
            searchBox.clear();
            databaseInit();
        }
    }

    public void choiceBox() {
        popupClose();
        pageNumber = 0;
        joiner = new StringJoiner(" AND ");
        databaseInit();
    }

    private void databaseInit() {
        String sortOrder;

        if (ascendDescend.isSelected()) {
            sortOrder = "ASC";
        } else {
            sortOrder = "DESC";
        }
        if (joiner.length() > 0) {
            if (currentActivity.getValue().equals("My Library")) {
                dbSearch("title_id, new_chapters, favorite", currentDatabase(), " WHERE " + joiner.toString(), sortOrder);
            } else {
                dbSearch("title_id", currentDatabase(), " WHERE " + joiner.toString(), sortOrder);
            }
        } else if (joiner.length() == 0) {
            if (currentActivity.getValue().equals("My Library")) {
                dbSearch("title_id, new_chapters, favorite", currentDatabase(), "", sortOrder);
            } else {
                dbSearch("title_id", currentDatabase(), "", sortOrder);
            }
        }
    }

    private void dbSearch(String columnName, String tableName, String requestedData, String sortOrder) {
        String sortByValue;

        if (currentActivity.getValue().equals("My Library")) {
            sortByValue = " ORDER BY new_chapters DESC, title_id ";
        } else {
            sortByValue = " ORDER BY title_id ";
        }

        mangaId.clear();
        database.openDb(Values.DB_NAME_MANGA.getValue());
        ResultSet resultSet = database.workingGenreFilter(columnName, tableName, requestedData, sortByValue, sortOrder);


        try {
            if (resultSet.next()) {
                do {
                    if (currentActivity.getValue().equals("My Library")) {
                        mangaId.add(new MangaListView(resultSet.getInt("title_id"), resultSet.getBoolean("new_chapters"), resultSet.getBoolean("favorite")));
                    } else {
//                            mangaId.add(resultSet.getString("title_id"));
                        mangaId.add(new MangaListView(resultSet.getInt("title_id"), false, false));
                    }
                } while (resultSet.next());
            }
        } catch (Exception e) {
            System.out.println(e + "  supposedly not possible fml");
        }

        database.closeDb();
        populateDisplay();
    }

    private void populateDisplay() {
        clearThumbs();
        hideThumbs();
        int numberOfLoops = 30;

        if (mangaId.size() - pageNumber < 30) {
            numberOfLoops = mangaId.size() - pageNumber;
        }


        try {
            for (int i = 0; i < numberOfLoops; i++) {
                MangaListView view = mangaId.get(i + pageNumber); // totally unnecessary new arraylist being created... with a single item in it. stupid
                thumbImage = imageViews.get(i);
                favoriteOverlay = favoriteOverlayViews.get(i);
                newChaptersOverlay = newChaptersOverlayViews.get(i);
                thumbImage.setSmooth(true);
                thumbImage.setImage(new Image(new FileInputStream(thumbsPath + File.separator + mangaId.get(i + pageNumber).getMangaNumber() + ".jpg")));
                if (view.isFavorite()) {
                    favoriteOverlay.setImage(new Image("assets/favorite_overlay.png"));
                }
                if (view.isNewChapters()) {
                    newChaptersOverlay.setImage(new Image("assets/new_chapter_multiple.png"));
                }
                thumbImage.setVisible(true);
                favoriteOverlay.setVisible(true);
                newChaptersOverlay.setVisible(true);
            }
        } catch (Exception e) {
            System.out.println(e + "   super vague?");
        }
        navButtonVisibility();
    }

    public void turnIndexPage(ActionEvent event) {
        Button button = (Button) event.getSource();
        popupClose();
        int arraySize = mangaId.size();
//        int remainder = 30 - (((mangaId.size() / 30) * 30) - (mangaId.size() % 30));
        int remainder = 30 * (arraySize / 30);

        if (remainder == arraySize) {
            remainder = remainder - 30;
        }

        System.out.println(remainder);
        System.out.println(mangaId.size());

        switch (button.getId()) {
            case "pageFirst":
                pageNumber = 0;
                break;
            case "pagePrevious":
                pageNumber = pageNumber - 30;
                break;
            case "pageNext":
                pageNumber = pageNumber + 30;
                break;
            case "pageLast":
                pageNumber = remainder;
                break;
        }
        populateDisplay();
    }

    public void popupOpen(MouseEvent event) throws Exception {

        innerPopupClose();
        ImageView image = (ImageView) event.getSource();

        thumbNumber = (Integer.parseInt(image.getId().substring(18)));

        ignoreMangaShort.setVisible(false);
        favorite.setVisible(false);
        readManga.setVisible(false);
        ignoreManga.setVisible(false);
        getInnerPopup.setVisible(false);
        unignore.setVisible(false);
        rereadManga.setVisible(false);
        getIssuePopup.setDisable(false);


        if (currentActivity.getSelectionModel().getSelectedItem().equals("My Library")) {
            ignoreMangaShort.setVisible(true);
            favorite.setVisible(true);
            readManga.setVisible(true);
        } else if (currentActivity.getSelectionModel().getSelectedItem().equals("Available Manga")) {
            ignoreManga.setVisible(true);
            getInnerPopup.setVisible(true);
            getIssuePopup.setDisable(true);
        } else if (currentActivity.getSelectionModel().getSelectedItem().equals("Not Interested")) {
            unignore.setVisible(true);
            getIssuePopup.setDisable(true);
        } else if (currentActivity.getSelectionModel().getSelectedItem().equals("Finished Reading")) {
            ignoreMangaShort.setVisible(true);
            favorite.setVisible(true);
            rereadManga.setVisible(true);
            getIssuePopup.setDisable(true);
        }

        if (thumbNumber <= 14) {
            popupPane.setLayoutX(1144);
            innerPopup.setLayoutX(1329);
            repairPopup.setLayoutX(1329);
        } else {
            popupPane.setLayoutX(15);
            innerPopup.setLayoutX(200);
            repairPopup.setLayoutX(200);
        }
        popupPane.setVisible(true);

        database.openDb(Values.DB_NAME_MANGA.getValue());
//        ResultSet results = database.filterManga(currentDatabase(), "title_id", mangaId.get(thumbNumber + pageNumber));
        MangaListView view = mangaId.get(thumbNumber + pageNumber);
        //redundant arraylist, this resultset needs to be utalized in a separate method
        ResultSet results = database.filterManga(currentDatabase(), "title_id", Integer.toString(mangaId.get(thumbNumber + pageNumber).getMangaNumber()));

        if (results.getString("title").length() > 63) {
            String finalTitle = results.getString("title").substring(0, 63) + " ...";
            mangaTitle.setText(finalTitle);
        } else {
            mangaTitle.setText(results.getString("title"));
        }
        mangaThumb.setImage(new Image(new FileInputStream(thumbsPath + File.separator + view.getMangaNumber() + ".jpg")));
        mangaSummary.setText(results.getString("summary"));
        mangaGenres.setText(results.getString("genre_tags").substring(0, results.getString("genre_tags").length() - 1));
        mangaAuthors.setText(results.getString("authors").substring(0, results.getString("authors").length() - 1));
//        webAddress = results.getString("web_address");
        totalChaptersNumber = results.getInt("total_chapters");
        currentPageNumber = results.getInt("current_page");
        lastChapterReadNumber = results.getInt("last_chapter_read");
        lastChapterDownloaded = results.getInt("last_chapter_downloaded");

        database.closeDb();
        results.close();
        selectedManga = mangaId.get(thumbNumber + pageNumber).getMangaNumber();
    }

    private void populateDatabase(String columnName, int mangaId, boolean isHistory) {

    }

    public void popupClose() {
        popupPane.setVisible(false);
        innerPopupClose();
    }

    private void innerPopupClose() {
        innerPopup.setVisible(false);
        haveNotRead.setSelected(false);
        haveRead.setSelected(false);
        lastChapterRead.clear();
        innerPopupNumbox.setVisible(false);
        repairPopup.setVisible(false);
    }

    public void showInnerPopup() {
        innerPopup.setVisible(true);
    }

    public void showRepairPopup() {
        repairPopup.setVisible(true);
    }

    public void getMangaButton() {
        startReading.setVisible(true);
        if (haveRead.isSelected()) {
            innerPopupNumbox.setVisible(true);
            lastChapterRead.requestFocus();
        } else if (haveNotRead.isSelected()) {
            innerPopupNumbox.setVisible(false);
            lastChapterRead.clear();
        }
    }

    public void getThisManga() {
        startingChapter = 0;
//        int totalChapters = 0;
        if (lastChapterRead.getText().length() > 0) {
            startingChapter = Integer.parseInt(lastChapterRead.getText());
        }

//        if (startingChapter > 0 && haveRead.isSelected() || haveNotRead.isSelected()) {
//            totalChapters = writePreDownloadValues();
//        } else

        if (startingChapter <= 0 && haveRead.isSelected()) {
            lastChapterRead.requestFocus();
        } else {
            if (haveRead.isSelected() && lastChapterRead.getText().length() > 0) {
                startingChapter = Integer.parseInt(lastChapterRead.getText());
                writePreDownloadValues();
            }
            popupClose();
            modifyThread.execute(this::moveToDownloading);
            haveRead.setSelected(false);
            haveNotRead.setSelected(false);
            modifyThread.execute(this::databaseInit);
        }

//        if (startingChapter < totalChapters) {
//            modifyThread.execute(this::moveToDownloading);
//        } else {
//            modifyThread.execute(this::moveToCompleted);
//        }
    }

    private void writePreDownloadValues() {
//        ArrayList chapterCount = null;
//        try {
//            chapterCount = indexMangaChapters.getChapterCount(webAddress);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.modifyManga(Values.DB_TABLE_AVAILABLE.getValue(), selectedManga, "last_chapter_read", startingChapter);
//        database.modifyManga(Values.DB_TABLE_AVAILABLE.getValue(), selectedManga, "total_chapters", chapterCount.size());
        database.closeDb();
//        return chapterCount.size();
    }

    private void moveToReading() {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.moveManga(currentDatabase(), Values.DB_TABLE_READING.getValue(), selectedManga);
        database.deleteManga(currentDatabase(), selectedManga);
        database.closeDb();
        joiner = new StringJoiner(" AND ");
    }

    private void moveToDownloading() {
        database.openDb(Values.DB_NAME_DOWNLOADING.getValue());
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.downloadDbAttach();
        database.moveManga(Values.DB_ATTACHED_PREFIX.getValue() + currentDatabase(), Values.DB_ATTACHED_DOWNLOADING.getValue(), selectedManga);
        database.deleteManga(Values.DB_ATTACHED_PREFIX.getValue() + currentDatabase(), selectedManga);
        database.downloadDbDetach();
        database.closeDb();
        database.closeDb();
        joiner = new StringJoiner(" AND ");
//        database.downloadQueueAdd("downloading", selectedManga, webAddress, startingChapter, 0);
//        database.closeDb();
    }

    private Runnable downloadManga = () -> {
        System.out.println("downloading");
        boolean firstDownload;
        int downloadingStartingChapter;
        int downloadingLastChapterDownloaded;
        int downloadingCurrentPage;
        int downloadingTitleId;
        String downloadingWebAddress;
        database.openDb(Values.DB_NAME_DOWNLOADING.getValue());
        ResultSet resultSet = database.downloadQueueFetch("downloading");

        try {
            if (resultSet.next()) {
                firstDownload = true;
                downloadingStartingChapter = resultSet.getInt("last_chapter_read");
                downloadingLastChapterDownloaded = resultSet.getInt("last_chapter_downloaded");
                downloadingTitleId = resultSet.getInt("title_id");
                downloadingWebAddress = AddressCheckAndCorrect.verifyAddress(resultSet.getString("web_address"), resultSet.getString("title"));
                database.modifyManga("downloading", downloadingTitleId, "web_address", downloadingWebAddress);
                downloadingCurrentPage = resultSet.getInt("current_page");
                database.closeDb();

                if (downloadingCurrentPage == -1) {
                    firstDownload = false;
                    //might be able to use this for downloading new titles in full; downloadingStartingChapter = 0 replaces above and above comes down here.
                }

                if (downloadingLastChapterDownloaded > downloadingStartingChapter) {
                    downloadingStartingChapter = downloadingLastChapterDownloaded; //so this makes sure that a resumed download starts at the last chapter that the last download left off
                }
                downloadMangaPages.getChapterPages(downloadingStartingChapter, downloadingWebAddress, downloadingTitleId, firstDownload);
            }
        } catch (Exception e) {
            System.out.println("errorShow with runnable downloadManga " + e);
        }
    };

    private void navButtonVisibility() {
        if (mangaId.size() > 30) {
            pageNumTextPane.setVisible(true);
        } else {
            pageNumTextPane.setVisible(false);
        }

        if (pageNumber == 0 && mangaId.size() > 30) {
            pageFirst.setVisible(false);
            pagePrevious.setVisible(false);
            pageNext.setVisible(true);
            pageLast.setVisible(true);
        } else if (pageNumber >= 30 && pageNumber < mangaId.size() - 30) {
            pageFirst.setVisible(true);
            pagePrevious.setVisible(true);
            pageNext.setVisible(true);
            pageLast.setVisible(true);
        } else if (pageNumber != 0 && pageNumber == (mangaId.size() - mangaId.size() % 30) - 30 && mangaId.size() % 30 == 0 || pageNumber != 0 && pageNumber == (mangaId.size() - mangaId.size() % 30)) {
            pageFirst.setVisible(true);
            pagePrevious.setVisible(true);
            pageNext.setVisible(false);
            pageLast.setVisible(false);
        } else {
            pageFirst.setVisible(false);
            pagePrevious.setVisible(false);
            pageNext.setVisible(false);
            pageLast.setVisible(false);
        }
        pageNumberDisplay();
    }

    private void pageNumberDisplay() {
        int currentPageNumber = (pageNumber / 30) + 1;
        int totalPagesNumber = mangaId.size() / 30;

        if (mangaId.size() % 30 > 0) {
            totalPagesNumber++;
        }

        if (currentPageNumber < 10) {
            currentPageNumberDisplay.setLayoutX(1669);
        } else if (currentPageNumber < 100) {
            currentPageNumberDisplay.setLayoutX(1655);
        } else {
            currentPageNumberDisplay.setLayoutX(1642);
        }

        currentPageNumberDisplay.setText(Integer.toString(currentPageNumber));
        lastPageNumberDisplay.setText(Integer.toString(totalPagesNumber));
    }

    public void gotoSpecificPage() {
        popupClose();
        if (!enterPageNumber.getText().equals("")) {
            pageNumber = (Integer.parseInt(enterPageNumber.getText()) - 1) * 30;
            enterPageNumber.clear();
            populateDisplay();
        }
    }

    private void clearThumbs() {
        for (int i = 0; i < 30; i++) {
            thumbImage = imageViews.get(i);
            favoriteOverlay = favoriteOverlayViews.get(i);
            newChaptersOverlay = newChaptersOverlayViews.get(i);
            thumbImage.setImage(null);
            favoriteOverlay.setImage(null);
            newChaptersOverlay.setImage(null);
        }
    }

    private void hideThumbs() {
        for (int i = 0; i < 30; i++) {
            thumbImage = imageViews.get(i);
            favoriteOverlay = favoriteOverlayViews.get(i);
            newChaptersOverlay = newChaptersOverlayViews.get(i);
            thumbImage.setVisible(false);
            favoriteOverlay.setVisible(false);
            newChaptersOverlay.setVisible(false);
        }
    }

//    public void lastPage() throws Exception {
//        clearThumbs();
//        hideThumbs();
//        popupClose();
////        int position = mangaId.size() - mangaId.size() % 30; - this is a duplicate of pagenumber, whats its purpose? lol I dont know.
//        int view = 0;
//        pageNumber = mangaId.size() - mangaId.size() % 30;
//
//        for (int i = pageNumber; view < mangaId.size() % 30; i++) {
//            MangaListView listView = mangaId.get(i);
//            thumbImage = imageViews.get(view);
//            favoriteOverlay = favoriteOverlayViews.get(view);
//            newChaptersOverlay = newChaptersOverlayViews.get(view);
//            thumbImage.setImage(new Image(new FileInputStream(thumbsPath + File.separator + listView.getMangaNumber() + ".jpg"), 167, 250, false, true));
//            thumbImage.setVisible(true);
//            favoriteOverlay.setVisible(true);
//            newChaptersOverlay.setVisible(true);
//            view++;
//        }
//        navButtonVisibility();
//    }

    //    private void moveToPendingDownload() {
//        String tableName = null;
//        if (currentActivity.getValue().equals("Available Manga")) {
//            tableName = Values.DB_TABLE_AVAILABLE.getValue();
//        } else if (currentActivity.getValue().equals("Not Interested")) {
//            tableName = Values.DB_TABLE_NOT_INTERESTED.getValue();
//        }
//
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        database.moveManga(tableName, Values.DB_TABLE_DOWNLOAD_PENDIND.getValue(), selectedManga);
//        database.closeDb();
//    }

    //    public void completeIncomplete() throws Exception {
//
//        //NEEDS TO BE A BUTTON!! THREE STAGE not currently in use!!
//        if (completeIncomplete.isSelected()) {
//
//        }
//
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        ResultSet results = database.filterManga("available_manga", "status", "Ongoing");
//
//        while (results.next()) {
//            System.out.println(results.getString("title"));
//        }
//        database.closeDb();
//    }

//    private void moveToCompleted() {
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        database.moveManga(currentDatabase(), Values.DB_TABLE_COMPLETED.getValue(), selectedManga);
//        database.deleteManga(currentDatabase(), selectedManga);
//        database.closeDb();
//        joiner = new StringJoiner(" AND ");
//    }

//    private void chapterNumber() {
//
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        ResultSet resultSet = database.filterManga("currently_reading", "title_id", Integer.toString(selectedManga));
//
//        try {
//            totalChaptersNumber = Integer.parseInt(resultSet.getString("total_chapters"));
//            currentPageNumber = Integer.parseInt(resultSet.getString("current_page"));
//            lastChapterReadNumber = Integer.parseInt(resultSet.getString("last_chapter_read"));
//            database.createBookmark("resume_last_manga", selectedManga, totalChaptersNumber, lastChapterReadNumber, currentPageNumber);
//        } catch (SQLException e) {
////            e.printStackTrace();
//            System.out.println(e);
//        }
//
////        if (currentPage.equals("null")) {
////            currentPage = "0";
////        }
////        if (lastChapterRead.equals("null")) {
////            lastChapterRead = "0";
////        }
//
//        database.closeDb();
//    }

//    public void onlyComplete() throws Exception {
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        ResultSet results = database.filterManga("available_manga", "status", "Completed");
//
//        while (results.next()) {
//            System.out.println(results.getString("title"));
//        }
//        database.closeDb();
//    }


}

//class Changes implements ChangeListener<Number> {
//
//    @Override
//    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//
//        for (Field fields : observable.getClass().)) {
//            System.out.println(fields.getName());
//        }
//
////        if (observable.getClass().getCanonicalName().length() == 64) {
////            System.out.println("double");
////        } else {
////            System.out.println("int");
////        }
//    }
//}




//    public void genreIncludeExclude(ActionEvent event) {
//        joiner = new StringJoiner(" && ");
//        CheckBox checkBox = (CheckBox) event.getSource();
//        int indexNumber = (Integer.parseInt(checkBox.getId().substring(5)));
////        StringJoiner joiner = new StringJoiner(" AND ");
////        GenreMap genreMap = new GenreMap();
////        Map<String, String> genres = genreMap.getGenreMap();
//
//        popupClose();
//        pageNumber = 0;
//
//        if (checkBox.isSelected()) {
//            genreStrings.set(indexNumber, "genre" + indexNumber + ".include");
//        } else if (!checkBox.isSelected() && !checkBox.isIndeterminate()) {
//            genreStrings.set(indexNumber, "genre" + indexNumber + ".exclude");
//        } else if (checkBox.isIndeterminate()) {
//            genreStrings.set(indexNumber, "");
//        }
//        searchStringBuilder();
//        mangaId.clear();
//
////        Predicate<MangaListView> predicate = m -> joiner;
////        populateDisplay();
//    }
//
//    private void searchStringBuilder() {
//        Map<String, Predicate<MangaListView>> genres = GenreMap.getGenreMap();
//        List<Predicate<MangaListView>> predicateList = new ArrayList();
//
//        for (int i = 0; i < 39; i++) {
//            if (genreStrings.get(i).length() > 0) {
//                System.out.println(genreStrings.get(i));
////                joiner.add(genres.get(genreStrings.get(i)));
//                predicateList.add(genres.get(genreStrings.get(i)));
//
//
//            }
//
//            Predicate<MangaListView> compositePredicate = predicateList.stream().reduce(w -> true, Predicate::and);