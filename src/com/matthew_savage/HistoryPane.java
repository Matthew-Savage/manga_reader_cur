package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;

public class HistoryPane {

//    private static Database database = new Database();
//
//+
//
//    static boolean storeHistory(ArrayList<MangaArrayList> list, int mangaId) {
//        return checkIfTitlePresent(list, mangaId);
//    }

    static ArrayList<MangaArrayList> retrieveStoredHistory() {
        return readFromDatabase();
    }

    public static void storeHistory() {
        checkIfTitlePresent();
    }

    private static void checkIfTitlePresent() {
        if (CategoryMangaLists.history.stream().noneMatch(v -> v.getTitleId() == CategoryMangaLists.selectedMangaIdentNumberTEMP)) {
            MangaValues.addToHistory(Values.DB_NAME_MANGA.getValue());
        }
    }


    private static ArrayList<MangaArrayList> resultSetToArray(ResultSet resultSet) throws Exception{
        ArrayList<MangaArrayList> historyList = new ArrayList<>();

        while (resultSet.next()) {
            historyList.add(new MangaArrayList(resultSet.getInt("title_id"), resultSet.getString("title"), resultSet.getString("summary")));
        }
        resultSet.close();
        return historyList;
    }

//    private static boolean checkIfTitlePresent(ArrayList<MangaArrayList> list, int mangaIdent) {
//        return list.stream().anyMatch(v -> v.getTitleId() == mangaIdent);
//    }


    private static ArrayList<MangaArrayList> readFromDatabase() {
        Database.accessDb(Values.DB_NAME_MANGA.getValue());
        try {
            return resultSetToArray(Database.retrieveHistoryEntries());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.terminateDbAccess();
        } return null;
    }

//    private void writeToDatabase(int mangaId, String title, String summary) {
//        database.openDb("main.db");
//        database.storeHistoryEntries(mangaId, title, summary);
//        database.closeDb();
//    }

}
