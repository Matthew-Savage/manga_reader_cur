package com.matthew_savage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryMangaLists {

    public static ArrayList<Manga> notCollectedMangaList = new ArrayList<>();
    public static ArrayList<Manga> rejectedMangaList = new ArrayList<>();
    public static ArrayList<Manga> completedMangaList = new ArrayList<>();
    public static ArrayList<Manga> collectedMangaList = new ArrayList<>();
    public static ArrayList<Manga> unsureMangaList = new ArrayList<>();
    public static ArrayList<Manga> history = new ArrayList<>();
    public static ArrayList<Manga> bookmark = new ArrayList<>();
    public static ArrayList<Manga> downloading = new ArrayList<>();
    public static ArrayList<StatsArrayList> stats = new ArrayList<>();
    public static ArrayList<Manga> currentContent = new ArrayList<>();
    public static ArrayList<Manga> fiveNewestTitles = new ArrayList<>();
    public static List<Predicate<Manga>> predicateList = new ArrayList<>();
    public static ArrayList<Manga> currentParentList = new ArrayList<>();
    public static ArrayList<File> mangaPageImageFiles = new ArrayList<>();

    public static int parentListIndexNumberTEMP;
    public static int currentContentListIndexNumberTEMP;

    public static int selectedMangaIdentNumberTEMP;
    public static String selectedMangaTitleTEMP;
    public static String selectedMangaAuthorsTEMP;
    public static String selectedMangaStatusTEMP;
    public static String selectedMangaSummaryTEMP;
    public static String selectedMangaWebAddressTEMP;
    public static String selectedMangaGenresTEMP;
    public static int selectedMangaTotalChapNumTEMP;
    public static int selectedMangaCurrentPageNumTEMP;
    public static int selectedMangaLastChapReadNumTEMP;
    public static int selectedMangaLastChapDownloadedTEMP;
    public static int selectedMangaNewChapNumTEMP;
    public static int selectedMangaIsFavoriteTEMP;

    public static int selectedMangaCurrentChapLastPageNumTEMP;


    public static ArrayList<Manga> toList(String currentActivity) {
        if (currentActivity.equals(StaticStrings.CAT_NOT_COLLECTED.getValue())) {
            return notCollectedMangaList;
        } else if (currentActivity.equals(StaticStrings.CAT_COLLECTED.getValue())) {
            return collectedMangaList;
        } else if (currentActivity.equals(StaticStrings.CAT_COMPLETED.getValue())) {
            return completedMangaList;
        } else if (currentActivity.equals(StaticStrings.CAT_UNSURE.getValue())) {
            return unsureMangaList;
        } else {
            return rejectedMangaList;
        }
    }
}