package com.matthew_savage;

import java.net.HttpURLConnection;
import java.net.URL;

public class InternetConnection {

    public static boolean checkIfConnected() {

        try {
            URL url = new URL("https://manganelo.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Object object = connection.getContent();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
