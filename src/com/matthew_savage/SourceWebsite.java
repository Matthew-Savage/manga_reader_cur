package com.matthew_savage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static com.matthew_savage.CategoryMangaLists.*;

public class SourceWebsite {

    private static int pageNumber = 23;
    private static int recordNumber = 0;
    private static int currentHighestIdentNum;
    private static ArrayList<MangaArrayList> invalidMangaEntries = new ArrayList<>();
    private static ArrayList<MangaArrayList> newMangaEntries = new ArrayList<>();

    public static void indexTitles() {
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
        ArrayList<MangaArrayList> mangaConsolidation = new ArrayList<>();

        mangaConsolidation.addAll(notCollectedMangaList);
        mangaConsolidation.addAll(collectedMangaList);
        mangaConsolidation.addAll(completedMangaList);
        mangaConsolidation.addAll(rejectedMangaList);
        mangaConsolidation.addAll(downloading);

        mangaConsolidation.sort(Comparator.comparingInt(MangaArrayList::getTitleId).reversed());
        determineFiveLatest(mangaConsolidation);
    }

    private static void determineFiveLatest(ArrayList<MangaArrayList> arrayList) {
        int x = 1;
            while (fiveNewestTitles.size() < 5) {
                boolean notYetPresent = true;
                for (MangaArrayList v : fiveNewestTitles) {
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
            System.out.println("yaya lets go!");
            if (comparisonsExhausted()) {
                System.out.println("exhausted!");
                    recordNumber = 0;
                    multiplier = 10;
                    // update preloader with message that search is being extended and will take longer.
            }
            String localTitle = localMangaTitle();
            for (Element mangaSummaryBox : fetchPageSource(selectionPageUrl()).select(".list-truyen-item-wrap")) {
                String title = remoteMangaTitle(mangaSummaryBox);
                String webAddress = remoteMangaWebAddress(mangaSummaryBox);
                if (title.equals(localTitle)) {
                    System.out.println("match found");
                    if (newMangaEntries.size() == 0) {
                        //fuck i dont know. what have I done here lol. I've trapped myself in the middle of a fucking
                        //method. need to do NOTHING if this is the case.
                        //i needed to rewrite this method anyways, now i def need to.
                        System.out.println("everything needs to stop now");
                        break search;
                    }
//                    processInvalidEntriesList();
                    Collections.reverse(newMangaEntries);
                    addAllOtherValues();
                    addNewManga();
                    updateFiveLatestManga();
                    break search;
                }
                newMangaEntries.add(new MangaArrayList(title, webAddress));
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
            if (multiplier == 10 && recordNumber == 5) {
                matchPossible = false;
                System.out.println("pump the brakes kid");
                // need to output public error message
                // this train is fucked
            }
        }
    }

    private static boolean checkIfMatching(String remoteTitle) {


        return remoteTitle.equals(localMangaTitle());
    }

    private static void addAllOtherValues() throws Exception {
        // check all other for loops to see if they should be enh for loops!! so clean!!
        for (MangaArrayList newMangaEntry : newMangaEntries) {
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
            //pretty sure I can hardcode static values in premadestatement whatever its called
            RemoteImage.saveLocally(mangaDetailsPage.select(".manga-info-pic img").first().attr("abs:src"), currentHighestIdentNum);
        }
        MangaValues.addNewTitles(newMangaEntries);
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
        return Values.URL_ROOT.getValue() + "manga_list?type=newest&category=all&state=all&page=" + pageNumber;
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
        for (MangaArrayList listItem : invalidMangaEntries) {
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

        MangaValues.setFiveLatestMangas(newMangaEntries);


        for (MangaArrayList five : newMangaEntries) {
            System.out.println(five.getTitle());
        }

    }

    private static boolean comparisonsExhausted() {
        return recordNumber == 5;
    }
}
