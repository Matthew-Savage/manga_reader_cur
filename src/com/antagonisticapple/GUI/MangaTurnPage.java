package com.antagonisticapple.GUI;

import com.antagonisticapple.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

import static com.antagonisticapple.CategoryMangaLists.*;

public class MangaTurnPage {

    private static int lastChapterRead = currentContent.get(currentContentListIndexNumber).getLastChapterRead();
    private static int totalChapters = currentContent.get(currentContentListIndexNumber).getTotalChapters();


    public static void turnPagePreviousNext(KeyEvent event, int currentChapterFinalPage) {
        int currentPageNum = currentContent.get(currentContentListIndexNumber).getCurrentPage();

        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            gotoNextPage(currentPageNum, currentChapterFinalPage);
        }
        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            gotoPreviousPage(currentPageNum, currentChapterFinalPage);
        }
    }

    public static void scrollPageUpDown(KeyEvent event, ScrollPane scrollPane){
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
            scrollCurrentPageUp(scrollPane);
        }
        if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            scrollCurrentPageDown(scrollPane);
        }
    }

    private static void gotoNextPage(int currentPageNum, int currentChapterFinalPage) {
        if (currentPageNum < currentChapterFinalPage) {
//            ControllerMain.updateCurrentPageNumber(currentPageNum);
//            ControllerMain.resetReadingViewToTopOfPage();
            MangaValues.changeCurrentPageNumber(currentPageNum + 1, Values.DB_NAME_MANGA.getValue());
        } else {
            nextChapter();
        }
    }

    private static void gotoPreviousPage(int currentPageNum, int currentChapterFinalPage) {
        if (currentPageNum > 0) {
//            ControllerMain.updateCurrentPageNumber(currentPageNum - 1);
            MangaValues.changeCurrentPageNumber(currentPageNum - 1, Values.DB_NAME_MANGA.getValue());
        } else {
            previousChapter();
        }
    }

    private static void scrollCurrentPageDown(ScrollPane scrollPane) {
        if (scrollPane.getVvalue() < 1.0) {
            ControllerMain.scrollMangaPageDown();
        }
    }

    private static void scrollCurrentPageUp(ScrollPane scrollPane) {
        if (scrollPane.getVvalue() > 1.0) {
            ControllerMain.scrollMangaPageUp();
        }
    }

    public static void simulateMouseWheelScroll(ScrollEvent scrollMouseWheel) {
        if (scrollMouseWheel.getDeltaY() == 40) {
            scrollMouseWheel.consume();
            ControllerMain.scrollMangaPageUp();
        }
        if (scrollMouseWheel.getDeltaY() == -40) {
            scrollMouseWheel.consume();
            ControllerMain.scrollMangaPageDown();
        }
    }

    public static int adjustScrollSpeed(ActionEvent event, int currentScrollSpeed) {
        Button button = (Button) event.getSource();

        if (button.getId().equals("sidebarPane_scrollSpeed_decrease") && currentScrollSpeed > 1) {
            return currentScrollSpeed - 1;
        }
        if (button.getId().equals("sidebarPane_scrollSpeed_increase") && currentScrollSpeed < 10) {
            return currentScrollSpeed + 1;
        }
        return currentScrollSpeed;
    }

    private static void nextChapter() {
        if (lastChapterRead > totalChapters) {
            MangaValues.changeLastChapterRead(lastChapterRead + 1, Values.DB_NAME_MANGA.getValue());
            //method to load nextg chapter into reader
        }
        if (lastChapterRead == totalChapters) {
            changeMangaStatus();
        }
    }

    private static void changeMangaStatus() {
        MangaValues.changeStatus(true, Values.DB_NAME_MANGA.getValue());
        //method to close reader
    }

    private static void previousChapter() {
        if (lastChapterRead > 0) {
            MangaValues.changeLastChapterRead(lastChapterRead - 1, Values.DB_NAME_MANGA.getValue());
            // currently the final page number is calculated AFTER this step, so -1 triggers a future if block
            // which then sets the correct pagenumber. not sure if thats okay or not yet, we'll see.
            // honestly pretty sure im going to break it apart and call fetch manga pages here as well as when
            // user first tries to read the manga. makes sense.
            MangaValues.changeCurrentPageNumber(-1, Values.DB_NAME_MANGA.getValue());
            //method to load previous chapter into reader
        }
    }
}
//why both calc chap method and changing chapreadnumber in this method?



//        if (event.getCode() == KeyCode.D && currentPageNumber < currentContent.size() || event.getCode() == KeyCode.RIGHT && currentPageNumber < currentContent.size()) {
//            currentContent.get(thumbNumber + pageNumber).setCurrentPage(currentPageNumber);
//            //this makes it not work for history, god damn it lol, does it end? we need to basically break setarray, its not gonna work
//            setArray().get(selectionIdentNum).setCurrentPage(currentPageNumber);
//            displayNextOrPreviousMangaBookPage(currentPageNumber);
//            scrollImagePane.setVvalue(0.0);
//            calculateChapterNumber();
//        } else if (event.getCode() == KeyCode.A && currentPageNumber > 0 || event.getCode() == KeyCode.LEFT && currentPageNumber > 0) {
//            currentContent.get(thumbNumber + pageNumber).setCurrentPage(currentPageNumber);
//            setArra y().get(selectionIdentNum).setCurrentPage(currentPageNumber);
//            displayNextOrPreviousMangaBookPage(currentPageNumber);
//        } else if (event.getCode() == KeyCode.A && currentPageNumber == 0 && lastChapterReadNumber > 0 || event.getCode() == KeyCode.LEFT && currentPageNumber == 0 && lastChapterReadNumber > 0) {
//            lastChapterReadNumber--;
//            currentPageNumber = -1;
//            currentContent.get(thumbNumber + pageNumber).setLastChapterRead(lastChapterReadNumber);
//            currentContent.get(thumbNumber + pageNumber).setCurrentPage(currentPageNumber);
//            //we need to call this fetch origional index number shit some other time, or at the very least, not repeatedly.
//            setArray().get(selectionIdentNum).setLastChapterRead(lastChapterReadNumber);
//            setArray().get(selectionIdentNum).setCurrentPage(currentPageNumber);
//            beginReading(currentContent.get(thumbNumber + pageNumber).getTitleId());
//            // change current folder to lower and current page to highest available
//            insertMangaBookmark();
