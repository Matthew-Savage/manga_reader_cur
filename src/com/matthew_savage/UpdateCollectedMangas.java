package com.matthew_savage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class UpdateCollectedMangas {

    private static ControllerMain controllerMain;
    private static Database database;


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
                        database.openDb(Values.DB_NAME_MANGA.getValue());
                        database.modifyManga("completed", selectedManga, "total_chapters", chapterCount.size());
//                    database.modifyManga("completed", selectedManga, "last_chapter_read", startingChapter); this doesnt change
                        database.modifyManga("completed", selectedManga, "new_chapters", 1);
                        database.closeDb();
                        //redundant with a method in ControllerMain class
                        database.openDb(Values.DB_NAME_DOWNLOADING.getValue());
                        database.openDb(Values.DB_NAME_MANGA.getValue());
                        database.downloadDbAttach();
                        database.moveManga(Values.DB_ATTACHED_PREFIX.getValue() + Values.DB_TABLE_COMPLETED.getValue(), Values.DB_ATTACHED_DOWNLOADING.getValue(), selectedManga);
                        database.deleteManga(Values.DB_ATTACHED_PREFIX.getValue() + Values.DB_TABLE_COMPLETED.getValue(), selectedManga);
                        database.downloadDbDetach();
                        database.closeDb();
                        database.closeDb();
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

    private static void checkforUpdates(String webAddress, int currentTotalChapters) throws Exception{
        int newChapterCount = IndexMangaChapters.getChapterAddresses(webAddress).size();

        // we should put a delay in this and run it slowly instead of spam the fuck out of the server. not a crazy delay but something.
        if (newChapterCount > currentTotalChapters) {
            //manga has updates, do stuff
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
        } catch (Exception e) {
            System.out.println(e);
        } return checkList;
    }


}