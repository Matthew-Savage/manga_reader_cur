package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;

public class UpdateCollectedMangas {

    private static Database database = new Database();

    static void seeIfUpdated() {
        ArrayList<MangaArrayList> checkList = fetchChecklist();

        for (MangaArrayList currentTitle : checkList) {
            try {
                compareLocalToRemote(currentTitle.getTitleId(), currentTitle.getWebAddress(), currentTitle.getTotalChapters());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static void queueForDownload(int mangaId, int newChapterCount) {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.modifyManga(Values.DB_TABLE_COMPLETED.getValue(), mangaId, "total_chapters", newChapterCount);
        database.modifyManga(Values.DB_TABLE_COMPLETED.getValue(), mangaId, "new_chapters", 1);
        database.closeDb();
        database.openDb(Values.DB_NAME_DOWNLOADING.getValue());
        database.openDb(Values.DB_NAME_MANGA.getValue());
        database.downloadDbAttach();
        database.moveManga(Values.DB_ATTACHED_PREFIX.getValue() + Values.DB_TABLE_COMPLETED.getValue(), Values.DB_ATTACHED_DOWNLOADING.getValue(), mangaId);
        database.deleteManga(Values.DB_ATTACHED_PREFIX.getValue() + Values.DB_TABLE_COMPLETED.getValue(), mangaId);
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

    private static ArrayList<MangaArrayList> fetchChecklist() {
        database.openDb(Values.DB_NAME_MANGA.getValue());
        ArrayList<MangaArrayList> checkList = assembleChecklist(database.filterManga("completed", "status", "Ongoing"));
        database.closeDb();
        return checkList;
    }

    private static ArrayList<MangaArrayList> assembleChecklist(ResultSet resultSet) {
        ArrayList<MangaArrayList> checkList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                checkList.add(new MangaArrayList(resultSet.getInt("title_id"), null, null, null, null, resultSet.getString("web_address"), null, resultSet.getInt("total_chapters"), 0, 0, 0, 0, false));
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e);
        } return checkList;
    }


}