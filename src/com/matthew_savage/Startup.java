package com.matthew_savage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Startup {

//    private static Database database = new Database();
    private static Statement statement;
    private static ResultSet resultSet;

    public static ArrayList<Manga> buildArray(String tableName) {
        return resultSetToArrayList(tableName);
    }

    public static void implementDatabaseChanges() {
        try {
            Database.accessDb(StaticStrings.DB_NAME_SETTINGS.getValue());
            statement = Database.dbConnection.createStatement();
            resultSet = statement.executeQuery("SELECT " + StaticStrings.DB_VER_COL_VER.getValue() + " FROM " + StaticStrings.DB_TABLE_VERSION.getValue());
            CategoryMangaLists.currentVersionNumber = resultSet.getDouble("version");
            resultSet.close();
            Database.terminateDbAccess();
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }

        if (CategoryMangaLists.currentVersionNumber < Double.parseDouble(StaticStrings.VER_NUM_CURRENT.getValue())) {
            changesNeeded();
            modifyVersionNumber();
        }
    }

    private static void changesNeeded() {
        Database.accessDb(StaticStrings.DB_NAME_MANGA.getValue());
        try {
            statement = Database.dbConnection.createStatement();
            statement.execute("DROP TABLE IF EXISTS newest_manga");
            statement.execute("CREATE TABLE IF NOT EXISTS undecided (entry_number INTEGER PRIMARY KEY, " +
                    "title_id INTEGER, " +
                    "title TEXT, " +
                    "authors TEXT, " +
                    "status TEXT, " +
                    "summary TEXT, " +
                    "web_address TEXT, " +
                    "genre_tags TEXT, " +
                    "total_chapters INTEGER, " +
                    "current_page INTEGER, " +
                    "last_chapter_read INTEGER, " +
                    "last_chapter_downloaded INTEGER, " +
                    "new_chapters INTEGER, " +
                    "favorite INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.terminateDbAccess();
        }
    }

    private static void modifyVersionNumber() {
        Database.accessDb(StaticStrings.DB_NAME_SETTINGS.getValue());
        CategoryMangaLists.currentVersionNumber = Double.parseDouble(StaticStrings.VER_NUM_CURRENT.getValue());
        try {
            statement = Database.dbConnection.createStatement();
            statement.execute("DELETE FROM " + StaticStrings.DB_TABLE_VERSION.getValue());
            statement.execute("INSERT INTO " + StaticStrings.DB_TABLE_VERSION.getValue() + " VALUES ('" + CategoryMangaLists.currentVersionNumber + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.terminateDbAccess();
        }
    }

    private static ArrayList<Manga> resultSetToArrayList(String tableName) {
        ArrayList<Manga> list = new ArrayList<>();
        resultSet = fetchResultSet(tableName);
            try {
                while (resultSet.next()) {
                    list.add(new Manga(
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
                            resultSet.getInt("favorite")));
                }
                resultSet.close();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                Logging.logError(e.toString());
            }
        return list;
    }

    private static ResultSet fetchResultSet(String tableName) {
        try {
            statement = Database.dbConnection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
        return resultSet;
    }
}
