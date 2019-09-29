package com.matthew_savage;

import java.util.ArrayList;

import static com.matthew_savage.CategoryMangaLists.*;

public class MangaConsolidated {

    public static ArrayList<Manga> list() {
        ArrayList<Manga> mangaConsolidation = new ArrayList<>();
        mangaConsolidation.addAll(notCollectedMangaList);
        mangaConsolidation.addAll(collectedMangaList);
        mangaConsolidation.addAll(completedMangaList);
        mangaConsolidation.addAll(rejectedMangaList);
        mangaConsolidation.addAll(undecidedMangaList);
        mangaConsolidation.addAll(downloading);
        return mangaConsolidation;
    }

}
