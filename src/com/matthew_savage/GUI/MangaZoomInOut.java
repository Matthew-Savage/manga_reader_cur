package com.matthew_savage.GUI;

import javafx.scene.control.Button;

public class MangaZoomInOut {

    public static double widthValue(Button button, double width) {

        if (button.getId().equals("sidebarPane_size_decrease")) {
            switch (String.valueOf(width)) {
                case "1920.0":
                    width = 1720;
                    break;
                case "1720.0":
                    width = 1520;
                    break;
                case "1520.0":
                    width = 1320;
                    break;
                case "1320.0":
                    width = 1120;
                    break;
                case "1120.0":
                    width = 920;
                    break;
            }
        } else if (button.getId().equals("sidebarPane_size_increase")) {
            switch (String.valueOf(width)) {
                case "920.0":
                    width = 1120;
                    break;
                case "1120.0":
                    width = 1320;
                    break;
                case "1320.0":
                    width = 1520;
                    break;
                case "1520.0":
                    width = 1720;
                    break;
                case "1720.0":
                    width = 1920;
                    break;
            }
        }
        return width;
    }
}
