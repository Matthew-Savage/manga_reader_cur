package com.matthew_savage.GUI;

import com.matthew_savage.ControllerMain;
import com.matthew_savage.MangaValues;
import com.matthew_savage.StaticStrings;
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
        if (selectedMangaCurrentPageNumTEMP < selectedMangaCurrentChapLastPageNumTEMP) {
            selectedMangaCurrentPageNumTEMP++;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentPageNumTEMP, selectedMangaIdentNumberTEMP);
        } else {
            nextChapter();
        }
    }

    private static void gotoPreviousPage() {
        if (selectedMangaCurrentPageNumTEMP > 0) {
            selectedMangaCurrentPageNumTEMP--;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentPageNumTEMP, selectedMangaIdentNumberTEMP);
        } else {
            previousChapter();
        }
    }

    private static void nextChapter() {
        selectedMangaLastChapReadNumTEMP++;
        modifyNewChapters();
        if (selectedMangaLastChapReadNumTEMP < selectedMangaTotalChapNumTEMP) {
            processNextChapter();
            ControllerMain.mangaImageFilesToList();
        }
        if (selectedMangaLastChapReadNumTEMP == selectedMangaTotalChapNumTEMP) {
            processNextChapter();
            changeMangaStatus();
        }
    }

    private static void modifyNewChapters() {
        if (selectedMangaNewChapNumTEMP > 0) {
            selectedMangaNewChapNumTEMP = selectedMangaNewChapNumTEMP - 1;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_NEW_CHAP_BOOL.getValue(), selectedMangaNewChapNumTEMP, selectedMangaIdentNumberTEMP);
        }
    }

    private static void processNextChapter() {
        selectedMangaCurrentPageNumTEMP = 0;
        MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_LAST_CHAP_READ.getValue(), selectedMangaLastChapReadNumTEMP, selectedMangaIdentNumberTEMP);
        MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentPageNumTEMP, selectedMangaIdentNumberTEMP);
    }

    private static void changeMangaStatus() {
//        MangaValues.addAndRemove(collectedMangaList, completedMangaList, parentListIndexNumberTEMP, true);





        MangaValues.addToQueue("INSERT INTO " + StaticStrings.DB_TABLE_COMPLETED.getValue() + " (" +
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

        MangaValues.addToQueue("DELETE FROM " + StaticStrings.DB_TABLE_READING.getValue() + " WHERE title_id = '" + selectedMangaIdentNumberTEMP + "'");
        completedMangaList.add(collectedMangaList.get(parentListIndexNumberTEMP));
        collectedMangaList.remove(parentListIndexNumberTEMP);








//        MangaValues.justRemove(history, ControllerMain.fetchOriginalIndexNumber(history, selectedMangaIdentNumberTEMP));
//        MangaValues.deleteAll(bookmark); // this is the problem right here. this just doesnt work for some reason.
        MangaValues.addToQueue("DELETE FROM bookmark");
        bookmark.clear();
        MangaValues.executeChanges();
    }

    private static void previousChapter() {
        if (selectedMangaLastChapReadNumTEMP > 0) {
            selectedMangaLastChapReadNumTEMP--;
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_LAST_CHAP_READ.getValue(), selectedMangaLastChapReadNumTEMP, selectedMangaIdentNumberTEMP);
            ControllerMain.mangaImageFilesToList();
            MangaValues.modifyValue(collectedMangaList, StaticStrings.DB_COL_CUR_PAGE.getValue(), selectedMangaCurrentChapLastPageNumTEMP, selectedMangaIdentNumberTEMP);
            selectedMangaCurrentPageNumTEMP = selectedMangaCurrentChapLastPageNumTEMP;
            //method to load previous chapter into reader
        }
    }
}
