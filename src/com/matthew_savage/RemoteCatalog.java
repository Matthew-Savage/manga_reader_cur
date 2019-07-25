package com.matthew_savage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.matthew_savage.CategoryMangaLists.*;

public class RemoteCatalog {

    private static int newestTitleIdent;
    private static ArrayList<Manga> results = new ArrayList<>();
    private static int currentPageNum = 1;
    private static int currentEntryNum = 0;
    private static int currentMultiplier = 1;
    private static boolean matchNotFound = true;

    public static void forceUpdate() {
        selectedMangaWebAddressTEMP = InvalidEntry.verifyAddress(selectedMangaWebAddressTEMP, selectedMangaTitleTEMP);
        newestTitleIdent = selectedMangaIdentNumberTEMP;
        results.clear();
        results.add(new Manga(selectedMangaTitleTEMP, selectedMangaWebAddressTEMP));
        populateRemainingValues(0, false);
        selectedMangaAuthorsTEMP = results.get(0).getAuthors();
        selectedMangaSummaryTEMP = results.get(0).getSummary();
        selectedMangaGenresTEMP = results.get(0).getGenreTags();
    }

    public static void indexTitles() {
        ControllerLoader.update.set("fetching new titles ...");
        prepareComparisonList();
        do {
            processRemoteEntries();
        } while (matchNotFound);
    }

    private static void prepareComparisonList() {
        populateFiveNewest();
        newestTitleIdent = fiveNewestTitles.get(0).getTitleId();
    }

    private static void populateFiveNewest() {
        ArrayList<Manga> mangaConsolidation = new ArrayList<>();
        mangaConsolidation.addAll(notCollectedMangaList);
        mangaConsolidation.addAll(collectedMangaList);
        mangaConsolidation.addAll(completedMangaList);
        mangaConsolidation.addAll(rejectedMangaList);
        mangaConsolidation.addAll(downloading);
        mangaConsolidation.sort(Comparator.comparingInt(Manga::getTitleId).reversed());
        determineFiveLatest(mangaConsolidation);
    }

    private static void determineFiveLatest(ArrayList<Manga> arrayList) {
        int i = 0;
        while (fiveNewestTitles.size() < 5) {
            boolean notYetPresent = true;
            for (Manga manga : fiveNewestTitles) {
                if (manga.getTitleId() == arrayList.get(i).getTitleId()) {
                    notYetPresent = false;
                    break;
                }
            }
            if (notYetPresent) {
                fiveNewestTitles.add(arrayList.get(i));
            } else {
                i++;
            }
        }

        for (Manga manga : fiveNewestTitles) {
            System.out.println(manga.getTitleId() + " - " + manga.getTitle());
        }
    }

    private static void processRemoteEntries() {
        for (Element remoteEntry : remoteEntries().select(entryVal())) {
            compareRemoteToLocal(remoteEntry);
        }
        tryNextPage();
    }

    private static void compareRemoteToLocal(Element element) {
        if (remoteMangaTitle(element).equals(localMangaTitle())) {
            matchNotFound = false;
            processResults();
            updateFiveLatest();
        } else {
            results.add(new Manga(remoteMangaTitle(element), remoteMangaWebAddress(element)));
        }
    }

    private static void processResults() {
        if (results.size() == 0) {
            ControllerLoader.update.set("No new titles.");
        } else {
            ControllerLoader.update.set("Adding " + results.size() + " new titles ...");
            Collections.reverse(results);
            Database.accessDb(StaticStrings.DB_NAME_MANGA.getValue());
            for (int i = 0; i < results.size(); i++) {
                newestTitleIdent++;
                populateRemainingValues(i, true);
                insertNewRecord(i, StaticStrings.DB_TABLE_AVAILABLE.getValue());
            }
            Database.terminateDbAccess();
        }
    }

    private static void populateRemainingValues(int indexNumber, boolean firstTimeFetched){
        Document document = fetchPageSource(results.get(indexNumber).getWebAddress());
        for (Element details : document.select(detailVal())) {
            results.get(indexNumber).setAuthors(appendPrefix(details.child(1).text()));
            results.get(indexNumber).setStatus(formatStatus(appendPrefix(details.child(2).text())));
            results.get(indexNumber).setGenreTags(appendPrefix(details.child(6).text()));
        }
        results.get(indexNumber).setTitleId(newestTitleIdent);
        document.select("h2 p").remove();
        results.get(indexNumber).setSummary(document.select("div#noidungm").text().replace("'", ""));
        RemoteImage.saveLocally(document.select(".manga-info-pic img").first().attr("abs:src"), newestTitleIdent);
        if (firstTimeFetched) {
            results.get(indexNumber).setTotalChapters(0);
            results.get(indexNumber).setCurrentPage(0);
            results.get(indexNumber).setLastChapterRead(0);
            results.get(indexNumber).setLastChapterDownloaded(0);
            results.get(indexNumber).setNewChapters(0);
            results.get(indexNumber).setFavorite(0);
            updateProgress(indexNumber);
        }
    }

    private static void insertNewRecord(int indexNumber, String tableName) {

        Database.addMangaEntry(tableName, results.get(indexNumber).getTitleId(),
                results.get(indexNumber).getTitle(),
                results.get(indexNumber).getAuthors(),
                results.get(indexNumber).getStatus(),
                results.get(indexNumber).getSummary(),
                results.get(indexNumber).getWebAddress(),
                results.get(indexNumber).getGenreTags(),
                results.get(indexNumber).getTotalChapters(),
                results.get(indexNumber).getCurrentPage(),
                results.get(indexNumber).getLastChapterRead(),
                results.get(indexNumber).getLastChapterDownloaded(),
                results.get(indexNumber).getNewChapters(),
                results.get(indexNumber).getFavorite());
        if (tableName.equals(StaticStrings.DB_TABLE_AVAILABLE.getValue())) {
            notCollectedMangaList.add(results.get(indexNumber));
        } else {
            fiveNewestTitles.add(results.get(indexNumber));
        }

    }

    private static void updateProgress(int IndexNumber) {
        ControllerLoader.updateBottom.set((IndexNumber * 100) / results.size() + " %");
    }

    private static void tryNextPage() {
        currentPageNum++;

        if (currentPageNum == (10 * currentMultiplier)) {
            currentEntryNum++;
            currentPageNum = 1;
            if (currentEntryNum == 5) {
                if (currentMultiplier == 1) {
                    ControllerLoader.update.set("Still fetching ...");
                    currentMultiplier = 10;
                    currentPageNum = 10;
                    currentEntryNum = 0;
                } else if (currentMultiplier == 10) {
                    ControllerLoader.updatable = false;
                    matchNotFound = false;
                }
            }
        }
    }

    private static void updateFiveLatest() {
        Collections.reverse(results);
        if (results.size() < 5) {
            results.addAll(fiveNewestTitles);
        }
        Database.accessDb(StaticStrings.DB_NAME_MANGA.getValue());
        Database.removeAll(StaticStrings.DB_TABLE_FIVE_NEWEST.getValue());
        for (int i = 0; i < 5; i++) {
            insertNewRecord(i, StaticStrings.DB_TABLE_FIVE_NEWEST.getValue());
        }
        Database.terminateDbAccess();
    }

    private static boolean comparisonsExhausted() {
        return currentEntryNum == 5;
    }

    private static Document remoteEntries() {
        return selectionPageUrl();
    }

    private static Document selectionPageUrl() {
        return fetchPageSource(StaticStrings.URL_ROOT.getValue() + "manga_list?type=newest&category=all&state=all&page=" + currentPageNum);
    }

    private static Document fetchPageSource(String webAddress) {
        try {
            return Jsoup.connect(webAddress).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String remoteMangaTitle(Element element) {
        return element.select("h3 a").text().replace("'", "");
    }

    private static String remoteMangaWebAddress(Element element) {
        return element.select("h3 a").first().attr("abs:href");
    }

    private static String localMangaTitle() {
        return fiveNewestTitles.get(currentEntryNum).getTitle();
    }

    private static String appendPrefix(String string) {
        return string.replaceAll(".*: ", "");
    }

    private static String formatStatus(String statusText) {
        if (statusText.contains("Completed") && statusText.length() > 9) {
            return statusText.substring(0, 9);
        } else if (statusText.contains("Ongoing") && statusText.length() > 7) {
            return statusText.substring(0, 7);
        } else {
            return statusText;
        }
    }

    private static String entryVal() {
        return ".list-truyen-item-wrap";
    }

    private static String detailVal() {
        return ".manga-info-text";
    }
}
