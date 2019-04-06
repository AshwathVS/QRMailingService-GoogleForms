package com.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingUtils {

    private static String LOG_FILE_NAME = "./Logging.log";

    private static File file = FileUtils.loadFile(LOG_FILE_NAME);

    private static FileHandler fileHandler;

    private static boolean isSetup = false;

    private static Logger logger;

    public static boolean setupLogging() {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fileHandler = new FileHandler(LOG_FILE_NAME, true);
            logger = Logger.getLogger(LoggingUtils.class.toString());
            logger.addHandler(fileHandler);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            isSetup = true;
            return true;
        } catch (IOException ex) {
            System.out.println("Error while initializing file..");
            return false;
        }
    }


    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    public static void logError(Exception ex) {
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        for(StackTraceElement stackTraceElement : stackTraceElements) {
            log(Level.SEVERE, stackTraceElement.toString());
        }
    }
}
