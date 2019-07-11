package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;

public class UpdateCollectedMangas {

    private static Database database = new Database();

    static void seeIfUpdated() {
        ArrayList<Manga> checkList = fetchChecklist();

        for (Manga currentTitle : checkList) {
            try {
                compareLocalToRemote(currentTitle.getTitleId(), currentTitle.getWebAddress(), currentTitle.getTotalChapters());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static void queueForDownload(int mangaId, int newChapterCount) {
        database.openDb(StaticStrings.DB_NAME_MANGA.getValue());
        database.modifyManga(StaticStrings.DB_TABLE_COMPLETED.getValue(), mangaId, "total_chapters", newChapterCount);
        database.modifyManga(StaticStrings.DB_TABLE_COMPLETED.getValue(), mangaId, "new_chapters", 1);
        database.closeDb();
        database.openDb(StaticStrings.DB_NAME_DOWNLOADING.getValue());
        database.openDb(StaticStrings.DB_NAME_MANGA.getValue());
        database.downloadDbAttach();
        database.moveManga(StaticStrings.DB_ATTACHED_PREFIX.getValue() + StaticStrings.DB_TABLE_COMPLETED.getValue(), StaticStrings.DB_ATTACHED_DOWNLOADING.getValue(), mangaId);
        database.deleteManga(StaticStrings.DB_ATTACHED_PREFIX.getValue() + StaticStrings.DB_TABLE_COMPLETED.getValue(), mangaId);
        database.downloadDbDetach();
        database.closeDb();
        database.closeDb();
    }

    private static void compareLocalToRemote(int mangaId, String webAddress, int currentTotalChapters) throws Exception{
        int newChapterCount = IndexMangaChapters.getChapterAddresses(webAddress).size();

        if (newChapterCount > currentTotalChapters) {
            queueForDownload(mangaId, newChapterCount);
        }
    }

    private static ArrayList<Manga> fetchChecklist() {
        database.openDb(StaticStrings.DB_NAME_MANGA.getValue());
        ArrayList<Manga> checkList = assembleChecklist(database.filterManga("completed", "status", "Ongoing"));
        database.closeDb();
        return checkList;
    }

    private static ArrayList<Manga> assembleChecklist(ResultSet resultSet) {
        ArrayList<Manga> checkList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                checkList.add(new Manga(resultSet.getInt("title_id"), null, null, null, null, resultSet.getString("web_address"), null, resultSet.getInt("total_chapters"), 0, 0, 0, 0, 0));
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e);
        } return checkList;
    }


}