package com.matthew_savage;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class DownloadRunnable {

    private static ArrayList<Manga> downloadQueue = new ArrayList<>();
    private static ArrayList<String> mangaChapters = new ArrayList<>();

    private static int currentMangaIdent;
    private static String currentMangaTitle;
    private static String currentMangaWebAddress;
    private static String currentMangaAuthors;
    private static int currentMangaLastChapDownloaded;
    private static int totalPendingDownloads;

    public static Runnable processDownloadQueue = DownloadRunnable::checkStatus;

    private static void checkStatus() {
        if (CategoryMangaLists.downloading.size() > 0 && ControllerMain.firstUpdateRun) {
            downloadQueue.clear();
            downloadQueue.addAll(CategoryMangaLists.downloading);
            CategoryMangaLists.downloading.clear();
        }
        if (downloadQueue.size() > 0) {
            totalPendingDownloads = downloadQueue.size();
            beginProcessingProcess();
        }
    }

    private static void beginProcessingProcess() {
        for (int i = 0; i < downloadQueue.size(); i++) {
            ControllerMain.downloadMessage.set(String.valueOf(totalPendingDownloads));
            currentMangaIdent = downloadQueue.get(i).getTitleId();
            currentMangaTitle = downloadQueue.get(i).getTitle();
            currentMangaAuthors = downloadQueue.get(i).getAuthors();
            currentMangaLastChapDownloaded = downloadQueue.get(i).getLastChapterDownloaded();
            currentMangaWebAddress = checkMangaAddress(downloadQueue.get(i).getWebAddress());
            if (currentMangaWebAddress == null) {
                continue;
            }
            populateChapterList();
            createLocalFolders();
            writeNewTotalChapValue(i);
            initiateChapterDownload(i);
        }
    }

    private static String checkMangaAddress(String webAddress) {
        String address = InvalidEntry.verifyAddress(webAddress, currentMangaTitle, currentMangaAuthors);
        if (!webAddress.equals(address)) {
            Logging.logError("DownloadRunnable - " + webAddress + " is invalid! Replacing with " + address);
            modifyDownloadDatabase(StaticStrings.DB_COL_URL.getValue(), address, currentMangaIdent);
        }
        return address;
    }

    private static void populateChapterList() {
        mangaChapters.clear();
        mangaChapters.addAll(IndexMangaChapters.getChapterAddresses(currentMangaWebAddress));
        Collections.reverse(mangaChapters);
    }

    private static void createLocalFolders() {
        try {
            for (int i = 0; i < mangaChapters.size(); i++) {
                Path to = Paths.get(StaticStrings.DIR_ROOT.getValue() + File.separator + StaticStrings.DIR_MANGA.getValue() + File.separator + currentMangaIdent + File.separator + i);
                if (Files.notExists(to)) {
                    Files.createDirectories(to);
                    Files.copy(Paths.get(StaticStrings.DIR_ROOT.getValue() + File.separator + "placeholder.png"), Paths.get(to + File.separator + "000.png"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }

    private static void writeNewTotalChapValue(int indexNumber) {
        downloadQueue.get(indexNumber).setTotalChapters(mangaChapters.size());
        modifyDownloadDatabase(StaticStrings.DB_COL_CHAP_TOT.getValue(), mangaChapters.size(), currentMangaIdent);
    }

    private static void initiateChapterDownload(int indexNumber) {
        if (chapPageCantBeScraped()) {
            ControllerMain.killDownloadProcess();
            ControllerMain.downloadThread.shutdown();
            ControllerMain.errorMessage.set(StaticStrings.ERR_HTML.getValue());
        } else {
            downloadChapters(indexNumber);
        }
    }

    private static boolean chapPageCantBeScraped() {
        try {
            Document chapterOne = Jsoup.connect(mangaChapters.get(0)).get();
            return chapterOne.select(".vung-doc img").first() == null;
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
        return true;
    }


    private static void downloadChapters(int indexNumber) {
        int startingChapter = currentMangaLastChapDownloaded;
        for (int i = currentMangaLastChapDownloaded; i < mangaChapters.size(); i++) {
            downloadChapterPages(i);
            currentMangaLastChapDownloaded++;
            writeNewLastChapDownValue(currentMangaLastChapDownloaded);
            insertIfReady(indexNumber, currentMangaLastChapDownloaded, startingChapter);
        }
    }

    private static void writeNewLastChapDownValue(int lastChapNum) {
        modifyDownloadDatabase(StaticStrings.DB_COL_LAST_CHAP_DL.getValue(), lastChapNum, currentMangaIdent);
    }

    private static void downloadChapterPages(int chapterNumber) {
        int imageNumber = 0;
        for (Element image : fetchChapterPages(chapterNumber).select(".vung-doc img")) {
            saveImage(image, chapterNumber, imageNumber);
            imageNumber++;
        }
    }

    private static Document fetchChapterPages(int indexNumber) {
        try {
            Connection.Response response = Jsoup.connect("https://manganelo.com/change_content_s" +
                    fetchVerificationUrl(mangaChapters.get(indexNumber))).method(Connection.Method.GET).execute();
            return Jsoup.connect(mangaChapters.get(indexNumber)).cookies(response.cookies()).get();
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
        return null;
    }

    private static int fetchVerificationUrl(String chapterUrl) throws Exception {
        Document imageUrl = Jsoup.connect(chapterUrl).get();
        return verifyImageServer(imageUrl.select(".vung-doc img").first().attr("abs:src"));
    }

    private static int verifyImageServer(String imageUrl) throws Exception {
        HttpURLConnection testConnection = (HttpURLConnection) (new URL(imageUrl).openConnection());
        testConnection.setRequestMethod("HEAD");
        testConnection.connect();

        if (testConnection.getResponseCode() == 200) {
            return 1;
        } else {
            return 2;
        }
    }

    private static void saveImage(Element pageUrl, int startingChapterNumber, int imageNumber) {
        try {
            InputStream imageUrl = new URL(pageUrl.select("img").first().attr("abs:src")).openStream();
            Files.copy(imageUrl, Paths.get(StaticStrings.DIR_ROOT.getValue() + File.separator + StaticStrings.DIR_MANGA.getValue() + File.separator + currentMangaIdent + File.separator + startingChapterNumber + File.separator + String.format("%03d", imageNumber) + ".png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }

    private static void insertIfReady(int indexNumber, int lastChapDown, int originalChapDown) {
        if (lastChapDown == (originalChapDown + 1)  && CategoryMangaLists.collectedMangaList.stream().noneMatch(v -> v.getTitleId() == downloadQueue.get(indexNumber).getTitleId())) {
            CategoryMangaLists.collectedMangaList.add(downloadQueue.get(indexNumber));
            MangaValues.addToQueue("INSERT INTO " + StaticStrings.DB_TABLE_READING.getValue() + " (" +
                    "title_id, " +
                    "title, " +
                    "authors, " +
                    "status, " +
                    "summary, " +
                    "web_address, " +
                    "genre_tags, " +
                    "total_chapters, " +
                    "current_page, " +
                    "last_chapter_read, " +
                    "last_chapter_downloaded, " +
                    "new_chapters, " +
                    "favorite) VALUES " + "(" +
                    "'" + downloadQueue.get(indexNumber).getTitleId() + "', " +
                    "'" + downloadQueue.get(indexNumber).getTitle() + "', " +
                    "'" + downloadQueue.get(indexNumber).getAuthors() + "', " +
                    "'" + downloadQueue.get(indexNumber).getStatus() + "', " +
                    "'" + downloadQueue.get(indexNumber).getSummary() + "', " +
                    "'" + downloadQueue.get(indexNumber).getWebAddress() + "', " +
                    "'" + downloadQueue.get(indexNumber).getGenreTags() + "', " +
                    "'" + downloadQueue.get(indexNumber).getTotalChapters() + "', " +
                    "'" + downloadQueue.get(indexNumber).getCurrentPage() + "', " +
                    "'" + downloadQueue.get(indexNumber).getLastChapterRead() + "', " +
                    "'" + downloadQueue.get(indexNumber).getLastChapterDownloaded() + "', " +
                    "'" + downloadQueue.get(indexNumber).getNewChapters() + "', " +
                    "'" + downloadQueue.get(indexNumber).getFavorite() + "')");
            MangaValues.executeChanges();
        }
        if (lastChapDown == mangaChapters.size()) {
            CategoryMangaLists.collectedMangaList.get(ControllerMain.fetchOriginalIndexNumber(CategoryMangaLists.collectedMangaList, currentMangaIdent)).setLastChapterDownloaded(lastChapDown);
            MangaValues.addToQueue("UPDATE " + StaticStrings.DB_TABLE_READING.getValue() + " SET " + StaticStrings.DB_COL_LAST_CHAP_DL.getValue() + " = '" +
                    lastChapDown + "' WHERE title_id = '" + currentMangaIdent + "'");
            MangaValues.executeChanges();
            totalPendingDownloads--;
            ControllerMain.downloadMessage.set(String.valueOf(totalPendingDownloads));
            downloadDatabaseDelete(currentMangaIdent);
        }
    }

    public static void addToDatabase(int titleIdent, String title, String authors, String status, String summary, String webAddress, String genres, int chapTotal, int curPage, int lastChapRead, int lastChapDown, int chapsNew, int fav) {
        Voucher.acquire();
        Database.accessDb(StaticStrings.DB_NAME_DOWNLOADING.getValue());
        String insert = "INSERT INTO " + StaticStrings.DB_TABLE_DOWNLOAD.getValue() + " (" +
                "title_id, " +
                "title, " +
                "authors, " +
                "status, " +
                "summary, " +
                "web_address, " +
                "genre_tags, " +
                "total_chapters, " +
                "current_page, " +
                "last_chapter_read, " +
                "last_chapter_downloaded, " +
                "new_chapters, " +
                "favorite) VALUES " + "(" +
                "'" + titleIdent + "', " +
                "'" + title + "', " +
                "'" + authors + "', " +
                "'" + status + "', " +
                "'" + summary + "', " +
                "'" + webAddress + "', " +
                "'" + genres + "', " +
                "'" + chapTotal + "', " +
                "'" + curPage + "', " +
                "'" + lastChapRead + "', " +
                "'" + lastChapDown + "', " +
                "'" + chapsNew + "', " +
                "'" + fav + "')";
        try (Statement sqlStatement = Database.dbConnection.createStatement()) {
            sqlStatement.execute(insert);
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
            Logging.logError(insert);
        }
        Database.terminateDbAccess();
        Voucher.release();
    }

    private static <T> void modifyDownloadDatabase(String valueColumnName, T newValue, int mangaIdentNumber) {
        Voucher.acquire();
        Database.accessDb(StaticStrings.DB_NAME_DOWNLOADING.getValue());
        String insert = "UPDATE " + StaticStrings.DB_TABLE_DOWNLOAD.getValue() + " SET " + valueColumnName + " = '" + newValue + "' WHERE title_id = '" + mangaIdentNumber + "'";
        try (Statement sqlStatement = Database.dbConnection.createStatement()) {
            sqlStatement.execute(insert);
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.logError("downloadRunnable - " + e.toString());
            Logging.logError(insert);
        }
        Database.terminateDbAccess();
        Voucher.release();
    }

    private static void downloadDatabaseDelete(int mangaIdentNumber) {
        Voucher.acquire();
        Database.accessDb(StaticStrings.DB_NAME_DOWNLOADING.getValue());
        String insert = "DELETE FROM " + StaticStrings.DB_TABLE_DOWNLOAD.getValue() + " WHERE title_id = '" + mangaIdentNumber + "'";
        try (Statement sqlStatement = Database.dbConnection.createStatement()) {
            sqlStatement.execute(insert);
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError("downloadRunnable - " + e.toString());
            Logging.logError(insert);
        }
        Database.terminateDbAccess();
        Voucher.release();
    }

}
