package com.matthew_savage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

class InvalidEntry {

    private static String databaseMangaName;

    static String verifyAddress(String webAddress, String mangaName) {

        String mangaNameFormatted = mangaName.replaceAll("[^a-zA-Z0-9 ]", "").replace(" ", "_");
        databaseMangaName = mangaName;

        if (isAddressInvalid(webAddress)) {
            String newAddress = checkResultAddresses(getResultAddresses(getSearchResults(mangaNameFormatted)));
            ErrorLogging.logError(webAddress + " is invalid! Replacing with " + newAddress);
            return newAddress;
        }
        return webAddress;
    }

    public static void deleteInvalidEntry(int mangaIdentNum) {
        // this needs to be logged to a physical FILE!!
    }

    private static boolean isAddressInvalid(String webAddress) {

        try {
            Document mangaError = Jsoup.connect(webAddress).get();
            return mangaError.select(".login").text().equals("Sorry, the page you have requested cannot be found. Click here go visit our homepage");
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogging.logError(e.toString());
        }
        return false;
    }

    private static Document getSearchResults(String mangaName) {
        try {
            return Jsoup.connect(StaticStrings.URL_ROOT.getValue() + StaticStrings.URL_SEARCH.getValue() + mangaName).get();
        } catch (IOException e) {
            e.printStackTrace();
            ErrorLogging.logError(e.toString());
        }
        return null;
    }

    private static ArrayList<String> getResultAddresses(Document searchResults) {
        ArrayList<String> addressList = new ArrayList<>();

        for (Element addresses : searchResults.select(".story_name")) {
            addressList.add(addresses.select("h3 a").first().attr("abs:href"));
        }
        return addressList;
    }

    private static String checkResultAddresses(ArrayList<String> addressList) {

        for (String address : addressList) {
            try {
                if (getFunctionalAddress(Jsoup.connect(address).get())) {
                    return address;
                }
            } catch (IOException e) {
                e.printStackTrace();
                ErrorLogging.logError(e.toString());
            }
        }
        return null;
    }

    private static boolean getFunctionalAddress(Document document) {
        return document.select(".manga-info-text li h1").text().replace("'", "").equals(databaseMangaName);
    }
}
