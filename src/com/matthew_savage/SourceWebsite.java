package com.matthew_savage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static com.matthew_savage.CategoryMangaLists.*;

public class SourceWebsite {

    private static int pageNumber = 22;
    private static int recordNumber = 0;
    private static int currentHighestIdentNum;
    private static ArrayList<Manga> invalidMangaEntries = new ArrayList<>();
    private static ArrayList<Manga> newMangaEntries = new ArrayList<>();

    public static void indexTitles() {
        ControllerLoader.update.set("Fetching new titles ...");
        if (fiveNewestTitles.isEmpty()) {
            indexAllTitles();
        } else {
            checkListValidity();
        }
    }

    private static void checkListValidity() {
        if (fiveNewestTitles.size() == 1) {
            consolidateLibrary();
        }

        System.out.println(fiveNewestTitles.get(0).getTitleId());
        currentHighestIdentNum = fiveNewestTitles.get(0).getTitleId();
        indexNewlyAddedTitles();
    }

    private static void consolidateLibrary() {
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
        int x = 1;
            while (fiveNewestTitles.size() < 5) {
                boolean notYetPresent = true;
                for (Manga v : fiveNewestTitles) {
                    if (v.getTitleId() == arrayList.get(x).getTitleId()) {
                        notYetPresent = false;
                        break;
                    }
                }
                if (notYetPresent) {
                    fiveNewestTitles.add(arrayList.get(x));
                } else {
                    x++;
                }
            }
    }

    private static void indexAllTitles() {
        // haha if this is actually happening something is totally fucked
        // need some kind of message explaining something is almost certainly wrong
        // but if not, they better start this before bed because its going to take about
        // a full fucking day to complete
    }

    private static void indexNewlyAddedTitles() {
        try {
            locateLastPageCataloged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void locateLastPageCataloged() throws Exception {
        int multiplier = 1;
        boolean matchPossible = true;

        search:
        while (matchPossible){
            if (comparisonsExhausted()) {
                    recordNumber = 0;
                    multiplier = 10;
                    ControllerLoader.update.set("Quickmatch unsuccessful - beginning extended match ...");
            }
            String localTitle = localMangaTitle();
            for (Element mangaSummaryBox : fetchPageSource(selectionPageUrl()).select(".list-truyen-item-wrap")) {
                String title = remoteMangaTitle(mangaSummaryBox);
                String webAddress = remoteMangaWebAddress(mangaSummaryBox);
                if (title.equals(localTitle)) {
                    System.out.println(pageNumber);
                    if (newMangaEntries.size() == 0) {
                        ControllerLoader.update.set("No new titles at this time.");
                        break search;
                    }
//                    processInvalidEntriesList();
                    ControllerLoader.update.set(newMangaEntries.size() + " titles available ... Adding");
                    Collections.reverse(newMangaEntries);
                    addAllOtherValues();
                    addNewManga();
                    updateFiveLatestManga();
                    break search;
                }
                newMangaEntries.add(new Manga(title, webAddress));
            }
            pageNumber++;
//            if (pageNumber > (9 * multiplier)) {
//                if (multiplier == 10) {
////                    queueEntryForDeletion();
//                    pageNumber = 10;
//                } else {
//                    pageNumber = 1;
//                }
//                recordNumber++;
//            }
//            if (multiplier == 10 && recordNumber == 5) {
//                matchPossible = false;
//                ControllerLoader.updatable = false;
//            }
        }
    }

    private static void addAllOtherValues() throws Exception {
        int r = 1;
        int i = 0;
        // check all other for loops to see if they should be enh for loops!! so clean!!
        for (Manga newMangaEntry : newMangaEntries) {
            Document mangaDetailsPage = fetchPageSource(newMangaEntry.getWebAddress());
            currentHighestIdentNum++;
            for (Element mangaDetailsBox : mangaDetailsPage.select(".manga-info-text")) {
                newMangaEntry.setAuthors(appendPrefix(mangaDetailsBox.child(1).text()));
                newMangaEntry.setStatus(formatStatus(appendPrefix(mangaDetailsBox.child(2).text())));
                newMangaEntry.setGenreTags(appendPrefix(mangaDetailsBox.child(6).text()));
            }
            newMangaEntry.setTitleId(currentHighestIdentNum);
            mangaDetailsPage.select("h2 p").remove();
            newMangaEntry.setSummary(mangaDetailsPage.select("div#noidungm").text().replace("'", ""));
            newMangaEntry.setTotalChapters(0);
            newMangaEntry.setCurrentPage(0);
            newMangaEntry.setLastChapterRead(0);
            newMangaEntry.setLastChapterDownloaded(0);
            newMangaEntry.setNewChapters(0);
            newMangaEntry.setFavorite(0);
            //pretty sure I can hardcode static values in premadestatement whatever its called
//            RemoteImage.saveLocally(mangaDetailsPage.select(".manga-info-pic img").first().attr("abs:src"), currentHighestIdentNum);
            i++;
            ControllerLoader.update.set((i*100) / newMangaEntries.size() + " percent completed ...");
        }
        addNewTitles();
    }

    private static void addNewTitles() {
        for (int i = 0; i < newMangaEntries.size(); i++) {
            MangaValues.addAndRemove(newMangaEntries, notCollectedMangaList, i, false);
        }
        MangaValues.executeChanges();
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

    private static String appendPrefix(String string) {
        return string.replaceAll(".*: ", "");
    }

    private static Document fetchPageSource(String webAddress) throws Exception {
        return Jsoup.connect(webAddress).get();
    }

    private static String selectionPageUrl() {
        return StaticStrings.URL_ROOT.getValue() + "manga_list?type=newest&category=all&state=all&page=" + pageNumber;
    }

    private static String remoteMangaTitle(Element element) {
        return element.select("h3 a").text().replace("'", "");
    }

    private static String remoteMangaWebAddress(Element element) {
        return element.select("h3 a").first().attr("abs:href");
    }

    private static String localMangaTitle() {
        return fiveNewestTitles.get(recordNumber).getTitle();
    }

    private static void queueEntryForDeletion() {
        invalidMangaEntries.add(fiveNewestTitles.get(recordNumber));
    }

    private static void processInvalidEntriesList() {
        // dont think im going to do anything here, at least for now
        for (Manga listItem : invalidMangaEntries) {
            InvalidEntry.deleteInvalidEntry(listItem.getTitleId());
        }
    }

    private static void addNewManga() {
        CategoryMangaLists.notCollectedMangaList.addAll(newMangaEntries);
    }

    private static void updateFiveLatestManga() {
        Collections.reverse(newMangaEntries);
        if (newMangaEntries.size() < 5) {
            newMangaEntries.addAll(fiveNewestTitles);
        }
        MangaValues.deleteAll(fiveNewestTitles);
        MangaValues.topFive(newMangaEntries);
        MangaValues.executeChanges();
    }

    private static boolean comparisonsExhausted() {
        return recordNumber == 5;
    }
}
