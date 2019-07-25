package com.matthew_savage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Objects;

class IndexMangaChapters {

    static ArrayList<String> getChapterAddresses(String webAddress) {
        ArrayList<String> linksList = new ArrayList<>();
        Document mangaChapters = null;
        try {
            mangaChapters = Jsoup.connect(webAddress).get();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogging.logError(e.toString());
        }

        for (Element list : Objects.requireNonNull(mangaChapters).select(".chapter-list .row")) {
            linksList.add(list.select("span a").first().attr("abs:href"));
        }
        return linksList;
    }
}