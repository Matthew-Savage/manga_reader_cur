package com.matthew_savage.GUI;

import com.matthew_savage.ControllerMain;
import com.matthew_savage.MangaValues;
import com.matthew_savage.Values;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static com.matthew_savage.CategoryMangaLists.*;

public class MangaPageTurning {


    public static void turnPagePreviousNext(KeyEvent event) {
        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            gotoNextPage();
        }
        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            gotoPreviousPage();
        }
    }


    private static void gotoNextPage() {
        if (selectedMangaCurrentPageNumTEMP < (selectedMangaCurrentChapLastPageNumTEMP)) {
            System.out.println("going to next page lolz");
            selectedMangaCurrentPageNumTEMP++;
//            MangaValues.changeCurrentPageNumber(currentPageNum + 1, Values.DB_NAME_MANGA.getValue());
        } else {
            nextChapter();
        }
    }

    private static void gotoPreviousPage() {
        if (selectedMangaCurrentPageNumTEMP > 0) {
            System.out.println("going to previous page lolz");
            selectedMangaCurrentPageNumTEMP--;
//            MangaValues.changeCurrentPageNumber(currentPageNum - 1, Values.DB_NAME_MANGA.getValue());
        } else {
            previousChapter();
        }
    }

    private static void nextChapter() {
        if (selectedMangaLastChapReadNumTEMP < selectedMangaTotalChapNumTEMP) {
            MangaValues.changeLastChapterRead(selectedMangaLastChapReadNumTEMP + 1, Values.DB_NAME_MANGA.getValue());
            MangaValues.changeCurrentPageNumber(0, Values.DB_NAME_MANGA.getValue());
            ControllerMain.mangaImageFilesToList();
        }
        if (selectedMangaLastChapReadNumTEMP == selectedMangaTotalChapNumTEMP) {
            changeMangaStatus();
        }
    }

    private static void changeMangaStatus() {
        MangaValues.changeStatus(true, Values.DB_NAME_MANGA.getValue());
        MangaValues.clearBookmark();
        //method to close reader
    }

    private static void previousChapter() {
        if (selectedMangaLastChapReadNumTEMP > 0) {
            MangaValues.changeLastChapterRead(selectedMangaLastChapReadNumTEMP - 1, Values.DB_NAME_MANGA.getValue());
            ControllerMain.mangaImageFilesToList();
            MangaValues.changeCurrentPageNumber(selectedMangaCurrentChapLastPageNumTEMP, Values.DB_NAME_MANGA.getValue());
            //method to load previous chapter into reader
        }
    }
}
