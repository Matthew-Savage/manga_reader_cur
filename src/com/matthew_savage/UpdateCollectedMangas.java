package com.matthew_savage;

import java.util.ArrayList;

import static com.matthew_savage.CategoryMangaLists.*;

public class UpdateCollectedMangas {

    private static int totalUpdatedTitles = 0;
    private static int totalUpdatedChapters = 0;
    private static ArrayList<Manga> currentlyCompleted = new ArrayList<>();

    public static void checkIfUpdated() {
        currentlyCompleted.clear();
        currentlyCompleted.addAll(completedMangaList);
        for (Manga thisTitle : currentlyCompleted) {
            if (thisTitle.getStatus().equals("Ongoing")) {
                compareRemoteToLocal(thisTitle);
            }
        }
        if (totalUpdatedTitles > 1) {
            ControllerMain.updateMessage.set(totalUpdatedChapters + " new chapters added across " +
                    totalUpdatedTitles + " separate manga's!");
        } else if (totalUpdatedTitles == 1) {
            ControllerMain.updateMessage.set("A single title has " +
                    totalUpdatedChapters + " new chapters for your reading pleasure, yay!");
        }
        ControllerMain.firstUpdateRun = true;
    }

    private static void compareRemoteToLocal(Manga manga) {
        String webAddress = checkAddress(manga.getWebAddress(), manga.getTitle(), manga.getAuthors(), manga.getTitleId());
        int remoteChapCount = IndexMangaChapters.getChapterAddresses(webAddress).size();
        int parentIndexNum = ControllerMain.fetchOriginalIndexNumber(completedMangaList, manga.getTitleId());

        if (remoteChapCount > manga.getTotalChapters()) {
            manga.setNewChapters(remoteChapCount - manga.getTotalChapters());
            manga.setTotalChapters(remoteChapCount);
            DownloadRunnable.addToDatabase(manga.getTitleId(),
                    manga.getTitle(),
                    manga.getAuthors(),
                    manga.getStatus(),
                    manga.getSummary(),
                    manga.getWebAddress(),
                    manga.getAuthors(),
                    manga.getTotalChapters(),
                    manga.getCurrentPage(),
                    manga.getLastChapterRead(),
                    manga.getLastChapterDownloaded(),
                    manga.getNewChapters(),
                    manga.getFavorite());
            downloading.add(completedMangaList.get(parentIndexNum));
            totalUpdatedTitles = totalUpdatedTitles + 1;
            totalUpdatedChapters = totalUpdatedChapters + manga.getNewChapters();
            MangaValues.justRemove(completedMangaList, parentIndexNum);
            MangaValues.executeChanges();
        }
    }

    private static String checkAddress(String webAddress, String title, String authors, int identNum) {
        String currentAddress = InvalidEntry.verifyAddress(webAddress, title, authors);
        if (!webAddress.equals(currentAddress)) {
            MangaValues.modifyValue(completedMangaList, StaticStrings.DB_COL_URL.getValue(), currentAddress, identNum);
        }
        return currentAddress;
    }
}