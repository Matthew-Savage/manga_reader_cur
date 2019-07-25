package com.matthew_savage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ErrorLogging {

    public static void logError(String error) {
        try {
            File errorFile = new File(StaticStrings.DIR_ROOT.getValue() + File.separator + "log.txt");
            FileWriter fileWriter = new FileWriter(errorFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (!errorFile.exists()) {
                errorFile.createNewFile();
            }
            bufferedWriter.write(error);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogging.logError(e.toString());
        }

    }
}