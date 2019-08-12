package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;

import static com.matthew_savage.CategoryMangaLists.collectedMangaList;

public class StatsPane {

    private static Database database = new Database();

    static ArrayList<StatsArrayList> retrieveStoredStats() {
        return readFromDatabase();
    }

    private static ArrayList<StatsArrayList> readFromDatabase() {
        database.openDb(StaticStrings.DB_NAME_SETTINGS.getValue());

        try {
            return resultsToArray(database.fetchTableData(StaticStrings.DB_TABLE_STATS.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        } finally {
            database.closeDb();
        }
        return null;
    }

    private static ArrayList<StatsArrayList> resultsToArray(ResultSet resultSet) throws Exception {
        ArrayList<StatsArrayList> statsList = new ArrayList<>();

        while (resultSet.next()) {
            statsList.add(new StatsArrayList(resultSet.getInt(StaticStrings.DB_STATS_COL_TITLE_TOT.getValue()),
                    resultSet.getInt(StaticStrings.DB_STATS_COL_READING_TOT.getValue()),
                    resultSet.getInt(StaticStrings.DB_STATS_COL_FIN_TOT.getValue()),
                    resultSet.getInt(StaticStrings.DB_STATS_COL_PAGES_TOT.getValue()),
                    resultSet.getInt(StaticStrings.DB_STATS_COL_FAVE_TOT.getValue()),
                    resultSet.getInt(StaticStrings.DB_STATS_COL_BL_TOT.getValue()),
                    resultSet.getInt(StaticStrings.DB_STATS_COL_PAGES_DAY.getValue()),
                    resultSet.getString(StaticStrings.DB_STATS_COL_GEN_ONE.getValue()),
                    resultSet.getString(StaticStrings.DB_STATS_COL_GEN_TWO.getValue()),
                    resultSet.getString(StaticStrings.DB_STATS_COL_GEN_THREE.getValue())));
        }
        resultSet.close();
        return statsList;
    }

    private static void trackGenres() {

    }

    private void loltest() {

//        ArrayList<String> tags = collectedMangaList.stream()
//                .flatMap(x -> Arrays.stream(x.getGenreTags().split(", ")))
//                .collect(Collectors.toCollection(ArrayList::new));
//
//        Map<String, Long> occurrences = tags.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
//
//        Map sorted = occurrences.entrySet().stream().sorted(comparingByKey()).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2));
//
//

        int action = 0;
        int adult = 0;
        int adventure = 0;
        int comedy = 0;
        int cooking = 0;
        int doujinshi = 0;
        int drama = 0;
        int ecchi = 0;
        int fantasy = 0;
        int genderBender = 0;
        int harem = 0;
        int historical = 0;
        int horror = 0;
        int josei = 0;
        int manhua = 0;
        int manhwa = 0;
        int martialArts = 0;
        int mature = 0;
        int mecha = 0;
        int medical = 0;
        int mystery = 0;
        int oneShot = 0;
        int psychological = 0;
        int romance = 0;
        int schoolLife = 0;
        int sciFi = 0;
        int seinen = 0;
        int shoujo = 0;
        int shoujoAi = 0;
        int shounen = 0;
        int shounenAi = 0;
        int sliceOfLife = 0;
        int smut = 0;
        int sports = 0;
        int supernatural = 0;
        int tragedy = 0;
        int webtoons = 0;
        int yaoi = 0;
        int yuri = 0;

        for (Manga title : collectedMangaList) {
            if (title.getGenreTags().contains("Action")) {
                action++;
            }
            if (title.getGenreTags().contains("Adult")) {
                adult++;
            }
            if (title.getGenreTags().contains("Adventure")) {
                adventure++;
            }
            if (title.getGenreTags().contains("Comedy")) {
                comedy++;
            }
            if (title.getGenreTags().contains("Cooking")) {
                cooking++;
            }
            if (title.getGenreTags().contains("Doujinshi")) {
                doujinshi++;
            }
            if (title.getGenreTags().contains("Drama")) {
                drama++;
            }
            if (title.getGenreTags().contains("Ecchi")) {
                ecchi++;
            }
            if (title.getGenreTags().contains("Fantasy")) {
                fantasy++;
            }
            if (title.getGenreTags().contains("Gender bender")) {
                genderBender++;
            }
            if (title.getGenreTags().contains("Harem")) {
                harem++;
            }
            if (title.getGenreTags().contains("Historical")) {
                historical++;
            }
            if (title.getGenreTags().contains("Horror")) {
                horror++;
            }
            if (title.getGenreTags().contains("Josei")) {
                josei++;
            }
            if (title.getGenreTags().contains("Manhua")) {
                manhua++;
            }
            if (title.getGenreTags().contains("Manhwa")) {
                manhwa++;
            }
            if (title.getGenreTags().contains("Martial arts")) {
                martialArts++;
            }
            if (title.getGenreTags().contains("Mature")) {
                mature++;
            }
            if (title.getGenreTags().contains("Mecha")) {
                mecha++;
            }
            if (title.getGenreTags().contains("Medical")) {
                medical++;
            }
            if (title.getGenreTags().contains("Mystery")) {
                mystery++;
            }
            if (title.getGenreTags().contains("One shot")) {
                oneShot++;
            }
            if (title.getGenreTags().contains("Psychological")) {
                psychological++;
            }
            if (title.getGenreTags().contains("Romance")) {
                romance++;
            }
            if (title.getGenreTags().contains("School life")) {
                schoolLife++;
            }
            if (title.getGenreTags().contains("Sci fi")) {
                sciFi++;
            }
            if (title.getGenreTags().contains("Seinen")) {
                seinen++;
            }
            if (title.getGenreTags().contains("Shoujo")) {
                shoujo++;
            }
            if (title.getGenreTags().contains("Shoujo ai")) {
                shoujoAi++;
            }
            if (title.getGenreTags().contains("Shounen")) {
                shounen++;
            }
            if (title.getGenreTags().contains("Shounen ai")) {
                shounenAi++;
            }
            if (title.getGenreTags().contains("Slice of life")) {
                sliceOfLife++;
            }
            if (title.getGenreTags().contains("Smut")) {
                smut++;
            }
            if (title.getGenreTags().contains("Sports")) {
                sports++;
            }
            if (title.getGenreTags().contains("Supernatural")) {
                supernatural++;
            }
            if (title.getGenreTags().contains("Tragedy")) {
                tragedy++;
            }
            if (title.getGenreTags().contains("Webtoons")) {
                webtoons++;
            }
            if (title.getGenreTags().contains("Yaoi")) {
                yaoi++;
            }
            if (title.getGenreTags().contains("Yuri")) {
                yuri++;
            }
        }

        System.out.println("Action: " + action +
                "\\nAdult: " + adult +
                "\\nAdventure: " + adventure +
                "\\nComedy: " + adult +
                "\\nCooking: " + adult +
                "\\nDoujinshi: " + adult +
                "\\nDrama: " + adult +
                "\\nEcchi: " + adult +
                "\\nFantasy: " + adult +
                "\\nGender bender: " + adult +
                "\\nHarem: " + adult +
                "\\nHistorical: " + adult +
                "\\nHorror: " + adult +
                "\\nJosei: " + adult +
                "\\nManhua: " + adult +
                "\\nManhwa: " + adult +
                "\\nMartial arts: " + adult +
                "\\nMature: " + adult +
                "\\nMecha: " + adult +
                "\\nMedical: " + adult +
                "\\nMystery: " + adult +
                "\\nOne shot: " + adult +
                "\\nPsychological: " + adult +
                "\\nRomance: " + adult +
                "\\nSchool life: " + adult +
                "\\nSci fi: " + adult +
                "\\nSeinen: " + adventure +
                "\\nShoujo: " + adult +
                "\\nShoujo ai: " + adult +
                "\\nShounen: " + adult +
                "\\nShounen ai: " + adult +
                "\\nSlice of life: " + adult +
                "\\nSmut: " + adult +
                "\\nSports: " + adult +
                "\\nSupernatural: " + adult +
                "\\nTragedy: " + adult +
                "\\nWebtoons: " + adult +
                "\\nYaoi: " + adult +
                "\\nYuri: " + adult
        );

    }

}
