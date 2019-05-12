package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;

public class StatsPane {

    private static Database database = new Database();

    static ArrayList<StatsArrayList> retrieveStoredStats() {
        return readFromDatabase();
    }

    private static ArrayList<StatsArrayList> readFromDatabase() {
        database.openDb(Values.DB_NAME_SETTINGS.getValue());

        try {
            return resultsToArray(database.fetchTableData(Values.DB_TABLE_STATS.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.closeDb();
        } return null;
    }

    private static ArrayList<StatsArrayList> resultsToArray(ResultSet resultSet) throws Exception {
        ArrayList<StatsArrayList> statsList = new ArrayList<>();

        while (resultSet.next()) {
            statsList.add(new StatsArrayList(resultSet.getInt(Values.DB_COL_TITLE_TOT.getValue()),
                    resultSet.getInt(Values.DB_COL_READING_TOT.getValue()),
                    resultSet.getInt(Values.DB_COL_FIN_TOT.getValue()),
                    resultSet.getInt(Values.DB_COL_PAGES_TOT.getValue()),
                    resultSet.getInt(Values.DB_COL_FAVE_TOT.getValue()),
                    resultSet.getInt(Values.DB_COL_BL_TOT.getValue()),
                    resultSet.getInt(Values.DB_COL_PAGES_DAY.getValue()),
                    resultSet.getString(Values.DB_COL_GEN_ONE.getValue()),
                    resultSet.getString(Values.DB_COL_GEN_TWO.getValue()),
                    resultSet.getString(Values.DB_COL_GEN_THREE.getValue())));
        }
        resultSet.close();
        return statsList;
    }
}
