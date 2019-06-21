package com.antagonisticapple;

import static com.antagonisticapple.CategoryMangaLists.*;

public class MangaValues {

    public static void changeStatus(boolean isComplete, String dbFileName) {
        if (isComplete) {
            currentParentList.get(parentListIndexNumber).setStatus(Values.DB_COL_STATUS_VAL_DONE.getValue());
            currentContent.get(currentContentListIndexNumber).setStatus(Values.DB_COL_STATUS_VAL_DONE.getValue());
        } else {
            currentParentList.get(parentListIndexNumber).setStatus(Values.DB_COL_STATUS_VAL_NOT_DONE.getValue());
            currentContent.get(currentContentListIndexNumber).setStatus(Values.DB_COL_STATUS_VAL_NOT_DONE.getValue());
        }
        Database.accessDb(dbFileName);
        Database.modifyManga(selectedMangaIdentNumber, Values.DB_COL_STATUS.getValue(), currentParentList.get(parentListIndexNumber).getStatus());
        //move manga to or from the correct db. clearly need to call close db sepertely just for the situation above.
    }

    public static void changeWebAddress(String webAddress, String dbFileName) {
        currentParentList.get(parentListIndexNumber).setWebAddress(webAddress);
        currentContent.get(currentContentListIndexNumber).setWebAddress(webAddress);
        Database.accessDb(dbFileName);
        Database.modifyManga(selectedMangaIdentNumber, Values.DB_COL_URL.getValue(), webAddress);
    }

    public static void changeTotalchapters(int totalChapters, String dbFileName) {
        currentParentList.get(parentListIndexNumber).setTotalChapters(totalChapters);
        currentContent.get(currentContentListIndexNumber).setTotalChapters(totalChapters);
        Database.accessDb(dbFileName);
        Database.modifyManga(selectedMangaIdentNumber, Values.DB_COL_CHAP_TOT.getValue(), totalChapters);
    }

    public static void changeCurrentPageNumber(int currentPageNumber, String dbFileName) {
        currentParentList.get(parentListIndexNumber).setCurrentPage(currentPageNumber);
        currentContent.get(currentContentListIndexNumber).setCurrentPage(currentPageNumber);
        Database.accessDb(dbFileName);
        Database.modifyManga(selectedMangaIdentNumber, Values.DB_COL_CUR_PAGE.getValue(), currentPageNumber);
    }

    public static void changeLastChapterRead(int lastChapterRead, String dbFileName) {
        currentParentList.get(parentListIndexNumber).setLastChapterRead(lastChapterRead);
        currentContent.get(currentContentListIndexNumber).setLastChapterRead(lastChapterRead);
        Database.accessDb(dbFileName);
        Database.modifyManga(selectedMangaIdentNumber, Values.DB_COL_LAST_CHAP_READ.getValue(), lastChapterRead);
    }

    public static void changeLastChapterDownloaded(int lastChapterDownloaded, String dbFileName) {
        currentParentList.get(parentListIndexNumber).setLastChapterRead(lastChapterDownloaded);
        currentContent.get(currentContentListIndexNumber).setLastChapterRead(lastChapterDownloaded);
        Database.accessDb(dbFileName);
        Database.modifyManga(selectedMangaIdentNumber, Values.DB_COL_LAST_CHAP_DL.getValue(), lastChapterDownloaded);
    }

    public static void changeNewChaptersFlag(boolean hasNewChapters, String dbFileName) {
        currentParentList.get(parentListIndexNumber).setNewChapters(hasNewChapters);
        currentContent.get(currentContentListIndexNumber).setNewChapters(hasNewChapters);
        Database.accessDb(dbFileName);
        Database.modifyManga(selectedMangaIdentNumber, Values.DB_COL_NEW_CHAP_BOOL.getValue(), hasNewChapters);
    }

    public static void changeFavoriteFlag(boolean isFavorited, String dbFileName) {
        currentParentList.get(parentListIndexNumber).setFavorite(isFavorited);
        currentContent.get(currentContentListIndexNumber).setFavorite(isFavorited);
        Database.accessDb(dbFileName);
        Database.modifyManga(selectedMangaIdentNumber, Values.DB_COL_FAVE_BOOL.getValue(), isFavorited);
    }

}
