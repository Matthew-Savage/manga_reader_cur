package com.matthew_savage;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

class DownloadMangaPages {

    private static ArrayList<String> mangaChapters = new ArrayList<>();
    private static int mangaIdentNum;
    private static String mangaTitle;

    public static Runnable processDownloadQueue = () -> {
        if (CategoryMangaLists.downloading.size() > 0) {
            for (Manga download : CategoryMangaLists.downloading) {
                mangaChapters.clear();
                mangaChapters.addAll(IndexMangaChapters.getChapterAddresses(download.getWebAddress()));
                mangaIdentNum = download.getTitleId();
                mangaTitle = download.getTitle();
                MangaValues.modifyValue(CategoryMangaLists.downloading, StaticStrings.DB_COL_CHAP_TOT.getValue(), mangaChapters.size(), mangaIdentNum);
                getChapterPages();
                downloadComplete();
                // update currently downloading static pane on homepage
                // create a scheduledthreadwhatever to check on this, fuck a timer
            }
        }
    };

    private static void getChapterPages() {
        if (checkSiteHtml()) {
            ControllerMain.killDownloadProcess();
            ControllerMain.downloadThread.shutdown();
            ControllerMain.errorMessage.set(StaticStrings.ERR_HTML.getValue());
            // write to error file
        } else {
            downloadEachChapter();
        }
    }

    private static boolean checkSiteHtml() {
        try {
            Document chapterOne = Jsoup.connect(mangaChapters.get(0)).get();
            return chapterOne.select(".vung-doc img").first() == null;
        } catch (Exception e) {
            e.printStackTrace();
        } return true;
    }

    private static void downloadEachChapter() {
        int chapterNum = 0;
        for (String chapter : mangaChapters) {
            processImageList(chapter, mangaIdentNum, chapterNum);
            chapterNum++;
            MangaValues.modifyValue(CategoryMangaLists.downloading, StaticStrings.DB_COL_LAST_CHAP_DL.getValue(), chapterNum, mangaIdentNum);
            checkIfReadReady(chapterNum);
        }
    }

    private static void checkDownloadHealth() {
        // someday
        ControllerMain.killDownloadProcess();
        ControllerMain.downloadThread.shutdown();
        ControllerMain.errorMessage.set(StaticStrings.ERR_DL_MISC.getValue());
    }

    private static void processImageList(String chapterAddress, int mangaIdentNum, int chapterNumber) {
        int imageNumber = 0;
        try {
            Connection.Response response = Jsoup.connect("https://manganelo.com/change_content_s" +
                    fetchVerificationUrl(chapterAddress)).method(Connection.Method.GET).execute();
            Document chapterImages = Jsoup.connect(chapterAddress).cookies(response.cookies()).get();
            createMangaFolder(mangaIdentNum, chapterNumber); // probably going to change this per checkifready notes

            for (Element image : chapterImages.select(".vung-doc img")) {
                saveImage(image, mangaIdentNum, chapterNumber, imageNumber);
                imageNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkIfReadReady(int chapterNum) {
        if (chapterNum == 1) {
            // database, insert into currently reading, maybe refresh currently reading pane via searchgenrewhatever?
            // maybe proactively create all manga chapter folders and put a "currently downloading" image in them
            // that would be fucking dope
            ControllerMain.downloadMessage.set(mangaTitle + StaticStrings.DL_PART.getValue());
        }
    }

    private static int fetchVerificationUrl(String chapterUrl) throws Exception{

        Document imageUrl = Jsoup.connect(chapterUrl).get();
        return verifyImageServer(imageUrl.select(".vung-doc img").first().attr("abs:src"));
    }

    private static int verifyImageServer(String imageUrl) throws Exception{
        int serverNumber;

        HttpURLConnection testConnection = (HttpURLConnection) (new URL(imageUrl).openConnection());
        testConnection.setRequestMethod("HEAD");
        testConnection.connect();

        if (testConnection.getResponseCode() == 200) {
            serverNumber = 1;
        } else {
            serverNumber = 2;
        }
        return serverNumber;
    }

    private static void createMangaFolder(int mangaId, int startingChapterNumber) throws Exception{
        Path to = Paths.get(StaticStrings.DIR_ROOT.getValue() + File.separator + StaticStrings.DIR_MANGA.getValue() + File.separator + mangaId + File.separator + startingChapterNumber);
        Files.createDirectories(to);
    }

    private static void saveImage(Element pageUrl, int mangaId, int startingChapterNumber, int image)throws Exception{
        InputStream imageUrl = new URL(pageUrl.select("img").first().attr("abs:src")).openStream();
        Files.copy(imageUrl, Paths.get(StaticStrings.DIR_ROOT.getValue() + File.separator + StaticStrings.DIR_MANGA.getValue() + File.separator + mangaId + File.separator + startingChapterNumber + File.separator + String.format("%03d", image) + ".png"), StandardCopyOption.REPLACE_EXISTING);
    }

    private static void downloadComplete() {
        MangaValues.addAndRemove(CategoryMangaLists.downloading, CategoryMangaLists.collectedMangaList, 0, true);
        // delete from downloading
        // update mangareader pane regarding downloads
        // send popup that download done or whatthefuckever.
        ControllerMain.downloadMessage.set(mangaTitle + StaticStrings.DL_DONE.getValue());
    }

}