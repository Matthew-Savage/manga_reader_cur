package com.matthew_savage.GUI;

import com.matthew_savage.ControllerMain;
import com.matthew_savage.MangaValues;
import com.matthew_savage.StaticStrings;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static com.matthew_savage.CategoryMangaLists.*;

public class MangaPageTurning {

    private static long firstTime;
    private static long secondTime;

    public static void turnPagePreviousNext(KeyEvent event) {
        secondTime = System.nanoTime();
        if (firstTime == 0 || secondTime - firstTime > 500) {
//            firstTime = System.nanoTime();
//            ifReady(event);
        } else {
            event.consume();
        }
        System.out.println((secondTime - firstTime) / 1000000);
    }

    private static void ifReady(KeyEvent event) {
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
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentPageNumTEMP, selectedMangaIdentNumberTEMP);
            MangaValues.executeChanges();
        } else {
            nextChapter();
        }
    }

    private static void gotoPreviousPage() {
        if (selectedMangaCurrentPageNumTEMP > 0) {
            System.out.println("going to previous page lolz");
            selectedMangaCurrentPageNumTEMP--;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentPageNumTEMP, selectedMangaIdentNumberTEMP);
            MangaValues.executeChanges();
        } else {
            previousChapter();
        }
    }

    private static void nextChapter() {
        if (selectedMangaLastChapReadNumTEMP < selectedMangaTotalChapNumTEMP) {
            selectedMangaLastChapReadNumTEMP++;
            selectedMangaCurrentPageNumTEMP = 0;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_LAST_CHAP_READ.getValue(), selectedMangaLastChapReadNumTEMP, selectedMangaIdentNumberTEMP);
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentPageNumTEMP, selectedMangaIdentNumberTEMP);
            MangaValues.executeChanges();
            ControllerMain.mangaImageFilesToList();
        }
        if (selectedMangaLastChapReadNumTEMP == selectedMangaTotalChapNumTEMP) {
            changeMangaStatus();
        }
    }

    private static void changeMangaStatus() {
        MangaValues.addAndRemove(collectedMangaList, completedMangaList, parentListIndexNumberTEMP, true);
        MangaValues.deleteAll(bookmark);
        MangaValues.executeChanges();
    }

    private static void previousChapter() {
        if (selectedMangaLastChapReadNumTEMP > 0) {
            selectedMangaLastChapReadNumTEMP--;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_LAST_CHAP_READ.getValue(), selectedMangaLastChapReadNumTEMP , selectedMangaIdentNumberTEMP);
            ControllerMain.mangaImageFilesToList();
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentChapLastPageNumTEMP, selectedMangaIdentNumberTEMP);
            selectedMangaCurrentPageNumTEMP = selectedMangaCurrentChapLastPageNumTEMP;
            MangaValues.executeChanges();
            //method to load previous chapter into reader
        }
    }

    private static void delay() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
