package com.matthew_savage.GUI;

import com.matthew_savage.Values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfoBox {

    public void openInfoBox(int imageClicked) {

    }

    public static int positionInfoBox(int thumbClickedIdent) {
        if (thumbClickedIdent < 15) {
            return 1144;
        }  else {
            return 15;
        }
    }

    public static int positionRepairBox(int thumbClickedIdent) {
        if (thumbClickedIdent < 15) {
            return 1329;
        } else {
            return 200;
        }
    }

    public static List<Boolean> displayCorrectInfoBox(String currentActivity) {
        if (currentActivity.equals(Values.CAT_NOT_COLLECTED.getValue())) {
            return Stream.of(false, false, false, true, true, false, false, true).collect(Collectors.toList());
        } else if (currentActivity.equals(Values.CAT_COLLECTED.getValue())) {
            return Stream.of(true, true, true, false, false, false, false, false).collect(Collectors.toList());
        } else if (currentActivity.equals(Values.CAT_COMPLETED.getValue())) {
            return Stream.of(true, true, false, false, false, false, true, true).collect(Collectors.toList());
        } else {
            return Stream.of(false, false, false, false, false, true, false, true).collect(Collectors.toList());
        }
    }
}
