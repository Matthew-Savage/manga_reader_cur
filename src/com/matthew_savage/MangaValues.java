package com.matthew_savage;

import javax.print.DocFlavor;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.matthew_savage.CategoryMangaLists.*;

public class MangaValues {

    // need to add some kind of method to autofill tablename.

    public static void changeStatus(boolean isComplete, String tableName, String dbFileName) {
        if (isComplete) {
            currentParentList.get(parentListIndexNumberTEMP).setStatus(Values.DB_COL_STATUS_VAL_DONE.getValue());
            currentContent.get(currentContentListIndexNumberTEMP).setStatus(Values.DB_COL_STATUS_VAL_DONE.getValue());
        } else {
            currentParentList.get(parentListIndexNumberTEMP).setStatus(Values.DB_COL_STATUS_VAL_NOT_DONE.getValue());
            currentContent.get(currentContentListIndexNumberTEMP).setStatus(Values.DB_COL_STATUS_VAL_NOT_DONE.getValue());
        }
        Database.accessDb(dbFileName);
        Database.modifyValue(tableName, selectedMangaIdentNumberTEMP, Values.DB_COL_STATUS.getValue(), currentParentList.get(parentListIndexNumberTEMP).getStatus());
        //move manga to or from the correct db. clearly need to call close db sepertely just for the situation above.
    }

    public static void changeWebAddress(String webAddress, String tableName, String dbFileName) {
        currentParentList.get(parentListIndexNumberTEMP).setWebAddress(webAddress);
        currentContent.get(currentContentListIndexNumberTEMP).setWebAddress(webAddress);
        Database.accessDb(dbFileName);
        Database.modifyValue(tableName, selectedMangaIdentNumberTEMP, Values.DB_COL_URL.getValue(), webAddress);
    }

    public static void changeTotalchapters(int totalChapters, String tableName, String dbFileName) {
        currentParentList.get(parentListIndexNumberTEMP).setTotalChapters(totalChapters);
        currentContent.get(currentContentListIndexNumberTEMP).setTotalChapters(totalChapters);
        Database.accessDb(dbFileName);
        Database.modifyValue(tableName, selectedMangaIdentNumberTEMP, Values.DB_COL_CHAP_TOT.getValue(), totalChapters);
    }

    public static void changeCurrentPageNumber(int currentPageNumber, String tableName, String dbFileName) {
        selectedMangaCurrentPageNumTEMP = currentPageNumber;
        currentParentList.get(parentListIndexNumberTEMP).setCurrentPage(currentPageNumber);
        currentContent.get(currentContentListIndexNumberTEMP).setCurrentPage(currentPageNumber);
        Database.accessDb(dbFileName);
        Database.modifyValue(tableName, selectedMangaIdentNumberTEMP, Values.DB_COL_CUR_PAGE.getValue(), currentPageNumber);
    }

    public static void changeLastChapterRead(int lastChapterRead, String tableName, String dbFileName) {
        selectedMangaLastChapReadNumTEMP = lastChapterRead;
        currentParentList.get(parentListIndexNumberTEMP).setLastChapterRead(lastChapterRead);
        currentContent.get(currentContentListIndexNumberTEMP).setLastChapterRead(lastChapterRead);
        Database.accessDb(dbFileName);
        Database.modifyValue(tableName, selectedMangaIdentNumberTEMP, Values.DB_COL_LAST_CHAP_READ.getValue(), lastChapterRead);
    }

    public static void changeLastChapterDownloaded(int lastChapterDownloaded, String tableName, String dbFileName) {
        currentParentList.get(parentListIndexNumberTEMP).setLastChapterRead(lastChapterDownloaded);
        currentContent.get(currentContentListIndexNumberTEMP).setLastChapterRead(lastChapterDownloaded);
        Database.accessDb(dbFileName);
        Database.modifyValue(tableName, selectedMangaIdentNumberTEMP, Values.DB_COL_LAST_CHAP_DL.getValue(), lastChapterDownloaded);
    }

    public static void changeNewChaptersFlag(int newChapters, String tableName, String dbFileName) {
        currentParentList.get(parentListIndexNumberTEMP).setNewChapters(newChapters);
        currentContent.get(currentContentListIndexNumberTEMP).setNewChapters(newChapters);
        Database.accessDb(dbFileName);
        Database.modifyValue(tableName, selectedMangaIdentNumberTEMP, Values.DB_COL_NEW_CHAP_BOOL.getValue(), newChapters);
    }

    public static void changeFavoriteFlag(boolean isFavorited, String tableName, String dbFileName) {
        currentParentList.get(parentListIndexNumberTEMP).setFavorite(isFavorited);
        currentContent.get(currentContentListIndexNumberTEMP).setFavorite(isFavorited);
        Database.accessDb(dbFileName);
        Database.modifyValue(tableName, selectedMangaIdentNumberTEMP, Values.DB_COL_FAVE_BOOL.getValue(), isFavorited);
    }

    public static void setBookmark(String dbFileName) {
        bookmark.clear();
        bookmark.add(0, currentContent.get(currentContentListIndexNumberTEMP));
        Database.accessDb(dbFileName);
        Database.createBookmark(Values.DB_TABLE_BOOKMARK.getValue(),
                selectedMangaIdentNumberTEMP,
                selectedMangaTotalChapNumTEMP,
                selectedMangaLastChapReadNumTEMP,
                selectedMangaCurrentPageNumTEMP);
        Database.terminateDbAccess();

    }

    public static void clearBookmark() {
        bookmark.clear();
        Voucher.acquire();
        Database.accessDb(Values.DB_NAME_MANGA.getValue());

        try {
            PreparedStatement delete = Database.dbConnection.prepareStatement("DELETE FROM " + Values.DB_TABLE_BOOKMARK.getValue());
            delete.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Database.terminateDbAccess();
        Voucher.release();

    }

    public static void addToDownloading(String sourceDatabase) {
        //write total chapters om arraylist only
        //delete from source database
        //write to downloads db
    }

    public static void modifyCurrentlyDownloadingManga() {

    }

    public static void removeFromDownloading() {

    }

    public static void addToHistory(String dbFileName) {
        System.out.println("adding to history");
        history.add(currentContent.get(currentContentListIndexNumberTEMP));
        try {
            Database.accessDb(dbFileName);
            PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(
                    "INSERT INTO history (title_id, title, summary) VALUES (?,?,?)");
            preparedStatement.setInt(1, selectedMangaIdentNumberTEMP);
            preparedStatement.setString(2, selectedMangaTitleTEMP);
            preparedStatement.setString(3, selectedMangaSummaryTEMP);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.terminateDbAccess();
        }
    }

    public static void setFiveLatestMangaTitles(ArrayList<MangaArrayList> arrayList) {

        try {
            Database.accessDb(Values.DB_NAME_MANGA.getValue());
            PreparedStatement delete = Database.dbConnection.prepareStatement("DELETE FROM newest_manga");
            PreparedStatement insert = Database.dbConnection.prepareStatement("INSERT INTO newest_manga (title_id, title) VALUES (?,?)");

            Voucher.acquire();
            delete.execute();
            Voucher.release();
            delete.close();

            for (int i = 0; i < 5; i++) {
                insert.setInt(1, arrayList.get(i).getTitleId());
                insert.setString(2, arrayList.get(i).getTitle());
                insert.addBatch();
            }

            Voucher.acquire();
            insert.executeBatch();
            insert.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.terminateDbAccess();
            Voucher.release();
        }
    }

    public static void insertMangaIntoDatabase(String tableName, String databaseName) {
        tableNameToArrayList(tableName).add(currentContent.get(currentContentListIndexNumberTEMP));
        try {
            Voucher.acquire();
            Database.accessDb(databaseName);
            Database.addMangaEntry(tableName,
                    selectedMangaIdentNumberTEMP,
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.terminateDbAccess();
            Voucher.release();
        }
    }

    public static void deleteMangaFromDatabase(String tableName, String databaseName) {
        tableNameToArrayList(tableName).remove(parentListIndexNumberTEMP);
        currentContent.remove(currentContentListIndexNumberTEMP);

        try {
            Database.accessDb(databaseName);
            Database.removeManga(tableName, selectedMangaIdentNumberTEMP);
            Database.terminateDbAccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewTitles(ArrayList<MangaArrayList> arrayList) {
        try {
            Database.accessDb(Values.DB_NAME_MANGA.getValue());
            PreparedStatement addTitle = Database.dbConnection.prepareStatement("" +
                    "INSERT INTO available_manga (title_id, " +
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
                    "favorite) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");

            for (MangaArrayList manga : arrayList) {
                addTitle.setInt(1, manga.getTitleId());
                addTitle.setString(2, manga.getTitle());
                addTitle.setString(3, manga.getAuthors());
                addTitle.setString(4, manga.getStatus());
                addTitle.setString(5, manga.getSummary());
                addTitle.setString(6, manga.getWebAddress());
                addTitle.setString(7, manga.getGenreTags());
                addTitle.setInt(8, 0);
                addTitle.setInt(9, 0);
                addTitle.setInt(10, 0);
                addTitle.setInt(11, 0);
                addTitle.setInt(12, 0);
                addTitle.setInt(13, 0);
                addTitle.addBatch();
            }

            Voucher.acquire();
            addTitle.executeBatch();
            addTitle.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.terminateDbAccess();
            Voucher.release();
        }
    }

    private static ArrayList<MangaArrayList> tableNameToArrayList(String tableName) {

        if (tableName.equals(Values.DB_TABLE_AVAILABLE.getValue())) {
            return notCollectedMangaList;
        } else if (tableName.equals(Values.DB_TABLE_READING.getValue())) {
            return collectedMangaList;
        } else if (tableName.equals(Values.DB_TABLE_BOOKMARK.getValue())) {
            return bookmark;
        } else if (tableName.equals(Values.DB_TABLE_COMPLETED.getValue())) {
            return completedMangaList;
        } else if (tableName.equals(Values.DB_TABLE_HISTORY.getValue())) {
            return history;
        } else if (tableName.equals(Values.DB_TABLE_FIVE_NEWEST.getValue())) {
            return fiveNewestTitles;
        } else {
            return rejectedMangaList;
        }
    }
}
