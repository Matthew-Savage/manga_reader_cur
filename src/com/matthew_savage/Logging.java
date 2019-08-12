package com.matthew_savage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Logging {

    public static void logError(String error) {
        try {
            File errorFile = new File(StaticStrings.DIR_ROOT.getValue() + File.separator + "err.log");
            FileWriter fileWriter = new FileWriter(errorFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (!errorFile.exists()) {
                errorFile.createNewFile();
            }
            bufferedWriter.write(System.currentTimeMillis() + " - " + error);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }

    public static void logDatabase(String process) {
        try {
            File dbFile = new File(StaticStrings.DIR_ROOT.getValue() + File.separator + "db.log");
            FileWriter fileWriter = new FileWriter(dbFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            }
            bufferedWriter.write(System.currentTimeMillis() + " - " + process);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }
}