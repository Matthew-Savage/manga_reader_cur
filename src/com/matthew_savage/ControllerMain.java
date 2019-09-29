package com.matthew_savage;

//TODO
// create installer that downloads latest complete reader
// add cancel button to update catalog
// add a tool suite to let natqlie fix issues on her own, like the zero total chapters bug i just fucking implemented
//        "dont reset page when adding manga.. fucking irritating as fuck"
//        genre tags should work WITH search box, just crashed the entire fucking program just now by running a search lol.
// maybe somehow prevent manga from becoming complete if still in download queue? no idea how but... moop. maybe set last chapter downloaded
// to -1? that might be something.
// lol all the stats logic. this app is getting respectable i feel like
// let user select what catagories to pull favorites from for fav foldier, cool!
// mark as complete button, bitches love mark as complete buttons
// need to make all these arraylists multithreaded bro. that curtainly cant be ideal that almost nothing is thread safe.
// in app bug submissipon
// find out what the deal is with the super high resolution korean shit. clearly we need a max resolution for the imgview
// moving to ignore and shit is putting manga in complete queue, that old bug
// clicking read again for a complete manga is totally broken

import com.matthew_savage.GUI.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.matthew_savage.CategoryMangaLists.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

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
    private Pane authorPaneShort;
    @FXML
    private Pane authorPaneLong;
    @FXML
    private Pane waitPane;
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
    private Pane historyClosePane;
    @FXML
    private Pane resetWarning;
    @FXML
    private Pane historyPaneContent;
    @FXML
    private Pane downloadQueue;
    @FXML
    private Pane statsPane;
    @FXML
    private Pane dropdown;
    @FXML
    private Pane errorIcon;
    @FXML
    private Pane updateIcon;
    @FXML
    private Pane updatePaneWait;
    @FXML
    private Pane updatePaneNew;
    @FXML
    private Pane updatePaneNotNew;
    @FXML
    private StackPane imageFrame;
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
    private TextField authorShort;
    @FXML
    private TextField authorLong;
    @FXML
    private TextField dropdownMessageUpdate;
    @FXML
    private TextField sidebarTotalChapNum;
    @FXML
    private TextField searchBox;
    @FXML
    private TextField chapterTot;
    @FXML
    private TextField status;
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
    private TextField downloadTotal;
    @FXML
    private TextField statGenreThree;
    @FXML
    private TextArea dropdownMessage;
    @FXML
    private TextArea mangaSummary;
    @FXML
    private TextArea genreText;
    @FXML
    private RadioButton tabNotCollected;
    @FXML
    private RadioButton tabCollected;
    @FXML
    private RadioButton tabCompleted;
    @FXML
    private RadioButton tabFavorites;
    @FXML
    private RadioButton tabTentative;
    @FXML
    private RadioButton tabRejected;
    @FXML
    private RadioButton searchTitle;
    @FXML
    private RadioButton searchAuthor;
    @FXML
    private RadioButton searchSummary;
    @FXML
    private Button pageFirst;
    @FXML
    private Button fetchAgain;
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
    private Button bookmarkToggle;
    @FXML
    private Button unfavorite;
    @FXML
    private Button sidebarPane_favorite;
    @FXML
    private Button sidebarPane_unfavorite;
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
    @FXML
    private Button titleAddShort;
    @FXML
    private Button titleReadShort;
    @FXML
    private Button titleFavAddShort;
    @FXML
    private Button titleFavRemoveShort;
    @FXML
    private Button undecidedShort;
    @FXML
    private Button rejectShort;
    @FXML
    private Button titleAddLong;
    @FXML
    private Button titleFavAddLong;
    @FXML
    private Button titleFavRemoveLong;
    @FXML
    private Button undecidedLong;
    @FXML
    private Button rejectLong;

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
    private File thumbsPath = new File(StaticStrings.DIR_ROOT.getValue() + File.separator + StaticStrings.DIR_THUMBS.getValue());
    private int indexIncrementValue = 0;
    private static Executor executor = Executors.newFixedThreadPool(10);
    private static Executor modifyThread = Executors.newSingleThreadExecutor();
    static ScheduledExecutorService downloadThread = Executors.newScheduledThreadPool(1);
    private static ScheduledExecutorService checkIfUpdated = Executors.newScheduledThreadPool(1);
//    private DownloadMangaPages downloadMangaPages = new DownloadMangaPages(this);

    public static StringProperty errorMessage = new SimpleStringProperty("");
    public static StringProperty downloadMessage = new SimpleStringProperty("");
    public static StringProperty updateMessage = new SimpleStringProperty("");
    public static boolean firstUpdateRun = false;
    private static double scrollValue;

    private static Timeline timeline = new Timeline();

    public void initialize() {

        imageView.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                processNavKeys(event);
            }
        });

        sidebarPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                processNavKeys(event);
            }
        });

        main.addEventFilter(KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                processNavKeys(event);
            }
        });

        GenreMap.createGenreString();
        GenreToolTipMap.createGenreToolTipStrings();
        CategoryMangaLists.currentParentList = CategoryMangaLists.collectedMangaList;
//        currentCategory.setItems(FXCollections.observableArrayList("Not Collected", "Collected", "Completed", "Rejected"));

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
            Logging.logError(e.toString());
        }

        //observablelist?

        executor.execute(this::generateHistoryPane);
        executor.execute(this::populateStats);
//        executor.execute(this::createOrResetGenreStringArray);
        executor.execute(this::displayInternetStatus);

        errorMessage.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Logging.logError(errorMessage.getValue());
                updateIcon.setVisible(false);
                dropdownMessageUpdate.setVisible(false);
                errorIcon.setVisible(true);
                dropdownMessage.setVisible(true);
                dropdownMessage.setText(errorMessage.getValue());
                timeline.play();
            }
        });

        dropdown.layoutYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() == -205) {
                    errorMessage.set("");
                }
            }
        });

        downloadMessage.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (downloadQueue.isVisible() && tabCollected.isSelected()) {
                    resetButton();
                }
                if (Integer.parseInt(newValue) > 0) {
                    downloadQueue.setVisible(true);
                    downloadTotal.setText(newValue);
                } else {
                    downloadQueue.setVisible(false);
                }
            }
        });

        updateMessage.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                errorIcon.setVisible(false);
                dropdownMessage.setVisible(false);
                updateIcon.setVisible(true);
                dropdownMessageUpdate.setVisible(true);
                dropdownMessageUpdate.setText(updateMessage.getValue());
                timeline.play();
            }
        });

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

        //back when shit was resizable i think

//        main.widthProperty().addListener(new ChangeListener<>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
////                imageView.setFitWidth(newValue.doubleValue());
//                adjustImageViewWidth(newValue.doubleValue());
//                mangaPageVerticalScrollPane.setMaxWidth(newValue.doubleValue());
//            }
//        });
//
//        main.heightProperty().addListener(new ChangeListener<>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                mangaPageVerticalScrollPane.setMinHeight(newValue.doubleValue());
//            }
//        });

        //back when shit was resizable i think end

        imageView.fitWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mangaPageVerticalScrollPane.setMaxWidth(newValue.doubleValue());
            }
        });

        timeline.setCycleCount(2);
        timeline.setAutoReverse(true);
//        KeyValue value = new KeyValue(errorDropdown.layoutYProperty(), -2);
//        KeyFrame frame = new KeyFrame(Duration.millis(800), value);


        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(400), new KeyValue(dropdown.layoutYProperty(), -2)),
                new KeyFrame(Duration.seconds(6), new KeyValue(dropdown.layoutYProperty(), -2)));
//                new KeyFrame(Duration.millis(500), new KeyValue(errorDropdown.layoutYProperty(), -205)));


//        timeline.getKeyFrames().add(frame);
//        timeline.setDelay(Duration.seconds(6));

        toggleBookmarkButton();
        defaultThumbPane(collectedMangaList);
        currentCategoryNumber = 2;
    }


//    FXMLLoader loader = new FXMLLoader(getClass().getResource("calc.fxml"));
//    Parent calcRoot = loader.load();
//    CalcController controller = loader.getController();
//controller.setVariables(...);
//    Scene showCalc = new Scene(calcRoot, 500, 1000);
//// ...

    public void genreToolTip(MouseEvent event) {
        CheckBox cb = (CheckBox) event.getSource();
        genreText.setText(GenreToolTipMap.getGenreToolTipMap().get(cb.getId()));
    }

    private void scrollUpDown(int direction) {
        scrollValue = (double) currentMouseWheelSpeed / 10;
        if (direction == 0) {
            scrollUp();
        } else if (direction == 1) {
            scrollDown();
        } else if (direction == 2) {
            navigateMangaCurrentlyReading();
        } else if (direction == 3) {
            closeWindow();
        }
    }

    private void scrollUp() {
        mangaPageVerticalScrollPane.setVvalue(mangaPageVerticalScrollPane.getVvalue() - scrollValue);
    }

    private void scrollDown() {
        mangaPageVerticalScrollPane.setVvalue(mangaPageVerticalScrollPane.getVvalue() + scrollValue);
    }

    private void closeWindow() {
        if (repairPane.isVisible()) {
            repairPane.setVisible(false);
        } else if (popupPane.isVisible()) {
            popupPane.setVisible(false);
        } else if (readMangaPane.isVisible()) {
            sidebarGoBack();
        } else if (statsPane.isVisible()) {
            statsPane.setVisible(false);
        } else if (historyPaneContent.isVisible()) {
            closeHistoryPane();
        }
    }

    public void genreToolTipClose() {
        genreText.clear();
    }

    public void openCategory(ActionEvent event) {
        RadioButton rb = (RadioButton) event.getSource();
        currentCategoryNumber = Integer.parseInt(rb.getId());
        resetButton();
    }

    public void fetchAgain() {
        updatePaneWait.setVisible(true);
        executor.execute(this::findIfNewChapters);

//        RemoteCatalog.forceUpdate();
//        populateMangaInfoPane();
    }

//    private Runnable findIfNewChapters = () -> {
//        int newMaxChapters = IndexMangaChapters.getChapterAddresses(selectedMangaWebAddressTEMP).size();
//        if (newMaxChapters > selectedMangaTotalChapNumTEMP) {
//            System.out.println(selectedMangaTotalChapNumTEMP);
//            selectedMangaTotalChapNumTEMP = newMaxChapters;
//            System.out.println(selectedMangaTotalChapNumTEMP);
//            updatePaneWait.setVisible(false);
//            updatePaneNew.setVisible(true);
//        } else {
//            updatePaneWait.setVisible(false);
//            updatePaneNotNew.setVisible(true);
//        }
//    };

    private void findIfNewChapters() {
        int newMaxChapters = IndexMangaChapters.getChapterAddresses(InvalidEntry.verifyAddress(selectedMangaWebAddressTEMP, selectedMangaTitleTEMP, selectedMangaAuthorsTEMP)).size();
        if (newMaxChapters > selectedMangaTotalChapNumTEMP) {
            if (selectedMangaNewChapNumTEMP > 0) {
                selectedMangaNewChapNumTEMP = (newMaxChapters - selectedMangaTotalChapNumTEMP) + selectedMangaNewChapNumTEMP;
            }
            selectedMangaTotalChapNumTEMP = newMaxChapters;
            updatePaneWait.setVisible(false);
            updatePaneNew.setVisible(true);
        } else {
            updatePaneWait.setVisible(false);
            updatePaneNotNew.setVisible(true);
        }
    };

    public void addNewChapters() {
        closeUpdatePane();
        getThisManga();
    }

    public static void killDownloadProcess() {
        downloadThread.shutdown();
    }

    public void closeUpdatePane() {
        updatePaneNew.setVisible(false);
        updatePaneNotNew.setVisible(false);
    }

    private void defaultThumbPane(ArrayList<Manga> currentActivityMangaList) {
        initializeGenreStringsArray();
        populateCurrentContentList(currentActivityMangaList);
        setThumbsPaneStaticNavElements();
        populateCurrentCategoryPaneThumbs();
    }

    private void populateCurrentContentList(ArrayList<Manga> currentActivityMangaList) {
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
        if (waitPane.isVisible()) {
            waitPane.setVisible(false);
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
        return tabCollected.isSelected() || tabCompleted.isSelected();
    }

    private void prioritizeFavoritesAndUpdates() {
        currentContent = currentContent.stream()
                .sorted(Comparator
                        .comparing(Manga::getNewChapters)
                        .thenComparing(Manga::getFavorite)
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
            Logging.logError(e.toString());
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
            Logging.logError(e.toString());
        }
        return null;
    }

    private void addFavoriteBannerIfFavorite(int listIndexNumber) {
        //dont need to pass this value in with new catmangalists class
        if (currentContent.get(listIndexNumber + indexIncrementValue).getFavorite() == 1) {
            favoriteOverlayViews.get(listIndexNumber).setImage(new Image("assets/favorite_overlay.png"));
            favoriteOverlayViews.get(listIndexNumber).setVisible(true);
        }
    }

    private void addNewChaptersBannerIfNewChapters(int listIndexNumber) {
        int newChapterCount = currentContent.get(listIndexNumber + indexIncrementValue).getNewChapters();
        if (newChapterCount > 0 && newChapterCount < 9) {
            newChaptersOverlayViews.get(listIndexNumber).setImage(new Image("assets/new_chapters_" + newChapterCount + ".png"));
        } else if (newChapterCount >= 9) {
            newChaptersOverlayViews.get(listIndexNumber).setImage(new Image("assets/new_chapters_9.png"));
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
        popupClose();
        populateCurrentContentList(history);
        populateHistory();
        setHistoryPaneVisibility(true);
    }

    public void closeHistoryPane() {
        setHistoryPaneVisibility(false);
        resetThumbsPaneToDefault(toList());
    }

    private void setHistoryPaneVisibility(boolean isVisible) {
        historyClosePane.setVisible(isVisible);
        historyPaneContent.setVisible(isVisible);
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

    private void toggleBookmarkButton() {
        if (bookmark.size() == 0) {
            bookmarkToggle.setDisable(true);
        } else {
            bookmarkToggle.setDisable(false);
        }
    }

    public void beginReadingAtBookmark() {
        selectedMangaIdentNumberTEMP = bookmark.get(0).getTitleId();
        getSelectedMangaValues(collectedMangaList);
        preReadingTasks();
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
        updatePaneWait.setLayoutX(InfoBox.positionInfoBox(thumbClickedIdent));
        updatePaneNew.setLayoutX(InfoBox.positionInfoBox(thumbClickedIdent));
        updatePaneNotNew.setLayoutX(InfoBox.positionInfoBox(thumbClickedIdent));
    }

    public static int fetchOriginalIndexNumber(ArrayList<Manga> parentList, int titleID) {
        return IntStream.range(0, parentList.size())
                .filter(i -> parentList.get(i).getTitleId() == titleID)
//                        currentContent.get(currentContentListIndexNumberTEMP).getTitleId())
                .findFirst()
                .orElse(-1);
    }

//    public static void fetchOriginalIndexNumber(ArrayList<Manga> parentList, int titleID) {
//        parentListIndexNumberTEMP = IntStream.range(0, Objects.requireNonNull(matchListToCurrentCategory()).size())
//                .filter(i -> Objects.requireNonNull(matchListToCurrentCategory()).get(i).getTitleId() == selectedMangaIdentNumberTEMP)
//                .findFirst()
//                .orElse(-1);
//    }

    private void infoBoxToggleButtons(List<Boolean> isShown) {
        titleAddShort.setVisible(isShown.get(0));
        titleReadShort.setVisible(isShown.get(1));
        undecidedShort.setVisible(isShown.get(2));
        rejectShort.setVisible(isShown.get(3));
        titleAddLong.setVisible(isShown.get(4));
        undecidedLong.setVisible(isShown.get(5));
        rejectLong.setVisible(isShown.get(6));
        authorPaneShort.setVisible(isShown.get(7));
        authorPaneLong.setVisible(isShown.get(8));
        getIssuePopup.setDisable(isShown.get(9));
        fetchAgain.setDisable(isShown.get(10));
        disableFavoriteButtons();
        toggleFavoriteButton();
    }

    private void disableFavoriteButtons() {
        titleFavRemoveShort.setVisible(false);
        titleFavAddShort.setVisible(false);
        titleFavRemoveLong.setVisible(false);
        titleFavAddLong.setVisible(false);
    }

    private void toggleFavoriteButton() {
        if (selectedMangaIsFavoriteTEMP == 1) {
            if (catalogPane.isVisible()) {
                toggleInfoBoxFav(false);
            } else {
                sidebarPane_unfavorite.setVisible(true);
                sidebarPane_favorite.setVisible(false);
            }
        } else {
            if (catalogPane.isVisible()) {
                toggleInfoBoxFav(true);
            } else {
                sidebarPane_unfavorite.setVisible(false);
                sidebarPane_favorite.setVisible(true);
            }
        }
    }

    private void toggleInfoBoxFav(boolean show) {
        if (tabTentative.isSelected() || tabRejected.isSelected()) {
            titleFavRemoveLong.setVisible(!show);
            titleFavAddLong.setVisible(show);
        } else {
            titleFavRemoveShort.setVisible(!show);
            titleFavAddShort.setVisible(show);
        }
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

    public void toggleZoomControls() {
        if (imageView.getFitWidth() == 1920) {
            sidebarPaneSizeIncrease.setVisible(false);
        } else if (imageView.getFitWidth() == 920) {
            sidebarPaneSizeDecrease.setVisible(false);
        } else {
            sidebarPaneSizeDecrease.setVisible(true);
            sidebarPaneSizeIncrease.setVisible(true);
        }
    }

    public void mangaBookPageZoomInOrOut(ActionEvent event) {
        // seems like a lot of shit is going on here eh?
        adjustImageViewWidth(MangaZoomInOut.widthValue((Button) event.getSource(), imageView.getFitWidth()));
        toggleZoomControls();
    }

    public void getMangaIdFromButtonPressed(ActionEvent buttonPressed) {
        Button button = (Button) buttonPressed.getSource();
        selectedMangaIdentNumberTEMP = history.get(Integer.parseInt(button.getId().substring(11))).getTitleId();
        getSelectedMangaValues(collectedMangaList);

        // if we put completed back in we have to close the pane BEOFRE we do any of the compltedtoreading shit

        closeHistoryPane();
        preReadingTasks();

    }

    public void getMangaIdFromImageClicked(MouseEvent imageClicked) {
        popupClose();
        ImageView image = (ImageView) imageClicked.getSource();
        clickedThumbIdent = Integer.parseInt(image.getId().substring(18));
        selectedMangaIdentNumberTEMP = currentContent.get(indexIncrementValue + clickedThumbIdent).getTitleId();
        getSelectedMangaValues(toList());
        openMangaInfoPane();
    }

    private void getSelectedMangaValues(ArrayList<Manga> parentList) {
        parentListIndexNumberTEMP = fetchOriginalIndexNumber(parentList, selectedMangaIdentNumberTEMP);
        selectedMangaIdentNumberTEMP = parentList.get(parentListIndexNumberTEMP).getTitleId();
        selectedMangaTitleTEMP = parentList.get(parentListIndexNumberTEMP).getTitle();
        selectedMangaAuthorsTEMP = parentList.get(parentListIndexNumberTEMP).getAuthors();
        selectedMangaStatusTEMP = parentList.get(parentListIndexNumberTEMP).getStatus();
        selectedMangaSummaryTEMP = parentList.get(parentListIndexNumberTEMP).getSummary();
        selectedMangaWebAddressTEMP = parentList.get(parentListIndexNumberTEMP).getWebAddress();
        selectedMangaGenresTEMP = parentList.get(parentListIndexNumberTEMP).getGenreTags();
        selectedMangaTotalChapNumTEMP = parentList.get(parentListIndexNumberTEMP).getTotalChapters();
        selectedMangaCurrentPageNumTEMP = parentList.get(parentListIndexNumberTEMP).getCurrentPage();
        selectedMangaLastChapReadNumTEMP = parentList.get(parentListIndexNumberTEMP).getLastChapterRead();
        selectedMangaLastChapDownloadedTEMP = parentList.get(parentListIndexNumberTEMP).getLastChapterDownloaded();
        selectedMangaNewChapNumTEMP = parentList.get(parentListIndexNumberTEMP).getNewChapters();
        selectedMangaIsFavoriteTEMP = parentList.get(parentListIndexNumberTEMP).getFavorite();
    }

    public void openMangaInfoPane() {
        infoBoxToggleButtons(InfoBox.displayCorrectInfoBox());
        positionInfoAndRepairBox(clickedThumbIdent);
        populateMangaInfoPane();
        popupPane.setVisible(true);
    }

    private void populateMangaInfoPane() {
        mangaTitle.setText(abridgeTitle(selectedMangaTitleTEMP));
        mangaThumb.setImage(getImageFromFilesystem(selectedMangaIdentNumberTEMP));
        mangaSummary.setText(selectedMangaSummaryTEMP);
        mangaGenres.setText(removeEndingComma(selectedMangaGenresTEMP));
        if (catIsCollectedOrComplete()) {
            authorShort.setText(removeEndingComma(selectedMangaAuthorsTEMP));
            chapterTot.setText(String.valueOf(selectedMangaTotalChapNumTEMP));
            status.setText(selectedMangaStatusTEMP);
        } else {
            authorLong.setText(removeEndingComma(selectedMangaAuthorsTEMP));
        }
    }

    private String removeEndingComma(String original) {
        if (original.endsWith(",")) {
            return original.substring(0, original.length() - 1);
        } else {
            return original;
        }
    }

    public void appClose() {
        downloadThread.shutdownNow();
        Stage stage = (Stage) appClose.getScene().getWindow();
        stage.close();

//        errorMessage.set("Cupcaked Reader is presently unable to download! This is likely due to a change " +
//                "to the source code of Manganelo.com. Please alert the development " +
//                "team regarding this issue as soon as you are able in order to prevent the extended unavailability of " +
//                "new releases as well as updates to collected titles. A detailed error message has been logged " +
//                "internally.");
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
                checkIfUpdated.scheduleWithFixedDelay(updateRunnable, 15, 1800, TimeUnit.SECONDS);
                downloadThread.scheduleWithFixedDelay(DownloadRunnable.processDownloadQueue, 1, 1, TimeUnit.MINUTES);
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
        int loopcount = 10;
        if (history.size() < 10) {
            loopcount = history.size();
        }

        try {
            for (int i = 0; i < loopcount; i++) {
                historyThumbViews.get(i).setSmooth(true);
                historyThumbViews.get(i).setImage(new Image(new FileInputStream(thumbsPath + File.separator + history.get(i).getTitleId() + ".jpg")));
                historyTitleFields.get(i).setText(history.get(i).getTitle());
                historySummaryFields.get(i).setText(history.get(i).getSummary());
                historyReadButtons.get(i).setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
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

    public void changeCompletedToReading() {
        // this is only being used by the hiustory pane, or the used to be history pane at least... fml
        selectedMangaLastChapReadNumTEMP = 0;
        selectedMangaCurrentPageNumTEMP = 0;
        MangaValues.modifyValue(completedMangaList, StaticStrings.DB_COL_LAST_CHAP_READ.getValue(), selectedMangaLastChapReadNumTEMP, selectedMangaIdentNumberTEMP);
        MangaValues.modifyValue(completedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentPageNumTEMP, selectedMangaIdentNumberTEMP);
        MangaValues.addAndRemove(completedMangaList, collectedMangaList, parentListIndexNumberTEMP, true);
        popupClose();
        getSelectedMangaValues(collectedMangaList);
        preReadingTasks();
    }

    public void preReadingTasks() {
        if (HistoryPane.storeHistory()) {
        }
//        removeNewChapFlagIfPresent();

        if (tabCompleted.isSelected()) {
            //TODO
            // move from completed to currently reading, reset everything back to zero. database shit
        }

        try {
            mangaImageFilesToList();
            launchReader();
        } catch (NullPointerException|IndexOutOfBoundsException e) {
            e.printStackTrace();
//            repairCollectedManga();
            errorMessage.set("\n" + selectedMangaTitleTEMP + " was unable to successfully launch. To correct this issue the manga has been queued for repair and will once again appear within your collection " +
                    "when this process has completed.");
        }
    }

    private String sourceDatabase() {
        if (tabNotCollected.isSelected()) {
            return StaticStrings.DB_TABLE_AVAILABLE.getValue();
        } else if (tabCollected.isSelected()) {
            return StaticStrings.DB_TABLE_READING.getValue();
        } else if (tabCompleted.isSelected()) {
            return StaticStrings.DB_TABLE_COMPLETED.getValue();
//        } else if (tabFavorites.isSelected()) {
//            // would need to know parent db, fucking hell. honestly might need to setup a new database field to designate the table, or status. maybe everything should either be available or reading in the db? sorted
//            // into catagories during startup?
        } else if (tabTentative.isSelected()) {
            return StaticStrings.DB_TABLE_UNDECIDED.getValue();
        } else if (tabRejected.isSelected()) {
            return StaticStrings.DB_TABLE_NOT_INTERESTED.getValue();
        } else {
            return null;
        }
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
        setBookmark();
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
        toggleFavoriteButton();
    }

    private static long firstTime;
    private static boolean firstSet = false;

    public void processNavKeys(Event event) {
        if (System.currentTimeMillis() - firstTime > 70 || firstSet) {
            firstSet = false;
            firstTime = System.currentTimeMillis();
            if (selectedMangaTotalChapNumTEMP == selectedMangaLastChapReadNumTEMP && selectedMangaCurrentPageNumTEMP == 0) {
                event.consume();
            } else {
                scrollUpDown(InputSorting.sortInputs(event));
            }
        } else {
            event.consume();
        }
    }

    private void navigateMangaCurrentlyReading() {
            if (bookmark.isEmpty()) {
                toggleBookmarkButton();
                resetThumbsPaneToDefault(collectedMangaList);
                closeReadingPaneAndChildren();
            } else {
                setBookmark();
                setCurrentPageImageInReader();
                setReaderSidebarCurChapNum();
                setReaderSidebarCurPageNum();
            }
    }

    public static void setBookmark() {
        bookmark.add(0, collectedMangaList.get(parentListIndexNumberTEMP));
        MangaValues.addToQueue("DELETE FROM bookmark");
        MangaValues.addToQueue("INSERT INTO bookmark (" +
                "title_id, " +
                "title, " +
                "authors, " +
                "status, " +
                "summary, " +
                "web_address, " +
                "genre_tags, " +
                "total_chapters, " +
                "current_page, " +
                "last_chapter_read, " +
                "last_chapter_downloaded, " +
                "new_chapters, " +
                "favorite) VALUES " + "(" +
                "'" + bookmark.get(0).getTitleId() + "', " +
                "'" + bookmark.get(0).getTitle() + "', " +
                "'" + bookmark.get(0).getAuthors() + "', " +
                "'" + bookmark.get(0).getStatus() + "', " +
                "'" + bookmark.get(0).getSummary() + "', " +
                "'" + bookmark.get(0).getWebAddress() + "', " +
                "'" + bookmark.get(0).getGenreTags() + "', " +
                "'" + bookmark.get(0).getTotalChapters() + "', " +
                "'" + bookmark.get(0).getCurrentPage() + "', " +
                "'" + bookmark.get(0).getLastChapterRead() + "', " +
                "'" + bookmark.get(0).getLastChapterDownloaded() + "', " +
                "'" + bookmark.get(0).getNewChapters() + "', " +
                "'" + bookmark.get(0).getFavorite() + "')");
        MangaValues.executeChanges();
    }

    public void sidebarVisible() {
        sidebarPane.setOpacity(1);
        readerRequestFocus();
    }

    public void sidebarInvisible() {
        sidebarPane.setOpacity(0);
        readerRequestFocus();
    }

    public static void mangaImageFilesToList() throws NullPointerException {
        // doesnt need to be in controller at all
        mangaPageImageFiles.clear();
        File file = new File(StaticStrings.DIR_ROOT.getValue() +
                File.separator +
                StaticStrings.DIR_MANGA.getValue() +
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
        toggleBookmarkButton();
        catalogPane.setVisible(true);
        tabCollected.setSelected(true);
        resetThumbsPaneToDefault(collectedMangaList);
    }

    public void sidebarGotoChapter() {
        if (gotoChapter.getText().length() > 0) {
            if (selectedMangaNewChapNumTEMP > 0) {
                if ((Integer.parseInt(gotoChapter.getText()) - 1) - selectedMangaLastChapReadNumTEMP > selectedMangaNewChapNumTEMP) {
                    selectedMangaNewChapNumTEMP = 0;
                } else {
                    selectedMangaNewChapNumTEMP = (Integer.parseInt(gotoChapter.getText()) - 1) - selectedMangaLastChapReadNumTEMP;
                }
                MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_NEW_CHAP_BOOL.getValue(), selectedMangaNewChapNumTEMP, selectedMangaIdentNumberTEMP);
            }
            selectedMangaLastChapReadNumTEMP = Integer.parseInt(gotoChapter.getText()) - 1;
            selectedMangaCurrentPageNumTEMP = 0;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_LAST_CHAP_READ.getValue(), selectedMangaLastChapReadNumTEMP, selectedMangaIdentNumberTEMP);
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentPageNumTEMP, selectedMangaIdentNumberTEMP);
            gotoChapter.clear();
            mangaImageFilesToList();
            setCurrentPageImageInReader();
            setReaderSidebarCurPageNum();
            setReaderSidebarCurChapNum();
            setBookmark();
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
            e.printStackTrace();
            Logging.logError(e.toString());
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

    private void createListFromFilteredStreamResults(ArrayList<Manga> arrayList, Predicate<Manga> predicate) {
        populateCurrentContentList(arrayList.stream()
                .filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new)));
        setThumbsPaneStaticNavElements();
        populateCurrentCategoryPaneThumbs();
    }

    private void searchStringBuilder() {
        Map<String, Predicate<Manga>> genres = GenreMap.getGenreMap();

        for (int i = 0; i < 39; i++) {
            if (genreStrings.get(i).length() > 0) {
                predicateList.add(genres.get(genreStrings.get(i)));
            }
        }
        createListFromFilteredStreamResults(Objects.requireNonNull(toList()), predicateList.stream().reduce(w -> true, Predicate::and));
    }

    public void searchBox() {
        popupClose();
        waitPane.setVisible(true);
        executor.execute(this::searchWait);
    }

    public void searchWait() {
        Predicate<Manga> predicate;
        ArrayList<Predicate<Manga>> predicateArrayList = new ArrayList<>();
        for (String eachWord : removeInvalidValues(searchBox.getText()).split(" ")) {
            if (searchTitle.isSelected()) {
                predicate = m -> removeInvalidValues(m.getTitle()).matches(
                        "(?i).*\\b(" + eachWord + ")\\b.*");
            } else if (searchAuthor.isSelected()) {
                predicate = m -> removeInvalidValues(m.getAuthors()).matches(
                        "(?i).*\\b(" + eachWord + ")\\b.*");
            } else {
                predicate = m -> removeInvalidValues(m.getSummary()).matches(
                        "(?i).*\\b(" + eachWord + ")\\b.*");
            }
            predicateArrayList.add(predicate);
        }
        searchBox.clear();
        createListFromFilteredStreamResults(Objects.requireNonNull(toList()), predicateArrayList.stream().reduce(Predicate::and).orElse(p -> true));
    }

    private String removeInvalidValues(String string) {
        return string.replaceAll("[^a-zA-Z0-9!:;.,<>~`@#$%^&= ]+", "");
    }

    public void resetButton() {
        disableFavoriteButtons();
        resetThumbsPaneToDefault(toList());
    }

    private void resetThumbsPaneToDefault(ArrayList<Manga> currentActivityMangaList) {
        popupClose();
        ascendDescend.setSelected(false);
        for (CheckBox genres : checkBoxes) {
            genres.setIndeterminate(true);
            genres.setSelected(false);
        }
        predicateList.clear();
        defaultThumbPane(currentActivityMangaList);
    }

    private Runnable updateRunnable = UpdateCollectedMangas::checkIfUpdated;

    private void populateStats() {
        statTitlesTotal.setText(StaticStrings.STAT_PRE_TITLE_TOT.getValue() + stats.get(0).getTitlesTotal() + StaticStrings.STAT_SUF_TITLE_TOT.getValue());
        statTitlesReading.setText(StaticStrings.STAT_PRE_READING_TOT.getValue() + stats.get(0).getTitlesReading() + StaticStrings.STAT_SUF_READING_TOT.getValue());
        statTitlesFinished.setText(StaticStrings.STAT_PRE_FIN_TOT.getValue() + stats.get(0).getTitlesFinished() + StaticStrings.STAT_SUF_FIN_TOT.getValue());
        statPagesRead.setText(StaticStrings.STAT_PRE_PAGES_TOT.getValue() + stats.get(0).getPagesRead() + StaticStrings.STAT_SUF_PAGES_TOT.getValue());
        statTitlesFavorite.setText(StaticStrings.STAT_PRE_FAVE_TOT.getValue() + stats.get(0).getFavorites() + StaticStrings.STAT_SUF_FAVE_TOT.getValue());
        statTitlesIgnore.setText(StaticStrings.STAT_PRE_BL_TOT.getValue() + stats.get(0).getBlacklisted() + StaticStrings.STAT_SUF_BL_TOT.getValue());
        statPagesDaily.setText(StaticStrings.STAT_PRE_PAGES_DAY.getValue() + stats.get(0).getDailyPages() + StaticStrings.STAT_SUF_PAGES_DAY.getValue());
        statGenreOne.setText(StaticStrings.STAT_PRE_GEN_ONE.getValue() + stats.get(0).getGenreOne() + ".");
        statGenreTwo.setText(StaticStrings.STAT_PRE_GEN_TWO.getValue() + stats.get(0).getGenreTwo() + ".");
        statGenreThree.setText(StaticStrings.STAT_PRE_GEN_THREE.getValue() + stats.get(0).getGenreThree() + ".");
    }

    public void repairCollectedManga() {
        if (selectedMangaLastChapReadNumTEMP < 3) {
            selectedMangaLastChapReadNumTEMP = 0;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_LAST_CHAP_READ.getValue(), 0, selectedMangaIdentNumberTEMP);
        } else {
            selectedMangaLastChapReadNumTEMP = selectedMangaLastChapReadNumTEMP - 3;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_LAST_CHAP_READ.getValue(), selectedMangaLastChapReadNumTEMP, selectedMangaIdentNumberTEMP);
        }
        selectedMangaLastChapDownloadedTEMP = 0;
        selectedMangaCurrentPageNumTEMP = 0;
        MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_LAST_CHAP_DL.getValue(), 0, selectedMangaIdentNumberTEMP);
        MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), 0, selectedMangaIdentNumberTEMP);
        getThisManga();
    }

    public void addNewManga() {
        if (selectedMangaTotalChapNumTEMP == selectedMangaLastChapDownloadedTEMP) {
            MangaValues.addAndRemove(toList(), collectedMangaList, parentListIndexNumberTEMP, true);
            MangaValues.executeChanges();
            popupClose();
            searchStringBuilder();
        } else {
            getThisManga();
        }
    }

    private void getThisManga() {
        downloading.add(new Manga(selectedMangaIdentNumberTEMP,
                selectedMangaTitleTEMP,
                selectedMangaAuthorsTEMP,
                selectedMangaStatusTEMP,
                selectedMangaSummaryTEMP,
                selectedMangaWebAddressTEMP,
                selectedMangaGenresTEMP,
                selectedMangaTotalChapNumTEMP,
                selectedMangaCurrentPageNumTEMP,
                selectedMangaLastChapReadNumTEMP,
                selectedMangaLastChapDownloadedTEMP,
                selectedMangaNewChapNumTEMP,
                selectedMangaIsFavoriteTEMP));
        DownloadRunnable.addToDatabase(selectedMangaIdentNumberTEMP,
                selectedMangaTitleTEMP,
                selectedMangaAuthorsTEMP,
                selectedMangaStatusTEMP,
                selectedMangaSummaryTEMP,
                selectedMangaWebAddressTEMP,
                selectedMangaGenresTEMP,
                selectedMangaTotalChapNumTEMP,
                selectedMangaCurrentPageNumTEMP,
                selectedMangaLastChapReadNumTEMP,
                selectedMangaLastChapDownloadedTEMP,
                selectedMangaNewChapNumTEMP,
                selectedMangaIsFavoriteTEMP);
        MangaValues.justRemove(toList(), parentListIndexNumberTEMP);
        MangaValues.executeChanges();
        popupClose();
        searchStringBuilder();
    }

    public void ignoreManga() {
        //todo
        // hardcode database changes i guess..
            MangaValues.addAndRemove(toList(), rejectedMangaList, parentListIndexNumberTEMP, true);
            MangaValues.executeChanges();
        popupClose();
        searchStringBuilder();
    }

    public void makeMangaUndecided() {
        //todo
        // hardcode database changes i guess.
        // update: this should be good, test it and if so, apply to ignore and honestly anywhere
        // still using the old method. something about it just doesnt work. sick of fighting with it.


        MangaValues.addToQueue("INSERT INTO " + StaticStrings.DB_TABLE_UNDECIDED.getValue() + " (" +
                "title_id, " +
                "title, " +
                "authors, " +
                "status, " +
                "summary, " +
                "web_address, " +
                "genre_tags, " +
                "total_chapters, " +
                "current_page, " +
                "last_chapter_read, " +
                "last_chapter_downloaded, " +
                "new_chapters, " +
                "favorite) VALUES " + "(" +
                "'" + selectedMangaIdentNumberTEMP + "', " +
                "'" + selectedMangaTitleTEMP + "', " +
                "'" + selectedMangaAuthorsTEMP + "', " +
                "'" + selectedMangaStatusTEMP + "', " +
                "'" + selectedMangaSummaryTEMP + "', " +
                "'" + selectedMangaWebAddressTEMP + "', " +
                "'" + selectedMangaGenresTEMP + "', " +
                "'" + selectedMangaTotalChapNumTEMP + "', " +
                "'" + selectedMangaCurrentPageNumTEMP + "', " +
                "'" + selectedMangaLastChapReadNumTEMP + "', " +
                "'" + selectedMangaLastChapDownloadedTEMP + "', " +
                "'" + selectedMangaNewChapNumTEMP + "', " +
                "'" + selectedMangaIsFavoriteTEMP + "')");

        MangaValues.addToQueue("DELETE FROM " + sourceDatabase() + " WHERE title_id = '" + selectedMangaIdentNumberTEMP + "'");
        undecidedMangaList.add(toList().get(parentListIndexNumberTEMP));
        toList().remove(parentListIndexNumberTEMP);
//        MangaValues.addAndRemove(toList(), undecidedMangaList, parentListIndexNumberTEMP, true);
        MangaValues.executeChanges();
        popupClose();
        searchStringBuilder();
    }

    public void toggleFavorite() {
        if (selectedMangaIsFavoriteTEMP == 1) {
            selectedMangaIsFavoriteTEMP = 0;
            MangaValues.modifyValue(toList(), StaticStrings.DB_COL_FAVE_BOOL.getValue(), selectedMangaIsFavoriteTEMP, selectedMangaIdentNumberTEMP);
        } else {
            selectedMangaIsFavoriteTEMP = 1;
            MangaValues.modifyValue(toList(), StaticStrings.DB_COL_FAVE_BOOL.getValue(), selectedMangaIsFavoriteTEMP, selectedMangaIdentNumberTEMP);
        }
        toggleFavoriteButton();
        if (catalogPane.isVisible()) {
            searchStringBuilder();
        } else {
            resetThumbsPaneToDefault(toList());
        }
        MangaValues.executeChanges();
        modifyFavoriteList();
    }

    private void modifyFavoriteList() {
        if (tabFavorites.isSelected()) {
            favoritesMangaList.remove(parentListIndexNumberTEMP);
            popupClose();
            searchStringBuilder();
            favCollectedOrComplete();
        } else if (tabCollected.isSelected() || tabCompleted.isSelected()) {
            buildFavoritesArray();
        }
    }

    private void favCollectedOrComplete() {
        ArrayList<Manga> originList;
        if (selectedMangaLastChapReadNumTEMP == selectedMangaTotalChapNumTEMP) {
            originList = completedMangaList;
        } else {
            originList = collectedMangaList;
        }
        MangaValues.modifyValue(originList, StaticStrings.DB_COL_FAVE_BOOL.getValue(), selectedMangaIsFavoriteTEMP, selectedMangaIdentNumberTEMP);
    }
}