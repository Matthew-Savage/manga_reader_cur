package com.matthew_savage.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfoBox {


    public void openInfoBox(int imageClicked) {

    }

    public static List<Boolean> displayCorrectInfoBox(String currentActivity) {

        switch (currentActivity) {
            case "My Library":
                return Stream.of(true, true, true, false, false, false, false, false).collect(Collectors.toList());
            case "Available Manga":
                return Stream.of(false, false, false, true, true, false, false, true).collect(Collectors.toList());
            case "Not Interested":
                return Stream.of(false, false, false, false, false, true, false, true).collect(Collectors.toList());
            case "Finished Reading":
                return Stream.of(true, true, false, false, false, false, true, true).collect(Collectors.toList());
        }
        return Stream.of(false, false, false, false, false, false, false, false).collect(Collectors.toList());
    }


}
