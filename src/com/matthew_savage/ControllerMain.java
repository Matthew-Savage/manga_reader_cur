package com.matthew_savage;

import com.matthew_savage.GUI.InfoBox;
import com.matthew_savage.GUI.MangaZoomInOut;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private TextField mangaTitle;
    @FXML
    private TextField mangaGenres;
    @FXML
    private TextField mangaAuthors;
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
    private static ArrayList<MangaArrayList> blacklist = new ArrayList<>();
    private static ArrayList<MangaArrayList> completed = new ArrayList<>();
    private static ArrayList<MangaArrayList> reading = new ArrayList<>();
    private static ArrayList<MangaArrayList> history = new ArrayList<>();
    private static ArrayList<MangaArrayList> bookmark = new ArrayList<>();
    private static ArrayList<MangaArrayList> downloading = new ArrayList<>();
    private static ArrayList<StatsArrayList> stats = new ArrayList<>();
    private static ArrayList<MangaArrayList> currentContent = new ArrayList<>();
    private static List<Predicate<MangaArrayList>> predicateList = new ArrayList<>();
    private static ArrayList<MangaArrayList> historyInverted = new ArrayList<>();

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
    private int startingChapter;
    private int pageNumber = 0;
    private int selectionIdentNum = -1;
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
        available = ControllerLoader.getAvailable();
        blacklist = ControllerLoader.getBlacklist();
        completed = ControllerLoader.getCompleted();
        reading = ControllerLoader.getReading();
        history = ControllerLoader.getHistory();
        bookmark = ControllerLoader.getBookmark();
        downloading = ControllerLoader.getDownloading();
        stats = ControllerLoader.getStats();

        currentActivity.setItems(FXCollections.observableArrayList("Available Manga", "My Library", "Finished Reading", "Not Interested"));

        //we can get rid of genrestrings I.. think? lol
        //the rest of these arraybuilders need to move to a method and run in thier own thread minus the ones that will display instantly I guess huh?

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

        executor.execute(this::checkForHistory);
        executor.execute(this::populateStats);
        executor.execute(this::createGenreStringArray);
        executor.execute(this::displayInternetStatus);

        //maybe we can class this with TextField textField, maybe a public static textField that I set and then call via a getter

//        lastChapterRead.lengthProperty().addListener(new ChangeListener<>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                forceNumbersOnly(oldValue, newValue, lastChapterRead);
//            }
//        });

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
        currentActivity.setValue("My Library");
    }


    //========================================================

    public void toggleZoomControls() {
        if (imageView.getFitWidth() == 1720) {
            sidebarPaneSizeIncrease.setVisible(true);
        } else if (imageView.getFitWidth() == 920) {
            sidebarPaneSizeDecrease.setVisible(false);
        }
    }

    public void mangaBookPageZoomInOrOut(ActionEvent event) {
        imageView.setFitWidth(MangaZoomInOut.widthValue((Button) event.getSource(), imageView.getFitWidth()));
        toggleZoomControls();
    }

    //========================================================

    private Runnable updateShit = UpdateCollectedMangas::seeIfUpdated;

    public void appClose() {
        downloadThread.shutdownNow();
        Stage stage = (Stage) appClose.getScene().getWindow();
        stage.close();
    }

    public void appMinimize() {
        Stage stage = (Stage) appMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    private void displayInternetStatus() {
        noInternet.setSmooth(true);
        noInternet.setImage(new Image("assets/no_internet.png"));

        if (ControllerLoader.isOnline()) {
            noInternet.setVisible(false);
            //checkforupdates will launch download
//            checkIfUpdated.scheduleWithFixedDelay(updateShit, 0, 30, TimeUnit.MINUTES);
//            downloadThread.scheduleWithFixedDelay(downloadManga, 120, 10, TimeUnit.SECONDS);
        }
    }

    private void checkForHistory() {
        mapHistoryPane();

        if (history.size() > 0) {
            try {
                populateHistory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void historyShow() {
        try {
            populateHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void mapHistoryPane() {
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
        }
        if (event.getDeltaY() == -40.0) {
            event.consume();
            scrollUpDown(1);
        }
    }

    private void setNewChaptersFlagFalse() {
        currentContent.get(thumbNumber + pageNumber).setNewChapters(false);
        reading.get(fetchOriginalIndexNumber()).setNewChapters(false);
    }

    private void fetchMangaPages(String pathToManga) {
        mangaPages.clear();
        File file = new File(pathToManga);
        mangaPages.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
    }

    private void readerRequestFocus() {
        scrollImagePane.requestFocus();
    }

    private void populateHistory() throws Exception {
        historyInverted.clear();
        historyInverted.addAll(history);
//        Collections.reverse(historyInverted);

        for (int i = 0; i < historyInverted.size(); i++) {
            historyThumbViews.get(i).setSmooth(true);
            historyThumbViews.get(i).setImage(new Image(new FileInputStream(thumbsPath + File.separator + historyInverted.get(i).getTitleId() + ".jpg")));
            historyTitleFields.get(i).setText(historyInverted.get(i).getTitle());
            historySummaryFields.get(i).setText(historyInverted.get(i).getSummary());
            historyReadButtons.get(i).setVisible(true);
        }
    }

    public void openFromHistory(ActionEvent event) {
//        historyPaneVisibility(false);
        Button button = (Button) event.getSource();


        System.out.println(historyInverted.get(0).getTitleId());

        System.out.println(historyInverted.get(Integer.parseInt(button.getId().substring(11))).getTitleId());

        selectionIdentNum = historyInverted.get(Integer.parseInt(button.getId().substring(11))).getTitleId();

        totalChaptersNumber = historyInverted.get(Integer.parseInt(button.getId().substring(11))).getTotalChapters();
        currentPageNumber = historyInverted.get(Integer.parseInt(button.getId().substring(11))).getCurrentPage();
        lastChapterReadNumber = historyInverted.get(Integer.parseInt(button.getId().substring(11))).getLastChapterRead();
        lastChapterDownloaded = historyInverted.get(Integer.parseInt(button.getId().substring(11))).getLastChapterDownloaded();
        beginReading(historyInverted.get(Integer.parseInt(button.getId().substring(11))).getTitleId());
        bookmark.set(0, historyInverted.get(Integer.parseInt(button.getId().substring(11))));
    }

    public void mangaProblem() {

        if (currentContent.get(thumbNumber + pageNumber).getLastChapterRead() - 3 < 0) {
            currentContent.get(thumbNumber + pageNumber).setLastChapterRead(0);
        } else {
            currentContent.get(thumbNumber + pageNumber).setLastChapterRead(currentContent.get(thumbNumber + pageNumber).getLastChapterRead() - 3);
        }

        currentContent.get(thumbNumber + pageNumber).setLastChapterDownloaded(0);
        //whats this fucking -1 page shit, pretty sure we dont need this anymore
        currentContent.get(thumbNumber + pageNumber).setCurrentPage(-1);
        getThisManga();
    }

    public void getThisManga() {
        downloading.add(currentContent.get(thumbNumber + pageNumber));
        Objects.requireNonNull(setArray()).remove(fetchOriginalIndexNumber());
        currentContent.remove(thumbNumber + pageNumber);
        popupClose();
        searchStringBuilder();
    }

    public void preReadingTasks() {
        bookmark.set(0, currentContent.get(thumbNumber + pageNumber));
        if (!HistoryPane.storeHistory(history, currentContent.get(thumbNumber + pageNumber).getTitleId())) {
            history.add(currentContent.get(thumbNumber + pageNumber));
        }
        selectionIdentNum = fetchOriginalIndexNumber();
        beginReading(currentContent.get(thumbNumber + pageNumber).getTitleId());
    }

    public void changeCompletedToReading() {
        currentContent.get(thumbNumber + pageNumber).setLastChapterRead(0);
        currentContent.get(thumbNumber + pageNumber).setCurrentPage(0);
        reading.add(currentContent.get(thumbNumber + pageNumber));
        beginReading(currentContent.get(thumbNumber + pageNumber).getTitleId());
        completed.remove(fetchOriginalIndexNumber());
        currentContent.remove(thumbNumber + pageNumber);
        popupClose();
        searchStringBuilder();
    }

    private void beginReading(int folderNumber) {
        try {
            readMangaPane.setVisible(true);
            sidebarPane.setVisible(true);
            catalogPane.setVisible(false);
            popupClose();
            readerRequestFocus();
            fetchMangaPages(Values.DIR_ROOT.getValue() + File.separator + Values.DIR_MANGA.getValue() + File.separator + folderNumber + File.separator + lastChapterReadNumber);
            //in case user is going backwards I guess? maybe a more elegant way to do this
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
            e.printStackTrace();
        }
    }

    public void turnMangaBookPage(KeyEvent event) {
        readerRequestFocus();

        if (event.getCode() == KeyCode.D && currentPageNumber < currentContent.size() || event.getCode() == KeyCode.RIGHT && currentPageNumber < currentContent.size()) {
            currentPageNumber++;
            currentContent.get(thumbNumber + pageNumber).setCurrentPage(currentPageNumber);
            //this makes it not work for history, god damn it lol, does it end? we need to basically break setarray, its not gonna work
            setArray().get(selectionIdentNum).setCurrentPage(currentPageNumber);
            displayNextOrPreviousMangaBookPage(currentPageNumber);
            scrollImagePane.setVvalue(0.0);
            calculateChapterNumber();
            insertMangaBookmark();
        } else if (event.getCode() == KeyCode.A && currentPageNumber > 0 || event.getCode() == KeyCode.LEFT && currentPageNumber > 0) {
            currentPageNumber--;
            currentContent.get(thumbNumber + pageNumber).setCurrentPage(currentPageNumber);
            setArray().get(selectionIdentNum).setCurrentPage(currentPageNumber);
            displayNextOrPreviousMangaBookPage(currentPageNumber);
            insertMangaBookmark();
        } else if (event.getCode() == KeyCode.A && currentPageNumber == 0 && lastChapterReadNumber > 0 || event.getCode() == KeyCode.LEFT && currentPageNumber == 0 && lastChapterReadNumber > 0) {
            lastChapterReadNumber--;
            currentPageNumber = -1;
            currentContent.get(thumbNumber + pageNumber).setLastChapterRead(lastChapterReadNumber);
            currentContent.get(thumbNumber + pageNumber).setCurrentPage(currentPageNumber);
            //we need to call this fetch origional index number shit some other time, or at the very least, not repeatedly.
            setArray().get(selectionIdentNum).setLastChapterRead(lastChapterReadNumber);
            setArray().get(selectionIdentNum).setCurrentPage(currentPageNumber);
            beginReading(currentContent.get(thumbNumber + pageNumber).getTitleId());
            // change current folder to lower and current page to highest available
            insertMangaBookmark();
        } else if (event.getCode() == KeyCode.W && scrollImagePane.getVvalue() > 0.0 || event.getCode() == KeyCode.UP && scrollImagePane.getVvalue() > 0.0) {
            scrollUpDown(-1);
        } else if (event.getCode() == KeyCode.S && scrollImagePane.getVvalue() < 1.0 || event.getCode() == KeyCode.DOWN && scrollImagePane.getVvalue() < 1.0) {
            scrollUpDown(1);
        }
    }

    private void calculateChapterNumber() {
        if (currentPageNumber == mangaPages.size() - 1) {
            lastChapterReadNumber++;
            currentPageNumber = 0;
            beginReading(currentContent.get(thumbNumber + pageNumber).getTitleId());
        }

        if (lastChapterReadNumber == totalChaptersNumber) {
            finishedReading();
        }
    }

    private void insertMangaBookmark() {
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        database.modifyManga("currently_reading", selectionIdentNum, "last_chapter_read", lastChapterReadNumber);
//        database.modifyManga("currently_reading", selectionIdentNum, "current_page", currentPageNumber);
//        database.createBookmark("resume_last_manga", selectionIdentNum, totalChaptersNumber, lastChapterReadNumber, currentPageNumber);
//        database.closeDb();
        //NEW
        bookmark.set(0, currentContent.get(thumbNumber + pageNumber));
    }

    private void finishedReading() {
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        database.moveManga(Values.DB_TABLE_READING.getValue(), Values.DB_TABLE_COMPLETED.getValue(), selectionIdentNum);
//        database.deleteManga(Values.DB_TABLE_READING.getValue(), selectionIdentNum);
//        database.createBookmark(Values.DB_TABLE_BOOKMARK.getValue(), -1, -1, -1, -1);
//        database.closeDb();
        imageView.setImage(null);
        readMangaPane.setVisible(false);
        catalogPane.setVisible(true);
        sidebarPane.setVisible(false);
        currentActivity.setValue("My Library");
//        databaseInit();
        //NEW
        completed.add(currentContent.get(thumbNumber + pageNumber));
        reading.remove(fetchOriginalIndexNumber());
        currentContent.remove(thumbNumber + pageNumber);
        bookmark.get(0).setTitleId(-1);
        bookmark.get(0).setTotalChapters(-1);
        bookmark.get(0).setCurrentPage(-1);
        bookmark.get(0).setLastChapterRead(-1);
    }

    public void sidebarVisible() {
        sidebarPane.setOpacity(1);
    }

    public void sidebarInvisible() {
        sidebarPane.setOpacity(0);
        readerRequestFocus();
    }

    public void sidebarFavorite() {
        //new
        //this needs to move to its own method, why do we have two favorite methods?
        if (currentContent.get(thumbNumber + pageNumber).isFavorite()) {
            currentContent.get(thumbNumber + pageNumber).setFavorite(false);
            setArray().get(fetchOriginalIndexNumber()).setFavorite(false);
        } else {
            currentContent.get(thumbNumber + pageNumber).setFavorite(true);
            setArray().get(fetchOriginalIndexNumber()).setFavorite(true);
        }
        //new end
        searchStringBuilder();

//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        database.modifyManga("currently_reading", selectionIdentNum, "favorite", 1);
//        database.closeDb();
        // modify the manga list itself to change favorite value from 0 to 1 and then run populate display. allows us to add a favorite without querying the database
    }

    public void sidebarGoBack() {
        imageView.setImage(null);
        readMangaPane.setVisible(false);
        sidebarPane.setVisible(false);
        catalogPane.setVisible(true);
        if (currentActivity.getValue() != null) {
//            databaseInit();
        }
        //lol why would it ever be null?
    }

    public void sidebarGotoChapter() {
        if (!gotoChapter.getText().equals("")) {
            lastChapterReadNumber = (Integer.parseInt(gotoChapter.getText()) - 1);
            currentPageNumber = 0;
            gotoChapter.clear();
            beginReading(Objects.requireNonNull(setArray()).get(thumbNumber + pageNumber).getTitleId());
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

        //new
        //this icon will only even be NOT greyed out if there is a bookmark to fucking load bitch

        selectionIdentNum = bookmark.get(0).getTitleId();
        totalChaptersNumber = bookmark.get(0).getTotalChapters();
        lastChapterReadNumber = bookmark.get(0).getLastChapterRead();
        currentPageNumber = bookmark.get(0).getCurrentPage();
        beginReading(selectionIdentNum);

//        currentActivity.setValue("My Library");
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        ResultSet resultSet = database.fetchTableData("resume_last_manga");
//
//        try {
//            if (resultSet.next()) {
//                selectionIdentNum = resultSet.getInt("title_id");
//                totalChaptersNumber = resultSet.getInt("total_chapters");
//                lastChapterReadNumber = resultSet.getInt("last_chapter_read");
//                currentPageNumber = resultSet.getInt("current_page");
//                database.closeDb();
//                beginReading();
//            } else {
//                launchPane.setVisible(false);
//                catalogPane.setVisible(true);
//                databaseInit();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

//    private Runnable mangaPageTurned = () -> {
////        database.modifyManga("currently_reading", selectionIdentNum, "current_page", currentPageNumber);
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

    public void genreIncludeExclude(ActionEvent event) {
        predicateList.clear();
        CheckBox checkBox = (CheckBox) event.getSource();
        int indexNumber = (Integer.parseInt(checkBox.getId().substring(5)));

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
    }

    private ArrayList<MangaArrayList> setArray() {

        switch (currentActivity.getValue()) {
            case "Available Manga":
                return available;
            case "My Library":
                return reading;
            case "Not Interested":
                return blacklist;
            case "Finished Reading":
                return completed;
        }
        return null;
    }

    private void filter(ArrayList<MangaArrayList> arrayList, Predicate<MangaArrayList> predicate) {
        currentContent.clear();
        currentContent = arrayList.stream()
                .filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new));

        if (!ascendDescend.isSelected()) {
            Collections.reverse(currentContent);
        }
        populateDisplay();
    }

    private void searchStringBuilder() {
        Map<String, Predicate<MangaArrayList>> genres = GenreMap.getGenreMap();

        //bad?
        for (int i = 0; i < 39; i++) {
            if (genreStrings.get(i).length() > 0) {
                predicateList.add(genres.get(genreStrings.get(i)));
            }
        }

//        for (String predicate : genreStrings) {
//            predicateList.add(genres.get(predicate));
//        }
// the enhanced for loop doesnt work but I honeslty dont get why, seems like it should be fine? maybe fuck around with this more for fun.
        filter(Objects.requireNonNull(setArray()), predicateList.stream().reduce(w -> true, Predicate::and));
    }

    public void searchBox() {
        currentContent.clear();
        popupClose();
        String searchTerm = removeInvalidValues(searchBox.getText());

        filter(Objects.requireNonNull(setArray()), m -> m.getTitle()
                .matches("(?i).*\\b(" + searchTerm + ")\\b.*") || m.getSummary()
                .matches("(?i).*\\b(" + searchTerm + ")\\b.*"));
        searchBox.clear();
    }

    private String removeInvalidValues(String string) {
        return string.replaceAll("[^a-zA-Z0-9!:;.,<>~`!@#$%^&]+", "");
    }

    public void genreReset() {
        ascendDescend.setSelected(false);
        popupClose();
        for (CheckBox genres : checkBoxes) {
            genres.setIndeterminate(true);
            genres.setSelected(false);
        }
        genreStrings.clear();
        currentContent.clear();
        predicateList.clear();
        createGenreStringArray();
        searchStringBuilder();
    }

    public void ascendDescend() {
        searchStringBuilder();
    }

    private void prioritizeFavoritesAndUpdates() {
        currentContent = currentContent.stream()
                .sorted(Comparator
                        .comparing(MangaArrayList::isNewChapters)
                        .thenComparing(MangaArrayList::isFavorite)
                        .reversed())
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    public void ignoreManga() {
//        if (currentActivity.getValue().equals("Not Interested")) {
//            if (lastChapterDownloaded == totalChaptersNumber) {
//                modifyThread.execute(this::moveToReading);
//                //new
//                reading.add(blacklist.get(selectionIdentNum));
//                blacklist.remove(selectionIdentNum);
//            } else {
//                modifyThread.execute(this::moveToDownloading);
//                //new
//                //all this add remove shit can be moved to methods, too duplicatie and all voer.
//                downloading.add(blacklist.get(selectionIdentNum));
//                blacklist.remove(selectionIdentNum);
//            }
//        } else {
//            database.openDb(Values.DB_NAME_MANGA.getValue());
//            database.moveManga(currentDatabase(), "not_interested", selectionIdentNum);
//            database.deleteManga(currentDatabase(), selectionIdentNum);
//            database.closeDb();
//            //new
//            blacklist.add(currentContent.get(thumbNumber + pageNumber));
//        }
        //new
        if (currentActivity.getValue().equals("Not Interested")) {
            downloading.add(blacklist.get(thumbNumber + pageNumber));
        } else {
            blacklist.add(currentContent.get(thumbNumber + pageNumber));
        }
        setArray().remove(fetchOriginalIndexNumber());
        currentContent.remove(thumbNumber + pageNumber);
        //new
        popupClose();
        searchStringBuilder();
//        modifyThread.execute(this::databaseInit);
//        navButtonVisibility();
    }

    private void createGenreStringArray() {
        for (int i = 0; i < 39; i++) {
            genreStrings.add(i, "");
        }
    }

    private String currentDatabase() {
        //so gross, sooo grossssss

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

    public void choiceBox() {
        popupClose();
        searchStringBuilder();
//        pageNumber = 0;
//        currentContent.clear();
//        currentContent.addAll(Objects.requireNonNull(setArray()));
//        ascendDescend.setSelected(false);
//        populateDisplay();
    }

    public void populateDisplay() {
//        Collections.reverse(currentContent);
        if (currentActivity.getValue().equals("My Library") || currentActivity.getValue().equals("Finished Reading")) {
            prioritizeFavoritesAndUpdates();
        }
        resetThumbs();
        int numberOfLoops = 30;

        if (currentContent.size() - pageNumber < 30) {
            numberOfLoops = currentContent.size() - pageNumber;
        }


        try {
            for (int i = 0; i < numberOfLoops; i++) {
                thumbImage = imageViews.get(i);
                favoriteOverlay = favoriteOverlayViews.get(i);
                newChaptersOverlay = newChaptersOverlayViews.get(i);
                thumbImage.setSmooth(true);
                thumbImage.setImage(new Image(new FileInputStream(thumbsPath + File.separator + currentContent.get(i + pageNumber).getTitleId() + ".jpg")));
                if (currentContent.get(i + pageNumber).isFavorite()) {
                    favoriteOverlay.setImage(new Image("assets/favorite_overlay.png"));
                }
                if (currentContent.get(i + pageNumber).isNewChapters()) {
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

    public void gotoSpecificPage() {
        popupClose();
        if (!enterPageNumber.getText().equals("")) {
            pageNumber = (Integer.parseInt(enterPageNumber.getText()) - 1) * 30;
            enterPageNumber.clear();
            populateDisplay();
        }
    }

    private void resetThumbs() {
        falsifyAndNullify(imageViews);
        falsifyAndNullify(favoriteOverlayViews);
        falsifyAndNullify(newChaptersOverlayViews);
    }

    private void falsifyAndNullify(ArrayList<ImageView> imageView) {
        for (ImageView image : imageView) {
            image.setImage(null);
            image.setVisible(false);
        }
    }

    public void turnIndexPage(ActionEvent event) {
        Button button = (Button) event.getSource();
        popupClose();
        int arraySize = currentContent.size();
//        int remainder = 30 - (((mangaId.size() / 30) * 30) - (mangaId.size() % 30));
        int remainder = 30 * (arraySize / 30);

        if (remainder == arraySize) {
            remainder = remainder - 30;
        }

        System.out.println(remainder);
        System.out.println(currentContent.size());

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

    public void getMangaIdFromButtonPressed(ActionEvent buttonPressed) {
        Button button = (Button) buttonPressed.getSource();
        button.getId().substring(11)

    }

    public void getMangaIdFromImageClicked(MouseEvent imageClicked) {
        ImageView image = (ImageView) imageClicked.getSource();
    }

    public void popupOpen(MouseEvent event) throws Exception {

        ImageView image = (ImageView) event.getSource();

        thumbNumber = (Integer.parseInt(image.getId().substring(18)));

        infoBoxToggleButtons(InfoBox.displayCorrectInfoBox(currentActivity.getValue()));

        infoBoxPosition(thumbNumber);
        popupPane.setVisible(true);

//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        ResultSet results = database.filterManga(currentDatabase(), "title_id", mangaId.get(thumbNumber + pageNumber));
//        MangaListView view = currentContent.get(thumbNumber + pageNumber);
        //redundant arraylist, this resultset needs to be utalized in a separate method
//        ResultSet results = database.filterManga(currentDatabase(), "title_id", Integer.toString(currentContent.get(thumbNumber + pageNumber).getTitleId()));

//        if (results.getString("title").length() > 63) {
//            String finalTitle = results.getString("title").substring(0, 63) + " ...";
//            mangaTitle.setText(finalTitle);
//        } else {
//            mangaTitle.setText(results.getString("title"));
//        }

        mangaTitle.setText(abridgeTitle(thumbNumber + pageNumber));
        mangaThumb.setImage(new Image(new FileInputStream(thumbsPath + File.separator + currentContent.get(thumbNumber + pageNumber).getTitleId() + ".jpg")));
        mangaSummary.setText(currentContent.get(thumbNumber + pageNumber).getSummary());
        mangaGenres.setText(currentContent.get(thumbNumber + pageNumber).getGenreTags());
        mangaAuthors.setText(currentContent.get(thumbNumber + pageNumber).getAuthors());
        totalChaptersNumber = currentContent.get(thumbNumber + pageNumber).getTotalChapters();
        currentPageNumber = currentContent.get(thumbNumber + pageNumber).getCurrentPage();
        lastChapterReadNumber = currentContent.get(thumbNumber + pageNumber).getLastChapterRead();
        lastChapterDownloaded = currentContent.get(thumbNumber + pageNumber).getLastChapterDownloaded();


//        mangaSummary.setText(results.getString("summary"));
//        mangaGenres.setText(results.getString("genre_tags").substring(0, results.getString("genre_tags").length() - 1));
//        mangaAuthors.setText(results.getString("authors").substring(0, results.getString("authors").length() - 1));
//        totalChaptersNumber = results.getInt("total_chapters");
//        currentPageNumber = results.getInt("current_page");
//        lastChapterReadNumber = results.getInt("last_chapter_read");
//        lastChapterDownloaded = results.getInt("last_chapter_downloaded");

//        database.closeDb();
//        results.close();
        //super questionable, looks like its only worth a shit for db whatever
        selectionIdentNum = currentContent.get(thumbNumber + pageNumber).getTitleId(); //honestly only worth a fuck for db shit now
        fetchOriginalIndexNumber();

    }

    private int fetchOriginalIndexNumber() {
        return IntStream.range(0, Objects.requireNonNull(setArray()).size())
                .filter(i -> Objects.requireNonNull(setArray()).get(i).getTitleId() == currentContent.get(thumbNumber + pageNumber).getTitleId())
                .findFirst()
                .orElse(-1);
    }

    private void infoBoxToggleButtons(List<Boolean> isShown) {
        ignoreMangaShort.setVisible(isShown.get(0));
        favorite.setVisible(isShown.get(1));
        readManga.setVisible(isShown.get(2));
        ignoreManga.setVisible(isShown.get(3));
        getInnerPopup.setVisible(isShown.get(4));
        unignore.setVisible(isShown.get(5));
        rereadManga.setVisible(isShown.get(6));
        getIssuePopup.setDisable(isShown.get(7));
    }

    private String abridgeTitle(int mangaSelected) {
        String title = currentContent.get(mangaSelected).getTitle();
        if (title.length() > 63) {
            return title.substring(0, 63) + " ...";
        }
        return title;
    }

    private void infoBoxPosition(int imageClicked) {
        if (imageClicked <= 14) {
            popupPane.setLayoutX(1144);
//            innerPopup.setLayoutX(1329);
            repairPopup.setLayoutX(1329);
        } else {
            popupPane.setLayoutX(15);
//            innerPopup.setLayoutX(200);
            repairPopup.setLayoutX(200);
        }
    }

    public void popupClose() {
        popupPane.setVisible(false);
        repairPopup.setVisible(false);
    }

    public void showRepairPopup() {
        repairPopup.setVisible(true);
    }

    private void writePreDownloadValues() {
        //legacy
//        ArrayList chapterCount = null;
//        try {
//            chapterCount = indexMangaChapters.getChapterCount(webAddress);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.modifyManga(Values.DB_TABLE_AVAILABLE.getValue(), selectionIdentNum, "last_chapter_read", startingChapter);
//        database.modifyManga(Values.DB_TABLE_AVAILABLE.getValue(), selectionIdentNum, "total_chapters", chapterCount.size());
        database.closeDb();
//        return chapterCount.size();
    }

    private void moveToReading() {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.moveManga(currentDatabase(), Values.DB_TABLE_READING.getValue(), selectionIdentNum);
        database.deleteManga(currentDatabase(), selectionIdentNum);
        database.closeDb();
    }

    private void moveToDownloading() {
        database.openDb(Values.DB_NAME_DOWNLOADING.getValue());
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.downloadDbAttach();
        database.moveManga(Values.DB_ATTACHED_PREFIX.getValue() + currentDatabase(), Values.DB_ATTACHED_DOWNLOADING.getValue(), selectionIdentNum);
        database.deleteManga(Values.DB_ATTACHED_PREFIX.getValue() + currentDatabase(), selectionIdentNum);
        database.downloadDbDetach();
        database.closeDb();
        database.closeDb();
//        database.downloadQueueAdd("downloading", selectionIdentNum, webAddress, startingChapter, 0);
//        database.closeDb();
    }

    private Runnable downloadManga = () -> {
        //this is gonna be fun on a bun son
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

        //new
//        check this whole first download shit because yeah, pretty sure its meaningless now, lol thats quite the reduction there billy
//        while (!downloading.isEmpty()) {
//            downloadMangaPages.getChapterPages(downloading.get(0).getLastChapterDownloaded(), );
//        }
        try {
            for (MangaArrayList download : downloading) {
                downloadMangaPages.getChapterPages(download.getLastChapterDownloaded(), download.getWebAddress(), download.getTitleId(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    private void navButtonVisibility() {
        if (currentContent.size() > 30) {
            pageNumTextPane.setVisible(true);
        } else {
            pageNumTextPane.setVisible(false);
        }

        if (pageNumber == 0 && currentContent.size() > 30) {
            pageFirst.setVisible(false);
            pagePrevious.setVisible(false);
            pageNext.setVisible(true);
            pageLast.setVisible(true);
        } else if (pageNumber >= 30 && pageNumber < currentContent.size() - 30) {
            pageFirst.setVisible(true);
            pagePrevious.setVisible(true);
            pageNext.setVisible(true);
            pageLast.setVisible(true);
        } else if (pageNumber != 0 && pageNumber == (currentContent.size() - currentContent.size() % 30) - 30 && currentContent.size() % 30 == 0 || pageNumber != 0 && pageNumber == (currentContent.size() - currentContent.size() % 30)) {
            // if pagenumber does NOT equal 0 but DOES equal the highest available page. much easier than this shitshow.
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
        int totalPagesNumber = currentContent.size() / 30;

        if (currentContent.size() % 30 > 0) {
            totalPagesNumber++;
        }

        if (totalPagesNumber == 0) {
            currentPageNumber = 0;
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
}


//    public void getMangaButton() {
//        startReading.setVisible(true);
//        if (haveRead.isSelected()) {
//            innerPopupNumbox.setVisible(true);
//            lastChapterRead.requestFocus();
//        } else if (haveNotRead.isSelected()) {
//            innerPopupNumbox.setVisible(false);
//            lastChapterRead.clear();
//        }
//    }

//    public void showInnerPopup() {
//        innerPopup.setVisible(true);
//        System.out.println("reading");
//    }

//    public void showAvailableMangaBooksPane() {
//        //legacy?
//        popupClose();
////        launchPane.setVisible(false);
//        currentActivity.setValue("Available Manga");
//        catalogPane.setVisible(true);
//    }
//
//    public void showOwnedMangaBooksPane() {
//        //legacy?
//        popupClose();
////        launchPane.setVisible(false);
//        currentActivity.setValue("My Library");
//        catalogPane.setVisible(true);
////        databaseInit();
//    }

//    private void resetThumbs() {
//        //replace with imageviews.clear? derp? same below?
//        for (int i = 0; i < 30; i++) {
//            thumbImage = imageViews.get(i);
//            favoriteOverlay = favoriteOverlayViews.get(i);
//            newChaptersOverlay = newChaptersOverlayViews.get(i);
//            thumbImage.setImage(null);
//            favoriteOverlay.setImage(null);
//            newChaptersOverlay.setImage(null);
//        }
//    }

//    private void hideThumbs() {
//        for (int i = 0; i < 30; i++) {
//            thumbImage = imageViews.get(i);
//            favoriteOverlay = favoriteOverlayViews.get(i);
//            newChaptersOverlay = newChaptersOverlayViews.get(i);
//            thumbImage.setVisible(false);
//            favoriteOverlay.setVisible(false);
//            newChaptersOverlay.setVisible(false);
//        }
//    }


//    private int getFirstChapter(int mangaDir) {
//        //legacy
//        int dirNumber = 0;
//
//        while (dirNumber < 9999) {
//            if (Files.notExists(Paths.get(Values.DIR_ROOT.getValue() + File.separator + Values.DIR_MANGA.getValue() + File.separator + mangaDir + File.separator + dirNumber))) {
//                dirNumber++;
//            }
//        }
//        return dirNumber;
//    }


//    private void databaseInit() {
//        String sortOrder;
//
//        if (ascendDescend.isSelected()) {
//            sortOrder = "ASC";
//        } else {
//            sortOrder = "DESC";
//        }
//        if (joiner.length() > 0) {
//            if (currentActivity.getValue().equals("My Library")) {
//                dbSearch("title_id, new_chapters, favorite", currentDatabase(), " WHERE " + joiner.toString(), sortOrder);
//            } else {
//                dbSearch("title_id", currentDatabase(), " WHERE " + joiner.toString(), sortOrder);
//            }
//        } else if (joiner.length() == 0) {
//            if (currentActivity.getValue().equals("My Library")) {
//                dbSearch("title_id, new_chapters, favorite", currentDatabase(), "", sortOrder);
//            } else {
//                dbSearch("title_id", currentDatabase(), "", sortOrder);
//            }
//        }
//    }
//
//    private void dbSearch(String columnName, String tableName, String requestedData, String sortOrder) {
//        String sortByValue;
//
//        if (currentActivity.getValue().equals("My Library")) {
//            sortByValue = " ORDER BY new_chapters DESC, title_id ";
//        } else {
//            sortByValue = " ORDER BY title_id ";
//        }
//
//        mangaId.clear();
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        ResultSet resultSet = database.workingGenreFilter(columnName, tableName, requestedData, sortByValue, sortOrder);
//
//
//        try {
//            if (resultSet.next()) {
//                do {
//                    if (currentActivity.getValue().equals("My Library")) {
//                        mangaId.add(new MangaListView(resultSet.getInt("title_id"), resultSet.getBoolean("new_chapters"), resultSet.getBoolean("favorite")));
//                    } else {
////                            mangaId.add(resultSet.getString("title_id"));
//                        mangaId.add(new MangaListView(resultSet.getInt("title_id"), false, false));
//                    }
//                } while (resultSet.next());
//            }
//        } catch (Exception e) {
//            System.out.println(e + "  supposedly not possible fml");
//        }
//
//        database.closeDb();
//        populateDisplay();
//    }

//    public void lastPage() throws Exception {
//        resetThumbs();
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
//        database.moveManga(tableName, Values.DB_TABLE_DOWNLOAD_PENDIND.getValue(), selectionIdentNum);
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
//        database.moveManga(currentDatabase(), Values.DB_TABLE_COMPLETED.getValue(), selectionIdentNum);
//        database.deleteManga(currentDatabase(), selectionIdentNum);
//        database.closeDb();
//        joiner = new StringJoiner(" AND ");
//    }

//    private void chapterNumber() {
//
//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        ResultSet resultSet = database.filterManga("currently_reading", "title_id", Integer.toString(selectionIdentNum));
//
//        try {
//            totalChaptersNumber = Integer.parseInt(resultSet.getString("total_chapters"));
//            currentPageNumber = Integer.parseInt(resultSet.getString("current_page"));
//            lastChapterReadNumber = Integer.parseInt(resultSet.getString("last_chapter_read"));
//            database.createBookmark("resume_last_manga", selectionIdentNum, totalChaptersNumber, lastChapterReadNumber, currentPageNumber);
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