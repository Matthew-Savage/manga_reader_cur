package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;

public class HistoryPane {

    static ArrayList<Manga> retrieveStoredHistory() {
        return readFromDatabase();
    }

    public static void storeHistory() {
        checkIfTitlePresent();
    }

    private static void checkIfTitlePresent() {
        if (CategoryMangaLists.history.stream().noneMatch(v -> v.getTitleId() == CategoryMangaLists.selectedMangaIdentNumberTEMP)) {
            MangaValues.addAndRemove(CategoryMangaLists.collectedMangaList, CategoryMangaLists.history, CategoryMangaLists.parentListIndexNumberTEMP, false);
        }
    }

    private static ArrayList<Manga> resultSetToArray(ResultSet resultSet) throws Exception{
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
        } finally {
            Database.terminateDbAccess();
        } return null;
    }
}
