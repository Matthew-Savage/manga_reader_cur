package com.matthew_savage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.matthew_savage.CategoryMangaLists.*;

public class MangaValues {

//    private static ArrayList<String> queue = new ArrayList<>();
    private static ArrayBlockingQueue<String> concurrentQueue = new ArrayBlockingQueue<>(10, true);

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void executeChanges() {
        executor.execute(processDatabaseActions);
    }

    private static Runnable processDatabaseActions = () -> {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" +
                    StaticStrings.DIR_ROOT.getValue() +
                    File.separator + StaticStrings.DIR_DB.getValue() +
                    File.separator + StaticStrings.DB_NAME_MANGA.getValue());
            Statement statement = connection.createStatement();
            for (String process : concurrentQueue) {
                Logging.logDatabase(process);
                statement.execute(process);
                concurrentQueue.poll();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    };

    public static void addToQueue(String process) {
//        queue.add(process);

        try {
            concurrentQueue.put(process);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void addAndRemove(ArrayList<Manga> source, ArrayList<Manga> dest, int sourceIndexNum, boolean sourceRemove) {
        String process = assembleInsertStatement(dest, source, sourceIndexNum);
        dest.add(source.get(sourceIndexNum));
        concurrentQueue.add(process);

        if (sourceRemove) {
            concurrentQueue.add(assembleRemoveStatement(source, sourceIndexNum));
            source.remove(sourceIndexNum);
        }
    }

    public static void justRemove(ArrayList<Manga> source, int sourceIndexNum) {
        concurrentQueue.add(assembleRemoveStatement(source, sourceIndexNum));
        source.remove(sourceIndexNum);
    }

    public static void deleteAll(ArrayList<Manga> source) {
        concurrentQueue.add("DELETE FROM " + arrayListToTableName(source));
        source.clear();
    }

    public static <T> void modifyValue(ArrayList<Manga> currentParent, String columnName, T newValue, int mangaIdentNum) {
        updateListValues(currentParent);
        concurrentQueue.add(assembleModifyStatement(currentParent, columnName, newValue, mangaIdentNum));
    }

    private static void updateListValues(ArrayList<Manga> currentParent) {
        currentParent.get(parentListIndexNumberTEMP).setTitle(selectedMangaTitleTEMP);
        currentParent.get(parentListIndexNumberTEMP).setStatus(selectedMangaStatusTEMP);
        currentParent.get(parentListIndexNumberTEMP).setWebAddress(selectedMangaWebAddressTEMP);
        currentParent.get(parentListIndexNumberTEMP).setTotalChapters(selectedMangaTotalChapNumTEMP);
        currentParent.get(parentListIndexNumberTEMP).setCurrentPage(selectedMangaCurrentPageNumTEMP);
        currentParent.get(parentListIndexNumberTEMP).setLastChapterRead(selectedMangaLastChapReadNumTEMP);
        currentParent.get(parentListIndexNumberTEMP).setNewChapters(selectedMangaNewChapNumTEMP);
        currentParent.get(parentListIndexNumberTEMP).setFavorite(selectedMangaIsFavoriteTEMP);
    }

    private static <T> String assembleModifyStatement(ArrayList<Manga> currentParent, String columnName, T newValue, int mangaIdentNum) {
        return "UPDATE " + arrayListToTableName(currentParent) + " SET " + columnName + " = '" +
                newValue + "' WHERE title_id = '" + mangaIdentNum + "'";
    }

    private static String assembleInsertStatement(ArrayList<Manga> dest, ArrayList<Manga> source, int sourceIndexNum) {
            return "INSERT INTO " + arrayListToTableName(dest) + " (" +
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
                    "'" + source.get(sourceIndexNum).getTitleId() + "', " +
                    "'" + source.get(sourceIndexNum).getTitle() + "', " +
                    "'" + source.get(sourceIndexNum).getAuthors() + "', " +
                    "'" + source.get(sourceIndexNum).getStatus() + "', " +
                    "'" + source.get(sourceIndexNum).getSummary() + "', " +
                    "'" + source.get(sourceIndexNum).getWebAddress() + "', " +
                    "'" + source.get(sourceIndexNum).getGenreTags() + "', " +
                    "'" + source.get(sourceIndexNum).getTotalChapters() + "', " +
                    "'" + source.get(sourceIndexNum).getCurrentPage() + "', " +
                    "'" + source.get(sourceIndexNum).getLastChapterRead() + "', " +
                    "'" + source.get(sourceIndexNum).getLastChapterDownloaded() + "', " +
                    "'" + source.get(sourceIndexNum).getNewChapters() + "', " +
                    "'" + source.get(sourceIndexNum).getFavorite() + "')";

    }

    private static String assembleRemoveStatement(ArrayList<Manga> arrayList, int sourceIndexNum) {
        return "DELETE FROM " + arrayListToTableName(arrayList) + " WHERE title_id = '" + arrayList.get(sourceIndexNum).getTitleId() + "'";
    }

    private static String arrayListToTableName(ArrayList<Manga> arrayList) {
        if (arrayList.equals(notCollectedMangaList)) {
            return StaticStrings.DB_TABLE_AVAILABLE.getValue();
        } else if (arrayList.equals(collectedMangaList)) {
            return StaticStrings.DB_TABLE_READING.getValue();
        } else if (arrayList.equals(completedMangaList)) {
            return StaticStrings.DB_TABLE_COMPLETED.getValue();
        } else if (arrayList.equals(history)) {
            return StaticStrings.DB_TABLE_HISTORY.getValue();
        } else if (arrayList.equals(undecidedMangaList)) {
            return StaticStrings.DB_TABLE_UNDECIDED.getValue();
        } else if (arrayList.equals(bookmark)) {
            return StaticStrings.DB_TABLE_BOOKMARK.getValue();
        } else if (arrayList.equals(downloading)) {
            return StaticStrings.DB_TABLE_DOWNLOAD.getValue();
        } else {
            return StaticStrings.DB_TABLE_NOT_INTERESTED.getValue();
        }
    }
}
