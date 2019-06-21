package com.matthew_savage;

import com.matthew_savage.GUI.*;
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

import static com.matthew_savage.CategoryMangaLists.*;

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
    private Pane repairPane;
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
    private ScrollPane mangaPageVerticalScrollPane;
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
    private static TextField sidebarTotalChapNum;
    @FXML
    private TextField searchBox;
    @FXML
    private TextField currentPageNumberDisplay;
    @FXML
    private TextField lastPageNumberDisplay;
    @FXML
    private TextField enterPageNumber;
    @FXML
    private static TextField sidebarCurChapNum;
    @FXML
    private static TextField sidebarPageNumber;
    @FXML
    private TextField gotoChapter;
    @FXML
    private static TextField sidebarCurScrollSpeed;
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
    private ChoiceBox<String> currentCategory;
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

    private static int thumbsPageCurrentPageNum;
    private static int thumbsPageLastPageNum;
    private static int clickedThumbIdent;
    private static int clickedButtonIdent;
    private static int currentListIndex;
//    private static int selectedMangaIdentNumber;
    private static String selectedMangaTitle;
    private static String selectedMangaAuthors;
    private static String selectedMangaStatus;
    private static String selectedMangaSummary;
    private static String selectedMangaWebAddress;
    private static String selectedMangaGenreTags;
    private static int currentParentListIndex;
    private static int selectedMangaTotalChapters;
    private static int selectedMangaPageCurrentlyReading;
    private static int selectedMangaLastChapterRead;
    private static int selectedMangaLastChapterDownloaded;
    private static int selectedMangaCurrentChapterFinalPage;
    private static boolean hasNewlyAddedChapters;
    private static boolean hasBeenFavorited;
    private static Robot softwareMouse;
    private static int currentMouseWheelSpeed = 5;
    private static int currentContentListSize;

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
    private int indexIncrementValue = 0;
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
    private static int scrollSpeed = 5;

    public void initialize() {
        GenreMap.createGenreString();
        CategoryMangaLists.currentParentList = CategoryMangaLists.collectedMangaList;
        currentCategory.setItems(FXCollections.observableArrayList("Not Collected", "Collected", "Completed", "Rejected"));

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

        try {
            softwareMouse = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        executor.execute(this::checkForHistory);
        executor.execute(this::populateStats);
        executor.execute(this::createOrResetGenreStringArray);
        executor.execute(this::displayInternetStatus);

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
//                imageView.setFitWidth(newValue.doubleValue());
                adjustImageViewWidth(newValue.doubleValue());
                mangaPageVerticalScrollPane.setMinWidth(newValue.doubleValue());
            }
        });

        main.heightProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mangaPageVerticalScrollPane.setMinHeight(newValue.doubleValue());
            }
        });

        populateCurrentContentList(collectedMangaList);
        setThumbsPaneStaticNavElements();
        populateCurrentCategoryPaneThumbs();
    }

    // START OF APP FLOW

    private void populateCurrentContentList(ArrayList<MangaArrayList> currentActivityMangaList) {
        currentContent.clear();
        currentContent.addAll(currentActivityMangaList);
        currentContentListSize = currentContent.size();
        indexIncrementValue = 0;

        if (!currentActivityMangaList.equals(history)) {
            Collections.reverse(currentContent);
        }
    }

    private void setThumbsPaneStaticNavElements() {
        thumbsPageDetermineLastPageNum();
        thumbsPageSetLastPageNum();
    }

    private void setThumbsPaneDynamicNavElements() {
        thumbsPageDetermineCurrentPageNum();
        thumbsPageSetCurrentPageNum();
        thumbsPageToggleNavButtonVisibility(ToggleNavButtons.navButtonVisibility(thumbsPageCurrentPageNum, thumbsPageLastPageNum));
    }

    private void thumbsPageDetermineCurrentPageNum() {
        thumbsPageCurrentPageNum = ThumbPageNumbering.setCurrentCatalogPageNumber(indexIncrementValue);
    }

    private void thumbsPageDetermineLastPageNum() {
        thumbsPageLastPageNum = ThumbPageNumbering.setTotalCatalogPagesNumber(currentContentListSize);
    }

    private void thumbsPageSetCurrentPageNum() {
        currentPageNumberDisplay.setText(String.valueOf(thumbsPageCurrentPageNum));
        currentPageNumberDisplay.setLayoutX(ThumbPageNumbering.setCurrentCatalogPageNumberLayoutX(thumbsPageCurrentPageNum));
    }

    private void thumbsPageToggleNavButtonVisibility(List<Boolean> isShown) {
        pageFirst.setVisible(isShown.get(0));
        pagePrevious.setVisible(isShown.get(1));
        pageNumTextPane.setVisible(isShown.get(2));
        pageNext.setVisible(isShown.get(3));
        pageLast.setVisible(isShown.get(4));
    }

    private void thumbsPageSetLastPageNum() {
        lastPageNumberDisplay.setText(String.valueOf(thumbsPageLastPageNum));
    }

    private void populateCurrentCategoryPaneThumbs() {
        clearAndHideThumbImageViews();
        checkIfCategoryCollectedOrCompleted();
        setThumbsPaneDynamicNavElements();

        for (int i = 0; i < getTerminationValue(); i++) {
            imageViews.get(i).setSmooth(true);
            imageViews.get(i).setImage(Objects.requireNonNull(getImageFromFilesystem(i + indexIncrementValue)));
            addFavoriteBannerIfFavorite(i);
            addNewChaptersBannerIfNewChapters(i);
            imageViews.get(i).setVisible(true);
        }
        // this and the page number shit should really be happening sooner, like the second the currentcontentlist is created
//        navButtonVisibility();
    }

    private void clearAndHideThumbImageViews() {
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

    private void checkIfCategoryCollectedOrCompleted() {
        if (currentCategory.getValue().equals("Collected") || currentCategory.getValue().equals("Completed")) {
            prioritizeFavoritesAndUpdates();
        }
    }

    private void prioritizeFavoritesAndUpdates() {
        // test this with the fucking method for setting current content and see if it divides by zero
        // and kills us all
        currentContent = currentContent.stream()
                .sorted(Comparator
                        .comparing(MangaArrayList::isNewChapters)
                        .thenComparing(MangaArrayList::isFavorite)
                        .reversed())
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    private int getTerminationValue() {
        int numberOfThumbs = currentContentListSize - indexIncrementValue;

        if (numberOfThumbs < 30) {
            return numberOfThumbs;
        } else {
            return 30;
        }
    }

    private Image getImageFromFilesystem(int listIndexNumber) {
        //dont need to pass this value in with new catmangalists class
        try {
            return new Image(new FileInputStream(thumbsPath + File.separator + currentContent.get(listIndexNumber).getTitleId() + ".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Image getImageFromFilesystem(File imageFile) {
        //dont need to pass this value in with new catmangalists class
        try {
            return new Image(new FileInputStream(imageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addFavoriteBannerIfFavorite(int listIndexNumber) {
        //dont need to pass this value in with new catmangalists class
        if (currentContent.get(listIndexNumber + indexIncrementValue).isFavorite()) {
            favoriteOverlayViews.get(listIndexNumber).setImage(new Image("assets/favorite_overlay.png"));
            favoriteOverlayViews.get(listIndexNumber).setVisible(true);
        }
    }

    private void addNewChaptersBannerIfNewChapters(int listIndexNumber) {
        //dont need to pass this value in with new catmangalists class
        if (currentContent.get(listIndexNumber + indexIncrementValue).isNewChapters()) {
            newChaptersOverlayViews.get(listIndexNumber).setImage(new Image("assets/new_chapter_multiple.png"));
            newChaptersOverlayViews.get(listIndexNumber).setVisible(true);
        }
    }

    // END OF APP FLOW

    // DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING
    // DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING

    public void ascendDescend() {
        Collections.reverse(currentContent);
        setThumbsPaneDynamicNavElements();
        populateCurrentCategoryPaneThumbs();
    }

    public void openHistoryPane() {
        populateCurrentContentList(history);
        setHistoryPaneVisibility(true);
    }

    public void closeHistoryPane() {
        populateCurrentContentList(matchListToCurrentCategory());
        setHistoryPaneVisibility(false);
    }

    private void setHistoryPaneVisibility(boolean isVisible) {
        historyClosePane.setVisible(isVisible);
        historyPaneContent.setVisible(isVisible);
    }

    private ArrayList<MangaArrayList> matchListToCurrentCategory() {

        switch (currentCategory.getValue()) {
            case "Not Collected":
                return notCollectedMangaList;
            case "Collected":
                return collectedMangaList;
            case "Rejected":
                return rejectedMangaList;
            case "Completed":
                return completedMangaList;
        }
        return null;
    }

    public static void scrollMangaPageUp() {
        softwareMouse.mouseWheel(-currentMouseWheelSpeed);
    }

    public static void scrollMangaPageDown() {
        softwareMouse.mouseWheel(currentMouseWheelSpeed);
    }

    public void scrollWheel(ScrollEvent scrollMouseWheel) {
        MangaTurnPage.simulateMouseWheelScroll(scrollMouseWheel);
    }

    public void adjustMangaPageUpDownSpeed(ActionEvent event) {
        scrollSpeed = MangaTurnPage.adjustScrollSpeed(event, scrollSpeed);
        displayCurrentScrollSpeed();
    }

    private void displayCurrentScrollSpeed() {
        sidebarCurScrollSpeed.setText(Integer.toString(scrollSpeed));
    }

    private void readerRequestFocus() {
        mangaPageVerticalScrollPane.requestFocus();
    }

    private void adjustImageViewWidth(double newWidth) {
        imageView.setFitWidth(newWidth);
    }

    // END END END DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING
    // END END END DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING DONE REFACTORING

    //========================================================

    //fix check for last manga added, need to have like 3-5 mangas and if the first one isnt found after x pages
    //then go to next one. and make the pool get bigger. like first sweep is 10 pages and next sweep is like 50 pages
    //also need to delete the now missing manga. what a fucking pain

    //need to put current downloads in ui

    //need to make all the found new chapters shit ui shit

    //need to sort new chapters by number of new chapters, so make a list of every manga with new chapters
    //and sort from highest to lowest or whatever, then output. ez. remove from reading and add to this new list
    //no db writes necessary for this i feel like

    //add genre tag mouseovers to ui

    //lol all the stats logic. this app is getting respectable i feel like


    //=========================================================

    public void toggleZoomControls() {
        if (imageView.getFitWidth() == 1720) {
            sidebarPaneSizeIncrease.setVisible(true);
        } else if (imageView.getFitWidth() == 920) {
            sidebarPaneSizeDecrease.setVisible(false);
        }
    }

    public void mangaBookPageZoomInOrOut(ActionEvent event) {
        // seems like a lot of shit is going on here eh?
        adjustImageViewWidth(MangaZoomInOut.widthValue((Button) event.getSource(), imageView.getFitWidth()));
        toggleZoomControls();
    }

    public void getMangaIdFromButtonPressed(ActionEvent buttonPressed) {
        Button button = (Button) buttonPressed.getSource();
        clickedButtonIdent = Integer.parseInt(button.getId().substring(11));
//        currentListIndex = indexIncrementValue + clickedButtonIdent;
        currentContentListIndexNumber = indexIncrementValue + clickedButtonIdent;
    }

    public void getMangaIdFromImageClicked(MouseEvent imageClicked) {
        ImageView image = (ImageView) imageClicked.getSource();
        clickedThumbIdent = Integer.parseInt(image.getId().substring(18));
//        currentListIndex = indexIncrementValue + clickedThumbIdent;
        currentContentListIndexNumber = indexIncrementValue + clickedThumbIdent;
        openMangaInfoPane();
    }

    public void openMangaInfoPane() {
        infoBoxToggleButtons(InfoBox.displayCorrectInfoBox(currentCategory.getValue()));
        positionInfoAndRepairBox(clickedThumbIdent);
        populateMangaInfoPane();
        popupPane.setVisible(true);
    }

    private void setCurrentMangaVariables() {
        // i dont know about this, either needs to set values in catmangalist class or be pruned extensively
        selectedMangaIdentNumber = currentContent.get(currentListIndex).getTitleId();
        selectedMangaTitle = currentContent.get(currentListIndex).getTitle();
        selectedMangaAuthors = currentContent.get(currentListIndex).getAuthors();
        selectedMangaStatus = currentContent.get(currentListIndex).getStatus();
        selectedMangaSummary = currentContent.get(currentListIndex).getSummary();
        selectedMangaWebAddress = currentContent.get(currentListIndex).getWebAddress();
        selectedMangaGenreTags = currentContent.get(currentListIndex).getGenreTags();
        selectedMangaTotalChapters = currentContent.get(currentListIndex).getTitleId();
        selectedMangaPageCurrentlyReading = currentContent.get(currentListIndex).getTitleId();
        selectedMangaLastChapterRead = currentContent.get(currentListIndex).getTitleId();
        selectedMangaLastChapterDownloaded = currentContent.get(currentListIndex).getTitleId();
        hasNewlyAddedChapters = currentContent.get(currentListIndex).isNewChapters();
        hasBeenFavorited = currentContent.get(currentListIndex).isFavorite();
    }

    private void populateMangaInfoPane() {
        // i dont know about this either.
        mangaTitle.setText(abridgeTitle(selectedMangaTitle));
        mangaThumb.setImage(getImageFromFilesystem(selectedMangaIdentNumber));
        mangaSummary.setText(selectedMangaSummary);
        mangaAuthors.setText(selectedMangaAuthors);
    }

    public void readSelectedManga() {

    }

    public void downloadSelectedManga() {

    }


    //========================================================


    //replace all this shit below too most likely? i can turn these two methods into a single method, send scroll
    //speed to remote class, and return new acrollspeed. nice. run displayscrollspeed

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

    private void setNewChaptersFlagFalse() {
        currentContent.get(currentListIndex).setNewChapters(false);
        collectedMangaList.get(fetchOriginalIndexNumber()).setNewChapters(false);
    }

    private void fetchMangaPages(String pathToManga) {
        // this needs to be come public static, also do we really need to pass a path? I feel like no.
        mangaPages.clear();
        File file = new File(pathToManga);
        mangaPages.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
        selectedMangaCurrentChapterFinalPage = mangaPages.size();
    }

    public static void mangaImageFilesToList() {
        // doesnt need to be in controller at all
        mangaPageImageFiles.clear();
        File file = new File(Values.DIR_ROOT.getValue() +
                File.separator +
                Values.DIR_MANGA.getValue() +
                File.separator +
                selectedMangaIdentNumber +
                File.separator +
                selectedMangaLastChapReadNum);
        mangaPageImageFiles.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
        selectedMangaCurrentChapterFinalPage = mangaPageImageFiles.size();
    }



    private void populateHistory() throws Exception {
        historyInverted.clear();
        historyInverted.addAll(history);
//        Collections.reverse(historyInverted);

        // surly we can just iterate from top to bottom, this seems kind of ghetto.

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

        // this whole button button shit is a seperate meothod now, so lets use it. or maybe this whole
        // method is obsolite, who even knows at this point fml


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

        if (currentContent.get(currentListIndex).getLastChapterRead() - 3 < 0) {
            currentContent.get(currentListIndex).setLastChapterRead(0);
        } else {
            currentContent.get(currentListIndex).setLastChapterRead(currentContent.get(currentListIndex).getLastChapterRead() - 3);
        }

        currentContent.get(currentListIndex).setLastChapterDownloaded(0);
        //whats this fucking -1 page shit, pretty sure we dont need this anymore
        // oh wait, this tells the reader to start at the last page of the chapter right? is that helpful? i dont think
        // so billy. its gross, its just all gross.
        currentContent.get(currentListIndex).setCurrentPage(-1);
        getThisManga();
    }

    public void getThisManga() {
        downloading.add(currentContent.get(currentListIndex));
        Objects.requireNonNull(matchListToCurrentCategory()).remove(fetchOriginalIndexNumber());
        currentContent.remove(currentListIndex);
        popupClose();
        searchStringBuilder();
    }

    public void changeCompletedToReading() {
        currentContent.get(currentListIndex).setLastChapterRead(0);
        currentContent.get(currentListIndex).setCurrentPage(0);
        collectedMangaList.add(currentContent.get(currentListIndex));
        beginReading(currentContent.get(currentListIndex).getTitleId());
        completedMangaList.remove(fetchOriginalIndexNumber());
        currentContent.remove(currentListIndex);
        popupClose();
        searchStringBuilder();
    }

    public void preReadingTasks() {
        //calling titleid more than once, this should be set somewhere.. no?
        //this whole method is shit. shouldnt be setting a bookmark, shoudlnt be setting values, just no.
        bookmark.set(0, currentContent.get(currentListIndex));
        if (!HistoryPane.storeHistory(history, currentContent.get(currentListIndex).getTitleId())) {
            history.add(currentContent.get(currentListIndex));
        }
        currentParentListIndex = fetchOriginalIndexNumber();
        selectedMangaIdentNumber = currentContent.get(currentListIndex).getTitleId();
        mangaImageFilesToList();
        launchReader();
    }

    private void launchReader() {
        popupClose();
        readerRequestFocus();
        adjustImageViewWidth(920);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        setCurrentPageImageInReader();
        setAllSidebarInfoFields();
        openReadingPaneAndChildren();
        removeNewChapFlagIfPresent();
    }

    private static void setAllSidebarInfoFields() {
        setReaderSidebarCurChapNum();
        setReaderSidebarFinalChapNum();
        setReaderSidebarCurScrollSpeed();
        setReaderSidebarCurPageNum();
    }

    private void setCurrentPageImageInReader() {
        imageView.setImage(getImageFromFilesystem(mangaPageImageFiles.get(selectedMangaCurrentPageNum)));
        setReaderSidebarCurPageNum();
    }

    private static void setReaderSidebarCurChapNum() {
        sidebarCurChapNum.setText(String.valueOf(selectedMangaLastChapterRead + 1));
    }

    private static void setReaderSidebarFinalChapNum() {
        sidebarTotalChapNum.setText(String.valueOf(selectedMangaTotalChapNum));
    }

    private static void setReaderSidebarCurScrollSpeed() {
        sidebarCurScrollSpeed.setText(String.valueOf(scrollSpeed));
    }

    private static void setReaderSidebarCurPageNum() {
        sidebarPageNumber.setText(String.valueOf(selectedMangaCurrentPageNum + 1));
    }

    private static void removeNewChapFlagIfPresent() {
        if (currentContent.get(currentContentListIndexNumber).isNewChapters()) {
            MangaValues.changeNewChaptersFlag(false, Values.DB_NAME_MANGA.getValue());
        }
    }

    private void beginReading(int folderNumber) {
        try {
            popupClose();
            readerRequestFocus();
            fetchMangaPages(Values.DIR_ROOT.getValue() + File.separator + Values.DIR_MANGA.getValue() + File.separator + folderNumber + File.separator + lastChapterReadNumber);
            //in case user is going backwards I guess? maybe a more elegant way to do this
            //def needs to be in the page turning whatever bullshit logic
            if (currentPageNumber == -1) {
                currentPageNumber = mangaPages.size() - 1;
            }
            adjustImageViewWidth(920);
            imageView.setImage(new Image(new FileInputStream(mangaPages.get(currentPageNumber))));
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            sidebarCurChapNum.setText(Integer.toString(lastChapterReadNumber + 1));
            sidebarPageNumber.setText(Integer.toString(currentPageNumber + 1));
            sidebarTotalChapNum.setText(Integer.toString(totalChaptersNumber));
            sidebarCurScrollSpeed.setText(Integer.toString(scrollSpeed));
            setNewChaptersFlagFalse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openReadingPaneAndChildren() {
        setReadingPaneVisibility(true);
    }

    private void closeReadingPaneAndChildren() {
        setReadingPaneVisibility(false);
    }

    private void setReadingPaneVisibility(boolean isVisible) {
        readMangaPane.setVisible(isVisible);
        sidebarPane.setVisible(isVisible);
        catalogPane.setVisible(!isVisible);
    }

    public void navigateMangaCurrentlyReading(KeyEvent event) {
        readerRequestFocus();
        MangaTurnPage.turnPagePreviousNext(event, selectedMangaCurrentChapterFinalPage);
        MangaTurnPage.scrollPageUpDown(event, mangaPageVerticalScrollPane);

        //calculatechapternumber should actually happen first no? mangaturnpage should run that first as a private method
        //bookmark, call from mangaturnpage? makes sense
        //display new page, call from mangaturnpage
        //scrollimagepane, move back to top, call from mangaturnpage
    }

    public void turnMangaBookPage(KeyEvent event) {
        //before this is run, no need to run every fucking pgeturn
        //set value of indexposition in currentcontent - done, currentListIndex
        //set value of indexposition in source list?

        readerRequestFocus();

        if (event.getCode() == KeyCode.D && currentPageNumber < currentContentListSize || event.getCode() == KeyCode.RIGHT && currentPageNumber < currentContentListSize) {
            currentPageNumber++;
            currentContent.get(currentListIndex).setCurrentPage(currentPageNumber);
            //this makes it not work for history, god damn it lol, does it end? we need to basically break setarray, its not gonna work
            matchListToCurrentCategory().get(selectionIdentNum).setCurrentPage(currentPageNumber);
            displayNextOrPreviousMangaBookPage(currentPageNumber);
            //maybe break this into a turn mangabookpage forward and backward? I dont know.
            mangaPageVerticalScrollPane.setVvalue(0.0);
            calculateChapterNumber();
            insertMangaBookmark();
        } else if (event.getCode() == KeyCode.A && currentPageNumber > 0 || event.getCode() == KeyCode.LEFT && currentPageNumber > 0) {
            currentPageNumber--;
            currentContent.get(currentListIndex).setCurrentPage(currentPageNumber);
            matchListToCurrentCategory().get(selectionIdentNum).setCurrentPage(currentPageNumber);
            displayNextOrPreviousMangaBookPage(currentPageNumber);
            insertMangaBookmark();
        } else if (event.getCode() == KeyCode.A && currentPageNumber == 0 && lastChapterReadNumber > 0 || event.getCode() == KeyCode.LEFT && currentPageNumber == 0 && lastChapterReadNumber > 0) {
            lastChapterReadNumber--;
            currentPageNumber = -1;
            currentContent.get(currentListIndex).setLastChapterRead(lastChapterReadNumber);
            currentContent.get(currentListIndex).setCurrentPage(currentPageNumber);
            //we need to call this fetch origional index number shit some other time, or at the very least, not repeatedly.
            matchListToCurrentCategory().get(selectionIdentNum).setLastChapterRead(lastChapterReadNumber);
            matchListToCurrentCategory().get(selectionIdentNum).setCurrentPage(currentPageNumber);
            beginReading(currentContent.get(currentListIndex).getTitleId());
            // change current folder to lower and current page to highest notCollectedMangaList
            insertMangaBookmark();
        } else if (event.getCode() == KeyCode.W && mangaPageVerticalScrollPane.getVvalue() > 0.0 || event.getCode() == KeyCode.UP && mangaPageVerticalScrollPane.getVvalue() > 0.0) {
//            scrollUpDown(-1);
        } else if (event.getCode() == KeyCode.S && mangaPageVerticalScrollPane.getVvalue() < 1.0 || event.getCode() == KeyCode.DOWN && mangaPageVerticalScrollPane.getVvalue() < 1.0) {
//            scrollUpDown(1);
        }
    }

//    public static void resetReadingViewToTopOfPage() {
//        mangaPageVerticalScrollPane.setVvalue(0.0);
//    }

    public static void updateCurrentPageNumber(int currentPageNumber) {
        selectedMangaPageCurrentlyReading = currentPageNumber;
        currentContent.get(currentListIndex).setCurrentPage(currentPageNumber);
        currentParentList.get(currentParentListIndex).setCurrentPage(currentPageNumber);
    }



    private void calculateChapterNumber() {
        if (currentPageNumber == mangaPages.size() - 1) {
            lastChapterReadNumber++;
            currentPageNumber = 0;
            beginReading(currentContent.get(currentListIndex).getTitleId());
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
        bookmark.set(0, currentContent.get(currentListIndex));
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
        currentCategory.setValue("Collected");
//        databaseInit();
        //NEW
        completedMangaList.add(currentContent.get(currentListIndex));
        collectedMangaList.remove(fetchOriginalIndexNumber());
        currentContent.remove(currentListIndex);
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
        if (currentContent.get(currentListIndex).isFavorite()) {
            currentContent.get(currentListIndex).setFavorite(false);
            matchListToCurrentCategory().get(fetchOriginalIndexNumber()).setFavorite(false);
        } else {
            currentContent.get(currentListIndex).setFavorite(true);
            matchListToCurrentCategory().get(fetchOriginalIndexNumber()).setFavorite(true);
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
        if (currentCategory.getValue() != null) {
//            databaseInit();
        }
        //lol why would it ever be null?
    }

    public void sidebarGotoChapter() {
        if (!gotoChapter.getText().equals("")) {
            lastChapterReadNumber = (Integer.parseInt(gotoChapter.getText()) - 1);
            currentPageNumber = 0;
            gotoChapter.clear();
            beginReading(Objects.requireNonNull(matchListToCurrentCategory()).get(currentListIndex).getTitleId());
            insertMangaBookmark();
        }
    }

    private void forceNumbersOnly(Number oldValue, Number newValue, TextField textField) {
        //move to its own class.
        if (newValue.intValue() > oldValue.intValue()) {
            char character = textField.getText().charAt(oldValue.intValue());
            if (!(character >= '0' && character <= '9')) {
                textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
            }
        }
        if (gotoChapter.getText().length() > 0 || enterPageNumber.getText().length() > 0) {
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
            if (Integer.parseInt(enterPageNumber.getText()) < 1 && enterPageNumber.getText().length() > 0) {
                enterPageNumber.setText("1");
            }
            if (Integer.parseInt(enterPageNumber.getText()) > thumbsPageLastPageNum && enterPageNumber.getText().length() > 0) {
                enterPageNumber.setText(String.valueOf(thumbsPageLastPageNum));
            }
        } catch (NumberFormatException e) {
            Platform.runLater(() -> gotoChapter.clear());
        }
    }

    public void beginReadingAtBookmark() {
        //need to change this methodname probably cause its not actually launching reader
        //need something in initialization block to determine if button is active or not.
        // "you dont have any bookmarks yet you silly bastard" same with historypane

        selectedMangaIdentNumber = bookmark.get(0).getTitleId();
        selectedMangaTotalChapNum = bookmark.get(0).getTotalChapters();
        selectedMangaLastChapReadNum = bookmark.get(0).getLastChapterRead();
        selectedMangaCurrentPageNum = bookmark.get(0).getCurrentPage();
//        beginReading(selectedMangaIdentNumber);
        HistoryPane.storeHistory();
        launchReader();
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

    public void generateStringListForPredicateMap(ActionEvent event) {
        predicateList.clear();
        CheckBox checkBox = (CheckBox) event.getSource();
        int indexNumber = (Integer.parseInt(checkBox.getId().substring(5)));

        popupClose();
        //better place for this?
        indexIncrementValue = 0;

        if (checkBox.isSelected()) {
            genreStrings.set(indexNumber, "genre" + indexNumber + ".include");
        } else if (!checkBox.isSelected() && !checkBox.isIndeterminate()) {
            genreStrings.set(indexNumber, "genre" + indexNumber + ".exclude");
        } else if (checkBox.isIndeterminate()) {
            genreStrings.set(indexNumber, "");
        }
        searchStringBuilder();
    }

    private void createListFromFilteredStreamResults(ArrayList<MangaArrayList> arrayList, Predicate<MangaArrayList> predicate) {
        populateCurrentContentList(arrayList.stream()
                .filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new)));
        setThumbsPaneStaticNavElements();
        populateCurrentCategoryPaneThumbs();
    }

    private void searchStringBuilder() {
        Map<String, Predicate<MangaArrayList>> genres = GenreMap.getGenreMap();

        for (int i = 0; i < 39; i++) {
            if (genreStrings.get(i).length() > 0) {
                predicateList.add(genres.get(genreStrings.get(i)));
            }
        }
        createListFromFilteredStreamResults(Objects.requireNonNull(matchListToCurrentCategory()), predicateList.stream().reduce(w -> true, Predicate::and));
    }

    public void searchBox() {
//        currentContent.clear();
        popupClose();
        String searchTerm = removeInvalidValues(searchBox.getText());

        createListFromFilteredStreamResults(Objects.requireNonNull(matchListToCurrentCategory()), m -> m.getTitle()
                .matches("(?i).*\\b(" + searchTerm + ")\\b.*") || m.getSummary()
                .matches("(?i).*\\b(" + searchTerm + ")\\b.*"));
        searchBox.clear();
    }

    private String removeInvalidValues(String string) {
        return string.replaceAll("[^a-zA-Z0-9!:;.,<>~`!@#$%^&]+", "");
    }

    public void resetThumbsPaneToDefault() {
        popupClose();
        ascendDescend.setSelected(false);
        for (CheckBox genres : checkBoxes) {
            genres.setIndeterminate(true);
            genres.setSelected(false);
        }
        predicateList.clear();
        createOrResetGenreStringArray();
        populateCurrentContentList(matchListToCurrentCategory());
        setThumbsPaneStaticNavElements();
        populateCurrentCategoryPaneThumbs();
    }

    public void ignoreManga() {
//        if (currentCategory.getValue().equals("Rejected")) {
//            if (lastChapterDownloaded == totalChaptersNumber) {
//                modifyThread.execute(this::moveToReading);
//                //new
//                collectedMangaList.add(rejectedMangaList.get(selectionIdentNum));
//                rejectedMangaList.remove(selectionIdentNum);
//            } else {
//                modifyThread.execute(this::moveToDownloading);
//                //new
//                //all this add remove shit can be moved to methods, too duplicatie and all voer.
//                downloading.add(rejectedMangaList.get(selectionIdentNum));
//                rejectedMangaList.remove(selectionIdentNum);
//            }
//        } else {
//            database.openDb(Values.DB_NAME_MANGA.getValue());
//            database.moveManga(currentDatabase(), "not_interested", selectionIdentNum);
//            database.deleteManga(currentDatabase(), selectionIdentNum);
//            database.closeDb();
//            //new
//            rejectedMangaList.add(currentContent.get(currentListIndex));
//        }
        //new
        if (currentCategory.getValue().equals("Rejected")) {
            downloading.add(rejectedMangaList.get(currentListIndex));
        } else {
            rejectedMangaList.add(currentContent.get(currentListIndex));
        }
        matchListToCurrentCategory().remove(fetchOriginalIndexNumber());
        currentContent.remove(currentListIndex);
        //new
        popupClose();
        searchStringBuilder();
//        modifyThread.execute(this::databaseInit);
//        navButtonVisibility();
    }

    private void createOrResetGenreStringArray() {
        for (int i = 0; i < 39; i++) {
            genreStrings.add(i, "");
        }
    }

    private String currentDatabase() {
        //so gross, sooo grossssss
        String database = "";

        switch (currentCategory.getValue()) {
            case "Not Collected":
                database = Values.DB_TABLE_AVAILABLE.getValue();
                break;
            case "Collected":
                database = Values.DB_TABLE_READING.getValue();
                break;
            case "Rejected":
                database = Values.DB_TABLE_NOT_INTERESTED.getValue();
                break;
            case "Completed":
                database = Values.DB_TABLE_COMPLETED.getValue();
                break;
        }
        return database;
    }

    public void choiceBox() {
        popupClose();
        ascendDescend.setSelected(false);
        populateCurrentContentList(matchListToCurrentCategory());
        setThumbsPaneStaticNavElements();
        setThumbsPaneDynamicNavElements();
        //this is technically redundant, may want to break searchstringbuilder into two methods, especially if this
        //gets used more than twice.
        searchStringBuilder();
//        currentParentList = matchListToCurrentCategory();
//        popupClose();
//        searchStringBuilder();
//        indexIncrementValue = 0;
//        currentContent.clear();
//        currentContent.addAll(Objects.requireNonNull(returnSourceListForCurrentCategory()));
//        ascendDescend.setSelected(false);
//        populateCurrentCatagoryPaneThumbs();
    }

    public void gotoSpecificPage() {
        popupClose();
        if (!enterPageNumber.getText().equals("")) {
            indexIncrementValue = (Integer.parseInt(enterPageNumber.getText()) - 1) * 30;
            enterPageNumber.clear();
            setThumbsPaneDynamicNavElements();
            populateCurrentCategoryPaneThumbs();
        }
    }

    public void turnIndexPage(ActionEvent event) {
        // use button id method to get the button id.
        // and obviously all this needs to be sent away and not done here.
        Button button = (Button) event.getSource();
        popupClose();

        int remainder = 30 * (currentContentListSize / 30);

        System.out.println(currentContentListSize);
        System.out.println(remainder);

        if (remainder == currentContentListSize) {
            remainder = remainder - 30;
        }

        switch (button.getId()) {
            case "pageFirst":
                indexIncrementValue = 0;
                break;
            case "pagePrevious":
                indexIncrementValue = indexIncrementValue - 30;
                break;
            case "pageNext":
                indexIncrementValue = indexIncrementValue + 30;
                break;
            case "pageLast":
                indexIncrementValue = remainder;
                break;
        }
        populateCurrentCategoryPaneThumbs();
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

        infoBoxToggleButtons(InfoBox.displayCorrectInfoBox(currentCategory.getValue()));

        positionInfoAndRepairBox(thumbNumber);

        popupPane.setVisible(true);

//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        ResultSet results = database.filterManga(currentDatabase(), "title_id", mangaId.get(currentListIndex));
//        MangaListView view = currentContent.get(currentListIndex);
        //redundant arraylist, this resultset needs to be utalized in a separate method
//        ResultSet results = database.filterManga(currentDatabase(), "title_id", Integer.toString(currentContent.get(currentListIndex).getTitleId()));

//        if (results.getString("title").length() > 63) {
//            String finalTitle = results.getString("title").substring(0, 63) + " ...";
//            mangaTitle.setText(finalTitle);
//        } else {
//            mangaTitle.setText(results.getString("title"));
//        }

//        mangaTitle.setText(abridgeTitle(currentListIndex));
//        mangaThumb.setImage(new Image(new FileInputStream(thumbsPath + File.separator + currentContent.get(currentListIndex).getTitleId() + ".jpg")));
//        mangaSummary.setText(currentContent.get(currentListIndex).getSummary());
//        mangaGenres.setText(currentContent.get(currentListIndex).getGenreTags());
//        mangaAuthors.setText(currentContent.get(currentListIndex).getAuthors());
//        totalChaptersNumber = currentContent.get(currentListIndex).getTotalChapters();
//        currentPageNumber = currentContent.get(currentListIndex).getCurrentPage();
//        lastChapterReadNumber = currentContent.get(currentListIndex).getLastChapterRead();
//        lastChapterDownloaded = currentContent.get(currentListIndex).getLastChapterDownloaded();


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
        selectionIdentNum = currentContent.get(currentListIndex).getTitleId(); //honestly only worth a fuck for db shit now
        fetchOriginalIndexNumber();

    }

    private void positionInfoAndRepairBox(int thumbClickedIdent) {
        popupPane.setLayoutX(InfoBox.positionInfoBox(thumbClickedIdent));
        repairPane.setLayoutX(InfoBox.positionRepairBox(thumbClickedIdent));
    }

    private int fetchOriginalIndexNumber() {
        return IntStream.range(0, Objects.requireNonNull(matchListToCurrentCategory()).size())
                .filter(i -> Objects.requireNonNull(matchListToCurrentCategory()).get(i).getTitleId() == currentContent.get(currentListIndex).getTitleId())
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

    private String abridgeTitle(String title) {
        if (title.length() > 63) {
            return title.substring(0, 63) + " ...";
        }
        return title;
    }

    public void popupClose() {
        popupPane.setVisible(false);
        repairPane.setVisible(false);
    }

    public void showRepairPopup() {
        repairPane.setVisible(true);
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
}

//        lastChapterRead.lengthProperty().addListener(new ChangeListener<>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                forceNumbersOnly(oldValue, newValue, lastChapterRead);
//            }
//        });

//    public void historyShow() {
//        try {
//            populateHistory();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        historyPaneVisibility(true);
//    }
//
//    public void historyHide() {
//        historyPaneVisibility(false);
//    }

//    private void scrollUpDown(int direction) {
//        //can replace this with mangaturnpage class method
//        if (direction == 1) {
//            softwareMouse.mouseWheel(scrollSpeed);
//        } else if (direction == -1) {
//            softwareMouse.mouseWheel(-scrollSpeed);
//        }
//    }


//    private void infoBoxPosition(int imageClicked) {
//        if (imageClicked <= 14) {
//            popupPane.setLayoutX(1144);
////            innerPopup.setLayoutX(1329);
//            repairPane.setLayoutX(1329);
//        } else {
//            popupPane.setLayoutX(15);
////            innerPopup.setLayoutX(200);
//            repairPane.setLayoutX(200);
//        }
//    }

//    private void pageNumberDisplay() {
//        int currentPageNumber = (indexIncrementValue / 30) + 1;
//        int totalPagesNumber = currentContent.size() / 30;
//
//        if (currentContent.size() % 30 > 0) {
//            totalPagesNumber++;
//        }
//
//        if (totalPagesNumber == 0) {
//            currentPageNumber = 0;
//        }
//
//        if (currentPageNumber < 10) {
//            currentPageNumberDisplay.setLayoutX(1669);
//        } else if (currentPageNumber < 100) {
//            currentPageNumberDisplay.setLayoutX(1655);
//        } else {
//            currentPageNumberDisplay.setLayoutX(1642);
//        }
//        currentPageNumberDisplay.setText(Integer.toString(currentPageNumber));
//        lastPageNumberDisplay.setText(Integer.toString(totalPagesNumber));
//    }


//    private void navButtonVisibility() {
//        if (currentContent.size() > 30) {
//            pageNumTextPane.setVisible(true);
//        } else {
//            pageNumTextPane.setVisible(false);
//        }
//
//        if (indexIncrementValue == 0 && currentContent.size() > 30) {
//            pageFirst.setVisible(false);
//            pagePrevious.setVisible(false);
//            pageNext.setVisible(true);
//            pageLast.setVisible(true);
//        } else if (indexIncrementValue >= 30 && indexIncrementValue < currentContent.size() - 30) {
//            pageFirst.setVisible(true);
//            pagePrevious.setVisible(true);
//            pageNext.setVisible(true);
//            pageLast.setVisible(true);
//        } else if (indexIncrementValue != 0 && indexIncrementValue == (currentContent.size() - currentContent.size() % 30) - 30 && currentContent.size() % 30 == 0 || indexIncrementValue != 0 && indexIncrementValue == (currentContent.size() - currentContent.size() % 30)) {
//            // if pagenumber does NOT equal 0 but DOES equal the highest notCollectedMangaList page. much easier than this shitshow.
//            pageFirst.setVisible(true);
//            pagePrevious.setVisible(true);
//            pageNext.setVisible(false);
//            pageLast.setVisible(false);
//        } else {
//            pageFirst.setVisible(false);
//            pagePrevious.setVisible(false);
//            pageNext.setVisible(false);
//            pageLast.setVisible(false);
//        }
//        pageNumberDisplay();
//    }

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
//        System.out.println("collectedMangaList");
//    }

//    public void showAvailableMangaBooksPane() {
//        //legacy?
//        popupClose();
////        launchPane.setVisible(false);
//        currentCategory.setValue("Not Collected");
//        catalogPane.setVisible(true);
//    }
//
//    public void showOwnedMangaBooksPane() {
//        //legacy?
//        popupClose();
////        launchPane.setVisible(false);
//        currentCategory.setValue("Collected");
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
//            if (currentCategory.getValue().equals("Collected")) {
//                dbSearch("title_id, new_chapters, favorite", currentDatabase(), " WHERE " + joiner.toString(), sortOrder);
//            } else {
//                dbSearch("title_id", currentDatabase(), " WHERE " + joiner.toString(), sortOrder);
//            }
//        } else if (joiner.length() == 0) {
//            if (currentCategory.getValue().equals("Collected")) {
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
//        if (currentCategory.getValue().equals("Collected")) {
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
//                    if (currentCategory.getValue().equals("Collected")) {
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
//        populateCurrentCatagoryPaneThumbs();
//    }

//    public void lastPage() throws Exception {
//        resetThumbs();
//        hideThumbs();
//        popupClose();
////        int position = mangaId.size() - mangaId.size() % 30; - this is a duplicate of pagenumber, whats its purpose? lol I dont know.
//        int view = 0;
//        indexIncrementValue = mangaId.size() - mangaId.size() % 30;
//
//        for (int i = indexIncrementValue; view < mangaId.size() % 30; i++) {
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
//        if (currentCategory.getValue().equals("Not Collected")) {
//            tableName = Values.DB_TABLE_AVAILABLE.getValue();
//        } else if (currentCategory.getValue().equals("Rejected")) {
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
//        indexIncrementValue = 0;
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
////        populateCurrentCatagoryPaneThumbs();
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