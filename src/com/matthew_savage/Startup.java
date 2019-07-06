package com.matthew_savage;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Startup {

    private static Database database = new Database();

    public static ArrayList<MangaArrayList> buildArray(String fileName, String tableName) {
        return resultSetToArrayList(fileName, tableName);
    }

    public static void implementDatabaseChanges() {
        File userFile = new File(Values.DIR_ROOT.getValue() + File.separator + Values.DIR_DB.getValue() + File.separator + "usr.db");

        if (!userFile.exists()) {
            System.out.println("user file doesn't exist bro! making it.. bro!");
            database.openDb("usr.db");
            database.createUserTable();
            database.storeVersionEntry("version_number", 2);
            database.storeStatEntries("stats", 0, 0, 0, 0,0, 0, 0, "", "", "");
            database.closeDb();
            changesNeeded();
        } else {
            System.out.println("no changes needed!");
        }
    }

    private static void changesNeeded() {
        database.openDb("main.db");
        database.deleteTable("download_pending");
        database.createHistoryTable();
        database.createBookmarkTable();
        fetchCurrentBookmark();
        database.deleteTable("resume_last_manga");
        database.createIndex(Values.DB_TABLE_COMPLETED.getValue());
        database.createIndex(Values.DB_TABLE_FIVE_NEWEST.getValue());
        database.createIndex(Values.DB_TABLE_READING.getValue());
        database.createIndex(Values.DB_TABLE_NOT_INTERESTED.getValue());
        database.closeDb();
    }

    private static void fetchCurrentBookmark() {
        ResultSet resultSet = database.fetchTableData("resume_last_manga");

        try {
                while (resultSet.next()) {
                    fromOldToNew(resultSet.getInt("title_id"), resultSet.getInt("total_chapters"), resultSet.getInt("last_chapter_read"), resultSet.getInt("current_page"));
                }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fromOldToNew(int titleId, int totalChapters, int lastChapterRead, int currentPage) {
        database.addNewManga("bookmark",
                titleId,
                "null",
                "null",
                "null",
                "null",
                "null",
                "null",
                totalChapters,
                currentPage,
                lastChapterRead,
                0,
                0,
                0);
    }

    private static ArrayList<MangaArrayList> resultSetToArrayList(String fileName, String tableName) {
        ArrayList<MangaArrayList> list = new ArrayList<>();
        ResultSet resultSet = fetchResultSet(fileName, tableName);
        if (tableName.equals(Values.DB_TABLE_FIVE_NEWEST.getValue())) {
            try {
                while (resultSet.next()) {
                    list.add(new MangaArrayList(
                            resultSet.getInt("title_id"),
                            resultSet.getString("title"),
                            null,
                            null,
                            null,
                            null,
                            null,
                            0,
                            0,
                            0,
                            0,
                            0,
                            false));
                }
                resultSet.close();
                database.closeDb();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                while (resultSet.next()) {
                    list.add(new MangaArrayList(
                            resultSet.getInt("title_id"),
                            resultSet.getString("title"),
                            resultSet.getString("authors"),
                            resultSet.getString("status"),
                            resultSet.getString("summary"),
                            resultSet.getString("web_address"),
                            resultSet.getString("genre_tags"),
                            resultSet.getInt("total_chapters"),
                            resultSet.getInt("current_page"),
                            resultSet.getInt("last_chapter_read"),
                            resultSet.getInt("last_chapter_downloaded"),
                            resultSet.getInt("new_chapters"),
                            resultSet.getBoolean("favorite")));
                }
                resultSet.close();
                database.closeDb();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private static ResultSet fetchResultSet(String fileName, String tableName) {
        database.openDb(fileName);
        return database.fetchTableData(tableName);
    }
}
