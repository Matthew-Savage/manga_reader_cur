package com.matthew_savage;

import com.matthew_savage.GUI.*;
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
    private TextField sidebarTotalChapNum;
    @FXML
    private TextField searchBox;
    @FXML
    private TextField currentPageNumberDisplay;
    @FXML
    private TextField lastPageNumberDisplay;
    @FXML
    private TextField enterPageNumber;
    @FXML
    private TextField sidebarCurChapNum;
    @FXML
    private TextField sidebarPageNumber;
    @FXML
    private TextField gotoChapter;
    @FXML
    private TextField sidebarCurScrollSpeed;
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
    @FXML
    private Button scrollSpeedDown;
    @FXML
    private Button scrollSpeedUp;


//    private Changes changes = new Changes();

    private static int thumbsPageCurrentPageNum;
    private static int thumbsPageLastPageNum;
    private static int clickedThumbIdent;
    private static int clickedButtonIdent;
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
    private File thumbsPath = new File(Values.DIR_ROOT.getValue() + File.separator + Values.DIR_THUMBS.getValue());
    private int indexIncrementValue = 0;
    private int selectionIdentNum = -1;
    private static Executor executor = Executors.newFixedThreadPool(10);
    private static Executor modifyThread = Executors.newSingleThreadExecutor();
    static ScheduledExecutorService downloadThread = Executors.newScheduledThreadPool(1);
    private static ScheduledExecutorService checkIfUpdated = Executors.newScheduledThreadPool(1);
    private DownloadMangaPages downloadMangaPages = new DownloadMangaPages(this);

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

        executor.execute(this::generateHistoryPane);
//        executor.execute(this::populateStats);
//        executor.execute(this::createOrResetGenreStringArray);
        executor.execute(this::displayInternetStatus);

        enterPageNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                forceNumbersOnly(enterPageNumber);
            }
        });

        gotoChapter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                forceNumbersOnly(gotoChapter);
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

        defaultThumbPane(collectedMangaList);
    }


//    FXMLLoader loader = new FXMLLoader(getClass().getResource("calc.fxml"));
//    Parent calcRoot = loader.load();
//    CalcController controller = loader.getController();
//controller.setVariables(...);
//    Scene showCalc = new Scene(calcRoot, 500, 1000);
//// ...

    // START OF APP FLOW

    private void defaultThumbPane(ArrayList<MangaArrayList> currentActivityMangaList) {
        initializeGenreStringsArray();
        populateCurrentContentList(currentActivityMangaList);
        setThumbsPaneStaticNavElements();
        populateCurrentCategoryPaneThumbs();
    }

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
        //need to combine this and other into one huge method.
        //honestly can toggle the entire UI with one list.
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
            imageViews.get(i).setImage(Objects.requireNonNull(getImageFromFilesystem(currentContent.get(i + indexIncrementValue).getTitleId())));
            addFavoriteBannerIfFavorite(i);
            addNewChaptersBannerIfNewChapters(i);
            imageViews.get(i).setVisible(true);
        }
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
        if (catIsCollectedOrComplete()) {
            prioritizeFavoritesAndUpdates();
        }
    }

    private boolean catIsCollectedOrComplete() {
        return currentCategory.getValue().equals(Values.CAT_COLLECTED.getValue()) || currentCategory.getValue().equals(Values.CAT_COMPLETED.getValue());
    }

    private void prioritizeFavoritesAndUpdates() {
        // test this with the fucking method for setting current content and see if it divides by zero
        // and kills us all
        // need to sort updates by least to most as well, fun!
        currentContent = currentContent.stream()
                .sorted(Comparator
                        .comparing(MangaArrayList::getNewChapters) // it cant really be this easy, can it?
                        .thenComparing(MangaArrayList::isFavorite)
                        .reversed())
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    private int getTerminationValue() {
        //move out of here
        int numberOfThumbs = currentContentListSize - indexIncrementValue;

        if (numberOfThumbs < 30) {
            return numberOfThumbs;
        } else {
            return 30;
        }
    }

    private Image getImageFromFilesystem(int listIndexNumber) {
        //move out of here
        //dont need to pass this value in with new catmangalists class
        try {
            return new Image(new FileInputStream(thumbsPath + File.separator + listIndexNumber + ".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Image getImageFromFilesystem(File imageFile) {
        //move out of here
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
        if (currentContent.get(listIndexNumber + indexIncrementValue).getNewChapters() > 0) {
            newChaptersOverlayViews.get(listIndexNumber).setImage(new Image("assets/new_chapter_multiple.png"));
        }
        newChaptersOverlayViews.get(listIndexNumber).setVisible(true);
    }

    // END OF APP FLOW

    public void ascendDescend() {
        popupClose();
        Collections.reverse(currentContent);
        setThumbsPaneDynamicNavElements();
        populateCurrentCategoryPaneThumbs();
    }

    public void openHistoryPane() {
        populateCurrentContentList(history);
        populateHistory();
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
        //get this the fuck out of here
        System.out.println(genreStrings.size());

        if (currentCategory.getValue().equals(Values.CAT_NOT_COLLECTED.getValue())) {
            return notCollectedMangaList;
        } else if (currentCategory.getValue().equals(Values.CAT_COLLECTED.getValue())) {
            return collectedMangaList;
        } else if (currentCategory.getValue().equals(Values.CAT_REJECTED.getValue())) {
            return rejectedMangaList;
        } else {
            return completedMangaList;
        }
    }

    public static void scrollMangaPageUp() {
        softwareMouse.mouseWheel(-currentMouseWheelSpeed);
    }

    public static void scrollMangaPageDown() {
        softwareMouse.mouseWheel(currentMouseWheelSpeed);
    }

    public void scrollWheel(ScrollEvent scrollMouseWheel) {
        MangaPageScrolling.simulateMouseWheelScroll(scrollMouseWheel);
    }

    public void adjustMangaPageUpDownSpeed(ActionEvent event) {
        currentMouseWheelSpeed = MangaPageScrolling.adjustScrollSpeed(event, currentMouseWheelSpeed);
        setReaderSidebarCurScrollSpeed();
    }

    private void readerRequestFocus() {
        mangaPageVerticalScrollPane.requestFocus();
    }

    private void adjustImageViewWidth(double newWidth) {
        imageView.setFitWidth(newWidth);
    }

    public void beginReadingAtBookmark() {
        //need to change this methodname probably cause its not actually launching reader
        //need something in initialization block to determine if button is active or not.
        // "you dont have any bookmarks yet you silly bastard" same with historypane

        selectedMangaIdentNumberTEMP = bookmark.get(0).getTitleId();
        selectedMangaTotalChapNumTEMP = bookmark.get(0).getTotalChapters();
        selectedMangaLastChapReadNumTEMP = bookmark.get(0).getLastChapterRead();
        selectedMangaCurrentPageNumTEMP = bookmark.get(0).getCurrentPage();
        mangaImageFilesToList();
        launchReader();
    }

    public void gotoSpecificPage() {
        popupClose();
        if (enterPageNumber.getText().length() > 0) {
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

    private void positionInfoAndRepairBox(int thumbClickedIdent) {
        popupPane.setLayoutX(InfoBox.positionInfoBox(thumbClickedIdent));
        repairPane.setLayoutX(InfoBox.positionRepairBox(thumbClickedIdent));
    }

    private int fetchOriginalIndexNumber() {
        return IntStream.range(0, Objects.requireNonNull(matchListToCurrentCategory()).size())
                .filter(i -> Objects.requireNonNull(matchListToCurrentCategory()).get(i).getTitleId() == currentContent.get(currentContentListIndexNumberTEMP).getTitleId())
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

    private boolean userIsScrolling(KeyEvent event) {
        return event.getCode() == KeyCode.UP
                || event.getCode() == KeyCode.W
                || event.getCode() == KeyCode.DOWN
                || event.getCode() == KeyCode.S;
    }

    private void initializeGenreStringsArray() {
        genreStrings.clear();
        for (int i = 0; i < 39; i++) {
            genreStrings.add(i, "");
        }
    }

    public void choiceBox() {
        popupClose();
        ascendDescend.setSelected(false);
        searchStringBuilder();
        setThumbsPaneStaticNavElements();
        setThumbsPaneDynamicNavElements();
    }

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
        currentContentListIndexNumberTEMP = indexIncrementValue + clickedButtonIdent;
        getSelectedMangaValues();
        preReadingTasks();
    }

    public void getMangaIdFromImageClicked(MouseEvent imageClicked) {
        ImageView image = (ImageView) imageClicked.getSource();
        clickedThumbIdent = Integer.parseInt(image.getId().substring(18));
        currentContentListIndexNumberTEMP = indexIncrementValue + clickedThumbIdent;
        getSelectedMangaValues();
        openMangaInfoPane();
    }

    private void getSelectedMangaValues() {
        selectedMangaIdentNumberTEMP = currentContent.get(currentContentListIndexNumberTEMP).getTitleId();
        selectedMangaTitleTEMP = currentContent.get(currentContentListIndexNumberTEMP).getTitle();
        selectedMangaAuthorsTEMP = currentContent.get(currentContentListIndexNumberTEMP).getAuthors();
        selectedMangaStatusTEMP = currentContent.get(currentContentListIndexNumberTEMP).getStatus();
        selectedMangaSummaryTEMP = currentContent.get(currentContentListIndexNumberTEMP).getSummary();
        selectedMangaWebAddressTEMP = currentContent.get(currentContentListIndexNumberTEMP).getWebAddress();
        selectedMangaGenresTEMP = currentContent.get(currentContentListIndexNumberTEMP).getGenreTags();
        selectedMangaTotalChapNumTEMP = currentContent.get(currentContentListIndexNumberTEMP).getTotalChapters();
        selectedMangaCurrentPageNumTEMP = currentContent.get(currentContentListIndexNumberTEMP).getCurrentPage();
        selectedMangaLastChapReadNumTEMP = currentContent.get(currentContentListIndexNumberTEMP).getLastChapterRead();
        selectedMangaLastChapDownloadedTEMP = currentContent.get(currentContentListIndexNumberTEMP).getLastChapterDownloaded();
        selectedMangaNewChapNumTEMP = currentContent.get(currentContentListIndexNumberTEMP).getNewChapters();
        selectedMangaIsFavoriteTEMP = currentContent.get(currentContentListIndexNumberTEMP).isFavorite();
    }

    public void openMangaInfoPane() {
        infoBoxToggleButtons(InfoBox.displayCorrectInfoBox(currentCategory.getValue()));
        positionInfoAndRepairBox(clickedThumbIdent);
        populateMangaInfoPane();
        popupPane.setVisible(true);
    }

    private void populateMangaInfoPane() {
        mangaTitle.setText(abridgeTitle(selectedMangaTitleTEMP));
        mangaThumb.setImage(getImageFromFilesystem(selectedMangaIdentNumberTEMP));
        mangaSummary.setText(selectedMangaSummaryTEMP);
        mangaAuthors.setText(selectedMangaAuthorsTEMP);
        mangaGenres.setText(selectedMangaGenresTEMP);
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

    private void generateHistoryPane() {
        mapHistoryPane();
        toggleHistoryButton();
    }

    private void toggleHistoryButton() {
        if (history.size() > 0) {
//            toggleHistoryButton(true);
        }
    }

    private void populateHistory() {
        try {
            for (int i = 0; i < history.size(); i++) {
                historyThumbViews.get(i).setSmooth(true);
                historyThumbViews.get(i).setImage(new Image(new FileInputStream(thumbsPath + File.separator + history.get(i).getTitleId() + ".jpg")));
                historyTitleFields.get(i).setText(history.get(i).getTitle());
                historySummaryFields.get(i).setText(history.get(i).getSummary());
                historyReadButtons.get(i).setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void preReadingTasks() {
        MangaValues.setBookmark(Values.DB_NAME_MANGA.getValue());
        HistoryPane.storeHistory();
        parentListIndexNumberTEMP = fetchOriginalIndexNumber();
        removeNewChapFlagIfPresent();
        mangaImageFilesToList();
        launchReader();
    }

    private void launchReader() {
        popupClose();
        adjustImageViewWidth(920);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        setCurrentPageImageInReader();
        setAllSidebarInfoFields();
        openReadingPaneAndChildren();
        readerRequestFocus();
    }

    private void setAllSidebarInfoFields() {
        setReaderSidebarCurChapNum();
        setReaderSidebarFinalChapNum();
        setReaderSidebarCurScrollSpeed();
        setReaderSidebarCurPageNum();
    }

    private void setCurrentPageImageInReader() {
        imageView.setImage(getImageFromFilesystem(mangaPageImageFiles.get(selectedMangaCurrentPageNumTEMP)));
        mangaPageVerticalScrollPane.setVvalue(0.0);
    }

    private void setReaderSidebarCurChapNum() {
        sidebarCurChapNum.setText(String.valueOf(selectedMangaLastChapReadNumTEMP + 1));
    }

    private void setReaderSidebarFinalChapNum() {
        sidebarTotalChapNum.setText(String.valueOf(selectedMangaTotalChapNumTEMP));
    }

    private void setReaderSidebarCurScrollSpeed() {
        sidebarCurScrollSpeed.setText(String.valueOf(currentMouseWheelSpeed));
        toggleScrollSpeedButtons();
    }

    private void toggleScrollSpeedButtons() {
        if (currentMouseWheelSpeed > 9) {
            scrollSpeedUp.setVisible(false);
        }
        if (currentMouseWheelSpeed < 2) {
            scrollSpeedDown.setVisible(false);
        }
        if (currentMouseWheelSpeed > 1 && currentMouseWheelSpeed < 10) {
            scrollSpeedUp.setVisible(true);
            scrollSpeedDown.setVisible(true);
        }
    }

    private void setReaderSidebarCurPageNum() {
        sidebarPageNumber.setText(String.valueOf(selectedMangaCurrentPageNumTEMP + 1));
    }

    private void openReadingPaneAndChildren() {
        setReadingPaneVisibility(true);
    }

    private void closeReadingPaneAndChildren() {
        imageView.setImage(null);
        setReadingPaneVisibility(false);
    }

    private void setReadingPaneVisibility(boolean isVisible) {
        readMangaPane.setVisible(isVisible);
        sidebarPane.setVisible(isVisible);
        catalogPane.setVisible(!isVisible);
    }

    public void navigateMangaCurrentlyReading(KeyEvent event) {
        //split this into two methods someday
        if (userIsScrolling(event)) {
            MangaPageScrolling.scrollPageUpDown(event);
        } else {
            readerRequestFocus();
            MangaPageTurning.turnPagePreviousNext(event);
            if (bookmark.isEmpty()) {
                closeReadingPaneAndChildren();
                MangaValues.changeStatus(true, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
            } else {
                MangaValues.setBookmark(Values.DB_NAME_MANGA.getValue());
                setCurrentPageImageInReader();
                setReaderSidebarCurChapNum();
                setReaderSidebarCurPageNum();
            }
        }
    }

    public void sidebarVisible() {
        sidebarPane.setOpacity(1);
    }

    public void sidebarInvisible() {
        sidebarPane.setOpacity(0);
        readerRequestFocus();
    }

    public static void mangaImageFilesToList() {
        // doesnt need to be in controller at all
        mangaPageImageFiles.clear();
        File file = new File(Values.DIR_ROOT.getValue() +
                File.separator +
                Values.DIR_MANGA.getValue() +
                File.separator +
                selectedMangaIdentNumberTEMP +
                File.separator +
                selectedMangaLastChapReadNumTEMP);
        mangaPageImageFiles.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
        selectedMangaCurrentChapLastPageNumTEMP = mangaPageImageFiles.size() - 1;
    }

    public void sidebarGoBack() {
        imageView.setImage(null);
        readMangaPane.setVisible(false);
        sidebarPane.setVisible(false);
        catalogPane.setVisible(true);
    }

    public void sidebarGotoChapter() {
        if (gotoChapter.getText().length() > 0) {
            MangaValues.changeLastChapterRead(Integer.parseInt(gotoChapter.getText()) - 1, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
            MangaValues.changeCurrentPageNumber(0, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
            gotoChapter.clear();
            mangaImageFilesToList();
            setCurrentPageImageInReader();
            setReaderSidebarCurPageNum();
            setReaderSidebarCurChapNum();
            MangaValues.setBookmark(Values.DB_NAME_MANGA.getValue());
        }
    }

    private void forceNumbersOnly(TextField textField) {
        try {
            if (textField.getText().length() > 0) {
                if (textField.getText().matches("[0-9]+")) {
                    minMaxNumber();
                } else {
                    textField.setText(textField.getText().substring(0, textField.getLength() - 1));
                }
            }
        } catch (Exception e) {
            // only possible exception is IllegalArgumentException
            // only possible cause is user frantically spamming field
            System.out.println(e);
        }
    }

    private void minMaxNumber() {
        if (gotoChapter.getText().length() > 0) {
            if (Integer.parseInt(gotoChapter.getText()) < 1) {
                gotoChapter.setText("1");
            }
            if (Integer.parseInt(gotoChapter.getText()) > selectedMangaTotalChapNumTEMP) {
                gotoChapter.setText(Integer.toString(selectedMangaTotalChapNumTEMP));
            }
        }
        if (enterPageNumber.getText().length() > 0) {
            if (Integer.parseInt(enterPageNumber.getText()) < 1) {
                enterPageNumber.setText("1");
            }
            if (Integer.parseInt(enterPageNumber.getText()) > thumbsPageLastPageNum) {
                enterPageNumber.setText(String.valueOf(thumbsPageLastPageNum));
            }
        }
    }

    public void generateStringListForPredicateMap(ActionEvent event) {
        predicateList.clear();
        CheckBox checkBox = (CheckBox) event.getSource();
        int indexNumber = (Integer.parseInt(checkBox.getId().substring(5)));
        popupClose();
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
        ArrayList<Predicate<MangaArrayList>> predicateArrayList = new ArrayList<>();
        for (String eachWord : removeInvalidValues(searchBox.getText()).split(" ")) {
            predicateArrayList.add(m -> removeInvalidValues(m.getTitle()).matches(
                    "(?i).*\\b(" + eachWord + ")\\b.*") || removeInvalidValues(m.getSummary()).matches(
                    "(?i).*\\b(" + eachWord + ")\\b.*"));
        }
        searchBox.clear();
        createListFromFilteredStreamResults(Objects.requireNonNull(matchListToCurrentCategory()), predicateArrayList.stream().reduce(Predicate::and).orElse(p -> true));
    }

    private String removeInvalidValues(String string) {
        return string.replaceAll("[^a-zA-Z0-9!:;.,<>~`@#$%^&= ]+", "");
    }

    public void resetThumbsPaneToDefault() {
        popupClose();
        ascendDescend.setSelected(false);
        for (CheckBox genres : checkBoxes) {
            genres.setIndeterminate(true);
            genres.setSelected(false);
        }
        predicateList.clear();
        initializeGenreStringsArray();
        defaultThumbPane(matchListToCurrentCategory());
    }
    //========================================================

    //fix check for last manga added, need to have like 3-5 mangas and if the first one isnt found after x pages
    //then go to next one. and make the pool get bigger. like first sweep is 10 pages and next sweep is like 50 pages
    //also need to delete the now missing manga. what a fucking pain

    //need to put current downloads in ui

    //need to make all the found new chapters shit ui shit

    //need to sort new chapters by number of new chapters, so make a list of every manga with new chapters
    //and sort from highest to lowest or whatever, then output. ez. remove from reading and add to this new list
    //no db writes necessary for this i feel like / false, so what actually needs to happen is new chapters is no
    //longer a bool, instead 0 is no new chapters, and when downloading, every chapter downloaded is new chapters++
    //final number will be number of new chapters, easy. and we cant sort by new chapters amount I dont feel like,
    //or maybe we can. but if we cant, or maybe regardless, lets set the number of new chapters as a number for
    //display. cool.

    // need to log deleted entries via invalidentry AND manga urls being changed by the same
    // thing. need to create a logging class.

    // also make sure to actually write delete entry method because right now its blank kekeke

    //add genre tag mouseovers to ui

    //lastfivemangas resultset is going to have a LOT of missing fields, need to set them to null and blabla whatever.

    //lol all the stats logic. this app is getting respectable i feel like

    //add tentative section, button, dropdown, database, everything lol.

    //add below to just about every text field
//            sidebarCurScrollSpeed.setEditable(false);
//        sidebarCurScrollSpeed.setMouseTransparent(true);
//        sidebarCurScrollSpeed.setFocusTraversable(false);


    //=========================================================

    public void readSelectedManga() {

    }

    public void downloadSelectedManga() {

    }


    //========================================================

    private Runnable updateShit = UpdateCollectedMangas::seeIfUpdated;

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

    // done and ready for testing

    public void mangaProblem() {
        if (selectedMangaLastChapReadNumTEMP < 3) {
            MangaValues.changeLastChapterRead(0, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
        } else {
            MangaValues.changeLastChapterRead(selectedMangaLastChapReadNumTEMP - 3, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
        }
        MangaValues.changeLastChapterDownloaded(0, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
        MangaValues.changeCurrentPageNumber(0, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
        getThisManga(Values.DB_TABLE_READING.getValue());
    }

    public void addNewManga() {
        getThisManga(Values.DB_TABLE_AVAILABLE.getValue());
    }

    private void getThisManga(String sourceDatabase) {
        MangaValues.addToDownloading(sourceDatabase);
        popupClose();
        searchStringBuilder();
    }

    public void changeCompletedToReading() {
        selectedMangaLastChapReadNumTEMP = 0;
        selectedMangaCurrentPageNumTEMP = 0;
        MangaValues.insertMangaIntoDatabase(Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
        MangaValues.deleteMangaFromDatabase(Values.DB_TABLE_COMPLETED.getValue(), Values.DB_NAME_MANGA.getValue());
        popupClose();
        resetThumbsPaneToDefault();
    }

    // done and ready for testing end

    public void ignoreManga() {
        String tableName;
        if (currentCategory.getValue().equals(Values.CAT_REJECTED.getValue())) {
            tableName = Values.DB_TABLE_READING.getValue();
        } else {
            tableName = Values.DB_TABLE_NOT_INTERESTED.getValue();
        }
        MangaValues.insertMangaIntoDatabase(tableName, Values.DB_NAME_MANGA.getValue());
        MangaValues.deleteMangaFromDatabase(tableName, Values.DB_NAME_MANGA.getValue());
        popupClose();
        searchStringBuilder();
    }

    private static void removeNewChapFlagIfPresent() {
        if (currentContent.get(currentContentListIndexNumberTEMP).getNewChapters() > 0) {
            MangaValues.changeNewChaptersFlag(0, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
        }
    }

    public void toggleFavorite() {
        if (selectedMangaIsFavoriteTEMP) {
            MangaValues.changeFavoriteFlag(false, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
        } else {
            MangaValues.changeFavoriteFlag(true, Values.DB_TABLE_READING.getValue(), Values.DB_NAME_MANGA.getValue());
        }
        if (catalogPane.isVisible()) {
            searchStringBuilder();
        } else {
            resetThumbsPaneToDefault();
        }
    }

    private void writePreDownloadValues() {
        //legacy
//        ArrayList chapterCount = null;
//        try {
//            chapterCount = indexMangaChapters.getChapterCount(webAddress);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        database.openDb(Values.DB_NAME_MANGA.getValue());
//        database.modifyManga(Values.DB_TABLE_AVAILABLE.getValue(), selectionIdentNum, "last_chapter_read", startingChapter);
////        database.modifyManga(Values.DB_TABLE_AVAILABLE.getValue(), selectionIdentNum, "total_chapters", chapterCount.size());
//        database.closeDb();
//        return chapterCount.size();
    }

//    private Runnable downloadManga = () -> {
//        //this is gonna be fun on a bun son
//        System.out.println("downloading");
//        boolean firstDownload;
//        int downloadingStartingChapter;
//        int downloadingLastChapterDownloaded;
//        int downloadingCurrentPage;
//        int downloadingTitleId;
//        String downloadingWebAddress;
//        database.openDb(Values.DB_NAME_DOWNLOADING.getValue());
//        ResultSet resultSet = database.downloadQueueFetch("downloading");
//
//        try {
//            if (resultSet.next()) {
//                firstDownload = true;
//                downloadingStartingChapter = resultSet.getInt("last_chapter_read");
//                downloadingLastChapterDownloaded = resultSet.getInt("last_chapter_downloaded");
//                downloadingTitleId = resultSet.getInt("title_id");
//                downloadingWebAddress = InvalidEntry.verifyAddress(resultSet.getString("web_address"), resultSet.getString("title"));
//                database.modifyManga("downloading", downloadingTitleId, "web_address", downloadingWebAddress);
//                downloadingCurrentPage = resultSet.getInt("current_page");
//                database.closeDb();
//
//                if (downloadingCurrentPage == -1) {
//                    firstDownload = false;
//                    //might be able to use this for downloading new titles in full; downloadingStartingChapter = 0 replaces above and above comes down here.
//                }
//
//                if (downloadingLastChapterDownloaded > downloadingStartingChapter) {
//                    downloadingStartingChapter = downloadingLastChapterDownloaded; //so this makes sure that a resumed download starts at the last chapter that the last download left off
//                }
//                downloadMangaPages.getChapterPages(downloadingStartingChapter, downloadingWebAddress, downloadingTitleId, firstDownload);
//            }
//        } catch (Exception e) {
//            System.out.println("errorShow with runnable downloadManga " + e);
//        }
//
//        //new
////        check this whole first download shit because yeah, pretty sure its meaningless now, lol thats quite the reduction there billy
////        while (!downloading.isEmpty()) {
////            downloadMangaPages.getChapterPages(downloading.get(0).getLastChapterDownloaded(), );
////        }
//        try {
//            for (MangaArrayList download : downloading) {
//                downloadMangaPages.getChapterPages(download.getLastChapterDownloaded(), download.getWebAddress(), download.getTitleId(), true);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    };
}