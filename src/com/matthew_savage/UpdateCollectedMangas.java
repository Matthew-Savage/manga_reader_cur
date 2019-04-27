package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class UpdateCollectedMangas {

    private ControllerMain controllerMain;
    private static Database database = new Database();


    private static void checkForUpdates() {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> updateCheckAddresses = new ArrayList<>();
        int selectedManga;
        int startingChapter;
        String webAddress;

        int updateTotalChapters;
        try {
            database.openDb(Values.DB_NAME_MANGA.getValue());
            ResultSet resultSet = database.filterManga("completed", "status", "Ongoing");
            if (resultSet.next()) {
                do {
                    updateCheckAddresses.add(resultSet.getInt("title_id"));
                } while (resultSet.next());
                resultSet.close();
                database.closeDb();
                for (int i = 0; i < updateCheckAddresses.size(); i++) {
                    selectedManga = updateCheckAddresses.get(i);
                    database.openDb(Values.DB_NAME_MANGA.getValue());
                    resultSet = database.filterManga("completed", "title_id", Integer.toString(selectedManga));
                    ArrayList chapterCount = IndexMangaChapters.getChapterAddresses(resultSet.getString("web_address"));
                    updateTotalChapters = resultSet.getInt("total_chapters");
                    webAddress = resultSet.getString("web_address");
                    startingChapter = (resultSet.getInt("last_chapter_read"));
                    database.closeDb();
                    System.out.println(chapterCount.size() + "  -  chapter count!");
                    if (chapterCount.size() > updateTotalChapters) {
                        int sizeDifference = chapterCount.size() - updateTotalChapters;
                        System.out.println("holy shit a manga " + updateCheckAddresses.get(i) + " has " + sizeDifference + " new chapters!");
//                    chapterPages.getChapterPages(updateTotalChapters, updateWebAddress, Integer.toString(selectedManga));
//                        database.openDb(Values.DB_NAME_MANGA.getValue());
//                        database.modifyManga("completed", selectedManga, "total_chapters", chapterCount.size());
////                    database.modifyManga("completed", selectedManga, "last_chapter_read", startingChapter); this doesnt change
//                        database.modifyManga("completed", selectedManga, "new_chapters", 1);
//                        database.closeDb();
//                        //redundant with a method in ControllerMain class
//                        database.openDb(Values.DB_NAME_DOWNLOADING.getValue());
//                        database.openDb(Values.DB_NAME_MANGA.getValue());
//                        database.downloadDbAttach();
//                        database.moveManga(Values.DB_ATTACHED_PREFIX.getValue() + Values.DB_TABLE_COMPLETED.getValue(), Values.DB_ATTACHED_DOWNLOADING.getValue(), selectedManga);
//                        database.deleteManga(Values.DB_ATTACHED_PREFIX.getValue() + Values.DB_TABLE_COMPLETED.getValue(), selectedManga);
//                        database.downloadDbDetach();
//                        database.closeDb();
//                        database.closeDb();
                        //end redundant

//                        database.moveManga("completed", "download_pending", selectedManga);
//                        database.closeDb();
//                        dbMoveAndCopy.copyToDownloading(selectedManga, webAddress, startingChapter);
                    }
                }
            }
            TimeUnit.MILLISECONDS.sleep(100);
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (Exception e) {
            //Not Possible
            System.out.println(e);
        }
    }

    static void seeIfUpdated() {
        System.out.println("update checker started!");
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
            System.out.println(mangaId + " has new chapters!!");
            queueForDownload(mangaId, newChapterCount);
        } else {
            System.out.println(mangaId + " has NO new chapters.");
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
                checkList.add(new MangaArrayList(resultSet.getInt("title_id"), null, null, null, null, resultSet.getString("web_address"), null, resultSet.getInt("total_chapters"), 0, 0, 0, false, false));
            }
            resultSet.close();
            System.out.println("checklist assembled, " + checkList.size() + " items big!");
        } catch (Exception e) {
            System.out.println(e);
        } return checkList;
    }


}