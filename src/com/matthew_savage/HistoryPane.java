package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;

public class HistoryPane {

    private static Database database = new Database();

    static ArrayList<MangaArrayList> retrieveStoredHistory() {
        return readFromDatabase();
    }

    static boolean storeHistory(ArrayList<MangaArrayList> list, int mangaId) {
        return checkIfExists(list, mangaId);
    }

    private static ArrayList<MangaArrayList> resultSetToArray(ResultSet resultSet) throws Exception{
        ArrayList<MangaArrayList> historyList = new ArrayList<>();

        while (resultSet.next()) {
            historyList.add(new MangaArrayList(resultSet.getInt("title_id"), resultSet.getString("title"), resultSet.getString("summary")));
        }
        resultSet.close();
        return historyList;
    }

    private static boolean checkIfExists(ArrayList<MangaArrayList> list, int mangaId) {
        return list.stream().anyMatch(v -> v.getTitleId() == mangaId);
    }

    private static ArrayList<MangaArrayList> readFromDatabase() {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        try {
            return resultSetToArray(database.retrieveHistoryEntries());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.closeDb();
        } return null;
    }

    private void writeToDatabase(int mangaId, String title, String summary) {
        database.openDb("main.db");
        database.storeHistoryEntries(mangaId, title, summary);
        database.closeDb();
    }

}
