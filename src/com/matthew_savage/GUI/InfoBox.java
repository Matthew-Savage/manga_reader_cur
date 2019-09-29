package com.matthew_savage.GUI;

import com.matthew_savage.CategoryMangaLists;
import com.matthew_savage.StaticStrings;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfoBox {

    public static int positionInfoBox(int thumbClickedIdent) {
        if (thumbClickedIdent < 15) {
            return 1150;
        } else {
            return 35;
        }
    }

    public static int positionRepairBox(int thumbClickedIdent) {
        if (thumbClickedIdent < 15) {
            return 1330;
        } else {
            return 219;
        }
    }

    public static List<Boolean> displayCorrectInfoBox() {
        Boolean read = CategoryMangaLists.selectedMangaTotalChapNumTEMP == CategoryMangaLists.selectedMangaLastChapDownloadedTEMP;
        switch (CategoryMangaLists.currentCategoryNumber) {
            case 1:
                return Stream.of(true, false, true, true, false, false, false, false, true, true, true).collect(Collectors.toList());
            case 2:
                return Stream.of(false, true, true, true, false, false, false, true, false, false, false).collect(Collectors.toList());
            case 3:
                return Stream.of(false, true, true, true, false, false, false, true, false, false, false).collect(Collectors.toList());
            case 4:
                return Stream.of(!read, read, true, true, false, false, false, false, true, false, false).collect(Collectors.toList());
            case 5:
                return Stream.of(false, false, false, false, true, false, true, false, true, true, true).collect(Collectors.toList());
            case 6:
                return Stream.of(false, false, false, false, true, true, false, false, true, true, true).collect(Collectors.toList());
        }
        return null;
    }
}
