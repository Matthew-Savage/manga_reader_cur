package com.matthew_savage.GUI;

import com.matthew_savage.CategoryMangaLists;

public class ThumbPageNumbering {

    public static int setCurrentCatalogPageNumber(int indexIncrementValue) {
        if (CategoryMangaLists.currentContent.size() == 0) {
            return 0;
        } else {
            return (indexIncrementValue / 30) + 1;
        }
    }

    public static int setTotalCatalogPagesNumber(int currentCategoryListSize) {
        if ((currentCategoryListSize % 30) > 0) {
            return (currentCategoryListSize / 30) + 1;
        } else {
            return currentCategoryListSize / 30;
        }
    }

    public static int setCurrentCatalogPageNumberLayoutX(int currentPageNumber) {
        if (currentPageNumber < 10) {
            return 1669;
        } else if (currentPageNumber < 100) {
            return 1655;
        } else {
            return 1642;
        }
    }
}
