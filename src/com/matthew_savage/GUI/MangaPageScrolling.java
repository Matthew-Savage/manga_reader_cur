package com.matthew_savage.GUI;

import com.matthew_savage.ControllerMain;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

public class MangaPageScrolling {

//    public static void scrollPageUpDown(KeyEvent event) {
//        System.out.println(event.getCode());
//        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
//        }
//        if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
////            ControllerMain.scrollMangaPageDown();
//        }
//    }
//
//    public static void simulateMouseWheelScroll(ScrollEvent scrollMouseWheel) {
//        if (scrollMouseWheel.getDeltaY() == 40) {
//            scrollMouseWheel.consume();
//            ControllerMain.scrollMangaPageUp();
//        }
//        if (scrollMouseWheel.getDeltaY() == -40) {
//            scrollMouseWheel.consume();
////            ControllerMain.scrollMangaPageDown();
//        }
//    }

    public static int adjustScrollSpeed(ActionEvent event, int currentScrollSpeed) {
        Button button = (Button) event.getSource();

        if (button.getId().equals("sidebarPane_scrollSpeed_decrease") && currentScrollSpeed > 1) {
            return currentScrollSpeed - 1;
        }
        if (button.getId().equals("sidebarPane_scrollSpeed_increase") && currentScrollSpeed < 10) {
            return currentScrollSpeed + 1;
        }
        return currentScrollSpeed;
    }
}
