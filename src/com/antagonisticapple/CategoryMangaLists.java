package com.antagonisticapple;

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
    public static List<Predicate<MangaArrayList>> predicateList = new ArrayList<>();
    public static ArrayList<MangaArrayList> historyInverted = new ArrayList<>();
    public static ArrayList<MangaArrayList> currentParentList = new ArrayList<>();
    public static ArrayList<File> mangaPageImageFiles = new ArrayList<>();
    public static int parentListIndexNumber;
    public static int currentContentListIndexNumber;
    public static int selectedMangaIdentNumber;
    public static int selectedMangaLastChapReadNum;
    public static int selectedMangaTotalChapNum;
    public static int selectedMangaCurrentPageNum;
//    public static int selectedMangaCurrentChapFinalPage;
}