package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {

    public static final String QR_BASE_LOCATION = "./QR/";

    public static boolean setupQRLocation() {
        File file = new File(QR_BASE_LOCATION);
        if(file.exists()) {
            file.delete();
        }
        return file.mkdir();
    }

    public static File loadFile(String path) {
        try {
            return new File(path);
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static StringBuilder readFileAsStringBuilder(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str;
            while((str = reader.readLine()) != null) {
                stringBuilder.append(str);
            }
            reader.close();
        }
        catch (IOException ex) {
            System.out.println("File not Found, try again by adding a MailMessage.html file to the root directory");
        }
        return stringBuilder;
    }

    public static boolean deleteQRFile(String fileName) {
        if(!fileName.contains(".png")) {
            fileName += ".png";
        }
        File file = new File(QR_BASE_LOCATION + fileName);
        return file.delete();
    }



}
