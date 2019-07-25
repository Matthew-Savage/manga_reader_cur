package com.matthew_savage.GUI;

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

    public static List<Boolean> displayCorrectInfoBox(String currentActivity) {

        if (currentActivity.equals(StaticStrings.CAT_NOT_COLLECTED.getValue())) {
            return Stream.of(false, false, true, true, false, false, true, false, true).collect(Collectors.toList());
        } else if (currentActivity.equals(StaticStrings.CAT_COLLECTED.getValue())) {
            return Stream.of(true, true, false, false, false, false, false, true, false).collect(Collectors.toList());
        } else if (currentActivity.equals(StaticStrings.CAT_COMPLETED.getValue())) {
            return Stream.of(true, false, false, false, false, true, true, true, false).collect(Collectors.toList());
        } else {
            return Stream.of(false, false, false, false, true, false, true, false, true).collect(Collectors.toList());
        }
    }
}
