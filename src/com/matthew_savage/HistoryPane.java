package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;

public class HistoryPane {

    static ArrayList<Manga> retrieveStoredHistory() {
        return readFromDatabase();
    }

    public static boolean storeHistory() {
        return checkIfTitlePresent();
    }

    private static boolean checkIfTitlePresent() {
        if (CategoryMangaLists.history.stream().noneMatch(v -> v.getTitleId() == CategoryMangaLists.selectedMangaIdentNumberTEMP)) {
            MangaValues.addToQueue("INSERT INTO " + StaticStrings.DB_TABLE_HISTORY.getValue() + " (" +
                    "title_id, " +
                    "title, " +
                    "summary) VALUES " + "(" +
                    "'" + CategoryMangaLists.selectedMangaIdentNumberTEMP + "', " +
                    "'" + CategoryMangaLists.selectedMangaTitleTEMP + "', " +
                    "'" + CategoryMangaLists.selectedMangaSummaryTEMP + "')");
            CategoryMangaLists.history.add(0, CategoryMangaLists.collectedMangaList.get(CategoryMangaLists.parentListIndexNumberTEMP));
            return false;
        } else {
            return true;
        }
    }

    private static ArrayList<Manga> resultSetToArray(ResultSet resultSet) throws Exception {
        ArrayList<Manga> historyList = new ArrayList<>();

        while (resultSet.next()) {
            historyList.add(new Manga(resultSet.getInt("title_id"), resultSet.getString("title"), resultSet.getString("summary")));
        }
        resultSet.close();
        return historyList;
    }

    private static ArrayList<Manga> readFromDatabase() {
        Database.accessDb(StaticStrings.DB_NAME_MANGA.getValue());
        try {
            return resultSetToArray(Database.retrieveHistoryEntries());
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogging.logError(e.toString());
        } finally {
            Database.terminateDbAccess();
        }
        return null;
    }
}
