package com.matthew_savage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CategoryMangaLists {

    public static ArrayList<MangaArrayList> notCollectedMangaList = new ArrayList<>();
    public static ArrayList<MangaArrayList> rejectedMangaList = new ArrayList<>();
    public static ArrayList<MangaArrayList> completedMangaList = new ArrayList<>();
    public static ArrayList<MangaArrayList> collectedMangaList = new ArrayList<>();
    public static ArrayList<MangaArrayList> history = new ArrayList<>();
    public static ArrayList<MangaArrayList> bookmark = new ArrayList<>();
    public static ArrayList<MangaArrayList> downloading = new ArrayList<>();
    public static ArrayList<StatsArrayList> stats = new ArrayList<>();
    public static ArrayList<MangaArrayList> currentContent = new ArrayList<>();
    public static ArrayList<MangaArrayList> fiveNewestTitles = new ArrayList<>();
    public static List<Predicate<MangaArrayList>> predicateList = new ArrayList<>();
    public static ArrayList<MangaArrayList> currentParentList = new ArrayList<>();
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
    public static boolean selectedMangaIsFavoriteTEMP;

    public static int selectedMangaCurrentChapLastPageNumTEMP;


}