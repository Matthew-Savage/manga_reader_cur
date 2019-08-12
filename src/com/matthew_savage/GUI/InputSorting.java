package com.matthew_savage.GUI;

import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.scene.input.ScrollEvent.SCROLL;

public class InputSorting {

    public static int sortInputs(Event event) {
        if (userScrollingUp(event)) {
            return 0;
        } else if (userScrollingDown(event)) {
            return 1;
        } else if (userNextPreviousPage(event)) {
            MangaPageTurning.turnPagePreviousNext((KeyEvent)event);
            return 2;
        } else if (userHitEscape(event)) {
            return 3;
        } else {
            return 4;
        }
    }

    public static boolean userScrollingUp(Event event) {
        if (event.getEventType().equals(KEY_PRESSED)) {
            KeyEvent keyEvent = (KeyEvent) event;
            return keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W;
        } else if (event.getEventType().equals(SCROLL)) {
            ScrollEvent scrollEvent = (ScrollEvent) event;
            return scrollEvent.getDeltaY() == 40 || scrollEvent.getDeltaY() != 40 && scrollEvent.getDeltaY() > 0.0;
        } else {
            return false;
        }
    }

    public static boolean userScrollingDown(Event event) {
        if (event.getEventType().equals(KEY_PRESSED)) {
            KeyEvent keyEvent = (KeyEvent) event;
            return keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S;
        } else if (event.getEventType().equals(SCROLL)) {
            ScrollEvent scrollEvent = (ScrollEvent) event;
            return scrollEvent.getDeltaY() == -40 || scrollEvent.getDeltaY() != -40 && scrollEvent.getDeltaY() < 0.0;
        } else {
            return false;
        }
    }

    private static boolean userNextPreviousPage(Event event) {
        KeyEvent keyEvent = (KeyEvent) event;
        return keyEvent.getCode().equals(KeyCode.RIGHT) || keyEvent.getCode().equals(KeyCode.LEFT)
                || keyEvent.getCode().equals(KeyCode.A) || keyEvent.getCode().equals(KeyCode.D);
    }

    private static boolean userHitEscape(Event event) {
        KeyEvent keyEvent = (KeyEvent) event;
        return keyEvent.getCode().equals(KeyCode.ESCAPE);
    }
}
