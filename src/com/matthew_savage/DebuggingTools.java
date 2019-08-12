package com.matthew_savage;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringJoiner;

public class DebuggingTools {

    private static ArrayList<Manga> compiledManga = new ArrayList<>();
    private static ArrayList<Integer> missingIdentNumbers = new ArrayList<>();
    private static ArrayList<Integer> duplicateIdentNumbers = new ArrayList<>();

    public static void mangaCollectionHealth() {
        compiledManga = MangaConsolidated.list();
        compiledManga.sort(Comparator.comparingInt(Manga::getTitleId));
//        checkIfDuplicatesPresent();
//        checkIfIdentMissing();
        findPage();
//        if (duplicateIdentNumbers.size() == 0) {
//            outputResults(missingIdentNumbers, "Missing Ident Nums");
//        } else {
//            outputDuplicateResults();
//            System.out.println("Cannot proceed with checking for missing titles until all duplicates removed!");
//        }
    }

    private static void checkIfDuplicatesPresent() {
        for (int i = 1; i < compiledManga.size(); i++) {
            if (compiledManga.get(i).getTitleId() == compiledManga.get(i - 1).getTitleId()) {
                duplicateIdentNumbers.add(compiledManga.get(i).getTitleId());
            }
        }
    }

    private static void checkIfIdentMissing() {
        int indexNumber = 0;
        for (int i = 0; i < compiledManga.size(); i++) {
            if (compiledManga.get(indexNumber).getTitleId() != (i + 1)) {
                missingIdentNumbers.add(i + 1);
            } else {
                indexNumber++;
            }
        }
    }

    private static void outputResults(ArrayList<Integer> list, String listName) {
        if (list.size() > 0) {
            System.out.println("============= " + listName + " Start =============");
            for (Integer number : list) {
                System.out.println(number);
            }
            System.out.println("============= " + listName + " End =============");
        } else {
            System.out.println("No " + listName + "!");
        }
    }

    private static void outputDuplicateResults() {
        StringBuilder stringBuilder;
        ArrayList<String> duplicatedOuput = new ArrayList<>();
        for (Integer entry : duplicateIdentNumbers) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(entry);
            if (CategoryMangaLists.notCollectedMangaList.stream().anyMatch(v -> v.getTitleId() == entry)) {
                stringBuilder.append(" - notCollected");
            }
            if (CategoryMangaLists.collectedMangaList.stream().anyMatch(v -> v.getTitleId() == entry)) {
                stringBuilder.append(" - collected");
            }
            if (CategoryMangaLists.completedMangaList.stream().anyMatch(v -> v.getTitleId() == entry)) {
                stringBuilder.append(" - completed");
            }
            if (CategoryMangaLists.rejectedMangaList.stream().anyMatch(v -> v.getTitleId() == entry)) {
                stringBuilder.append(" - rejected");
            }
            if (CategoryMangaLists.undecidedMangaList.stream().anyMatch(v -> v.getTitleId() == entry)) {
                stringBuilder.append(" - undecided");
            }
            if (CategoryMangaLists.downloading.stream().anyMatch(v -> v.getTitleId() == entry)) {
                stringBuilder.append(" - downloading");
            }
            duplicatedOuput.add(stringBuilder.toString());
        }

        for (String string : duplicatedOuput) {
            System.out.println(string);
        }
    }

    private static void findPage() {
        System.out.println(RemoteCatalog.findPageNumber("ZINGNIZE"));
    }
}
