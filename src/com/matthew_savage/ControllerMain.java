package com.matthew_savage;

// we really shouldnt ship this build without a way to automate future updates, doing manually is soooo fucking lame its unreal. even placeholder image would have been so easy, doesnt exist? fine, download from blablabla.
// add favorites only category
// and tentataive!
// add update button to info pane for... - lol finished but it doesnt actually do anything, neat lol fuck. need to write changes to db if there were any, but there arent. need to have it check another website i guess.
// add cancel button to update catalog
// add a tool suite to let natqlie fix issues on her own, like the zero total chapters bug i just fucking implemented
//        "dont reset page when adding manga.. fucking irritating as fuck"
//        "let me search just title, descrip, or author fuck" - gonna have to be a choicebox due to space
// honestly choicebox is fucking gay, side tabs would be way cooler, and replace choicebox with checkbox bubble whatevers fuck. tabs would actually be the same thing tbh lol, yaya - push thumbs to side, really cool
//        genre tags should work WITH search box, just crashed the entire fucking program just now by running a search lol.
// maybe somehow prevent manga from becoming complete if still in download queue? no idea how but... moop. maybe set last chapter downloaded
// to -1? that might be something.
// search author by clicking
// if last char on author and genre is a comma, which it is, remove it.


import com.matthew_savage.GUI.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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

    private static Timeline timeline = new Timeline();

    public void initialize() {
        GenreMap.createGenreString();
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
            ErrorLogging.logError(e.toString());
        }

        //observablelist?

        executor.execute(this::generateHistoryPane);
        executor.execute(this::populateStats);
//        executor.execute(this::createOrResetGenreStringArray);
        executor.execute(this::displayInternetStatus);

        errorMessage.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ErrorLogging.logError(errorMessage.getValue());
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
//        loltest();
    }


//    FXMLLoader loader = new FXMLLoader(getClass().getResource("calc.fxml"));
//    Parent calcRoot = loader.load();
//    CalcController controller = loader.getController();
//controller.setVariables(...);
//    Scene showCalc = new Scene(calcRoot, 500, 1000);
//// ...

    // START OF APP FLOW

    private void loltest() {

//        ArrayList<String> tags = collectedMangaList.stream()
//                .flatMap(x -> Arrays.stream(x.getGenreTags().split(", ")))
//                .collect(Collectors.toCollection(ArrayList::new));
//
//        Map<String, Long> occurrences = tags.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
//
//        Map sorted = occurrences.entrySet().stream().sorted(comparingByKey()).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2));
//
//

        int action = 0;
        int adult = 0;
        int adventure = 0;
        int comedy = 0;
        int cooking = 0;
        int doujinshi = 0;
        int drama = 0;
        int ecchi = 0;
        int fantasy = 0;
        int genderBender = 0;
        int harem = 0;
        int historical = 0;
        int horror = 0;
        int josei = 0;
        int manhua = 0;
        int manhwa = 0;
        int martialArts = 0;
        int mature = 0;
        int mecha = 0;
        int medical = 0;
        int mystery = 0;
        int oneShot = 0;
        int psychological = 0;
        int romance = 0;
        int schoolLife = 0;
        int sciFi = 0;
        int seinen = 0;
        int shoujo = 0;
        int shoujoAi = 0;
        int shounen = 0;
        int shounenAi = 0;
        int sliceOfLife = 0;
        int smut = 0;
        int sports = 0;
        int supernatural = 0;
        int tragedy = 0;
        int webtoons = 0;
        int yaoi = 0;
        int yuri = 0;

        for (Manga title : collectedMangaList) {
            if (title.getGenreTags().contains("Action")) {
                action++;
            }
            if (title.getGenreTags().contains("Adult")) {
                adult++;
            }
            if (title.getGenreTags().contains("Adventure")) {
                adventure++;
            }
            if (title.getGenreTags().contains("Comedy")) {
                comedy++;
            }
            if (title.getGenreTags().contains("Cooking")) {
                cooking++;
            }
            if (title.getGenreTags().contains("Doujinshi")) {
                doujinshi++;
            }
            if (title.getGenreTags().contains("Drama")) {
                drama++;
            }
            if (title.getGenreTags().contains("Ecchi")) {
                ecchi++;
            }
            if (title.getGenreTags().contains("Fantasy")) {
                fantasy++;
            }
            if (title.getGenreTags().contains("Gender bender")) {
                genderBender++;
            }
            if (title.getGenreTags().contains("Harem")) {
                harem++;
            }
            if (title.getGenreTags().contains("Historical")) {
                historical++;
            }
            if (title.getGenreTags().contains("Horror")) {
                horror++;
            }
            if (title.getGenreTags().contains("Josei")) {
                josei++;
            }
            if (title.getGenreTags().contains("Manhua")) {
                manhua++;
            }
            if (title.getGenreTags().contains("Manhwa")) {
                manhwa++;
            }
            if (title.getGenreTags().contains("Martial arts")) {
                martialArts++;
            }
            if (title.getGenreTags().contains("Mature")) {
                mature++;
            }
            if (title.getGenreTags().contains("Mecha")) {
                mecha++;
            }
            if (title.getGenreTags().contains("Medical")) {
                medical++;
            }
            if (title.getGenreTags().contains("Mystery")) {
                mystery++;
            }
            if (title.getGenreTags().contains("One shot")) {
                oneShot++;
            }
            if (title.getGenreTags().contains("Psychological")) {
                psychological++;
            }
            if (title.getGenreTags().contains("Romance")) {
                romance++;
            }
            if (title.getGenreTags().contains("School life")) {
                schoolLife++;
            }
            if (title.getGenreTags().contains("Sci fi")) {
                sciFi++;
            }
            if (title.getGenreTags().contains("Seinen")) {
                seinen++;
            }
            if (title.getGenreTags().contains("Shoujo")) {
                shoujo++;
            }
            if (title.getGenreTags().contains("Shoujo ai")) {
                shoujoAi++;
            }
            if (title.getGenreTags().contains("Shounen")) {
                shounen++;
            }
            if (title.getGenreTags().contains("Shounen ai")) {
                shounenAi++;
            }
            if (title.getGenreTags().contains("Slice of life")) {
                sliceOfLife++;
            }
            if (title.getGenreTags().contains("Smut")) {
                smut++;
            }
            if (title.getGenreTags().contains("Sports")) {
                sports++;
            }
            if (title.getGenreTags().contains("Supernatural")) {
                supernatural++;
            }
            if (title.getGenreTags().contains("Tragedy")) {
                tragedy++;
            }
            if (title.getGenreTags().contains("Webtoons")) {
                webtoons++;
            }
            if (title.getGenreTags().contains("Yaoi")) {
                yaoi++;
            }
            if (title.getGenreTags().contains("Yuri")) {
                yuri++;
            }
        }

        System.out.println("Action: " + action +
                "\\nAdult: " + adult +
                "\\nAdventure: " + adventure +
                "\\nComedy: " + adult +
                "\\nCooking: " + adult +
                "\\nDoujinshi: " + adult +
                "\\nDrama: " + adult +
                "\\nEcchi: " + adult +
                "\\nFantasy: " + adult +
                "\\nGender bender: " + adult +
                "\\nHarem: " + adult +
                "\\nHistorical: " + adult +
                "\\nHorror: " + adult +
                "\\nJosei: " + adult +
                "\\nManhua: " + adult +
                "\\nManhwa: " + adult +
                "\\nMartial arts: " + adult +
                "\\nMature: " + adult +
                "\\nMecha: " + adult +
                "\\nMedical: " + adult +
                "\\nMystery: " + adult +
                "\\nOne shot: " + adult +
                "\\nPsychological: " + adult +
                "\\nRomance: " + adult +
                "\\nSchool life: " + adult +
                "\\nSci fi: " + adult +
                "\\nSeinen: " + adventure +
                "\\nShoujo: " + adult +
                "\\nShoujo ai: " + adult +
                "\\nShounen: " + adult +
                "\\nShounen ai: " + adult +
                "\\nSlice of life: " + adult +
                "\\nSmut: " + adult +
                "\\nSports: " + adult +
                "\\nSupernatural: " + adult +
                "\\nTragedy: " + adult +
                "\\nWebtoons: " + adult +
                "\\nYaoi: " + adult +
                "\\nYuri: " + adult
        );

    }

    public void openCategory(ActionEvent event) {
        RadioButton rb = (RadioButton) event.getSource();
        currentCategoryNumber = Integer.parseInt(rb.getId());
        //all the other stuff that happens kek
    }

    public void fetchAgain() {
        RemoteCatalog.forceUpdate();
        populateMangaInfoPane();
    }

    public static void killDownloadProcess() {
        downloadThread.shutdown();
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
        // test this with the fucking method for setting current content and see if it divides by zero
        // and kills us all
        // need to sort updates by least to most as well, fun!
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
            ErrorLogging.logError(e.toString());
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
            ErrorLogging.logError(e.toString());
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
            // need to update this with real number logic
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
        resetThumbsPaneToDefault(matchListToCurrentCategory());
    }

    private void setHistoryPaneVisibility(boolean isVisible) {
        historyClosePane.setVisible(isVisible);
        historyPaneContent.setVisible(isVisible);
    }

    private ArrayList<Manga> matchListToCurrentCategory() {

        if (tabNotCollected.isSelected()) {
            return notCollectedMangaList;
        } else if (tabCollected.isSelected()) {
            return collectedMangaList;
        } else if (tabRejected.isSelected()) {
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
        ignoreMangaShort.setVisible(isShown.get(0));
        readManga.setVisible(isShown.get(1));
        ignoreManga.setVisible(isShown.get(2));
        getInnerPopup.setVisible(isShown.get(3));
        unignore.setVisible(isShown.get(4));
        rereadManga.setVisible(isShown.get(5));
        getIssuePopup.setDisable(isShown.get(6));
        authorPaneShort.setVisible(isShown.get(7));
        authorPaneLong.setVisible(isShown.get(8));
        if (tabCollected.isSelected() || tabCompleted.isSelected()) {
            toggleFavoriteButton();
        }
    }

    private void toggleFavoriteButton() {
        if (CategoryMangaLists.selectedMangaIsFavoriteTEMP == 1) {
            if (catalogPane.isVisible()) {
                unfavorite.setVisible(true);
                favorite.setVisible(false);
            } else {
                sidebarPane_unfavorite.setVisible(true);
                sidebarPane_favorite.setVisible(false);
            }
        } else {
            if (catalogPane.isVisible()) {
                unfavorite.setVisible(false);
                favorite.setVisible(true);
            } else {
                sidebarPane_unfavorite.setVisible(false);
                sidebarPane_favorite.setVisible(true);
            }
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
        getSelectedMangaValues(matchListToCurrentCategory());
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
//        FIXME
//        infoBoxToggleButtons(InfoBox.displayCorrectInfoBox(currentCategory.getValue()));
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
            if (ControllerLoader.isUpdatable()) {
                checkIfUpdated.scheduleWithFixedDelay(updateRunnable, 15, 1800, TimeUnit.SECONDS);
                downloadThread.scheduleWithFixedDelay(DownloadRunnable.processDownloadQueue, 1, 1, TimeUnit.MINUTES);
            }
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
            ErrorLogging.logError(e.toString());
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
        if (HistoryPane.storeHistory()) {
        }
//        removeNewChapFlagIfPresent();
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

    public void processNavKeys(KeyEvent event) {
        if (System.currentTimeMillis() - firstTime > 70 || firstSet) {
            firstSet = false;
            firstTime = System.currentTimeMillis();
            navigateMangaCurrentlyReading(event);
        } else {
            event.consume();
        }
    }

    private void navigateMangaCurrentlyReading(KeyEvent event) {
        //split this into two methods someday
        if (userIsScrolling(event)) {
            MangaPageScrolling.scrollPageUpDown(event);
        } else {
            readerRequestFocus();
            MangaPageTurning.turnPagePreviousNext(event);
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
    }

    public void sidebarInvisible() {
        sidebarPane.setOpacity(0);
        readerRequestFocus();
    }

    public static void mangaImageFilesToList() {
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
        if (tabCompleted.isSelected()) {
            tabCollected.setSelected(true);
        }
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
            // only possible exception is IllegalArgumentException
            // only possible cause is user frantically spamming field
            System.out.println(e);
            ErrorLogging.logError(e.toString());
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
        createListFromFilteredStreamResults(Objects.requireNonNull(matchListToCurrentCategory()), predicateList.stream().reduce(w -> true, Predicate::and));
    }

    public void searchBox() {
        popupClose();
        waitPane.setVisible(true);
        executor.execute(this::searchWait);
    }

    public void searchWait() {
        ArrayList<Predicate<Manga>> predicateArrayList = new ArrayList<>();
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

    public void resetButton() {
        favorite.setVisible(false);
        unfavorite.setVisible(false);
        resetThumbsPaneToDefault(matchListToCurrentCategory());
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
    //========================================================

    //need to put current downloads in ui

    //need to make all the found new chapters shit ui shit

    // need to log deleted entries via invalidentry AND manga urls being changed by the same
    // thing. need to create a logging class.

    //add genre tag mouseovers to ui

    //lol all the stats logic. this app is getting respectable i feel like

    //add tentative section, button, dropdown, database, everything lol.

    //add below to just about every text field
//            sidebarCurScrollSpeed.setEditable(false);
//        sidebarCurScrollSpeed.setMouseTransparent(true);
//        sidebarCurScrollSpeed.setFocusTraversable(false);

//    CURRENTLY THUMB DOWNLOAD AND OTHER THINGS ARE COMMENTED OUT OF SOURCEWEBSITE, ALSO STARTING PAGENUMBER WRONG

    //=========================================================

    public void readSelectedManga() {

    }

    public void downloadSelectedManga() {

    }


    //========================================================

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

    // done and ready for testing

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
        getThisManga(collectedMangaList);
    }

    public void addNewManga() {
        getThisManga(notCollectedMangaList);
    }

    private void getThisManga(ArrayList<Manga> sourceDatabase) {
//        MangaValues.addAndRemove(sourceDatabase, downloading, parentListIndexNumberTEMP, true);
//        MangaValues.executeChanges();
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
        MangaValues.justRemove(matchListToCurrentCategory(), parentListIndexNumberTEMP);
        MangaValues.executeChanges();
        popupClose();
        searchStringBuilder();
    }

    public void changeCompletedToReading() {
        selectedMangaLastChapReadNumTEMP = 0;
        selectedMangaCurrentPageNumTEMP = 0;
        MangaValues.modifyValue(completedMangaList, StaticStrings.DB_COL_LAST_CHAP_READ.getValue(), selectedMangaLastChapReadNumTEMP, selectedMangaIdentNumberTEMP);
        MangaValues.modifyValue(completedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentPageNumTEMP, selectedMangaIdentNumberTEMP);
        MangaValues.addAndRemove(completedMangaList, collectedMangaList, parentListIndexNumberTEMP, true);
        popupClose();
        getSelectedMangaValues(collectedMangaList);
        preReadingTasks();
    }

    // done and ready for testing end

    public void ignoreManga() {
        if (tabRejected.isSelected()) {
            getThisManga(rejectedMangaList);
        } else {
//            FIXME
//            MangaValues.addAndRemove(toList(currentCategory.getValue()), rejectedMangaList, parentListIndexNumberTEMP, true);
            MangaValues.executeChanges();
        }
        popupClose();
        searchStringBuilder();
    }

    private static void removeNewChapFlagIfPresent() {
        if (selectedMangaNewChapNumTEMP > 0) {
            selectedMangaNewChapNumTEMP = 0;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_NEW_CHAP_BOOL.getValue(), 0, selectedMangaIdentNumberTEMP);
        }
    }

    // need to have a if else method for what the current category is, it just needs to happen lets be real.
    // do that before moving on from this favorite toggle or youll forget.

    public void toggleFavorite() {
        if (selectedMangaIsFavoriteTEMP == 1) {
            selectedMangaIsFavoriteTEMP = 0;
//FIXME
//            MangaValues.modifyValue(toList(currentCategory.getValue()), StaticStrings.DB_COL_FAVE_BOOL.getValue(), selectedMangaIsFavoriteTEMP, selectedMangaIdentNumberTEMP);
        } else {
            selectedMangaIsFavoriteTEMP = 1;
//            FIXME
//            MangaValues.modifyValue(toList(currentCategory.getValue()), StaticStrings.DB_COL_FAVE_BOOL.getValue(), selectedMangaIsFavoriteTEMP, selectedMangaIdentNumberTEMP);
        }

        toggleFavoriteButton();

        if (catalogPane.isVisible()) {
            searchStringBuilder();
        } else {
            resetThumbsPaneToDefault(matchListToCurrentCategory());
        }
        MangaValues.executeChanges();
    }
}