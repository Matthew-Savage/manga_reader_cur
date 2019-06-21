package com.antagonisticapple.GUI;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToggleNavButtons {

    public static List<Boolean> navButtonVisibility(int currentPageNumber, int totalPagesNumber) {

        if (totalPagesNumber == 1) {
            return Stream.of(false, false, false, false, false).collect(Collectors.toList());
        } else{
            return multiPageToggle(currentPageNumber, totalPagesNumber);
        }
    }

    private static List<Boolean> multiPageToggle(int currentPageNumber, int totalPagesNumber) {

        if (currentPageNumber == 1) {
            return Stream.of(false, false, true, true, true).collect(Collectors.toList());
        } else if (currentPageNumber > 1 && currentPageNumber < totalPagesNumber) {
            return Stream.of(true, true, true, true, true).collect(Collectors.toList());
        } else {
            return Stream.of(true, true, true, false, false).collect(Collectors.toList());
        }
    }
}
