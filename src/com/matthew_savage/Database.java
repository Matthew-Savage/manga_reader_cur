package com.matthew_savage;

import java.io.File;
import java.sql.*;
import java.sql.PreparedStatement;

public class Database {

    private Connection dbConnect;
    public static Connection dbConnection;

    public Connection openDb(String databaseFilename) {
        try {
            return dbConnect = DriverManager.getConnection("jdbc:sqlite:" + Values.DIR_ROOT.getValue() + File.separator + Values.DIR_DB.getValue() + File.separator + databaseFilename);
        } catch (SQLException e) {
            System.out.println("Opening DB connect failed");
        }
        return null;
    }
    //-----------------------------------------------------
    //all this shit needs to run using its own thread, probably make a global executor service
    public static void accessDb(String dbFileName) {
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + Values.DIR_ROOT.getValue() + File.separator + Values.DIR_DB.getValue() + File.separator + dbFileName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addManga(ResultSet resultSet) {

    }

    public static void testStatement(PreparedStatement preparedStatement) {
//        PreparedStatement
    }

    public static void removeManga(String tableName, int mangaIdentNumber) {
        try (Statement sqlStatement = dbConnection.createStatement()) {
            sqlStatement.execute("DELETE FROM " + tableName + " WHERE title_id = '" + mangaIdentNumber + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static <T> void modifyValue (String tableName, int mangaIdentNumber, String valueColumnName, T newValue) {
        try (Statement sqlStatement = dbConnection.createStatement()) {
            sqlStatement.execute("UPDATE " + tableName + " SET " + valueColumnName + " = '" + newValue + "' WHERE title_id = '" + mangaIdentNumber + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMangaEntry(String tableName, int mangaId, String title, String authors, String status, String summary, String webAddress, String genreTags, int totalChapters, int currentPage, int lastChapterRead, int lastChapterDownloaded, int newChaptersBoolean, boolean favoriteBoolean) {

        try (Statement sqlStatement = dbConnection.createStatement()) {
            sqlStatement.execute("INSERT INTO " + tableName + " (title_id, title, authors, status, summary, web_address, genre_tags, total_chapters, current_page, last_chapter_read, last_chapter_downloaded, new_chapters, favorite) VALUES " +
                    "('" + mangaId + "', '" + title + "', '" + authors + "', '" + status + "', '" + summary + "', '" + webAddress + "', '" + genreTags + "', '" + totalChapters + "', '" + currentPage + "', '" + lastChapterRead + "', '" + lastChapterDownloaded + "', '" + newChaptersBoolean + "', '" + favoriteBoolean + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void terminateDbAccess() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------

    public void addNewManga(String tableName, int mangaId, String title, String authors, String status, String summary, String webAddress, String genreTags, int totalChapters, int currentPage, int lastChapterRead, int lastChapterDownloaded, int newChaptersBoolean, int favoriteBoolean) {

        try (Statement sqlStatement = dbConnect.createStatement()) {
            sqlStatement.execute("INSERT INTO " + tableName + " (title_id, title, authors, status, summary, web_address, genre_tags, total_chapters, current_page, last_chapter_read, last_chapter_downloaded, new_chapters, favorite) VALUES " +
                    "('" + mangaId + "', '" + title + "', '" + authors + "', '" + status + "', '" + summary + "', '" + webAddress + "', '" + genreTags + "', '" + totalChapters + "', '" + currentPage + "', '" + lastChapterRead + "', '" + lastChapterDownloaded + "', '" + newChaptersBoolean + "', '" + favoriteBoolean + "')");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    <T> void modifyManga(String tableName, int mangaId, String columnToModify, T newColumnValue) {
        try (Statement sqlStatement = dbConnect.createStatement()) {
            sqlStatement.execute("UPDATE " + tableName + " SET " + columnToModify + " = '" + newColumnValue + "' WHERE title_id = '" + mangaId + "'");
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "   db message");
        }

    }

    void downloadQueueAdd(String tableName, int titleId, String webAddress, int startingChapter, int lastChapterDownloaded) {
        try (Statement sqlStatement = dbConnect.createStatement()) {
            sqlStatement.execute("INSERT INTO " + tableName + " (title_id, web_address, starting_chapter, last_chapter_downloaded) VALUES ('" + titleId + "', '" + webAddress + "', '" + startingChapter + "', '" + lastChapterDownloaded + "')");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet downloadQueueFetch(String tableName) {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            return sqlStatement.executeQuery("SELECT * FROM " + tableName + " ORDER BY ROWID ASC LIMIT 1");
        } catch (Exception e) {

        }
        return null;
    }

    void downloadQueueRemove(String tableName, int mangaId) {
        try (Statement sqlStatement = dbConnect.createStatement()) {
            sqlStatement.execute("DELETE FROM " + tableName + " WHERE title_id = '" + mangaId + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void downloadDbAttach() {
        try (Statement sqlStatement = dbConnect.createStatement()) {
            sqlStatement.execute("ATTACH DATABASE '" + Values.DIR_ROOT.getValue() + File.separator + Values.DIR_DB.getValue() + File.separator + Values.DB_NAME_DOWNLOADING.getValue() + "' AS downloadDb");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void downloadDbDetach() {
        try (Statement sqlStatement = dbConnect.createStatement()) {
            sqlStatement.execute("DETACH DATABASE 'downloadDb'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteManga(String sourceTable, int mangaID) {
        try (Statement sqlStatement = dbConnect.createStatement()) {
            sqlStatement.execute("DELETE FROM " + sourceTable + " WHERE title_id = '" + mangaID + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void moveManga(String sourceTable, String destinationTable, int mangaID) {
        try (Statement sqlStatement = dbConnect.createStatement()) {
            sqlStatement.execute("INSERT INTO " + destinationTable + " SELECT * FROM " + sourceTable + " WHERE title_id = '" + mangaID + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet workingGenreFilter(String column, String table, String tagQuery, String sortByValue, String sortOrder) {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            System.out.println("SELECT " + column + " FROM " + table + tagQuery + sortByValue + sortOrder);
            return sqlStatement.executeQuery("SELECT " + column + " FROM " + table + tagQuery + sortByValue + sortOrder);

        } catch (Exception e) {

        }
        return null;
    }

    public ResultSet filterManga(String tableName, String column, String databaseSearchString) {
//needs a lot of work to make it valid for the 19 different fetches we need

        try {
            Statement sqlStatement = dbConnect.createStatement();
            return sqlStatement.executeQuery("SELECT * FROM " + tableName + " WHERE " + column + " = '" + databaseSearchString + "'");
        } catch (Exception e) {

        }
        return null;
    }

    public void updateNewestManga(String tableName, int mangaId, String title) {
        try {
            Statement sqlStatement = dbConnect.createStatement();
//            sqlStatement.execute("DELETE FROM " + tableName + " WHERE rowid=1"); //replacing this with a deleteall instead of only rowid1
            sqlStatement.execute("DELETE FROM " + tableName);
            sqlStatement.execute("INSERT OR REPLACE INTO " + tableName + " (title_id, title) VALUES ('" + mangaId + "', '" + title + "')");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void createBookmark(String tableName, int mangaID, int totalChapters, int lastChapterRead, int currentPage) {
        System.out.println("create bookmark running!  - " + mangaID + " - " + lastChapterRead + " - " + currentPage);
        try {
            Statement sqlStatement = dbConnection.createStatement();
            sqlStatement.execute("DELETE FROM " + tableName + " WHERE rowid=1");
            sqlStatement.execute("INSERT OR REPLACE INTO " + tableName + " (title_id, total_chapters, last_chapter_read, current_page) VALUES ('" + mangaID + "', '" + totalChapters + "', '" + lastChapterRead + "', '" + currentPage + "')");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet fetchTableData(String tableName) {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            return sqlStatement.executeQuery("SELECT * FROM " + tableName);
        } catch (Exception e) {

        }
        return null;
    }

    public void saveSettings(String tableName, String settingName, String settingValue) {

        try {
            Statement sqlStatement = dbConnect.createStatement();
            sqlStatement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (settingName TEXT, settingValue TEXT, CONSTRAINT singular UNIQUE (settingName))");
            sqlStatement.execute("INSERT OR REPLACE INTO " + tableName + " (settingName, settingValue) VALUES ('" + settingName + "', '" + settingValue + "')");
        } catch (Exception e) {
        }
    }

    void deleteTable(String tableName) {
        try {
            Statement sqlStatemenet = dbConnect.createStatement();
            sqlStatemenet.execute("DROP TABLE IF EXISTS " + tableName );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createHistoryTable() {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            sqlStatement.execute("CREATE TABLE IF NOT EXISTS history (entry_number INTEGER PRIMARY KEY, title_id INTEGER, title TEXT, summary TEXT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createBookmarkTable() {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            sqlStatement.execute("CREATE TABLE IF NOT EXISTS bookmark (title_id INTEGER, title TEXT, authors TEXT, status TEXT, summary TEXT, web_address TEXT, genre_tags TEXT, total_chapters INTEGER, current_page INTEGER, last_chapter_read INTEGER, last_chapter_downloaded INTEGER, new_chapters INTEGER, favorite INTEGER)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createUserTable() {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            sqlStatement.execute("CREATE TABLE IF NOT EXISTS version_number (version INTEGER)");
            sqlStatement.execute("CREATE TABLE IF NOT EXISTS stats" +
                    " (titles_total INTEGER," +
                    " reading_total INTEGER," +
                    " finished_reading INTEGER," +
                    " pages_read INTEGER," +
                    " favorites INTEGER," +
                    " blacklisted INTEGER," +
                    " pages_daily INTEGER," +
                    " first_genre TEXT," +
                    " second_genre TEXT," +
                    " third_genre TEXT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createIndex(String tableName) {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            sqlStatement.execute("CREATE UNIQUE INDEX IF NOT EXISTS `" + tableName + "_i` ON `" + tableName + "` ( `title_id` )");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void storeVersionEntry(String tableName, int value) {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            sqlStatement.execute("INSERT INTO " + tableName + " (version) VALUES ('" + value + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void storeStatEntries(String tableName, int titlesTotal, int readingTotal, int finishedReading, int pagesRead, int favorites, int blacklisted, int pagesDaily, String firstGenre, String secondGenre, String thirdGenre) {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            sqlStatement.execute("INSERT INTO " + tableName + " (titles_total, reading_total, finished_reading, pages_read, favorites, blacklisted, pages_daily, first_genre, second_genre, third_genre) " +
                    "VALUES ('" + titlesTotal + "', '" + readingTotal + "', '" + finishedReading + "', '" + pagesRead + "', '" + favorites + "', '" + blacklisted + "', '" + pagesDaily + "', '" + firstGenre + "', '" + secondGenre + "', '" + thirdGenre + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void storeHistoryEntries(int mangaId, String title, String summary) {
        try {
            Statement sqlStatement = dbConnect.createStatement();
            sqlStatement.execute("INSERT INTO history (title_id, title, summary) VALUES ('" + mangaId + "', '" + title + "', '" + summary + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet retrieveHistoryEntries() {
        try {
            Statement sqlStatement = dbConnection.createStatement();
            return sqlStatement.executeQuery("SELECT * FROM history ORDER BY entry_number DESC LIMIT 10");
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }

    public String retrieveSettings(String tableName, String settingName) {
        ResultSet settingValue;
        String settingValueFinal = null;

        try {
            Statement sqlStatement = dbConnect.createStatement();
            settingValue = sqlStatement.executeQuery("SELECT settingValue FROM " + tableName + " WHERE settingName = '" + settingName + "'");
            settingValueFinal = settingValue.getString("SettingValue");

        } catch (Exception e) {
        }
        return settingValueFinal;
    }


    public void closeDb() {
        try {
            if (dbConnect != null) {
                dbConnect.close();
            }
        } catch (SQLException e) {
            System.out.println("Closing DB connect failed");
        }
    }
}