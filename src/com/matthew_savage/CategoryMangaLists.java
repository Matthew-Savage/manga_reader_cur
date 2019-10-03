package com.matthew_savage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CategoryMangaLists {

    public static ArrayList<Manga> notCollectedMangaList = new ArrayList<>();
    public static ArrayList<Manga> rejectedMangaList = new ArrayList<>();
    public static ArrayList<Manga> completedMangaList = new ArrayList<>();
    public static ArrayList<Manga> collectedMangaList = new ArrayList<>();
    public static ArrayList<Manga> favoritesMangaList = new ArrayList<>();
    public static ArrayList<Manga> undecidedMangaList = new ArrayList<>();
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
    public static int historyListIndexNumber;
//    public static int currentContentListIndexNumberTEMP;

    public static Double currentVersionNumber;

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
    public static int currentCategoryNumber;

    public static void buildFavoritesArray() {
        favoritesMangaList.clear();
        ArrayList<Manga> collectedAndCompleted = new ArrayList<>();
        collectedAndCompleted.addAll(collectedMangaList);
        collectedAndCompleted.addAll(completedMangaList);
        favoritesMangaList = collectedAndCompleted.stream().filter(i -> i.getFavorite() == 1).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Manga> toList() {
        switch (currentCategoryNumber) {
            case 1:
                return notCollectedMangaList;
            case 2:
                return collectedMangaList;
            case 3:
                return completedMangaList;
            case 4:
                return favoritesMangaList;
            case 5:
                return undecidedMangaList;
            case 6:
                return rejectedMangaList;
        }
        return null;
    }
}